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

import com.tcay.util.Log;

/**
 * ${PROJECT_NAME}
 *
 * Teton Cay Group Inc. ${YEAR}
 *

 * User: allen
 * Date: 10/11/13
 * Time: 6:42 AM
 *
 */
public class TagHeuerRaceRun {

    private static final Log log = Log.getInstance();

    public TagHeuerRaceRun(int runNumber) {
      this.runNumber = runNumber;
    }


    private int runNumber;


    private void logTime(String action, String rawTime) {
        String formattedBib = String.format("%4s", bibNumber);
        log.info("eTIMER>Bib#" + formattedBib + " RUN#" + runNumber + " " + action +
                (rawTime == null? "" : " " + rawTime));
    }


    public int getRunNumber() {
        return runNumber;
    }


    public String getBibNumber() {
        return bibNumber;
    }

    public void setBibNumber(String bibNumber) {
        this.bibNumber = bibNumber;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
        logTime("START", null);
    }

    public double getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(double elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    private String bibNumber;
    private String startTime;

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
        String rawTime = String.format("%8.2f", getElapsedTime());
        logTime("STOP", rawTime);
    }

    private String stopTime;
    private double elapsedTime;


}