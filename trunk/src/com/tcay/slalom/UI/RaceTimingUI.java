package com.tcay.slalom.UI;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import com.tcay.slalom.tagHeuer.TagHeuerRaceRun;
import com.tcay.util.Log;
import com.tcay.slalom.BoatEntry;
import com.tcay.slalom.Race;
import com.tcay.slalom.RaceRun;
import com.tcay.slalom.UI.components.BibLabel;
//import com.tcay.slalom.UI.components.JButtonFinishRaceRun;
import java.util.List;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 10/29/13
 * Time: 7:38 PM
 *
 * @UserDoc The Timing Screen displays has 3 sections:
 * <p/>
 * Start List
 * Staring Block
 * Finish Line
 */
public class RaceTimingUI {

    // Panels on this screen
    private JPanel mainPanel;
    private JPanel selectRacerPanel;
    private JPanel startPanel;
    private JPanel finishPanel;
    private JPanel statusBarPanel;

    private JPanel innerFinishPanel1;
    private JPanel innerFinishPanel2;
    private JPanel innerFinishPanel3;


    // controls on Start List panel
    private JComboBox<BoatEntry> startListComboBox;
    private JButton startListSelectRacerReadyButton;
    private JButton reRunButton;
    private JButton newRunButton;
    // This is a spacer to prevent layout manager from resizing Start List panel
    // when we change the "Waiting for a Finish or DNF" label's visiblity
    // DO NOT remove unless you are sure you know what you are doing
    private JLabel spacerForLayoutManager;


    // controls on Starting Block panel
    private JLabel jRacerInStartGateLabel;
    private JButton startButton;
    private JButton DNSButton;

    // controls on Finish Line panel
    private RaceTimingBoatOnCourseUI racer1UI;
    private RaceTimingBoatOnCourseUI racer2UI;
    private RaceTimingBoatOnCourseUI racer3UI;
    private RaceRun run1 = null;
    private RaceRun run2 = null;
    private RaceRun run3 = null;

    private JLabel waitingForAFinishLabel;
    private JLabel bibLabel;

    // State members
    private BoatEntry boatReadyToStart = null;
    private RaceRun rerunPending = null;
    private Race race;

    private static RaceTimingUI instance;
    private Log log;

    public synchronized static RaceTimingUI getInstance() {
        if (instance == null) {
            instance = new RaceTimingUI();
        }
        return instance;
    }

    /**
     *
     */
    private RaceTimingUI() {
        log = Log.getInstance();
        race = Race.getInstance();
        $$$setupUI$$$();
        loadStartList();

        startListSelectRacerReadyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                nextRacerToStartingBlock(); // todo BUG when LIST is Empty at end !!!
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (boatReadyToStart != null) {
                    RaceRun run;
                    if (rerunPending != null) {
                        run = rerunPending;
                        rerunPending = null;
                    } else {
                        startListComboBox.removeItem(boatReadyToStart);

                        // select the first entry left in the start list, this should be the
                        // next racer scheduled to start
                        if (startListComboBox.getItemCount() > 0) {
                            startListComboBox.setSelectedIndex(0);
                        }

                        // in a rerun, need to NOT have this  todo, what did I mean with this comment
                        run = boatReadyToStart.onDeck();   //todo this method needs to be evaluated, instantiates a run .. even if canceled from UI
                    }
                    run.start();
                    // fixme
                    maybeMakeDUMMYTagHeuerTime(run);


                    shuffleActiveRuns(run);

                    racer1UI.updateRun(run1);
                    if (run2 != null) {
                        racer2UI.updateRun(run2);

                    }
                    if (run3 != null) {
                        racer3UI.updateRun(run3);
                    }

                    boatReadyToStart = null;
                    updateButtonVisibility();
                }
            }
        });

        DNSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (boatReadyToStart != null) {
                    boatReadyToStart.didDNS();
                    startListComboBox.removeItem(boatReadyToStart);
                    if (startListComboBox.getItemCount() > 0)
                        startListComboBox.setSelectedIndex(0);

                    boatReadyToStart = null;
                    updateButtonVisibility();
                }
                //To change body of implemented methods use File | Settings | File Templates.

            }
        });


        newRunButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {


                int n = JOptionPane.showConfirmDialog(
                        mainPanel,
                        "Set Second Runs !  Are you sure ALL first runs are complete ?",
                        "Confirm Second Runs",
                        JOptionPane.YES_NO_OPTION);

                if (n == 0) {
                    Race.getInstance().incrementCurrentRunIteration();
                    updateWhichRunLabel();
                    loadStartList();
                }

            }
        });

        reRunButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                // Create Dialog Box
                // Select Race Run Boat and Run Number for redo
                SelectRerun doRerun = new SelectRerun();
                doRerun.pack();
                doRerun.setVisible(true);

                RaceRun rerunPending = Race.getInstance().getPendingRerun();


                try {
                    if (rerunPending != null) {
                        boatReadyToStart = rerunPending.getBoat();
//                    jRacerInStartGateLabel.setText(boatReadyToStart.getRacer().getShortName());
                        jRacerInStartGateLabel.setText(boatReadyToStart.toString());
                        jRacerInStartGateLabel.setIcon(boatReadyToStart.getImageIcon());
                        //  run = boatReadyToStart.onDeck();   //todo this method needs to be evaluated, instantiates a run .. even if canceled from UI
                        //  run.start();
                    }
                } catch (Exception e) {
                    log.write(e);
                }
                updateButtonVisibility();
            }
        });
        updateButtonVisibility();
    }


    private void createUIComponents() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        race.askSampleDataIfNoRacersExist(mainPanel);

        // setup start list panel
        startListComboBox = new JComboBox();

        // starting block panel
        bibLabel = new BibLabel();

        Timer screenUpdatetimer = new Timer(100,
                new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        updateRunTimers();
                    }
                });
        screenUpdatetimer.setInitialDelay(500);
        screenUpdatetimer.start();

        racer1UI = new RaceTimingBoatOnCourseUI(this);
        racer2UI = new RaceTimingBoatOnCourseUI(this);
        racer3UI = new RaceTimingBoatOnCourseUI(this);

        innerFinishPanel1 = (JPanel) racer1UI.$$$getRootComponent$$$();
        innerFinishPanel2 = (JPanel) racer2UI.$$$getRootComponent$$$();
        innerFinishPanel3 = (JPanel) racer3UI.$$$getRootComponent$$$();
    }

    ///fixme
    private void maybeMakeDUMMYTagHeuerTime(RaceRun run) {
        // dummy up
        if (Race.getInstance().isTagHeuerEmulation()) {   //fixme TEMPORARY DEMO
            if (Math.random() * 20 < 16) {
                run.setTagHeuerRaceRun(new TagHeuerRaceRun(race.getCurrentRunIteration()));
            }
        }

    }


    private void updateRunTimers() {
        if (run1 != null) {
            racer1UI.updateTimer();
            if (run2 != null) {
                racer2UI.updateTimer();

                if (run3 != null) {
                    racer3UI.updateTimer();
                }
            }
        }
    }


    private void loadStartList() {
        List<BoatEntry> startList = Race.getInstance().getRemainingStartList();

        for (BoatEntry be : startList) {
            if (Race.getInstance().getExistingRun(be) == null) {
                startListComboBox.addItem(be);
            }
        }

    }

    private void nextRacerToStartingBlock() {
        try {
            boatReadyToStart = (BoatEntry) startListComboBox.getSelectedItem();

            startListComboBox.invalidate();  //todo need this ?
            if (boatReadyToStart != null) {
                jRacerInStartGateLabel.setText(boatReadyToStart.toString());                 // NUll pointer Exception
                jRacerInStartGateLabel.setIcon(boatReadyToStart.getImageIcon());
                bibLabel.setText(boatReadyToStart.getRacer().getBibNumber());
            } else {
                jRacerInStartGateLabel.setText("");
                bibLabel.setText("");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
// D131029       jRacerInStartGateLabel.repaint();
        updateButtonVisibility();
    }


    protected void updateButtonVisibility() {
        //racer1UI.updateButtonVisibility();
        //racer2UI.updateButtonVisibility();
        //racer3UI.updateButtonVisibility();

        startButton.setVisible(false);
        reRunButton.setVisible(false);

        waitingForAFinishLabel.setVisible(false);

        if (boatReadyToStart == null && startListComboBox.getItemCount() > 0) {
            nextRacerToStartingBlock();
        }


        if (boatReadyToStart == null) {
            jRacerInStartGateLabel.setText("no one");
            bibLabel.setText("");
            DNSButton.setVisible(false);

        } else {
            DNSButton.setVisible(true);
            if (canDisplayAnotherRealtimeRun()) {
                reRunButton.setVisible(true);
                startButton.setVisible(true);

            } else {
                waitingForAFinishLabel.setVisible(true);
            }
        }

        //jRacerInStartGateLabel.repaint();

        // doesn't work here,needed before frame setVisible and after pack()
        // startButton.requestFocusInWindow();
    }


    private boolean canBump(RaceRun run) {
        boolean bump = false;
        if (run == null || run.isComplete() || run.isDnf()) {
            bump = true;
        }
        return bump;
    }


    private boolean canDisplayAnotherRealtimeRun() {
        boolean canDisplay = false;
        if (canBump(run1) || canBump(run2) || canBump(run3))
            canDisplay = true;
        return canDisplay;
    }

    private void shuffleActiveRuns(RaceRun run) {

        if (canBump(run3)) {       // todo BUG here in overtake situation, wrong run removed from screen
            run3 = run2;
            run2 = run1;
            run1 = run;
        } else {
            if (canBump(run2)) {       // todo BUG here in overtake situation, wrong run removed from screen
                run2 = run1;
                run1 = run;
            } else if (canBump(run1)) {
                run1 = run;
            } else {
                log.warn("Can't bump any run from screen");
            }
        }
    }


    /**
     * @param frame
     */
    public void setTitle(JFrame frame) {
        String name = Race.getInstance().getName();
        frame.setTitle("Timing - " + Race.getInstance().getRunTitle() + " " + name);
    }

    /*
        private void addFinishButtonListeners() {

            racer1FinishButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if (run1 != null) {
                        run1.finish();
                    }
                    updateButtonVisibility();

                }
            });
        }
    */
    private void updateWhichRunLabel() {
        JFrame frame = (JFrame) SwingUtilities.getRoot(jRacerInStartGateLabel);
        setTitle(frame);
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
        mainPanel.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        selectRacerPanel = new JPanel();
        selectRacerPanel.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:267px:noGrow,left:12dlu:noGrow,fill:max(d;4px):noGrow,left:51dlu:noGrow,fill:85px:noGrow", "top:29px:grow,top:6dlu:noGrow,center:32px:noGrow"));
        selectRacerPanel.setToolTipText("Start list contains all boats that are registered and have not yet compoleted the current run in progress");
        mainPanel.add(selectRacerPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(658, 84), null, 0, false));
        selectRacerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Start List"));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        CellConstraints cc = new CellConstraints();
        selectRacerPanel.add(panel1, cc.xy(7, 1));
        newRunButton = new JButton();
        newRunButton.setBackground(Color.pink);
        newRunButton.setOpaque(true);
        newRunButton.setText("New Run");
        newRunButton.setToolTipText("Select next run e.g. second runs");
        panel1.add(newRunButton, BorderLayout.NORTH);
        reRunButton = new JButton();
        reRunButton.setBackground(Color.pink);
        reRunButton.setOpaque(true);
        reRunButton.setText("Re-Run");
        reRunButton.setToolTipText("Select a boat to do a Re-run, this Re-Run will replace their existing run ");
        selectRacerPanel.add(reRunButton, cc.xy(7, 3));
        startListSelectRacerReadyButton = new JButton();
        startListSelectRacerReadyButton.setBackground(Color.green);
        startListSelectRacerReadyButton.setOpaque(true);
        startListSelectRacerReadyButton.setText("Select Racer");
        startListSelectRacerReadyButton.setToolTipText("put selected boat from the start list into the starting block (replaces boat currently in starting block)");
        startListSelectRacerReadyButton.setVerticalAlignment(3);
        selectRacerPanel.add(startListSelectRacerReadyButton, cc.xy(5, 3, CellConstraints.DEFAULT, CellConstraints.BOTTOM));
        startListComboBox.setBackground(Color.green);
        startListComboBox.setOpaque(true);
        startListComboBox.setToolTipText("List of all boats that have not yet started the current run");
        selectRacerPanel.add(startListComboBox, cc.xy(3, 3, CellConstraints.DEFAULT, CellConstraints.BOTTOM));
        final JLabel label1 = new JLabel();
        label1.setText("Boats that have not started this run");
        selectRacerPanel.add(label1, cc.xy(3, 1, CellConstraints.DEFAULT, CellConstraints.BOTTOM));
        startPanel = new JPanel();
        startPanel.setLayout(new FormLayout("fill:53px:noGrow,left:4dlu:noGrow,fill:219px:noGrow,left:12dlu:noGrow,fill:118px:noGrow,left:52dlu:noGrow,fill:85px:noGrow", "center:30px:noGrow,top:7dlu:noGrow,center:max(d;4px):noGrow"));
        startPanel.setToolTipText("Starting block is the boat that is about to begin racing");
        mainPanel.add(startPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        startPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Starting Block"));
        startButton = new JButton();
        startButton.setBackground(Color.green);
        startButton.setOpaque(true);
        startButton.setText("Start");
        startButton.setToolTipText("Start timer for this boat");
        startPanel.add(startButton, cc.xy(5, 1));
        DNSButton = new JButton();
        DNSButton.setBackground(Color.pink);
        DNSButton.setOpaque(true);
        DNSButton.setText("DNS");
        DNSButton.setToolTipText("Mark boat as DID NOT START");
        startPanel.add(DNSButton, cc.xy(7, 1));
        waitingForAFinishLabel = new JLabel();
        waitingForAFinishLabel.setForeground(Color.red);
        waitingForAFinishLabel.setText("Waiting for a finish or DNF");
        startPanel.add(waitingForAFinishLabel, cc.xyw(5, 3, 2));
        jRacerInStartGateLabel = new JLabel();
        startPanel.add(jRacerInStartGateLabel, cc.xy(3, 1));
        startPanel.add(bibLabel, cc.xy(1, 1));
        spacerForLayoutManager = new JLabel();
        startPanel.add(spacerForLayoutManager, cc.xy(3, 3));
        finishPanel = new JPanel();
        finishPanel.setLayout(new FormLayout("fill:134px:noGrow,left:4dlu:noGrow,fill:48px:noGrow,left:8dlu:noGrow,fill:179px:noGrow,left:5dlu:noGrow,fill:117px:noGrow,fill:14px:noGrow,fill:85px:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow"));
        finishPanel.setToolTipText("Finish Line shows all boats currently started and on the course");
        mainPanel.add(finishPanel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(634, 155), null, 0, false));
        finishPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Finish Line"));
        finishPanel.add(innerFinishPanel1, cc.xyw(1, 1, 10));
        finishPanel.add(innerFinishPanel2, cc.xyw(1, 3, 10));
        finishPanel.add(innerFinishPanel3, cc.xyw(1, 5, 10));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        mainPanel.add(spacer2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, 1, null, null, null, 0, false));
        statusBarPanel = new JPanel();
        statusBarPanel.setLayout(new FormLayout("fill:d:grow", "center:d:grow"));
        mainPanel.add(statusBarPanel, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, new Dimension(-1, 20), new Dimension(-1, 20), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
