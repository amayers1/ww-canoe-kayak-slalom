package com.tcay.slalom.UI.components;

import com.tcay.slalom.Race;

import javax.swing.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * ICF_50_ReasonButton - Reasons for 50 second penalty assessment from ICF Gate Judging Form
 *
 * SlalomScoring
 * Teton Cay Group Inc. 2013
 * <p/>
 * User: allen
 * Date: 11/7/13
 * Time: 8:43 PM
 */



public class ICF_50_ReasonButton extends JButton {
    String    reasonCode;

    public String getReasonCode() {
        return reasonCode;
    }

    public ICF_50_ReasonButton(String text, Icon icon, String reasonCode) {
         super(text, icon);
         this.reasonCode = reasonCode;
    }

    public ICF_50_ReasonButton(Icon icon, String reasonCode) {
        super(icon);
        this.reasonCode = reasonCode;
    }
}
