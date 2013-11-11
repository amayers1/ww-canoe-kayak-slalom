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

package com.tcay.slalom.UI;

import java.io.Serializable;

/**
 * ${PROJECT_NAME}
 *
 * Teton Cay Group Inc. ${YEAR}
 *

 * User: allen
 * Date: 8/31/13
 * Time: 10:37 PM
 *
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
