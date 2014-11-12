package com.sean.im.server.push;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.sean.commom.util.BytesUtil;
import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.core.Protocol;
import com.sean.im.commom.core.ProtocolUtil;
import com.sean.im.context.spi.FlockSpi;
import com.sean.im.context.spi.FriendSpi;
import com.sean.im.context.spi.UserSpi;
import com.sean.log.core.LogFactory;
import com.sean.service.core.ApplicationContext;

/**
 * IM推送服务器
 * @author sean
 */
public class IMServer
{
	public static IMServer CTX;

	private Map<Long, IoSession> clients;
	private long userId = 0;

	private static final String USERID = "userId";
	private static final Logger logger = LogFactory.getFrameworkLogger();
	public static final String version = "1.0";

	public IMServer()
	{
		clients = new HashMap<Long, IoSession>(10000);
	}

	/**
	 * 启动服务器
	 * @param port
	 */
	public void start(int port)
	{
		try
		{
			NioSocketAcceptor acceptor = new NioSocketAcceptor();
			// 读取二进制协议
			acceptor.getFilterChain().addLast("protocol", new ProtocolCodecFilter(new ProtocolParser()));

			// 设置Session参数
			IoSessionConfig config = acceptor.getSessionConfig();
			// 缓冲1M
			config.setReadBufferSize(1024 * 1024);
			// 10秒没有发生读写就进入空闲
			config.setIdleTime(IdleStatus.BOTH_IDLE, 10);

			// Bind
			acceptor.setHandler(new IMHandler());
			acceptor.bind(new InetSocketAddress(port));

			logger.info("Server started ,listen on " + port);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 用户是否在线
	 * @param userId
	 * @return
	 */
	public boolean isOnline(long userId)
	{
		return this.clients.get(userId) != null;
	}

	/**
	 * 推送
	 * @param protocol
	 */
	public void push(Protocol notify)
	{
		// 标记为推送模式
		notify.type = 2;
		IoSession is = this.clients.get(notify.receiver);
		if (is != null)
		{
			is.write(ProtocolUtil.toBytes(notify));
			logger.debug("Server push : " + notify);
		}
	}

	public void pushAndClose(Protocol notify)
	{
		// 标记为推送模式
		notify.type = 2;
		IoSession is = this.clients.remove(notify.receiver);
		if (is != null)
		{
			is.write(ProtocolUtil.toBytes(notify));
			is.removeAttribute(USERID);
			is.close(true);
			logger.debug("Server push : " + notify);
		}
	}

	/**
	 * 通知所有好友
	 * @param user
	 * @param notify
	 */
	public void pushToAllFriend(long user, Protocol notify)
	{
		FriendSpi fs = ApplicationContext.CTX.getBean(FriendSpi.class);
		List<Long> friends = fs.getFriendIdOfUser(user);
		for (long f : friends)
		{
			notify.receiver = f;
			IMServer.CTX.push(notify);
		}
	}

	/**
	 * 通知群成员
	 * @param user
	 * @param flockId
	 * @param notify
	 */
	public void pushToFlockMembers(long user, long flockId, Protocol notify)
	{
		FlockSpi fs = ApplicationContext.CTX.getBean(FlockSpi.class);
		List<Long> members = fs.getFlockMembers(flockId);
		for (long m : members)
		{
			if (m != user)
			{
				notify.receiver = m;
				IMServer.CTX.push(notify);
			}
		}
	}

	/**
	 * 获取在线用户数
	 * @return
	 */
	public int getClientCount()
	{
		return this.clients.size();
	}

	/**
	 * 删除客户端
	 * @param clientId
	 * @param needPush
	 */
	public void removeClient(long clientId)
	{
		IoSession session = this.clients.remove(clientId);
		if (session != null)
		{
			session.close(true);
		}
	}

	/**
	 * 事件捕捉
	 * @author sean
	 */
	private class IMHandler extends IoHandlerAdapter
	{
		@Override
		public void sessionCreated(IoSession session) throws Exception
		{
			synchronized (this)
			{
				userId--;
			}
			session.setAttribute(USERID, userId);
			clients.put(userId, session);
			logger.debug("Server create socket for user " + userId);
		}

		@Override
		public void messageReceived(IoSession session, Object message)
		{
			byte[] data = (byte[]) message;
			// 心跳包
			if (data.length == 1)
			{
				logger.debug("服务端收到心跳包");
			}
			else
			{
				long userId = Long.parseLong(session.getAttribute(USERID).toString());
				long newId = BytesUtil.toLong(data);

				IoSession is = clients.remove(userId);
				if (is != null)
				{
					is.setAttribute(USERID, newId);
					clients.put(newId, is);

					logger.debug("user " + newId + " regist push server");
				}
			}
		}

		@Override
		public void sessionClosed(IoSession session) throws Exception
		{
			long clientId = (long) session.getAttribute(USERID, Long.MAX_VALUE * -1);
			if (clients.remove(clientId) != null)
			{
				// 修改状态
				UserSpi us = ApplicationContext.CTX.getBean(UserSpi.class);
				us.initUserStatus(clientId);

				// 通知好友
				Protocol notify = new Protocol(Actions.StatusChangedHandler);
				notify.setParameter("status", 0);
				notify.setParameter("userId", clientId);
				pushToAllFriend(clientId, notify);
				logger.info("Client " + clientId + " was closed");
			}
		}

		@Override
		public void exceptionCaught(IoSession session, Throwable cause)
		{
			cause.printStackTrace();
			logger.error(cause);

			session.close(true);
			long clientId = Long.parseLong(session.getAttribute(USERID).toString());
			if (clients.remove(clientId) != null)
			{
				logger.info("Server catch an exception:" + cause + ", close and remove client " + clientId);
			}
		}
	}
}
