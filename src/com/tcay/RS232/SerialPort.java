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

import j.extensions.comm.SerialComm;

import java.io.InputStream;

/**
 * Handle the automatic port detection for attached timing equipment on serial port.
 *
 */
public class SerialPort {
        SerialComm port;
        boolean canOpen;

        SerialComm getPort() {
            return port;
        }

        void setCanOpen(boolean canOpen) {
            this.canOpen = canOpen;
        }

        void setHadException(boolean hadException) {
            this.hadException = hadException;
        }

        boolean hadException;
        InputStream inputStream;


    public SerialPort(SerialComm port) {
            this.port = port;

            hadException = false;
            canOpen = false;
            inputStream = null;

        }

    public String getName() {
        return port.getDescriptivePortName();
    }

    public String getSystemName() {
        return port.getSystemPortName();
    }
}