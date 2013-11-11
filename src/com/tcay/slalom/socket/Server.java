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

import com.tcay.util.Log;
import com.tcay.slalom.Penalty;
import com.tcay.slalom.Race;
import com.tcay.slalom.RaceRun;
import com.tcay.slalom.UI.JudgingSection;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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
 * Server listens for clients to connect via socket,then after connection services Requests which are primarily
 * data 'pulls', but also can be 'pushes' to update the race for things such as JudgingSection input.
 *
 * Handle the socket on the server end. When a connection is requested, it spawns a new thread to do the servicing
 * and immediately returns to listening.  The server keeps a unique client number for each  client that connects
 * just to show interesting logging messages.  It is certainly not necessary to do this.
 */
public class Server implements Runnable {

    protected static final int SOCKET_PORT = 9898;
    private static ArrayList<ObjectServerReader> readers = new ArrayList<ObjectServerReader>();

    private ObjectServerWriter osw;
    private ObjectServerReader osr;
    private Log log;

    public static void main(String[] args) throws Exception {
        new Server().listen();
    }


    public Server() {
        log = Log.getInstance();
    }






    public void listen () {
        Socket socket;
        log.info("Server is running.");
        int clientNumber = 0;
        try {
            ServerSocket listener = new ServerSocket(SOCKET_PORT);    /// fixme 2nd invocation of app
            try {

                while (true) {
                    socket = listener.accept();
                    osw = new ObjectServerWriter(socket, clientNumber);
                    osr = new ObjectServerReader(socket, clientNumber, osw);

                    // not here, in run() for osr     xxx readers.add(osr);

                    new Thread(osw).start();
                    new Thread(osr).start();
                    clientNumber++;
                }
            } finally {
                listener.close();
            }
        } catch (IOException e) {
            log.write(e);
        }

    }

    @Override
    public void run() {
        listen();
    }


    /**
     * A private thread to handle requests on a particular
     * socket.  The client terminates the dialogue by sending a ??
     */
    private static class ObjectServerWriter implements Runnable {
        private Socket socket;
        private int clientNumber;
        ArrayList<ClientRequest> requests;
        private Log log;


        public ObjectServerWriter(Socket socket, int clientNumber) {
            log = Log.getInstance();

            this.socket = socket;
            this.clientNumber = clientNumber;
            requests = new  ArrayList<ClientRequest>();
            log.trace("New connection with client# " + clientNumber + " at " + socket);
        }


        public void addRequest(ClientRequest request) {
            synchronized (requests) {
                requests.add(request) ;
            }
        }


        public ClientRequest retrieveRequest() {
            ClientRequest request = null;
            synchronized (requests) {
                if (requests.size() > 0) {
                    request = requests.get(0);
                    requests.remove(0);
                }
                return request;
            }
        }

        private ServerResponse composeResponse(ClientRequest request) {
            ServerResponse response = null;
            if (request.getRequestCmd() == ClientRequest.REQ_GET_SCORABLE_RUNS) {
                response = new ServerResponse(request);//ClientRequest.REQ_GET_SCORABLE_RUNS);
                for (RaceRun run:Race.getInstance().getScorableRuns()){
                    response.add(run);
                }
                log.trace("Wrote Scorable run response");
            }

            if (request.getRequestCmd() == ClientRequest.REQ_GET_NBR_RUNS_STARTED_OR_COMPLETED) {
                //log.trace("Processing Get Nbr Runs Completed");
                response = new ServerResponse(request);//new ServerResponse(ClientRequest.REQ_GET_NBR_RUNS_STARTED_OR_COMPLETED);
                response.setRunsStartedOrCompletedCnt(Race.getInstance().getRunsStartedOrCompletedCnt());
                //log.trace("Wrote runs started or completed response");
            }


            if (request.getRequestCmd() == ClientRequest.REQ_IS_GATE_IN_SECTION) {
                //log.trace("Processing Is Gate in section");
                response = new ServerResponse(request);//new ServerResponse(ClientRequest.REQ_IS_GATE_IN_SECTION);
                Boolean inSection = Race.getInstance().isGateInSection(request.getGate(),request.getSection());

                response.add(inSection);
                //log.trace("Wrote Is Gate in section response");
            }



            if (request.getRequestCmd() ==  ClientRequest.REQ_IS_UPSTREAM ) {
                //log.trace("Processing Is Gate upstream");

                response = new ServerResponse(request);//new ServerResponse(ClientRequest.REQ_IS_UPSTREAM);
                Boolean isUp = Race.getInstance().isUpstream(request.getGate());

                response.add(isUp);
                //log.trace("Wrote Is Gate upstream # " + request.getGate() + " " + isUp);
            }

            if (request.getRequestCmd() ==  ClientRequest.REQ_GET_NBR_GATES ) {
                //log.trace("Processing Is Nbr Gates");

                response = new ServerResponse(request);//new ServerResponse(ClientRequest.REQ_IS_UPSTREAM);
                Integer nbrGates  = Race.getInstance().getNbrGates();

                response.add(nbrGates);
                //log.trace("Wrote Nbr Gates");
            }
            if (request.getRequestCmd() ==  ClientRequest.REQ_GET_SECTIONS ) {
                //log.trace("Processing  Sections");

                response = new ServerResponse(request);//ClientRequest.REQ_GET_SCORABLE_RUNS);
                for (JudgingSection s :Race.getInstance().getSections()){
                    response.add(s);
                }
                //log.trace("Wrote  Sections");

            }

            if (request.getRequestCmd() ==  ClientRequest.REQ_IS_FIRST_GATE_IN_SECTION) {
                boolean rc = false;
                response = new ServerResponse(request);//new ServerResponse(ClientRequest.REQ_IS_UPSTREAM);
                rc = Race.getInstance().isFirstGateInSection(request.getGate());
                response.add(rc);

            }
            return(response);
        }


        /**
         * Services this thread's client by first sending the
         * client a welcome message then repeatedly reading strings
         * and sending back the capitalized version of the string.
         */
        public void run() {
            try {

                OutputStream os = socket.getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
                ClientRequest request;

                Race race = Race.getInstance();
                //Integer i = new Integer(1);
                oos.writeObject(race);
                ServerResponse response;
                while (true) {
                    while (true) {
                        for (request = retrieveRequest();request!= null;request = retrieveRequest() ) {
                            response = composeResponse(request);
                            if (response != null) {
                                oos.writeObject(response);
                            }
                        }
                    }

                }
//            } catch (InterruptedException ie) {
//            } catch (ClassNotFoundException cnfe) {
//                 ie.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
                log.trace("Error handling client# " + clientNumber + ": " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.trace("Couldn't close a socket, what's going on?");
                }
                log.trace("Connection with client# " + clientNumber + " closed");
            }
        }
    }


    /**
     * An ObjectServerReader object will exist for each client attached
     */
    private static class ObjectServerReader implements Runnable {
        private Socket socket;
        private int clientNumber;
        ObjectServerWriter writer;
        private Log log;
        JudgingSection judgingSection;


        public ObjectServerReader(Socket socket, int clientNumber, ObjectServerWriter writer) {
            log = Log.getInstance();

            this.socket = socket;
            this.clientNumber = clientNumber;
            this.writer = writer;
            log.trace("New Read connection with client# " + clientNumber + " at " + socket);
        }

        private void addPenaltiesFromClient(ClientRequest req) {
            RaceRun clientRun;
            RaceRun run;

            clientRun = (RaceRun)req.getDataList().get(0);
            ArrayList<Penalty> clientPenaltyList = (ArrayList<Penalty>)req.getDataList().get(1);

            run       = Race.getInstance().findRun(clientRun);
            for (Penalty p:clientPenaltyList) {
                run.setPenalty(p.getGate(), p.getPenaltySeconds(), false);
            }

            Race.getInstance().updateResults();
        }

   /**
         * Services this thread's client by first sending the
         * client a welcome message then repeatedly reading strings
         * and sending back the capitalized version of the string.
         */
        public void run() {
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                Object o;
                while (true) {

                    try {
                        o = ois.readObject();
                        if (o.getClass() == ClientRequest.class)  {
                            ClientRequest req = (ClientRequest)o;

                            if (req.getRequestCmd() == ClientRequest.REQ_UPDATE_PENALTIES)  {
                                addPenaltiesFromClient(req);
                            }

                            if (req.getRequestCmd() == ClientRequest.REQ_UPDATE_SECTION_ONLINE)  {
                                Integer section = (Integer)req.getDataList().get(0);
                                judgingSection = Race.getInstance().updateSectionOnline(section);
                                judgingSection = Race.getInstance().getSection(section);
                            }

                            writer.addRequest(req);
                        }

                        if (o.getClass() == Penalty.class) {
                            log.trace("Socket Read" + o);
                        }

                        o = null;


                    } catch (EOFException eof) {
                        log.warn("Communication error client ... closing connection");
                        socket.close();           // todo code will attempt to close in finally below ... ok for now
                                           ///fixme .. close both ois and iis when disconnecting !!!  thread cleanup


                        if (judgingSection != null) {
                            judgingSection.setClientDeviceAttached(false);
                        }
                        log.info("connection closed");
                        break;
                    } catch (ClassNotFoundException e) {
                        log.write(e);
                    }
                }
            } catch (IOException e) {
                log.write(e);
                log.trace("Error handling read client# " + clientNumber + ": " + e);
                if (judgingSection != null) {
                    judgingSection.setClientDeviceAttached(false);
                }
            } finally {
                try {
                    socket.close();
                    if (judgingSection != null) {
                        judgingSection.setClientDeviceAttached(false);
                    }
                } catch (IOException e) {
                    log.write(e);
                    log.trace("Couldn't close a read socket, what's going on?");
                }
                log.trace("Connection with read client# " + clientNumber + " closed");
            }
        }
    }
}
