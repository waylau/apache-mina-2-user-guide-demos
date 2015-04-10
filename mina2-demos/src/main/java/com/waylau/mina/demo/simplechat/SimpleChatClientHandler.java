package com.waylau.mina.demo.simplechat;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.waylau.mina.demo.sumup.message.ResultMessage;

/**
 * 说明：
 *
 * @author <a href="http://www.waylau.com">waylau.com</a>  2015年4月10日
 */
public class SimpleChatClientHandler extends IoHandlerAdapter implements
		IoHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(SimpleChatServerHandler.class);
	/**
	 * 
	 */
	public SimpleChatClientHandler() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public void messageReceived(IoSession session, Object message) {
		String str = message.toString();
		LOGGER.info("[Clinet]"+ session.getRemoteAddress()+ str);
	}
}
