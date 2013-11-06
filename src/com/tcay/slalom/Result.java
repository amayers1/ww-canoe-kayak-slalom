package com.tcay.slalom;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 9/26/13
 * Time: 5:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class Result implements Comparable {
    public Racer getRacer() {
        return racer;
    }

    public void setRacer(Racer racer) {
        this.racer = racer;
    }

    public RaceRun getRun1() {
        return run1;
    }

    public void setRun1(RaceRun run1) {
        this.run1 = run1;
    }

    public RaceRun getRun2() {
        return run2;
    }

    public void setRun2(RaceRun run2) {
        this.run2 = run2;
    }

    public RaceRun getBestRun() {
        return bestRun;
    }

    public void setBestRun(RaceRun bestRun) {
        this.bestRun = bestRun;
    }

    Racer racer;
    RaceRun run1;
    RaceRun run2;
    RaceRun bestRun;
    BoatEntry boat;

    public BoatEntry getBoat() {
        return boat;
    }

    public void setBoat(BoatEntry boat) {
        this.boat = boat;
    }


    static public ArrayList<Result> getResultsByClassTime(ArrayList<Result> results) {
        ArrayList<Result> sorted = new ArrayList(results);

        Collections.sort(sorted);
        return sorted;
    }


    @Override
    public int compareTo(Object o) {

        Result res = (Result) o;

        int k = (getBoat().getBoatClass().compareTo(res.getBoat().getBoatClass())) * 100000;


        double i = res.getBestRun().getElapsed()*100.0 + res.getBestRun().getTotalPenalties()*100.0 ;

        double j = getBestRun().getElapsed()*100.0 + getBestRun().getTotalPenalties()*100.0 ;


//        int x = (int)(j-i);
        return k + (int)(j-i);
    }

}
