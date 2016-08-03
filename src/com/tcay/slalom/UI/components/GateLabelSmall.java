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

import com.tcay.slalom.Race;
import com.tcay.slalom.UI.tables.RunScoringTable;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ItemListener;

/**
 * SlalomScoring
 * Teton Cay Group Inc. 2013
 * <p/>
 * User: allen
 * Date: 11/9/13
 * Time: 4:28 PM
 */

public class GateLabelSmall extends JLabel implements TableCellRenderer {
    protected GateLabelSmall rendererComponent;
    protected int column;

    public GateLabelSmall(){//ItemListener itemListener) {
        rendererComponent = this;
        //rendererComponent.addItemListener(itemListener);
    }

    protected void setColumn(int column) {
        this.column = column;
    }



    @Override
    public Component getTableCellRendererComponent(JTable jTable, Object o, boolean b, boolean b2, int i, int i2) {
        Race race = Race.getInstance();
        if (jTable != null) {
            JTableHeader header = jTable.getTableHeader();
            if (header != null) {
                rendererComponent.setForeground(header.getForeground());
                rendererComponent.setBackground(header.getBackground());
                rendererComponent.setFont(header.getFont());
            }
        }
        setColumn(column);
        int gate = i2 - RunScoringTable.COL_TOTAL;

        rendererComponent.setText(new Integer(gate).toString());

        rendererComponent.setIcon(race.isUpstream(gate) ?
                                       race.getUpstreamTinyII() :
                                       race.getDownstreamTinyII());
        rendererComponent.setHorizontalAlignment(JLabel.CENTER);
        rendererComponent.setHorizontalTextPosition(JLabel.CENTER);
        rendererComponent.setVerticalTextPosition(JLabel.TOP);

        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        return rendererComponent;
    }
}