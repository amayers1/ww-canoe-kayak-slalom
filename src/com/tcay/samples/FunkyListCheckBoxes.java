package com.tcay.samples; /**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 10/12/13
 * Time: 3:20 PM
 * To change this template use File | Settings | File Templates.
 */

/// from https://forums.oracle.com/thread/1482163

import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class FunkyListCheckBoxes extends JList
{
    protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    public FunkyListCheckBoxes()
    {
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
                    repaint();
                }
            }
        });

        addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_SPACE)
                {
                    int index = getSelectedIndex();
                    if (index != -1)
                    {
                        JCheckBox checkbox = (JCheckBox) getModel().getElementAt(index);
                        checkbox.setSelected(!checkbox.isSelected());
                        repaint();
                    }
                }
            }
        });

        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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

        FunkyListCheckBoxes cbList = new FunkyListCheckBoxes();

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
 /*


public class com.tcay.samples.FunkyListCheckBoxes extends JFrame {

    private final FunkyCellRendererComponent funkyCRC = new FunkyCellRendererComponent();

    public com.tcay.samples.FunkyListCheckBoxes() {
        getContentPane().setLayout(new FlowLayout());

        String[] items = { "Item 1", "Item 2", "Item 3", "Item 4" };
        JList theList = new JList(items);
        theList.setCellRenderer(
                new ListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(JList list, Object value, int index,
                                                                  boolean isSelected, boolean cellHasFocus) {
                        funkyCRC.setup(list, value, index, isSelected, cellHasFocus);
                        return funkyCRC;
                    }
                });
        getContentPane().add(theList);

    }

    public static void main(String[] args) {
        com.tcay.samples.FunkyListCheckBoxes gui = new com.tcay.samples.FunkyListCheckBoxes();
        gui.pack();
        gui.setVisible(true);
    }

    class FunkyCellRendererComponent extends JPanel {

        private JCheckBox checkBox = new JCheckBox();
        private JLabel label = new JLabel();
        private JList list;
        private int index;

        public FunkyCellRendererComponent() {
            super(null);
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            add(this.checkBox);
            add(this.label);
        }

        public void setup(JList list, Object value, int index,
                          boolean isSelected, boolean cellHasFocus) {
            this.list = list;
            this.index = index;
            this.label.setText((String) list.getModel().getElementAt(index));
            if (isSelected) {
                setForeground(list.getSelectionForeground());
                setBackground(list.getSelectionBackground());
            } else {
                setForeground(list.getForeground());
                setBackground(list.getBackground());
            }
            this.checkBox.setSelected(isSelected);
        }

    }

}

*/