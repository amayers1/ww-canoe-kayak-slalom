package com.tcay.samples; /**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 10/12/13
 * Time: 6:14 PM
 * To change this template use File | Settings | File Templates.
 */

import javax.swing.*;
import java.awt.*;


import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.net.URL;


import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;

public class IconCcheckBoxSample {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Iconizing CheckBox");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Icon checked = new CheckBoxIcon();
        Icon unchecked = new CheckBoxIcon();

        JCheckBox aCheckBox1 = new JCheckBox("Pizza", unchecked);
        aCheckBox1.setSelectedIcon(checked);
        JCheckBox aCheckBox2 = new JCheckBox("Calzone");
        aCheckBox2.setIcon(unchecked);
        aCheckBox2.setSelectedIcon(checked);




        Icon checkBoxIcon = new CheckBoxIcon();
        JCheckBox aCheckBox3 = new JCheckBox("Anchovies", checkBoxIcon);
        JCheckBox aCheckBox4 = new JCheckBox("Stuffed Crust", checked);
        frame.setLayout(new GridLayout(0,1));
        frame.add(aCheckBox1);
        frame.add(aCheckBox2);
        frame.add(aCheckBox3);
        frame.add(aCheckBox4);
        frame.setSize(300, 200);
        frame.setVisible(true);
    }


}
class CheckBoxIcon implements Icon {
    public void paintIcon(Component component, Graphics g, int x, int y) {
        AbstractButton abstractButton = (AbstractButton)component;
        ButtonModel buttonModel = abstractButton.getModel();

        //ImageIcon ii = createImageIcon("com/tcay/slalom/downstreamGateSmall.jpg","info");

        if(buttonModel.isSelected()) {
//            g.drawImage(createImage("com/tcay/slalom/downstreamGateSmall.jpg","info"), x, y, component);
            g.drawImage(createImage("images/downstreamGateTiny.jpg","info"), x, y, component);
//            g.drawImage(createImage("images/upstreamGateSmall.jpg","info"), x, y, component);
        }
        else {
//i            g.drawImage(createImage("com/tcay/slalom/upstreamGateSmall.jpg","info"), x, y, component);
            g.drawImage(createImage("images/upstreamGateTiny.jpg","info"), x, y, component);
        }

    }
    public int getIconWidth() {
        return 26;
    }
    public int getIconHeight() {
        return 41;
    }

    protected static Image createImage(String path, String description) {
        URL imageURL = CheckBoxIcon.class.getResource(path);
        Image icn = null;


        if (imageURL == null) {
            if(null==icn){
                System.out.println("path: "+path);
                icn = new ImageIcon (path).getImage();//com.tcay.samples.CheckBoxIcon.class.getResource(path.replace("..",""))).getImage();
                if(null!=icn)
                    return icn;
                else{
                    System.err.println("Resource not found: " + path);
                    return null;
                }
            }
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }


    /** Returns an ImageIcon, or null if the path was invalid. */
    protected ImageIcon createImageIcon(String path,
                                        String description) {
        URL imgURL = //getClass().
                CheckBoxIcon.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {

            ImageIcon ii = new ImageIcon (path);
            System.err.println("Couldn't find file: " + path);
            return ii;//null;
        }
    }



}