package com.tcay.slalom.UI.client;

import com.tcay.slalom.Race;
import com.tcay.slalom.UI.components.GatePenaltyButton;
import com.tcay.slalom.UI.components.ICF_50_ReasonButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * SlalomScoring
 * Teton Cay Group Inc. 2013
 * <p/>
 * User: allen
 * Date: 11/7/13
 * Time: 8:59 PM
 */


/**
 * Request explanation of how the 50 second penalty for a missed gate occurred
 */
public class ClientRacePenaltyICF50SecondReasonDialog {

    // Inspired from :  http://stackoverflow.com/questions/13921759/joptionpane-with-images-on-the-buttons

     String reasonCode = null;

     private ICF_50_ReasonButton[] reasons = {
             new ICF_50_ReasonButton( "Upside down in gate", Race.getInstance().getIcfPenaltyUpsideDown(), "G"),
             new ICF_50_ReasonButton( "Intentionally moved gate", Race.getInstance().getIcfPenaltyIntentionallyMovedGate(), "F"),
             new ICF_50_ReasonButton( "Not 1 unit thru gate", Race.getInstance().getIcfPenaltyDidntGoThroughas1Unit(), "E"),
             new ICF_50_ReasonButton( "Washed back thru gate line", Race.getInstance().getIcfPenaltyWashedBackThroughGateLine(), "D"),
             new ICF_50_ReasonButton( "Wrong Direction", Race.getInstance().getIcfPenaltyWrongDirection(), "C"),
             new ICF_50_ReasonButton( "Head AND Boat not in gate", Race.getInstance().getIcfPenaltyHeadAndBoatNotInGateTogether(), "B"),
             new ICF_50_ReasonButton( "Out of Sequence", null, "A" )
    } ;


    public String doDialog(GatePenaltyButton penaltyButton, String racer, int gate) {
        JFrame frame = new JFrame();

        JOptionPane optionPane = new JOptionPane();
        optionPane.setIcon(Race.getInstance().getKayakSmall());
        optionPane.setMessage("How did " + racer + " miss gate " + gate + " ?");
        optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);

        for (ICF_50_ReasonButton jb: reasons) {
            setListenerAndOption(jb, optionPane, penaltyButton);
            jb.setHorizontalTextPosition(JButton.CENTER);
            jb.setVerticalTextPosition(JButton.BOTTOM);

        }
        optionPane.setOptions(reasons);
        JDialog dialog = optionPane.createDialog(frame, "ICF Missed Gate Reason");
        //dialog.setDefaultCloseOperation();
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                //frame.dispose();
            }
        });
        dialog.setVisible(true);
        return(reasonCode);
    }

    public /*static*/ void setListenerAndOption(final ICF_50_ReasonButton jb, final JOptionPane optionPane, final GatePenaltyButton penaltyButton) {
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                // Return current text label, instead of argument to method
                ICF_50_ReasonButton btn = (ICF_50_ReasonButton)actionEvent.getSource();

                optionPane.setValue(btn.getReasonCode());
                reasonCode =  btn.getReasonCode();
            }
        };
        jb.addActionListener(actionListener);
    }
}
