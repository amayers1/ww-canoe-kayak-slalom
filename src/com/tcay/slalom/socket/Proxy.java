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

import com.sun.javaws.exceptions.InvalidArgumentException;
import com.tcay.slalom.BoatEntry;
import com.tcay.slalom.Penalty;
import com.tcay.util.Log;
import com.tcay.slalom.Race;
import com.tcay.slalom.RaceRun;
import com.tcay.slalom.UI.JudgingSection;

import java.io.IOException;
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
 * Used to route through socket for remote client, or allow direct shared memory when appropriate
 * (e.g. when the objects are running in the same application memory space)
 */
public class Proxy {
    Log log;

    Client clientSocket;


    {
        log = Log.getInstance();
    }


    static String[] message = {"Client cannot be null"};

    public Proxy(Client client) {//throws InvalidArgumentException {//boolean standAlone) {
        if (client == null) {
            //race = Race.getInstance();     // NO LONGER LEGAL 131110 (ajm) only use race if NOT standalone,
            //throw new InvalidArgumentException(message);
        }
        else {
            clientSocket = client;
            try {
                clientSocket.connectToServer();
            } catch (IOException e) {
                //log.warn("");

                e.printStackTrace();
            }
        }
    }

    public ArrayList<RaceRun> getScorableRuns() {
        ClientRequest request;
        ArrayList<RaceRun> returnList = new ArrayList<RaceRun>();
            request = new ClientRequest(ClientRequest.REQ_GET_SCORABLE_RUNS);
            clientSocket.send(request);
            ServerResponse response = clientSocket.getResponse(request);

            // this list has to be a copy for each window, as they are potentially all on separate devices
            if ( response.getRequestType() == ClientRequest.REQ_GET_SCORABLE_RUNS ) {

                int i=0;
                for (Object o:response.getResponseList()) {
                    ((RaceRun)o).setLog(log);
                    returnList.add( (RaceRun)o );
                    i++;
                }
            }
        return returnList;
    }

    public void updateSectionOnline(Integer section) {
            ClientRequest request = new ClientRequest(ClientRequest.REQ_UPDATE_SECTION_ONLINE);
            request.addObject(section);
            clientSocket.send(request);
    }


    /**
     * Send penalties from Client back to server.
     *
     * This method was problematic as the Client and Server code both used RaceRun.
     * The Server code receiving the RaceRun with the attached PenaltyList was getting confused
     * (or at least the programmer was). This would result in only being able to send section
     * penalties once, then after that, the Server would keep the first penalties sent.  Some
     * issue with serialization that I did not sort out.   Sending the penalty list as a separate
     * object as done below, then having the server process it works fine for now.
     *
     * @param run
     */


    public void updateResults(Log clientLog, RaceRun run, int section) {
        ClientRequest request = new ClientRequest(ClientRequest.REQ_UPDATE_PENALTIES);
        StringBuffer penaltyLog = new StringBuffer();

        request.setSection(section);
            request.addObject(run);
            request.addObject(run.getPenaltyList());
//            penaltyLog.append("REMOTE PEN " + run.getLogString());


//        penaltyLog.append(padRight(" ", section * 15));
        for (Penalty p:run.getPenaltyList()) {
            run.setPenalty(p.getGate(), p.getPenaltySeconds(), false);
//            penaltyLog.append(String.format("% 2d", p.getPenaltySeconds()));
        }


        run.logPenalties(clientLog, section, "RPEN ");
        clientSocket.send(request);
    }

    // A150519 (ajm) Start

//todo flush out

    public void forceNewNRCRun() {
        ClientRequest request = new ClientRequest(ClientRequest.REQ_NRC_FORCE_NEW_RUN);
        clientSocket.send(request);

    }

    public RaceRun updateNewNRCeyeStart(Long startMillis) {
        RaceRun rr=null;
        ClientRequest request = new ClientRequest(ClientRequest.REQ_NRC_EYES_START);

        request.addObject(startMillis);
        //request.addObject(run.getPenaltyList());
        clientSocket.send(request);



        ServerResponse response = clientSocket.getResponse(request);

        // this list has to be a copy for each window, as they are potentially all on separate devices
        if ( response.getRequestType() == ClientRequest.REQ_NRC_EYES_START ) {
            int i=0;
            for (Object o:response.getResponseList()) {
                if (i==0) {
                    rr = (RaceRun)o;
                    ((RaceRun)o).setLog(log);
                }
                else if (i==1) {
                    BoatEntry boatInStartingBlock = (BoatEntry)o;
                    System.out.println("CLIENT Boat Ready To Start is " + boatInStartingBlock.getRacer().getShortName());
                }
                i++;
            }

        }



         return(rr);
    }

    public RaceRun updateNewNRCeyeFinish(Long startMillis, Long stopMillis) {//}, long stopMillis) {
        RaceRun rr=null;

        ClientRequest request = new ClientRequest(ClientRequest.REQ_NRC_EYES_FINISH);
        request.addObject(startMillis);
        request.addObject(stopMillis);

        //request.addObject(run);
        //request.addObject(run.getPenaltyList());
        clientSocket.send(request);


        ServerResponse response = clientSocket.getResponse(request);

        int i=0;
        for (Object o:response.getResponseList()) {
            rr = (RaceRun)o;
System.out.println("PROXY STOP FROM SERVER " +rr.getBoat().getRacer().getShortName() + " " + rr.getStopWatch().getStartTime() + "-" + rr.getStopWatch().getEndTime() );
            ((RaceRun)o).setLog(log);
            i++;
        }

        System.out.println("!!!!  @1 PROXY Client IS STOPWATCH STOPPED ?" + rr.getStopWatch().isStopped() + " " + rr.getBoat().getRacer().getShortName() );

        return(rr);

    }

// A150519 (ajm) End






    public boolean isGateInSection(int iGate, int section)  {
        ClientRequest request;
        Boolean rc = true;
            request = new ClientRequest(ClientRequest.REQ_IS_GATE_IN_SECTION, iGate, section);
            clientSocket.send(request);
            ServerResponse response = clientSocket.getResponse(request);
            if (response.getRequestType() == ClientRequest.REQ_IS_GATE_IN_SECTION) {
                rc = (Boolean)response.getResponseList().get(0);
            }
        return rc;

    }


    public boolean isFirstGateInSection(int iGate, int section) {
        ClientRequest request;
        Boolean rc = false;
            request = new ClientRequest(ClientRequest.REQ_IS_FIRST_GATE_IN_SECTION, iGate, section);
            clientSocket.send(request);
            ServerResponse response = clientSocket.getResponse(request);
            if (response.getRequestType() == ClientRequest.REQ_IS_FIRST_GATE_IN_SECTION) {
                rc = (Boolean)response.getResponseList().get(0);
            }
        return rc;
    }

    public ArrayList<JudgingSection> getSections() {
        ClientRequest request;
        ArrayList<JudgingSection> sections = new ArrayList<JudgingSection>();
            request = new ClientRequest(ClientRequest.REQ_GET_SECTIONS);
            clientSocket.send(request);
            ServerResponse response = clientSocket.getResponse(request);
            if (response.getRequestType() == ClientRequest.REQ_GET_SECTIONS) {
                for (Object o:response.getResponseList()) {
                    sections.add( (JudgingSection)o );
                }
            }
        return sections;

    }


    public int getNbrGates() {
        Integer nbrGates = 1;
        ClientRequest request;
            request = new ClientRequest(ClientRequest.REQ_GET_NBR_GATES);
            clientSocket.send(request);
            ServerResponse response = clientSocket.getResponse(request);
            if (response.getRequestType() == ClientRequest.REQ_GET_NBR_GATES) {
                nbrGates = (Integer)response.getResponseList().get(0);
            }
        return nbrGates;

    }

    public long getRunsStartedOrCompletedCnt() {
        ClientRequest request;
        long rc = 0l;
            request = new ClientRequest(ClientRequest.REQ_GET_NBR_RUNS_STARTED_OR_COMPLETED);
            clientSocket.send(request);

            ServerResponse response = clientSocket.getResponse(request);
            if ( response.getRequestType() == ClientRequest.REQ_GET_NBR_RUNS_STARTED_OR_COMPLETED ) {
                rc = response.getRunsStartedOrCompletedCnt();
            }
        return rc;
    }

    public boolean isUpstream(int iGate) {
        ClientRequest request;
        Boolean rc = false;
            request = new ClientRequest(ClientRequest.REQ_IS_UPSTREAM, iGate);
            clientSocket.send(request);

            ServerResponse response = clientSocket.getResponse(request);
            if (response.getRequestType() == ClientRequest.REQ_IS_UPSTREAM) {
                rc = (Boolean)response.getResponseList().get(0);
            }
        return rc;
    }
}
