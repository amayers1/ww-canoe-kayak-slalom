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

package com.tcay.slalom.UI;

import com.tcay.slalom.ListOfRaces;
import com.tcay.slalom.Race;
import com.tcay.slalom.RaceRun;
import com.tcay.slalom.UI.http.SlalomResultsScoringHTTP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by allen on 10/6/16.
 */
public class MarkRunStatus {
    private JPanel panel;
    private JButton OKButton;
    private JButton liveButton;
    private JButton officialButton;
    private JButton unofficialButton;
    // private JButton cancelButton;
    private JComboBox RunsStatus;


    private void close() {
        Component c = panel.getRootPane().getParent();
        c.setVisible(false);
        ((JFrame) c).dispose();

    }


    public MarkRunStatus() {
        $$$setupUI$$$();
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }


        });

//        cancelButton.addActionListener(new ActionListener() //{

//            @Override
//            public void actionPerformed(ActionEvent e) {

        //               close();

//            }
        //      });


        unofficialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                markClassRun(RaceRun.Status.UNOFFICIAL);
                Race.getInstance().printResultsHTTP(boatClass, runNbr);
            }
        });
        officialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                markClassRun(RaceRun.Status.OFFICIAL);
                Race.getInstance().printResultsHTTP(boatClass, runNbr);
            }
        });
        liveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                markClassRun(RaceRun.Status.LIVE);

            }
        });
    }

    String boatClass = null;
    int runNbr = 0;

    private void markClassRun(RaceRun.Status status) {

        Object o = RunsStatus.getSelectedItem();
        String statusString = (String) o;
        String s;


        StringTokenizer st = new StringTokenizer(statusString, ":");

        int i = 0;
        while (st.hasMoreTokens()) {
            s = st.nextToken();
            System.out.println(s);
            switch (i) {
                case 0:
                    boatClass = s;
                    break;
                case 1:
                    runNbr = Integer.parseInt(s);
                    break;
                case 2:
                    // current status string
                    break;
            }


            i++;

        }

        Race.getInstance().markClassRun(boatClass, runNbr, status);
        Race.getInstance().updateResults(true);  // A170417 Issue#49

        updateRunsStatus();

    }

    private void updateRunsStatus() {
        Object[] classes = Race.getInstance().getClassesRuns().toArray();
        RunsStatus.removeAllItems();
        for (Object aClass : classes) {
            RunsStatus.addItem(aClass);
        }

        System.out.println("OK");
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here

        RunsStatus = new JComboBox(Race.getInstance().getClassesRuns().toArray());

    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        final JPanel spacer1 = new JPanel();
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel.add(spacer2, gbc);
        officialButton = new JButton();
        officialButton.setText("Official");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(officialButton, gbc);
        unofficialButton = new JButton();
        unofficialButton.setText("Unofficial");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(unofficialButton, gbc);
        OKButton = new JButton();
        OKButton.setText("Done");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(OKButton, gbc);
        liveButton = new JButton();
        liveButton.setText("Live");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(liveButton, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(RunsStatus, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }
}
