package com.waylau.mina.demo.simplechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
 

/**
 * 说明：简单聊天室 客户端
 *
 * @author <a href="http://www.waylau.com">waylau.com</a>  2015年4月10日
 */
public class SimpleChatClient {
	private static final long CONNECT_TIMEOUT = 30 * 1000L; // 30 秒;
	private static final String HOSTNAME = "127.0.0.1";
	private static final int PORT = 8080;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		NioSocketConnector connector = new NioSocketConnector();
		connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);
		connector.getFilterChain().addLast( "codec", 
				new ProtocolCodecFilter( new TextLineCodecFactory( Charset.forName( "UTF-8" ))));
		connector.setHandler(new SimpleChatClientHandler());
		IoSession session;
		ConnectFuture future = connector.connect(new InetSocketAddress(
				HOSTNAME, PORT));
		future.awaitUninterruptibly();
		session = future.getSession();

        while(true){
        	try {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
				session.write(in.readLine());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		// wait until the summation is done
		//session.getCloseFuture().awaitUninterruptibly();
		//connector.dispose();
	}

}
