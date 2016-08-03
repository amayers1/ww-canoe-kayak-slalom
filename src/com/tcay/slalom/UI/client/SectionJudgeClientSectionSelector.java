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

package com.tcay.slalom.UI.client;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.tcay.slalom.UI.JudgingSection;
import com.tcay.slalom.socket.Client;
import com.tcay.slalom.socket.Proxy;

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
 * Date: 10/26/13
 * Time: 9:47 AM
 */
public class SectionJudgeClientSectionSelector {

    private JComboBox comboBoxJudgingSection;
    private JPanel panel1;
    private JButton doneButton;
    private Proxy proxy;
    JFrame frame;


    private void close() {      // todo create super class parent to hold this
        Component c = panel1.getRootPane().getParent();
        c.setVisible(false);
        ((JFrame) c).dispose();

    }


    public SectionJudgeClientSectionSelector() {
        frame = new JFrame("Judging - New Frame");


        $$$setupUI$$$();
        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                close();
                frame.setContentPane(new ClientRacePenaltiesUIDynamic(comboBoxJudgingSection.getSelectedIndex() + 1, proxy).getRootComponent());
                frame.pack();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);         // todo unhide, etc
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
                Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
                int x = (int) rect.getMaxX() - frame.getWidth();
                int y = (int) rect.getMaxY() - frame.getHeight();
                frame.setLocation(x, y);
                frame.setVisible(true);
            }
        });

/*
        leeStartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                proxy.updateNewNRCeyeStart();
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });


        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                proxy.updateNewNRCeyeFinish();


                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        */
    }


    ArrayList<JudgingSection> judgingSections;
    int nbrSections;

    private void createUIComponents() {
        //   try {
        proxy = new Proxy(new Client());
        judgingSections = new ArrayList<JudgingSection>();
        //   } catch (InvalidArgumentException e) {
        //       e.printStackTrace();
        //   }
        nbrSections = proxy.getSections().size();
        judgingSections = proxy.getSections();

        //System.out.println("Setup comboBoxJudgingSection");
        comboBoxJudgingSection = new JComboBox();
        comboBoxJudgingSection.setModel(new DefaultComboBoxModel() {
            @Override
            public int getSize() {
                return nbrSections;//proxy.getSections().size();
            }

            @Override
            public Object getElementAt(int i) {
                return judgingSections.get(i);// proxy.getSections().get(i);
            }
        });
        if (comboBoxJudgingSection.getItemCount() > 0) {
            comboBoxJudgingSection.setSelectedIndex(0);       /// OK ?  fix me  out of bounds if server has no data yet
        }
        //System.out.println("Setup comboBoxJudgingSection DONE");

    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Section Judge Client");


        SectionJudgeClientSectionSelector sectionJudgeClient = new SectionJudgeClientSectionSelector(); ///fixme section 1 should be dynamic
        frame.setContentPane(sectionJudgeClient.panel1);
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
        panel1.setLayout(new FormLayout("fill:122px:noGrow,left:39dlu:noGrow,fill:257px:grow", "center:d:noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow"));
        CellConstraints cc = new CellConstraints();
        panel1.add(comboBoxJudgingSection, cc.xy(3, 1));
        final JLabel label1 = new JLabel();
        label1.setText("Section");
        panel1.add(label1, cc.xy(1, 1));
        doneButton = new JButton();
        doneButton.setText("Done");
        panel1.add(doneButton, cc.xy(3, 3));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
