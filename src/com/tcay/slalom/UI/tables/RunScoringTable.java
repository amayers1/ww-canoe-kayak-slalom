package com.tcay.slalom.UI.tables;

import com.tcay.slalom.UI.components.TableHeaderRenderer;
import com.tcay.util.Log;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.JLabel;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 10/17/13
 * Time: 8:41 PM
 * To change this template use File | Settings | File Templates.
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
        header.setDefaultRenderer(new TableHeaderRenderer(table, JLabel.CENTER));

        TableCellRenderer renderer = new RunScoringTableCellRenderer();

        TableColumn column;
        for (int i = 0; i < table.getColumnCount(); i++) {
            column = table.getColumnModel().getColumn(i);
            switch(i) {
                case COL_BOATCLASS:
                    column.setMinWidth(30);
                    column.setPreferredWidth(50);
                    break;
                case COL_RUN_NBR:
                    column.setPreferredWidth(34);
                    column.setMinWidth(25);
                    column.setMaxWidth(34);
                    break;
                case COL_RACERNAME:
                    column.setPreferredWidth(140);
                    column.setMinWidth(80);
                    break;
                case COL_RAW:
                     column.setMinWidth(20);
                     /// missing break intentional ... we want to fall through to COL_TOTAL !
                case COL_TOTAL:
                    column.setMinWidth(100);
                    column.setMaxWidth(140);
                    column.setPreferredWidth(140);
                    break;
                case COL_BIB:
                    column.setPreferredWidth(30);
                    column.setMinWidth(20);
                    break;
                default:
                    column.setPreferredWidth(25);
                    column.setMinWidth(20);
                    break;
            }
        }

        try
        {
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
