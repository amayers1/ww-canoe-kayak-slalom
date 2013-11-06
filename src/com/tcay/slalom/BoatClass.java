package com.tcay.slalom;

import java.util.ArrayList;

/**
 * Teton Cay Group, Inc.
 * User: allen
 * Date: Aug 21, 2013
 * Time: 5:11:41 PM
 */
public class BoatClass {
    private String className;             // K1, C1, C2, K1 Rec, C1 Rec, K2 Rec  sex of race(ers) will determine W or M
    //private ArrayList racerList;


     public BoatClass(String boatClass) {
         className = boatClass;
     }


     public String toString() {
         return(className);
     }

}
