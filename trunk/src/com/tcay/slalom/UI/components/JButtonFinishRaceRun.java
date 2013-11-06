package com.tcay.slalom.UI.components;

import com.tcay.slalom.RaceRun;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 10/29/13
 * Time: 11:03 PM
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
public class JButtonFinishRaceRun extends JButton {
    RaceRun run;

    public RaceRun getRun() {
        return run;
    }

    public void setRun(RaceRun run) {
        this.run = run;
    }
}
