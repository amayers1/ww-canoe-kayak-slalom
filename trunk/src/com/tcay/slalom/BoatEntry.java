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
