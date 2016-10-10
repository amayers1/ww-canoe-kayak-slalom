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

package com.tcay.slalom.timingDevices.MicrogateREI2;

import com.tcay.slalom.timingDevices.PhotoCellAgent;
import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang.StringUtils;
//import org.apache.commons.*;

/**
 * Created by allen on 3/14/16.
 */
public class MicrogateAgent extends PhotoCellAgent {
//    protected Log logRaw;


    public MicrogateAgent() {
//        logRaw = new Log(getLogName());

    }

    public String getName() {
        return PREFIX;
    }


    public String getLogName() {
        return getName()+"_RAW";
    }

    /**
     * Process a line of output from Microgate / Alge Timing Box in Extended REI2 mode
     *
     * REI2 Extended Protocol (Abbreviated to what we will use and expect)
     *
     * Each line will be 52 bytes + CR + LF
     *
     * Desc             #bytes  pos ASCII Code      Notes
     *                              (Dec, Hex)
     *
     * DLE              1       1   16,10h          Protocol identifier
     * Identifier       1       2                   R=REI2                  REI2 -"R" Required, all other ignored
     *
     * Device addr      1       3   32,20h          Reserved future use
     * Dummy char       1       4   32,20h          Reserved future use
     * Program in use   1       5                   S=Single Starts         REI2  "S" REQUIRED all other ignored
     *
     * Mode             1       6                   O=Online                REI2  "O" REQUIRED all other ignored
     *                                              F=Offline               REI2 ignored - Find out exact meaning)
     * Counter          6       7                   from 000001 to 999999, with wrap  ignored REI2 ignored
     *                                                                                        REI2      Microgate
     *                                                                                        REI2      use only
     * Competitor Nbr   5       13                  (REI2 - Verify this matches Bib #, is it
     *                                               REI2      input into Microgate when bibs out of order
     *                                              (REI2      (not sequential?)
     * Group            3       18                  (REI2 - verify that we will ignore this field)
     * Run/Trial        3       21                  001 <= Nm <= 250   (REI2 - verify this is the slalom run #)
     * Physical Channel 3       24                  (REI2 - verify this represents the photo eyes
     *                                               REI2      (start eye and stop eye)
     *                                               REI2      we plan NOT to use this field
     *                                               REI2      in favor of logical channel
     * Logical Channel  3       27                  000 <= Logical Channel <= 255
     *                                              000=Start
     *                                              001..240=Lap n
     *                                              .... (REI2 - verify 248, 249, 250 not used)
     *                                              255=STOP
     *
     *                                              REI2 Assumption: we will use Competitor + Run/Trial#
     *                                              REI2                Matching code "000=Start" as realtime clock
     *                                              REI2                with code  "255=STOP" as realtime clock
     *                                              REI2                and take difference as raw time in
     *                                              REI2                whatever units Microgate supplies
     *                                              REI2               (ten thousandths??)
     *
     * Information      1       30                  0=Time of day            REI2 Verify - "0" is what we see in
     *                                                                       REI2      example text
     *                                                                       REI2      and are the only date lines
     *                                                                       REI2      we plan to use
     *                                              1=Run net time (split)
     *                                              2=Total net time (split)
     *
     *                                              REI2 0,1,2 only values seen in sample file
     *
     * Time/Speed      10       31
     * Date             8       41                  DDMMYYYY
     * Dummy Char       2       49                  reserved future use
     * CR               1       51
     * LF               1       52
     *
     * @param s - output from Microgate / Alge
     */

    // Define the "logical channels" we will interprete
    private static final String LC_START_EYE = "000";
    private static final String LC_STOP_EYE = "255";
    private static final String INFO_TIME_OF_DAY = "0";
    private static final String INFO_RUN_NET_TIME = "1";
    private static final String INFO_TOTAL_NET_TIME = "2";
    private static final String PREFIX = "REI2";

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
        String racerBib;// = null;
        boolean rc = false;
        String program = null;
        String online = null;
        String token = null;
        String time = null;
        String runNbr = null;
        //int runNbr;


        // use pos-1 for begin index
        // use pos-1 + #chars for end index

        if (s.substring(1, 2).equals("R")) {  // Got a valid
            if (logRawLines++%20==0)
               logRaw.info("                                         12345678901234567890123456789012345678901234567890");
            logRaw.info(PREFIX + " Extended Protocol Converter: input>>" + s.substring(0,50));
            rc = true;

            program = s.substring(4, 5);
            online = s.substring(5, 6);
            token = s.substring(6, 12);
            racerBib = Integer.valueOf(s.substring(12, 17)).toString(); /// This strips off the leading zeros
            runNbr = s.substring(20, 23);

            // get Logical Channel, we care ONLY about START_EYE and STOP_EYE
            token = s.substring(26, 29);
            if (token.equals(LC_START_EYE)) {
                msgType = START_MASK;
                //log.info("REI2 START:" + racerBib);
                //startRun(racerBib, startTime);
            } else if (token.equals(LC_STOP_EYE)) {
                msgType = STOP_MASK;
                //log.info("REI2 STOP:"+ racerBib);
            } else {
                logRaw.warn(PREFIX + " UNKNOWN Logical Channel#=" + token);
            }

            String date = s.substring(40, 48);
            token = s.substring(29, 30);
            if (token.equals(INFO_TIME_OF_DAY)) {
                msgType |= TOD_MASK;
                time = s.substring(30, 40);
                String timeOfDay = toTimeOfDay(time);
                //logRaw.info("TIME OF DAY=" + date + " " + timeOfDay);
            } else if( token.equals(INFO_RUN_NET_TIME)) {
                String sTime = s.substring(30, 40);
                String ourTime = timeString (sTime, date);
                msgType |= RUNTIME_MASK;
                //logRaw.info("RUN TIME=" + ourTime);
            } else {
                if (token.equals(INFO_TOTAL_NET_TIME)) {
                    msgType |= TOTALTIME_MASK;
                    String sTime = s.substring(30, 40);
                    String ourTime = timeString (sTime, date);
                    //logRaw.info("TOTAL TIME=" + ourTime);
                    //stopRun(racerBib, String stopTime);
                } else {
                    logRaw.warn(PREFIX + " UNKNOWN Information Type=" + token);
                }
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
            msg.append("r=");
            msg.append(Integer.valueOf(runNbr));
            msg.append(" ");

            if ((msgType & TOD_MASK) == TOD_MASK) {
                time = s.substring(30, 40);
                String timeOfDay = toTimeOfDay(time);
                msg.append("CHRONO ");
                msg.append(" ");
                msg.append(timeOfDay);
                if ((msgType & START_MASK) == START_MASK) {
                    startRun(racerBib,timeOfDay);
                }

            } else {

                String sTime = s.substring(30, 40);
                String elapsedTime = timeString (sTime, date);
                if ((msgType & TOTALTIME_MASK) == TOTALTIME_MASK) {
                    msg.append("TOTAL  ");
                    saveResult(racerBib, Double.valueOf(timeStringInSecs(sTime, date)));//Double.valueOf(elapsedTime));
                } else {
                    msg.append("RUN    ");
                    stopRun(racerBib, timeStringInSecs(sTime, date));//elapsedTime);
                }
                msg.append("Elapsed=");

                msg.append(timeStringInSecs(sTime, date));
                msg.append("  ");
                msg.append(elapsedTime);
            }
            logRaw.info(msg.toString());


        } else {
            logRaw.warn("Microgate/Alge: unknown serial data>" + s);
        }
        //logRaw.info("<<END REI2 Transmission\n\n");
        return rc;
    }


    private String timeString (String rei2Time, String rei2date) {

        boolean negative = false;
        String ourTime="0000000000";
        String sign = rei2date.substring(0, 1);
        if (sign.equals("-")) {
            negative = true;
            rei2date = rei2date.replace('-', '0');
        } else if (sign.equals("+")) {
            rei2date = rei2date.replace('+', '0');
        }

        Long seconds = getSecondsOnly(rei2Time);//Long.valueOf(rei2Time) % 10000;
        Long tenThousanths = toTenThousandths(rei2Time);//Long.valueOf(rei2Time) % 10000;

        int days = Integer.parseInt(rei2date);
        if (negative) {
            days = -days;
        }
        String sDays = days != 0 ? days + ((days!=1)?" days ":" day ") : "";

        ourTime = sDays  + toHours(rei2Time) + ":"+ toMinutes(rei2Time) + ":" + seconds + "." + tenThousanths;
        return(ourTime);
    }

    private String timeStringInSecs (String rei2Time, String rei2date) {

        boolean negative = false;
        String ourTime="0000000000";
        String sign = rei2date.substring(0, 1);
        if (sign.equals("-")) {
            negative = true;
            rei2date = rei2date.replace('-', '0');
        } else if (sign.equals("+")) {
            rei2date = rei2date.replace('+', '0');
        }

        Long seconds = getSecondsOnly(rei2Time);//Long.valueOf(rei2Time) % 10000;
        Long tenThousanths = toTenThousandths(rei2Time);//Long.valueOf(rei2Time) % 10000;

        seconds += toMinutes(rei2Time) * 60L;
        seconds += toHours(rei2Time) * 60L * 60L;

        int days = Integer.parseInt(rei2date);
        if (negative) {
            days = -days;
            seconds -= 24*60*60;
        }
        //String sDays = days != 0 ? days + ((days!=1)?" days ":" day ") : "";

        ourTime = //sDays  + toHours(rei2Time) + ":"+ toMinutes(rei2Time) + ":" +
                seconds + "." + tenThousanths;
        return(ourTime);
    }





    /*
        return long integer time in seconds
     */
    private long getSecondsOnly(String time) {

        long seconds = Long.valueOf(time)%1000000;
        seconds /= 10000;
        return(seconds);
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

    private long toHundreths(String timeString) {
        long hundreths = (Long.valueOf(timeString)/100)%100;

        return hundreths;
    }

    private long toTenThousandths(String timeString) {
        long tenThousandths = (Long.valueOf(timeString)%10000);

        return tenThousandths;
    }

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

}
