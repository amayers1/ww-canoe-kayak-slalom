package com.tcay.samples;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 10/28/13
 * Time: 7:12 AM
 * To change this template use File | Settings | File Templates.
 */


import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class PropertyChange extends JFrame {
    public PropertyChange() {
        super();
        OuterView theGUI = new OuterView();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(theGUI);
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PropertyChange();
            }
        });
    }
}

class OuterView extends JPanel {
    private String innerValue = "";

    public OuterView() {
        super();
        InnerView innerPanel = new InnerView();
        innerPanel.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(InnerView.COMBO_CHANGED)) {
                    innerValue = evt.getNewValue().toString();
                    System.out.println("new value from inside of OuterView: "
                            + innerValue);
                }
            }
        });
        JButton button = new JButton("display OuterView's model");
        button.addActionListener(new ButtonListener());
        add(innerPanel);
        add(button);
    }

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            System.out.println("button was clicked. innerValue: " + innerValue);
        }
    }
}

class InnerView extends JPanel {
    public static final String COMBO_CHANGED = "Combo Changed";
    // private SwingPropertyChangeSupport pcSupport = new
    // SwingPropertyChangeSupport(this);
    String oldValue = "";

    public InnerView() {
        super();
        //setBorder(new Border());
        String[] items = new String[] { "item 1", "item 2", "item 3" };
        JComboBox comboBox = new JComboBox(items);
        comboBox.addActionListener(new ComboBoxListener());
        add(comboBox);

    }

    private class ComboBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            String text = ((JComboBox) ae.getSource()).getSelectedItem()
                    .toString();
            firePropertyChange(COMBO_CHANGED, oldValue, text);
            oldValue = text;
            System.out.println("store " + text + " in InnerView's model");
        }
    }
}