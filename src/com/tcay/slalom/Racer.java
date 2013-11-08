package com.tcay.slalom;

import com.tcay.Person;
import com.tcay.util.Log;

import java.io.Serializable;

/**
 * Teton Cay Group, Inc.
 * User: allen
 * Date: Aug 21, 2013
 * Time: 5:03:47 PM
 */


/**
 * Racer is the class representing the person (or persons in C2) that will paddle in the race
 */
public class Racer extends Person implements Serializable {

    private String federationNumber;        // Racers Federation membership Identifier ICF/USACK/etc.
    private String tShirtSize;
    private String club;
    private String bibNumber;
    private String mobilePhone;



    public String getFederationNumber() {
        return federationNumber;
    }

    public void setFederationNumber(String federationNumber) {
        this.federationNumber = federationNumber;
    }

    public String gettShirtSize() {
        return tShirtSize;
    }

    public void settShirtSize(String tShirtSize) {
        this.tShirtSize = tShirtSize;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }


    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    private String ageGroup;


    public String getBibNumber() {
        return bibNumber;
    }

    public void setBibNumber(String bibNumber) {
        this.bibNumber = bibNumber;
    }


    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }



    public Racer(String first, String last, String sex, String club) {
        setFirstName(first);
        setLastName(last);
        setSex(sex);
        setClub(club);
    }

    public String toString() {
        return(getLastName() + ", " + getFirstName());
    }

    private void init() {
        tShirtSize = "";

    }
    public Racer(String bib, String last, String first, String sex, String club) {
        init();
        bibNumber = bib;
        setFirstName(first);
        setLastName(last);
        setSex(sex);
        setClub(club);
    }

    public Racer(String bib, String last, String first, String sex, String club, String ageGroup) {
        init();
        bibNumber = bib;
        setFirstName(first);
        setLastName(last);
        setSex(sex);
        setClub(club);
        setAgeGroup(ageGroup);
    }

    @Override
    public void setSex(String sex) {
        String setTo = "";
        if ( sex.matches("F") || sex.matches("W"))
            setTo = "F";

        super.setSex(setTo);
    };

    @Override
    public String getSex() {
        String sex = super.getSex();
        if ( sex.matches("F") )
            sex = "W";
        return(sex);

    }

    public String getShortName() {
        String firstInitial = "";
        try {
          firstInitial = getFirstName().substring(0,1);
        }

        catch (Exception e) {
            Log.getInstance().write(e);
        }
        return(firstInitial + ". " + getLastName() );
    }

}
