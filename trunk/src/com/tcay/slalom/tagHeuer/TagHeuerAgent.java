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

package com.tcay.slalom.tagHeuer;

import com.tcay.RS232.CandidateCP520Port;
import com.tcay.slalom.Race;
import com.tcay.slalom.RaceRun;
import com.tcay.util.Log;

import java.util.ArrayList;

/**
 * ${PROJECT_NAME}
 *
 * Teton Cay Group Inc. ${YEAR}
 *

 * User: allen
 * Date: 10/10/13
 * Time: 5:26 PM
 *
 * This class in an 'agent' for the TagHeuer ChronoPrinter 520/540 Timing System
 *
 */
public class TagHeuerAgent {
    /// Have to close out a run /// all


    private ArrayList<TagHeuerRaceRun> tagHeuerRaceRuns = new ArrayList<TagHeuerRaceRun>();
    private TagHeuerRaceRun run = null;
    private Log log;

    public TagHeuerAgent() {
        log = Log.getInstance();
    }


    private TagHeuerRaceRun findRun(String bibNumber, int runNumber) {
        TagHeuerRaceRun found = null;
        for (TagHeuerRaceRun r:tagHeuerRaceRuns) {
            if (r.getBibNumber().compareTo(bibNumber) == 0) {
                if (r.getRunNumber() == runNumber ) {

                    found = r;
                    break;
                }
            }
        }
        return(found);
    }




    public void startRun(String bibNumber, String startTime) {
        TagHeuerRaceRun found = findRun(bibNumber, Race.getInstance().getCurrentRunIteration());//, startTime);

        if (found == null)  {
            found = new TagHeuerRaceRun(Race.getInstance().getCurrentRunIteration()); // todo problem if racer already in starting block and App changes run#
            found.setBibNumber(bibNumber);
            tagHeuerRaceRuns.add(found);
        }
        else {
            log.info("New starting time for #" + bibNumber);
        }
        found.setStartTime(startTime);

    }

    public void stopRun(String bibNumber, String stopTime) {
        TagHeuerRaceRun found = findRun(bibNumber, Race.getInstance().getCurrentRunIteration());

        if (found == null)  {
            found = new TagHeuerRaceRun(Race.getInstance().getCurrentRunIteration());
            found.setBibNumber(bibNumber);
            log.info("NO!  This is a problem, no start found for a STOP time!!!");
        }
        found.setStopTime(stopTime);
    }

    public void saveResult(String bibNumber, double elapsed) {
        TagHeuerRaceRun found = findRun(bibNumber, Race.getInstance().getCurrentRunIteration());

        if (found != null)  {
            found.setElapsedTime(elapsed);           // todo Is this the best way .. what about manual time, wiped out??
        }

        //look for matching race run, and attach if found
        if (found != null) {
            Race race = Race.getInstance();
            race.saveTagHeuer(found);
        }
        else {
            /// todo - if NULL here we wtill want to save Bib and time, but to where ?
        }
    }

    /**
     *
     * @param s
     */
    public boolean processCP520_output(/*CandidateCP520Port port,*/ String s) {
        // Read from the RS 232 port
        boolean rc = false;

        String delims = "[ ]+";
        String[] tokens;// = s.split(delims);

        log.info("Tag Heuer CP 520:" + s);
        Character c = s.charAt(0);
        switch (c) {

            // New Run #
            //i.e. "S 004          NET TIME
            case 'S' :

                tokens = s.split(delims);
                log.info("\nRUN #" + tokens[1]);
                rc = true;//port.setCP520(true);
                break;

            /////////////////////////////////////////////////
            // Time - if channel is M1 = start Time
            //i.e. "T       134 M1 20:02:45.766000"
            //                  Normal Start for Bib 134
            //i.e. "T           M2 20:02:45.766000"
            //                  Stop input received, no bib assigned
            //     "T*      192 M1 20:04:37.57000"
            //                  Changed Bib number for
            //                  start time NOW associated
            //                  to bib 192
            ////////////////////////////////////////////////
            case 'T' :
                c = s.charAt(1);
                // '-' false start
                // '*' means bib number changed for the associated start time
                switch (c) {
                    case ' ':
                        break;
                    case '-':
                        break;
                    case '*':
                        break;
                    default:
                        break;
                }

                tokens = s.split(delims);

                String bib = "";
                String timerImpulseName = "";
                String time = "";

                if (tokens.length < 4) {
                    // No Bib ... only a stop time
                    // todo Document how to test communication ... NO Bib on CP520 ... sen stop impulses with button '2'

                    timerImpulseName = tokens[1];
                    time = tokens[2];
                }
                else {
                    bib = tokens[1];
                    timerImpulseName = tokens[2];
                    time = tokens[3];
                }



                if (timerImpulseName.compareTo("M2") == 0)           {
                    // Finish time
                    log.info("\nMANUAL TIME FROM TagHeuer FINISH Bib=" + bib + "   @time=" + time);
                    stopRun(bib, time);
                    rc = true;//port.setCP520(true);

                }
                else if (timerImpulseName.compareTo("M1") == 0)           {
                    // Start Time
                    log.info("\nMANUAL TIME FROM TagHeuer START Bib=" + bib + "   @time=" + time);
                    startRun(bib, time);
                    rc = true;//port.setCP520(true);

                }
                if (timerImpulseName.compareTo("2") == 0)           {
                    // Finish time
                    log.info("\nFINISH Bib=" + bib + "   @time=" + time);
                    stopRun(bib, time);
                    rc = true;//port.setCP520(true);

                }
                else if (timerImpulseName.compareTo("1") == 0)           {
                    // Start Time
                    log.info("\nSTART Bib=" + bib + "   @time=" + time);
                    startRun(bib, time);
                    rc = true;//port.setCP520(true);

                }
                break;
            //////////////////////////////////////////
            // Result
            //i.e. "R   3   134          19.920000"
            case 'R' :              // This is a RESULT in CP520 NET TIME mode
                //////////////////////////////////////////
                tokens = s.split(delims);
                log.info("\nFINISH Bib=" + tokens[2] + "   elapsed time=" + tokens[3]);

                String timeDelims = "[ :]+";
                String[] timePieces = tokens[3].split(timeDelims);
                double dValue = 0.0;
                if (timePieces.length > 2) {
                    log.info("HOURS!! Really?");
                    dValue = 99999.00;
                }
                else if (timePieces.length > 1)
                {
                    dValue = Double.valueOf(timePieces[0]) * 60.0 +
                            Double.valueOf(timePieces[1]);
                }  else {
                    dValue = Double.valueOf(tokens[3]);
                }
                rc = true;//port.setCP520(true);


                saveResult(tokens[2], dValue);
                break;
            default :
                break;

        }
        return rc;
    }
}
