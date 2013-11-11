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

package com.tcay.slalom;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Teton Cay Group, Inc.
 * User: allen
 * Date: Aug 21, 2013
 * Time: 5:11:06 PM
 */
public class BoatEntry implements Serializable{
    //private BoatClass boatClass = null;
    private String boatClass;
    private ArrayList<Racer> racerList = null;   // List of racers for this BoatEntry
    private ArrayList<RaceRun> runList = null;


//    public BoatEntry(Racer racer, BoatClass bc)
//    {
//            racerList = new ArrayList<Racer>();           // Could be 2 Racers (e.g. C2)
//            racerList.add(racer);
//            boatClass = bc;
//    }

    public BoatEntry(Racer racer, String bc)
    {
        racerList = new ArrayList<Racer>();           // Could be 2 Racers (e.g. C2)
        racerList.add(racer);

        // todo determine boat class
        boatClass = bc;
    }




    public RaceRun onDeck(int runNumber) { /// for Reruns

        if (runList == null) {
            runList = new ArrayList<RaceRun>();
        }

        if (runList.size() > 2) {  // OK record and find out later if this was a rerun
            // todo exception ???
        }
        RaceRun thisRun = new RaceRun(this, runNumber);
        runList.add(thisRun);

        return(thisRun);
    }

    // called to setup a BoatEntry to begin a run
    public RaceRun onDeck() {
        RaceRun thisRun = null;

        if (runList == null) {
            runList = new ArrayList<RaceRun>();
        }

        if (runList.size() > 2) {  // OK record and find out later if this was a rerun
                  // todo exception ???
        }

        thisRun = new RaceRun(this);

        runList.add(thisRun);
        return(thisRun);
    }

    //todo Cna USE ???
    public RaceRun didDNS() {
        RaceRun thisRun = null;

        if (runList == null) {
            runList = new ArrayList<RaceRun>();
        }

        if (runList.size() > 2) {  // OK record and find out later if this was a rerun
            // todo exception ???
        }

        thisRun = new RaceRun(this);
        thisRun.dns();

        runList.add(thisRun);
        return(thisRun);
    }




//    public Integer runCount() {
//       return(runList.size());
//    }


    public Racer getRacer() {        /// Modify or add methods for C2
       return(racerList.get(0));
    }


    public String getBoatClass() {
        return(boatClass);/// + getRacer().getSex());
    }


    public String toString() {
        Racer racer = getRacer();
        return(racer.getBibNumber() + " " + racer.getShortName() + " " +getBoatClass() + " (" + racer.getClub() +")" );
    }

    public String racerClassClub() {
        Racer racer = getRacer();
        return(racer.getShortName() + " " +getBoatClass() + " (" + racer.getClub() +")" );
    }

    public ImageIcon getImageIcon() {
        return Race.getInstance().getImageIcon(getRacer().getClub());
    }
    public ImageIcon getImageIconTiny() {
        return Race.getInstance().getImageIconTiny(getRacer().getClub());
    }


}
