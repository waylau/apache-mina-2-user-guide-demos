package com.waylau.mina.demo.sumup.codec;

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

import com.waylau.mina.demo.sumup.message.AddMessage;
import com.waylau.mina.demo.sumup.message.ResultMessage;

public class SumUpProtocolCodecFactory extends DemuxingProtocolCodecFactory {
	public SumUpProtocolCodecFactory(boolean server) {
		if (server) {
			super.addMessageDecoder(AddMessageDecoder.class);
			super.addMessageEncoder(ResultMessage.class,
					ResultMessageEncoder.class);
		} else // Client
		{
			super.addMessageEncoder(AddMessage.class, AddMessageEncoder.class);
			super.addMessageDecoder(ResultMessageDecoder.class);
		}
	}
}
