package com.tcay.slalom.UI.tables;

import com.tcay.Singleton;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 9/28/13
 * Time: 9:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResultsTable extends Singleton {


    // These are 0 Origin indices into the columns
    static public final int COL_BOATCLASS=0;
    static public final int COL_BIB=1;
    static public final int COL_CLUB_OR_COUNTRY=2;
    static public final int COL_RACERNAME=3;
    static public final int COL_RAW1=4;
    static public final int COL_TIMIMGMODE1=5;
    static public final int COL_PEN1=6;


    static public final int COL_RAW2=7;
    static public final int COL_TIMIMGMODE2=8;
    static public final int COL_PEN2=9;

    static public final int COL_BESTRUN_TOTAL=10;
    static public final int COL_TIMINGMODE_BEST=11;


    static public final String TIMINGMODE_MANUAL="m";
    static public final String TIMINGMODE_AUTOMATIC="*";

    ResultsTableModel tableModel;

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

            } else if (i==COL_BIB || i==COL_CLUB_OR_COUNTRY ) {
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
