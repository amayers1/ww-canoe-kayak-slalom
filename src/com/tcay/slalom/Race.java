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

package com.tcay.slalom;

import com.tcay.VirtualLED;
import com.tcay.RS232.PhotoEyePortListener;
import com.tcay.slalom.UI.RaceTimingUI;
import com.tcay.slalom.UI.components.LedIndicator;
import com.tcay.slalom.UI.http.SlalomRacerResultsHTTP;
import com.tcay.slalom.UI.http.SlalomResultsHTTP;
import com.tcay.slalom.UI.http.SlalomResultsScoringHTTP;
import com.tcay.slalom.timingDevices.MicrogateREI2.MicrogateAgent;
import com.tcay.slalom.timingDevices.PhotoCellAgent;
import com.tcay.slalom.timingDevices.PhotoCellRaceRun;
import com.tcay.slalom.timingDevices.TimingImpulse;
import com.tcay.slalom.timingDevices.alge.AlgeTimy;
import com.tcay.slalom.timingDevices.tagHeuer.TagHeuerAgent;
import com.tcay.util.DuplicateBibException;
import com.tcay.util.Log;
import com.tcay.slalom.UI.JudgingSection;
import com.tcay.slalom.UI.tables.ResultsTableModel;
import com.tcay.slalom.UI.tables.RunScoringTableModel;
import com.tcay.util.XStreamRaceConverter;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import javax.swing.*;
//import java.beans.XMLEncoder;
import java.io.*;
import java.util.*;

/**
 * Teton Cay Group, Inc.
 * User: allen
 * Date: Aug 21, 2013
 * Time: 5:07:46 PM
 */


public class Race extends RaceResources implements Serializable

{


    private transient static Race instance = null;
    private transient ArrayList<TimingImpulse>finishPulses;
    private transient ArrayList<TimingImpulse>startPulses;
    private transient LedIndicator finishEyeLED = new LedIndicator(VirtualLED.RED);
    private transient LedIndicator startEyeLED = new LedIndicator(VirtualLED.GREEN);


    private transient static final int trainingMode = 0;// todo If this is one then AUTO increment of runs happens, CANT USE IN RACE
    //or last racer in first runs has thte run number in race set to 2 before they finish == NO MATCHING
    // Run on FINISH//  e.g Run 1 for last racer doesn't == run 2 iteration for auto incremented  race 1;
    private transient int MAX_RUNS_ALLOWED = 2; //99;//2;


    // Race Configuration
    private transient Calendar cal = Calendar.getInstance();

    public PhotoCellAgent getPhotoCellAgent() {
        return photoCellAgent;
    }


    public ImageIcon getPhotoCellTinyII() {
        if (timyEnabled) {
            return tagHeuerTinyII;
        }
        if (tagHeuerEnabled) {
            return tagHeuerTinyII;
        }
        return microgateII;
    }


// Only allow REAL EYE pulses ... not those done by buttons from the timing box.  Make configurable

    public void addStopImpulse( TimingImpulse impulse) {
         finishPulses.add(impulse);
    }

    public void addStartImpulse( TimingImpulse impulse) {
        startPulses.add(impulse);
    }

    private transient PhotoCellAgent photoCellAgent;//Calendar cal = Calendar.getInstance();
    //private transient PhotoCellAgent agent;

    public boolean isPHOTO_EYE_AUTOMATIC() {
        return PHOTO_EYE_AUTOMATIC;
    }

    // PHOTO_EYE_AUTOMATIC mode requires only verification of timing device impulses, and doesn't use
    // the START / FINISH button manual backup timing
    private transient boolean PHOTO_EYE_AUTOMATIC = false;// TODO parameterize true;

    Date date = cal.getTime();


    private String name;
    private String location;
    private Integer nbrGates;
    private List<Integer> upstreamGates;
    private ArrayList<JudgingSection> judgingSections;
    private List<Racer> racers;
    private boolean icfPenalties = false;


    // Race Status
    private RaceRun pendingRerun;// = null;
    private RaceRun pendingAdjustment;
    private List<BoatEntry> startList = null;     // todo on Crash recovery don't have an yet-to-be-started list, only all racers
    private ArrayList<RaceRun> activeRuns = null;
    private long runsStartedOrCompletedCnt = 0;
    private int currentRunIteration;  // are we on 1st runs, or 2nd runs ?
    private ArrayList<RaceRun> completedRuns = null;


    // Transient Objects

    // Move these to SlalomApp or elsewhere in a device handler object
    private transient Thread photoEyeThread;     // Thread to get input from Photocells
    private transient PhotoEyePortListener photoEyeListener;   // Tag Heuer CP 520/540 listener/command processor


    private transient ArrayList<Result> results = new ArrayList<Result>();
    private transient LastRace lastRace;

    public Boolean getTagHeuerEnabled() {
        return tagHeuerEnabled;
    }

    public Boolean getMicrogateEnabled() {
        return microgateEnabled;
    }

    public Boolean getAlgeTimyEnabled() {
        return timyEnabled;
    }
    public void setTagHeuerEnabled(Boolean tagHeuerEnabled) {
        this.tagHeuerEnabled = tagHeuerEnabled;
    }

    public void setMicrogateEnabled(Boolean microgateEnabled) {
        this.microgateEnabled = microgateEnabled;
    }

    public void setAlgeTimyEnabled(Boolean timyEnabled) {
        this.timyEnabled = timyEnabled;
    }

    private Boolean tagHeuerEnabled = false;
    private Boolean microgateEnabled = false;
    private Boolean timyEnabled = false;

    private transient Boolean tagHeuerConnected;
    private transient Boolean microgateConnected;
    private transient Log log;

    private transient XStream xstream;


   // private transient SlalomResultsHTTP_Save resultsHTTP;
//D161004
    private transient SlalomRacerResultsHTTP racerResultsHTTP;
    private transient SlalomResultsScoringHTTP scoreboardResultsHTTP_New;
    private transient SlalomResultsHTTP someResultsHTTP;


    public synchronized static Race getInstance() {
        if (instance == null) {
            instance = new Race();
        }
        return instance;
    }

    public static int getTrainingMode() {
        return trainingMode;
    }

    public int getMaxRunsAllowed() {
        return MAX_RUNS_ALLOWED;
    }

//    public boolean isTagHeuerEmulation() {
//        return tagHeuerEmulation;
//    }
//
//    public void setTagHeuerEmulation(boolean tagHeuerEmulation) {
//        this.tagHeuerEmulation = tagHeuerEmulation;
//    }

    public Boolean getTagHeuerConnected() {
        return tagHeuerConnected;
    }

    public Boolean getMicrogateConnected() {
        return microgateConnected;
    }

    public void setTagHeuerConnected(Boolean tagHeuerConnected) {
        this.tagHeuerConnected = tagHeuerConnected;
    }


   // public void setPhotoCellAgent(PhotoCellAgent photoCellAgent) {
   //     this.photoCellAgent = photoCellAgent;
   // }

    public void setMicrogateConnected(Boolean microgateConnected) {
        this.microgateConnected = microgateConnected;
    }

    public boolean isIcfPenalties() {
        return icfPenalties;
    }

    public void setIcfPenalties(boolean icfPenalties) {
        this.icfPenalties = icfPenalties;
    }


// TODO 170418 Figure this out and optimize
    public boolean TODOKludgeStartFRomPhotoEye(String bibNumber) {
        boolean started = false;
        RaceTimingUI timingUI =  RaceTimingUI.getInstance();
        if (timingUI.startButtonActionHandlerFromPhotoEye(bibNumber)) {
            started = true;
        }
        return started;
    }


    // TODO 170418 Figure this out and optimize
    public boolean TODOKludgeFinishFRomPhotoEye(String bibNumber) {
        boolean stopped = false;
        RaceTimingUI timingUI =  RaceTimingUI.getInstance();
        if (timingUI.stopButtonActionHandlerFromPhotoEye(bibNumber)) {
            stopped = true;
        }
        return stopped;
    }



    public JudgingSection updateSectionOnline(Integer section) {
        JudgingSection found = null;
        for (JudgingSection js:judgingSections) {
            if (js.getSectionNbr().compareTo(section) == 0) {
                js.setClientDeviceAttached(true);
                found = js;
                break;
            }
        }
        return found;

    }


    public void maybeStartPhotoCellInterface() {

        //PhotoCellAgent agent = null;
        if (photoEyeThread == null) {

            // Todo - change Configuration form checkbox to radio buttons - mutually exclusive

            if (timyEnabled) {
                photoCellAgent = new AlgeTimy();
            } else if (tagHeuerEnabled) {
                photoCellAgent = new TagHeuerAgent();
            } else if (microgateEnabled) {
                photoCellAgent = new MicrogateAgent();
            }


            photoEyeListener = new PhotoEyePortListener(photoCellAgent);
            photoEyeThread = new Thread(photoEyeListener);
            photoEyeThread.start();

            //photoCellAgent = photoEyeListener.getAgent();

        }
        else {
            log.info("ALREADY HAVE A THREAD HERE !!!");
        }

    }

    public void setUpstreamGates(List<Integer> upstreamGates) {
        this.upstreamGates = upstreamGates;
    }
    public ArrayList<Result> getTempResults() {
        return results;
    }


// todo investigate changing to string return
    public int getCurrentRunIteration() {       // Run #1, #2, etc.  for all racers
        return currentRunIteration;
    }

    public String getRunTitle() {
       return ( getCurrentRunIterationOrdinal() + " Runs" );
    }


    public RaceRun getPendingRerun() {
        RaceRun r = pendingRerun;

        pendingRerun = null;
        return r;
    }

    public void setPendingRerun(RaceRun newRun) {

        pendingRerun = newRun;

    }


    public void setPendingAdjustment(RaceRun adjustRun) {

        pendingAdjustment = adjustRun;

    }

    public RaceRun getPendingAdjustment() {
        RaceRun r = pendingAdjustment;

        pendingAdjustment = null;
        return r;
    }





    public String getCurrentRunIterationOrdinal() {
        String ordinal = " ?? ";
        switch (currentRunIteration)       {

            case 1:
                ordinal = FIRST;
                break;
            case 2:
                ordinal = SECOND;
                break;
            default:
                ordinal =  new Integer(currentRunIteration).toString();
                break;
        }


        return ordinal;
    }



    /**
     * Clears racers / boat list, active runs, completed runs and startlist
     * <p>
     * This method used when importing a startlist from an external source
     *
     * @param  none
     * @return none     the image at the specified URL
     */
  //  protected void clearRacers() {
  //      racers = new ArrayList<Racer>();
  //      startList = new ArrayList<BoatEntry>();
  //      activeRuns = new ArrayList<RaceRun>();
  //      completedRuns = new ArrayList<RaceRun>();
  //  }


    protected void clearRace() {
        racers = new ArrayList<Racer>();
        startList = new ArrayList<BoatEntry>();
        activeRuns = new ArrayList<RaceRun>();
        completedRuns = new ArrayList<RaceRun>();
        //clearRacers();
        judgingSections = new ArrayList<JudgingSection>();
        upstreamGates = new ArrayList<Integer>();

        finishPulses = new ArrayList<TimingImpulse>();
        startPulses = new ArrayList<TimingImpulse>();

        location = "";
        pendingRerun = null;
        name = UNNAMED_RACE;
        currentRunIteration = 1;
        runsStartedOrCompletedCnt = 0;
        lastRace = new LastRace();
        nbrGates = DEFAULT_NBR_GATES;

        // resettable transient members
        results = new ArrayList<Result>();
        // todo ?? lastRace;
    }




    /*private Changed for XMLEncoder .... SUCKS !!! 170418*/
    public Race() {
        if (trainingMode==1) {
            MAX_RUNS_ALLOWED = 99;
        }

        log = Log.getInstance();


        xstream = initXML(); //  D20160407 RIO CAUSING CRASH



        clearRace();

        lastRace = new LastRace();
        tagHeuerConnected = false;//new Boolean(false);
        microgateConnected = false;
//D161004        resultsHTTP = new SlalomResultsHTTP_Save();
        // todo set up on timing page to test and then enable CP520
//C160315        Thread t = new Thread( photoEyeListener = new PhotoEyePortListener());
//C160315        t.start();

        //TODO - determine if any photo eyes in use, add appropriate handler/listener
        //D20160305maybeStartPhotoCellInterface();  Was being called in ClietnPenalty App

//D161004
        racerResultsHTTP = new SlalomRacerResultsHTTP();
        scoreboardResultsHTTP_New = new SlalomResultsScoringHTTP();
        someResultsHTTP = new SlalomResultsHTTP();
    }





    public void removeCompletedRun(RaceRun remove) {
        completedRuns.remove(remove);
    }


    public void incrementCurrentRunIteration() {
       if (currentRunIteration < MAX_RUNS_ALLOWED ){
          currentRunIteration++;
       }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        lastRace.setName(name);
        lastRace.saveSerializedData();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getNbrGates() {
        return nbrGates;
    }

    public ArrayList<JudgingSection> getSections() {
        return judgingSections;
    }

    public void setNbrGates(Integer nbrGates) {
        this.nbrGates = nbrGates;
    }

    public List<Integer> getUpstreamGates() {
        return upstreamGates;
    }


    public boolean isUpstream(int iGate) {
        for (int i:upstreamGates) {
            if ( i==iGate) {
                return true;

            }
        }
        return false;
    }


    public int getNbrOfSections() {
        return judgingSections.size();
    }


    public ArrayList<JudgingSection> getSectionEndingGates() {
        return judgingSections;
    }

    public void setSectionEndingGates(ArrayList<JudgingSection> sectionEndingGates) {
        this.judgingSections = sectionEndingGates;
    }

    public void addSection(JudgingSection section) {
        judgingSections.add( section);
    }


    public boolean isFirstGateInSection(int iGate) {
        for (JudgingSection s:judgingSections) {
            if (iGate == s.getFirstGate() )  {
                return true;
            }
        }
        return false;
    }

    public String getSectionsConnectedNamesAsString() {
        StringBuffer sb = new StringBuffer();
        boolean first = true;
        for (JudgingSection s:judgingSections) {
            if (s.getClientDeviceAttached())  {
                if (!first) {
                    sb.append(",");
                }
                first = false;
                sb.append(s.getSectionNbr());
            }
        }
        if (sb.length()==0){
            sb.append(SECTIONS_NONE);
        }
        return sb.toString();
    }


    public JudgingSection getSection(int section) {
        JudgingSection judgingSection = null;
        for (JudgingSection s:judgingSections) {
            if (s.getClientDeviceAttached())  {
                judgingSection = s;
                break;
            }
        }
        return judgingSection;
    }


    public boolean isGateInSection(int iGate, int section)  {
        if (section==0) {  // All Sections are represented by 0 /// fixme kludge
            return true;
        }
        int currSection = 1;
        for (JudgingSection s:judgingSections) {
            if (section == currSection) {
                if (iGate >= s.getFirstGate() && iGate <= s.getLastGate())  {
//System.out.println(iGate + " is in section " + section);
                    return true;
                }
            }
            currSection++;
        }
//        System.out.println(iGate + " is NOT in section " + section);

        return false;
    }


    public boolean isLastGateInSection(int iGate) {
        for (JudgingSection s:judgingSections) {
            if (iGate == s.getLastGate() )  {
                return true;
            }
        }
        return false;
    }



    public ArrayList<RaceRun> getCompletedRuns() {
        return completedRuns;
    }


    public RaceRun getExistingRun(BoatEntry be) {   // todo Add Run#
        RaceRun run = null;
        for (RaceRun r:completedRuns) {
            if (r.getRunNumber() == currentRunIteration && r.getBoat() == be) {
                run = r;
                break;
            }
        }

        if (run == null) {
            for (RaceRun r:activeRuns) {
                if (r.getRunNumber() == currentRunIteration && r.getBoat() == be) {
                    run = r;
                    break;
                }
            }
        }

        return run;

    }


    public ArrayList<RaceRun> getCompletedRunsByClassTime() {
        ArrayList<RaceRun> sorted = new ArrayList<RaceRun>(completedRuns);

        Collections.sort(sorted);
        return sorted;
    }



    public ArrayList<RaceRun> getActiveRuns() {
        return activeRuns;
    }


    public List<Racer> getRacers() {
        return racers;
    }

    /**
     * Add an entry to the StartList rejecting any duplicate Bib assignments in the same race class
     * It is possible to have the same bib in a different classes in lower levels of races (e.g. C1 and C2)
     *
     * @param racer
     * @param boatClass
     * @throws DuplicateBibException
     */
    public void addBoat( Racer racer, String boatClass) throws DuplicateBibException {
        if (count(racer.getBibNumber(),boatClass) < 1) {
           racers.add(racer);
           startList.add(new BoatEntry( racer, boatClass));
        }
        else {
            log.error("Can't add bib " + racer.getBibNumber() + " for " + boatClass + " " +  racer.getShortName() +
                    " already used by " + whoIs(racer.getBibNumber(),boatClass) );
            throw new DuplicateBibException();
        }
    }




    public void addRun(RaceRun newRun) {
        triggerStartEyeLedIndicator(); /// todo 201704 Hook from PhotoEyes


        synchronized (activeRuns) {
            activeRuns.add(newRun);
            runsStartedOrCompletedCnt++;   /// count of total racer starts

            System.out.println("CURRENTLY " + activeRuns.size() + " ACTIVE RUNS");
        }
    }


    public void finishedRun(RaceRun run) {


        triggerFinishEyeLedIndicator();  /// todo 201704 Hook from PhotoEyes
        synchronized (activeRuns) {
            activeRuns.remove(run);
            runsStartedOrCompletedCnt++;
        }

        synchronized (completedRuns) {
            completedRuns.add(run);
        }
    }

    public boolean isDNF(String bibNumber, int runNumber) {boolean dnf = false;
       for (RaceRun rr:completedRuns) {
           if (rr.getBoat().getRacer().getBibNumber().equals(bibNumber) &&
               rr.getRunNumber() == runNumber ) {
               if (rr.isDnf()) {
                   dnf = true;

               }
           }
        }
        return(dnf);
    }



    //A150009 (ajm) Start

    public RaceRun getNewestCompletedRun() {
        RaceRun run = null;
    //    if (getActiveRuns().size() > 0) {
    //        run = getActiveRuns().get(0);
    //    }

        int size = getCompletedRuns().size();
        if ( size>0) {

          run = getCompletedRuns().get(size-1);
        }

        return(run);

    }

    public RaceRun getRecentCompletedRun(int which) {
        RaceRun run = null;
        //    if (getActiveRuns().size() > 0) {
        //        run = getActiveRuns().get(0);
        //    }

        int size = getCompletedRuns().size();
        if ( size>=which ) {
            run = getCompletedRuns().get(size-which);
        }

        return(run);

    }






    public RaceRun getNewestActiveRun() {
        RaceRun run = null;
        int size = getActiveRuns().size();
        if (size > 0) {
            run = getActiveRuns().get(size-1);
        }
        return(run);

    }

    public RaceRun getOldestActiveRun() {
        RaceRun run = null;
        int size = getActiveRuns().size();
        if (size > 0) {
            run = getActiveRuns().get(0);
        }
        return(run);

    }



    //A150009 (ajm) End


    public long getRunsStartedOrCompletedCnt() {
        return runsStartedOrCompletedCnt;
    }

    public static final int MAX_COMPLETED_RUNS_IN_SCORING_PICKLIST = 2;
    public static final int MAX_ACTIVE_RUNS_IN_SCORING_PICKLIST = 2;


    private void addaFewCompletedRuns(ArrayList<RaceRun> scorable) {

       // add all runs for now
        try {

            for (RaceRun r:completedRuns)  {
                scorable.add(r);
            }
        }
        catch (Exception e ) {
            log.warn("BAD ARRAY EXCEPTION AJM");
        }
    }


    public ArrayList<RaceRun> getScorableRuns() {
        ArrayList<RaceRun> scorable = new ArrayList<RaceRun>();
        addaFewCompletedRuns(scorable);
        for (RaceRun r:activeRuns) {
            scorable.add(r);
        }

        return scorable;
    }


    public List<BoatEntry> getRemainingStartList() {
        return startList;
    }


    private int count(String bibNbr, String boatClass)  {
        int count = 0;
        for (BoatEntry boat:startList) {
            if (boat.getRacer().getBibNumber().trim().compareTo(bibNbr.trim()) == 0 &&
                boat.getBoatClass().compareTo(boatClass) == 0  ) {
                count ++;
            }

        }
        return count;
    }

    private String whoIs(String bibNbr, String boatClass)  {
        String who  = "";
        for (BoatEntry boat:startList) {
            if (boat.getRacer().getBibNumber().trim().compareTo(bibNbr.trim()) == 0 &&
                    boat.getBoatClass().compareTo(boatClass) == 0  ) {
                who = boat.getRacer().getBibNumber() + " " + boat.getBoatClass() + " " +  boat.getRacer().getShortName();
            }

        }
        return who;
    }



    public String lookForDuplicateBibsInTheSameClass() {
        String returnString = null;
        for (BoatEntry boat:startList) {
            if ( count(boat.getRacer().getBibNumber(),boat.getBoatClass()) > 1 ) {

                returnString = "Bib number " + boat.getRacer().getBibNumber() +  " occurs more than once in " + boat.getBoatClass();
                System.out.println(returnString);
                log.warn(returnString);
            }
        }
        return(returnString);
    }



    private XStream initXML() {
        XStream xstream = new XStream(new DomDriver());
        xstream.registerConverter(new XStreamRaceConverter());

        xstream.alias("race", Race.class);
        xstream.alias("run", RaceRun.class);
        xstream.alias("section", JudgingSection.class);
        xstream.alias("boatEntry", BoatEntry.class);
        xstream.alias("racer", Racer.class);

        return(xstream);
    }



    private void saveXML() {
        String filename = getName() + ".xml";

        File fileOut=null;
        FileOutputStream out=null;
        try {
            fileOut = new File(filename);
            out = new FileOutputStream(fileOut);
            String xml = xstream.toXML(this);
            out.write(xml.getBytes());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
               out.close();
            } catch (IOException e) {
            }
        }

        log.trace("Saved XML to " + filename);




        String xml = xstream.toXML(this);
 //       System.out.println(xml);
    }

    private Race loadXML() {
        String fileName = lastRace.getName();
//        String filename = getName() + ".xml";
        fileName+=".xml";
        char[] xml = new char[10000];
        Race race=null;

        File file=null;
        ObjectInputStream in = null;
       // BufferedReader in=null;
        try {
            ///file = new File(fileName);
//            FileInputStream fileIn = new FileInputStream(fileName);//"RaceRun.ser");

//            in = xstream.createObjectInputStream;//)OutputStream(aReader);

            Reader someReader = new FileReader(fileName);

             in = xstream.createObjectInputStream(someReader);
//            in = new BufferedReader(new FileReader(file));//InputStream(file);


          //  BufferedReader reader = new BufferedReader(new FileReader("/path/to/file.txt"));
          //  String line = null;
          // while ((line =

//          in.read(xml); ///readLine()) != null) {
                // ...


            // in = new ObjectInputStream(fileIn);
            deSerializeXML(in);

            in.close();



            //in.read(xml);
//            String xmlString = new String(xml);
//            race  = (Race)xstream.fromXML(xmlString);
//            System.out.println(race);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }

        return race;
        //Race race  = (Race)xstream.fromXML(xml);
    }



/*
    private void saveXMLEncoder() {
        XMLEncoder encoder=null;
        try{
            encoder=new XMLEncoder(new BufferedOutputStream(new FileOutputStream("Race.XML")));
        }catch(FileNotFoundException fileNotFound){
            System.out.println("ERROR: While Creating or Opening the File dvd.xml");
        }
        encoder.writeObject(this);
        encoder.close();

    }
*/

    //fixme todo change all to XML serialization, so class versions are NOT an issue !
    public void saveSerializedData() {


  //      saveXMLEncoder();

        saveXML();
        try
        {
            lastRace.setName(getName());
            lastRace.saveSerializedData();
            String filename = getName() + ".ser";

            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);  ///MUST FIX 2016 Nationals ERROR CONCurrentMpdificationException
            out.writeObject(this); // See   Ref#20161008 This was Line 913 BELOW on 20161008   //// This was line 869 in bwlow Log -->> Got CONCurrentMpdificationException ///20160727 in test
            /*
            PRIOR CRASH  Prior to 20161008  See bottom of source file for new carsh dump
at java.io.ObjectOutputStream.writeObject(ObjectOutputStream.java:329)
	at com.tcay.slalom.Race.saveSerializedData(Race.java:869)
	at com.tcay.slalom.Race.updateResults(Race.java:1177)
	at com.tcay.slalom.Race.updateResults(Race.java:1168)
	at com.tcay.slalom.RaceRun.updateResults(RaceRun.java:535)
	at com.tcay.slalom.RaceRun.setPhotoCellRaceRun(RaceRun.java:139)
	at com.tcay.slalom.Race.associatePhotoCellRun(Race.java:1163)
	at com.tcay.slalom.timingDevices.PhotoCellAgent.saveResult(PhotoCellAgent.java:57)
	at com.tcay.slalom.timingDevices.tagHeuer.TagHeuerAgent.processDeviceOutput(TagHeuerAgent.java:174)
	at com.tcay.RS232.PhotoEyePortListener.readAndProcess(PhotoEyePortListener.java:241)
	at com.tcay.RS232.PhotoEyePortListener.processPhotoEyeDataFromDevice(PhotoEyePortListener.java:190)
	at com.tcay.RS232.PhotoEyePortListener.listenAndProcessPortOutput(PhotoEyePortListener.java:304)
	at com.tcay.RS232.PhotoEyePortListener.run(PhotoEyePortListener.java:76)
            */

            out.close();
            fileOut.close();
            log.trace("Saved serialized data to " + filename);

        }catch(IOException i)
        {
            i.printStackTrace();
        }

    }




    public void loadSerializedData() {
//fixme        Race x = loadXML();
        //return;
        //lastRace.getName();
//        RaceRun run;
        try {
  //          lastRace.loadSerializedData();
            String fileName = lastRace.getName();
            FileInputStream fileIn = new FileInputStream(fileName +".ser");//"RaceRun.ser");
            try
            {
                ObjectInputStream in = new ObjectInputStream(fileIn);
                deSerialize(in);

                in.close();
                fileIn.close();

                tagHeuerConnected = new Boolean(false);    /// make sure it exists - transient object
                microgateConnected = new Boolean(false);   /// make sure it exists - transient object

            } catch ( InvalidClassException ice) {
                log.info("Invalid Class from deserialization " + ice.classname);
            } catch (EOFException eof) {
                log.info("EOF on Serialized data");
            } catch(IOException i) {
                i.printStackTrace();
            //} catch (ClassNotFoundException cnf) {
            //    cnf.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException fnf) {
              // Empty block OK - ignore this exception
        }

        // load required transient members
        Log raceRunLog = Log.getInstance();
        for (RaceRun r:activeRuns) {
            r.setLog(raceRunLog);
        }
        for (RaceRun r:completedRuns) {
            r.setLog(raceRunLog);
        }
        if (pendingRerun!=null)  {
            pendingRerun.setLog(raceRunLog);
        }




        //     updateResults();   //todo OK Here ???        NO - didn't set

    }

    private void deSerializeXML(ObjectInputStream in) {
        Object o;
        try {
            while ((o = in.readObject()) != null) {
                System.out.println(o);
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public Date getDate() {
        return date;
    }

    private void deSerialize(ObjectInputStream in) throws DuplicateBibException {
        Object o;
        try {
            while ((o = in.readObject()) != null) {
                Race raceFromSerialized = (Race)o;

                this.date = raceFromSerialized.date;
                this.name = raceFromSerialized.name;
                this.location = raceFromSerialized.location;
                setNbrGates(raceFromSerialized.nbrGates);//this.nbrGates = race.nbrGates;// = 25;
                this.upstreamGates = raceFromSerialized.upstreamGates;
                this.judgingSections = raceFromSerialized.judgingSections;
                for (JudgingSection s:judgingSections) {
                    // must assign all transients, otherwise they are null and cause problsm
                    s.setClientDeviceAttached(new Boolean(false));
                }

                // Race Status
                this.pendingRerun = raceFromSerialized.pendingRerun;
                this.activeRuns = raceFromSerialized.activeRuns;
                this.completedRuns = raceFromSerialized.completedRuns;

                this.startList = raceFromSerialized.startList;
                this.runsStartedOrCompletedCnt = raceFromSerialized.runsStartedOrCompletedCnt;
                this.currentRunIteration = raceFromSerialized.currentRunIteration;  // are we on 1st runs, or 2nd runs ?
                this.racers = raceFromSerialized.racers;
                this.tagHeuerEnabled = raceFromSerialized.tagHeuerEnabled; // todo REMOVE ->tagHeuerEmulation = raceFromSerialized.tagHeuerEmulation;
                this.microgateEnabled = raceFromSerialized.microgateEnabled;
                this.timyEnabled = raceFromSerialized.timyEnabled;

                this.icfPenalties = raceFromSerialized.icfPenalties;
            }


        }
        catch (InvalidClassException ice) {      // todo - change top XML race data to avoid cversion problems
            clearRace();
            System.out.println("All data cleared, incompatible race object version information");
        }
        catch (EOFException io) {
           // io.printStackTrace();

        } catch (IOException io) {
            io.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        String duplicates = lookForDuplicateBibsInTheSameClass();
        if (duplicates != null) {
            log.error(duplicates);
            throw new DuplicateBibException();
        }
    }


    public void askSampleDataIfNoRacersExist(JPanel panel)
    {
        if (Race.getInstance().getRacers().size() == 0) {
            int n = JOptionPane.showConfirmDialog(
                    panel,
                    "No Course or Racers Exist, would you like to load a sample race ?",
                    "Load Sample Data",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.YES_OPTION,
                    Race.getInstance().getSlalomCourseSmall());

            if (n == 0) {
                new TestData().load();
            }

        }

    }


    private boolean sameRun(RaceRun ourRun, RaceRun otherRun) {
        boolean same = false;
        BoatEntry b;
        Racer racer;
        String bib;
        try {
            b = ourRun.getBoat();
            racer = b.getRacer();
            bib = racer.getBibNumber();

            if (bib.compareTo(otherRun.getBoat().getRacer().getBibNumber()) == 0)  {
                if (ourRun.getRunNumber() == otherRun.getRunNumber()) {
                    same = true;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return(same);
    }

    private boolean sameRun(RaceRun ourRun, PhotoCellRaceRun otherRun) {
        boolean same = false;
        BoatEntry b;
        Racer racer;
        String bib;
        try {
            b = ourRun.getBoat();
            racer = b.getRacer();
            bib = racer.getBibNumber();

            if (bib.compareTo(otherRun.getBibNumber()) == 0)  {
                // We only want to store real time Tag Heuer electronic eye times
                // with the current run
                if (ourRun.getRunNumber() == otherRun.getRunNumber()) {
                    same = true;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return(same);
    }



    // fixme consolidate with associatePhotoCellRun() method
    public RaceRun findRun(RaceRun clientRaceRun) {

        RaceRun matchingRun = null;

        for (RaceRun r:activeRuns) {
            if (sameRun(r, clientRaceRun)) {
                matchingRun = r;
                break;
            }
        }

        if (matchingRun == null) {
            for (RaceRun r:completedRuns) {
                try {

                    if (sameRun(r, clientRaceRun)) {
                        matchingRun = r;
                        break;
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }
        return(matchingRun);
    }


    private BoatEntry boatInStartingBlock=null;
    public BoatEntry getBoatInStartingBlock() {
        return boatInStartingBlock;
    }
    public void setBoatInStartingBlock(BoatEntry boat) {
        boatInStartingBlock = boat;
    }



    public BoatEntry getBoatReadyToFinish() {
        RaceTimingUI timingUI =  RaceTimingUI.getInstance();
        return    timingUI.boatReadyToFinish();
    }


    /**
     * Joins PhotoCellRaceRun (Start Eye and Finish Eye time) with the associated RaceRun in the system
     * @param photoCellRun
     */
    public void associatePhotoCellRun(PhotoCellRaceRun photoCellRun) {

        RaceRun matchingRun = null;

        for (RaceRun r:activeRuns) {
            try {
                if (sameRun(r, photoCellRun)) {
                    matchingRun = r;
                   // log.info/*trace*/("Found bib#" + r.getBoat().getRacer().getBibNumber() + " r#"+ r.getRunNumber() +  " in Active Runs");

                }
            }
            catch (Exception e) {
                e.printStackTrace();

            }
        }

        if (matchingRun == null) {
            for (RaceRun r:completedRuns) {
                try {
                    if (sameRun(r, photoCellRun)) {
                        matchingRun = r;
                    //    log.trace("Found bib#" + r.getBoat().getRacer().getBibNumber() + " r#"+ r.getRunNumber() + " + in Completed Runs");
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }

        if (matchingRun != null ) {
            matchingRun.setPhotoCellRaceRun(photoCellRun);      ///MUST FIX 2016 Nationals ERROR CONCurrentMpdificationException
        }
    }



    public void updateResults(RaceRun run, boolean saveSerialized) {
        updateResults(saveSerialized);       ///MUST FIX 2016 Nationals ERROR CONCurrentMpdificationException
        racerResultsHTTP.outputWeb(run.getBoat());
    //M161006    scoreboardResultsHTTP_New.outputWeb("Sorted", getCompletedRunsByClassTime(), true);
    }


    public void updateResults(boolean saveSerialized) {
        results = accumulateResults(getCompletedRuns());
        ResultsTableModel.getInstance().updateResults();
        RunScoringTableModel.getInstance().updateResults();
if (saveSerialized)      {  saveSerializedData();   }            ///MUST FIX 2016 Nationals ERROR CONCurrentMpdificationException
//D161004        resultsHTTP.outputWeb("Sorted", getCompletedRunsByClassTime(), true);

//M161006
        scoreboardResultsHTTP_New.outputWeb("Sorted", getCompletedRunsByClassTime(), true);


    }


    public void printResultsHTTP(String boatClass, int runNbr) {
        someResultsHTTP.outputWeb("", getCompletedRunsByClassTime(), true, boatClass, runNbr);
    }

    private Result findRacer(RaceRun run){
        Result result = null;
        for (Result r:results) {
            if (r.getRacer() == run.getBoat().getRacer()) {
                result = r;
                break;
            }

        }

        return result;

    }


    private ArrayList<Result> accumulateResults(ArrayList<RaceRun> runs) {
        Result res;
        results = new ArrayList<Result>();

        for (RaceRun r:runs) {
            res = findRacer(r);
            if (res == null) {
                res = new Result();
                results.add(res);
            }
            res.setRacer(r.getBoat().getRacer());
            res.setBoat(r.getBoat());

            switch (r.getRunNumber()) {
                case 1:
                    res.setRun1(r);
                    break;
                case 2:
                    res.setRun2(r);
                    break;
            }
        }

        float run1Time;
        float run2Time;
        for (Result res1:results) {
            //run1Time = (float)999999.99;
            //run2Time = (float)999999.99;

            try {

                if (res1.getRun1() == null || res1.getRun2() == null ) {
                    if (res1.getRun1() == null) {
                        res1.setBestRun(res1.getRun2());
                    }
                    if (res1.getRun2() == null) {
                        res1.setBestRun(res1.getRun1());
                    }
                }
                else {
                    run1Time = res1.getRun1().getElapsed()+ res1.getRun1().getTotalPenalties();
                    run2Time = res1.getRun2().getElapsed()+ res1.getRun2().getTotalPenalties();
                    if ( run1Time <= run2Time ) {
                        res1.setBestRun(res1.getRun1());
                    }
                    else {
                        res1.setBestRun(res1.getRun2());
                    }
                }
            }
            catch (Exception e) {
                log.write(e);
            }
        }


        ArrayList<Result>sorted = Result.getResultsByClassTime(results);
        results = sorted;  //A10112013

        String lastBoatClass="";
        int place = 1;

        for (Result r:sorted) {
            try {
                r.getRun1().setGold(false);                       /// TODO: if skipping 1st runs fro some reason, this will cause a null pointer reference
                r.getRun1().setSilver(false);
                r.getRun1().setBronze(false);
            } catch (NullPointerException e) {
                // Intentionally empty exception block
            }

            try {
                r.getRun2().setGold(false);
                r.getRun2().setSilver(false);
                r.getRun2().setBronze(false);
            } catch (NullPointerException e) {
                // Intentionally empty exception block
            }

            // todo check this logic   20141122
            try {
                if (lastBoatClass.compareTo(r.getBoat().getBoatClass()) != 0 ) {
                    lastBoatClass = r.getBoat().getBoatClass();
                    place = 1;
                }
                switch (place) {
                    case 1:
                        r.getBestRun().setGold(true);
                        break;
                    case 2:
                        r.getBestRun().setSilver(true);
                        break;
                    case 3:
                        r.getBestRun().setBronze(true);
                        break;
                    default:
                        break;

                }
            } catch (NullPointerException e) {
               // Intentionally empty exception block
            }

            r.getBestRun().setPlaceInClass(place++);
        }
        return(sorted);
    }
    public String getHTTPGateHeadersString() {
        StringBuffer sb = new StringBuffer();

        for (int iGate = 1; iGate<=Race.getInstance().getNbrGates(); iGate++) {
            sb.append(String.format("<td>%d</td>", iGate));
        }

        return sb.toString();
    }

    public String getHTTPGateHeadersWidthString()
    {
        StringBuffer sb = new StringBuffer();

        for (int iGate = 1; iGate<=Race.getInstance().getNbrGates(); iGate++) {
            sb.append("<col span=\"1\" style=\"width: 3%;\">");
        }

        return sb.toString();
    }



    public void markClassRun(String boatClass, int runNbr, RaceRun.Status status) {
        for (RaceRun r : getCompletedRunsByClassTime()) {
            if (r.getRunNumber() == runNbr) {
                if (r.getBoat().getBoatClass().compareTo(boatClass) == 0) {
                    r.setStatus(status);
                }
            }
        }
        updateResults(true);
    }





    ArrayList<String> classesRuns;
    public ArrayList getClassesRuns() {
        classesRuns = new ArrayList<String>();

        StringBuffer classRun = new StringBuffer();
        StringBuffer testClassRun;

        for (int run = 1; run < 3; run++) {
            for (RaceRun r : getCompletedRunsByClassTime()) {
                if (r.getRunNumber() == run) {
                    testClassRun = new StringBuffer();
                    testClassRun.append(r.getBoat().getBoatClass());
                    testClassRun.append(":");
                    testClassRun.append(run);
                    testClassRun.append(":");
                    testClassRun.append(r.getStatusString());

                    if (testClassRun.toString().compareTo(classRun.toString()) != 0) {
                        classRun = testClassRun;
                        classesRuns.add(classRun.toString());
                    }

                }
            }
        }
        return(classesRuns);


    }


    long startEyeTripped = 0;
    long finishEyeTripped = 0;

    static int LED_ON_TIME = 1000;

    // Todo - Hook to the photo eye  code 170419
    public void triggerStartEyeLedIndicator() {
        startEyeTripped = System.currentTimeMillis();
    }


    public void triggerFinishEyeLedIndicator() {
        finishEyeTripped = System.currentTimeMillis();
    }



    public void checkLEDs() {
        isStartEyeTripped();
        isFinishEyeTripped();
    }


    // todo refactor into photoEyeListener or photoCellAgent
    private boolean isFinishEyeTripped() {

        boolean tripped = false;
        if (finishEyeTripped>0) {
            if (System.currentTimeMillis() - finishEyeTripped <= LED_ON_TIME) {
                tripped = true;
            }
        }
        if (System.currentTimeMillis() - finishEyeTripped > LED_ON_TIME) {
            finishEyeTripped = 0;
        }

        if (tripped) {
            finishEyeLED.setIsOn(true);
        } else {
            finishEyeLED.setIsOn(false);
        }

        return tripped;
    }


    // todo refactor into photoEyeListener or photoCellAgent
    private boolean isStartEyeTripped() {

        boolean tripped = false;
        if (startEyeTripped>0) {
            if (System.currentTimeMillis() - startEyeTripped <= LED_ON_TIME) {
                tripped = true;
            }
        }
        if (System.currentTimeMillis() - startEyeTripped > LED_ON_TIME) {
            startEyeTripped = 0;
        }


        if (tripped) {
            startEyeLED.setIsOn(true);
        } else {
            startEyeLED.setIsOn(false);
        }

        return tripped;
    }

    // todo refactor into photoEyeListener or photoCellAgent
    public LedIndicator getStartEyeLED() {
        return(startEyeLED);
    }

    // todo refactor into photoEyeListener or photoCellAgent
    public LedIndicator getFinishEyeLED() {
        return(finishEyeLED);
    }






}
/*


20161008 US Nationals Crash Start
        SPEN B=237 r=2                                0  0  0  0  0
        VSTOP B=238 r=2 rawElapsed=   134.01 rawMANUAL=   134.01
        Exception in thread "Thread-8" java.util.ConcurrentModificationException
        at java.util.ArrayList.writeObject(ArrayList.java:766)
        at sun.reflect.GeneratedMethodAccessor6.invoke(Unknown Source)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.lang.reflect.Method.invoke(Method.java:498)
        at java.io.ObjectStreamClass.invokeWriteObject(ObjectStreamClass.java:1028)
        at java.io.ObjectOutputStream.writeSerialData(ObjectOutputStream.java:1496)
        at java.io.ObjectOutputStream.writeOrdinaryObject(ObjectOutputStream.java:1432)
        at java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1178)
        at java.io.ObjectOutputStream.defaultWriteFields(ObjectOutputStream.java:1548)
        at java.io.ObjectOutputStream.writeSerialData(ObjectOutputStream.java:1509)
        at java.io.ObjectOutputStream.writeOrdinaryObject(ObjectOutputStream.java:1432)
        at java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1178)
        at java.io.ObjectOutputStream.writeObject(ObjectOutputStream.java:348)
        at com.tcay.slalom.Race.saveSerializedData(Race.java:913)
        at com.tcay.slalom.Race.updateResults(Race.java:1239)
        at com.tcay.slalom.Race.updateResults(Race.java:1229)
        at com.tcay.slalom.socket.Server$ObjectServerReader.addPenaltiesFromClient(Server.java:407)
        at com.tcay.slalom.socket.Server$ObjectServerReader.run(Server.java:464)
        at java.lang.Thread.run(Thread.java:745)
        SPEN B=239 r=2


        VSTOP B=238 r=2 rawElapsed=   134.01 rawMANUAL=   134.01
        Exception in thread "Thread-8" java.util.ConcurrentModificationException
        at java.util.ArrayList.writeObject(ArrayList.java:766)
        at sun.reflect.GeneratedMethodAccessor6.invoke(Unknown Source)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.lang.reflect.Method.invoke(Method.java:498)
        at java.io.ObjectStreamClass.invokeWriteObject(ObjectStreamClass.java:1028)
        at java.io.ObjectOutputStream.writeSerialData(ObjectOutputStream.java:1496)
        at java.io.ObjectOutputStream.writeOrdinaryObject(ObjectOutputStream.java:1432)
        at java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1178)
        at java.io.ObjectOutputStream.defaultWriteFields(ObjectOutputStream.java:1548)
        at java.io.ObjectOutputStream.writeSerialData(ObjectOutputStream.java:1509)
        at java.io.ObjectOutputStream.writeOrdinaryObject(ObjectOutputStream.java:1432)
        at java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1178)
        at java.io.ObjectOutputStream.writeObject(ObjectOutputStream.java:348)
        at com.tcay.slalom.Race.saveSerializedData(Race.java:913)
        at com.tcay.slalom.Race.updateResults(Race.java:1239)
        at com.tcay.slalom.Race.updateResults(Race.java:1229)
        at com.tcay.slalom.socket.Server$ObjectServerReader.addPenaltiesFromClient(Server.java:407)
        at com.tcay.slalom.socket.Server$ObjectServerReader.run(Server.java:464)
        at java.lang.Thread.run(Thread.java:745)
        SPEN B=239 r=2




        20161008 US Nationals Crash End




Ref#20161008

the writeObject is causing concurrency issues coming from both the Race Timing Thread and the Client Penalty Thread
20161008 - short term resolve by removing the saveSerializedData call from the Client.  The penalties will get saved with the next timer object

 FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);  ///MUST FIX 2016 Nationals ERROR CONCurrentMpdificationException
            out.writeObject(this); // See   Ref#20161008 This was Line 913 BELOW on 20161008   //// This was line 869 in bwlow Log -->> Got CONCurrentMpdificationException ///20160727 in test
            /*



*/

