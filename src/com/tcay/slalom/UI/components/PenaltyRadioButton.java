package com.tcay.slalom.UI.components;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 8/31/13
 * Time: 9:14 PM
 * To change this template use File | Settings | File Templates.
 */

/*
 * Convenience class to make code more readable.  We can store the penalty seconds and the gate number
 * in the radio button.
 */
public class PenaltyRadioButton extends JRadioButton {

    public int getPenaltySeconds() {
        return penaltySeconds;
    }

    public int getGateNbr() {

        return gateNbr;
    }

    int gateNbr;
    int penaltySeconds;


    private void init (int gateNbr, int penaltySeconds) {
        this.gateNbr = gateNbr;
        this.penaltySeconds = penaltySeconds;

    }


    public PenaltyRadioButton(String text, int gateNbr, int penaltySeconds) {
        super(text);
        init (gateNbr, penaltySeconds);
    }


//    public PenaltyRadioButton(int gateNbr, int penaltySeconds) {
//        super();
//        init (gateNbr, penaltySeconds);
//    }

}
