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

package com.tcay.slalom.UI.tables;


import com.tcay.slalom.RaceRun;
import com.tcay.slalom.UI.SlalomApp;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;


public class    ResultsTableCellRenderer extends DefaultTableCellRenderer
{


    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
//fixme
        setOpaque(false);

		Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (row%2 == 0) {
            cell.setBackground( Color.white );
        }
        else {
            cell.setBackground( SlalomApp.LIGHT_CYAN ); ///barely Cyan
        }

        try {

            // Right justify right justification
            if ( column ==  ResultsTable.COL_RAW1 || column == ResultsTable.COL_RAW2 ||
                 column == ResultsTable.COL_PEN1 || column == ResultsTable.COL_PEN2 ||
                 column == ResultsTable.COL_BESTRUN_TOTAL   ) {

                cell.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                if (value.getClass() == Class.forName("java.lang.String") ) {
                    if ( value == RaceRun.DNS || value == RaceRun.DNF    ) {
                        cell.setBackground(SlalomApp.LIGHT_RED);
                        //cell.setFont();Foreground(Color.WHITE);    /// light red


                    }
                }
            }
            else
            {
                cell.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            }

            if (column == ResultsTable.COL_TIMIMGMODE1 || column == ResultsTable.COL_TIMIMGMODE2) {
//fix me - research & eliminate this crap
                JLabel c = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); // params from above );
                  //      // This...
                  //      String pathValue = <getYourPathValue>; // Could be value.toString()
                c.setToolTipText("Timing Mode - *=TagHeuer m=manual");//setToolTip(pathValue);


                if (value.getClass() == Class.forName("java.lang.String") ) {
                    if ( ((String)value).compareTo(ResultsTable.TIMINGMODE_MANUAL) == 0 ) {
                        cell.setBackground(SlalomApp.LIGHT_RED);    /// light red
                    }
                    else if (((String)value).compareTo(ResultsTable.TIMINGMODE_AUTOMATIC) == 0 )  {
                           cell.setBackground(SlalomApp.LIGHT_GREEN);    /// light red
                    }
                }
            }
        }
        catch (Exception e) {

        }



        try {
            if (column == ResultsTable.COL_TIMINGMODE_BEST) {
/*
                if ( ((String)value).compareTo(ResultsTable.TIMINGMODE_MANUAL_LONG) == 0 ) {
                    cell.setBackground(SlalomApp.LIGHT_RED);    /// light red
                }
                else if (((String)value).compareTo(ResultsTable.TIMINGMODE_AUTOMATIC_LONG) == 0 )  {
                    cell.setBackground(SlalomApp.LIGHT_GREEN);    /// light red
                }
                if (value.getClass() == Class.forName("javax.swing.ImageIcon") )         {
                    cell.setBackground( SlalomApp.LIGHT_GREEN );
                }
                else {
                    cell.setBackground( SlalomApp.LIGHT_RED );
                }

*/
            }
        }
        catch (Exception e) {

        }

        // Penalties < 50 == yellow background > 50 = red
        if (column == ResultsTable.COL_PEN1 || column == ResultsTable.COL_PEN2) {
            if( value instanceof Integer )
            {
                Integer amount = (Integer) value;
                if( amount.intValue() > 0 )
                {
                    if  (amount.intValue() >= 50)
                        cell.setBackground( SlalomApp.LIGHT_RED );
                    else cell.setBackground(SlalomApp.LIGHT_YELLOW);//Color.yellow );
                }
            }
        }

		return cell;
	}
}
