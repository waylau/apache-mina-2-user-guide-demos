package com.waylau.mina.demo.imagine.server;

import java.lang.management.ManagementFactory;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.integration.jmx.IoFilterMBean;
import org.apache.mina.integration.jmx.IoServiceMBean;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.waylau.mina.demo.imagine.codec.ImageCodecFactory;

/**
 * entry point for the server used in the tutorial on protocol codecs
 *
 * @author waylau.com
 * @date 2015-2-26
 */

public class ImageServer {
    public static final int PORT = 33789;

    public static void main(String[] args) throws Exception {
        
        // create a JMX MBean Server server instance
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        
        // Create a class that handles sessions, incoming and outgoing data.  For
        // this step, we will pass in the MBeanServer so that sessions can be 
        // registered on the MBeanServer.
        ImageServerIoHandler handler = new ImageServerIoHandler( mBeanServer );
        
        // This socket acceptor will handle incoming connections
        NioSocketAcceptor acceptor = new NioSocketAcceptor();
        
        // create a JMX-aware bean that wraps a MINA IoService object.  In this
        // case, a NioSocketAcceptor.  
        IoServiceMBean acceptorMBean = new IoServiceMBean( acceptor );
        
        // create a JMX ObjectName.  This has to be in a specific format.  
        ObjectName acceptorName = new ObjectName( acceptor.getClass().getPackage().getName() + 
            ":type=acceptor,name=" + acceptor.getClass().getSimpleName());
        
        // register the bean on the MBeanServer.  Without this line, no JMX will happen for
        // this acceptor.
        mBeanServer.registerMBean( acceptorMBean, acceptorName );
        
        // add an IoFilter .  This class is responsible for converting the incoming and 
        // outgoing raw data to ImageRequest and ImageResponse objects
        ProtocolCodecFilter protocolFilter = new ProtocolCodecFilter(new ImageCodecFactory(false));
        
        // create a JMX-aware bean that wraps a MINA IoFilter object.  In this
        // case, a ProtocolCodecFilter
        IoFilterMBean protocolFilterMBean = new IoFilterMBean( protocolFilter );
        
        // create a JMX ObjectName.
        ObjectName protocolFilterName = new ObjectName( protocolFilter.getClass().getPackage().getName() + 
            ":type=protocolfilter,name=" + protocolFilter.getClass().getSimpleName() );
        
        // register the bean on the MBeanServer.  Without this line, no JMX will happen for
        // this filter.
        mBeanServer.registerMBean( protocolFilterMBean, protocolFilterName );
        
        // add the protocolFilter to the acceptor, otherwise no filtering of data will happen
        acceptor.getFilterChain().addLast("protocol", protocolFilter);
        
        // get a reference to the filter chain from the acceptor
        DefaultIoFilterChainBuilder filterChainBuilder = acceptor.getFilterChain();
        
        // add an ExecutorFilter to the filter chain.  The preferred order is to put the executor filter
        // after any protocol filters due to the fact that protocol codecs are generally CPU-bound
        // which is the same as I/O filters.
        filterChainBuilder.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
        
        // set this NioSocketAcceptor's handler to the ImageServerHandler
        acceptor.setHandler(handler);
        
        // Bind to the specified address.  This kicks off the listening for 
        // incoming connections
        acceptor.bind(new InetSocketAddress(PORT));
        System.out.println("Step 3 server is listenig at port " + PORT);
    }
}
