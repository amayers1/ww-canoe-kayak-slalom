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

import com.tcay.slalom.UI.components.GateLabelSmall;
import com.tcay.slalom.UI.components.TableHeaderRenderer;
import com.tcay.util.Log;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.JLabel;

/**
 * ${PROJECT_NAME}
 *
 * Teton Cay Group Inc. ${YEAR}
 *

 * User: allen
 * Date: 10/17/13
 * Time: 8:41 PM
 *
 */
public class RunScoringTable {                                    // TODO customize for Runs and not Results

    // These are 0 Origin indices into the columns
    static public final int COL_BOATCLASS=0;
    static public final int COL_BIB=1;
    static public final int COL_RACERNAME=2;
    static public final int COL_RUN_NBR=3;
    static public final int COL_RAW=4;
    static public final int COL_TOTAL=5;

    public JTable createTable() {
        RunScoringTableModel model = RunScoringTableModel.getInstance();

        JTable table = new JTable( model );
        JTableHeader header = table.getTableHeader();

        TableHeaderRenderer thRenderer = new TableHeaderRenderer(table, JLabel.CENTER);
        header.setDefaultRenderer(thRenderer);

        TableCellRenderer renderer = new RunScoringTableCellRenderer();

        TableColumn column;
        for (int i = 0; i < table.getColumnCount(); i++) {
            column = table.getColumnModel().getColumn(i);
            switch(i) {
                case COL_BOATCLASS:
                    column.setMinWidth(30);
                    column.setPreferredWidth(40);
                    column.setMaxWidth(50);

                    break;
                case COL_RUN_NBR:
                    column.setPreferredWidth(30);
                    column.setMinWidth(20);
                    column.setMaxWidth(30);
                    break;
                case COL_RACERNAME:
                    column.setPreferredWidth(140);
                    column.setMinWidth(80);
                    break;
                case COL_RAW:
                     column.setMinWidth(20);
                     column.setMaxWidth(80);
                     column.setPreferredWidth(80);
                     /// missing break intentional ... we want to fall through to COL_TOTAL !
                case COL_TOTAL:
                    column.setMinWidth(80);
                    column.setMaxWidth(100);
                    column.setPreferredWidth(80);
                    break;
                case COL_BIB:
                    column.setPreferredWidth(30);
                    column.setMinWidth(20);
                    column.setMaxWidth(40);

                    break;
                default:
                    // Set column headers to display to display gate labels
                    TableColumn tc = table.getColumnModel().getColumn(i);
                    tc.setHeaderRenderer(new GateLabelSmall());

                    column.setPreferredWidth(25);
                    column.setMinWidth(20);
                    column.setMaxWidth(30);
                    break;
            }
        }

        try
        {
//D131110(ajm)            table.setDefaultRenderer( Class.forName( "javax.swing.JLabel" ), thRenderer );

            table.setDefaultRenderer( Class.forName( "java.lang.Integer" ), renderer );
            table.setDefaultRenderer( Class.forName( "java.lang.String" ), renderer );
            table.setDefaultRenderer( Class.forName( "java.lang.Float" ), renderer );
            table.setDefaultRenderer( Class.forName( "java.lang.Double" ), renderer );
            table.setDefaultRenderer( Class.forName( "javax.swing.Icon" ), renderer );
        }
        catch( ClassNotFoundException ex )
        {
            Log.getInstance().fatal("RunScoringTable ClassNotFoundException");
            System.exit( 0 );
        }
        return(table);
    }
}
