package com.tcay.slalom.UI;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 10/15/13
 * Time: 12:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestForm {
    private JPanel panel1;

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
        panel1 = new JPanel();
        panel1.setLayout(new FormLayout("fill:d:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:370px:grow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:99px:noGrow"));
        final JLabel label1 = new JLabel();
        label1.setText("Label");
        CellConstraints cc = new CellConstraints();
        panel1.add(label1, cc.xy(1, 1));
        final JLabel label2 = new JLabel();
        label2.setText("Label");
        panel1.add(label2, cc.xywh(3, 1, 1, 3));
        final JLabel label3 = new JLabel();
        label3.setText("Label");
        panel1.add(label3, cc.xywh(5, 1, 1, 3));
        final JLabel label4 = new JLabel();
        label4.setText("Label");
        panel1.add(label4, cc.xy(3, 5));
        final JLabel label5 = new JLabel();
        label5.setText("Label");
        panel1.add(label5, cc.xy(5, 5));
        final JLabel label6 = new JLabel();
        label6.setText("Label");
        panel1.add(label6, cc.xy(1, 5));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
