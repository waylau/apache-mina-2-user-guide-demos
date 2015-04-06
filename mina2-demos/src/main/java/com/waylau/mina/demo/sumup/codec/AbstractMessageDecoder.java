package com.waylau.mina.demo.sumup.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import com.waylau.mina.demo.sumup.message.AbstractMessage;

public abstract class AbstractMessageDecoder implements MessageDecoder {
	private final int type;

	private int sequence;

	private boolean readHeader;

	protected AbstractMessageDecoder(int type) {
		this.type = type;
	}

	public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
		// Return NEED_DATA if the whole header is not read yet.
		if (in.remaining() < Constants.HEADER_LEN) {
			return MessageDecoderResult.NEED_DATA;
		}

		// Return OK if type and bodyLength matches.
		if (type == in.getShort()) {
			return MessageDecoderResult.OK;
		}

		// Return NOT_OK if not matches.
		return MessageDecoderResult.NOT_OK;
	}

	public MessageDecoderResult decode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		// Try to skip header if not read.
		if (!readHeader) {
			in.getShort(); // Skip 'type'.
			sequence = in.getInt(); // Get 'sequence'.
			readHeader = true;
		}

		// Try to decode body
		AbstractMessage m = decodeBody(session, in);
		// Return NEED_DATA if the body is not fully read.
		if (m == null) {
			return MessageDecoderResult.NEED_DATA;
		} else {
			readHeader = false; // reset readHeader for the next decode
		}
		m.setSequence(sequence);
		out.write(m);

		return MessageDecoderResult.OK;
	}

	/**
	 * @return <tt>null</tt> if the whole body is not read yet
	 */
	protected abstract AbstractMessage decodeBody(IoSession session, IoBuffer in);
}