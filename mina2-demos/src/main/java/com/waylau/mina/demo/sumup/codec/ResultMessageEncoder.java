package com.waylau.mina.demo.sumup.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import com.waylau.mina.demo.sumup.message.ResultMessage;

public class ResultMessageEncoder<T extends ResultMessage> extends
		AbstractMessageEncoder<T> {
	public ResultMessageEncoder() {
		super(Constants.RESULT);
	}

	@Override
	protected void encodeBody(IoSession session, T message, IoBuffer out) {
		if (message.isOk()) {
			out.putShort((short) Constants.RESULT_OK);
			out.putInt(message.getValue());
		} else {
			out.putShort((short) Constants.RESULT_ERROR);
		}
	}

	public void dispose() throws Exception {
	}
}
