package com.sean.im.server.push;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * 二进制协议解析
 * @author sean
 */
public class ProtocolParser implements ProtocolCodecFactory
{
	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception
	{
		return new ProtocolEncoder()
		{
			@Override
			public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception
			{
				byte[] b = (byte[]) message;
				IoBuffer buf = IoBuffer.allocate(b.length).setAutoExpand(true);
				buf.put(b);
				buf.flip();
				out.write(buf);
				out.flush();
			}

			@Override
			public void dispose(IoSession session) throws Exception
			{
			}
		};
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception
	{
		return new ProtocolDecoder()
		{
			@Override
			public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception
			{
				byte[] b = new byte[in.limit()];
				in.get(b);
				out.write(b);
			}

			@Override
			public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception
			{
			}

			@Override
			public void dispose(IoSession session) throws Exception
			{
			}
		};
	}

}
