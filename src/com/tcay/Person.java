package com.tcay;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Teton Cay Group, Inc.
 * User: allen
 * Date: Aug 21, 2013
 * Time: 5:04:22 PM
 */
public class Person implements Serializable {

    private String firstName = null;
    private String lastName = null;
    private Calendar birthday = null;
    private String sex = null;
    private PhysicalAddress streetAddress = null;
    private String emailAddress;
    private String mobilePhone;

    public Calendar getBirthday() {
        return birthday;
    }

    public void setBirthday(Calendar birthday) {
        this.birthday = birthday;
    }

    public PhysicalAddress getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(PhysicalAddress streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSex() {
        return sex;
    }
}

