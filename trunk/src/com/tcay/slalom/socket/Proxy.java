package com.tcay.slalom.socket;

import com.sun.javaws.exceptions.InvalidArgumentException;
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

    public Proxy(Client client) throws InvalidArgumentException {//boolean standAlone) {
        if (client == null) {
            //race = Race.getInstance();     // NO LONGER LEGAL 131110 (ajm) only use race if NOT standalone,
            throw new InvalidArgumentException(message);
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
     * issue with serialization that I did not sort put.   Sending the penalty list as a separate
     * object as done below, then having the server process it works fine for now.
     *
     * @param run
     */
    public void updateResults(RaceRun run) {
        ClientRequest request = new ClientRequest(ClientRequest.REQ_UPDATE_PENALTIES);

            request.addObject(run);
            request.addObject(run.getPenaltyList());
            clientSocket.send(request);
    }





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
