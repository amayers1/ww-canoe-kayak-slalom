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

package com.tcay.slalom.UI;

import javax.swing.*;
import java.awt.*;

/**
 * Created by allen on 4/16/16.
 */
public class SlalomDialogUI {


     public static boolean confirmStart(Frame frame, String bibNumber) {
        boolean answer = false;

        Object[] options = {"Good START",
                "FALSE START", "Wrong BIB !"};
        int n = JOptionPane.showOptionDialog(frame,
                "Confirm that BIB# " + bibNumber + "just started",
                "Confirm START Eye",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

         if (n==0) {
             answer = true;
         }
         return answer;

    }


    public static boolean confirmFinish(Frame frame, String bibNumber) {
        boolean answer = false;

        Object[] options = {"Good FINISH",
                "FALSE FINISH","Wrong BIB !"};
        int n = JOptionPane.showOptionDialog(frame,
                "Confirm that BIB# " + bibNumber + "just finished",
                "Confirm FINISH Eye",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (n==0) {
            answer = true;
        }
        return answer;

    }

}
