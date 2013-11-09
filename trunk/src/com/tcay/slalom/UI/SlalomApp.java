package com.tcay.slalom.UI;

import com.tcay.slalom.UI.client.ClientRacePenaltiesUIDynamic;
import com.tcay.slalom.UI.components.StatusBar;
import com.tcay.slalom.UI.tables.ResultsTable;
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
import java.util.Locale;

/**
 * Teton Cay Group, Inc.
 * User: allen
 * Date: Aug 27, 2013
 * Time: 12:04:53 PM
 */



/**
 * The main entry point for the application  Create the GUI and show it.
 *
 */
public class SlalomApp {


    //http://www.loc.gov/standards/iso639-2/php/code_list.php Country Codes


    public static final Color RED = new Color(255,0,0);
    public static final Color LIGHT_RED = new Color(255,204,204);
    public static final Color LIGHT_GREEN = new Color(153,255,153);
    public static final Color LIGHT_YELLOW = new Color(255,255,153);
    public static final Color LIGHT_CYAN = new Color(153,255,255);
    public static final Color ULTRA_LIGHT_CYAN = new Color(204,255,255);
    public static final Color ULTRA_LIGHT_GREEN = new Color(102,255,102);
    public static final Color ULTRA_LIGHT_RED = new Color(255,230,230);
    public static final Color BLUE = new Color(0,0,255);


    private JFrame timingFrame = null;
    private JFrame timingFrame1 = null;


    private static SlalomApp instance = null;
    private Log log;
    private Race race;


    //Constructor must be protected or private to prevent creating new object
    protected SlalomApp() {

        Locale.setDefault(new Locale("de", "DE"));

        log = Log.getInstance();
        race = Race.getInstance();
        race.loadSerializedData();


        Server soss  = new Server();
        new Thread(soss).start();
    }


    private synchronized static SlalomApp getInstance() {
        if (instance==null)
            instance = new SlalomApp();
        return instance;
    }


    static JLabel label = null;
    private JFrame appFrame;
    JPanel mainPanel = new JPanel();

    private void createAndShowGUI() {



        //Create and set up the window.
        appFrame = new JFrame("Slalom Main Menu Frame");
        appFrame.setLayout(new BorderLayout());
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.setTitle("Slalom Race Organizer's Application");
        JLabel picLabel = new JLabel(Race.getInstance().getSlalomBackgroundII());
        appFrame.add(picLabel, BorderLayout.CENTER);
        appFrame.setSize(660, 340);
        appFrame.setPreferredSize(new Dimension(660, 340));
        // set location to middle of screen
        appFrame.setLocationRelativeTo(null);



        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
        int x = (int) (rect.getMaxX() - appFrame.getWidth());
        int y = 0;//(int) rect.getMaxY() - appFrame.getHeight();
        appFrame.setLocation(x, y);

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



        menuItem = new JMenuItem("Load SDF Racers from file import.txt");
        menu.add(menuItem);
        menuItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {


                        NRC_StartListImporter importer = new NRC_StartListImporter();
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
                        // todo Fix this kludge to get JPAnel for Dialog Box
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
                            title = "Section " + (i + 1);

                            //JFrame
                            f = new JFrame(title + " - " + Race.getInstance().getName());
                            f.add(new JPanel() {

                                @Override // placeholder for actual content
                                public Dimension getPreferredSize() {
                                    return new Dimension(320, 240);
                                }

                            });
                            //frame.setContentPane(new ClientRacePenaltiesUIDynamic(i+1).getRootComponent());
                            f.setContentPane(new ClientRacePenaltiesUIDynamic(i + 1, false,null).getRootComponent());
                            f.pack();
                            f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);//JFrame.EXIT_ON_CLOSE);         // todo unhide, etc
                            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                            GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
                            Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
                            int x = (int) rect.getMaxX() - ((Race.getInstance().getNbrOfSections() - i) * f.getWidth());
                            int y = (int) rect.getMaxY() - f.getHeight();
                            f.setLocation(x, y);
                            f.setVisible(true);
                        }
                    }
                }
        );


        menuItem = new JMenuItem("Section Penalty Scoring - OBSOLETE all in 1 window");
        menu.add(menuItem);
        menuItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JFrame frame = new JFrame("Race Penalty Scoring");
                        frame.setContentPane(new ClientRacePenaltiesUIDynamic().getRootComponent());
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
                        RaceTimingUI timingUI = RaceTimingUI.getInstance();

                        if (timingFrame1 == null) {                  // fixme ALL Frames except Scoring should be handled like this - singleton
                            timingFrame1 = new JFrame();
                            timingFrame1.setLayout(new BorderLayout());
                            timingFrame1.add(timingUI.$$$getRootComponent$$$(), BorderLayout.NORTH);

                            JPanel statusBar = new StatusBar().getPanel(timingFrame1);
                            timingFrame1.add(statusBar, BorderLayout.SOUTH );
                            timingFrame1.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                            timingFrame1.pack();
                        }

                        timingUI.setTitle(timingFrame1);
                        timingFrame1.setVisible(true);
                    }
                }
        );


        menu = new JMenu("Results");
        menuBar.add(menu);
        menuItem = new JMenuItem("Leaderboard");
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Results dump to console");
        menu.add(menuItem);

        menuItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        Race race = Race.getInstance();

                        outputResults("", race.getCompletedRuns(), false);
                        outputResults("Sorted", race.getCompletedRunsByClassTime(), true);
                        LeaderBoard leaderBoard = new LeaderBoard(null);
                        JFrame frame = new JFrame();
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
                        Race race = Race.getInstance();

                        outputResults("", race.getCompletedRuns(), false);
                        outputResults("Sorted", race.getCompletedRunsByClassTime(), true);
                        LeaderBoardScroll leaderBoard = new LeaderBoardScroll();
                        JFrame frame = new JFrame();
                        frame.setContentPane(leaderBoard.$$$getRootComponent$$$());
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.pack();
                        frame.setVisible(true);
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
                        Race race = Race.getInstance();

                        outputResults("", race.getCompletedRuns(), false);
                        outputResults("Sorted", race.getCompletedRunsByClassTime(), true);
                        ScoringBoard scoringBoard = new ScoringBoard();
                        appFrame.setContentPane(scoringBoard.$$$getRootComponent$$$());
                        appFrame.pack();
                        appFrame.setVisible(true);
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



    private void outputResults(String title, ArrayList<RaceRun> runs, boolean breakOnClassChange) {
        log.info("\n" + title + " Results");
        String lastBoatClass = null;
        for (RaceRun r:runs) {


            float totalTime;

            if (breakOnClassChange) {
                if (lastBoatClass != null) {
                    if (lastBoatClass.compareTo(r.getBoat().getBoatClass()) != 0)
                        log.info("----");
                }
                lastBoatClass = r.getBoat().getBoatClass();
            }
            totalTime = (float)r.getTotalPenalties();
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

            ArrayList<Result>sorted = Race.getInstance().getTempResults();
            for (Result r:sorted) {
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

                s1 = String.format("%1$3s",r.getBoat().getRacer().getBibNumber());
                output.write(s1 + ", ");
                String s = r.getBoat().getRacer().getShortName();
                s1 = String.format("%1$-15s",s);
                output.write(s1 + ", ");


                output.write( " run1=");
                s1 = r.getRun1()!=null?r.getRun1().formatTimeExpanded():RaceRun.TIME_EXPANDED_FILL;
                output.write(s1);

                output.write( " run2=");
                s1 = r.getRun2()!=null?r.getRun2().formatTimeExpanded():RaceRun.TIME_EXPANDED_FILL;
                output.write(s1);
               /// todo must integrate TH results before sort  - DONE VERIFY 10/11/2013
                output.write(        "   best=");
                s1 = r.getBestRun()!=null?r.getBestRun().formatTimeTotalOnly():RaceRun.TIME_ONLY_FILL;
                output.write(s1);


                if (r.getBestRun().getTagHeuerRaceRun() != null)  {    /// todo must integrate TH results before sort
                   output.write(ResultsTable.TIMINGMODE_AUTOMATIC);
                }
                output.newLine();
            }

            output.close();
        } catch ( Exception e ) {
           log.write(e);
        }
        finally {
            try {output.close();} catch (Exception ex) {}
        }

    }



    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getInstance().createAndShowGUI();
            }
        });
    }

}
