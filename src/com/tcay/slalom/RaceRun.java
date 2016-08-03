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

import com.tcay.slalom.timingDevices.PhotoCellRaceRun;
import com.tcay.slalom.UI.tables.ResultsTable;
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

    /// To support NRC race timer and other trining devices
    public void setStartMillis(Long startMillis) {    //todo change access to protected or other improvement
        stopWatch.setPassedInStartTime(startMillis);
    }

    public void setStopMillis(Long stopMillis) {    //todo change access to protected or other improvement
        stopWatch.setPassedInStopTime(stopMillis);
    }

    /// todo fixme TEMPORARY
    public StopWatch getStopWatch() {
        return stopWatch;
    }

    private StopWatch stopWatch = null;     /// implement splits in stopWatch
    private ArrayList<Penalty> penaltyList = null;
    private boolean dnf = false;

    public boolean isDns() {
        return dns;
    }

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


    //
//    private transient PhotoCellRaceRun photoCellRaceRun;
    private /* TODO Investigate "transient" */ PhotoCellRaceRun photoCellRaceRun;
    private transient Log log;


    /*

        public String getPENLogString() {
            StringBuffer penaltyLog = new StringBuffer();

            penaltyLog.append("PEN   B=" + getBoat().getRacer().getBibNumber());
            penaltyLog.append(" r=" + getRunNumber());

            return penaltyLog.toString();

        }
    */
    public String getLogString() {
        StringBuffer penaltyLog = new StringBuffer();

        penaltyLog.append("B=" + getBoat().getRacer().getBibNumber());
        penaltyLog.append(" r=" + getRunNumber());

        return penaltyLog.toString();

    }





    public void setLog(Log log) {
        this.log = log;
    }

    /**
     *
     * @return the TagHeuer electronic timing information object for this RaceRun
     */
    public PhotoCellRaceRun getPhotoCellRaceRun() {

        return photoCellRaceRun;
    }


    /**
     *
     * @param photoCellRaceRun -the TagHeuer electronic timing information object
     *                        associated with this RaceRun.
     */
    public void setPhotoCellRaceRun(PhotoCellRaceRun photoCellRaceRun) {//PhotoCellRaceRun photoCellRaceRun) {
        this.photoCellRaceRun = photoCellRaceRun;
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
        stopWatch = new StopWatch("Bib#" + boat.getRacer().getBibNumber());
        penaltyList = new ArrayList<Penalty>();
    }


    /**
     *
     * @param boat
     */
    public RaceRun(BoatEntry boat) {
        init(boat);
        runNumber = Race.getInstance().getCurrentRunIteration();   // todo verify
        log.info("NEW RaceRun(b)" + this);

    }

    /**
     *
     * @param boat
     * @param runNumber
     */
    public RaceRun(BoatEntry boat, int runNumber) {

        init(boat);
        this.runNumber = runNumber;   // todo verify
        log.info("NEW RaceRun(b,r)" + this);   /// RERUNS COME HERE  ... WHY DOES Run 1 get Wiped OUT ????
    }


    //(ajm) 150519
    public RaceRun() {

        log = Log.getInstance();

        init(new BoatEntry(new Racer("String1", "String2","String3","String4"), "TEST"));
        log.info("NEW RaceRun()" + this);

    }

    /**
     * start the clock on this RaceRun
     */
    public void start() {
        stopWatch.start();
        log.info("VSTRT " + getLogString());//getBoat().getRacer().getBibNumber() + " r=" + getRunNumber());
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

        log.info("VSTOP " + getLogString() //+getBoat().getRacer().getBibNumber() + " " +
                //"r=" + getRunNumber()
                        + " rawElapsed=" + getRawElapsedTimeString() + " rawMANUAL=" + getManualRawElapsedTimeString());




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


    public String getPenaltyString() {
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

    public String getPenaltyStringBlankIfNone() {
        String s = null;
        if (dns || dnf) {
            s = "      ";
        }
        else {
            long penalties = getTotalPenalties();
            if (penalties!=0) {
                StringBuffer sb = new StringBuffer();
                sb.append("+").append(penalties);
                s = String.format("%1$6s", sb);
            }
            else {
                s="";
            }
        }
        return s;
    }


    private String getManualRawElapsedTimeString() {

        StringBuffer sb = new StringBuffer(30);
        float time = getManualElapsed();
        sb.append( String.format(" %1$8.2f", time));

        return sb.toString();
    }


    private String getRawElapsedTimeString() {

        StringBuffer sb = new StringBuffer(30);
        float time = getElapsed();


        //if (photoCellRaceRun == null || photoCellRaceRun.getElapsedTime() == 0) {
        //    sb.append("*");
       // }


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
            if (photoCellRaceRun != null && photoCellRaceRun.getElapsedTime() != 0) {
                result = (float)photoCellRaceRun.getElapsedTime();
            }
            else {
                result = stopWatch.getElapsed();   /// todo investigate EYE Start with APP FINISH gives stopwatch time until we get EYE finish

            }
        }

        return result; //stopWatch.getElapsed();
    }

/*
    public String getElapsedLogString() {
float eTime=0.0f;
        String result=null;//(float)9999.0;
        if (!dnf) {
            if (photoCellRaceRun != null && photoCellRaceRun.getElapsedTime() != 0) {
                eTime = (float)photoCellRaceRun.getElapsedTime();
                result = new String("+"+eTime);
            }
            else {
                result = new String("*" + stopWatch.getElapsed());
            }
        }

        return result;
    }


*/
    public String toString() {
//        return( //boat.getRacer().getBibNumber() +
//                "run#" + getRunNumber() + " " + boat.getRacer().getShortName() );

        return toStringBibRunShortName()  ;
    }


    private String toStringBibRunShortName() {
        return( boat.getRacer().getBibNumber() +
                "run#" + getRunNumber() + " " + boat.getRacer().getShortName() + (isDnf()?" DNF":"") + (isDns()?" DNS":""));
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
        Penalty penalty = null;

        for (Penalty p : penaltyList) {
            if (gate == p.gate) {
                penalty = p;
                break;
            }
        }
        if (penalty==null) {
            log.trace("setPenalty():"+"Requested penalty for gate (NONE FOUND)" + gate + " on bib#" + boat.getRacer().getBibNumber() + " run#" + runNumber );
        } else {
            log.trace("setPenalty():" + "PENALTY RECALLED  (" + penalty.getPenaltySeconds() + ") for gate (FOUND)" + gate + " on bib#" + boat.getRacer().getBibNumber() + " run#" + runNumber );

        }

        return penalty;
    }
// called from both server and remote client
    public void setPenalty(int gate, int seconds, boolean fromClient) {
        Penalty penalty = getPenalty(gate);
        boolean foundIt = false;

        if (penalty == null ) {
            penalty = new Penalty(gate, seconds, fromClient);
            penaltyList.add(penalty);
        } else {
            foundIt = true;
            penalty.setPenaltySeconds(seconds);
            penalty.setFromClient(fromClient);
        }
        log.trace("setPenalty("  + (fromClient?"@Client":"@Server")  + "):"+(foundIt?"CHANGE ":"NEW    ") + "RR.SetPenalty bib#" + boat.getRacer().getBibNumber() + " r#" + runNumber + " " + penalty.getSummary());
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
        Race.getInstance().updateResults(this);
        //Race.getInstance().updateResults();
    }

    public static final String TIME_EXPANDED_FILL = "                        ";
    public static final String TIME_ONLY_FILL ="         ";

    public String formatTimeExpanded() {
        StringBuffer sb = new StringBuffer();


        StringBuffer sbIimes = new StringBuffer();

        sb.append( String.format(getResultString()) );
        sb.append(getPenaltyString());
        sb.append(getTotalTimeString());

        String suffix = " ";
        if (getPhotoCellRaceRun() != null) {
            suffix = getPhotoCellRaceRun() != null ? ResultsTable.TIMINGMODE_AUTOMATIC: ResultsTable.TIMINGMODE_MANUAL;
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

        sb.append(getTotalTimeString());
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


    public static String padRight(String s, int n) {
        String sRet="";
        if (n > 0 ) {
            sRet = String.format("%1$-" + n + "s", s);
        }
        return sRet;
    }
    public void logPenalties(Log reqLog, int section, String source ) {

        String logString = penaltyStringExtended(source,section);
//        sb.append(source+" ");
//        sb.append(getLogString());
//        sb.append(padRight(" ", section * 15));

//        for (Penalty p:getPenaltyList()) {
//            if ( Race.getInstance().isGateInSection(p.getGate(), section)) {
//                sb.append(String.format(" %2d", p.getPenaltySeconds()));
//            }
 //       }

        reqLog.info(logString);//sb.toString());
    }


    public String penaltyStringExtended(String source, int section)
    {

        StringBuffer sb = new StringBuffer();

        sb.append(source+" ");
        sb.append(getLogString());
        sb.append(padRight(" ", section * 15));

        for (Penalty p:getPenaltyList()) {
            if ( Race.getInstance().isGateInSection(p.getGate(), section)) {
                sb.append(String.format(" %2d", p.getPenaltySeconds()));
            }
        }

        return sb.toString();


    }

    public String penaltyStringExtended()
    {
        StringBuffer sb = new StringBuffer();
        Penalty p;



        for (int iGate = 1; iGate<=Race.getInstance().getNbrGates(); iGate++) {
            p = getPenalty(iGate);
            if (p!=null) {
                sb.append(String.format(" %2d", p.getPenaltySeconds()));
            } else {
                sb.append("   ");
            }


        }
        return sb.toString();
    }



    public String penaltyStringHTMLExtended()
    {
        StringBuffer sb = new StringBuffer();
        Penalty p;



        for (int iGate = 1; iGate<=Race.getInstance().getNbrGates(); iGate++) {
            p = getPenalty(iGate);
            if (p!=null) {
                sb.append(String.format("<td>%2d</td>", p.getPenaltySeconds()));
            } else {
                sb.append("<td></td>");
            }


        }
        return sb.toString();
    }




}
