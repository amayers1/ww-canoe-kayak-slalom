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

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;


// with icons here https://forums.oracle.com/thread/2552746

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 10/12/13
 * Time: 3:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class JCheckBoxGateList extends JList
{
    protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);



    private void init() {
        setCellRenderer(new CheckBoxCellRenderer());

        addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                int index = locationToIndex(e.getPoint());
                if (index != -1)
                {
                    JCheckBox checkbox = (JCheckBox) getModel().getElementAt(index);
                    checkbox.setSelected(!checkbox.isSelected());

                    if (checkbox.isSelected()) {
                        checkbox.setIcon(Race.getInstance().getUpstreamSmallII());
                    }
                    else {
                        checkbox.setIcon(Race.getInstance().getDownstreamSmallII());
                    }

                    repaint();
                }
            }
        });

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    int index = getSelectedIndex();
                    if (index != -1) {
                        JCheckBox checkbox = (JCheckBox) getModel().getElementAt(index);
                        checkbox.setSelected(!checkbox.isSelected());
                        repaint();
                    }
                }
            }
        });

        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    }


    public JCheckBoxGateList(Vector vector) {
        super(vector);
        init();
    }


    public JCheckBoxGateList()
    {
        init();
    }

    protected class CheckBoxCellRenderer implements ListCellRenderer
    {
        public Component getListCellRendererComponent(JList list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus)
        {
            JCheckBox checkbox = (JCheckBox) value;
            checkbox.setBackground(isSelected ? getSelectionBackground() : getBackground());
            checkbox.setForeground(isSelected ? getSelectionForeground() : getForeground());

            checkbox.setEnabled(isEnabled());
            checkbox.setFont(getFont());
            checkbox.setFocusPainted(false);

            checkbox.setBorderPainted(true);
            checkbox.setBorder(isSelected ? UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);

            return checkbox;
        }
    }


    public static void main(String args[])
    {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        JCheckBoxGateList cbList = new JCheckBoxGateList();

        Object[] cbArray = new Object[3];
        cbArray[0] = new JCheckBox("one");
        cbArray[1] = new JCheckBox("two");
        cbArray[2] = new JCheckBox("three");

        cbList.setListData(cbArray);

        frame.getContentPane().add(cbList);
        frame.pack();
        frame.setVisible(true);
    }
}
