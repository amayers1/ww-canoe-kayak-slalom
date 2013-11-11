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

import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.tcay.slalom.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * ${PROJECT_NAME}
 *
 * Teton Cay Group Inc. ${YEAR}
 *

 * User: allen
 * Date: 8/29/13
 * Time: 10:25 AM
 *
 */
@Deprecated
public class RacePenaltiesUI_REFERENCE {
    private JComboBox activeOrRecentRunsComboBox;     // todo dynamic update .... newly started racers don't show
    private JRadioButton rb1_Clean;
    private JRadioButton rb1_50;
    private JRadioButton rb1_Touch;
    private JLabel gate1Label;

    private JRadioButton rb2_Clean;
    private JRadioButton rb2_50;
    private JRadioButton rb2_Touch;
    private JLabel gate2Label;

    private JRadioButton rb3_Clean;
    private JRadioButton rb3_50;
    private JRadioButton rb3_Touch;
    private JLabel gate3Label;

    private JPanel panel1;
    private JButton selectRaceRun;
    private JLabel raceRunLabel;
    private JButton doneBtn;
    private ButtonGroup group;

    private long getRunsStartedOrCompletedCnt = 0;


    RaceRun selectedRun = null;

    Timer listCheckForUpdatesTimer;

    private void loadPenalties(RaceRun run) {
        ArrayList<Penalty> penalties = run.getPenaltyList();


        rb1_Clean.setSelected(true);
        rb2_Clean.setSelected(true);
        rb3_Clean.setSelected(true);


        for (Penalty p : penalties) {
            int seconds = p.getPenaltySeconds();
            int gate = p.getGate().intValue();
            switch (gate) {
                case 1:
                    switch (seconds) {
                        case 2:
                            rb1_Touch.setSelected(true);
                            break;
                        case 50:
                            rb1_50.setSelected(true);
                            break;
                        default:
                            break;
                    }
                    break;
                case 2:
                    switch (seconds) {
                        case 2:
                            rb2_Touch.setSelected(true);
                            break;
                        case 50:
                            rb2_50.setSelected(true);
                            break;
                        default:

                            break;
                    }
                    break;
                case 3:
                    switch (seconds) {
                        case 2:
                            rb3_Touch.setSelected(true);
                            break;
                        case 50:
                            rb3_50.setSelected(true);
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }

    }


    public RacePenaltiesUI_REFERENCE() {
        $$$setupUI$$$();


        rb1_Clean.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedRun.setPenalty(1, 0, false);
            }
        });
        rb1_Touch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedRun.setPenalty(1, 2, false);
            }
        });


        rb1_50.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedRun.setPenalty(1, 50, false);
            }
        });


        rb2_Clean.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedRun.setPenalty(2, 0, false);
            }
        });
        rb2_Touch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedRun.setPenalty(2, 2, false);
            }
        });


        rb2_50.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedRun.setPenalty(2, 50, false);
            }
        });


        rb3_Clean.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedRun.setPenalty(3, 0, false);
            }
        });
        rb3_Touch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedRun.setPenalty(3, 2, false);
            }
        });


        rb3_50.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedRun.setPenalty(3, 50, false);
            }
        });


        selectRaceRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedRun = (RaceRun) activeOrRecentRunsComboBox.getSelectedItem();
                raceRunLabel.setText(selectedRun.toString());       // todo null pointer here when scoring window up without any events to trigger list populate
                selectRaceRun.setEnabled(false);
                activeOrRecentRunsComboBox.setEnabled(false);

                loadPenalties(selectedRun);

                updateButtonVisibility();
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        doneBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //todo Done action handler
                selectedRun = null;
                selectRaceRun.setEnabled(true);
                activeOrRecentRunsComboBox.setEnabled(true);
                raceRunLabel.setText(null);
                updateButtonVisibility();
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }


    private ComboBoxModel updateComboBoxModel() {
        ArrayList<RaceRun> scorableList = Race.getInstance().getScorableRuns();
        ComboBoxModel model = new DefaultComboBoxModel(scorableList.toArray()) {
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
        };
        return (model);
    }


    private void createUIComponents() {


        //

        activeOrRecentRunsComboBox = new JComboBox();
        //

        doneBtn = new JButton();

        listCheckForUpdatesTimer
                = new Timer(250,
                new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        // don't change comboBox contents if it has focus - confuses the user
                        if (activeOrRecentRunsComboBox.hasFocus() == false) {
                            if (getRunsStartedOrCompletedCnt != Race.getInstance().getRunsStartedOrCompletedCnt()) {
                                getRunsStartedOrCompletedCnt = Race.getInstance().getRunsStartedOrCompletedCnt();
                                activeOrRecentRunsComboBox.setModel(updateComboBoxModel());
                                int cnt = activeOrRecentRunsComboBox.getItemCount();
                                int index = cnt - 1;
                                activeOrRecentRunsComboBox.setSelectedIndex(index);

                            }
                        }
                    }
                });
        listCheckForUpdatesTimer.setInitialDelay(500);
        listCheckForUpdatesTimer.start();


        panel1 = new JPanel();
        panel1.setPreferredSize(new Dimension(400, 500));

        raceRunLabel = new JLabel();

        //ArrayList<RaceRun> scorableList = Race.getInstance().getScorableRuns();
        //activeOrRecentRunsComboBox = new JComboBox();
        //for (RaceRun r : scorableList) {
        //    activeOrRecentRunsComboBox.addItem(r);
        //}

        gate1Label = new JLabel();
        gate1Label.setText("Gate 1");
        //Group the radio buttons.
        rb1_Clean = new JRadioButton();
        rb1_Touch = new JRadioButton();
        rb1_50 = new JRadioButton();
        rb1_Clean.setSelected(true);
        group = new ButtonGroup();
        group.add(rb1_Clean);
        group.add(rb1_50);
        group.add(rb1_Touch);


        gate2Label = new JLabel();
        gate2Label.setText("Gate 2");

        rb2_Clean = new JRadioButton();
        rb2_Touch = new JRadioButton();
        rb2_50 = new JRadioButton();

        rb2_Clean.setSelected(true);


        group = new ButtonGroup();
        group.add(rb2_Clean);
        group.add(rb2_50);
        group.add(rb2_Touch);

        gate3Label = new JLabel();
        gate3Label.setText("Gate 3");

        rb3_Clean = new JRadioButton();
        rb3_Touch = new JRadioButton();
        rb3_50 = new JRadioButton();

        rb3_Clean.setSelected(true);


        group = new ButtonGroup();
        group.add(rb3_Clean);
        group.add(rb3_50);
        group.add(rb3_Touch);


        updateButtonVisibility();
    }


    public static void main(String[] args) {
        new TestData();

        JFrame frame = new JFrame("Race Penalties UI");
        frame.setContentPane(new RacePenaltiesUI_REFERENCE().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    private void updateButtonVisibility() {
        if (selectedRun == null) {
            rb1_Clean.setVisible(false);
            rb1_Touch.setVisible(false);
            rb1_50.setVisible(false);

            rb2_Clean.setVisible(false);
            rb2_Touch.setVisible(false);
            rb2_50.setVisible(false);

            rb3_Clean.setVisible(false);
            rb3_Touch.setVisible(false);
            rb3_50.setVisible(false);

            doneBtn.setVisible(false);


        } else {
            rb1_Clean.setVisible(true);
            rb1_Touch.setVisible(true);
            rb1_50.setVisible(true);

            rb2_Clean.setVisible(true);
            rb2_Touch.setVisible(true);
            rb2_50.setVisible(true);

            rb3_Clean.setVisible(true);
            rb3_Touch.setVisible(true);
            rb3_50.setVisible(true);

            doneBtn.setVisible(true);

        }
    }

    public void setData(RacePenaltiesUI_REFERENCE data) {
    }

    public void getData(RacePenaltiesUI_REFERENCE data) {
    }

    public boolean isModified(RacePenaltiesUI_REFERENCE data) {
        return false;
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
        panel1.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:d:grow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:d:grow,top:3dlu:noGrow,center:max(d;4px):noGrow"));
        panel1.setMinimumSize(new Dimension(513, 200));
        panel1.setOpaque(false);
        panel1.setPreferredSize(new Dimension(513, 200));
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Penalty Scoring"));
        CellConstraints cc = new CellConstraints();
        panel1.add(activeOrRecentRunsComboBox, cc.xyw(3, 1, 4));
        rb1_Touch.setText("Touch (2)");
        panel1.add(rb1_Touch, cc.xy(5, 5));
        rb1_Clean.setText("Clean");
        panel1.add(rb1_Clean, cc.xy(3, 5));
        panel1.add(gate1Label, cc.xy(1, 5));
        final JLabel label1 = new JLabel();
        label1.setText("Racer");
        panel1.add(label1, cc.xy(1, 3));
        rb1_50.setText("Missed (50)");
        panel1.add(rb1_50, cc.xywh(7, 5, 1, 2));
        panel1.add(raceRunLabel, cc.xy(3, 3));
        selectRaceRun = new JButton();
        selectRaceRun.setText("Select Boat");
        panel1.add(selectRaceRun, cc.xy(7, 1));
        rb2_Touch.setText("Touch (2)");
        panel1.add(rb2_Touch, cc.xy(5, 9));
        rb2_Clean.setText("Clean");
        panel1.add(rb2_Clean, cc.xy(3, 9));
        panel1.add(gate2Label, cc.xy(1, 9));
        rb2_50.setText("Missed (50)");
        panel1.add(rb2_50, cc.xy(7, 9));
        rb3_Touch.setText("Touch (2)");
        panel1.add(rb3_Touch, cc.xy(5, 13));
        rb3_Clean.setText("Clean");
        panel1.add(rb3_Clean, cc.xy(3, 13));
        panel1.add(gate3Label, cc.xy(1, 13));
        rb3_50.setText("Missed (50)");
        panel1.add(rb3_50, cc.xy(7, 13));
        doneBtn.setText("Done");
        panel1.add(doneBtn, cc.xy(5, 17));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, cc.xy(5, 15, CellConstraints.DEFAULT, CellConstraints.FILL));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, cc.xy(5, 11, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JLabel label2 = new JLabel();
        label2.setText("Section 2");
        panel1.add(label2, cc.xy(1, 11));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.FILL));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }


    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */

}
