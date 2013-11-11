package com.tcay.slalom.UI.tables;


import com.tcay.slalom.Race;
import com.tcay.slalom.UI.SlalomApp;
import com.tcay.util.Log;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;


public class RunScoringTableCellRenderer extends DefaultTableCellRenderer
{

    Log log;
    {
       log = Log.getInstance();
    }


	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (row%2 == 0) {
            cell.setBackground(Color.white);
        }
        else {
            cell.setBackground( SlalomApp.LIGHT_CYAN );
        }

        try {

            cell.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            // Right justify right justification
            if (column<RunScoringTable.COL_TOTAL+1 || column > RunScoringTable.COL_TOTAL)
            {
                if ( column ==  RunScoringTable.COL_RAW ||
                     column == RunScoringTable.COL_TOTAL ||
                     column== RunScoringTable.COL_RUN_NBR ||
                     column > RunScoringTable.COL_TOTAL   ) {

                     cell.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                }
            }

        }
        catch (Exception e) {
            log.write(e);

        }


        // Penalties < 50 == yellow background > 50 = red
        setIcon(null);                                  // had to clear some previous icon???  todo investigate and make proper comment here
        if (column > RunScoringTable.COL_TOTAL) {

            if (row%2 == 1) {
                if (Race.getInstance().isUpstream(column - RunScoringTable.COL_TOTAL)) {
                    cell.setBackground( SlalomApp.ULTRA_LIGHT_CYAN);
                }
            }

            if( value instanceof String )
            {

                if (((String)value).trim().isEmpty() == false
                    && ((String)value).compareTo("--")!= 0  && ((String)value).compareTo("-")!= 0) {
                    Integer amount =  new Integer(   ((String)value).trim()   );
                    if( amount > 0 )
                    {
                        if  (amount >= 50)
                            cell.setBackground( SlalomApp.LIGHT_RED );
                        else cell.setBackground(SlalomApp.LIGHT_YELLOW);
                    }
                    else if (row%2 == 1) {
                        cell.setBackground( SlalomApp.LIGHT_CYAN ); ///barely Cyan
                    }
                }
            }
        }

		return cell;
	}
}
