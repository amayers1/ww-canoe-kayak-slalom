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

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.tcay.slalom.Penalty;
import com.tcay.slalom.Race;
import com.tcay.slalom.RaceRun;
import com.tcay.slalom.TestData;
import com.tcay.slalom.UI.client.ClientRacePenaltiesUIDynamic;
import com.tcay.slalom.UI.components.PenaltyRadioButton;

import javax.swing.*;
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
public class RacePenaltiesUIDynamicOrig {
    private JComboBox activeOrRecentRunsComboBox;     // todo dynamic update .... newly started racers don't show
    private JPanel panel1;
    private JButton selectRaceRun;                    // the run being scored or displayed in the UI
    private JLabel raceRunLabel;                      // Name of racer and which run
    private JButton doneBtn;
    private int nbrGates;                             // actual number of gates on this course
    private long getRunsStartedOrCompletedCnt = 0;    // current total number of runs (either on course or completed)
    private RaceRun selectedRun = null;               // the run that will be/is displayed in the Penalty UI
    private Timer listCheckForUpdatesTimer;           // timer to trigger auto update of activeOrRecentRunsComboBox
    private ButtonGroup bg;                           // group to allow mutually exclusive
    private static final int MAX_NBR_GATES = 25;
    private static final int POSSIBLE_GATE_OUTCOMES = 3;    // (Clean=0, touch=2, miss=50)

    private PenaltyRadioButton[] buttons = new PenaltyRadioButton[MAX_NBR_GATES* POSSIBLE_GATE_OUTCOMES];



    private void createPenaltyButtons()
    {

        bg = new ButtonGroup();
        String[] names = {"Clean", "Touch", "50"};
        int[] seconds = {0,2,50};
        for (int i = 0; i < buttons.length; i++) {                  // create enough buttons for maximum displayable
            buttons[i] = new PenaltyRadioButton(names[i%3],         //
                                                (i/3)+1,            // assign gate numbers to the buttons
                                                seconds[i%3]);      // assign appropriate # of seconds (0,2,50))
                                                // todo one time instantiation
            if (i%3==0) {
               bg = new ButtonGroup();
            }

           bg.add(buttons[i]);
           buttons[i].addActionListener(
                   new ActionListener() {
                       public void actionPerformed(ActionEvent e) {
                           PenaltyRadioButton btn;
                           btn = (PenaltyRadioButton)e.getSource() ;
                           selectedRun.setPenalty( btn.getGateNbr(), btn.getGateNbr(),false);
                       }
                   });
        }

    }





    private void addDynToForm()
    {
        CellConstraints cc = new CellConstraints();
        int row=0;
        for (int i = 0; i < nbrGates*3; i++) {
            if (i%3==0)
                panel1.add(new JLabel("Gate " + ((i/3)+1)), cc.xy(1,(3+(row*2))));     // todo move to single load time instantiation
            panel1.add(buttons[i], cc.xy(((i%3)*2)+3, (3+(row*2))));
            if (i%3==2) {
                row++;
            }
        }
    }


    private void loadPenalties(RaceRun run) {
        ArrayList<Penalty> penalties = run.getPenaltyList();


        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setEnabled(true);
            if (i%3==0)
                buttons[i].setSelected(true);  // set Clean to true
        }

        for (Penalty p:penalties) {
            int gateNbr = p.getGate();

            int index = (gateNbr-1)*3;

            switch (p.getPenaltySeconds()) {
                case 2:
                    index +=1;
                    break;
                case 50:
                    index +=2;
                    break;
            }
            buttons[index].setSelected(true);
        }
    }


    public RacePenaltiesUIDynamicOrig() {

        nbrGates = Math.min(Race.getInstance().getNbrGates(), buttons.length);

        setupUI();

        addDynToForm();


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

        };
        return (model);
    }


    private void createUIComponents() {


        //

        activeOrRecentRunsComboBox = new JComboBox();
        //

        doneBtn = new JButton();

        listCheckForUpdatesTimer
                = new Timer(2000,   // changed from 250 t0 25000
                new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        // don't change comboBox contents if it has focus - confuses the user
                        if (!activeOrRecentRunsComboBox.hasFocus()) {
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
        listCheckForUpdatesTimer.setInitialDelay(2500);    //Changed to 5000
        listCheckForUpdatesTimer.start();
        raceRunLabel = new JLabel();
        createPenaltyButtons();
        updateButtonVisibility();
    }


    public static void main(String[] args) {
        new TestData();

        JFrame frame = new JFrame("Race Penalties UI");
        frame.setContentPane(new RacePenaltiesUIDynamicOrig().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    private void updateButtonVisibility() {
        if (selectedRun == null) {

            for (PenaltyRadioButton p:buttons) {
                p.setEnabled(false);
                p.setVisible(false);

            }
            doneBtn.setVisible(false);
        } else {
            for (PenaltyRadioButton p:buttons) {
                p.setEnabled(true);
                p.setVisible(true);
            }
            doneBtn.setVisible(true);
        }
    }

    public void setData(RacePenaltiesUIDynamicOrig data) {
    }

    public void getData(RacePenaltiesUIDynamicOrig data) {
    }

    public boolean isModified(RacePenaltiesUIDynamicOrig data) {
        return false;
    }


    private void setupUI() {
        createUIComponents();
        panel1 = new JPanel();

        panel1.setLayout(new FormLayout("fill:140px:grow,left:4dlu:noGrow,fill:139px:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:139px:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:29dlu:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,fill:max(d;4px):noGrow", "center:d:noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow"));
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Penalty Scoring"));
        CellConstraints cc = new CellConstraints();
        panel1.add(activeOrRecentRunsComboBox, cc.xyw(3, 1, 4));
        selectRaceRun = new JButton();
        selectRaceRun.setText("Select Boat");
        panel1.add(selectRaceRun, cc.xy(7, 1));
        doneBtn.setText("Done");
        panel1.add(doneBtn, cc.xy(5, (nbrGates*2)+3));
      //  final Spacer spacer1 = new Spacer();
      //  panel1.add(spacer1, cc.xy(5, 13, CellConstraints.DEFAULT, CellConstraints.FILL));
    }

    public JComponent getRootComponent() {
        return panel1;
    }

}
