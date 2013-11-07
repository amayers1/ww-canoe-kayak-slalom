package com.tcay.slalom.socket;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * SlalomScoring
 * Teton Cay Group Inc. 2013
 * <p/>
 * User: allen
 * Date: 11/6/13
 * Time: 5:41 PM
 */


/**
 * Server Response for Client Requests
 */
public class ServerResponse implements Serializable {
    private ArrayList<Object> responseList;
    private long runsStartedOrCompletedCnt;
    private Integer requestNbr;
    private int requestType;

    public Integer getRequestNbr() {
        return requestNbr;
    }

    public long getRunsStartedOrCompletedCnt() {
        return runsStartedOrCompletedCnt;
    }

    public void setRunsStartedOrCompletedCnt(long runsStartedOrCompletedCnt) {
        this.runsStartedOrCompletedCnt = runsStartedOrCompletedCnt;
    }


    public ArrayList<Object> getResponseList() {
        return responseList;
    }

    public int getRequestType() {
        return requestType;
    }


    public ServerResponse(ClientRequest request) {//int requestType, int requestNbr) {
        this.requestNbr = request.getRequestNbr();
        this.requestType = request.getRequestCmd();
        responseList = new ArrayList<Object>();
    }


    public void add(Object o) {
        responseList.add(o);
    }
}
