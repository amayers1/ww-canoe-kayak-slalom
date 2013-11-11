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

    public GateLabelSmall(){;//ItemListener itemListener) {
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