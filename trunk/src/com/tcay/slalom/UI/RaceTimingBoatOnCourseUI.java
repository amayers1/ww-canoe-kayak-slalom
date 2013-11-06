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
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 10/30/13
 * Time: 7:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class RaceTimingBoatOnCourseUI {
    private JPanel subPanel;
    private RaceRun run;
    private JLabel nameLabel;
    private JLabel timerLabel;
    private JButton finishButton;
    private JButton DNFButton;
    private JLabel bibLabel;

    protected RaceTimingBoatOnCourseUI(RaceTimingUI raceTimingUI) {
        final RaceTimingUI timingUI = raceTimingUI;
        $$$setupUI$$$();

        DNFButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (run != null) {
                    run.dnf();
                }
                updateButtonVisibility();
                timingUI.updateButtonVisibility();
            }
        });
        finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (run != null) {
                    run.finish();
                }
                updateButtonVisibility();
                timingUI.updateButtonVisibility();
            }
        });
        updateButtonVisibility();
    }


    protected void updateTimer() {
        timerLabel.setText(run.getResultString() + " +" + run.getTotalPenalties());
        if (run.getTagHeuerRaceRun() != null) {
            timerLabel.setIcon(Race.getInstance().getTagHeuerII());
        } else {
            timerLabel.setIcon(Race.getInstance().getStopWatchII());
        }
    }

    protected void updateRun(RaceRun run) {
        this.run = run;
        nameLabel.setText(run.getBoat().racerClassClub());
        nameLabel.setIcon(run.getBoat().getImageIcon());
        bibLabel.setText(run.getBoat().getRacer().getBibNumber());
        updateButtonVisibility();
    }

    protected void updateButtonVisibility() {
        boolean visible = false;
        if (run != null && !run.isComplete() && !run.isDnf()) {
            visible = true;
        }
        DNFButton.setVisible(visible);
        finishButton.setVisible(visible);
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
        subPanel.setLayout(new FormLayout("fill:134px:noGrow,left:4dlu:noGrow,fill:48px:noGrow,left:8dlu:noGrow,fill:179px:noGrow,left:5dlu:noGrow,fill:117px:noGrow,fill:14px:noGrow,fill:max(d;4px):noGrow", "center:d:noGrow"));
        finishButton = new JButton();
        finishButton.setBackground(Color.red);
        finishButton.setOpaque(true);
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
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return subPanel;
    }
}
