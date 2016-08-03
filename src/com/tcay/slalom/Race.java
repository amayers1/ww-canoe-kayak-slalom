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

import com.tcay.RS232.PhotoEyeListener;
import com.tcay.slalom.UI.RaceTimingUI;
import com.tcay.slalom.UI.http.SlalomRacerResultsHTTP;
import com.tcay.slalom.UI.http.SlalomResultsHTTP;
import com.tcay.slalom.timingDevices.PhotoCellAgent;
import com.tcay.slalom.timingDevices.PhotoCellRaceRun;
import com.tcay.util.DuplicateBibException;
import com.tcay.util.Log;
import com.tcay.slalom.UI.JudgingSection;
import com.tcay.slalom.UI.tables.ResultsTableModel;
import com.tcay.slalom.UI.tables.RunScoringTableModel;
///import com.tcay.util.XStreamRaceConverter;
//import com.thoughtworks.xstream.XStream;
//import com.thoughtworks.xstream.io.xml.DomDriver;

import javax.swing.*;
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
        //      if (microgateEnabled) {
        return microgateII;
        //      }
        //      return null;


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
    private List<BoatEntry> startList = null;     // todo on Crash recovery don't have an yet-to-be-started list, only all racers
    private ArrayList<RaceRun> activeRuns = null;
    private long runsStartedOrCompletedCnt = 0;
    private int currentRunIteration;  // are we on 1st runs, or 2nd runs ?
    private ArrayList<RaceRun> completedRuns = null;


    // Transient Objects

    // Move these to SlalomApp or elsewhere in a device handler object
    private transient Thread photoEyeThread;     // Thread to get input from Photocells
    private transient PhotoEyeListener photoEyeListener;   // Tag Heuer CP 520/540 listener/command processor


    private transient ArrayList<Result> results = new ArrayList<Result>();
    private transient LastRace lastRace;

    public Boolean getTagHeuerEnabled() {
        return tagHeuerEnabled;
    }

    public Boolean getMicrogateEnabled() {
        return microgateEnabled;
    }

    public void setTagHeuerEnabled(Boolean tagHeuerEnabled) {
        this.tagHeuerEnabled = tagHeuerEnabled;
    }

    public void setMicrogateEnabled(Boolean microgateEnabled) {
        this.microgateEnabled = microgateEnabled;
    }

    public void setTimyEnabled(Boolean timyEnabled) {
        this.timyEnabled = timyEnabled;
    }

    private Boolean tagHeuerEnabled = false;
    private Boolean microgateEnabled = false;
    private Boolean timyEnabled = false;

    private transient Boolean tagHeuerConnected;
    private transient Boolean microgateConnected;
    private transient Log log;
    //    private transient XStream xstream;
    private transient SlalomResultsHTTP resultsHTTP;
    private transient SlalomRacerResultsHTTP racerResultsHTTP;


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


    public void setPhotoCellAgent(PhotoCellAgent photoCellAgent) {
        this.photoCellAgent = photoCellAgent;
    }

    public void setMicrogateConnected(Boolean microgateConnected) {
        this.microgateConnected = microgateConnected;
    }

    public boolean isIcfPenalties() {
        return icfPenalties;
    }

    public void setIcfPenalties(boolean icfPenalties) {
        this.icfPenalties = icfPenalties;
    }

//    public Thread getPhotoCellThread() {
//        return photoEyeThread;
//    }

//    public void setPhotoCellThread(Thread photoEyeThread) {
//        this.photoEyeThread = photoEyeThread;
//    }


    public boolean TODOKludgeStartFRomPhotoEye(String bibNumber) {
        boolean started = false;
        RaceTimingUI timingUI =  RaceTimingUI.getInstance();
        if (timingUI.startButtonActionHandlerFromPhotoEye(bibNumber)) {
            started = true;
        }
        return started;
    }


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

        if (photoEyeThread == null) {

            photoEyeListener = new PhotoEyeListener();
            photoEyeThread = new Thread(photoEyeListener);
            photoEyeThread.start();

            photoCellAgent = photoEyeListener.getAgent();

        }
        else {
            log.info("ALREADY HAVE A THREAD HERE !!!");
        }

    }


    public void setUpstreamGates(List<Integer> upstreamGates) {
        this.upstreamGates = upstreamGates;
    }

    public PhotoEyeListener getPhotoEyeListener() {
        return photoEyeListener;
    }

    public void setPhotoEyeListener(PhotoEyeListener photoEyeListener) {
        this.photoEyeListener = photoEyeListener;
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


    protected void clearRace() {
        racers = new ArrayList<Racer>();
        startList = new ArrayList<BoatEntry>();
        activeRuns = new ArrayList<RaceRun>();
        completedRuns = new ArrayList<RaceRun>();
        judgingSections = new ArrayList<JudgingSection>();
        upstreamGates = new ArrayList<Integer>();
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




    private Race() {
        if (trainingMode==1) {
            MAX_RUNS_ALLOWED = 99;
        }

        log = Log.getInstance();
      //  xstream = initXML();   D20160407 RIO CAUSING CRASH
        clearRace();

        lastRace = new LastRace();
        tagHeuerConnected = false;//new Boolean(false);
        microgateConnected = false;
        resultsHTTP = new SlalomResultsHTTP();
        // todo set up on timing page to test and then enable CP520
//C160315        Thread t = new Thread( photoEyeListener = new PhotoEyeListener());
//C160315        t.start();

        //TODO - determine if any photo eyes in use, add appropriate handler/listener
        //D20160305maybeStartPhotoCellInterface();  Was being called in ClietnPenalty App

        racerResultsHTTP = new SlalomRacerResultsHTTP();
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
        synchronized (activeRuns) {
            activeRuns.add(newRun);
            runsStartedOrCompletedCnt++;   /// count of total racer starts

            System.out.println("CURRENTLY " + activeRuns.size() + " ACTIVE RUNS");
        }
    }


    public void finishedRun(RaceRun run) {
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


/*
    private XStream initXML() {
        XStream xstream = new XStream(new DomDriver());
        xstream.registerConverter(new XStreamRaceConverter());

        xstream.alias("race", Race.class);
//        xstream.alias("run", RaceRun.class);
//        xstream.alias("section", JudgingSection.class);
//        xstream.alias("boatEntry", BoatEntry.class);
//        xstream.alias("racer", Racer.class);

        return(xstream);
    }
*/

/*
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

*/

    //fixme todo change all to XML serialization, so class versions are NOT an issue !
    public void saveSerializedData() {

        //saveXML();
        try
        {
            lastRace.setName(getName());
            lastRace.saveSerializedData();
            String filename = getName() + ".ser";

            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);   //// This was line 869 in bwlow Log -->> Got CONCurrentMpdificationException ///20160727 in test
            /*
at java.io.ObjectOutputStream.writeObject(ObjectOutputStream.java:329)
	at com.tcay.slalom.Race.saveSerializedData(Race.java:869)
	at com.tcay.slalom.Race.updateResults(Race.java:1177)
	at com.tcay.slalom.Race.updateResults(Race.java:1168)
	at com.tcay.slalom.RaceRun.updateResults(RaceRun.java:535)
	at com.tcay.slalom.RaceRun.setPhotoCellRaceRun(RaceRun.java:139)
	at com.tcay.slalom.Race.associatePhotoCellRun(Race.java:1163)
	at com.tcay.slalom.timingDevices.PhotoCellAgent.saveResult(PhotoCellAgent.java:57)
	at com.tcay.slalom.timingDevices.tagHeuer.TagHeuerAgent.processDeviceOutput(TagHeuerAgent.java:174)
	at com.tcay.RS232.PhotoEyeListener.readAndProcess(PhotoEyeListener.java:241)
	at com.tcay.RS232.PhotoEyeListener.processPhotoEyeDataFromDevice(PhotoEyeListener.java:190)
	at com.tcay.RS232.PhotoEyeListener.listenAndProcessPortOutput(PhotoEyeListener.java:304)
	at com.tcay.RS232.PhotoEyeListener.run(PhotoEyeListener.java:76)
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
                this.microgateEnabled = raceFromSerialized.timyEnabled;

                this.icfPenalties = raceFromSerialized.icfPenalties;
            }


        }
        catch (InvalidClassException ice) {
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
            matchingRun.setPhotoCellRaceRun(photoCellRun);
        }
    }

    public void updateResults(RaceRun run) {
        updateResults();
        racerResultsHTTP.outputWeb(run.getBoat());
    }


    public void updateResults() {
        results = accumulateResults(getCompletedRuns());
        ResultsTableModel.getInstance().updateResults();
        RunScoringTableModel.getInstance().updateResults();
        saveSerializedData();
        resultsHTTP.outputWeb("Sorted", getCompletedRunsByClassTime(), true);
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


}

