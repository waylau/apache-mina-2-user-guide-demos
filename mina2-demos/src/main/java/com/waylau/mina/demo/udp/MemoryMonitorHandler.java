package com.waylau.mina.demo.udp;

import java.net.SocketAddress;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class MemoryMonitorHandler extends IoHandlerAdapter {
	private MemoryMonitor server;

	public MemoryMonitorHandler(MemoryMonitor server) {
		this.server = server;
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		session.close(true);
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {

		if (message instanceof IoBuffer) {
			IoBuffer buffer = (IoBuffer) message;
			SocketAddress remoteAddress = session.getRemoteAddress();
			server.recvUpdate(remoteAddress, buffer.getLong());
		}
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		System.out.println("Session closed...");
		SocketAddress remoteAddress = session.getRemoteAddress();
		server.removeClient(remoteAddress);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {

		System.out.println("Session created...");

		SocketAddress remoteAddress = session.getRemoteAddress();
		server.addClient(remoteAddress);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		System.out.println("Session idle...");
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		System.out.println("Session Opened...");
	}
}
