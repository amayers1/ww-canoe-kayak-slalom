package com.tcay.util;

/**
 * SlalomScoring
 * Teton Cay Group Inc. 2013
 * <p/>
 * User: allen
 * Date: 11/7/13
 * Time: 3:15 PM
 */
public class DuplicateBibException extends Exception {
    public String getMessage() {
        return("A duplicate bib number cannot be used in the same class");
    }
}
