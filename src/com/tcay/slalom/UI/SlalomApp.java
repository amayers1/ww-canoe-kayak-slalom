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


import com.tcay.slalom.UI.PDF.PDF_Results;
import com.tcay.slalom.UI.client.ClientRacePenaltiesUIDynamic;
import com.tcay.slalom.UI.components.StatusBar;
import com.tcay.slalom.UI.http.SlalomResultsHTTP;
import com.tcay.slalom.UI.tables.ResultsTable;
import com.tcay.slalom.UI.tables.ResultsTableSpectator;
import com.tcay.slalom.socket.Client;
import com.tcay.slalom.socket.Proxy;
import com.tcay.util.Log;
import com.tcay.slalom.*;
import com.tcay.slalom.socket.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * Teton Cay Group, Inc.
 * User: allen
 * Date: Aug 27, 2013
 * Time: 12:04:53 PM
 */


/**
 * The main entry point for the application  Create the GUI and show it.
 * <p/>
 * Need "sudo launchctl load -w /System/Library/LaunchDaemons/org.apache.httpd.plist" to start Apache on OSX Yosemite
 * <p/>
 * <p/>
 * TagHeuer interface works by matching a manually verified start (via tha manual start button oin the App) to a
 * CP5xx device bib number.   Currently if using the TagHeuer interface, you must keep the CP 5xx bib numbers
 * correct and select  the Applications starting racer by "Start" button.
 * <p/>
 * An enhancement is to use a "Eyes Only" mode, where the app accepts inputs from the photo cells only and doesn't
 * require any input to be done on the the TagHeuer CP 5xx.
 * <p/>
 * <p/>
 * switch clocks (started wrong racer)
 * Enhancement List from 11/23/2014 charlotte Race
 * <p/>
 * <p/>
 * penalty screen not showing ups as red, or configuration not holding (Charlotte 1123)
 * <p/>
 * need a (MISSED TIME/Need Manual Time) button
 * <p/>
 * modification of time to input stopwatch time
 * <p/>
 * DONE leading spaces on bib nor need trim() or proper strtok
 * <p/>
 * Clock delays - with 5 socket client judging stations
 * <p/>
 * Cant change configuration after start (upstreams)
 * <p/>
 * Debugger runs faster than Run, TH only recognized in Debug ???
 * <p/>
 * Add racer on the fly
 * <p/>
 * Can’t skip 1st runs - Null pointer on run1.xxx
 * <p/>
 * Bigger Stop button
 * <p/>
 * Bigger Start Button
 * <p/>
 * <p/>
 * <p/>
 * <p/>
 * <p/>
 * How to Use the TagHeure Interface:
 * Reset TagHeuer CP 500 by Clearing memory at the start of a race
 * Change Run number after first runs onn CP 520 ... App will automatically ask for 2nd runs after all 1st
 * runs from start list are depleted.
 */
public class SlalomApp {


    //http://www.loc.gov/standards/iso639-2/php/code_list.php Country Codes


    public static final Color RED = new Color(255, 0, 0);
    public static final Color LIGHT_RED = new Color(255, 204, 204);
    public static final Color LIGHT_GREEN = new Color(153, 255, 153);
    public static final Color LIGHT_YELLOW = new Color(255, 255, 153);
    public static final Color LIGHT_CYAN = new Color(153, 255, 255);
    public static final Color ULTRA_LIGHT_CYAN = new Color(204, 255, 255);
    public static final Color ULTRA_LIGHT_GREEN = new Color(102, 255, 102);
    public static final Color ULTRA_LIGHT_RED = new Color(255, 230, 230);
    public static final Color BLUE = new Color(0, 0, 255);
    static JLabel label = null;
    private static SlalomApp instance = null;
    JPanel mainPanel = new JPanel();
    private JFrame timingFrame = null;
    private JFrame timingFrame1 = null;
    private Log log;
    private Race race;
    private JFrame appFrame;
    private SlalomResultsHTTP slalomHTTP;





    //Constructor must be protected or private to prevent creating new object
    protected SlalomApp() {

        // Sample German - Locale.setDefault(new Locale("de", "DE"));

        log = Log.getInstance();
        race = Race.getInstance();
        race.loadSerializedData();
        race.maybeStartPhotoCellInterface();
        slalomHTTP = new SlalomResultsHTTP();
        log.setCurrentLevel(Log.LOG_DEBUG);//;INFO);
        Server soss = new Server();
        new Thread(soss).start();
    }

    public /*rivate*/  static SlalomApp getInstance() {

        if (instance == null)
            instance = new SlalomApp();
        return instance;
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        System.out.println("Loading");
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                 getInstance().createAndShowGUI();
            }
        });
    }

    private void createAndShowGUI() {


        //Create and set up the window.
        appFrame = new JFrame("Slalom Main Menu Frame");
        appFrame.setLayout(new BorderLayout());
        appFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        appFrame.setTitle("Slalom Race Organizer's Application");
//TODO CUSTOMIZE 2016 RIO
        //JLabel picLabel = new JLabel(Race.getInstance().getSlalomBackgroundII());
        JLabel picLabel = new JLabel(Race.getInstance().getRaceBackgroundII());

        appFrame.add(picLabel, BorderLayout.CENTER);
        appFrame.setSize(760, 340);
        appFrame.setPreferredSize(new Dimension(760, 340));

        setLocationRightTop(appFrame);
        JMenuBar menuBar;
        JMenu menu;
        JMenuItem menuItem;

        //Create the menu bar.
        menuBar = new JMenuBar();

        //Build the Race Setup Menu
        menu = new JMenu("Setup/Configuration");
        menu.getAccessibleContext().setAccessibleDescription(
                "Race Configuration, Registration, Scoring, Results, etc.");
        menuBar.add(menu);

        //
        menuItem = new JMenuItem("Race Configuration / Setup");
        menuItem.getAccessibleContext().setAccessibleDescription(
                "TBD");
        menu.add(menuItem);

        menuItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JFrame frame = new JFrame("Race Configuration");

                        frame.setContentPane(new RaceConfigUI().$$$getRootComponent$$$());
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.pack();
                        frame.setVisible(true);
                    }
                }
        );


        menuItem = new JMenuItem("Select Race to Load");
        menu.add(menuItem);
        menuItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        log.info("Race Selection");
                        JFrame frame = new JFrame("Race Selector");
                        frame.setContentPane(new RaceSelector().$$$getRootComponent$$$());
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.pack();
                        frame.setVisible(true);


                    }
                }
        );


        menuItem = new JMenuItem("Load Sample Race Data");
        menu.add(menuItem);
        menuItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        new TestData().load();
                    }
                }
        );


        menuItem = new JMenuItem("Load SDF Racers from NRC file import.txt");
        menu.add(menuItem);
        menuItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {


//                        NRC_StartListImporter2016August importer = new NRC_StartListImporter2016August();
                        NRCStartListImporter importer = new NRCStartListImporter();
                        importer.readImportFile();
                        importer.readImportFile();
                    }
                }
        );

        menuItem = new JMenuItem("Load CSV Racers from USNWC file USNWC.csv");
        menu.add(menuItem);
        menuItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {


                        USNWC_StartListImporter importer = new USNWC_StartListImporter();
                        importer.readImportFile();
                    }
                }
        );




        // Classes Config
        menuItem = new JMenuItem("TBD - Boat Classes Configuration", race.getKayakSmall());
        menu.add(menuItem);

        // Build the Registration Menu
        menu = new JMenu("Registration");
        menu.getAccessibleContext().setAccessibleDescription(
                "Register Racers");
        menuBar.add(menu);

        menuItem = new JMenuItem("TBD - Register Racer", race.getRacerImg());
        menu.add(menuItem);
        menuItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JFrame frame = new JFrame("Register Racer WIP");
                        frame.setContentPane(new RegisterRacer().$$$getRootComponent$$$());
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.pack();
                        frame.setVisible(true);
                    }
                }
        );

        menuItem = new JMenuItem("TBD - Assign Bibs", race.getRaceBibSmall());
        menu.add(menuItem);

        // Build the Scoring/Penalties Menu
        menu = new JMenu("Section Scoring");
        menu.getAccessibleContext().setAccessibleDescription("This menu does penalty scoring");
        menuBar.add(menu);


        menuItem = new JMenuItem("Section Penalty Scoring");
        menu.add(menuItem);
        menuItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        menuSectionScoringAction();
                    }
                }
        );


        menuItem = new JMenuItem("Section Penalty Scoring - OBSOLETE all in 1 window");
        menu.add(menuItem);
        menuItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        Proxy proxy = null;
                        //try {
                        proxy = new Proxy(new Client());
                        //} catch (InvalidArgumentException e1) {
                        //    e1.printStackTrace();
                        //}

                        JFrame frame = new JFrame("Race Penalty Scoring");
                        frame.setContentPane(new ClientRacePenaltiesUIDynamic(0, proxy).getRootComponent());
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.pack();
                        frame.setVisible(true);
                    }
                }
        );


        menuItem = new JMenuItem("Section Penalty Scoring - OBSOLETE Old UI");
        menu.add(menuItem);
        menuItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JFrame frame = new JFrame("Race Penalty Scoring (Radio Buttons)");
                        frame.setContentPane(new RacePenaltiesUIDynamicOrig().getRootComponent());
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.pack();
                        frame.setVisible(true);
                    }
                }
        );


        // Build the Timing Menu
        menu = new JMenu("Timing");
        menu.getAccessibleContext().setAccessibleDescription(
                "Race Run Start and Finish Timing");
        menuBar.add(menu);


        menuItem = new JMenuItem("Race Start/Stop Timing");
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Start and Finish Race Runs - Timers");
        menu.add(menuItem);

        menuItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        createAndDisplayTimingPanel();
                    }
                }
        );


        menu = new JMenu("Results");
        menuBar.add(menu);




        //
        menuItem = new JMenuItem("Mark Runs");
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Mark Runs");
        menu.add(menuItem);

        menuItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {

                        MarkRunStatus markRunsStatus = new MarkRunStatus();
                        JFrame frame = new JFrame("Mark Runs Status");
                        frame.setContentPane(markRunsStatus.$$$getRootComponent$$$());//.$$$getRootComponent$$$());
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.pack();
                        frame.setVisible(true);
                        //Race.getInstance().getClassesRuns();
                    }
                });

        //


        menuItem = new JMenuItem("Leaderboard");
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Results dump to console");
        menu.add(menuItem);

        menuItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        Race race = Race.getInstance();

                        //outputResults("", race.getCompletedRuns(), false);
                        //outputResults("Sorted", race.getCompletedRunsByClassTime(), true);
                        JFrame frame = new JFrame("Leader Board");
                        LeaderBoard leaderBoard = new LeaderBoard(null, frame);

                        frame.setContentPane(leaderBoard.$$$getRootComponent$$$());
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.pack();
                        frame.setVisible(true);

                    }
                }
        );

        menuItem = new JMenuItem("Spectator Leaderboard");
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Results dump to console");
        menu.add(menuItem);

        menuItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        Race race = Race.getInstance();
                        // are the next lines needed, other than forcing the sort, it too shuld not be needed
                        //outputResults("", race.getCompletedRuns(), false);
                        //outputResults("Sorted", race.getCompletedRunsByClassTime(), true);
                        ResultsTable rt = new ResultsTableSpectator();
                        JFrame frame = new JFrame("Spectator Leaderboard");
                        LeaderBoard leaderBoard = new LeaderBoard(rt, frame);
                        ((ResultsTableSpectator) rt).removeDetailColumns();

                        frame.setContentPane(leaderBoard.$$$getRootComponent$$$());
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.pack();
                        frame.setVisible(true);
                    }
                }
        );


        menuItem = new JMenuItem("Scrolling Scoreboard");
        menu.add(menuItem);
        menuItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        menuScrollingScoreBoardAction();
                    }
                }
        );


        menuItem = new JMenuItem("Penalties - Virtual Scoring Sheet");
        menuItem.getAccessibleContext().setAccessibleDescription(
                "ZZZZ Results dump to console");
        menu.add(menuItem);

        menuItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        menuVirtualScoringSheetAction();
                    }
                }
        );


        menuItem = new JMenuItem("Dump results to file");
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Results dump to file SlalomTestResults.txt");
        menu.add(menuItem);

        menuItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        outputResults("Sorted", race.getCompletedRunsByClassTime(), true);
                    }
                }
        );




        menuItem = new JMenuItem("Dump results to PDF file");
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Results dump to file SlalomTestResults.pdf");
        menu.add(menuItem);

        menuItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        outputResultsPDF("Sorted", race.getCompletedRunsByClassTime(), true);
                    }
                }
        );


        menuItem = new JMenuItem("Dump results to CSV file");
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Results dump to file Results.CSV");
        menu.add(menuItem);

        menuItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        outputCSVResults("Sorted", race.getCompletedRunsByClassTime(), true);
                    }
                }
        );


        menuItem = new JMenuItem("Dump results to WebServer file");
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Results dump to Web");
        menu.add(menuItem);

        menuItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {

                        Race.getInstance().printResultsHTTP(null, 0);
                        //slalomHTTP.outputWeb("Sorted", race.getCompletedRunsByClassTime(), true);
                    }
                }
        );


        appFrame.setJMenuBar(menuBar);

        JPanel statusBar = new StatusBar().getPanel(appFrame);
        appFrame.add(statusBar, BorderLayout.SOUTH);

        //Display the window.
        appFrame.pack();
        appFrame.setVisible(true);

    }


    public ArrayList<Proxy> menuSectionScoringAction() {
        ArrayList<Proxy> proxies = new ArrayList<Proxy>();
        Proxy proxy = null;
        // todo Fix this kludge to get JPanel for Dialog Box
        JFrame f = new JFrame("Dummy");

        JPanel jp;
        f.add(jp = new JPanel() {

            @Override // placeholder for actual content
            public Dimension getPreferredSize() {
                return new Dimension(320, 240);
            }

        });

        if (Race.getInstance().getNbrOfSections() == 0) {
            Race.getInstance().askSampleDataIfNoRacersExist(jp);
        }

        String title;
        int nbrSections = Race.getInstance().getNbrOfSections();
        for (int i = 0; i < nbrSections; i++) {
            title = "Section " + (i + 1) + " Judge";

            //JFrame
            f = new JFrame(title + " - " + Race.getInstance().getName());
            f.add(new JPanel() {

                @Override // placeholder for actual content
                public Dimension getPreferredSize() {
                    return new Dimension(320, 240);
                }

            });
            //Proxy
            proxy = null;
            //try {
            proxy = new Proxy(new Client());
            proxies.add(proxy);
            // } catch (InvalidArgumentException e1) {
            //     e1.printStackTrace();
            // }

            //frame.setContentPane(new ClientRacePenaltiesUIDynamic(i+1).getRootComponent());
            f.setContentPane(new ClientRacePenaltiesUIDynamic(i + 1, proxy).getRootComponent());// todo very through this call in race simulati
            f.pack();
            f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);//JFrame.EXIT_ON_CLOSE);         // todo unhide, etc
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
            Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
            int x = (int) rect.getMaxX() - ((Race.getInstance().getNbrOfSections() - i) * f.getWidth());
            int y = (int) rect.getMaxY() - f.getHeight();
            f.setLocation(x, y);
            f.setVisible(true);
//
        }
        return (proxies);
    }

    ;

    public RaceTimingUI createAndDisplayTimingPanel() {

        RaceTimingUI timingUI = RaceTimingUI.getInstance();

        if (timingFrame1 == null) {                  // fixme ALL Frames except Scoring should be handled like this - singleton
            timingFrame1 = new JFrame();
            timingFrame1.setLayout(new BorderLayout());
            timingFrame1.add(timingUI.$$$getRootComponent$$$(), BorderLayout.NORTH);

            JPanel statusBar = new StatusBar().getPanel(timingFrame1);
            timingFrame1.add(statusBar, BorderLayout.SOUTH);
            timingFrame1.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            timingFrame1.pack();
        }

        timingUI.setTitle(timingFrame1);
        timingFrame1.setVisible(true);
        return (timingUI);

    }


    private void outputResultsPDF(String title, ArrayList<RaceRun> runs, boolean breakOnClassChange) {
        log.info("\n" + title + " ResultsPDF");

        PDF_Results pdf_results = new PDF_Results();
        pdf_results.doit( title, runs, breakOnClassChange);


    }





    private void outputResults(String title, ArrayList<RaceRun> runs, boolean breakOnClassChange) {
        log.info("\n" + title + " Results");
        String lastBoatClass = null;
        for (RaceRun r : runs) {


            float totalTime;

            if (breakOnClassChange) {
                if (lastBoatClass != null) {
                    if (lastBoatClass.compareTo(r.getBoat().getBoatClass()) != 0)
                        log.info("---");
                }
                lastBoatClass = r.getBoat().getBoatClass();
            }
            totalTime = (float) r.getTotalPenalties();
            totalTime += r.getElapsed();

            log.info(String.format("Run# %1s ", r.getRunNumber()) +
                    String.format("%3s ", r.getBoat().getBoatClass()) +
                    String.format("%-25s ", r.getBoat().getRacer().getShortName()) +
                    String.format("%7s ", r.getResultString()) +
                    String.format("+%-3s ", r.getTotalPenalties()) +
                    String.format("%8.2f", totalTime));

            log.info("");
        }

        BufferedWriter output = null;
        try {
            File file = new File("SlalomTestResults.txt");
            output = new BufferedWriter(new FileWriter(file));

            ArrayList<Result> sorted = Race.getInstance().getTempResults();
            for (Result r : sorted) {
                if (true || breakOnClassChange) {     //Fixme  constant true
                    if (lastBoatClass != null) {
                        if (lastBoatClass.compareTo(r.getBoat().getBoatClass()) != 0)
                            output.newLine();
                    }
                    if (lastBoatClass == null || lastBoatClass.compareTo(r.getBoat().getBoatClass()) != 0) {


                        output.write(r.getBoat().getBoatClass());
                        output.newLine();
                    }
                    lastBoatClass = r.getBoat().getBoatClass();
                }


//                output.write(r.getBoat().getBoatClass() + ", ");
                String s1;

                s1 = String.format("%1$3s", r.getBoat().getRacer().getBibNumber());
                output.write(s1 + ", ");
                String s = r.getBoat().getRacer().getShortName();
                s1 = String.format("%1$-15s", s);
                output.write(s1 + ", ");


                output.write(" run1=");
                s1 = r.getRun1() != null ? r.getRun1().formatTimeExpanded() : RaceRun.TIME_EXPANDED_FILL;
                output.write(s1);

                output.write(" run2=");
                s1 = r.getRun2() != null ? r.getRun2().formatTimeExpanded() : RaceRun.TIME_EXPANDED_FILL;
                output.write(s1);
                /// todo must integrate TH results before sort  - DONE VERIFY 10/11/2013
                output.write("   best=");
                s1 = r.getBestRun() != null ? r.getBestRun().formatTimeTotalOnly() : RaceRun.TIME_ONLY_FILL;
                output.write(s1);


                if (r.getBestRun().getPhotoCellRaceRun() != null) {    /// todo must integrate TH results before sort
                    output.write(ResultsTable.TIMINGMODE_AUTOMATIC);
                }
                output.newLine();
            }

            output.write("\n\n\nlegend " + ResultsTable.TIMINGMODE_ADJUSTED + "=Corrected  " +
                                           ResultsTable.TIMINGMODE_AUTOMATIC + "=photoEye  " +
                                           ResultsTable.TIMINGMODE_MANUAL + "=Manual (virtual stopwatch)");
;
            output.close();
        } catch (Exception e) {
            log.write(e);
        } finally {
            try {
                output.close();
            } catch (Exception ex) {
            }
        }

    }




    private void outputCSVResults(String title, ArrayList<RaceRun> runs, boolean breakOnClassChange) {
        log.info("\n" + title + "CSV Results");
        String lastBoatClass = null;
        for (RaceRun r : runs) {


            float totalTime;

            if (breakOnClassChange) {
                if (lastBoatClass != null) {
//                    if (lastBoatClass.compareTo(r.getBoat().getBoatClass()) != 0)
//                        log.info("----");
                }
                lastBoatClass = r.getBoat().getBoatClass();
            }
            totalTime = (float) r.getTotalPenalties();
            totalTime += r.getElapsed();

//            log.info(String.format("Run# %1s ", r.getRunNumber()) +
//                    String.format("%3s ", r.getBoat().getBoatClass()) +
//                    String.format("%-25s ", r.getBoat().getRacer().getShortName()) +
//                    String.format("%7s ", r.getResultString()) +
//                    String.format("+%-3s ", r.getTotalPenalties()) +
//                    String.format("%8.2f", totalTime));

//            log.info("");
        }

        BufferedWriter output = null;
        try {
            File file = new File(race.getName() + ".CSV");
            output = new BufferedWriter(new FileWriter(file));


            //////
            output.write("Bib,");
            output.write("Name,");
            output.write("Run 1 raw, penalties, total,");
            output.write("Run 2 raw, penalties, total,");
            /// todo must integrate TH results before sort  - DONE VERIFY 10/11/2013
            output.write("Best");
            output.newLine();

            ///////


            ArrayList<Result> sorted = Race.getInstance().getTempResults();
            for (Result r : sorted) {
                if (true || breakOnClassChange) {     //Fixme  constant true
                    if (lastBoatClass != null) {
                        if (lastBoatClass.compareTo(r.getBoat().getBoatClass()) != 0)
                            output.newLine();
                    }
                    if (lastBoatClass == null || lastBoatClass.compareTo(r.getBoat().getBoatClass()) != 0) {


                        output.write(r.getBoat().getBoatClass());
                        output.newLine();
                    }
                    lastBoatClass = r.getBoat().getBoatClass();
                }


//                output.write(r.getBoat().getBoatClass() + ", ");
                String s1;

                s1 = r.getBoat().getRacer().getBibNumber();
                output.write(s1 + ", ");
                s1 = r.getBoat().getRacer().getShortName();
                output.write(s1 + ", ");


//                output.write( " run1=");
                //s1 = r.getRun1()!=null?r.getRun1().formatTimeExpanded():RaceRun.TIME_EXPANDED_FILL;


                //
                StringBuffer sb = new StringBuffer();

                if (r.getRun1() != null) {
                    sb.append(r.getRun1().getResultString());
                    sb.append(",");
                    sb.append(r.getRun1().getPenaltyString());
                    sb.append(",");
                    sb.append(r.getRun1().getTotalTimeString());
                    sb.append(",");
                } else sb.append(",,,");

                if (r.getRun2() != null) {
                    sb.append(r.getRun2().getResultString());
                    sb.append(",");
                    sb.append(r.getRun2().getPenaltyString());
                    sb.append(",");
                    sb.append(r.getRun2().getTotalTimeString());
                    sb.append(",");
                } else sb.append(",,,");


                //


                output.write(sb.toString());//s1);

                //output.write( " run2=");
                //s1 = r.getRun2()!=null?r.getRun2().formatTimeExpanded():RaceRun.TIME_EXPANDED_FILL;
                //output.write(s1);
                /// todo must integrate TH results before sort  - DONE VERIFY 10/11/2013
                //output.write(        "   best=");
                s1 = r.getBestRun() != null ? r.getBestRun().formatTimeTotalOnly() : RaceRun.TIME_ONLY_FILL;
                output.write(s1);

// C20170416 (ajm)  Remove 'e' from Best Time Output
         //       if (r.getBestRun().getPhotoCellRaceRun() != null) {    /// todo must integrate TH results before sort
         //           output.write(ResultsTable.TIMINGMODE_AUTOMATIC);
         //       }
                output.newLine();
            }

            output.close();
        } catch (Exception e) {
            log.write(e);
        } finally {
            try {
                output.close();
            } catch (Exception ex) {
            }
        }
    }

    public void menuScrollingScoreBoardAction() {
        Race race = Race.getInstance();

//        outputResults("", race.getCompletedRuns(), false);
//        outputResults("Sorted", race.getCompletedRunsByClassTime(), true);
        LeaderBoardScroll leaderBoard = new LeaderBoardScroll();
        JFrame frame = new JFrame();
        frame.setTitle("Scrolling Scoreboard");
        frame.setContentPane(leaderBoard.$$$getRootComponent$$$());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(600, 300));
        frame.setSize(600, 300);
        setLocationRightTop(frame);

        frame.pack();
        frame.setVisible(true);
    }


    public void menuVirtualScoringSheetAction() {
        Race race = Race.getInstance();

//        outputResults("", race.getCompletedRuns(), false);
        //       outputResults("Sorted", race.getCompletedRunsByClassTime(), true);
        ScoringBoard scoringBoard = new ScoringBoard();

        JFrame frame = new JFrame();
        frame.setTitle("ScoringSheet");
        frame.setContentPane(scoringBoard.$$$getRootComponent$$$());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.setPreferredSize(new Dimension(700, 350));
        frame.setSize(700, 350);

        setLocationBottomLeft(frame);
        //appFrame.setContentPane(scoringBoard.$$$getRootComponent$$$());
        //app
        frame.pack();
        //app
        frame.setVisible(true);

    }

    private void setLocationRightTop(JFrame frame) {
        // set location to middle of screen
        frame.setLocationRelativeTo(null);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
        int x = (int) (rect.getMaxX() - frame.getWidth());
        int y = 0;//(int) rect.getMaxY() - appFrame.getHeight();
        System.out.println(frame.getTitle() + " width=" + frame.getWidth() + " Putting Window RightTop at " + x + ", " + y);
        log.warn(frame.getTitle() + " width=" + frame.getWidth() + " Putting Window RightTop at " + x + ", " + y);


        frame.setLocation(x, y);


    }

    private void setLocationBottomLeft(JFrame frame) {
        // set location to middle of screen
        frame.setLocationRelativeTo(null);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();

        int x = 0;//(int) (rect.getMaxX() - frame.getWidth());
        int y = (int) rect.getMaxY() - frame.getHeight();
        System.out.println(frame.getTitle() + " width=" + frame.getWidth() + " Putting Window BottomLeft at " + x + ", " + y);
        log.warn(frame.getTitle() + " width=" + frame.getWidth() + " Putting Window BottomLeft at " + x + ", " + y);
        frame.setLocation(x, y);
    }


}
