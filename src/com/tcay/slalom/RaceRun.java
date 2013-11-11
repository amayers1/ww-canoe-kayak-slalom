package com.tcay.slalom;

import com.tcay.slalom.UI.tables.ResultsTable;
import com.tcay.slalom.tagHeuer.TagHeuerRaceRun;
import com.tcay.timing.StopWatch;
import com.tcay.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Teton Cay Group, Inc.
 * User: allen
 * Date: Aug 21, 2013
 * Time: 5:13:17 PM
 */


/**
 * RaceRun is the class that represents a run for an actual boat/paddler(s) entry in the race
 *
 * @author      Allen Mayers
 * @version     %I%, %G%
 * @since       1.0
 */
public class  RaceRun implements Comparable<RaceRun>,Serializable {

    private BoatEntry boat;
    private StopWatch stopWatch = null;     /// implement splits in stopWatch
    private ArrayList<Penalty> penaltyList = null;
    private boolean dnf = false;
    private boolean dns = false;
    private int runNumber;
    private boolean gold;
    private boolean silver;
    private boolean bronze;

    public int getPlaceInClass() {
        return placeInClass;
    }

    public void setPlaceInClass(int placeInClass) {
        this.placeInClass = placeInClass;
    }

    private int placeInClass;



    private transient TagHeuerRaceRun tagHeuerRaceRun;
    private transient Log log;


    public void setLog(Log log) {
        this.log = log;
    }

    /**
     *
     * @return the TagHeuer electronic timing information object for this RaceRun
     */
    public TagHeuerRaceRun getTagHeuerRaceRun() {

        return tagHeuerRaceRun;
    }


    /**
     *
     * @param tagHeuerRaceRun -the TagHeuer electronic timing information object
     *                        associated with this RaceRun.
     */
    public void setTagHeuerRaceRun(TagHeuerRaceRun tagHeuerRaceRun) {
        this.tagHeuerRaceRun = tagHeuerRaceRun;
        updateResults();
    }

    /**
     *
     * @return the run number (i.e. typically only run#1 or run#2) for this RaceRun.
     * todo: maximum runs will be configurable in the future
     */
    public int getRunNumber() {
        return runNumber;
    }


    /**
     *
     * @return list of penalties associated with this run
     */
    public ArrayList<Penalty> getPenaltyList() {
        return penaltyList;
    }



    public void clearPenaltyList() {
        penaltyList = null;
        penaltyList = new ArrayList<Penalty>();
    }


    /**
     *
     * @return the BoatEntry for this RaceRun
     */
    public BoatEntry getBoat() {
        return boat;
    }


    private void init(BoatEntry boat) {
        log = Log.getInstance();
        this.boat = boat;
        stopWatch = new StopWatch();
        penaltyList = new ArrayList<Penalty>();
    }


    /**
     *
     * @param boat
     */
    public RaceRun(BoatEntry boat) {
        init(boat);
        runNumber = Race.getInstance().getCurrentRunIteration();   // todo verify
    }

    /**
     *
     * @param boat
     * @param runNumber
     */
    public RaceRun(BoatEntry boat, int runNumber) {
        init(boat);
        this.runNumber = runNumber;   // todo verify
    }


    public RaceRun() {

        log = Log.getInstance();

    }

    /**
     * start the clock on this RaceRun
     */
    public void start() {
        stopWatch.start();
        Race.getInstance().addRun(this);
        updateResults();

    }


    /**
     *
     * @return if this RaceRun is completed
     */
    public boolean isComplete() {
        return stopWatch.isStopped();
    }

    /**
     * set this RaceRun to DNF (Did Not Finish)  status
     */
    public void dnf() {
        dnf = true;
        //stopWatch.setDNF();
        Race.getInstance().finishedRun(this);
        updateResults();


    }


    /**
     *
     * @return is this RaceRun a DNF
     */
    public boolean isDnf() {
        return dnf;
    }


    public void dns() {
        dns = true;
        //stopWatch.setDNF();
        Race.getInstance().finishedRun(this);
        updateResults();

    }


    public void finish() {
        stopWatch.stop();
        Race race = Race.getInstance();
        race.finishedRun(this);

        updateResults();

    }


//    public void loadAppFromSerializedData() {
//        Race.getInstance().finishedRun(this);
//        updateResults();
//    }


    public static final String DNS = "DNS";
    public static final String DNF = "DNF";

    /**
     *
     * @return either the raw time, or DNS, DNF as appropriate
     */
    public String getResultString() {
        String s = null;
        if (dns) {
           s = DNS;//"      DNS";
        }
        else if (dnf) {
           s = DNF;//"      DNF";

        }
        else {
            s = getRawElapsedTimeString();
        }
        return s;
    }

    /**
     *
     * @return
     */
    public String getTotalTimeString() {

        StringBuffer sb = new StringBuffer();
        if (dns || dnf) {
            //sb.append("         ");
            if (dnf) {
                sb.append("DNF");

            }
            else if (dns) {
                sb.append("DNS");
            }
        }
        else
        {
            float totalTime = getTotalPenalties();
            totalTime += getElapsed();
            sb.append( String.format(" %1$8.2f", totalTime));
        }
        return (sb.toString());
    }


    private String getPenaltyString() {
        String s = null;
        if (dns || dnf) {
            s = "      ";
        }
        else {
            long penalties = getTotalPenalties();
            StringBuffer sb = new StringBuffer();
            sb.append("+").append(penalties);
            s = String.format( "%1$6s", sb);
        }
        return s;
    }



    private String getRawElapsedTimeString() {

        StringBuffer sb = new StringBuffer(30);
        float time = getElapsed();
        sb.append( String.format(" %1$8.2f", time));

        return sb.toString();
    }


    /**
     *
     * @return the manual elapsed time for the run. Does NOT return
     * any Electronic time (i.e. Tag Heuer CP-520)
     *
     */
    private float getManualElapsed() {
        return stopWatch.getElapsed();
    }


    // provides the most 'accurate' time, TagHeuer if available or manual if not

    /**
     *
     * @return the elapsed time for the run. Electronic time is returned if
     *           available, otherwise manual 'stopwatch' time from the app is
     *           returned.
     *
     */
    public float getElapsed() {

        float result = (float)9999.0;
        if (!dnf) {
            if (tagHeuerRaceRun != null && tagHeuerRaceRun.getElapsedTime() != 0) {
                result = (float)tagHeuerRaceRun.getElapsedTime();
            }
            else {
                result = stopWatch.getElapsed();
            }
        }

        return result; //stopWatch.getElapsed();
    }


    public String toString() {
//        return( //boat.getRacer().getBibNumber() +
//                "run#" + getRunNumber() + " " + boat.getRacer().getShortName() );

        return toStringBibRunShortName()  ;
    }


    private String toStringBibRunShortName() {
        return( boat.getRacer().getBibNumber() +
                "run#" + getRunNumber() + " " + boat.getRacer().getShortName() );
    }

    public String getPenaltyString(int gate) {
        String penalty = "  ";
        if ( getPenalty(gate) != null) {
           int p = getPenalty(gate).getPenaltySeconds();
            penalty = String.format("%2d",p);
        }
        return penalty;
    }

    private Penalty getPenalty(int gate) {
        Penalty penalty = null;//new Penalty(gate,0);//null;   todo Dummied up for now for ScoringTable

        for (Penalty p:penaltyList)     {
            if (gate == p.gate) {
                penalty = p;
                break;
            }
        }

        return penalty;
    }

    public void setPenalty(int gate, int seconds, boolean fromClient) {
        Penalty penalty = getPenalty(gate);
        if (penalty == null ) {
            penalty = new Penalty(gate, seconds, fromClient);
            penaltyList.add(penalty);
        }
        else {
            penalty.setPenaltySeconds(seconds);
            penalty.setFromClient(fromClient);
        }
        log.info(penalty.getSummary());
        //updateResults();  this is done in Proxy no 131028 (ajm)
    }

    public int getTotalPenalties() {
        int penaltySecs = 0;

        for (Penalty p:penaltyList)     {
            penaltySecs += p.penaltySeconds;
        }
        return penaltySecs;
    }


    @Override
    public int compareTo(RaceRun r) {                         /// sort by run#, class and total time




        float j = (this.getBoat().getBoatClass().compareTo(r.getBoat().getBoatClass())) * 1000000;

        float i = (int)( (  (this.getElapsed()+this.getTotalPenalties()) -
                          ( r.getElapsed()+r.getTotalPenalties() )) * 100.0 );
        return (int)(j+i);
    }


    private void updateResults() {
        Race.getInstance().updateResults();
    }

    public static final String TIME_EXPANDED_FILL = "                        ";
    public static final String TIME_ONLY_FILL ="         ";

    public String formatTimeExpanded() {
        StringBuffer sb = new StringBuffer();


        StringBuffer sbIimes = new StringBuffer();

//        if (run != null) {
            sb.append( String.format(getResultString()) );
            sb.append(getPenaltyString());
            sb.append(getTotalTimeString());
//        }
//        else {
//            sb.append( "                        ");   TODO handle for run==null   at caller ?
//        }

        String suffix = " ";
        if (getTagHeuerRaceRun() != null) {
            suffix = getTagHeuerRaceRun() != null ? ResultsTable.TIMINGMODE_AUTOMATIC: ResultsTable.TIMINGMODE_MANUAL;
        }
        sb.append(suffix);



        sbIimes.append(  "  >>" );
        sbIimes.append(  (new Float(getManualElapsed())).toString()  );
        sbIimes.append(  " man. vs. "  );

        sbIimes.append(  (new Float(getElapsed())).toString()  );
        sbIimes.append(  " 'eye' time << "  );


        if ( getManualElapsed() != getElapsed() )  {
              Double diff = new Double((getElapsed() - getManualElapsed()));

             sbIimes.append(  " diff= "  );
             sbIimes.append(  String.format("%7.2f", diff ));     // todo test, need toString()
        }


        sb.append(sbIimes);

        return( sb.toString());
    }

    public String formatTimeTotalOnly() {
        StringBuffer sb = new StringBuffer();

//        if (run != null) {
            sb.append(getTotalTimeString());
//        }
//        else {
//            sb.append( "         ");        todo handle null Runs at caller?
//        }
        return( sb.toString());
    }

    public boolean isGold() {
        return gold;
    }

    public void setGold(boolean gold) {
        this.gold = gold;
    }

    public boolean isSilver() {
        return silver;
    }

    public void setSilver(boolean silver) {
        this.silver = silver;
    }

    public boolean isBronze() {
        return bronze;
    }

    public void setBronze(boolean bronze) {
        this.bronze = bronze;
    }
}
