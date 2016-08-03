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

package com.tcay.timing;

/**
 * Teton Cay Group, Inc.
 * User: allen
 * Date: Aug 28, 2013
 * Time: 10:18:30 AM
 */


import java.io.Serializable;
import java.text.DecimalFormat;

public class StopWatch implements Serializable
{
//    private static final DecimalFormat secondsFormatter = new DecimalFormat("0");
//    private static final DecimalFormat timeFormatter = new DecimalFormat("00");
    private long startTime;
    private String name;

    public long getEndTime() {
        return endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    private long endTime;


    static private long nextId=1;

    public long getId() {
        return id;
    }

    private long id;



    {
        id = nextId++;
    }

    public StopWatch() {
        //endTime =startTime = 0;   /// 20150521 (ajm)
//        System.out.println("NEW STOPWATCH " + id);

    }


    public StopWatch(String name) {
        this.name = name;
//        System.out.println("NEW STOPWATCH " + name + " " + id);
    }

    public boolean isStopped() {
        return stopped;
    }

    boolean stopped;// = false;

    public static void main(String[] args)
    {
    }


    public void setPassedInStartTime(Long startMillis) {
        stopped = false;//todo NRC document
        if (endTime==startTime && !stopped) {  // todo fix kludge
            startTime = startMillis;
            System.out.println("setPassedInStartTime @5 id=" + id + " Hash=" +System.identityHashCode(this));
        }

    }

    public void setPassedInStopTime(Long stopMillis) {               //todo NRC document
    // todo add protection    if (endTime==startTime && stopped==false) {  // todo fix kludge
            endTime = stopMillis;
        stopped = true;
        id +=1000;
        System.out.println("setPassedInStopTime @6 id="+ id+ " Hash=" +System.identityHashCode(this));
   //     }

    }




    public void start()
    {
        stopped =false;
        startTime = System.currentTimeMillis();
        endTime = startTime;
    }

    public void stop() {
  //      stopLap();
        endTime = System.currentTimeMillis();
        stopped = true;
    }



//    public void stopLap()
//    {   if (!stopped)
//           endTime = System.currentTimeMillis();
//    }

    static final long DNF = 999999;    //todo eliminate


//    public void setDNF() {
//        endTime = startTime + DNF;
//        stopped = true;
//    }



    public float getElapsed()
    {
        Long diff = new Long(System.currentTimeMillis() - startTime);
        double elapsed;

//        System.out.println("@GetElapsed id="+ id + "  isStopped=" + stopped);

        if (stopped)
            diff = new Long(endTime - startTime);
        //diff = (endTime == startTime ? System.currentTimeMillis():endTime) - startTime;


        long remainder = diff.longValue()%3600000;        // strip off hours
        long sec = remainder/1000;
        remainder = remainder%1000;
        long hundreths =  remainder/10;

        elapsed = (double)sec + (double)hundreths/100.0;

        return (float)elapsed;
    }
}

