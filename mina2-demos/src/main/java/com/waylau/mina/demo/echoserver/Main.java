package com.waylau.mina.demo.echoserver;

import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.waylau.mina.demo.echoserver.ssl.BogusSslContextFactory;

/**
 * (<b>Entry point</b>) Echo server
 *
 * @author waylau.com
 * @date 2015-4-6
 */
public class Main {
    /** Choose your favorite port number. */
    private static final int PORT = 8080;

    /** Set this to true if you want to make the server SSL */
    private static final boolean USE_SSL = false;

    public static void main(String[] args) throws Exception {
        SocketAcceptor acceptor = new NioSocketAcceptor();
        acceptor.setReuseAddress( true );
        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
        
        // Add SSL filter if SSL is enabled.
        if (USE_SSL) {
            addSSLSupport(chain);
        }

        // Bind
        acceptor.setHandler(new EchoProtocolHandler());
        acceptor.bind(new InetSocketAddress(PORT));

        System.out.println("Listening on port " + PORT);
        
        for (;;) {
            System.out.println("R: " + acceptor.getStatistics().getReadBytesThroughput() + 
                ", W: " + acceptor.getStatistics().getWrittenBytesThroughput());
            Thread.sleep(3000);
        }
    }

    private static void addSSLSupport(DefaultIoFilterChainBuilder chain)
            throws Exception {
        SslFilter sslFilter = new SslFilter(BogusSslContextFactory
                .getInstance(true));
        chain.addLast("sslFilter", sslFilter);
        System.out.println("SSL ON");
    }
}
