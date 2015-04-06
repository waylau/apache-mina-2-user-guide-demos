package com.waylau.mina.demo.chat;

import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.logging.MdcInjectionFilter;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.waylau.mina.demo.echoserver.ssl.BogusSslContextFactory;

/**
 * (<b>Entry point</b>) Chat server
 *
 * @author waylau.com
 * @date 2015-4-6
 */
public class Main {
    /** Choose your favorite port number. */
    private static final int PORT = 1234;

    /** Set this to true if you want to make the server SSL */
    private static final boolean USE_SSL = false;

    public static void main(String[] args) throws Exception {
        NioSocketAcceptor acceptor = new NioSocketAcceptor();
        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();

        MdcInjectionFilter mdcInjectionFilter = new MdcInjectionFilter();
        chain.addLast("mdc", mdcInjectionFilter);

        // Add SSL filter if SSL is enabled.
        if (USE_SSL) {
            addSSLSupport(chain);
        }

        chain.addLast("codec", new ProtocolCodecFilter(
                new TextLineCodecFactory()));

        addLogger(chain);

        // Bind
        acceptor.setHandler(new ChatProtocolHandler());
        acceptor.bind(new InetSocketAddress(PORT));

        System.out.println("Listening on port " + PORT);
    }

    private static void addSSLSupport(DefaultIoFilterChainBuilder chain)
            throws Exception {
        SslFilter sslFilter = new SslFilter(BogusSslContextFactory
                .getInstance(true));
        chain.addLast("sslFilter", sslFilter);
        System.out.println("SSL ON");
    }

    private static void addLogger(DefaultIoFilterChainBuilder chain)
            throws Exception {
        chain.addLast("logger", new LoggingFilter());
        System.out.println("Logging ON");
    }
}
