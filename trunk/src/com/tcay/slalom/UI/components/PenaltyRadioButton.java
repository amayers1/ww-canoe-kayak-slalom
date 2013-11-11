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
