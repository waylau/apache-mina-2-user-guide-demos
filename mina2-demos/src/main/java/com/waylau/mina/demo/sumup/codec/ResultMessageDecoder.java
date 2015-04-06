package com.waylau.mina.demo.sumup.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.waylau.mina.demo.sumup.message.AbstractMessage;
import com.waylau.mina.demo.sumup.message.ResultMessage;

public class ResultMessageDecoder extends AbstractMessageDecoder {
	private int code;

	private boolean readCode;

	public ResultMessageDecoder() {
		super(Constants.RESULT);
	}

	@Override
	protected AbstractMessage decodeBody(IoSession session, IoBuffer in) {
		if (!readCode) {
			if (in.remaining() < Constants.RESULT_CODE_LEN) {
				return null; // Need more data.
			}

			code = in.getShort();
			readCode = true;
		}

		if (code == Constants.RESULT_OK) {
			if (in.remaining() < Constants.RESULT_VALUE_LEN) {
				return null;
			}

			ResultMessage m = new ResultMessage();
			m.setOk(true);
			m.setValue(in.getInt());
			readCode = false;
			return m;
		} else {
			ResultMessage m = new ResultMessage();
			m.setOk(false);
			readCode = false;
			return m;
		}
	}

	public void finishDecode(IoSession session, ProtocolDecoderOutput out)
			throws Exception {
	}
}
