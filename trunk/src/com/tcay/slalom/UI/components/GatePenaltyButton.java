/*
 * This file is part of SlalomApp.
 *
 *     SlalomApp is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     SlalomApp is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with SlalomApp.  If not, see <http://www.gnu.org/licenses/>.
 */

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
    private String reasonFor50;



    public int getGate() {
        return gate;
    }


    public int getPenalty() {
        return penalty;
    }

    public String getReasonFor50() {
        return reasonFor50;
    }

    public void setReasonFor50(String code) {

        this.reasonFor50 = code;
    }

    public void setPenalty(int penalty) {
        setPenalty( penalty, null);
    }


    public void setPenalty(int penalty, String reasonCode) {
        String penaltyText = "";
        this.reasonFor50 = reasonCode;
        this.penalty = penalty;
        switch (penalty) {
            case 0:
                break;
            case 2:
                penaltyText = "+2";
                break;
            case 50:
                penaltyText = "+50" + (reasonFor50==null?"":"(" + reasonFor50 + ")");
                break;

            default:
                break;
        }

        setText( gate + " " + penaltyText );
    }


    public GatePenaltyButton(int gate, String label, ImageIcon icon) {
        super(label, icon);
        this.gate = gate;
        penalty = 0;
        setFont(f);
    }

}
