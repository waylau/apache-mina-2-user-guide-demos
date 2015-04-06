package com.waylau.mina.demo.sumup;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.waylau.mina.demo.sumup.message.AddMessage;
import com.waylau.mina.demo.sumup.message.ResultMessage;

/**
 * {@link IoHandler} for SumUp server.
 *
 * @author waylau.com
 * @date 2015-2-26
 */
public class ServerSessionHandler extends IoHandlerAdapter {
    
    private static final String SUM_KEY = "sum";

    private final static Logger LOGGER = LoggerFactory.getLogger(ServerSessionHandler.class);
    
    @Override
    public void sessionOpened(IoSession session) {
        // set idle time to 60 seconds
        session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 60);

        // initial sum is zero
        session.setAttribute(SUM_KEY, Integer.valueOf(0));
    }

    @Override
    public void messageReceived(IoSession session, Object message) {
        // client only sends AddMessage. otherwise, we will have to identify
        // its type using instanceof operator.
        AddMessage am = (AddMessage) message;

        // add the value to the current sum.
        int sum = ((Integer) session.getAttribute(SUM_KEY)).intValue();
        int value = am.getValue();
        long expectedSum = (long) sum + value;
        if (expectedSum > Integer.MAX_VALUE || expectedSum < Integer.MIN_VALUE) {
            // if the sum overflows or underflows, return error message
            ResultMessage rm = new ResultMessage();
            rm.setSequence(am.getSequence()); // copy sequence
            rm.setOk(false);
            session.write(rm);
        } else {
            // sum up
            sum = (int) expectedSum;
            session.setAttribute(SUM_KEY, Integer.valueOf(sum));

            // return the result message
            ResultMessage rm = new ResultMessage();
            rm.setSequence(am.getSequence()); // copy sequence
            rm.setOk(true);
            rm.setValue(sum);
            session.write(rm);
        }
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) {
        LOGGER.info("Disconnecting the idle.");
        // disconnect an idle client
        session.close(true);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        // close the connection on exceptional situation
        session.close(true);
    }
}