package com.waylau.mina.demo.chat;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * (<b>Entry point</b>) Chat server which uses Spring and the serverContext.xml
 * file to set up MINA and the server handler.
 *
 * @author waylau.com
 * @date 2015-4-6
 */
public class SpringMain {

    public static void main(String[] args) throws Exception {
        if (System.getProperty("com.sun.management.jmxremote") != null) {
            System.out.println("JMX enabled.");
        } else {
            System.out
                    .println("JMX disabled. Please set the "
                            + "'com.sun.management.jmxremote' system property to enable JMX.");
        }
        getApplicationContext();
        System.out.println("Listening ...");
    }

    public static ConfigurableApplicationContext getApplicationContext() {
        return new ClassPathXmlApplicationContext("com/waylau/mina/demo/chat/serverContext.xml");
    }
}
