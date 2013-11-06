package com.tcay.slalom.socket;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 10/23/13
 * Time: 2:35 PM
 * To change this template use File | Settings | File Templates.
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
