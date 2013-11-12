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

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;

/**
 * SlalomScoring
 * Teton Cay Group Inc. 2013
 * <p/>
 * User: allen
 * Date: 11/10/13
 * Time: 8:19 AM
 */
public class ResultsTableSpectator extends ResultsTable {
    static final Font f = new Font("Dialog", Font.PLAIN, 20);
    // http://stackoverflow.com/questions/10245305/how-to-add-image-background-on-jtable-that-does-not-scroll-when-scrolling-jtab
    JTable table;

    public JTable createTable() {
        table = super.createTable();

        table.setFont(f);
        table.setIntercellSpacing(new Dimension(5,5));
        table.setRowHeight(30);

        // MUST remove columns from the end of the table, otherwise Column Numbers are not correct
        // as prior culumns have been deleted

        return(table);
    }


    public/*for now todo*/  void removeDetailColumns()  {



        table.removeColumn(table.getColumnModel().getColumn(COL_TIMINGMODE_BEST));

        table.removeColumn(table.getColumnModel().getColumn(COL_PEN2));
        table.removeColumn(table.getColumnModel().getColumn(COL_TIMIMGMODE2));
        table.removeColumn(table.getColumnModel().getColumn(COL_RAW2));

        table.removeColumn(table.getColumnModel().getColumn(COL_PEN1));
        table.removeColumn(table.getColumnModel().getColumn(COL_TIMIMGMODE1));
        table.removeColumn(table.getColumnModel().getColumn(COL_RAW1));

        // Need to do this by column name as we have deleted columns and can't reliably do it by position !
        for (int i=0; i<table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);

            System.out.println("Column " + i + "= " +table.getColumnName(i));
            if (table.getColumnName(i).compareTo(ResultsTable.COL_BOATCLASS_NAME) == 0 ) {
                column.setPreferredWidth(50);
                column.setMaxWidth(50);
                column.setMinWidth(30);
            }
            if (table.getColumnName(i).compareTo(ResultsTable.COL_POSITION_NAME) == 0 ) {
                column.setPreferredWidth(50);
                column.setMaxWidth(50);
                column.setMinWidth(20);
            }

            if (table.getColumnName(i).compareTo(ResultsTable.COL_BIB_NAME) == 0 ) {
                column.setPreferredWidth(50);
                column.setMaxWidth(50);
                column.setMinWidth(20);
            }
            if (table.getColumnName(i).compareTo(ResultsTable.COL_CLUB_OR_COUNTRY_NAME) == 0 ) {
                column.setPreferredWidth(25);
                column.setMaxWidth(25);
                column.setMinWidth(25);
            }
            if (table.getColumnName(i).compareTo(ResultsTable.COL_RACERNAME_NAME) == 0 ) {
                column.setPreferredWidth(150);
                column.setMaxWidth(200);
                column.setMinWidth(50);
            }
            if (table.getColumnName(i).compareTo(ResultsTable.COL_BESTRUN_TOTAL_NAME) == 0 ) {
                column.setMinWidth(50);
                column.setPreferredWidth(50);
                column.setMaxWidth(100);
            }
        }
    }
}
