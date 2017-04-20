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

package com.tcay.slalom.timingDevices.alge;

//import com.tcay.slalom.Race;
//import com.tcay.slalom.RaceRun;
import com.tcay.slalom.timingDevices.PhotoCellAgent;
import com.tcay.slalom.timingDevices.PhotoCellRaceRun;
//import org.apache.commons.lang.StringUtils;

/**
 * Created by allen on 3/14/16.
 */
public class AlgeTimy extends PhotoCellAgent {
//    protected Log logRaw;


    public AlgeTimy() {
//        logRaw = new Log(getLogName());

    }

    public String getName() {
        return PREFIX;
    }


    public String getLogName() {
        return getName()+"_RAW";
    }

    /**
     * Process a line of output from Alge "Timy" Timing Box
     *
     * Timy  Protocol (Abbreviated to what we will use and expect)
     *
     * TBD
     *
     *
     *
     * Each line will be 52 bytes + CR + LF
     *
     * Desc             #bytes  pos ASCII Code      Notes
     *                              (Dec, Hex)
     *
     * y                1       1                   Blank or Info
     * x                1       2                   Blank
     * NNNN             4       3                   Start Number  (Bib? TODO)
     * CCC              8       7                   Channel 0 = Start 1 = Finish
     *
     *
     *
     *
     *
     * TOD COMPLETE TBD
     *
     * @param s - output from Alge Timy
     */

    // Define the "logical channels" we will interprete
    private static final String LC_START_EYE = "C0";
    private static final String LC_STOP_EYE = "C1";
    ///private static final String INFO_TIME_OF_DAY = "0";
    //private static final String INFO_RUN_NET_TIME = "1";
    //private static final String INFO_TOTAL_NET_TIME = "2";
    private static final String PREFIX = "TIMY";

    private static final int START_MASK     = 0x0001;
    private static final int STOP_MASK      = 0x0010;
    private static final int TOD_MASK       = 0x0100;
    private static final int TOTALTIME_MASK = 0x1000;
    private static final int RUNTIME_MASK   = 0x2000;



    public int getCommandSize() {return(52);}
    static int logRawLines=0;





    public boolean processDeviceOutput(String s) {

        // Read from the RS 232 port
        int    msgType = 0;
        String racerBib = null;
        boolean rc = false;
        String program = null;
        String online = null;
        String sequence = null;
        String token = null;
        String time = null;
        String manual = "";
        PhotoCellRaceRun pcrr = null;

        //String runNbr = null;
        //int runNbr;


        // use pos-1 for begin index
        // use pos-1 + #chars for end index

        try {
            if (s.substring(0, 1).equals(" ")) {  // Got a valid line


                try {
                    if (logRawLines++ % 20 == 0)
                        logRaw.debug("                                         12345678901234567890123456789");
                    logRaw.debug(PREFIX + " Extended Protocol Converter: input>>" + s.substring(0, 26));

                } catch (Exception e) {
                    //log.warn
                }
                rc = true;

//            program = s.substring(4, 5);
//            online = s.substring(5, 6);
//            token = s.substring(6, 12);
                sequence = s.substring(1, 5);
                //racerBib = Integer.valueOf(token).toString(); /// This strips off the leading zeros
                //runNbr = s.substring(20, 23);

                // get Logical Channel, we care ONLY about START_EYE and STOP_EYE
                time = s.substring(10, 21);

                manual = s.substring(8, 9);
                token = s.substring(6, 8);
                if (token.equals(LC_START_EYE)) {
                    msgType = START_MASK;
                    //logRaw.info(PREFIX +" START:" + racerBib + (manual.contains("M")?" MANUAL":""));
                    racerBib = startRun(time);
                } else if (token.equals(LC_STOP_EYE)) {
                    msgType = STOP_MASK;

                    pcrr =  stopRun(time);//, racerBib);
                    if (pcrr!=null) {
                        racerBib = pcrr.getBibNumber();
                    }
//                timeStringInSecs(time);





                    // logRaw.info(PREFIX + " STOP:"+ racerBib+  (manual.contains("M")?" MANUAL":""));
                } else {
                    logRaw.warn(PREFIX + " UNKNOWN Logical Channel#=" + token);
                }




                StringBuffer msg = new StringBuffer(PREFIX);
                msg.append(" ");
                if ((msgType & START_MASK) == START_MASK) {
                    msg.append("ESTRT ");
                    //startRun(racerBib,);

                } else if ((msgType & STOP_MASK) == STOP_MASK) {
                    msg.append("ESTOP ");
                }


                msg.append("B=");
                msg.append(Integer.valueOf(racerBib));
                msg.append(" ");
//            msg.append("r=");
//            msg.append(Integer.valueOf(runNbr));
//            msg.append(" ");


                if ( pcrr != null) {
                    if ((msgType & STOP_MASK) == STOP_MASK ) {
                        msg.append("Elapsed=");
                        msg.append(pcrr.getElapsedTime());
                        saveResult(racerBib, pcrr.getElapsedTime());//Double.valueOf(timeStringInSecs(sTime, date)));//Double.valueOf(elapsedTime));

                    }

                }
                else {
                    if ((msgType & STOP_MASK) == STOP_MASK ) {
                        msg.append(" NO RUN START FOUND FOR FINISH (DNF?) !");
                    }
                }

                logRaw.info(msg.toString());


            }


        } catch (Exception e) {
            logRaw.error(e.getMessage());
        }

        return rc;
    }



    public PhotoCellRaceRun stopRun(String stopTime) {//}, String bib) {
        PhotoCellRaceRun found = super.stopRun(stopTime);

        if (found!=null) {
            double start = toSecondsAndHundreths(found.getStartTime());
            double stop = toSecondsAndHundreths(stopTime);
            found.setElapsedTime(stop-start);

            System.out.println("B=" + found.getBibNumber() + " ALGE PHOTO CELL time=" +found.getElapsedTime());
        }
        return(found);
    }


    private String timeStringInSecs (String timyTime) {
        String ourTime="0000000000";

        Long seconds = getSecondsOnly(timyTime);//Long.valueOf(rei2Time) % 10000;
        Long tenThousanths = toTenThousandths(timyTime);//Long.valueOf(rei2Time) % 10000;

        seconds += toMinutes(timyTime) * 60L;
        seconds += toHours(timyTime) * 60L * 60L;


        ourTime = //sDays  + toHours(rei2Time) + ":"+ toMinutes(rei2Time) + ":" +
                seconds + "." + tenThousanths;
        return(ourTime);
    }





    /*
        return long integer time in seconds
     */


    private double toSecondsAndHundreths(String time) {
        double hours = Double.valueOf(time.substring(0, 2));
        double minutes = Double.valueOf(time.substring(3, 5));
        double ourSeconds = Double.valueOf(time.substring(6, 8));
        double hundreths = Double.valueOf(time.substring(9, 11));

        double seconds = hours*60*60;
        seconds += minutes * 60;
        seconds += ourSeconds;
        seconds += hundreths/100.00;

        return seconds;

    }


    private long getSecondsOnly(String time) {


        double hours = Double.valueOf(time.substring(0,2));
        double minutes = Double.valueOf(time.substring(3, 5));
        double ourSeconds = Double.valueOf(time.substring(6, 8));
        double hundreths = Double.valueOf(time.substring(9, 11));




//        long seconds = Long.valueOf(time)%1000000;
//        seconds /= 10000;
        return(0);//seconds);
    }

    private long minutesAndSecondsTotSeconds(String time) {
        long seconds = Long.valueOf(time)%1000000/10000;
        long minutes = toMinutes(time);
        seconds += 60 * minutes;

        return(seconds);
    }

    private long getHundreths(String time) {
        long hundreths = Long.valueOf(time)%10000/100;

        return(hundreths);
    }


    /*
        return long integer time in minutes
     */
    private long toMinutes(String time) {
        long minutes = Long.valueOf(time.substring(2,4));//seconds - hours*60*60) / 60l;

        return(minutes);
    }
    /*
        return long integer time in hours
     */

    private long toHours(String time){//long seconds) {
        long hours = Long.valueOf(time.substring(0, 2));//seconds/(60*60);

        return(hours);
    }
/*
    private long toHundreths(String timeString) {
        long hundreths = (Long.valueOf(timeString)/100)%100;

        return hundreths;
    }
*/
    private long toTenThousandths(String timeString) {
        long tenThousandths = (Long.valueOf(timeString)%10000);

        return tenThousandths;
    }
/*
    private String toTimeOfDay(String time) {
        String timeOfDay=null;
        Long seconds = getSecondsOnly(time);
        Long hundreths = getHundreths(time);
        Long hours = toHours(time);//seconds);//(seconds/(60*60));
        Long minutes = toMinutes(time);//(seconds, hours);//(seconds - hours*60*60) / 60l;
        seconds %= 60;  /// discard portion > 59, we will indicate that in minutes here/// needed ???

        //Long tenThousanths = Long.valueOf(time)%10000;

        String sMinutes = StringUtils.leftPad(minutes.toString(), 2, "0");
        String sSeconds = StringUtils.leftPad(seconds.toString(), 2, "0");
        String sHundreths = StringUtils.leftPad(hundreths.toString(), 2, "0");



        //logRaw.info("TIME IN SECONDS " + minutesAndSecondsTotSeconds(time));
        timeOfDay = hours+":"+sMinutes+":"+ sSeconds + "." + sHundreths;
        return( timeOfDay);

    }
    */

}
