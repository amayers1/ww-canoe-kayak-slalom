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

package com.tcay.util;

import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.net.*;
import java.util.logging.*;
import java.util.*;

/**
 * ${PROJECT_NAME}
 *
 * Teton Cay Group Inc. ${YEAR}
 *

 * User: allen
 * Date: 11/3/13
 * Time: 8:27 PM
 *
 */
public class NistTimeSync {

    public static void main(String args[  ]) {
        try {     // Handle startup exceptions at the end of this block
            // Get an encoder for converting strings to bytes
            CharsetEncoder encoder = Charset.forName("US-ASCII").newEncoder( );

            // Allow an alternative port for testing with non-root accounts
            int port = 13;   // RFC867 specifies this port.
            if (args.length > 0) port = Integer.parseInt(args[0]);

            // The port we'll listen on

            //ajm A131103
            InetAddress addr = InetAddress.getByName("132.163.4.101");

            //SocketAddress localport = new InetSocketAddress(addr, port);
            SocketAddress localport = new InetSocketAddress(port);

            // Create and bind a TCP channel to listen for connections on.
            ServerSocketChannel tcpserver = ServerSocketChannel.open( );
            tcpserver.socket( ).bind(localport);

            // Also create and bind a DatagramChannel to listen on.
            DatagramChannel udpserver = DatagramChannel.open( );
            udpserver.socket( ).bind(localport);

            // Specify non-blocking mode for both channels, since our
            // Selector object will be doing the blocking for us.
            tcpserver.configureBlocking(false);
            udpserver.configureBlocking(false);

            // The Selector object is what allows us to block while waiting
            // for activity on either of the two channels.
            Selector selector = Selector.open( );

            // Register the channels with the selector, and specify what
            // conditions (a connection ready to accept, a datagram ready
            // to read) we'd like the Selector to wake up for.
            // These methods return SelectionKey objects, which we don't
            // need to retain in this example.
            tcpserver.register(selector, SelectionKey.OP_ACCEPT);
            udpserver.register(selector, SelectionKey.OP_READ);

            // This is an empty byte buffer to receive empty datagrams with.
            // If a datagram overflows the receive buffer size, the extra bytes
            // are automatically discarded, so we don't have to worry about
            // buffer overflow attacks here.
            ByteBuffer receiveBuffer = ByteBuffer.allocate(0);

            // Now loop forever, processing client connections
            for(;;) {
                try { // Handle per-connection problems below
                    // Wait for a client to connect
                    selector.select( );

                    // If we get here, a client has probably connected, so
                    // put our response into a ByteBuffer.
                    String date = new java.util.Date( ).toString( ) + "\r\n";
                    ByteBuffer response=encoder.encode(CharBuffer.wrap(date));

                    // Get the SelectionKey objects for the channels that have
                    // activity on them. These are the keys returned by the
                    // register( ) methods above. They are returned in a
                    // java.util.Set.
                    Set keys = selector.selectedKeys( );

                    // Iterate through the Set of keys.
                    for(Iterator i = keys.iterator( ); i.hasNext( ); ) {
                        // Get a key from the set, and remove it from the set
                        SelectionKey key = (SelectionKey)i.next( );
                        i.remove( );

                        // Get the channel associated with the key
                        Channel c = (Channel) key.channel( );

                        // Now test the key and the channel to find out
                        // whether something happened on the TCP or UDP channel
                        if (key.isAcceptable( ) && c == tcpserver) {
                            // A client has attempted to connect via TCP.
                            // Accept the connection now.
                            SocketChannel client = tcpserver.accept( );
                            // If we accepted the connection successfully,
                            // then send our response back to the client.
                            if (client != null) {
                                client.write(response);  // send response
                                client.close( );          // close connection
                            }
                        }
                        else if (key.isReadable( ) && c == udpserver) {
                            // A UDP datagram is waiting.  Receive it now,
                            // noting the address it was sent from.
                            SocketAddress clientAddress =
                                    udpserver.receive(receiveBuffer);
                            // If we got the datagram successfully, send
                            // the date and time in a response packet.
                            if (clientAddress != null)
                                udpserver.send(response, clientAddress);
                        }
                    }
                }
                catch(java.io.IOException e) {
                    // This is a (hopefully transient) problem with a single
                    // connection: we log the error, but continue running.
                    // We use our classname for the logger so that a sysadmin
                    // can configure logging for this server independently
                    // of other programs.
                    Logger l = Logger.getLogger(NistTimeSync.class.getName( ));
                    l.log(Level.WARNING, "IOException in DaytimeServer", e);
                }
                catch(Throwable t) {
                    // If anything else goes wrong (out of memory, for example),
                    // then log the problem and exit.
                    Logger l = Logger.getLogger(NistTimeSync.class.getName( ));
                    l.log(Level.SEVERE, "FATAL error in DaytimeServer", t);
                    System.exit(1);
                }
            }
        }
        catch(Exception e) {
            // This is a startup error: there is no need to log it;
            // just print a message and exit
            System.err.println(e);
            System.exit(1);
        }
    }





    /*

    source : http://cosinekitty.com/nist/

 *      C# class for retrieving accurate date and time from NIST atomic clock timeservers.
 *      By Don Cross - 30 August 2008 - http://cosinekitty.com

    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Text;
    using System.Net;
    using System.Net.Sockets;
    using System.Runtime.InteropServices;
    using System.Diagnostics;

    namespace Nist
    {
        public class NistClock
        {
            public NistClock ()
            : this (IPAddress.Parse ("132.163.4.101"))
            {
            }

            public NistClock (IPAddress _timeServerIpAddress)
            {
                timeServerIpAddress = _timeServerIpAddress;
            }

            public bool PrintDiagnosticMessages
            {
                get { return printDiagnosticMessages; }
                set { printDiagnosticMessages = value; }
            }

            private string QueryDaytimeString (out TimeSpan roundTripTime)
            {
                // Use RFC-867 protocol to query time from NIST time server...

                int count = 0;
                Stopwatch stopWatch = new Stopwatch ();

                while (true) {
                    ++count;

                    using (Socket socket = new Socket (AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp)) {
                        IPEndPoint hostEndPoint = new IPEndPoint (timeServerIpAddress, 13);

                        stopWatch.Reset ();
                        stopWatch.Start ();
                        socket.Connect (hostEndPoint);
                        int numberOfBytes = socket.Receive (buffer);
                        stopWatch.Stop ();

                        if (numberOfBytes == 51) {
                            roundTripTime = stopWatch.Elapsed;
                            string daytimeString = System.Text.ASCIIEncoding.ASCII.GetString (buffer, 0, numberOfBytes).Trim ();
                            return daytimeString;
                        } else {
                            if (printDiagnosticMessages) {
                                Console.WriteLine ("Attempt #{0}:  byte count received = {1}", count, numberOfBytes);
                            }
                        }
                    }
                    System.Threading.Thread.Sleep (100);
                }
            }

            private static char[] SeparatorArray = new char[] { ' ' };

            // In USA, we use "." as decimal point (e.g. pi = 3.141592)
            // but in Europe, they use commas (pi = 3,141592).
            // The following is needed for parsing double with "." delimiter for users with non-US defaults:
            private static System.Globalization.CultureInfo EnglishUSACulture = new System.Globalization.CultureInfo ("en-US");

            private static DateTime ParseDaytimeProtocol (string daytimeString)
            {
                // 54708 08-08-30 18:53:02 50 0 0 770.8 UTC(NIST) *
                //   0       1        2     3 4 5   6      7      8
                // http://tf.nist.gov/service/its.htm
                // See "Daytime Protocol (RFC-867)"

                string[] resultTokens = daytimeString.Split (SeparatorArray, StringSplitOptions.RemoveEmptyEntries);
                if (resultTokens[7] != "UTC(NIST)" || resultTokens[8] != "*") {
                    throw new ApplicationException (string.Format ("Invalid RFC-867 daytime protocol string: '{0}'", daytimeString));
                }

                int mjd = int.Parse (resultTokens[0]);  // "JJJJ is the Modified Julian Date (MJD). The MJD has a starting point of midnight on November 17, 1858."
                DateTime now = new DateTime (1858, 11, 17);
                now = now.AddDays (mjd);

                string[] timeTokens = resultTokens[2].Split (':');
                int hours = int.Parse (timeTokens[0]);
                int minutes = int.Parse (timeTokens[1]);
                int seconds = int.Parse (timeTokens[2]);
                double millis = double.Parse (resultTokens[6], EnglishUSACulture);     // this is speculative: official documentation seems out of date!

                now = now.AddHours (hours);
                now = now.AddMinutes (minutes);
                now = now.AddSeconds (seconds);
                now = now.AddMilliseconds (-millis);

                return now;
            }

            public DateTime GetCorrectedSystemLocalTime ()
            {
                TimeSpan diff = MeasureSystemClockError ();
                DateTime corrected = DateTime.Now + diff;
                return corrected;
            }

            public TimeSpan MeasureSystemClockError ()
            {
                TimeSpan roundTrip;
                string daytimeString = QueryDaytimeString (out roundTrip);
                double halfTripMilliseconds = roundTrip.TotalMilliseconds / 2.0;
                TimeSpan halfTrip = new TimeSpan (0, 0, 0, 0, (int)halfTripMilliseconds);

                DateTime nist = NistClock.ParseDaytimeProtocol (daytimeString) + halfTrip;
                DateTime here = DateTime.Now.ToUniversalTime ();
                TimeSpan diff = (nist - here);

                return diff;
            }

            public TimeSpan SynchronizeLocalClock ()
            {
                TimeSpan diff = MeasureSystemClockError ();
                DateTime corrected = DateTime.Now + diff;
                NistClock.SetTimeLocal (corrected);
                return diff;
            }

            // Code snippet for setting system time found at:
            // http://www.codeguru.com/forum/archive/index.php/t-246724.html

            [StructLayout (LayoutKind.Sequential)]
            public struct SYSTEMTIME
            {
                public short wYear;
                public short wMonth;
                public short wDayOfWeek;
                public short wDay;
                public short wHour;
                public short wMinute;
                public short wSecond;
                public short wMilliseconds;
            }

            [DllImport ("kernel32.dll", SetLastError = true)]
            public static extern bool SetSystemTime ([In] ref SYSTEMTIME st);   // http://msdn.microsoft.com/en-us/library/ms724942(VS.85).aspx

            public static void SetTimeLocal (DateTime local)
            {
                SetTimeUtc (local.ToUniversalTime ());
            }

            public static void SetTimeUtc (DateTime utc)
            {
                SYSTEMTIME st = new SYSTEMTIME ();  // http://msdn.microsoft.com/en-us/library/ms724950(VS.85).aspx
                st.wYear = (short)utc.Year;
                st.wMonth = (short)utc.Month;
                st.wDayOfWeek = 0;     // Microsoft documentation (see URL above) says: "The wDayOfWeek member of the SYSTEMTIME structure is ignored."
                st.wDay = (short)utc.Day;
                st.wHour = (short)utc.Hour;
                st.wMinute = (short)utc.Minute;
                st.wSecond = (short)utc.Second;
                st.wMilliseconds = (short)utc.Millisecond;

                if (!SetSystemTime (ref st)) {
                    throw new ApplicationException ("Error setting time.");
                }
            }

            private IPAddress timeServerIpAddress;
            private byte[] buffer = new byte[256];
            private bool printDiagnosticMessages = false;
        }
    }

    */
}
