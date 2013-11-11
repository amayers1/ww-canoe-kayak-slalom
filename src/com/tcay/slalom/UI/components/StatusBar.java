/*
 * This file is part of SlalomApp.
 *
 *     SlalomApp is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     SlalomApp is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with SlalomApp.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.tcay.slalom.UI.components;


import com.tcay.slalom.Race;
import com.tcay.slalom.RaceResources;
import com.tcay.util.IpAddress;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 10/27/13
 * Time: 8:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class StatusBar {
    /// todo make class
    //private static StatusBar instance = null;
    private JLabel tagHeuerStatusLabelImageIcon;
    private JPanel statusPanel;
    private JLabel runIteration;
    private JLabel sectionsConnected;

//    public JPanel getStatusPanel() {
//        return statusPanel;
//    }


    public StatusBar() {
        createUI();
    }

//    public JLabel getTagHeuerStatusLabelImageIcon() {
//        return tagHeuerStatusLabelImageIcon;
//    }

    public void add(JComponent jc) {
        statusPanel.add(jc);
        ///new JLabel("Client " + ihasConnectedCnt++))
    }

    public void remove(JComponent jc) {
        //remove(jc);

    }

    public void updateSections() {
        runIteration.setText(new Integer(Race.getInstance().getCurrentRunIteration()).toString());
        String sectionOnline = Race.getInstance().getSectionsConnectedNamesAsString();
        runIteration.invalidate();

        if (Race.getInstance().isTagHeuerEmulation()) {   // fake out
            tagHeuerStatusLabelImageIcon.setEnabled(true);
            tagHeuerStatusLabelImageIcon.invalidate();

        }
        else {
            tagHeuerStatusLabelImageIcon.setEnabled(Race.getInstance().getTagHeuerConnected());
            tagHeuerStatusLabelImageIcon.invalidate();
        }

        sectionsConnected.setText(sectionOnline);
        sectionsConnected.invalidate();
        //statusPanel.invalidate();
    }


    private void createUI() {
///statusPanel.firePropertyChange();
        //JPanel
        statusPanel = new JPanel();

        tagHeuerStatusLabelImageIcon = new JLabel(RaceResources.getInstance().getTagHeuerII());
        runIteration = new JLabel();
        sectionsConnected = new JLabel();


        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        //frame.add(statusPanel, BorderLayout.SOUTH);

        //statusPanel.setPreferredSize(new Dimension(parent.getWidth(), 24));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusPanel.setBackground(new Color(-3342388));


        JLabel ipLabel = new JLabel("ip:" + new IpAddress().getIp4());
        statusPanel.add(ipLabel);
        statusPanel.add(Box.createRigidArea(new Dimension(10, 0)));     /// add space

        JLabel statusLabel = new JLabel("Run#");
        statusLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        statusPanel.add(statusLabel);

        runIteration.setText(new Integer(Race.getInstance().getCurrentRunIteration()).toString());
        statusPanel.add(runIteration);
        statusPanel.add(Box.createRigidArea(new Dimension(10, 0)));     /// add space


        statusLabel = new JLabel("Sections online:");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);
        sectionsConnected.setText(Race.getInstance().getSectionsConnectedNamesAsString());
        statusPanel.add(sectionsConnected);


        statusPanel.add(Box.createRigidArea(new Dimension(30, 0)));     /// add space

        statusPanel.add(tagHeuerStatusLabelImageIcon);
        tagHeuerStatusLabelImageIcon.setEnabled(false);


        Timer screenUpdatetimer = new Timer(500,
                new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        updateSections();
                    }
                });
        screenUpdatetimer.setInitialDelay(2000);
        screenUpdatetimer.start();
    }


    public JPanel getPanel(JFrame parent)///JFrame frame)
    {
        statusPanel.setPreferredSize(new Dimension(parent.getWidth(), 24));
        statusPanel.setMinimumSize(new Dimension(parent.getWidth(), 24));
        return (statusPanel);
    }
}
