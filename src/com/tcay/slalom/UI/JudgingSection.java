package com.tcay.slalom.UI;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 8/31/13
 * Time: 10:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class JudgingSection implements Serializable {
    private int firstGate;    // 1st gate in the section
    private int lastGate;     // last gate in the section
    private Integer sectionNbr;

    private transient Boolean clientDeviceAttached;

    public Integer getSectionNbr() {
        return sectionNbr;
    }

    public void setSectionNbr(Integer sectionNbr) {
        this.sectionNbr = sectionNbr;
    }


    public JudgingSection() {
    }


    public JudgingSection(Integer sectionNbr,int start, int end) {
        this.sectionNbr = sectionNbr;
        clientDeviceAttached = false;
        firstGate = start;
        lastGate = end;
    }

    public int getFirstGate() {
        return firstGate;
    }

    public int getLastGate() {
        return lastGate;
    }

    public String toString() {
       return(sectionNbr.toString());
    }

    public Boolean getClientDeviceAttached() {
        return clientDeviceAttached;
    }

    public void setClientDeviceAttached(Boolean clientDeviceAttached) {
        this.clientDeviceAttached = clientDeviceAttached;
    }
}
