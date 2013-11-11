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
    }
}
