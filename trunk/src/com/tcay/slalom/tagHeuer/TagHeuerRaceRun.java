package com.tcay.slalom.tagHeuer;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 10/11/13
 * Time: 6:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class TagHeuerRaceRun {



    public TagHeuerRaceRun(int runNumber) {
      this.runNumber = runNumber;
    }


    private int runNumber;

    public int getRunNumber() {
        return runNumber;
    }


    public String getBibNumber() {
        return bibNumber;
    }

    public void setBibNumber(String bibNumber) {
        this.bibNumber = bibNumber;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public double getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(double elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    private String bibNumber;
    private String startTime;

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    private String stopTime;
    private double elapsedTime;


}
