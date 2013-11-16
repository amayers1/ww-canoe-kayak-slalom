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

//import com.intellij.ui.components.JBScrollPane;
//import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.sun.javaws.exceptions.InvalidArgumentException;
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
import java.awt.event.*;
import java.util.ArrayList;

/**
 * SlalomApp
 *
 * Teton Cay Group Inc. 2013
 *

 * User: allen
 * Date: 8/29/13
 * Time: 10:25 AM
 *
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
    private boolean demoMode;

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
                    Integer penalty;// =0;
                    if (btn.getPenalty() == 0) {
                        penalty = 2;
                    }
                    else {
                        penalty = btn.getPenalty();
                        if (penalty == 2)  {
                            penalty = 50;
                            if (Race.getInstance().isIcfPenalties()) {
                                ClientRacePenaltyICF50SecondReasonDialog reasonDialog = new ClientRacePenaltyICF50SecondReasonDialog( );
                                reasonCode = reasonDialog.doDialog(btn, selectedRun.getBoat().toString(), btn.getGate());
                            }

                        }
                        else if (penalty == 50)  {
                            penalty = 0;
                        }
                    }
                    btn.setPenalty(penalty, reasonCode);
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


    public ClientRacePenaltiesUIDynamic(int section, Proxy proxy, boolean demoMode) throws Exception {
        if (proxy == null) {
            throw new Exception("Cannot call with null Proxy");
        }
        else {
            raceProxy = proxy;
        }

        this.demoMode = demoMode;

        setupUI(section);
    }


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

            long serverRunsStartedOrCompletedCnt = raceProxy.getRunsStartedOrCompletedCnt();
            if (getRunsStartedOrCompletedCnt != serverRunsStartedOrCompletedCnt) {
                getRunsStartedOrCompletedCnt = serverRunsStartedOrCompletedCnt;
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
                    int iterations = 0;
                    public void actionPerformed(ActionEvent actionEvent) {
                        iterations++;
                        // don't change comboBox contents if it has focus - confuses the user
                        while (activeOrRecentRunsComboBox == null) {
                            try {
                                //Thread.sleep(100);
                                Thread.sleep(250);
                            } catch (InterruptedException e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            }
                        }
                        if (demoMode && (iterations%50==0)) {
                            int entry = getFirstUnJudgedEntry();
                            if (entry > -1 && entry < activeOrRecentRunsComboBox.getItemCount()) {
                                activeOrRecentRunsComboBox.setSelectedIndex(entry);
                                selectedRun = (RaceRun)activeOrRecentRunsComboBox.getSelectedItem();
                                selectRaceRun.doClick();
                                int howManyPenalties = (int)(Math.random()*1.45);
                                int whatGate;
                                for (int i=0;i<howManyPenalties;i++) {
                                    whatGate = (int)(Math.random()* penaltyButtons.size());
                                    if (whatGate!=0) {
                                        /// simulate a penalty
                                        GatePenaltyButton pb = penaltyButtons.get(whatGate);//.setPenalty(2);
                                        pb.doClick();
                                        if (iterations%5000==0)   {
                                            pb.doClick();   ///  clicks == 50 second penalty
                                        }
                                    }
                                }
                                doneBtn.doClick();
                            }
                        }
                        refreshComboModelIfAppropriate();
                    }
                });
        listCheckForUpdatesTimer.setInitialDelay(500);
        listCheckForUpdatesTimer.start();
    }


    public static void main(String[] args) {

        // Runs standalone, this app will talk to SlalomApp via socket
        JFrame frame = new JFrame("Race Penalties UI");

        Proxy proxy;
        try {
            proxy = new Proxy(new Client());
            ClientRacePenaltiesUIDynamic racePenaltiesUI = new ClientRacePenaltiesUIDynamic(1, proxy, false); ///fixme section 1 should be dynamic
            frame.setContentPane(racePenaltiesUI.innerPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        innerPanel.add(selectRaceRun, cc.xy(3, 3));   //1,3
        innerPanel.add(new JLabel("Bib#"),cc.xy(1,3));
        innerPanel.add(bibLabel,cc.xy(1, 5));

        innerPanel.add(raceRunLabel,        cc.xyw(3, 5, 3));


        // todo fix kludge ordering here in calls --- FIXME CRASH HER ON 2nd invocation
        int maxRow = setupPenaltyButtons();   // max Row set here

        doneBtn.setText("Done");
        doneBtn.setBackground( SlalomApp.LIGHT_RED);
        doneBtn.setOpaque(true);
        innerPanel.add(doneBtn, cc.xy(3, 3));
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
        // FIXME CRASH HERE
        setupInnerPanelUI();
        scrollPane1.setViewportView(innerPanel);



        selectRaceRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectRaceRunButtonHandler();
            }
        });
        doneBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                doneButtonActionHandler();
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

            for (Penalty p:r.getPenaltyList()) {
                if (raceProxy.isGateInSection(p.getGate(), onlyThisSection)) {
                    hasPenalties = true;
                    break;
                }
            }
            if (!hasPenalties) {
                break;
            }
        }

        if (i > activeOrRecentRunsComboBox.getItemCount()-1)  {
            // if i = getItemCount todo this should mean that all runs are scored at this section
           //log.warn("Bad INDEX to activeOrRecentRunsComboBox itemcount = " + activeOrRecentRunsComboBox.getItemCount() + " index = " + i);
           i = -1;//activeOrRecentRunsComboBox.getItemCount()-1;
        }

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

    public void selectRaceRunButtonHandler() {
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

    public void doneButtonActionHandler() {
        //todo Done action handler
//                if (raceProxy.isStandAlone() == true) {
//                    selectedRun.clearPenaltyList();   /// todo  check if this is ok,  how to get unjudged entry if penalties cleared
//                }

        //selectedRun.clearPenaltyList();                //fixme A131028 (ajm) problems resetting penalties

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

}
