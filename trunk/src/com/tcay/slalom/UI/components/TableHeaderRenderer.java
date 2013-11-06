package com.tcay.slalom.UI.components;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 10/31/13
 * Time: 7:24 AM
 * Need to force alignment of Table Headers within the columns
 * keywords: JTable Header Alignment CENTER LEFT RIGHT
 */
public class TableHeaderRenderer implements TableCellRenderer {
    DefaultTableCellRenderer renderer;

    /**
     * @param table
     * @param alignment one of:    JLabel.CENTER, JLabel.LEFT, JLabel.RIGHT, OR
     *                  preferred: JLabel.LEADING, JLabel.TRAILING
     *                  (works with Right to Left natural languages)
     */
    public TableHeaderRenderer(JTable table, int alignment) {
        renderer = (DefaultTableCellRenderer)
                table.getTableHeader().getDefaultRenderer();
        renderer.setHorizontalAlignment(alignment);

    }


    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int col) {
        return renderer.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, col);
    }
}
