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
 * Client Requests to Server for information or to update Server Race data
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


    // These NEW 20150519 are data upstream to server
    public static final int REQ_NRC_EYES_START = 50;     // Send a start from the NRC Photo Eye System
    public static final int REQ_NRC_EYES_FINISH = 51;    // Send a finish from the NRC Photo Eye System
    public static final int REQ_NRC_FORCE_NEW_RUN =52;   /// force system to reload start list and increment run



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

    public void setSection(int section) {
        this.section = section;
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
