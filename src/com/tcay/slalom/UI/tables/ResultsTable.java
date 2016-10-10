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

import com.tcay.Singleton;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 *
 *
 * ${PROJECT_NAME}
 *
 * Teton Cay Group Inc. ${YEAR}
 *

 * User: allen
 * Date: 9/28/13
 * Time: 9:13 PM
 *
 */
public class ResultsTable {// extends Singleton {


    protected boolean isOpaque = false;
    // These are 0 Origin indices into the columns
    static public final int COL_BOATCLASS=0;
    static public final int COL_POSITION=1;
    static public final int COL_BIB=2;

    static public final int COL_CLUB_OR_COUNTRY=3;
    static public final int COL_RACERNAME=4;
    static public final int COL_RAW1=5;
    static public final int COL_TIMIMGMODE1=6;
    static public final int COL_PEN1=7;


    static public final int COL_RAW2=8;
    static public final int COL_TIMIMGMODE2=9;
    static public final int COL_PEN2=10;

    static public final int COL_BESTRUN_TOTAL=11;
    static public final int COL_TIMINGMODE_BEST=12;


    static public final String TIMINGMODE_MANUAL="m";
    static public final String TIMINGMODE_AUTOMATIC="e";
    static public final String TIMINGMODE_ADJUSTED="c";



    static public final String COL_BOATCLASS_NAME="Class";
    static public final String COL_POSITION_NAME="Pos";
    static public final String COL_BIB_NAME="Bib";

    static public final String COL_CLUB_OR_COUNTRY_NAME="Club";
    static public final String COL_RACERNAME_NAME="Name";
    static public final String COL_RAW1_NAME="Raw";
    static public final String COL_TIMIMGMODE1_NAME="tm";
    static public final String COL_PEN1_NAME="Pen";


    static public final String COL_RAW2_NAME="Raw";
    static public final String COL_TIMIMGMODE2_NAME="tm";
    static public final String COL_PEN2_NAME="Pen";

    static public final String COL_BESTRUN_TOTAL_NAME="Best";
    static public final String COL_TIMINGMODE_BEST_NAME="tm";


    ResultsTableModel tableModel;

    public boolean isOpaque() {
        return isOpaque;
    }

    protected void setupColumns (JTable table) {

        TableColumn column = null;
        for (int i = 0; i < table.getColumnCount(); i++) {
            column = table.getColumnModel().getColumn(i);


            if ( i==COL_BOATCLASS || i == COL_RAW1 || i == COL_RAW2  ||
                    i == COL_PEN1  || i == COL_PEN2 || i == COL_BESTRUN_TOTAL  ) {
                column.setPreferredWidth(50);
            }
            else if (i==COL_TIMIMGMODE1 || i==COL_TIMIMGMODE2 || i==COL_TIMINGMODE_BEST) {
                column.setPreferredWidth(22);
                column.setMaxWidth(22);
            } else if (i==COL_CLUB_OR_COUNTRY ) {
                column.setPreferredWidth(25);
                column.setMaxWidth(25);
                column.setMinWidth(25);

            } else if (i==COL_BIB || i == COL_POSITION ) {
                column.setPreferredWidth(30);
                column.setMaxWidth(30);
                column.setMinWidth(20);
            } else if (i==COL_RACERNAME ) {
                column.setPreferredWidth(200);
                column.setMinWidth(30);
            }
            else {
                column.setPreferredWidth(100);
            }

            // Always want to see this
            if (i == COL_BESTRUN_TOTAL) {
                column.setMinWidth(70);
            }
            if (i==COL_BOATCLASS ) {
                column.setMinWidth(50);
            }
        }


    }


    protected void setupRenderers(JTable table)
    {
        TableCellRenderer renderer = new ResultsTableCellRenderer();
        try
        {
            table.setDefaultRenderer( Class.forName( "java.lang.Integer" ), renderer );
            table.setDefaultRenderer( Class.forName( "java.lang.String" ), renderer );
            table.setDefaultRenderer( Class.forName( "java.lang.Float" ), renderer );
            table.setDefaultRenderer( Class.forName( "java.lang.Double" ), renderer );
        }
        catch( ClassNotFoundException ex )
        {
            System.exit( 0 );
        }

    }

    public JTable createTable() {
        tableModel = ResultsTableModel.getInstance();
        JTable table = new JTable( tableModel );

        setupColumns(table);
        setupRenderers(table);

        return(table);

    }

    public void showTable() {

        JTable table = createTable();
//TEST
//table.setAutoCreateRowSorter(true);

        JFrame frame = new JFrame();
        frame.addWindowListener(
                new WindowAdapter()
                {
                    public void windowClosing(WindowEvent e)
                    {
//                        System.exit(0);
                    }
                }
        );

        frame.getContentPane().add( table );
        frame.pack();
        frame.setVisible( true );
    }
}
