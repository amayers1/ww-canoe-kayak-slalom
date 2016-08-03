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

package com.tcay;

import com.tcay.util.Log;

import javax.swing.*;

/**
 * ${PROJECT_NAME}
 *
 * Teton Cay Group Inc. ${YEAR}
 *
 * User: allen
 * Date: 10/30/13
 * Time: 10:34 AM
 *
 */


/**
 * Implementation of the political unit Country - codes, flags, etc.
 *
 * http://en.wikipedia.org/wiki/List_of_IOC_country_codes
 *
 */
public class Country {
    String name;
    String abbreviation;
    ImageIcon imageIcon;
    ImageIcon imageIconTiny;

    public Country(String name, String abbreviation, ImageIcon imageIcon, ImageIcon imageIconTiny) {
        this.name = name;
        this.imageIcon = imageIcon;
        this.abbreviation = abbreviation;
        this.imageIconTiny = imageIconTiny;
    }

    public String getName() {
        return name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }
    public ImageIcon getImageIconTiny() {
        return imageIconTiny;
    }
}
