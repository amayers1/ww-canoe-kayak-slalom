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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * ${PROJECT_NAME}
 *
 * Teton Cay Group Inc. ${YEAR}
 *

 * User: allen
 * Date: 10/27/13
 * Time: 8:37 PM
 *
 */
public class StatusBar {
    /// todo make class
    //private static StatusBar instance = null;
    private JLabel tagHeuerStatusLabelImageIcon;
    private JLabel microGateStatusLabelImageIcon;
    private JLabel algeStatusLabelImageIcon;

    private JPanel statusPanel;
    private JLabel runIteration;
    private JLabel sectionsConnected;

    private JLabel startEyeIndicator;
    private JLabel finishEyeIndicator;


//    public JPanel getStatusPanel() {
//        return statusPanel;
//    }


    public StatusBar() {
        createUI();
    }


    public void add(JComponent jc) {
        statusPanel.add(jc);
        ///new JLabel("Client " + ihasConnectedCnt++))
    }

    public void remove(JComponent jc) {
        //remove(jc);

    }


    /**
     *  Turn LEDs off if they have fired
     */

   // boolean startEyeLedOn = false;
  //  boolean stopEyeLedOn = false;


    public void updateLEDs() {
        // for now just flash on and off as a demo

       // if (Race.getInstance().isStartEyeTripped()) {
//     //       startEyeLedOn = true;
       //     Race.getInstance().clearStartEyeLedIndicator();
       // }
           Race.getInstance().checkLEDs();
        startEyeIndicator.setIcon(startEyeLED.getCurrentIcon());
        finishEyeIndicator.setIcon(finishEyeLED.getCurrentIcon());

        //startEyeIndicator.toggleOnOff();
        //stopEyeIndicator.toggleOnOff();

    }

    private static String timingBoxToolText = "Show list of timing impulses and allow for applying a selected impulse to a Race Run";

    public void updateSections() {
        runIteration.setText(new Integer(Race.getInstance().getCurrentRunIteration()).toString());
        String sectionOnline = Race.getInstance().getSectionsConnectedNamesAsString();
        runIteration.invalidate();

        if (Race.getInstance().getTagHeuerEnabled()) { // TODO Reimplement isTagHeuerEmulation()) {   // fake out
            tagHeuerStatusLabelImageIcon.setToolTipText(timingBoxToolText);
            tagHeuerStatusLabelImageIcon.setEnabled(true);
            tagHeuerStatusLabelImageIcon.setVisible(true);
            tagHeuerStatusLabelImageIcon.setEnabled(Race.getInstance().getTagHeuerConnected());
            tagHeuerStatusLabelImageIcon.invalidate();

        }
        if (Race.getInstance().getAlgeTimyEnabled()) {
            algeStatusLabelImageIcon.setToolTipText(timingBoxToolText);
            algeStatusLabelImageIcon.setEnabled(true);
            algeStatusLabelImageIcon.setVisible(true);
            algeStatusLabelImageIcon.invalidate();
        }

        if (Race.getInstance().getMicrogateEnabled()){
            microGateStatusLabelImageIcon.setToolTipText(timingBoxToolText);
            microGateStatusLabelImageIcon.setEnabled(true);
            microGateStatusLabelImageIcon.setVisible(true);

            microGateStatusLabelImageIcon.invalidate();
        }


// todo ??? HMMM Why?        microGateStatusLabelImageIcon.setRequestFocusEnabled(Race.getInstance().getMicrogateConnected());

        sectionsConnected.setText(sectionOnline);
        sectionsConnected.invalidate();
        //statusPanel.invalidate();
    }

    LedIndicator startEyeLED;
    LedIndicator finishEyeLED;

    private void createUI() {
///statusPanel.firePropertyChange();
        //JPanel

        startEyeLED = Race.getInstance().getStartEyeLED();
        finishEyeLED = Race.getInstance().getFinishEyeLED();

        statusPanel = new JPanel();

        tagHeuerStatusLabelImageIcon = new JLabel(RaceResources.getInstance().getTagHeuerII());
        microGateStatusLabelImageIcon = new JLabel(RaceResources.getInstance().getMicrogateII());
        algeStatusLabelImageIcon = new JLabel(RaceResources.getInstance().getTagHeuerII());//getAlgeII());
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
        tagHeuerStatusLabelImageIcon.setVisible(false);

        statusPanel.add(microGateStatusLabelImageIcon);
        microGateStatusLabelImageIcon.setEnabled(false);//true);   //todo
        microGateStatusLabelImageIcon.setVisible(false);

        statusPanel.add(algeStatusLabelImageIcon);
        algeStatusLabelImageIcon.setEnabled(true);
        algeStatusLabelImageIcon.setVisible(true);

        startEyeIndicator = new JLabel();
        startEyeIndicator.setEnabled(true);
        startEyeIndicator.setVisible(true);
        startEyeIndicator.setToolTipText("Start Line Sensor, click enable or disable");
        statusPanel.add(startEyeIndicator);


        finishEyeIndicator = new JLabel();
        finishEyeIndicator.setEnabled(true);
        finishEyeIndicator.setVisible(true);
        finishEyeIndicator.setToolTipText("Finish Line Sensor, click enable or disable");
        statusPanel.add(finishEyeIndicator);

        startEyeIndicator.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                boolean isActive = startEyeLED.isActive();
                startEyeLED.setIsActive(!isActive);
                startEyeIndicator.setIcon(startEyeLED.getCurrentIcon());
   //todo             startEyeIndicator.toggleActive();
            }
        });

        finishEyeIndicator.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                boolean isActive = finishEyeLED.isActive();

                finishEyeLED.setIsActive(!isActive);
                finishEyeIndicator.setIcon(finishEyeLED.getCurrentIcon());

            }
        });


        Timer screenUpdatetimer = new Timer(500, //500,   0.5 seconds was too fast
                new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        updateSections();
                        updateLEDs();
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
