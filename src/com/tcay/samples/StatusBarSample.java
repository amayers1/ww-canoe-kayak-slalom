package com.tcay.samples;

import com.tcay.slalom.RaceResources;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 10/27/13
 * Time: 7:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class StatusBarSample {

    private void doIt() {
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setSize(200, 200);

        // create the status bar panel and shove it down the bottom of the frame
        doStatusBar(frame);
        frame.setVisible(true);

    }

    public void doStatusBar(JFrame parent)///JFrame frame)
    {
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        parent.add(statusPanel, BorderLayout.SOUTH);                       /// need this

        statusPanel.setPreferredSize(new Dimension(parent.getWidth(), 24));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        JLabel statusLabel = new JLabel("status");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);



        statusLabel = new JLabel("Fuel Low");
        statusLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        statusPanel.add(statusLabel);

        statusPanel.add(new JButton("Button",RaceResources.getInstance().getTagHeuerII()));


        JLabel picLabel = new JLabel(RaceResources.getInstance().getTagHeuerII());
        statusPanel.add(picLabel);
        picLabel = new JLabel(RaceResources.getInstance().getTagHeuerII());

        statusPanel.add(picLabel);
        picLabel.setEnabled(false);


    }


    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new StatusBarSample().doIt();
            }
        });
    }


}
