package com.sean.im.client.core;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.sean.im.commom.core.BytesUtil;
import com.sean.im.commom.core.Protocol;
import com.sean.im.commom.core.ProtocolUtil;
import com.sean.log.core.LogFactory;

/**
 * 客户端
 * @author Sean
 */
public class Client
{
	private SocketChannel socketChannel;
	private Reader reader;
	private HeartBit heartBiter;
	private ByteBuffer readBuf;
	private ClientListener listener;
	private boolean isClosed = true;

	private static final Logger logger = LogFactory.getFrameworkLogger();

	/**
	 * 构造方法
	 * @param buffersize		缓冲区大小
	 */
	public Client(int buffersize)
	{
		readBuf = ByteBuffer.allocate(buffersize);
	}

	public void addListener(ClientListener listener)
	{
		this.listener = listener;
	}

	/**
	 * 连接服务器
	 */
	public void open(long userId)
	{
		try
		{
			socketChannel = SocketChannel.open(new InetSocketAddress(ApplicationContext.PushHost, ApplicationContext.PushPort));
			socketChannel.configureBlocking(true);

			ByteBuffer buf = ByteBuffer.allocate(20);
			buf.clear();
			buf.put(BytesUtil.toBytes(userId));
			buf.flip();
			socketChannel.write(buf);

			reader = new Reader();
			new Thread(reader).start();

			logger.debug("Client regist push server");
			if (listener != null)
			{
				listener.connect();
			}
			isClosed = false;

			if (heartBiter == null)
			{
				heartBiter = new HeartBit();
				Thread t = new Thread(heartBiter);
				t.setPriority(Thread.MIN_PRIORITY);
				t.start();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			if (!isClosed)
			{
				try
				{
					Thread.sleep(10 * 1000);
					open(userId);
				}
				catch (InterruptedException e1)
				{
					e1.printStackTrace();
				}
			}
		}
	}

	public void close() throws Exception
	{
		this.socketChannel.close();
		isClosed = true;
	}

	/**
	 * 推送消息处理
	 * @author sean
	 */
	private class Reader implements Runnable
	{
		@Override
		public void run()
		{
			Protocol protocol = null;
			while (true)
			{
				try
				{
					readBuf.clear();
					socketChannel.read(readBuf);
					readBuf.flip();
					byte[] b = new byte[readBuf.limit()];
					readBuf.get(b, 0, readBuf.limit());
					protocol = ProtocolUtil.fromBytes(b);
					// 如果是服务端主动发的消息，触发事件
					if (protocol.type == 2)
					{
						SwingUtilities.invokeLater(new DoMessageThread(protocol));
						logger.debug("Server push : " + protocol);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
					logger.error(e.getMessage(), e);
					if (listener != null)
					{
						listener.disconnect();
					}

					if (!isClosed)
					{
						try
						{
							Thread.sleep(10 * 1000);
							open(ApplicationContext.User.getId());
						}
						catch (InterruptedException e1)
						{
							e1.printStackTrace();
						}
					}
					else
					{
						break;
					}
				}
			}
		}
	}

	/**
	 * 心跳线程
	 * @author sean
	 */
	private class HeartBit implements Runnable
	{
		@Override
		public void run()
		{
			ByteBuffer buf = ByteBuffer.allocate(20);
			byte bit = 0;
			while (true)
			{
				try
				{
					buf.clear();
					buf.put(bit);
					buf.flip();
					socketChannel.write(buf);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					try
					{
						Thread.sleep(3000);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}

	private class DoMessageThread implements Runnable
	{
		private Protocol protocol;

		public DoMessageThread(Protocol protocol)
		{
			this.protocol = protocol;
		}

		@Override
		public void run()
		{
			ApplicationContext.CTX.doPush(protocol);
		}
	}

	public interface ClientListener
	{
		public void disconnect();

		public void connect();
	}

}
