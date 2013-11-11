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
