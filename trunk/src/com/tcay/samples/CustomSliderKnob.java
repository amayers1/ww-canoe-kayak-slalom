package com.tcay.samples; /**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 10/12/13
 * Time: 8:28 PM
 * To change this template use File | Settings | File Templates.
 */

//package java_sandbox;

import javax.imageio.ImageIO;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.plaf.basic.BasicSliderUI;

import java.awt.*;

import java.io.File;
import java.io.IOException;


public class CustomSliderKnob {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CustomSliderKnob();
            }
        });
    }

    public CustomSliderKnob() {

        JFrame f = new JFrame( "Swing Slider Knob" );
        f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );


        Container p = f.getContentPane();
        JSlider s = new JSlider();

        s.setUI( new mySliderUI( s ) );

        p.add( s );

        f.pack();
        f.setVisible(true);
    }

    private class mySliderUI extends BasicSliderUI {

        Image knobImage;

        public mySliderUI( JSlider aSlider ) {

            super( aSlider );

            try {
                this.knobImage = ImageIO.read( new File( "images/downstreamGate.jpg") );

            } catch ( IOException e ) {

                e.printStackTrace();
            }
        }
        public void paintThumb(Graphics g)  {

            g.drawImage( this.knobImage, thumbRect.x, thumbRect.y, 70, 69, null );

        }

    }

}