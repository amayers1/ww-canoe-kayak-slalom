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

package com.tcay.slalom.UI;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.tcay.slalom.Race;
import com.tcay.slalom.UI.tables.ResultsTable;
import com.tcay.slalom.UI.tables.ResultsTableAutoScroll;

import javax.swing.*;
import java.awt.*;

/**
 * ${PROJECT_NAME}
 * <p/>
 * Teton Cay Group Inc. ${YEAR}
 * <p/>
 * <p/>
 * User: allen
 * Date: 9/28/13
 * Time: 6:28 PM
 */
public class LeaderBoardScroll {
    private JPanel panel1;
    private JScrollPane leaderScrollPane;
    private JTable table;
    private ResultsTable resultsTable;


    //public LeaderBoard() {
    //}

    private void createUIComponents() {
        // TODO: place custom component creation code here
//        resultsTable  = new JBTable();
        resultsTable = new ResultsTableAutoScroll();

        table = resultsTable.createTable();
        leaderScrollPane = new JScrollPane();
        Race.getInstance().updateResults();  // TODO REALLY NEEDED ???

    }

    public void setData(LeaderBoardScroll data) {
    }

    public void getData(LeaderBoardScroll data) {
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        panel1 = new JPanel();
        panel1.setLayout(new FormLayout("fill:d:grow", "center:d:grow"));
        leaderScrollPane.setPreferredSize(new Dimension(650, 250));
        CellConstraints cc = new CellConstraints();
        panel1.add(leaderScrollPane, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.FILL));
        leaderScrollPane.setViewportView(table);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
