package com.tcay.slalom.UI.tables;

import com.tcay.slalom.Race;
import com.tcay.slalom.Result;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * SlalomScoring
 * Teton Cay Group Inc. ©2013
 * <p/>
 * User: allen
 * Date: 11/4/13
 * Time: 7:06 PM
 */
public class ResultsTableAutoScroll extends ResultsTable {

    int scrollPosition=0;


    @Override
    public JTable createTable() {
        tableModel = new ResultsTableModelAutoScroll();  /// fixme    .getInstance();
        JTable table = new JTable( tableModel );
        setupColumns(table);
        setupRenderers(table);
        return(table);

    }

    protected class ResultsTableModelAutoScroll extends ResultsTableModel {

        Timer scrollTimer;
        protected ResultsTableModelAutoScroll() {

            scrollTimer
                    = new Timer(2000,
                    new ActionListener() {
                        public void actionPerformed(ActionEvent actionEvent) {
                            scrollPosition++;
                            if (scrollPosition>=getRowCount()) {
                                scrollPosition=0;
                            }
                            updateResults();
                        }
                    });
            scrollTimer.setInitialDelay(2000);
            scrollTimer.start();
        }

        @Override
        protected ArrayList<Result> getSortedResults() {
            ArrayList<Result> original = Race.getInstance().getTempResults();
            ArrayList<Result> newList  = new ArrayList<Result>();
            // rotate the elements to form a continuous loop for results
            // still in sorted order, but scrolling

            int i=0;
            for (Result r:original) {
                if (i>=scrollPosition ) {
                    newList.add(r);
                }
                i++;
            }

            i=0;
            for (Result r:original) {
                if (i<scrollPosition ) {
                    newList.add(r);
                }
                i++;
            }
            return newList;
        }
    }
}
