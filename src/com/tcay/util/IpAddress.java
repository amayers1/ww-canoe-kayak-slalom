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

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * SlalomApp
 *
 * Teton Cay Group Inc. 2013
 *

 * User: allen
 * Date: 10/28/13
 * Time: 6:38 PM
 *
 */
public class IpAddress {
    public String getIp4(){
        String ip;
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // filters out 127.0.0.1 and inactive interfaces

                if (iface.isLoopback() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                boolean firstTime = true;
                while(addresses.hasMoreElements()) {
                    if (firstTime) {
                        firstTime = false;
                        Log.getInstance().info("Checking net addresses");
                    }
                    InetAddress addr = addresses.nextElement();
                    ip = addr.getHostAddress();
                    Log.getInstance().info(iface.getDisplayName() + " " + ip);

                    // 169.xxx.xxx.xxx is self assigned, do not use
                    if (!ip.contains(":") && ip.substring(0,3).compareTo("169")!=0) {
                        return(ip);
                    }
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        return(null);
    }
}
