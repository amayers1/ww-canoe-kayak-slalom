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

import java.io.Serializable;

/**
 * Teton Cay Group, Inc.
 * User: allen
 * Date: Aug 21, 2013
 * Time: 5:14:42 PM
 */
public class Penalty implements Serializable {
    Integer gate;
    Integer penaltySeconds;
    boolean fromClient;
    Boolean onEntry;
    Boolean onExit;
    Boolean bowHit;
    Boolean sternHit;
    Boolean paddleHit;
    Boolean bodyHit;
    Boolean engagedOutOfSequence;
    StringBuffer summary;

    public String getSummary() {
        return summary.toString();
    }

/*
    public String getLogString(RaceRun rr, int section) {
        StringBuffer penaltyLog = new StringBuffer();

        penaltyLog.append("PEN   B=" + rr.getBoat().getRacer().getBibNumber());
        penaltyLog.append(" r=" + rr.getRunNumber());
        penaltyLog.append(" s=" + section);

        return penaltyLog.toString();
    }

*/
    public void setSummary(StringBuffer summary) {
        this.summary = summary;
    }

    public boolean isFromClient() {
        return fromClient;
    }


    public void setFromClient(boolean fromClient) {
        this.fromClient = fromClient;
    }

    public Integer getGate() {
        return gate;
    }



    public Penalty(int gate, int seconds, boolean fromClient) {
        summary = new StringBuffer("PENALTY g:" + gate + " s:" + seconds + " Remote:" + (fromClient?"F":"T"));
        this.gate = gate;
        penaltySeconds = seconds;
        this.fromClient = fromClient;
    }

    public Integer getPenaltySeconds() {
        return penaltySeconds;
    }

    public void setPenaltySeconds(Integer penaltySeconds) {
        this.penaltySeconds = penaltySeconds;
    }

    public String toString() {
        return summary.toString();
    }
}

