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
    String summary;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
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
        summary = new String("-");
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
        return new String(gate + " " + penaltySeconds + summary);
    }
}

