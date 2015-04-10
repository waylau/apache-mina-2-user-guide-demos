/**
 * 
 */
package com.waylau.mina.demo.simplechat;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 

/**
 * 说明：
 *
 * @author <a href="http://www.waylau.com">waylau.com</a>  2015年4月10日
 */
public class SimpleChatServerHandler extends IoHandlerAdapter {
	
    private final static Logger LOGGER = LoggerFactory.getLogger(SimpleChatServerHandler.class);
    
    private final Set<IoSession> sessions = Collections
            .synchronizedSet(new HashSet<IoSession>()); // 存放所有会话列表
    
    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        LOGGER.warn("Unexpected exception.", cause);
        
        // 遇到未捕获的异常，则关闭连接
        session.close(true);
    }
	@Override
    public void sessionCreated(IoSession session) throws Exception{
		sessions.add(session);
        broadcast("[Client]" + session.getRemoteAddress() + " has join the chat");
    }
	
   @Override
    public void sessionClosed(IoSession session) throws Exception {
        sessions.remove(session);
        broadcast("[Client]" + session.getRemoteAddress() + " has left the chat");
    }
	   
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		String str = message.toString();
		broadcast(str);
		LOGGER.info("[Clinet]"+ session.getRemoteAddress()+ str);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		LOGGER.info("[Server] IDLE " + session.getIdleCount(status));
	}
	
	/**
	 * 广播消息
	 * @param message
	 */
    public void broadcast(String message) {
        synchronized (sessions) {
            for (IoSession session : sessions) {
                if (session.isConnected()) {
                    session.write(message);
                }
            }
        }
    }
}
