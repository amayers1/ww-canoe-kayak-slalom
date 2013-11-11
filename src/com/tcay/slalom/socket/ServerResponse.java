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
