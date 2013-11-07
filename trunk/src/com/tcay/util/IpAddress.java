package com.tcay.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 10/28/13
 * Time: 6:38 PM
 * To change this template use File | Settings | File Templates.
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
                while(addresses.hasMoreElements()) {
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
