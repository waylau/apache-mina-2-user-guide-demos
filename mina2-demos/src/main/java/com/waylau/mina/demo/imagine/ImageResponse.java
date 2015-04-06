package com.waylau.mina.demo.imagine;

import java.awt.image.BufferedImage;

/**
 * response sent by the server when receiving an {@link ImageRequest}
 *
 * @author waylau.com
 * @date 2015-2-26
 */
public class ImageResponse {

    private BufferedImage image1;

    private BufferedImage image2;

    public ImageResponse(BufferedImage image1, BufferedImage image2) {
        this.image1 = image1;
        this.image2 = image2;
    }

    public BufferedImage getImage1() {
        return image1;
    }

    public BufferedImage getImage2() {
        return image2;
    }
}
