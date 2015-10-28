package com.waylau.mina.demo.simplechat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;


/**
 * 说明：简单聊天室 服务器
 *
 * @author <a href="http://www.waylau.com">waylau.com</a> 2015年4月10日
 */
public class SimpleChatServer {
	
	public static void main(String[] args) {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8080;
		}
		
		SocketAcceptor acceptor = new NioSocketAcceptor(); // (1)
		
		acceptor.getFilterChain().addLast( "codec", 
				new ProtocolCodecFilter( new TextLineCodecFactory( Charset.forName( "UTF-8" ))));      // (2)
		acceptor.setHandler(new SimpleChatServerHandler()); // (3)
		
		acceptor.getSessionConfig().setReadBufferSize(2048); // (4)
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 100);// (5)
		
		try {
			acceptor.bind(new InetSocketAddress(port)); // (6)
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("[Server]Listening on port " + port);
	}
}
