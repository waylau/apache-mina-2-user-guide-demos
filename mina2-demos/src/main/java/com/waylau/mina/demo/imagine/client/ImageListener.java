package com.waylau.mina.demo.imagine.client;

import java.awt.image.BufferedImage;

/**
 * TODO Add documentation
 * 
 * @author waylau.com
 * @date 2015-2-26
 */
public interface ImageListener {
    void onImages(BufferedImage image1, BufferedImage image2);

    void onException(Throwable throwable);

    void sessionOpened();

    void sessionClosed();
}
