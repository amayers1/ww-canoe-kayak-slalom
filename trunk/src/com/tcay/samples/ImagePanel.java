package com.tcay.samples;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 10/30/13
 * Time: 6:00 PM
 * To change this template use File | Settings | File Templates.
 */
import com.tcay.slalom.Race;

import java.awt.*;
import java.net.URL;
import javax.swing.*;

public class ImagePanel extends JPanel {

    private Image img;

    public ImagePanel(LayoutManager layout) {
        super(layout);
    }

    public ImagePanel(Image img, LayoutManager layout) {
        this(layout);
        this.img = img;
    }

    public Image getImage() {
        return img;
    }

    public void setImage(Image value) {
        if (img != value) {
            Image old = img;
            img = value;
            firePropertyChange("image", old, img);
            revalidate();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        Image img = getImage();
        return img == null ? super.getPreferredSize() : new Dimension(img.getWidth(this), img.getHeight(this));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img != null) {
            g.drawImage(img, 0, 0, this);
        }
    }

    public void doIt() {
            JFrame frame = new JFrame();

            add(new JLabel("Label One"));
        add(new JLabel("Label Two"));
        add(new JLabel("Label Three"));
        add(new JLabel("Label Four"));
            frame.setContentPane(this);
            frame.pack();
            frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.

        // http://upload.wikimedia.org/wikipedia/de/d/de/TAG_Heuer_Logo.svg
        //final ImageIcon ii = null;
        URL imgURL = ImagePanel.class.getResource("SlalomLogoYelCyan.jpg");
        final ImageIcon ii = imgURL==null ? null : new ImageIcon(imgURL, "background");

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ImagePanel(ii.getImage(), new FlowLayout()).doIt();
            }
        });
    }
}
