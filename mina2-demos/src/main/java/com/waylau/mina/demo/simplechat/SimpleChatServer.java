/**
 * 
 */
package com.waylau.mina.demo.simplechat;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.waylau.mina.demo.chat.ChatProtocolHandler;


/**
 * 说明：简单聊天室 服务器
 *
 * @author <a href="http://www.waylau.com">waylau.com</a> 2015年4月10日
 */
public class SimpleChatServer {
    private final static Logger LOGGER = LoggerFactory.getLogger(SimpleChatServer.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8080;
		}
		
		SocketAcceptor acceptor = new NioSocketAcceptor();
		
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		acceptor.getFilterChain().addLast(
				"codec",
				new ProtocolCodecFilter(
						new ObjectSerializationCodecFactory()));
		acceptor.setHandler(new SimpleChatServerHandler());
		
		acceptor.getSessionConfig().setReadBufferSize(2048);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		
		try {
			acceptor.bind(new InetSocketAddress(port));
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOGGER.info("[Server]Listening on port " + port);
	}

}
