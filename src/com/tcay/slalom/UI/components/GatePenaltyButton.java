package com.tcay.slalom.UI.components;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 10/15/13
 * Time: 3:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class GatePenaltyButton extends JButton {
    static final Font f = new Font("Dialog", Font.PLAIN, 16);

    private int penalty;
    private int gate;



    public int getGate() {
        return gate;
    }


    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        String penaltyText = "";
        this.penalty = penalty;
        switch (penalty) {
            case 0:
                break;
            case 2:
                penaltyText = "+2";
                break;
            case 50:
                penaltyText = "+50";
                break;

            default:
                break;
        }

        setText( gate + " " + penaltyText);
    }


    public GatePenaltyButton(int gate, String label, ImageIcon icon) {
        super(label, icon);
        this.gate = gate;
        penalty = 0;
        setFont(f);
    }

}
