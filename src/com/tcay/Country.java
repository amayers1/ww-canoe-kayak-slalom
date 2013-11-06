package com.tcay;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 10/30/13
 * Time: 10:34 AM
 * To change this template use File | Settings | File Templates.
 */


/**
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
