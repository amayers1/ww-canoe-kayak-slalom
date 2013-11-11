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
import com.tcay.slalom.RaceRun;

import javax.swing.*;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
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
 * Client for remote devices.  Initially these are only Scoring Stations for Section Judges.
 */
public class Client {
    // fixme If you start client, and server has no race, then no gate buttons show up, but racers runs appear


    private static final int RETRY_SOCKET_CONNECTION_DELAY_MS = 3000;

    // Need to be final for "synchronized" access
    final ArrayList<ClientRequest> requests = new ArrayList<ClientRequest>();
    final ArrayList<ServerResponse> responses = new ArrayList<ServerResponse>();
    ObjectClientWriter ocw;
    ObjectClientReader ocr;
    private JFrame frame = new JFrame("Communications Dispatcher");
    private JTextField dataField = new JTextField(40);
    private JTextArea messageArea = new JTextArea(8, 60);
    private Log log;

    public Client() {
        init();
    }

    /**
     * Runs the client application.
     */
    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        client.frame.pack();
        client.frame.setVisible(true);
        client.connectToServer();//client.frame);
    }

    private void init() {
        log = Log.getInstance();

        messageArea.setEditable(false);
        frame.getContentPane().add(dataField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
    }

    public void send (ClientRequest req) {
        synchronized (requests) {
           requests.add(req);
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

    public void queue(ServerResponse res) {
        //log.trace("Waiting for sync ... response");
        synchronized (responses) {
            //log.trace("Queued response type " + res.getRequestType());
            responses.add(res);
        }
    }

    public ServerResponse getResponse(ClientRequest request) {
        ServerResponse response = null;

        //log.trace("   Waiting for response @1");
        while (response == null) {
            while (responses.size() == 0) {
                try {
                    Thread.sleep(0);//5);//0);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }

            synchronized (responses) {
                if (responses.size() > 0) {
                    for (ServerResponse possible:responses) {
                        if (request.getRequestNbr().intValue() == possible.getRequestNbr().intValue()) {
                            response = possible;
                            //log.trace(possible);
                            responses.remove(possible);
                            break;
                        }
                    }
                }
                //log.trace("   Returning response @2");
                if (response != null) {
                    return response;
                }
            }
        }
        return null;
    }

    /**
     * Implements the connection logic by prompting the end user for
     * the server's IP address, connecting, setting up streams, and
     * consuming the welcome messages from the server.  The ObjectServerWriter
     * protocol says that the server sends three lines of text to the
     * client immediately after establishing a connection.
     */
    public void connectToServer()/*JFrame f) */ throws IOException {

        // Get the server address from a dialog box.

        boolean slalomAppServerIsLocal = false;
        boolean connected = false;
        String serverAddress = "127.0.0.1";
        Socket socket = null;

        while (!connected) {
            log.trace("Trying Socket");
            connected = false;
            try {
                socket = new Socket(serverAddress, Server.SOCKET_PORT);
                slalomAppServerIsLocal = true;
                connected = true;
                log.trace("Local Socket found");


            } catch(ConnectException ce) {
                slalomAppServerIsLocal = false;
            }

            if (!slalomAppServerIsLocal) {
                serverAddress = JOptionPane.showInputDialog(
                        frame,
                        "Enter IP Address of the Server or blank for local: ",
                        "Slalom Section Judge Station",
                        JOptionPane.QUESTION_MESSAGE);

                // Make connection and initialize streams
                if (serverAddress.trim().length() == 0)
                    serverAddress = "127.0.0.1";

                try {
                    socket = new Socket(serverAddress, Server.SOCKET_PORT);   /// fixme - handle if no server is running ... wit
                    connected = true;
                }  catch(ConnectException ce) {

                }
            }
            if (!connected) {
                log.trace("Waiting " + RETRY_SOCKET_CONNECTION_DELAY_MS/1000+ " seconds for retry on Socket");
                try {
                    Thread.sleep(RETRY_SOCKET_CONNECTION_DELAY_MS);
                } catch (InterruptedException e) {
                }
            }
        }

        /// fixme loop on New Socket call until attached or an interrupt occurs
        ocw = new ObjectClientWriter(socket, this);
        ocr = new ObjectClientReader(socket, this);
        new Thread(ocw).start();
        new Thread(ocr).start();

    }

    private static class ObjectClientWriter implements Runnable {
        private Socket socket;
        private Client sosc;
        private Log log;



        public ObjectClientWriter(Socket socket, Client sosc) {
            log = Log.getInstance();
            this.sosc = sosc;
            this.socket = socket;
            log.info("Talking to SlalomApp server  " + socket);
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
                while (true) {
                    for (request = sosc.retrieveRequest();request!= null;request = sosc.retrieveRequest() ) {
                        oos.writeObject(request);
                        //log.trace("Wrote requestCmd type " + request.getRequestCmd());
                    }
                    //Thread.sleep(500);
                }
            //} catch (InterruptedException e) {
            //    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (SocketException se) {
                log.warn("Communication error with server");        // fixme reconnection try
            } catch (IOException e) {
                log.error("Error handling server : " + e);
                log.write(e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    log.error("Couldn't close a socket, what's going on?");
                    log.write(e);
                }
                log.info("Connection with server closed");
            }
        }
    }

    private static class ObjectClientReader implements Runnable {
        Client sosc;
        private Socket socket;
        private Log log;

        public ObjectClientReader(Socket socket, Client sosc) {
            this.sosc = sosc;
            this.socket = socket;
            log = Log.getInstance();
            log.info("Listening to SlalomApp server " + socket);
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
                        if (o.getClass() == ServerResponse.class) {
                            ServerResponse response = (ServerResponse)o;
                            sosc.queue(response);

                        }

                        if (o.getClass() == RaceRun.class) {
                            RaceRun r = (RaceRun)o;
                            log.trace(r.toString());
                        }

                        if (o.getClass() == Penalty.class) {
                            log.trace("Socket Read" + o );
                        }

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }

            } catch (EOFException eof) {
                log.warn("Problem communicating with SlalomApp server (EOF)");  // fixme reconnectiont try - scenario kill server

            } catch (IOException e) {
                log.error("Problem communicating with SlalomApp server (I/O)" + e);
                log.write(e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    log.error("Couldn't close a read socket, what's going on?");
                    log.write(e);
                }
                log.info("Connection to SlalomApp server closed");
            }
        }
    }
}
