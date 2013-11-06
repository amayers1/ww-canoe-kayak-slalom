package com.tcay.RS232;

import j.extensions.comm.SerialComm;

import java.io.InputStream;

public class CandidateCP520Port {
        SerialComm port;
        boolean canOpen;

        SerialComm getPort() {
            return port;
        }

        void setPort(SerialComm port) {
            this.port = port;
        }

        boolean isCanOpen() {
            return canOpen;
        }

        void setCanOpen(boolean canOpen) {
            this.canOpen = canOpen;
        }

        boolean isHadException() {
            return hadException;
        }

        void setHadException(boolean hadException) {
            this.hadException = hadException;
        }

        InputStream getInputStream() {
            return inputStream;
        }

        void setInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        boolean hadException;
        boolean isCP520;
        InputStream inputStream;

    public void setCP520(boolean CP520) {
        isCP520 = CP520;
    }

    public CandidateCP520Port(SerialComm port) {
            this.port = port;

            hadException = false;
            canOpen = false;
            inputStream = null;
            isCP520 = false;
        }

    public String getName() {
        return port.getDescriptivePortName();
    }

    public String getSystemName() {
        return port.getSystemPortName();
    }

}