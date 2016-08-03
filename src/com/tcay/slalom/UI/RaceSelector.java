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

///import com.intellij.openapi.roots.ui.configuration.JdkComboBox;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.tcay.slalom.ListOfRaces;
import com.tcay.slalom.Race;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * ${PROJECT_NAME}
 * <p/>
 * Teton Cay Group Inc. ${YEAR}
 * <p/>
 * <p/>
 * User: allen
 * Date: 10/20/13
 * Time: 5:08 PM
 */
public class RaceSelector {
    private JComboBox raceChoices;
    private JPanel panel1;
    private JButton loadButton;

    public RaceSelector() {
        $$$setupUI$$$();
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                String raceName = (String) raceChoices.getSelectedItem();

                Race.getInstance().setName(raceName);
                Race.getInstance().loadSerializedData();
                close();


            }
        });
    }

    private void close() {      // todo create super class parent to hold this
        Component c = panel1.getRootPane().getParent();
        c.setVisible(false);
        ((JFrame) c).dispose();

    }


    private void createUIComponents() {
        ArrayList races = new ListOfRaces().list(".");
        raceChoices = new JComboBox(races.toArray());
        // TODO: place custom component creation code here
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
        panel1.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:23dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:d:noGrow"));
        CellConstraints cc = new CellConstraints();
        panel1.add(raceChoices, cc.xy(3, 1));
        final JLabel label1 = new JLabel();
        label1.setText("Races");
        panel1.add(label1, cc.xy(1, 1));
        loadButton = new JButton();
        loadButton.setText("Load");
        panel1.add(loadButton, cc.xy(5, 1));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
