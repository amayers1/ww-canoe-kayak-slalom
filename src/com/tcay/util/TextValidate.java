package com.tcay.util;

import javax.swing.*;
import javax.swing.text.AbstractDocument;

/**
 * SlalomScoring
 * Teton Cay Group Inc. 2013
 * <p/>
 * User: allen
 * Date: 11/4/13
 * Time: 11:15 PM
 */
public class TextValidate {

    static public void digitsOnly(JTextField text, int min, int max) {
        ((AbstractDocument) text.getDocument()).setDocumentFilter(new IntegerDocumentFilter(min,max));
    }


    static public void digitsOnly(JTextField text) {
        ((AbstractDocument) text.getDocument()).setDocumentFilter(new IntegerDocumentFilter());
    }
}
