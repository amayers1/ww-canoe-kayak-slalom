package com.tcay.slalom.socket;

import com.tcay.util.Log;
import com.tcay.slalom.Race;
import com.tcay.slalom.RaceRun;
import com.tcay.slalom.UI.JudgingSection;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 10/23/13
 * Time: 12:12 PM
 * To change this template use File | Settings | File Templates.
 */


/**
 * Used to route through socket for remote client, or direct shared memory when appropriate
 */
public class Proxy {
    Race race;
    Log log;
    //boolean standAlone = true;    // default

    public boolean isStandAlone() {
        return clientSocket != null;//standAlone;
    }

    Client clientSocket;       // needed for remote


    {
        log = Log.getInstance();
    }



    public Proxy(Client client) {//boolean standAlone) {
        if (client == null) {
            race = Race.getInstance();           // only use race if NOT standalone,
        }
        else {
            clientSocket = client;
            try {
                clientSocket.connectToServer();
            } catch (IOException e) {
                log.warn("");

                e.printStackTrace();
            }
        }
    }

    public ArrayList<RaceRun> getScorableRuns() {
        ClientRequest clientRequest;
        if (isStandAlone() == true) {
            clientRequest = new ClientRequest(ClientRequest.REQ_GET_SCORABLE_RUNS);
            clientSocket.send(clientRequest);
            ServerResponse serverResponse = clientSocket.retrieveResponse(clientRequest);

            // this list has to be a copy for each window, as they are potentially all on separate devices
            ArrayList<RaceRun> returnList = new ArrayList<RaceRun>();
            if ( serverResponse.getRequestType() == ClientRequest.REQ_GET_SCORABLE_RUNS ) {
                int i=0;
                for (Object o:serverResponse.getResponseList()) {
                    ((RaceRun)o).setLog(log);
                    returnList.add( (RaceRun)o );
                    i++;
                    log.trace("Got Run from SlalomApp" + (RaceRun)o );
                }
                log.info("Got " + i + " runs from SlalomApp");
                return returnList;
            }
            return null;
        }
        else {
            return race.getScorableRuns();
        }
    }

    public void updateSectionOnline(Integer section) {
        if (isStandAlone() == true) {
            ClientRequest clientRequest = new ClientRequest(ClientRequest.REQ_UPDATE_SECTION_ONLINE);
            clientRequest.addObject(section);
            clientSocket.send(clientRequest);
        }
        else {
            race.updateSectionOnline(section);
        }
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
        ClientRequest clientRequest = new ClientRequest(ClientRequest.REQ_UPDATE_PENALTIES);

        if (isStandAlone() == true) {
            clientRequest.addObject(run);
            clientRequest.addObject(run.getPenaltyList());
            clientSocket.send(clientRequest);
        }
        else {
            race.updateResults();
        }
    }





    public boolean isGateInSection(int iGate, int section)  {
        ClientRequest clientRequest;
        if (isStandAlone() == true) {
            clientRequest = new ClientRequest(ClientRequest.REQ_IS_GATE_IN_SECTION, iGate, section);
            clientSocket.send(clientRequest);
            ServerResponse serverResponse = clientSocket.retrieveResponse(clientRequest);
            if (serverResponse.getRequestType() == ClientRequest.REQ_IS_GATE_IN_SECTION) {
                Boolean rc = (Boolean)serverResponse.getResponseList().get(0);
                return rc;
            }
            return(true); //fixme - shouldn't get here
        }
        else {
           return race.isGateInSection(iGate, section);
        }
    }


    public boolean isFirstGateInSection(int iGate, int section) {
        ClientRequest clientRequest;
        if (isStandAlone() == true) {
            clientRequest = new ClientRequest(ClientRequest.REQ_IS_FIRST_GATE_IN_SECTION, iGate, section);
            clientSocket.send(clientRequest);
            ServerResponse serverResponse = clientSocket.retrieveResponse(clientRequest);
            if (serverResponse.getRequestType() == ClientRequest.REQ_IS_FIRST_GATE_IN_SECTION) {
                Boolean rc = (Boolean)serverResponse.getResponseList().get(0);
                return rc;
            }
            return false;
        }
        else {
            return race.isFirstGateInSection(iGate);
        }

    }

    public ArrayList<JudgingSection> getSections() {
        ClientRequest clientRequest;
        if (isStandAlone() == true) {
            ArrayList<JudgingSection> sections = new ArrayList<JudgingSection>();
            clientRequest = new ClientRequest(ClientRequest.REQ_GET_SECTIONS);
            clientSocket.send(clientRequest);
            ServerResponse serverResponse = clientSocket.retrieveResponse(clientRequest);
            if (serverResponse.getRequestType() == ClientRequest.REQ_GET_SECTIONS) {
                for (Object o:serverResponse.getResponseList()) {
                    sections.add( (JudgingSection)o );
                }
            }
            return sections;
        }
        else {
            return race.getSections();
        }
    }


    public int getNbrGates() {
        Integer nbrGates = 1;
        ClientRequest clientRequest;
        if (isStandAlone() == true) {
            clientRequest = new ClientRequest(ClientRequest.REQ_GET_NBR_GATES);
            clientSocket.send(clientRequest);
            ServerResponse serverResponse = clientSocket.retrieveResponse(clientRequest);
            if (serverResponse.getRequestType() == ClientRequest.REQ_GET_NBR_GATES) {
                nbrGates = (Integer)serverResponse.getResponseList().get(0);
            }
            return nbrGates;
        }
        else {
            return race.getNbrGates();
        }
    }

    public long getRunsStartedOrCompletedCnt() {
        ClientRequest clientRequest;
        if (isStandAlone() == true) {
            clientRequest = new ClientRequest(ClientRequest.REQ_GET_NBR_RUNS_STARTED_OR_COMPLETED);
            clientSocket.send(clientRequest);

            ServerResponse serverResponse = clientSocket.retrieveResponse(clientRequest);
            if ( serverResponse.getRequestType() == ClientRequest.REQ_GET_NBR_RUNS_STARTED_OR_COMPLETED ) {
                return(serverResponse.getRunsStartedOrCompletedCnt());
            }
            return 0L;
        }
        else {

            return race.getRunsStartedOrCompletedCnt();
        }

    }

    public boolean isUpstream(int iGate) {
        ClientRequest clientRequest;
        if (isStandAlone() == true) {
            clientRequest = new ClientRequest(ClientRequest.REQ_IS_UPSTREAM, iGate);
            clientSocket.send(clientRequest);

            ServerResponse serverResponse = clientSocket.retrieveResponse(clientRequest);
            if (serverResponse.getRequestType() == ClientRequest.REQ_IS_UPSTREAM) {
                Boolean rc = (Boolean)serverResponse.getResponseList().get(0);
                return rc;
            }
            return false;
        }
        else {
            return race.isUpstream(iGate);
        }
    }
}
