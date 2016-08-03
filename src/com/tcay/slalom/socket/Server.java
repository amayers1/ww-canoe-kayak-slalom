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

import com.tcay.SignalSemaphore;
import com.tcay.slalom.BoatEntry;
import com.tcay.slalom.UI.RaceTimingUI;
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
        //log.setCurrentLevel(Log.LOG_TRACE);
    }






    public void listen () {
        Socket socket;
        SignalSemaphore signalSema;
        log.info("Server is running.");
        int clientNumber = 0;
        try {
            ServerSocket listener = new ServerSocket(SOCKET_PORT);    /// fixme 2nd invocation of app
            try {

                while (true) {
                    socket = listener.accept();
                    signalSema = new SignalSemaphore();
                    osw = new ObjectServerWriter(socket, clientNumber, signalSema);
                    osr = new ObjectServerReader(socket, clientNumber, osw, signalSema);

                    // not here, in run() for osr     xxx readers.add(osr);

                    new Thread(osw).start();
                    new Thread(osr).start();
                    clientNumber++;
                }
            } finally {
                listener.close();
            }
        } catch (IOException e) {
            log.write(e);///todo THIS Is App already running error or other
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
        SignalSemaphore wakeupSemaphore;

        public ObjectServerWriter(Socket socket, int clientNumber, SignalSemaphore wakeupSemaphore) {
            log = Log.getInstance();
            this.wakeupSemaphore = wakeupSemaphore;
            this.socket = socket;
            this.clientNumber = clientNumber;
            requests = new  ArrayList<ClientRequest>();
            log.trace("New Client#" + clientNumber + " outbound connection at socket#" + socket);
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
                response = new ServerResponse(request);
                for (RaceRun run:Race.getInstance().getScorableRuns()){
                    response.add(run);
                }
                log.trace("Wrote Scorable run response to sec=" + request.getSection());
            }

            if (request.getRequestCmd() == ClientRequest.REQ_GET_NBR_RUNS_STARTED_OR_COMPLETED) {
                //System.out.println("RQST ");
                //log.trace("Processing Get Nbr Runs Completed");
                response = new ServerResponse(request);
                response.setRunsStartedOrCompletedCnt(Race.getInstance().getRunsStartedOrCompletedCnt());
                log.trace("Wrote # runs started or completed response");
            }


            if (request.getRequestCmd() == ClientRequest.REQ_IS_GATE_IN_SECTION) {
                //log.trace("Processing Is Gate in section");
                response = new ServerResponse(request);//new ServerResponse(ClientRequest.REQ_IS_GATE_IN_SECTION);
                Boolean inSection = Race.getInstance().isGateInSection(request.getGate(),request.getSection());

                response.add(inSection);
                log.trace("Wrote Is Gate in section response");
            }



            if (request.getRequestCmd() ==  ClientRequest.REQ_IS_UPSTREAM ) {
                //log.trace("Processing Is Gate upstream");

                response = new ServerResponse(request);//new ServerResponse(ClientRequest.REQ_IS_UPSTREAM);
                Boolean isUp = Race.getInstance().isUpstream(request.getGate());

                response.add(isUp);
                log.trace("Wrote Is Gate upstream # " + request.getGate() + " " + isUp);
            }

            if (request.getRequestCmd() ==  ClientRequest.REQ_GET_NBR_GATES ) {
                //log.trace("Processing Is Nbr Gates");

                response = new ServerResponse(request);//new ServerResponse(ClientRequest.REQ_IS_UPSTREAM);
                Integer nbrGates  = Race.getInstance().getNbrGates();

                response.add(nbrGates);
                log.trace("Wrote Nbr Gates");
            }
            if (request.getRequestCmd() ==  ClientRequest.REQ_GET_SECTIONS ) {
                //log.trace("Processing  Sections");

                response = new ServerResponse(request);//ClientRequest.REQ_GET_SCORABLE_RUNS);
                for (JudgingSection s :Race.getInstance().getSections()){
                    response.add(s);
                }
                log.trace("Wrote  Sections");
            }

            if (request.getRequestCmd() ==  ClientRequest.REQ_IS_FIRST_GATE_IN_SECTION) {
                boolean rc = false;
                response = new ServerResponse(request);//new ServerResponse(ClientRequest.REQ_IS_UPSTREAM);
                rc = Race.getInstance().isFirstGateInSection(request.getGate());
                response.add(rc);
                log.trace("Wrote  REQ_IS_FIRST_GATE_IN_SECTION");

            }

            if (request.getRequestCmd() ==  ClientRequest.REQ_NRC_EYES_START)     {
                boolean rc = false;
                response = new ServerResponse(request);//new ServerResponse(ClientRequest.REQ_IS_UPSTREAM);

                RaceRun run = Race.getInstance().getNewestActiveRun();
                response.add(run);
                BoatEntry boatReadyToStart = Race.getInstance().getBoatInStartingBlock();
                response.add(boatReadyToStart);

            }

            if (request.getRequestCmd() ==  ClientRequest.REQ_NRC_EYES_FINISH)     {
                //boolean rc = false;
                response = new ServerResponse(request);//new ServerResponse(ClientRequest.REQ_IS_UPSTREAM);

                Race race = Race.getInstance();

                // todo NRC crash here if we run out of start list and we get a finish from NRC

                RaceRun run = race.getNewestCompletedRun();//OldestActiveRun();   //NewestCompletedRun



                System.out.println("!!!! "+ run.getBoat().getRacer().getShortName() + " @2 ID=" + run.getStopWatch().getId() + "ServerResponse SERVER IS STOPWATCH STOPPED ?" + run.getStopWatch().isStopped() + " " + run.getBoat().getRacer().getShortName() + " Hash=" +System.identityHashCode(run.getStopWatch()) );

                response.add(run);

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
                //while (true) {
                    while (true) {
                        try {
                            wakeupSemaphore.release();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        //System.out.println("DE QUEING AN OBJECT REQ !!");

                        for (request = retrieveRequest();request!= null;request = retrieveRequest() ) {
                            //System.out.println("MAKING RESPONSE !!");

                            response = composeResponse(request);
                            //System.out.println("MADE RESPONSE !!");

                            if (response != null) {
                                oos.writeObject(response);
                            }
                            //System.out.println("WROTE RESPONSE !!");

                        }
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

               // }
//            } catch (InterruptedException ie) {
//            } catch (ClassNotFoundException cnfe) {
//                 ie.printStackTrace();

            } catch (IOException e) {

                /// this is a port interrogation of UNKNOWN type - A PORT SCAN maybe

                e.printStackTrace();
                log.error("Error handling client# " + clientNumber + ": " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("Couldn't close a socket, what's going on?");
                }
                log.info("Connection with client# " + clientNumber + " closed");
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
        SignalSemaphore queSignale;


        public ObjectServerReader(Socket socket, int clientNumber, ObjectServerWriter writer, SignalSemaphore signalSemaphore) {
            log = Log.getInstance();
            this.queSignale = signalSemaphore;
            this.socket = socket;
            this.clientNumber = clientNumber;
            this.writer = writer;
            log.trace("New Client#" + clientNumber + " inbound connection at socket#" + socket);
        }



        public static String padRight(String s, int n) {
            String sRet="";
            if (n > 0 ) {
                sRet = String.format("%1$-" + n + "s", s);
            }
            return sRet;
        }

        public static String padLeft(String s, int n) {
            return String.format("%1$" + n + "s", s);
        }







        private void addPenaltiesFromClient(ClientRequest req) {
            RaceRun clientRun;
            RaceRun run;
            //StringBuffer penaltyLog = new StringBuffer();
            clientRun = (RaceRun)req.getDataList().get(0);
            ArrayList<Penalty> clientPenaltyList = (ArrayList<Penalty>)req.getDataList().get(1);
            run       = Race.getInstance().findRun(clientRun);

            //penaltyLog.append("PEN"+run.getLogString());
            //penaltyLog.append(" s=" + req.getSection());

            /// Todo fix this so that all requests have client section or identifier
            int reportingSection = req.getSection();// - 1;  //todo 20160405   CHECK THIS CAREFULLY AJM AJM  removed "-1"
            if (reportingSection<1) {
                reportingSection = 0;
            }


            //penaltyLog.append(padRight(" ", reportingSection * 15));
            for (Penalty p:clientPenaltyList) {
                //   TODO Exception in line below "run.setPenalty(p.getGate(), p.getPenaltySeconds(), false);"

                /*
                *Exception in thread "Thread-25" java.lang.NullPointerException
                	at com.tcay.slalom.socket.Server$ObjectServerReader.addPenaltiesFromClient(Server.java:389  )
                	at com.tcay.slalom.socket.Server$ObjectServerReader.run(Server.java:455)
                	at java.lang.Thread.run(Thread.java:695)

                 */
                run.setPenalty(p.getGate(), p.getPenaltySeconds(), false);
            //    penaltyLog.append(String.format("% 2d", p.getPenaltySeconds()));
            }

            //log.info(penaltyLog.toString());

            run.logPenalties(log, reportingSection, "SPEN" );


            Race.getInstance().updateResults(run);  // TODO REALLY NEEDED ???  YES - BUILD NEW HTML RACER page
        }

   /**
         * Services this thread's client by first sending the
         * client a welcome message then repeatedly reading strings
         * and sending back the capitalized version of the string.
         */
        public void run() {
            try {
                ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

                Object o;
                while (true) {

                    try {
                        //System.out.println("WAITING ON AN OBJECT REQ !!");
                        o = ois.readObject();
                        //System.out.println("READ AN OBJECT !!");
                        if (o.getClass() == ClientRequest.class)  {
                            ClientRequest req = (ClientRequest)o;
// A150519 (ajm) Start
// todo flush out
                            if (req.getRequestCmd() == ClientRequest.REQ_NRC_FORCE_NEW_RUN){
                                RaceTimingUI rtui =   RaceTimingUI.getInstance();

                                 rtui.forceNRCNewRun();
                            }

                            if (req.getRequestCmd() == ClientRequest.REQ_NRC_EYES_START)  {
                                Long startMillis = (Long)req.getDataList().get(0);
                                RaceTimingUI.getInstance().fakeyStart(startMillis); //todo refactor out of UI

                            }
                            if (req.getRequestCmd() == ClientRequest.REQ_NRC_EYES_FINISH)  {

                                RaceRun rr = Race.getInstance().getOldestActiveRun();//getNewestCompletedRun();



                                Long startMillis = (Long)req.getDataList().get(0);
                                Long stopMillis = (Long)req.getDataList().get(1);
             //todo tghread timing issue with finishing the run and then server send last
                                RaceTimingUI.getInstance().fakeyFinish(rr, startMillis, stopMillis);
                                System.out.println("!!!!  @1 SERVER IS STOPWATCH STOPPED ?" + rr.getStopWatch().isStopped() + " From " +
                                        rr.getStopWatch().getStartTime() + "-" + rr.getStopWatch().getEndTime());
                                //RaceTimingUI.getInstance().     //todo refactor out of UI
                                //RaceRun run = Race.getInstance().getNewestCompletedRun();
                                //run.finish();
                            }



// A150519 (ajm) End


                            if (req.getRequestCmd() == ClientRequest.REQ_UPDATE_PENALTIES)  {
                                addPenaltiesFromClient(req);
                            }



                            if (req.getRequestCmd() == ClientRequest.REQ_UPDATE_SECTION_ONLINE)  {
                                Integer section = (Integer)req.getDataList().get(0);
                                judgingSection = Race.getInstance().updateSectionOnline(section);
                                judgingSection = Race.getInstance().getSection(section);
                            }

                            //System.out.println("QUEING AN OBJECT REQ !!");

                            writer.addRequest(req);
                            queSignale.set();
                           // System.out.println("QUEUED !!");
                        }

                        if (o.getClass() == Penalty.class) {
                            log.trace("Socket Read" + o);
                        }
// THI IS A BUSY LOOP !!!!!!!
                        o = null;


                    } catch (EOFException eof) {


                        //todo update status bar
                        log.warn("Communication error client ... closing connection c=#" + clientNumber + " s=" + judgingSection.getSectionNbr());
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
                    if (judgingSection != null) {
                        judgingSection.setClientDeviceAttached(false);
                    socket.close();
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
