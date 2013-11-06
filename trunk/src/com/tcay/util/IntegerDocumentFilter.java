package com.tcay.util;

import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

/**
 * SlalomScoring
 * Teton Cay Group Inc. ©2013
 * <p/>
 * User: allen
 * Date: 11/4/13
 * Time: 11:07 PM
 */
public class IntegerDocumentFilter extends DocumentFilter {
    int min = Integer.MIN_VALUE;
    int max = Integer.MAX_VALUE;
    public IntegerDocumentFilter(int min, int max) {
        super();
        this.min = min;
        this.max = max;
    }

    public IntegerDocumentFilter() {
        super();
    }


    private boolean onlyDigits(String string) {
        int len = string.length();
        boolean isValidInteger = true;

        for (int i = 0; i < len; i++)
        {
            if (!Character.isDigit(string.charAt(i)))
            {
                isValidInteger = false;
                break;
            }
        }
        return isValidInteger;
    }

    private boolean withinRange(DocumentFilter.FilterBypass fp, String string) {

        String current = "";
        Document d = fp.getDocument();
        int i = d.getLength();
        try {
           current = d.getText(0,i);
        } catch (Exception e) {
           System.out.println(e);
        }
        current += string;

        boolean isInRange = true;
        int ourValue = new Integer(current);// Integer.getInteger(string);
        if ( ourValue > max ||  ourValue < min ) {
            isInRange = false;
        }


        return( isInRange);
    }



    @Override
    public void insertString(DocumentFilter.FilterBypass fp
            , int offset, String string, AttributeSet aset)
            throws BadLocationException
    {
        boolean isValidInteger = onlyDigits(string);
        if (isValidInteger) {
            isValidInteger = withinRange(fp, string);     // A131105 (ajm)
        }

        if (isValidInteger)
            super.insertString(fp, offset, string, aset);
        else
            Toolkit.getDefaultToolkit().beep();
    }

    @Override
    public void replace(DocumentFilter.FilterBypass fp, int offset
            , int length, String string, AttributeSet aset)
            throws BadLocationException
    {
        boolean isValidInteger = onlyDigits(string);
        if (isValidInteger) {
            isValidInteger = withinRange(fp, string);     // A131105 (ajm)
        }

        if (isValidInteger)
            super.replace(fp, offset, length, string, aset);
        else
            Toolkit.getDefaultToolkit().beep();
    }
}
