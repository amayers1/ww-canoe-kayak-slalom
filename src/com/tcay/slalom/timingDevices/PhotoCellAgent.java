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

package com.tcay.slalom.timingDevices;

import com.tcay.slalom.BoatEntry;
import com.tcay.slalom.Race;
import com.tcay.slalom.RaceRun;
import com.tcay.util.Log;

import java.util.ArrayList;

/**
 * Created by allen on 3/14/16.
 */
public abstract class PhotoCellAgent {

    protected ArrayList<PhotoCellRaceRun> photoCellRaceRuns = new ArrayList<PhotoCellRaceRun>();
    protected Log logRaw;
// todo investigate    private PhotoCellRaceRun run = null;

    public PhotoCellAgent() {

//        log = Log.getInstance();
        logRaw = new Log(getLogName());
    }

    public abstract String getLogName();
    public abstract int getCommandSize();
    public abstract boolean processDeviceOutput(String s);


    public void saveResult(String bibNumber, double elapsed) {
        PhotoCellRaceRun found = findRun(bibNumber, Race.getInstance().getCurrentRunIteration());

        if (found != null)  {
            found.setElapsedTime(elapsed);           // todo Is this the best way .. what about manual time, wiped out??
        }

        //look for matching race run, and attach if found
        if (found != null) {
            Race race = Race.getInstance();
            race.associatePhotoCellRun(found);
        }
        else {
            logRaw.error("Problem, atempt to save result, but PhotoCell run could not be found, bib#" + bibNumber + " elapsed="+elapsed);

            /// todo - if NULL here we wtill want to save Bib and time, but to where ?
        }
    }

    private PhotoCellRaceRun findRun(String bibNumber, int runNumber) {
        PhotoCellRaceRun found = null;
        for (PhotoCellRaceRun r:photoCellRaceRuns) {
            //logRaw.info("Chk for bib # "+ bibNumber+"=" + r.getBibNumber());

            if (r.getBibNumber().compareTo(bibNumber) == 0) {
                //logRaw.info("BIB Match OK ! runNumber? " + runNumber + "=" + r.getRunNumber());

                if (r.getRunNumber() == runNumber ) {
                    //logRaw.info("OK -> runNumber match OK !" + runNumber + "=" + r.getRunNumber());

                    found = r;
                    break;
                }
            }
        }
        return(found);
    }



    /* For Alge Timy type devices or 'relaxed' SlalomApp protocol not requiring bibs to match
    * in SlalomApp and Timing device start list.  In this case ONLY Slalom App needs to track bibs
    *
    */
    private PhotoCellRaceRun findRunToStart(int runNumber) {  /// TODO RUN to start will NOT yet be in photoCellRaceRuns ???
        PhotoCellRaceRun found = null;
        for (PhotoCellRaceRun r:photoCellRaceRuns) {
// TODO get Run           if (r.getBibNumber().compareTo(bibNumber) == 0) {
                if (r.getRunNumber() == runNumber ) {

                    found = r;
                    break;
                }
            }
//        }
        return(found);
    }





    private PhotoCellRaceRun findRunToStop(){//int runNumber) {

        Race race = Race.getInstance();
        PhotoCellRaceRun found = null;
        if (race.isPHOTO_EYE_AUTOMATIC()) {
            BoatEntry boat = Race.getInstance().getBoatReadyToFinish();
            for (PhotoCellRaceRun r:photoCellRaceRuns) {
                System.out.println("findRunToStop SHOULD STOP " + r.getBibNumber() + " ?");

                if (r.getBibNumber().compareTo(boat.getRacer().getBibNumber()) == 0) {
                    System.out.println("findRunToStop SHOULD STOP " + r.getBibNumber() + " MAYBE");

              //      if (r.getRunNumber() == runNumber) {
                        System.out.println("findRunToStop SHOULD STOP " + r.getBibNumber() + " RIGHT RUN" );

                        if (race.isDNF(r.getBibNumber(), r.getRunNumber())) {   //todo verify  r.getRunNumber() is proper
                            System.out.println("findRunToStop SHOULD NOTSTOP " + r.getBibNumber() + " DNF !!!!" );
                        }
                        else if (r.getElapsedTime() == 0 ) {
                            System.out.println("STOPPING " + r);
                            found = r;
                            break;
                        }
               //     }
               //     else {
               //         System.out.println("findRunToStop RUN MISMATCH PHOTOCELL RUN= " + r.getRunNumber() + " WRONG RUN ->" + runNumber);


                //    }
                }
            }

        }
        else {
                for (PhotoCellRaceRun r2:photoCellRaceRuns) {
// TODO get Run           if (r.getBibNumber().compareTo(bibNumber) == 0) {
                    //if (r2.getRunNumber() == runNumber ) {
                        if (r2.getElapsedTime() == 0) {
                            System.out.println("SETOPPING " + r2);
                            //todo                   r.setElapsedTime(10.00);  //TODO FIX to actual elapsed time
                            found = r2;
                            break;
                        }
                    //}
                }

        }

//        }
        return(found);
    }



    /*
    *
    * If in Virtual Stopwatch mode then the virtual stopwatch may have started already,
    * or the photo eye may have started first ... todo determine how to match proper run
    *
    * for now 4/17/2016 .. we will just rely on the photoeye start
    *
    *
     */
    private String getBibForStart() {
        String bibNumber = Race.getInstance().getBoatInStartingBlock().getRacer().getBibNumber();
        return(bibNumber);
    }


    /*
    *
    * For 'Dumb' devices like Alge Timy in PC-TIMER mode, just use the bib that
    * is in the SlalomApp's starting gate
    *
    */
    public String startRun(String startTime) {
        String bibNumber = getBibForStart();//Race.getInstance().getBoatInStartingBlock().getRacer().getBibNumber();


        if (Race.getInstance().isPHOTO_EYE_AUTOMATIC() ) {
            if (Race.getInstance().TODOKludgeStartFRomPhotoEye(bibNumber)) {
                startRun(bibNumber, startTime);

            }
        } else {
            startRun(bibNumber, startTime);
        }





//      PhotoCellRaceRun found = findRun(bibNumber, Race.getInstance().getCurrentRunIteration());//, startTime);

      //  if (found == null)  {


        /// Just start the boat in the starting gate

//            found = new PhotoCellRaceRun(Race.getInstance().getCurrentRunIteration()); // todo problem if racer already in starting block and App changes run#
//            found.setStartTime(startTime);
//            bibNumber = Race.getInstance().getBoatInStartingBlock().getRacer().getBibNumber();
//            found.setBibNumber(bibNumber);
//            photoCellRaceRuns.add(found);
      //  }
      //  else {
            //logRaw.info("New starting time for #" + bibNumber);
      //  }
//        found.setStartTime(startTime);

        return(bibNumber);
    }



// TODO FIX FIX FIX
    public void startRun(String bibNumber, String startTime) {   // TODO In case of ReRun getCurrentRunIteration may be wrong run
         PhotoCellRaceRun found = findRun(bibNumber, Race.getInstance().getCurrentRunIteration());//, startTime);

        if (found == null)  {
            found = new PhotoCellRaceRun(Race.getInstance().getCurrentRunIteration()); // todo problem if racer already in starting block and App changes run#
            found.setBibNumber(bibNumber);
            photoCellRaceRuns.add(found);
        }
        else {
            logRaw.info("New EStarting time for # (RERUN?)" + bibNumber);
        }
        found.setStartTime(startTime);

    }

    /*
    * TagHeuer and Microgate
     */
    public PhotoCellRaceRun stopRun(String bibNumber, String stopTime) {
        PhotoCellRaceRun found = findRun(bibNumber, Race.getInstance().getCurrentRunIteration());

        if (found == null)  {
            found = new PhotoCellRaceRun(Race.getInstance().getCurrentRunIteration());
            found.setBibNumber(bibNumber);
            logRaw.error("Problem, no start found for a STOP time!!!  Bib#" + bibNumber);
        }
        found.setStopTime(stopTime);
        return(found);
    }


    /*
    * ALGE "Timy"
    * For 'Dumb' devices like Alge Timy in PC-TIMER mode, just use the bib that
    * is in the SlalomApp's "next to Finish position
    *  TODO, provide overtake capability so that a racer overtaking is the next that is supposed to finish
    *  todo handle DNF so that Dumb Devices use correct
    *  TODO make automated test cases for both overtakse and DNFs
    */


    public PhotoCellRaceRun stopRun(String stopTime) {
        PhotoCellRaceRun found = findRunToStop();// Race.getInstance().getCurrentRunIteration());

        if (found!=null ) {

            if (Race.getInstance().isPHOTO_EYE_AUTOMATIC()) {
                if (Race.getInstance().TODOKludgeFinishFRomPhotoEye(found.getBibNumber())) {
                    //startRun(bibNumber, startTime);
                    System.out.println("EStart=" + found.getStartTime() + "  EFinish= " + stopTime);
                    found.setStopTime(stopTime);
                } else
                    {  // This Handles bug with fals finish 20160417
                        found = null;
                    }






            } else {

                System.out.println("EStart=" + found.getStartTime() + "  EFinish= " + stopTime);
                found.setStopTime(stopTime);
            }


        }   else {
            System.out.println("PhotoCellAgent:StopRun():  NULL HERE!!!!!");

        }

        //if (found == null)  {
        //    found = new PhotoCellRaceRun(Race.getInstance().getCurrentRunIteration());
        //    //found.setBibNumber(bibNumber);
        //    logRaw.info("NO!  This is a problem, no start found for a STOP time!!!");
       // }
        return(found);
    }


}
