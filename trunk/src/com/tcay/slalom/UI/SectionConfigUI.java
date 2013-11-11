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

//import com.intellij.ui.table.JBTable;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.tcay.slalom.Race;
import com.tcay.slalom.TestData;
import com.tcay.slalom.UI.components.JCheckBoxGateList;
import com.tcay.util.Log;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.List;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 8/29/13
 * Time: 9:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class SectionConfigUI extends JComponent {
    private JPanel panel1;
    private JComboBox comboBoxSection1;
    private JComboBox comboBoxSection2;
    private JComboBox comboBoxSection3;
    private JComboBox comboBoxSection4;
    private JComboBox comboBoxSection5;
    private JList upstreamCheckBoxList;
    private JButton doneButton;
    private JLabel section1Label;
    private JLabel section3Label;
    private JLabel section4Label;
    private JLabel section5Label;
    private JLabel section2Label;
    private JCheckBox icfPenaltyDiagrams;
    private JCheckBox tagHeuerEmulation;
    //    private JComboBox comboBoxSelectUpstream;
//    private JButton addButton;

    Integer nbrGates;
    Vector checkboxes;
    Log log;


    public JPanel getMainPanel() {
        return panel1;
    }


    public SectionConfigUI(boolean standalonePanel) {
        log = Log.getInstance();


        nbrGates = Race.getInstance().getNbrGates();
        doneButton = new JButton();
        $$$setupUI$$$();

        if (standalonePanel) {
            doneButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    saveConfig();
                    Component component = (Component) actionEvent.getSource();
                    JFrame frame = (JFrame) SwingUtilities.getRoot(component);
                    frame.dispose();
                }
            });
        } else {
            doneButton.setVisible(false);
        }
        updateComboBoxVisibility();

        comboBoxSection1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //JComboBox comboBox = (JComboBox) actionEvent.getSource();
                //Integer lastGate = (Integer) comboBox.getSelectedItem();
                updateComboBoxVisibility();
            }
        });

        updateComboBoxVisibility();
        comboBoxSection2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //JComboBox comboBox = (JComboBox) actionEvent.getSource();
                //Integer lastGate = (Integer) comboBox.getSelectedItem();
                updateComboBoxVisibility();
            }
        });


        comboBoxSection3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //JComboBox comboBox = (JComboBox) actionEvent.getSource();
                //Integer lastGate = (Integer) comboBox.getSelectedItem();
                updateComboBoxVisibility();
            }
        });

        comboBoxSection4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //JComboBox comboBox = (JComboBox) actionEvent.getSource();
                //Integer lastGate = (Integer) comboBox.getSelectedItem();
                updateComboBoxVisibility();
            }
        });
        comboBoxSection5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //JComboBox comboBox = (JComboBox) actionEvent.getSource();
                //Integer lastGate = (Integer) comboBox.getSelectedItem();
                updateComboBoxVisibility();
            }
        });


    }


    public void saveConfig() {
        ArrayList<Integer> upstreams = new ArrayList();

        for (Object o : checkboxes) {
            if (((JCheckBox) o).isSelected()) {
                upstreams.add(new Integer(((JCheckBox) o).getText()));
            }
        }
        Race.getInstance().setUpstreamGates(upstreams);
        ArrayList<JudgingSection> sections = new ArrayList();
        Integer lastGateInSection = (Integer) comboBoxSection1.getSelectedItem();
        Integer firstGateInSection = 1;
        Integer section = 1;
        sections.add(makeSection(section++, firstGateInSection, lastGateInSection));
        if (lastGateInSection < Race.getInstance().getNbrGates()) {
            firstGateInSection = lastGateInSection + 1;
            lastGateInSection = (Integer) comboBoxSection2.getSelectedItem();
            sections.add(makeSection(section++, firstGateInSection, lastGateInSection));

            if (lastGateInSection < Race.getInstance().getNbrGates()) {
                firstGateInSection = lastGateInSection + 1;
                lastGateInSection = (Integer) comboBoxSection3.getSelectedItem();
                sections.add(makeSection(section++, firstGateInSection, lastGateInSection));

                if (lastGateInSection < Race.getInstance().getNbrGates()) {
                    firstGateInSection = lastGateInSection + 1;
                    lastGateInSection = (Integer) comboBoxSection4.getSelectedItem();
                    sections.add(makeSection(section++, firstGateInSection, lastGateInSection));

                    if (lastGateInSection < Race.getInstance().getNbrGates()) {
                        firstGateInSection = lastGateInSection + 1;
                        lastGateInSection = (Integer) comboBoxSection5.getSelectedItem();
                        sections.add(makeSection(section++, firstGateInSection, lastGateInSection));
                    }
                }
            }
        }
        Race.getInstance().setSectionEndingGates(sections);
        Race.getInstance().setIcfPenalties(icfPenaltyDiagrams.isSelected());
        Race.getInstance().setTagHeuerEmulation(tagHeuerEmulation.isSelected());
    }


    private JudgingSection makeSection(Integer sectionNbr, Integer firstGate, Integer lastGate) {

        JudgingSection js = new JudgingSection(sectionNbr, firstGate, lastGate);
        return (js);

    }


    private Vector buildGateList() {
        int i;
        Vector gateList = new Vector();
        Integer iSelection;
        for (i = 0; i < nbrGates; i++) {
            iSelection = i + 1;
            gateList.add(iSelection);
        }
        return (gateList);
    }


    private boolean isUpstream(Integer iGate) {
        List<Integer> ups = Race.getInstance().getUpstreamGates();

        for (Integer up : ups) {
            if (up.intValue() == iGate)
                return (true);

        }
        return false;
    }


    private void rebuildCheckBoxes() {
        int i;
        Vector checkBoxList = new Vector();


        JCheckBox cb;
        Integer iGateNbr;
        for (i = 0; i < nbrGates; i++) {
            iGateNbr = i + 1;

            if (isUpstream(iGateNbr)) {
                cb = new JCheckBox(iGateNbr.toString(), Race.getInstance().getUpstreamSmallII());
                cb.setSelected(true);
                //cb.setIcon(Race.getInstance().getUpstreamSmallII());
            } else {
                cb = new JCheckBox(iGateNbr.toString(), Race.getInstance().getDownstreamSmallII());
            }
            checkBoxList.add(cb);
        }

        for (Object o : checkboxes) {
            Integer index = new Integer(((JCheckBox) o).getText().trim());
            try {
                if (((JCheckBox) o).isSelected()) {
                    cb = (JCheckBox) checkBoxList.get(index - 1);
                    cb.setSelected(true);                                                                //fixme this doesn't transfer to new list
                }
            } catch (Exception e) {
                log.error("Err 125");
            }
        }


        checkboxes = checkBoxList;
        upstreamCheckBoxList.setListData(checkboxes);
        //upstreamCheckBoxList = new JCheckBoxGateList(checkboxes);
        //upstreamCheckBoxList.invalidate();   //todo see if needed
        //return (checkBoxList);

    }


    private void buildCheckBoxes() {
        int i;
        Vector checkBoxList = new Vector();

        JCheckBox cb;
        Integer iGateNbr;
        for (i = 0; i < nbrGates; i++) {
            iGateNbr = i + 1;

            if (isUpstream(iGateNbr)) {
                cb = new JCheckBox(iGateNbr.toString(), Race.getInstance().getUpstreamSmallII());
                cb.setSelected(true);
                //cb.setIcon(Race.getInstance().getUpstreamSmallII());
            } else {
                cb = new JCheckBox(iGateNbr.toString(), Race.getInstance().getDownstreamSmallII());
            }
            checkBoxList.add(cb);
        }

        checkboxes = checkBoxList;
        upstreamCheckBoxList = new JCheckBoxGateList(checkboxes);

        // return (checkBoxList);
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
        //     table1 = new JBTable();
        //table1.addColumn(new TableColumn());
        buildComboBoxes();

        //comboBoxSelectUpstream = new JComboBox(model6);

        comboBoxSection1.setSelectedIndex(nbrGates - 1);                 ///fixme combo boxEs only have a section worth of gate .. not the whole list 1-25
        comboBoxSection2.setSelectedIndex(nbrGates - 1);
        comboBoxSection3.setSelectedIndex(nbrGates - 1);
        comboBoxSection4.setSelectedIndex(nbrGates - 1);
        comboBoxSection5.setSelectedIndex(nbrGates - 1);
        icfPenaltyDiagrams = new JCheckBox();
        icfPenaltyDiagrams.setSelected(Race.getInstance().isIcfPenalties());
        tagHeuerEmulation = new JCheckBox();
        tagHeuerEmulation.setSelected(Race.getInstance().isTagHeuerEmulation());


        ArrayList<JudgingSection> sections = Race.getInstance().getSectionEndingGates();

        int j = 1;
        for (JudgingSection js : sections) {

            switch (j) {
                case 1:
                    //fixme. after loading a sample race, then going to config or vice versa, have  sction with 25 gates, and onny
                    //fixme 18 gates total .... CRASH !!! when js.getLastGate > comboBox list
                    //fixme temporary mitigated with TestData bug fix 121104 (ajm) ... need more work to verify #of gates is NOT less than max section gate
                    comboBoxSection1.setSelectedIndex(js.getLastGate() - 1);                                 //fixme after new confg out of bounds
                    break;

                case 2:
                    comboBoxSection2.setSelectedIndex(js.getLastGate() - 1);
                    break;

                case 3:
                    comboBoxSection3.setSelectedIndex(js.getLastGate() - 1);
                    break;
                case 4:
                    comboBoxSection4.setSelectedIndex(js.getLastGate() - 1);
                    break;
                case 5:
                    comboBoxSection5.setSelectedIndex(js.getLastGate() - 1);
                    break;

                default:
                    break;
            }
            j++;
        }
        buildCheckBoxes();

    }


    private void buildComboBoxes() {
        Vector gateList = buildGateList();
        DefaultComboBoxModel model1 = new DefaultComboBoxModel(gateList);
        DefaultComboBoxModel model2 = new DefaultComboBoxModel(gateList);
        DefaultComboBoxModel model3 = new DefaultComboBoxModel(gateList);
        DefaultComboBoxModel model4 = new DefaultComboBoxModel(gateList);
        DefaultComboBoxModel model5 = new DefaultComboBoxModel(gateList);
        comboBoxSection1 = new JComboBox(model1);
        comboBoxSection2 = new JComboBox(model2);
        comboBoxSection3 = new JComboBox(model3);
        comboBoxSection4 = new JComboBox(model4);
        comboBoxSection5 = new JComboBox(model5);

    }

    private void rebuildComboBoxModels() {
        Vector gateList = buildGateList();
        DefaultComboBoxModel model1 = new DefaultComboBoxModel(gateList);
        DefaultComboBoxModel model2 = new DefaultComboBoxModel(gateList);
        DefaultComboBoxModel model3 = new DefaultComboBoxModel(gateList);
        DefaultComboBoxModel model4 = new DefaultComboBoxModel(gateList);
        DefaultComboBoxModel model5 = new DefaultComboBoxModel(gateList);

        comboBoxSection1.setModel(model1);
        comboBoxSection2.setModel(model2);
        comboBoxSection3.setModel(model3);
        comboBoxSection4.setModel(model4);
        comboBoxSection5.setModel(model5);

        comboBoxSection1.setSelectedIndex(nbrGates - 1);
        comboBoxSection2.setSelectedIndex(nbrGates - 1);
        comboBoxSection3.setSelectedIndex(nbrGates - 1);
        comboBoxSection4.setSelectedIndex(nbrGates - 1);
        comboBoxSection5.setSelectedIndex(nbrGates - 1);

    }


    public void updateSectionEndingGatesIfNeeded(Integer nbrGates) {
        this.nbrGates = nbrGates;
//        updateComboBoxVisibility();
        rebuildCheckBoxes();
        rebuildComboBoxModels();
        updateComboBoxVisibility();
    }


    private void setNextComboBoxSection(JComboBox thisOne, JComboBox previous) {

        Integer index = Math.min((Integer) previous.getSelectedItem() + 1, nbrGates) - 1;
        thisOne.setSelectedIndex(index);         // fixme - when increasing number of gates - crash out of bounds
    }

    private void handleComboBoxUpdate(JComboBox thisOne, JLabel thisLabel, JComboBox previous) {
        if ((Integer) thisOne.getSelectedItem() <= (Integer) previous.getSelectedItem()) {
            setNextComboBoxSection(thisOne, previous);
            //comboBoxSection2.setSelectedIndex((Integer) comboBoxSection1.getSelectedItem());
        }

        if ((Integer) previous.getSelectedItem() < nbrGates) {
            thisOne.setVisible(true);
            thisLabel.setVisible(true);
        }
//        else {
//            thisOne.setVisible(false);
//            thisLabel.setVisible(false);
//        }

    }


    //public
    private void updateComboBoxVisibility() {


        //boolean stopButtonsVisible = false;

        comboBoxSection2.setVisible(false);
        comboBoxSection3.setVisible(false);
        comboBoxSection4.setVisible(false);
        comboBoxSection5.setVisible(false);
        section2Label.setVisible(false);
        section3Label.setVisible(false);
        section4Label.setVisible(false);
        section5Label.setVisible(false);

        // make invisible all combo boxes that
        //are not needed - this after the last gate
        if ((Integer) comboBoxSection1.getSelectedItem() <= nbrGates) {                // fixme kludge

            handleComboBoxUpdate(comboBoxSection2, section2Label, comboBoxSection1);
        } else {
            comboBoxSection1.setSelectedIndex(nbrGates - 1);
            comboBoxSection2.setSelectedIndex(nbrGates - 1);
            comboBoxSection3.setSelectedIndex(nbrGates - 1);
            comboBoxSection4.setSelectedIndex(nbrGates - 1);
            comboBoxSection5.setSelectedIndex(nbrGates - 1);
        }

        if ((Integer) comboBoxSection2.getSelectedItem() <= nbrGates) {
            handleComboBoxUpdate(comboBoxSection3, section3Label, comboBoxSection2);
        } else {
            comboBoxSection2.setSelectedIndex(nbrGates - 1);
            comboBoxSection3.setSelectedIndex(nbrGates - 1);
            comboBoxSection4.setSelectedIndex(nbrGates - 1);
            comboBoxSection5.setSelectedIndex(nbrGates - 1);
        }
        int selected = (Integer) comboBoxSection3.getSelectedItem();
        if (selected <= nbrGates) {
            handleComboBoxUpdate(comboBoxSection4, section4Label, comboBoxSection3);
        } else {
            comboBoxSection3.setSelectedIndex(nbrGates - 1);
            comboBoxSection4.setSelectedIndex(nbrGates - 1);
            comboBoxSection5.setSelectedIndex(nbrGates - 1);
        }

        if ((Integer) comboBoxSection4.getSelectedItem() <= nbrGates) {
            handleComboBoxUpdate(comboBoxSection5, section5Label, comboBoxSection4);
        } else {
            comboBoxSection4.setSelectedIndex(nbrGates - 1);
            comboBoxSection5.setSelectedIndex(nbrGates - 1);
        }
        if ((Integer) comboBoxSection5.getSelectedItem() > nbrGates) {
            comboBoxSection5.setSelectedIndex(nbrGates - 1);
        }


//        if (run1 != null && !run1.isComplete() && !run1.isDnf()) {
//            stopButtonsVisible = true;
//        }
//        racer1DNFButton.setVisible(stopButtonsVisible);
//        racer1FinishButton.setVisible(stopButtonsVisible);

        // doesn't work here,needed before frame setVisible and after pack()       startButton.requestFocusInWindow();

    }


    public static void main(String[] args) {

        //  race = Race.getInstance();      //todo "static" remove?
        new TestData();

        JFrame frame = new JFrame("SectionConfigUI");
        frame.setContentPane(new SectionConfigUI(true).panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
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
        panel1.setLayout(new FormLayout("fill:247px:noGrow,left:5dlu:noGrow,fill:74px:noGrow,left:4dlu:noGrow,fill:74px:noGrow,left:23dlu:noGrow,fill:max(d;4px):noGrow", "center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:12px:noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:29px:noGrow,top:3dlu:noGrow,top:19dlu:noGrow,center:max(d;4px):noGrow,center:174px:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow"));
        CellConstraints cc = new CellConstraints();
        panel1.add(comboBoxSection1, cc.xy(5, 7));
        section1Label = new JLabel();
        section1Label.setText("Section 1");
        panel1.add(section1Label, cc.xy(3, 7));
        section2Label = new JLabel();
        section2Label.setText("Section 2");
        panel1.add(section2Label, cc.xy(3, 10));
        panel1.add(comboBoxSection2, cc.xy(5, 10));
        section3Label = new JLabel();
        section3Label.setText("Section 3");
        panel1.add(section3Label, cc.xy(3, 12));
        panel1.add(comboBoxSection3, cc.xy(5, 12));
        section4Label = new JLabel();
        section4Label.setText("Section 4");
        panel1.add(section4Label, cc.xy(3, 14));
        panel1.add(comboBoxSection4, cc.xy(5, 14));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("resources/SlalomAppMessages").getString("sectionConfig.lastGate"));
        panel1.add(label1, cc.xy(5, 3));
        section5Label = new JLabel();
        section5Label.setText("Section 5");
        panel1.add(section5Label, cc.xy(3, 16));
        panel1.add(comboBoxSection5, cc.xy(5, 16));
        final JList list1 = new JList();
        list1.setToolTipText("Causes diagrams for touches and reason codes for 50s to appear when assessing penalties");
        panel1.add(list1, cc.xy(5, 21, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, cc.xy(5, 20, CellConstraints.DEFAULT, CellConstraints.FILL));
        scrollPane1.setViewportView(upstreamCheckBoxList);
        doneButton.setText("Done");
        panel1.add(doneButton, cc.xy(1, 23));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("resources/SlalomAppMessages").getString("sectionConfig.sectionConfiguration"));
        panel1.add(label2, cc.xyw(3, 1, 5, CellConstraints.CENTER, CellConstraints.DEFAULT));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("resources/SlalomAppMessages").getString("sectionConfig.advancedOptions"));
        panel1.add(label3, cc.xy(1, 1));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("resources/SlalomAppMessages").getString("sectionConfig.selectUpstreamGates"));
        panel1.add(label4, cc.xyw(3, 19, 4, CellConstraints.CENTER, CellConstraints.DEFAULT));
        final JLabel label5 = new JLabel();
        label5.setFont(new Font(label5.getFont().getName(), label5.getFont().getStyle(), 8));
        label5.setText("to be moved to Advanced Options page");
        panel1.add(label5, cc.xy(1, 3));
        this.$$$loadButtonText$$$(icfPenaltyDiagrams, ResourceBundle.getBundle("resources/SlalomAppMessages").getString("sectionConfig.useIcfPenalties"));
        icfPenaltyDiagrams.setToolTipText(ResourceBundle.getBundle("resources/SlalomAppMessages").getString("sectionConfig.icfPenaltyDiagrams.tooltip"));
        panel1.add(icfPenaltyDiagrams, cc.xy(1, 7));
        tagHeuerEmulation.setLabel("Use Tag Heuer Emulation");
        this.$$$loadButtonText$$$(tagHeuerEmulation, ResourceBundle.getBundle("resources/SlalomAppMessages").getString("sectionConfig.useTagHeuerEmulation"));
        tagHeuerEmulation.setToolTipText(ResourceBundle.getBundle("resources/SlalomAppMessages").getString("sectionConfig.useTagHeuerEmulation.tooltip"));
        panel1.add(tagHeuerEmulation, cc.xy(1, 10));
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
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
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
            component.setMnemonic(mnemonic);
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
