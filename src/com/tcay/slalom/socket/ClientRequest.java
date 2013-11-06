package com.tcay.slalom.socket;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 10/23/13
 * Time: 12:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientRequest implements Serializable {
    public static final int REQ_GET_SCORABLE_RUNS = 1;
    public static final int REQ_GET_NBR_GATES = 2;
    public static final int REQ_GET_NBR_RUNS_STARTED_OR_COMPLETED = 3;
    public static final int REQ_GET_SECTIONS = 4;

    // these need a parameter - gate #
    public static final int REQ_IS_GATE_IN_SECTION = 10;
    public static final int REQ_IS_FIRST_GATE_IN_SECTION = 11;
    public static final int REQ_IS_UPSTREAM = 12;

    // These are data upstream to server
    public static final int REQ_UPDATE_PENALTIES = 20;
    public static final int REQ_UPDATE_SECTION_ONLINE = 21;



    private int requestCmd;
    private int gate = -1;
    private int section = -1;
    private static Integer lastRequestNbr=1;
    private Integer requestNbr;
    private ArrayList<Object> dataList;

    public ArrayList<Object> getDataList() {
        return dataList;
    }

    public Integer getRequestNbr() {
        return requestNbr;
    }


    public void addObject(Object o) {
        if (dataList == null) {
            dataList = new ArrayList<Object>();
        }
        dataList.add(o);
    }



    private void init(int request) {
        this.requestCmd = request;
        synchronized (lastRequestNbr) {
            synchronized (lastRequestNbr) {
                requestNbr = lastRequestNbr++;
            }
       }
    }


    public ClientRequest(int request) {
        init(request);
    }

    public ClientRequest(int request, int gate) {
        init(request);
        this.gate = gate;
    }

    public ClientRequest(int request, int gate, int section) {
        init(request);
        //this.requestCmd = request;
        this.gate = gate;
        this.section = section;
    }

    public int getRequestCmd() {
        return requestCmd;
    }

    public int getGate() {
        return gate;
    }

    public int getSection() {
        return section;
    }
}
