package com.tcay.slalom.UI;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.tcay.slalom.Race;
import com.tcay.slalom.UI.tables.ResultsTable;
import com.tcay.slalom.UI.tables.ResultsTableAutoScroll;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 9/28/13
 * Time: 6:28 PM
 * To change this template use File | Settings | File Templates.
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
        Race.getInstance().updateResults();

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
        leaderScrollPane.setPreferredSize(new Dimension(800, 600));
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