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



            // TODO Priority 1 - THe Concurent exception below is on the immediately following code block "for (Result r:original) {"
            
            /*Exception in thread "AWT-EventQueue-0" java.util.ConcurrentModificationException
	at java.util.AbstractList$Itr.checkForComodification(AbstractList.java:372)
	at java.util.AbstractList$Itr.next(AbstractList.java:343)
	at com.tcay.slalom.UI.tables.ResultsTableAutoScroll$ResultsTableModelAutoScroll.getSortedResults(ResultsTableAutoScroll.java:157)
	at com.tcay.slalom.UI.tables.ResultsTableModel.getValueAt(ResultsTableModel.java:139)
	at com.tcay.slalom.UI.tables.ResultsTableModel.getColumnClass(ResultsTableModel.java:115)
	at javax.swing.JTable.getColumnClass(JTable.java:2662)
	at javax.swing.JTable.getCellRenderer(JTable.java:5669)
	at javax.swing.plaf.basic.BasicTableUI.paintCell(BasicTableUI.java:2071)
	at javax.swing.plaf.basic.BasicTableUI.paintCells(BasicTableUI.java:1974)
	at javax.swing.plaf.basic.BasicTableUI.paint(BasicTableUI.java:1770)
	at javax.swing.plaf.ComponentUI.update(ComponentUI.java:153)
	at javax.swing.JComponent.paintComponent(JComponent.java:760)
	at javax.swing.JComponent.paint(JComponent.java:1037)
	at javax.swing.JComponent._paintImmediately(JComponent.java:5106)
	at javax.swing.JComponent.paintImmediately(JComponent.java:4890)
	at javax.swing.RepaintManager$3.run(RepaintManager.java:814)
	at javax.swing.RepaintManager$3.run(RepaintManager.java:802)
	at java.security.AccessController.doPrivileged(Native Method)
	at java.security.AccessControlContext$1.doIntersectionPrivilege(AccessControlContext.java:86)
	at javax.swing.RepaintManager.paintDirtyRegions(RepaintManager.java:802)
	at javax.swing.RepaintManager.paintDirtyRegions(RepaintManager.java:745)
	at javax.swing.RepaintManager.prePaintDirtyRegions(RepaintManager.java:725)
	at javax.swing.RepaintManager.access$1000(RepaintManager.java:46)
	at javax.swing.RepaintManager$ProcessingRunnable.run(RepaintManager.java:1680)
	at java.awt.event.InvocationEvent.dispatch(InvocationEvent.java:209)
	at java.awt.EventQueue.dispatchEventImpl(EventQueue.java:715)
	at java.awt.EventQueue.access$400(EventQueue.java:82)
	at java.awt.EventQueue$2.run(EventQueue.java:676)
	at java.awt.EventQueue$2.run(EventQueue.java:674)
	at java.security.AccessController.doPrivileged(Native Method)
	at java.security.AccessControlContext$1.doIntersectionPrivilege(AccessControlContext.java:86)
	at java.awt.EventQueue.dispatchEvent(EventQueue.java:685)
	at java.awt.EventDispatchThread.pumpOneEventForFilters(EventDispatchThread.java:296)
	at java.awt.EventDispatchThread.pumpEventsForFilter(EventDispatchThread.java:211)
	at java.awt.EventDispatchThread.pumpEventsForHierarchy(EventDispatchThread.java:201)
	at java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:196)
	at java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:188)
	at java.awt.EventDispatchThread.run(EventDispatchThread.java:122)
SPEN B=108 r=2                                0  0  0  0  0  0
SPEN B=82 r=2                                               0  0  0  0  0  0
NEW RaceRun(b)25run#2 D. Jung
            */

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
