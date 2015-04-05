package com.waylau.mina.demo.sumup;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

public class AddMessageEncoder<T extends AddMessage> extends
		AbstractMessageEncoder<T> {
	public AddMessageEncoder() {
		super(Constants.ADD);
	}

	@Override
	protected void encodeBody(IoSession session, T message, IoBuffer out) {
		out.putInt(message.getValue());
	}

	public void dispose() throws Exception {
	}
}
