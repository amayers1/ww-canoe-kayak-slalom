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

package com.tcay.RS232;

/**
 * ${PROJECT_NAME}
 *
 * Teton Cay Group Inc. ${YEAR}
 *

 * User: allen
 * Date: 10/11/13
 * Time: 6:07 AM
 *
 */


import com.tcay.slalom.Race;
import com.tcay.slalom.tagHeuer.TagHeuerAgent;
import com.tcay.util.Log;
import j.extensions.comm.SerialComm;
import java.io.InputStream;
import java.util.ArrayList;


public class TagHeuerCP520Listener implements Runnable {

    Log log;


    public TagHeuerCP520Listener() {
        log = Log.getInstance();
    }


    @Override
    public void run() {
        // fixme Don't run under Windoze, automatic port detection blows up  (Issue #7)
        String os =   System.getProperty("os.name");
        if (os.contains("OS X")){
           listenAndProcessPortOutput(9999999);
        }
    }

    TagHeuerAgent agent = new TagHeuerAgent();


    private boolean connected = false;

    public boolean isWaitingForFirstCommand() {
        return waitingForFirstCommand;
    }

    private boolean waitingForFirstCommand = true;




//    public class TestOnly {
//        public void run() {
//            listenAndProcessPortOutput(1);
//        }
//    }





    /**
     * does this look like a USB serial port, can ii tbe opened ?
     * @param ourPort
     * @return
     */
    private boolean isPortOK(CandidateCP520Port ourPort) {
        byte[] readBuffer = new byte[2048];

        SerialComm ubxPort = ourPort.getPort();


        if ((ourPort.getName().toLowerCase().contains("usb") && ourPort.getName().toLowerCase().contains("serial")) ||
            (ourPort.getSystemName().toLowerCase().contains("usb") && ourPort.getSystemName().toLowerCase().contains("serial"))    ) {
            if  (ubxPort.openPort()) {
                ourPort.setCanOpen(true);
                log.info("Opening " + ubxPort.getDescriptivePortName());// + ": " + ubxPort.openPort());
                ubxPort.setComPortTimeouts(1000/*TIMEOUT_READ_BLOCKING*/, 1000, 0);
                InputStream in = ubxPort.getInputStream();
                try
                {
                    for (int i = 0; i < 3; ++i)
                    {
                        //log.info("\nReading #" + i);
                        //log.info("Available: " + ubxPort.bytesAvailable());
                        int numRead = ubxPort.readBytes(readBuffer, readBuffer.length);
                        log.info("Read " + numRead + " bytes.");
                    }
                    in.close();
                    log.info("Closing " + ubxPort.getDescriptivePortName() + ": " + ubxPort.closePort());
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    ourPort.setHadException(true);
                    //return false;
                }
            }
            else {
                ourPort.setCanOpen(false);
                return false;
            }


        }

        return false;
    }


    ArrayList<CandidateCP520Port> ourPorts;


    private void getPorts() {
        ourPorts =  new ArrayList<CandidateCP520Port>();

        SerialComm[] ports =SerialComm.getCommPorts();
        for (SerialComm port:ports) {
            ourPorts.add(new CandidateCP520Port(port));
        }
    }


    private InputStream lookForTagHeuerCP520Port(int nbrOfCommandToProcess) {
        getPorts();

        log.info("Identifying possible serial ports for timing equipment interface:");

        for (CandidateCP520Port p:ourPorts) {
            if (isPortOK(p)) {         // see if it a CP520 is attached
                readAndProcess(p, nbrOfCommandToProcess);  // Process 1 command
            }
        }

        return null;      /// todo This happens on USB disconnect of Serial port              --- is this method called continuously
    }


    static final int NBR_BYTES_TO_READ = 1000;


    public boolean isConnected() {
        return connected;
    }

    private void readAndProcess(CandidateCP520Port p, int commandsToProcess)
    {

        int commandsProcessed = 0;

        SerialComm ubxPort=null;
        try
        {
            //int i = 0;
            char c = ' ';
            StringBuffer sb = new StringBuffer();

            ubxPort = p.getPort();

            log.info("Opening " + ubxPort.getDescriptivePortName() + ": " + ubxPort.openPort());
            ubxPort.setComPortTimeouts(1000, 1000, 0);
            InputStream in = ubxPort.getInputStream();

            for (int j = 0; j < NBR_BYTES_TO_READ; )  {

                c = (char)in.read();              // todo check for port disconnection and reconnection IOException
                //i++;

                j++;  //todo sort this out
                sb.append(c);
                if (c == '\r') {
                    //newListener.//
                    if (agent.processCP520_output(sb.toString())) {        // If one of these recognized then it's a CP520
                        p.setCP520(true);
                        connected = true;
                        Race.getInstance().setTagHeuerConnected(true);

                        waitingForFirstCommand = false;
                        j = 0;  /// reset buffer  todo sort this out, was quiting

                    }
                    sb = new StringBuffer();
                    //i = 0;

                    commandsProcessed ++;
                    if (commandsProcessed>=commandsToProcess) {
                        break;
                    }
                }
                //Sy s tem.out.print(c);
            }
            in.close();
        }
        catch (java.io.IOException ioe) {
            //connected = false;
            log.info("PORT CLOSING, TODO Retry"); ioe.printStackTrace();
        }
        catch (Exception e) { e.printStackTrace(); }
        //connected = false;

        log.info("\nClosing " + p.getName() + ": " + ubxPort.closePort());
    }

    private void listenAndProcessPortOutput(int nbrOfCommandToProcess) {

        lookForTagHeuerCP520Port(nbrOfCommandToProcess);

//        `TagHeuerCP520Listener newListener = new TagHeuerCP520Listener();

        // See if it's CP520 attached

        //readAndProcess(newListener, 60);




 return;

    }

    /// Serial-Comm sample code
    static public void main(String[] args)
    {
        new TagHeuerCP520Listener().listenAndProcessPortOutput(20);
    }

}
