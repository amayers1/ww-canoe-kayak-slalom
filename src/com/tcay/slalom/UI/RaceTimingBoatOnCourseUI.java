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
import com.tcay.slalom.RaceRun;
import com.tcay.slalom.UI.components.BibLabel;

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
 * Date: 10/30/13
 * Time: 7:21 AM
 */
public class RaceTimingBoatOnCourseUI {
    private JPanel subPanel;

    public RaceRun getRun() {
        return run;
    }

    private RaceRun run;
    private JLabel nameLabel;
    private JLabel timerLabel;
    private JButton finishButton;
    private JButton DNFButton;
    private JLabel bibLabel;
    private JButton overtakeButton;


    final RaceTimingUI timingUI;     //fixme sort out if ths move is OK  M131111

    protected RaceTimingBoatOnCourseUI(RaceTimingUI raceTimingUI) {
        //final RaceTimingUI
        timingUI = raceTimingUI;
        $$$setupUI$$$();

        DNFButton.addActionListener(new ActionListener() {
            @Override
            // TODO Confirmation DialogBox
            public void actionPerformed(ActionEvent actionEvent) {
                if (run != null) {
                    run.dnf();
                }

                timingUI.removeDNFrun(run);  /// remove the DNF
                updateButtonVisibility();
                timingUI.updateButtonVisibility();
            }
        });
        finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                finishButtonActionHandler();
            }
        });

        overtakeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                overtakeButtonActionHandler();
            }
        });


        updateButtonVisibility();
    }


    public void finishButtonActionHandler(boolean stopClock) {

        if (run != null) {
            if (stopClock) {
                run.finish();
            }
        }
        updateButtonVisibility();//todo switch order of callse 20160412
        //timingUI.updateRunsUI();
        timingUI.updateButtonVisibility();
        //        // Need UI to update other runs button for OverTake button vis


    }


    public void finishButtonActionHandler() {
        finishButtonActionHandler(true);

    }


    public void overtakeButtonActionHandler() {
//        updateButtonVisibility();//todo switch order of callse 20160412
        timingUI.doOvertake(run);
        updateButtonVisibility();//todo switch order of callse 20160412

        timingUI.updateButtonVisibility();
        //        // Need UI to update other runs button for OverTake button vis

        System.out.println("SWAPPED FOR OVERTAKE ");
    }


    protected void updateTimer() {
        if (run == null) {
            timerLabel.setText("");
            timerLabel.setIcon(null);
        } else {
            timerLabel.setText(run.getResultString() + " +" + run.getTotalPenalties());
            if (run.getPhotoCellRaceRun() != null) {  // todo
                timerLabel.setIcon(Race.getInstance().getPhotoCellTinyII());//getMicrogateII());//getTagHeuerII());  // TODO
            } else {
                timerLabel.setIcon(Race.getInstance().getStopWatchII());
            }

        }
    }

    protected void updateRun(RaceRun run) {
        this.run = run;
        nameLabel.setText(run == null ? "" : run.getBoat().racerClassClub());
        nameLabel.setIcon(run == null ? null : run.getBoat().getImageIcon());
        bibLabel.setText(run == null ? "" : run.getBoat().getRacer().getBibNumber());
        updateButtonVisibility();
        //return (run);
    }

    protected void updateButtonVisibility() {
        boolean visible = false;

        boolean overTakevisible = false;// false;
        if (run != null && !run.isComplete() && !run.isDnf()) {
            visible = true;

            ///qwas run != timingUI.getNextRunToFinish()
        }
        // if (timingUI.canOvertake(run)) {
        //     overTakevisible = true;
        // }
        DNFButton.setVisible(visible);

//20160726        if (Race.getInstance().isPHOTO_EYE_AUTOMATIC()) {/
//            finishButton.setVisible(visible);
//            finishButton.setEnabled(false);


//        } else {
        visible = timingUI.finishButtonShouldBeVisible(run);
        finishButton.setVisible(visible);
//        }


        //overtakeButton.setVisible(overTakevisible);//overTakevisible);
        updateOvertakeButtonVisibility();
    }


    protected void updateOvertakeButtonVisibility() {
        boolean overTakevisible = false;// false;
        if (timingUI.canOvertake(run)) {
            overTakevisible = true;
        }
        overtakeButton.setVisible(overTakevisible);//overTakevisible);


    }


    private void createUIComponents() {
        bibLabel = new BibLabel();
        timerLabel = new JLabel();
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
        subPanel = new JPanel();
        subPanel.setLayout(new FormLayout("fill:105px:noGrow,left:4dlu:noGrow,fill:58px:noGrow,left:5dlu:noGrow,fill:186px:noGrow,left:4dlu:noGrow,fill:142px:noGrow,fill:8px:noGrow,fill:54px:noGrow,left:4dlu:noGrow,fill:88px:noGrow", "center:64px:noGrow"));
        finishButton = new JButton();
        finishButton.setBackground(Color.red);
        finishButton.setMargin(new Insets(0, 4, 0, 4));
        finishButton.setMaximumSize(new Dimension(82, 40));
        finishButton.setMinimumSize(new Dimension(82, 40));
        finishButton.setOpaque(true);
        finishButton.setPreferredSize(new Dimension(82, 40));
        finishButton.setText("Finish");
        finishButton.setToolTipText("Stop timer for this boat");
        CellConstraints cc = new CellConstraints();
        subPanel.add(finishButton, cc.xy(1, 1));
        subPanel.add(timerLabel, cc.xy(7, 1));
        subPanel.add(bibLabel, cc.xy(3, 1));
        DNFButton = new JButton();
        DNFButton.setBackground(Color.yellow);
        DNFButton.setOpaque(true);
        DNFButton.setText("DNF");
        DNFButton.setToolTipText("Mark boat as DID NOT FINISH");
        subPanel.add(DNFButton, cc.xy(9, 1));
        nameLabel = new JLabel();
        subPanel.add(nameLabel, cc.xy(5, 1));
        overtakeButton = new JButton();
        overtakeButton.setBackground(new Color(-52225));
        overtakeButton.setOpaque(true);
        overtakeButton.setText("Overtake");
        subPanel.add(overtakeButton, cc.xy(11, 1));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return subPanel;
    }
}
