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
import com.tcay.slalom.timingDevices.MicrogateREI2.MicrogateAgent;
import com.tcay.slalom.timingDevices.PhotoCellAgent;
import com.tcay.slalom.timingDevices.alge.AlgeTimy;
import com.tcay.slalom.timingDevices.tagHeuer.TagHeuerAgent;
import com.tcay.util.Log;
import j.extensions.comm.SerialComm;

import java.io.InputStream;
import java.util.ArrayList;


/**
 * @author      Allen Mayers allen.mayers@gmail.com
 * @version     1.0
 * @since       2014
 *
 * This class handles port communications for photoeye timing systems that we accept input from :
 *
 *      TagHeuer CP 5xx devices
 *      Microgate/ALGE devices
 *
 */
public class PhotoEyeListener implements Runnable {

    private Log log;
    //private PhotoCellAgent photoCellAgent;


    public PhotoEyeListener() {
        log = Log.getInstance();
    }


    @Override
    public void run() {
        // fixme Don't run under Windoze, automatic port detection blows up  (Bugzilla Issue #7)
        String os =   System.getProperty("os.name");




        if (os.contains("OS X")){
           listenAndProcessPortOutput(9999999);
        }
    }


    // Todo figure out what if any photoeye equipment is in use, launch appropropriate decoding agent
    //TagHeuerAgent

    public PhotoCellAgent getAgent() {
        return agent;
    }

//    PhotoCellAgent agent = new AlgeTimy();//MicrogateAgent(); /// TODO TagHeuerAgent();
    PhotoCellAgent agent = new TagHeuerAgent();

   // photoCellAgent = agent;
    //new PhotoCellAgent();// TagHeuerAgent();


   // TODO  PhotoCellAgent agent = null;   //  TODO !!!!!!!!!!   new TagHeuerAgent();




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
    private boolean isPortOK(PhotoEyeEquipmentPort ourPort) {  /// MOVE TO RS232 classes
        byte[] readBuffer = new byte[2048];

        SerialComm ubxPort = ourPort.getPort();


        if ((ourPort.getName().toLowerCase().contains("usb") && ourPort.getName().toLowerCase().contains("serial")) ||
            (ourPort.getSystemName().toLowerCase().contains("usb") && ourPort.getSystemName().toLowerCase().contains("serial"))    ) {
            if  (ubxPort.openPort()) {
                ourPort.setCanOpen(true);
                log.info("Opening " + ubxPort.getDescriptivePortName());// + ": " + ubxPort.openPort());
//C20160328                ubxPort.setComPortTimeouts(1000/*TIMEOUT_READ_BLOCKING*/, 1000, 0);
                ubxPort.setComPortTimeouts(SerialComm.TIMEOUT_READ_BLOCKING, 1000, 0);
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


    ArrayList<PhotoEyeEquipmentPort> ourPorts;


    private void getPorts() {
        ourPorts =  new ArrayList<PhotoEyeEquipmentPort>();

        SerialComm[] ports =SerialComm.getCommPorts();
        for (SerialComm port:ports) {
            ourPorts.add(new PhotoEyeEquipmentPort(port));
        }
    }


    private InputStream processPhotoEyeDataFromDevice(int nbrOfCommandToProcess) {
        getPorts();

        log.info("2 Identifying possible serial ports for timing equipment interface:");

        for (PhotoEyeEquipmentPort p:ourPorts) {
            if (isPortOK(p)) {         // see if it a CP520 is attached
                readAndProcess(p, nbrOfCommandToProcess);  // Process 1 command
            }
        }

        // todo pop dialog box for retry ?  or automatic for X times ?

        return null;      /// todo This happens on USB disconnect of Serial port              --- is this method called continuously
    }


    static final int NBR_BYTES_TO_READ = 1000;


    public boolean isConnected() {
        return connected;
    }

    private void readAndProcess(PhotoEyeEquipmentPort p, int commandsToProcess) {

        int commandsProcessed = 0;

        SerialComm ubxPort = null;
        try {
            //int i = 0;
            char c = ' ';
            StringBuffer sb = new StringBuffer();

            ubxPort = p.getPort();

            log.info("Opening " + ubxPort.getDescriptivePortName() + ": " + ubxPort.openPort());
            ubxPort.setComPortTimeouts(SerialComm.TIMEOUT_READ_BLOCKING/*1000*/, 1000, 0);
            InputStream in = ubxPort.getInputStream();
            for (commandsProcessed = 0; commandsProcessed < commandsToProcess; ) {
                for (int j = 0; j < agent.getCommandSize(); ) {//NBR_BYTES_TO_READ; )  {

                    c = (char) in.read();              // todo check for port disconnection and reconnection IOException
                    // On disconnect we get
                    //java.io.IOException: This port appears to have been shutdown or disconnected.
                    //        at j.extensions.comm.SerialComm$SerialCommInputStream.read(SerialComm.java:512)
                    //at com.tcay.RS232.PhotoEyeListener.readAndProcess(PhotoEyeListener.java:221)



                    //i++;

                    j++;  //todo sort this out
                    sb.append(c);

                    // When we get a line of input, direct it to the appropriate timing boxes protocol converter
                    //if (c == '\r') {  / todo \r vs \n
                        if (c == '\r') {
                        if (agent.processDeviceOutput(sb.toString())) {        // If one of these recognized then it's a CP520
                            //p.setCP520(true);
                            //agent.setConnected();
                            connected = true;
                            Race.getInstance().setTagHeuerConnected(true);
                            //setPhotoCellAgent(agent);//setTagHeuerConnected(true);
                            waitingForFirstCommand = false;
                            j = 0;  /// reset buffer  todo sort this out, was quiting

                        }
                        sb = new StringBuffer();
                        //i = 0;

                        commandsProcessed++;
                        if (commandsProcessed >= commandsToProcess) {
                            break;
                        }

                    }


                    //newListener.//

//todo
/*

                    if (agent.processDeviceInput(sb.toString())) {        // If one of these recognized then it's a CP520
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
*/ // end commented out block
                }


                //System.out.print(c);
            }
            in.close();
        } catch (java.io.IOException ioe) {
            // TODO connected = false;
            log.info("PORT CLOSING, TODO Retry");//. Get HERE ON DISCONNECT OR BAD CONNECTION
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // TODO connected = false;

        log.info("\nClosing " + p.getName() + ": " + ubxPort.closePort());
    }

    private void listenAndProcessPortOutput(int nbrOfCommandToProcess) {

        processPhotoEyeDataFromDevice(nbrOfCommandToProcess);

//        `PhotoEyeListener newListener = new PhotoEyeListener();

        // See if it's CP520 attached

        //readAndProcess(newListener, 60);




 return;

    }

    /// Serial-Comm sample code
    static public void main(String[] args)
    {
        new PhotoEyeListener().listenAndProcessPortOutput(20);
    }

}
