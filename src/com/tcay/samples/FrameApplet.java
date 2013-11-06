package com.tcay.samples;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 10/20/13
 * Time: 6:18 PM
 * To change this template use File | Settings | File Templates.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This program demonstrates the basics of creating a frame
 * user interface with a menubar. It also shows how to
 * add a menubar and dropdown menus to the applet, which wasn't
 * possible in the basic AWT heavyweight component.
 **/
public class FrameApplet extends JApplet
        implements ActionListener
{
    JFrame fFrame;
    JMenuItem fMenuClose ;
    JMenuItem fMenuOpen;

    /** Build an applet interface with a menubar. A
     * a drop down menu includes Open/Close items
     * for opening and closing an instance of ParticleFrame.
     **/
    public void init () {
        JMenuBar mb = new JMenuBar ();
        JMenu m = new JMenu ("File");
        fMenuOpen= new JMenuItem ("Open");
        m.add (fMenuOpen);
        fMenuOpen.addActionListener (this);

        fMenuClose = new JMenuItem ("Close");
        m.add (fMenuClose);
        fMenuClose.addActionListener (this);
        mb.add (m);

        setJMenuBar (mb);

        fFrame = new ParticleFrame (this);
        fFrame.setVisible (true);
        fMenuOpen.setEnabled (false);
        fMenuClose.setEnabled (true);

    } // init

    /** Get the menu events here. Open an instance of ParticleFrame
     * or close the one currently displayed.
     **/
    public void actionPerformed (ActionEvent e) {
        String command = e.getActionCommand ();
        if (command.equals ("Close")) {
            close ();
        } else { // Open
            if (fFrame == null) {
                fFrame = new ParticleFrame (this);
                fFrame.setVisible (true);
                fMenuOpen.setEnabled (false);
                fMenuClose.setEnabled (true);
            }
        }
    } // actionPerformed

    /** Close the frame. **/
    void close () {
        fFrame.dispose ();
        fFrame = null;
        fMenuOpen.setEnabled (true);
        fMenuClose.setEnabled (false);
    } // close

} // class FrameApplet


/** A JFrame subclass that displays a menu bar
 * and a JComboBox.
 **/
class ParticleFrame extends JFrame
        implements ActionListener, ItemListener
{
    JLabel fLabelA;
    JLabel fLabelB;

    FrameApplet fApplet;

    ParticleFrame (FrameApplet applet) {
        super ("Frame Test");

        fApplet = applet;
        Container content_pane = getContentPane ();

        content_pane.setLayout (new GridLayout (1,3));

        JPanel choice_panel = new JPanel ();
        choice_panel.add (new JLabel ("Quark", JLabel.RIGHT) );

        JComboBox c = new JComboBox ();
        c.addItem ("Up");
        c.addItem ("Down");
        c.addItem ("Strange");
        c.addItem ("Charm");
        c.addItem ("Top");
        c.addItem ("Bottom");
        c.addItemListener (this);
        choice_panel.add (c);

        content_pane.add (choice_panel);

        fLabelA =new JLabel ("Quark: Up");
        content_pane.add (fLabelA);
        fLabelB =new JLabel ("Lepton: Electron");
        content_pane.add (fLabelB);

        // Use the helper method makeMenuItem
        // for making the menu items and registering
        // their listener.
        JMenu m = new JMenu ("Lepton");
        m.add (makeMenuItem ("electron"));
        m.add (makeMenuItem ("muon"));
        m.add (makeMenuItem ("tau"));

        JMenu sm = new JMenu ("Neutrino");
        sm.add (makeMenuItem ("e Neutrino"));
        sm.add (makeMenuItem ("mu Neutrino"));
        sm.add (makeMenuItem ("tau Neutrino"));

        m.add (sm);
        m.add (makeMenuItem ("Quit"));
        JMenuBar mb = new JMenuBar ();
        mb.add (m);

        setJMenuBar (mb);
        setSize (200,200);
        pack ();
        setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
    } // ctor

    /** Get the combobox item events here. **/
    public void itemStateChanged (ItemEvent e) {
        String command = e.getItem ().toString ();
        if (command.equals ("Quit") )
            dispose ();
        else
            fLabelA.setText ("Quark: " + command);
    } // itemStateChanged

    /** Get the menu events here. **/
    public void actionPerformed (ActionEvent e) {
        String command = e.getActionCommand ();
        if (command.equals ("Quit")) {
            fApplet.close ();
        } else {
            fLabelB.setText ("Lepton: " + command);
        }
    } // actionPerformed

    /** This "helper method" makes a menu item and then
     * registers this applet as a listener to it.
     **/
    private JMenuItem makeMenuItem (String name) {
        JMenuItem m = new JMenuItem (name);
        m.addActionListener (this);
        return m;
    } // makeMenuItem

} // class ParticleFrame
//- See more at: http://www.codemiles.com/java/jframe-on-applet-t385.html#sthash.ed5WqTf9.dpuf