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

import com.tcay.VirtualLED;
import com.tcay.slalom.RaceResources;

import javax.swing.*;

/**
 * Created by allen on 4/19/17.
 */


public class LedIndicator {
    private ImageIcon imageIconOn;
    private ImageIcon imageIconOff;

    public ImageIcon getCurrentIcon() {
        return currentIcon;
    }

    private ImageIcon currentIcon;
    private boolean   isOn = false;
    static private ImageIcon greenLed = RaceResources.getInstance().getLedGreen();
    static private ImageIcon redLed = RaceResources.getInstance().getLedRed();
    static private ImageIcon greenLedOff = RaceResources.getInstance().getLedGreenOff();
    static private ImageIcon redLedOff = RaceResources.getInstance().getLedRedOff();
    static private ImageIcon imageIconDisabled = RaceResources.getInstance().getLedDisabled();

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        if (!isActive) {
            currentIcon = imageIconDisabled;
        } else {
            setLedIcon();
        }
        this.isActive = isActive;
    }

    private boolean   isActive = true;

    public boolean isOn() {
        return isOn;
    }

    public void setIsOn(boolean isOn) {
        this.isOn = isOn;
        if (isActive()) {
            if (isOn) {
                currentIcon = imageIconOn;
            } else {
                currentIcon = imageIconOff;
            }

        }
    }


    public LedIndicator(VirtualLED type) {
        switch (type) {
            case RED:
                imageIconOff = redLedOff;
                imageIconOn  = redLed;
                break;
            case GREEN:
                imageIconOff = greenLedOff;
                imageIconOn  = greenLed;
                break;
        }
    }
    public void setLedIcon(){
        if (isActive()) {
            if (isOn) {
                currentIcon = imageIconOn;
            }
            else {
                currentIcon = imageIconOff;
            }
        }

    }


}
