package com.tcay.RS232;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 10/9/13
 * Time: 4:32 PM
 * To change this template use File | Settings | File Templates.
 */
import com.tcay.util.Log;
import j.extensions.comm.SerialComm;
import java.io.InputStream;




//import javax.comm.*;
                  // For Tag Heuer Synch the CP 520 to the computer time running SlalomApp

// http://rxtx.qbang.org/wiki/index.php/Main_Page



// /opt/minicom/2.2/bin/minicom


///
// Got something first ... 1 line, then
// Start Run #4
//"S 004          NET TIME

// Start bib 134
//"T       134 M1 20:02:45.766000"

// false start
//"T-      134 M1 20:02:45.766000"

// Result string Finish for bib 134
//"R   3   134          19.920000"


// after a 'false finish' will get this again ... a new time for same bib#
//"R   2   192        2:49.800000"

// Changed Bib number for start time associated to 192
//"T*      192 M1 20:04:37.57000""

// 123456789012345678901234567890
//"T*      192 M1 20:04:37.57000"

public class ListPorts {

    static Log log = Log.getInstance();

    public ListPorts() {
    }


    @Deprecated static public void readALine(String s) {
       // Read from the RS 232 port

//       String s = new String();



       Character c = s.charAt(0);//   null;  // == 1st char

       switch (c) {

           // New Run #
           //i.e. "S 004          NET TIME
           case 'S' :

               break;

           // Time - if channel is M1 = start Time
           //i.e. "T       134 M1 20:02:45.766000"  Normal Start for Bib 134
           //     "T*      192 M1 20:04:37.57000" Changed Bib number for start time associated to 192


           case 'T' :
               c = s.charAt(1);
               // '-' false start
               // '*' means bib number changed for the associated start time
               switch (c) {
                   case ' ':
                       break;
                   case '-':
                       break;
                   case '*':
                       break;
                   default:
                       break;
               }


               break;

           // Result
           //i.e. "R   3   134          19.920000"
           case 'R' :
               // Parse bib and finish run


               String delims = "[ ]+";
               String[] tokens = s.split(delims);



               String t = s.substring(7,11);
               Integer bib = new Integer(t);
               //stime.    HH:MM:SS.ttt000
               String  stime = new String(s.substring(15,29));

               break;


           default :
               break;

       }
   }

                                        /// Serial-Comm sample code
    static public void main(String[] args)
    {
        SerialComm[] ports = SerialComm.getCommPorts();
       log.info("Ports:");
        for (int i = 0; i < ports.length; ++i)
           log.info("   " + ports[i].getSystemPortName() + ": " + ports[i].getDescriptivePortName());
        SerialComm ubxPort = ports[4];




        byte[] readBuffer = new byte[2048];
       log.info("Opening " + ubxPort.getDescriptivePortName() + ": " + ubxPort.openPort());
        ubxPort.setComPortTimeouts(1000/*TIMEOUT_READ_BLOCKING*/, 1000, 0);
        InputStream in = ubxPort.getInputStream();
        try
        {
            for (int i = 0; i < 3; ++i)
            {
               log.trace("\nReading #" + i);
               log.trace("Available: " + ubxPort.bytesAvailable());
                int numRead = ubxPort.readBytes(readBuffer, readBuffer.length);
               log.trace("Read " + numRead + " bytes.");
            }
            in.close();
        } catch (Exception e) { e.printStackTrace(); }

       log.info("\n\nClosing " + ubxPort.getDescriptivePortName() + ": " + ubxPort.closePort());
       log.info("Reopening " + ubxPort.getDescriptivePortName() + ": " + ubxPort.openPort() + "\n");
        in = ubxPort.getInputStream();
        try
        {
            int i = 0;
            char c = ' ';
            StringBuffer sb = new StringBuffer();

            for (int j = 0; j < 1000; ++j)  {

                c = (char)in.read();
                i++;

                sb.append(c);
                if (c == '\r') {
                    readALine(sb.toString());
                    sb = new StringBuffer();
                    i = 0;
                }



                //Sy s tem.out.print(c);


            }
            in.close();
        } catch (Exception e) { e.printStackTrace(); }

       log.info("\nClosing " + ubxPort.getDescriptivePortName() + ": " + ubxPort.closePort());
    }



    public static void XXXmain(String args[]) {

       SerialComm[] sComm = SerialComm.getCommPorts();
       for (SerialComm s:sComm) {
          log.info(s.getSystemPortName()
                  + " "
                  + s.getBaudRate()
                  + " "
                  + s.getDescriptivePortName()
                  + " "
                  + s.bytesAvailable()
          );

       }

//        System.loadLibrary("rxtxSerial");
//        ListPorts lp = new ListPorts();

//        lp.getAvailableSerialPorts();
        //lp.newlistPorts();
    }

}
