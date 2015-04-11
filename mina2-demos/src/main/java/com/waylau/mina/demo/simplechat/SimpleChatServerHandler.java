package com.waylau.mina.demo.simplechat;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
 

/**
 * 说明：简单聊天室 服务器 处理器
 *
 * @author <a href="http://www.waylau.com">waylau.com</a>  2015年4月10日
 */
public class SimpleChatServerHandler extends IoHandlerAdapter {
	
    private final Set<IoSession> sessions = Collections
            .synchronizedSet(new HashSet<IoSession>()); // 存放所有会话列表
    
    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        cause.printStackTrace();
		System.out.println("[Server] Client:"+session.getRemoteAddress()+"异常");
        // 遇到未捕获的异常，则关闭连接
        session.close(true);
    }
	@Override
    public void sessionCreated(IoSession session) throws Exception{
		sessions.add(session);
        broadcast(" has join the chat", session);
    }
	
   @Override
    public void sessionClosed(IoSession session) throws Exception {
        sessions.remove(session);
        broadcast(" has left the chat", session);
    }
	   
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		String str = message.toString();
		broadcast(str, session);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		System.out.println("[Server] IDLE " + session.getRemoteAddress() +  session.getIdleCount(status));
	}
	
	/**
	 * 广播消息
	 * @param message
	 */
    public void broadcast(String message, IoSession exceptSession) {
        synchronized (sessions) {
            for (IoSession session : sessions) {
                if (session.isConnected()) {
                	if(session.equals(exceptSession)){
                		 session.write("[You]" + message);
                	}else{
                        session.write("[Client"+session.getRemoteAddress()+"] "+message);
                	}

                }
            }
        }
    }
}
