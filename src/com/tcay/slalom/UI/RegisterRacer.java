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

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.tcay.slalom.Race;
import com.tcay.slalom.Racer;

import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ${PROJECT_NAME}
 * <p/>
 * Teton Cay Group Inc. ${YEAR}
 * <p/>
 * <p/>
 * User: allen
 * Date: 8/28/13
 * Time: 3:11 PM
 */
public class RegisterRacer {
    private JPanel mainPanel;
    private JTextField firstName;
    private JTextField lastName;
    private JTextField mobilePhone;
    private JTextField email;
    private JTextField federationNbr;
    private JTextField tshirtSize;
    private JTextField sex;
    private JTextField birthdate;
    private JTextField clubCountry;
    private JCheckBox paidCheckBox;
    private JComboBox racerListComboBox;
    private JButton selectButton;
    private JTextField bibNbr;
    private JButton saveButton;
    private JButton cancelButton;
    Racer racer;

    public RegisterRacer() {
        $$$setupUI$$$();
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Object o = racerListComboBox.getSelectedItem();//;actionEvent.getSource();
                racer = (Racer) o;

                firstName.setText(racer.getFirstName());
                lastName.setText(racer.getLastName());
                mobilePhone.setText(racer.getMobilePhone());
                sex.setText(racer.getSex());
                email.setText(racer.getEmailAddress());
                federationNbr.setText(racer.getFederationNumber());
                tshirtSize.setText(racer.gettShirtSize());
//                birthdate.setText(racer.getBirthday().toString());
                clubCountry.setText(racer.getClub());

//                mainPanel.invalidate();
                //mobilePhone.setText(racer.get)

            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                racer.setFirstName(firstName.getText());
                racer.setLastName(lastName.getText());
                racer.setMobilePhone(mobilePhone.getText());
                racer.setSex(sex.getText());

                racer.setEmailAddress(email.getText());
                racer.setFederationNumber(federationNbr.getText());
                racer.settShirtSize(tshirtSize.getText());
//                birthdate.setText(racer.getBirthday().toString());
                racer.setClub(clubCountry.getText());


                racer.setClub(clubCountry.getText());

            }
        });
    }


    private ComboBoxModel updateComboBoxModel() {
        List<Racer> racers = Race.getInstance().getRacers();
        ComboBoxModel model = new DefaultComboBoxModel(racers.toArray());//scorableList.toArray()) {
        //@Override
        //public void setSelectedItem(Object o) {
        //    //To change body of implemented methods use File | Settings | File Templates.
        //}

        //@Override
        //public Object getSelectedItem() {
        //    return null;  //To change body of implemented methods use File | Settings | File Templates.
        //}

        // @Override
        // public int getSize() {
        //     return 0;  //To change body of implemented methods use File | Settings | File Templates.
        // }

        //@Override
        //public Object getElementAt(int i) {
        //    return null;  //To change body of implemented methods use File | Settings | File Templates.
        // }

        //@Override
        //public void addListDataListener(ListDataListener listDataListener) {
        //    //To change body of implemented methods use File | Settings | File Templates.
        //}

        //@Override
        //public void removeListDataListener(ListDataListener listDataListener) {
        //    //To change body of implemented methods use File | Settings | File Templates.
        //}
        //  }

        //;
        return (model);
//        return null;
    }


    public void setData(RegisterRacer data) {
    }

    public void getData(RegisterRacer data) {
    }

    public boolean isModified(RegisterRacer data) {
        return false;
    }

    private void createUIComponents() {
        racerListComboBox = new JComboBox();
        racerListComboBox.setModel(updateComboBoxModel());
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
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(9, 4, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.setBorder(BorderFactory.createTitledBorder("Register Racer"));
        final JLabel label1 = new JLabel();
        label1.setText("First Name");
        mainPanel.add(label1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Last Name");
        mainPanel.add(label2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Birthdate");
        mainPanel.add(label3, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Email Address");
        mainPanel.add(label4, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        firstName = new JTextField();
        mainPanel.add(firstName, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        lastName = new JTextField();
        mainPanel.add(lastName, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        birthdate = new JTextField();
        mainPanel.add(birthdate, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        email = new JTextField();
        mainPanel.add(email, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        federationNbr = new JTextField();
        mainPanel.add(federationNbr, new GridConstraints(5, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Federation#");
        mainPanel.add(label5, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Sex");
        mainPanel.add(label6, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sex = new JTextField();
        mainPanel.add(sex, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Bib Nbr");
        mainPanel.add(label7, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bibNbr = new JTextField();
        mainPanel.add(bibNbr, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        paidCheckBox = new JCheckBox();
        paidCheckBox.setText("Paid");
        mainPanel.add(paidCheckBox, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mainPanel.add(racerListComboBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Lookup Racer");
        mainPanel.add(label8, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        selectButton = new JButton();
        selectButton.setText("Select");
        mainPanel.add(selectButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tshirtSize = new JTextField();
        mainPanel.add(tshirtSize, new GridConstraints(7, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Shirt size");
        mainPanel.add(label9, new GridConstraints(7, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mobilePhone = new JTextField();
        mainPanel.add(mobilePhone, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("Mobile Phone");
        mainPanel.add(label10, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("Club/Country");
        mainPanel.add(label11, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        clubCountry = new JTextField();
        mainPanel.add(clubCountry, new GridConstraints(6, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:d:grow"));
        mainPanel.add(panel1, new GridConstraints(8, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        saveButton = new JButton();
        saveButton.setHorizontalAlignment(0);
        saveButton.setText("Save");
        CellConstraints cc = new CellConstraints();
        panel1.add(saveButton, cc.xy(1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
        cancelButton = new JButton();
        cancelButton.setText("Cancel");
        panel1.add(cancelButton, cc.xy(3, 1));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
