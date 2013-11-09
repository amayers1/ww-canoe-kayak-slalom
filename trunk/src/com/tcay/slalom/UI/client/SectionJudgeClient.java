package com.tcay.slalom.UI.client;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.tcay.slalom.socket.Client;
import com.tcay.slalom.socket.Proxy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 10/26/13
 * Time: 9:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class SectionJudgeClient {

    private JComboBox comboBoxJudgingSection;
    private JPanel panel1;
    private JButton doneButton;
    private Proxy proxy;
    JFrame frame;


    private void close() {      // todo create super class parent to hold this
        Component c = panel1.getRootPane().getParent();
        c.setVisible(false);
        ((JFrame) c).dispose();

    }


    public SectionJudgeClient() {
        frame = new JFrame("Judging - New Frame");


        $$$setupUI$$$();
        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                close();
                frame.setContentPane(new ClientRacePenaltiesUIDynamic(comboBoxJudgingSection.getSelectedIndex() + 1, proxy).getRootComponent());
                frame.pack();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);         // todo unhide, etc
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
                Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
                int x = (int) rect.getMaxX() - frame.getWidth();
                int y = (int) rect.getMaxY() - frame.getHeight();
                frame.setLocation(x, y);
                frame.setVisible(true);
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

        proxy = new Proxy(new Client());


        comboBoxJudgingSection = new JComboBox();
        comboBoxJudgingSection.setModel(new DefaultComboBoxModel() {
            @Override
            public int getSize() {
                return proxy.getSections().size();
            }

            @Override
            public Object getElementAt(int i) {
                return proxy.getSections().get(i);
            }
        });
        if (comboBoxJudgingSection.getItemCount() > 0)  {
            comboBoxJudgingSection.setSelectedIndex(0);       /// OK ?  fix me  out of bounds if server has no data yet
        }
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Section Judge Client");


        SectionJudgeClient sectionJudgeClient = new SectionJudgeClient(); ///fixme section 1 should be dynamic
        frame.setContentPane(sectionJudgeClient.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
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
        panel1.setLayout(new FormLayout("fill:122px:noGrow,left:39dlu:noGrow,fill:257px:grow", "center:d:noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow"));
        CellConstraints cc = new CellConstraints();
        panel1.add(comboBoxJudgingSection, cc.xy(3, 1));
        final JLabel label1 = new JLabel();
        label1.setText("Section");
        panel1.add(label1, cc.xy(1, 1));
        doneButton = new JButton();
        doneButton.setText("Done");
        panel1.add(doneButton, cc.xy(3, 3));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
