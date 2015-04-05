package com.waylau.mina.demo.sumup;

import java.net.InetSocketAddress;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class Client {

	private static final long CONNECT_TIMEOUT = 30 * 1000L; // 30 秒;

	// 当是 false 时，来使用对象序列化来代替自定义编解码器
	private static final boolean USE_CUSTOM_CODEC = true;

	private static final String HOSTNAME = "localhost";

	private static final int PORT = 8080;

	public static void main(String[] args) throws Throwable {
		if (args.length == 0) {
			System.out.println("Please specify the list of any integers");
			return;
		}

		// prepare values to sum up
		int[] values = new int[args.length];
		for (int i = 0; i < args.length; i++) {
			values[i] = Integer.parseInt(args[i]);
		}

		NioSocketConnector connector = new NioSocketConnector();
		connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);

		if (USE_CUSTOM_CODEC) {
			connector.getFilterChain().addLast(
					"codec",
					new ProtocolCodecFilter(
							new SumUpProtocolCodecFactory(false)));
		} else {
			connector.getFilterChain().addLast(
					"codec",
					new ProtocolCodecFilter(
							new ObjectSerializationCodecFactory()));
		}

		connector.getFilterChain().addLast("logger", new LoggingFilter());
		connector.setHandler(new ClientSessionHandler(values));
		IoSession session;

		for (;;) {
			try {
				ConnectFuture future = connector.connect(new InetSocketAddress(
						HOSTNAME, PORT));
				future.awaitUninterruptibly();
				session = future.getSession();
				break;
			} catch (RuntimeIoException e) {
				System.err.println("Failed to connect.");
				e.printStackTrace();
				Thread.sleep(5000);
			}
		}

		// wait until the summation is done
		session.getCloseFuture().awaitUninterruptibly();
		connector.dispose();
	}

}
