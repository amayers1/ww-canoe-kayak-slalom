package com.tcay.slalom.UI.client;

//import com.intellij.ui.components.JBScrollPane;
//import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.tcay.slalom.UI.SlalomApp;
import com.tcay.slalom.UI.components.ICF_50_ReasonButton;
import com.tcay.slalom.socket.Client;
import com.tcay.util.Log;
import com.tcay.slalom.*;
import com.tcay.slalom.UI.components.BibLabel;
import com.tcay.slalom.UI.components.GatePenaltyButton;
import com.tcay.slalom.socket.Proxy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 8/29/13
 * Time: 10:25 AM
 * To change this template use File | Settings | File Templates.
 */

/**
 * Cannot use Race.getInstance() in this class, must use Proxy as we may share memory with the
 * other classes or we may be remote and not have access to Race singleton data
 *
 */


public class ClientRacePenaltiesUIDynamic {
    private JComboBox activeOrRecentRunsComboBox;     // todo dynamic update .... newly started racers don't show  sometimes
    private JPanel innerPanel;
    private JButton selectRaceRun;                    // the run being scored or displayed in the UI
    private JLabel raceRunLabel;                      // Name of racer and which run
    private BibLabel bibLabel;
    private JButton doneBtn;

    private long getRunsStartedOrCompletedCnt = 0;    // current total number of runs (either on course or completed)
    private RaceRun selectedRun = null;               // the run that will be/is displayed in the Penalty UI
    private int onlyThisSection = 0;
    private Log log;

    private Proxy raceProxy;
    private RaceResources resources;

    //  Cannot use Race.getInstance()  !!!
    Race Race = null;  /// todo this is a track to avoid Race.getInstance() calls
    // Cannot use Race.getInstance() in this class, must use Proxy as we may NOT share memory with the
    // * other classes in SlalomApp, OR we may be remote and not have access to Race singleton data/



    private ArrayList<GatePenaltyButton> penaltyButtons;

    static final int ROW_OFFSET = 5;


    private int setupPenaltyButtons()
    {

        CellConstraints cc = new CellConstraints();
        int row=1;
        int col=-1;
        int section = 0;
        int iGate=0;
        ImageIcon icon;
        int maxRow = 0;
        GatePenaltyButton gateButton;

        int nbrGates = raceProxy.getNbrGates();

        for (int i = 0; i < nbrGates /*getNbrOfSections()*/; i++) {
            iGate++;

            if (raceProxy.isFirstGateInSection(iGate,section)){
                section++;


                row = 1;

                if (onlyThisSection==0) {// || onlyThisSection ==section  ) {
                    col+=2;
                    // Add Section Title
                    //log.trace("New Section #" + section + " at Gate #" + iGate);
                    // Need a INTELLIJ JAR todo eliminating jars   Spacer spacer1 = new Spacer();
                    //innerPanel.add(spacer1, cc.xy(col,( ROW_OFFSET+ (row * 2)), CellConstraints.DEFAULT, CellConstraints.FILL));
                    innerPanel.add(new JLabel("Section " + section), cc.xy(col,+(ROW_OFFSET + (row * 2))));
                    row++;
                    if (row > maxRow) {
                        maxRow = row;
                    }

                }
                else col=3;//1;
            }

            if (col<1)
                col = 3;

            //log.trace("gate " + iGate + " section " + section  );


            boolean rc = raceProxy.isGateInSection(iGate, onlyThisSection);
            if (onlyThisSection != 0 && rc == false )  {
//                log.trace("CONTINUE");
                continue;
            }


            if ( raceProxy.isUpstream(iGate) ) {
                icon = resources.getUpstreamSmallII();
            }
            else {
                icon = resources.getDownstreamSmallII();
            }

            gateButton = new GatePenaltyButton( iGate, (new Integer(iGate)).toString(),  icon);
            penaltyButtons.add(gateButton);

            innerPanel.add(gateButton, cc.xy(col, (ROW_OFFSET + (row * 2))));     // todo move to single load time instantiation
                row++;
            if (row > maxRow) {
                maxRow = row;
            }

            gateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    GatePenaltyButton btn = (GatePenaltyButton)actionEvent.getSource() ;
                    String reasonCode=null;
                    Integer penalty =0;
                    if (btn.getPenalty() == 0) {
                        penalty = 2;
                        //btn.setReasonFor50(null);
                    }
                    else {
                        penalty = btn.getPenalty();
                        if (penalty == 2)  {
                            penalty = 50;
                            if (Race.getInstance().isIcfPenalties()) {
                                ClientRacePenaltyICF50SecondReasonDialog reasonDialog = new ClientRacePenaltyICF50SecondReasonDialog( );
                                reasonCode = reasonDialog.doDialog(btn, selectedRun.getBoat().toString(), btn.getGate());
                                //btn.setText(btn.getText() + "(" + btn.getReasonFor50().getReasonCode() + ")");
                                //do50Options(btn);
                            }

                        }
                        else if (penalty == 50)  {
                            //btn.setReasonFor50(null);
                            penalty = 0;
                        }
                    }
                    //btn.setReasonFor50(reasonCode);
                    btn.setPenalty(penalty, reasonCode);
                                    // update the Button
                }
            });
        }
        return(maxRow);
    }

    private void clearPenaltyBtns() {
        for (GatePenaltyButton pb:penaltyButtons) {
            pb.setPenalty(0);
        }

    }



    private void loadPenalties(RaceRun run) {
        ArrayList<Penalty> penalties = run.getPenaltyList();
        clearPenaltyBtns();

        for (Penalty p:penalties) {
            int gateNbr = p.getGate();

            for (GatePenaltyButton pb:penaltyButtons) {
                if (gateNbr == pb.getGate()) {
                    pb.setPenalty(p.getPenaltySeconds());
                    break;
                }
            }
        }
    }

    {
        log = Log.getInstance();
        resources = RaceResources.getInstance();

    }


    public ClientRacePenaltiesUIDynamic(int section, Proxy proxy) {
        if (proxy == null) {
            raceProxy = new Proxy(new Client());//standAlone==true?new Client():null);
        }
        else {
            raceProxy = proxy;
        }

        setupUI(section);
    }

    public ClientRacePenaltiesUIDynamic(int section) {
    }



/*
    public ClientRacePenaltiesUIDynamic() { //Proxy proxy) {
//        raceProxy = new Proxy(false);
        //if (proxy == null) {
            raceProxy = new Proxy(null);     //fixme can't pass NULL   131109
        //}
        //else {
        //    raceProxy = proxy;
        //}
        setupUI();
    }
*/

    private ComboBoxModel updateComboBoxModel() {
        //log.trace("updateComboBoxModel::Requesting new SCORABLE RUNS !");
        // this list has to be a copy for each window, as they are potentially all on separate devices
        ArrayList<RaceRun> scorableList = raceProxy.getScorableRuns();
        ComboBoxModel model = new DefaultComboBoxModel(scorableList.toArray()) {

        };
        return (model);
    }

    private void refreshComboModelIfAppropriate() {
        if (!activeOrRecentRunsComboBox.hasFocus()) {
            if (getRunsStartedOrCompletedCnt != raceProxy.getRunsStartedOrCompletedCnt()) {
                getRunsStartedOrCompletedCnt = raceProxy.getRunsStartedOrCompletedCnt();
                activeOrRecentRunsComboBox.setModel(updateComboBoxModel());
                // D131025 (ajm)
                //getFirstUnJudgedEntry(); // 131026  todo, is this OK here
                activeOrRecentRunsComboBox.setSelectedIndex(getFirstUnJudgedEntry());
            }
        }
    }


    private void createUIComponents() {
        Timer listCheckForUpdatesTimer;           // timer to trigger auto update of activeOrRecentRunsComboBox


        listCheckForUpdatesTimer
                = new Timer(500,
                new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        // don't change comboBox contents if it has focus - confuses the user
                        while (activeOrRecentRunsComboBox == null) {
                            try {
                                //Thread.sleep(100);
                                Thread.sleep(250);
                            } catch (InterruptedException e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            }
                        }

                        refreshComboModelIfAppropriate();
                    }
                });
        listCheckForUpdatesTimer.setInitialDelay(500);
        listCheckForUpdatesTimer.start();
        // todo use setupPenaltyButtons - refactor
        // createPenaltyButtons();
        //updateButtonVisibility();
    }


    public static void main(String[] args) {
//        new TestData();

       // if run standalone, this app will talk to SlalomApp via socket
        JFrame frame = new JFrame("Race Penalties UI");



//        try {
//            clientSocket.connectToServer();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Proxy proxy = new Proxy(new Client());
        ClientRacePenaltiesUIDynamic racePenaltiesUI = new ClientRacePenaltiesUIDynamic(1, proxy); ///fixme section 1 should be dynamic
        frame.setContentPane(racePenaltiesUI.innerPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    private void updateButtonVisibility() {
        if (selectedRun == null) {
            for (GatePenaltyButton pb:penaltyButtons){
                pb.setEnabled(false);
            }
            doneBtn.setVisible(false);
        } else {
            for (GatePenaltyButton pb:penaltyButtons){
                //pb.setVisible(true);
                pb.setEnabled(true);
            }
            doneBtn.setVisible(true);
        }
    }

    public void setData(ClientRacePenaltiesUIDynamic data) {
    }

    public void getData(ClientRacePenaltiesUIDynamic data) {
    }

    public boolean isModified(ClientRacePenaltiesUIDynamic data) {
        return false;
    }



    JPanel panelOuter;

    private void setupInnerPanelUI() {

        penaltyButtons = new ArrayList<GatePenaltyButton>();

        innerPanel = new JPanel();

        activeOrRecentRunsComboBox = new JComboBox();

        activeOrRecentRunsComboBox.setRenderer(   new HighLightRowRenderer(activeOrRecentRunsComboBox.getRenderer()));
        activeOrRecentRunsComboBox.setToolTipText("Entries with light green background have not been scored, light red backgrounds have been scored");

        doneBtn = new JButton();
        raceRunLabel = new JLabel();
        bibLabel = new BibLabel();

        StringBuffer columnSpec = new StringBuffer();
        StringBuffer rowSpec = new StringBuffer();
        int i;


        int maxCol = 5;
        if (onlyThisSection>0)  {
            maxCol = 3;
            for (i=0;i<maxCol;i++) {
                if (i>0)
                    columnSpec.append(",");

                if (i%2==0) {
                    //columnSpec.append("fill:40px:grow," + "left:6dlu:noGrow");
                    columnSpec.append("fill:50px:grow," + "left:6dlu:noGrow");
                }
                else {
                    columnSpec.append("fill:120px:grow," + "left:6dlu:noGrow");
                }
            }
        }
        else  {
            for (i=0;i<maxCol;i++) {
                if (i>0)
                    columnSpec.append(",");

                columnSpec.append("fill:150px:grow," + "left:6dlu:noGrow");
            }
        }


        for (i=0;i<25+5;i++) {    // figure on 25 rows maximum + 5
            if (i>0)
                rowSpec.append(",");

            rowSpec.append("center:d:noGrow,top:3dlu:noGrow");
        }
        innerPanel.setLayout(new FormLayout( columnSpec.toString(), rowSpec.toString()));
        String title = new String(onlyThisSection == 0 ?
                           "Penalty Scoring" :
                           "Section " + onlyThisSection +  " Penalty Scoring");

        innerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),title));

        CellConstraints cc = new CellConstraints();
        //innerPanel.add(activeOrRecentRunsComboBox, cc.xyw(3, 1, 4));
        innerPanel.add(activeOrRecentRunsComboBox, cc.xyw(1, 1, 5));


        selectRaceRun = new JButton();
        selectRaceRun.setText("Select Boat");
        selectRaceRun.setBackground(SlalomApp.LIGHT_GREEN);
        selectRaceRun.setOpaque(true);
//        innerPanel.add(selectRaceRun, cc.xy(7, 1));
        innerPanel.add(selectRaceRun, cc.xy(3, 3));   //1,3


        innerPanel.add(new JLabel("Bib#"),cc.xy(1,3));
innerPanel.add(bibLabel,cc.xy(1, 5));
//innerPanel.add(raceRunLabel,cc.xy(3, 5));

        innerPanel.add(raceRunLabel,        cc.xyw(3, 5, 3));


        // todo fix kludge ordering here in calls
        int maxRow = setupPenaltyButtons();   // max Row set here

//        innerPanel.add(penaltyDescriptionLabel,      cc.xyw(1, (5 + maxRow * 2), 5));
//        innerPanel.add(penaltyDiagramLabel,          cc.xyw(1, (7 + maxRow * 2), 5));

        doneBtn.setText("Done");
        doneBtn.setBackground( SlalomApp.LIGHT_RED);
        doneBtn.setOpaque(true);
//        innerPanel.add(doneBtn, cc.xy(1, (maxRow*2) + 5));
        innerPanel.add(doneBtn, cc.xy(3, 3));//(maxRow*2) + 5));  //1,3




    }



    private void close() {

    }

    private void setupUI(int section) {
        onlyThisSection = section;

        raceProxy.updateSectionOnline(onlyThisSection);
        setupUI();
    }



    private void setupUI() {
        createUIComponents();

        panelOuter = new JPanel();
        panelOuter.setLayout(new FormLayout("fill:d:grow", "center:d:noGrow"));
        final JScrollPane scrollPane1 = new JScrollPane();
        CellConstraints ccOuter = new CellConstraints();
        panelOuter.add(scrollPane1, ccOuter.xy(1, 1, CellConstraints.FILL, CellConstraints.FILL));

        setupInnerPanelUI();
        scrollPane1.setViewportView(innerPanel);



        selectRaceRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedRun = (RaceRun) activeOrRecentRunsComboBox.getSelectedItem();
                if (selectedRun!= null) {
                    raceRunLabel.setText(selectedRun.toString());       // todo null pointer here when scoring window up without any events to trigger list populate
                    bibLabel.setText(selectedRun.getBoat().getRacer().getBibNumber());

                    selectRaceRun.setEnabled(false);
                    selectRaceRun.setVisible(false);
                    activeOrRecentRunsComboBox.setEnabled(false);

                    loadPenalties(selectedRun);
                    updateButtonVisibility();
                }
            }
        });
        doneBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //todo Done action handler

//                if (raceProxy.isStandAlone() == true) {
//                    selectedRun.clearPenaltyList();   /// todo  check if this is ok,  how to get unjudged entry if penalties cleared
//                }

                selectedRun.clearPenaltyList();                //fixme A131028 (ajm) problems resetting penalties

                for (GatePenaltyButton pb:penaltyButtons) {
                    selectedRun.setPenalty( pb.getGate(), pb.getPenalty(), true );
                }
                raceProxy.updateResults(selectedRun);



                selectedRun = null;
                selectRaceRun.setEnabled(true);
                selectRaceRun.setVisible(true);


///todo A131026 is this ok, does it update correctly?
//refreshComboModelIfAppropriate();
                activeOrRecentRunsComboBox.setSelectedIndex(getFirstUnJudgedEntry());
                activeOrRecentRunsComboBox.setEnabled(true);
                raceRunLabel.setText(null);
                bibLabel.setText(null);
                clearPenaltyBtns();
                updateButtonVisibility();
//M20131024                raceProxy.updateResults();
            }
        });


        updateButtonVisibility();
    }


    private int getFirstUnJudgedEntry() {
        int i;
        RaceRun r;
        boolean hasPenalties = true;


        for (i=0; i<activeOrRecentRunsComboBox.getItemCount() && hasPenalties; i++ )  {
            r = (RaceRun)activeOrRecentRunsComboBox.getItemAt(i);
            hasPenalties = false;
            //log.trace("Checking Run" + r);

            for (Penalty p:r.getPenaltyList()) {
                if (raceProxy.isGateInSection(p.getGate(), onlyThisSection)) {
                    hasPenalties = true;
                    //log.trace("Penalty Found at gate " + p.getGate() + "In section" + onlyThisSection + "  for " + r);
                    break;
                }
            }
            if (!hasPenalties) {
               // log.trace("No Penalties " + r);
                break;
            }
        }

        if (i > activeOrRecentRunsComboBox.getItemCount()-1)  {
            // if i = getItemCount todo this should mean that all runs are scored at this section
           log.warn("Bad INDEX to activeOrRecentRunsComboBox itemcount = " + activeOrRecentRunsComboBox.getItemCount() + " index = " + i);
           i = activeOrRecentRunsComboBox.getItemCount()-1;
        }

        //if (i<activeOrRecentRunsComboBox.getItemCount()-1)
        //    return i+1;;

        return i;


    }

    public JComponent getRootComponent() {
        //return innerPanel;
        return panelOuter;
    }

    public class HighLightRowRenderer implements ListCellRenderer {

        private final ListCellRenderer delegate;
        private int height = -1;

        public HighLightRowRenderer(ListCellRenderer delegate) {
            this.delegate = delegate;
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component component = delegate.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Dimension size = component.getPreferredSize();

            RaceRun run = (RaceRun)value;
            boolean haveScoringForThisSection = false;
            if (onlyThisSection !=0 && run != null) {//&& run.getPenaltyList()!=null) {
                for (Penalty p:run.getPenaltyList()) {
                    if (raceProxy.isGateInSection(p.getGate(),onlyThisSection)) {
                        haveScoringForThisSection = true;
                        break;
                    }
                }
                if (component.hasFocus() == false) {
                    if (haveScoringForThisSection == true ) {  // to do ONLY when not selected/&& index != 0) {
                        component.setBackground(SlalomApp.LIGHT_RED);
                        if (component instanceof JLabel) {
                            ((JLabel) component).setHorizontalTextPosition(JLabel.CENTER);
                        }
                    }
                    else {
                        component.setBackground(SlalomApp.LIGHT_GREEN);
                    }

                }
                else {
                    if (haveScoringForThisSection) {  // to do ONLY when not selected/&& index != 0) {
                        component.setBackground(SlalomApp.LIGHT_RED);
                        component.setForeground(SlalomApp.LIGHT_YELLOW);
                    }
                    else {
                        component.setBackground(SlalomApp.LIGHT_GREEN);
                        component.setForeground(SlalomApp.LIGHT_YELLOW);
                    }
                }
            }
            return component;
        }
    }


}
