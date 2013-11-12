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

import com.tcay.slalom.Race;
import com.tcay.slalom.Result;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * SlalomScoring
 * Teton Cay Group Inc. 2013
 * <p/>
 * User: allen
 * Date: 11/4/13
 * Time: 7:06 PM
 */
public class ResultsTableAutoScroll extends ResultsTableSpectator {

    int scrollPosition=0;


    @Override
    public JTable createTable() {
        JTable table = super.createTable();

        //tableModel = new ResultsTableModelAutoScroll();  /// fixme    .getInstance();
        //JTable table = new JTable( tableModel );
        //setupColumns(table);
        //setupRenderers(table);

        table.setModel(new ResultsTableModelAutoScroll());
        removeDetailColumns();
        return(table);

    }

/*
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

            } else if (i==COL_BIB || i==COL_CLUB_OR_COUNTRY || i == COL_POSITION ) {
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
*/


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
