package com.tcay.timing;

/**
 * Teton Cay Group, Inc.
 * User: allen
 * Date: Aug 28, 2013
 * Time: 10:18:30 AM
 */


import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Title:        StopWatch
 * Copyright:    Copyright (c) 2005
 * Company:      EXIT41
 * @author       Michelle Welden
 *
 * Description:  Calculates elapsed time between start and
 *               finish.
 *
 */
public class StopWatch implements Serializable
{
//    private static final DecimalFormat secondsFormatter = new DecimalFormat("0");
//    private static final DecimalFormat timeFormatter = new DecimalFormat("00");
    private long startTime;
    private long endTime;

    public boolean isStopped() {
        return stopped;
    }

    boolean stopped = false;

    public static void main(String[] args)
    {
    }

    public void start()
    {
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

