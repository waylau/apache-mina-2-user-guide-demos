package com.waylau.mina.demo.simplechat;

import java.net.InetSocketAddress;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 

/**
 * 说明：简单聊天室 客户端
 *
 * @author <a href="http://www.waylau.com">waylau.com</a>  2015年4月10日
 */
public class SimpleChatClient {
    private final static Logger LOGGER = LoggerFactory.getLogger(SimpleChatClient.class);
	private static final long CONNECT_TIMEOUT = 30 * 1000L; // 30 秒;
	private static final String HOSTNAME = "localhost";
	private static final int PORT = 8080;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		NioSocketConnector connector = new NioSocketConnector();
		connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);
		connector.getFilterChain().addLast("logger", new LoggingFilter());
		connector.getFilterChain().addLast(
				"codec",
				new ProtocolCodecFilter(
						new ObjectSerializationCodecFactory()));
		connector.setHandler(new SimpleChatClientHandler());
		IoSession session;

		for (;;) {
			try {
				ConnectFuture future = connector.connect(new InetSocketAddress(
						HOSTNAME, PORT));
				future.awaitUninterruptibly();
				session = future.getSession();
				break;
			} catch (RuntimeIoException e) {
				LOGGER.warn(e.getMessage());
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					LOGGER.error(e.getMessage());
				}
			}
		}

		// wait until the summation is done
		session.getCloseFuture().awaitUninterruptibly();
		connector.dispose();
	}

}
