package com.waylau.mina.demo.imagine.client;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * JPanel capable of drawing two {@link BufferedImage}'s
 *
 * @author waylau.com
 * @date 2015-2-26
 */
public class ImagePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    
    private BufferedImage image1;
    private BufferedImage image2;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image1 != null) {
            g.drawImage(image1, 20, 20, null);
            if (image2 != null) {
                g.drawImage(image2, 20, image1.getHeight() + 80, null);
            }
        }
    }

    public void setImages(BufferedImage image1, BufferedImage image2) {
        this.image1 = image1;
        this.image2 = image2;
        repaint();
    }

}
