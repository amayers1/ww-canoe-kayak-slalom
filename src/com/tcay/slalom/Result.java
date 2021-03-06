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

import java.util.ArrayList;
import java.util.Collections;

/**
 * ${PROJECT_NAME}
 *
 * Teton Cay Group Inc. ${YEAR}
 *

 * User: allen
 * Date: 9/26/13
 * Time: 5:05 PM
 *
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


        double i = 0;
        double j = 0;
        try {
            //todo 20151122 Null pointer exception here - 2015 Turkey Trot Day 2
            i = res.getBestRun().getElapsed()*100.0 + res.getBestRun().getTotalPenalties()*100.0;

            j = getBestRun().getElapsed()*100.0 + getBestRun().getTotalPenalties()*100.0;
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


//        int x = (int)(j-i);
        return k + (int)(j-i);
    }

}
