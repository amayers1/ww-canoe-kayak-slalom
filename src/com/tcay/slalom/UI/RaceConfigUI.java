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

import com.intellij.uiDesigner.core.GridLayoutManager;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.tcay.slalom.Race;
import com.tcay.slalom.TestData;
import com.tcay.util.TextValidate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;


/**
 * SlalomApp
 *
 * Teton Cay Group Inc. 2013
 *

 * User: allen
 * Date: 8/29/13
 * Time: 7:51 PM
 *
 */
public class RaceConfigUI {
    private JPanel panel1;
    private JPanel panelLower;
    private JPanel panelSectionConfig;

    private JTextField raceName;
    private JTextField raceVenue;
    private JTextField nbrGates;
    private JTextField upstreamGates;
    private JButton doneButton;
    private JSpinner dateSpinner1;
    private JTextField sectionEndingGates;
    private SectionConfigUI sectionConfig;


    public RaceConfigUI() {
        Date date = Calendar.getInstance().getTime();
        dateSpinner1 = new JSpinner();
        nbrGates = new JTextField(10);
        TextValidate.digitsOnly(nbrGates, 1, 25);
        /// 1 - last section highest gate ... could set
        // highest gate in last section to this number if lower thatn new # gates
        $$$setupUI$$$();

        dateSpinner1.setModel(new SpinnerDateModel(date, null, null, Calendar.DAY_OF_MONTH));
        dateSpinner1.setEditor(new JSpinner.DateEditor(dateSpinner1, "MM/dd/yy"));

        sectionConfig = new SectionConfigUI(false);
        panelSectionConfig.add(sectionConfig.getMainPanel());
        panelSectionConfig.setVisible(true);

        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                saveRace();
                close();
            }
        });

        Race race = Race.getInstance();
        raceName.setText(race.getName());
        raceVenue.setText(race.getLocation());
        nbrGates.setText(race.getNbrGates().toString());
        nbrGates.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Integer gateCount = (new Integer(nbrGates.getText().trim()));
                sectionConfig.updateSectionEndingGatesIfNeeded(gateCount);
            }
        });
    }


    private void close() {      // todo create super class parent to hold this
        Component c = panel1.getRootPane().getParent();
        c.setVisible(false);
        ((JFrame) c).dispose();

    }


    private void saveRace() {
        Race race = Race.getInstance();

        race.setName(raceName.getText());
        race.setLocation(raceVenue.getText());
        String gates = nbrGates.getText();
        race.setNbrGates(new Integer(gates.trim())); // THIS MUST HAPPEN BEFORE sectionConfig save
        sectionConfig.saveConfig();
    }

    //  public JTextField getRaceDate() {
    //      return raceDate;
    //  }

    //  public void setRaceDate(JTextField raceDate) {
    //      this.raceDate = raceDate;
    //  }

    public JTextField getRaceName() {
        return raceName;
    }

    public void setRaceName(JTextField raceName) {
        this.raceName = raceName;
    }

    public JTextField getRaceVenue() {
        return raceVenue;
    }

    public void setRaceVenue(JTextField raceVenue) {
        this.raceVenue = raceVenue;
    }

    public JTextField getNbrGates() {
        return nbrGates;
    }

    public void setNbrGates(JTextField nbrGates) {
        this.nbrGates = nbrGates;
    }

    public JTextField getUpstreamGates() {
        return upstreamGates;
    }

    public void setUpstreamGates(JTextField upstreamGates) {
        this.upstreamGates = upstreamGates;
    }

    public JTextField getSectionEndingGates() {
        return sectionEndingGates;
    }

    public void setSectionEndingGates(JTextField sectionEndingGates) {
        this.sectionEndingGates = sectionEndingGates;
    }

    public void setData(RaceConfigUI data) {
    }

    public void getData(RaceConfigUI data) {
    }

    public boolean isModified(RaceConfigUI data) {
        return false;
    }


    public static void main(String[] args) {
        new TestData();

        JFrame frame = new JFrame("RaceConfigUI");
        frame.setContentPane(new RaceConfigUI().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        panelSectionConfig = new JPanel();
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
        panel1 = new JPanel();
        panel1.setLayout(new FormLayout("fill:127px:noGrow,left:max(m;10px):noGrow,left:max(m;200px):grow,fill:max(d;4px):noGrow,fill:max(d;4px):noGrow", "center:d:noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:d:grow,top:3dlu:noGrow,center:max(d;4px):noGrow"));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("resources/SlalomAppMessages").getString("race.name"));
        CellConstraints cc = new CellConstraints();
        panel1.add(label1, cc.xy(1, 1));
        raceName = new JTextField();
        panel1.add(raceName, cc.xyw(3, 1, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("resources/SlalomAppMessages").getString("race.date"));
        panel1.add(label2, cc.xy(1, 3));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("resources/SlalomAppMessages").getString("race.location"));
        panel1.add(label3, cc.xy(1, 5));
        raceVenue = new JTextField();
        raceVenue.setMinimumSize(new Dimension(60, 28));
        raceVenue.setPreferredSize(new Dimension(60, 28));
        panel1.add(raceVenue, cc.xyw(3, 5, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        panelLower = new JPanel();
        panelLower.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panelLower, cc.xyw(1, 9, 5));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("resources/SlalomAppMessages").getString("race.config.totalNbrGates"));
        panel1.add(label4, cc.xy(1, 7));
        panel1.add(nbrGates, cc.xyw(3, 7, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        panel1.add(panelSectionConfig, cc.xyw(1, 11, 4, CellConstraints.CENTER, CellConstraints.FILL));
        doneButton = new JButton();
        doneButton.setText("Done");
        panel1.add(doneButton, cc.xyw(1, 13, 4));
        panel1.add(dateSpinner1, cc.xy(3, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadLabelText$$$(JLabel component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setDisplayedMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
