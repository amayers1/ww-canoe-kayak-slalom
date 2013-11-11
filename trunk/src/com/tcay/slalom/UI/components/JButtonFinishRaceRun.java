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
