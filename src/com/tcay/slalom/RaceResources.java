package com.tcay.slalom;

import com.tcay.Country;
import com.tcay.util.Log;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 10/23/13
 * Time: 12:25 PM
 */
public class RaceResources {

    protected static final String UNNAMED_RACE = "Unnamed Race";
    protected static final String SECTIONS_NONE = "none";
    protected static final String FIRST = "First";
    protected static final String SECOND = "Second";
    protected static final int    DEFAULT_NBR_GATES = 25;

    private transient ImageIcon tagHeuerII;
    private transient ImageIcon tagHeuerTinyII;
    private transient ImageIcon downstreamSmallII;
    private transient ImageIcon upstreamSmallII;
    private transient ImageIcon downstreamTinyII;
    private transient ImageIcon upstreamTinyII;
    private transient ImageIcon slalomCourseSmall;
    private transient ImageIcon stopWatchII;
    private transient ImageIcon emptyII = new ImageIcon();  // needed for table cell renderer, can't have null value for cell renderer

    private transient ArrayList<Country> countries;
    private transient ImageIcon blankII = new ImageIcon();  /// need to give back an Image Icon for table cell render to
    protected transient Log log;
    // handle correctly

    protected RaceResources() {
        log = Log.getInstance();
        loadImages();
        loadCountryImages();
    }

    private transient static RaceResources instance=null;

    public synchronized static RaceResources getInstance() {
        if (instance==null)  {
            instance = new RaceResources();
        }
        return instance;
    }

    private ImageIcon getImageIconFlag24(String name) {
        ImageIcon ii = blankII;
        URL imgURL;
        Class thisClass = getClass();

        imgURL = thisClass.getResource("images/flags24/" + name);     // todo From location of package of Race ???
        if (imgURL!=null)
            ii = new ImageIcon(imgURL, name);
        return ii;

    }
    private ImageIcon getImageIconFlag16(String name) {
        ImageIcon ii = blankII;
        URL imgURL;
        Class thisClass = getClass();

        imgURL = thisClass.getResource("images/flags16/" + name);     // todo From location of package of Race ???
        if (imgURL!=null)
            ii = new ImageIcon(imgURL, name);
        return ii;
    }


    private void loadCountryImages() {
        String flagImageName;
        countries = new ArrayList<Country>();
        flagImageName = "France.png";
        countries.add(new Country("France", "FRA", getImageIconFlag24(flagImageName), getImageIconFlag16(flagImageName)));

        flagImageName = "Spain.png";     // todo From location of package of Race ???
        countries.add(new Country("Spain", "ESP" , getImageIconFlag24(flagImageName), getImageIconFlag16(flagImageName)));

        flagImageName = "Slovakia.png";     // todo From location of package of Race ???
        countries.add(new Country("Slovakia", "SVK" , getImageIconFlag24(flagImageName), getImageIconFlag16(flagImageName)));

        flagImageName = "Australia.png";     // todo From location of package of Race ???
        countries.add(new Country("Australia", "AUS" , getImageIconFlag24(flagImageName), getImageIconFlag16(flagImageName)));

        flagImageName = "Austria.png";     // todo From location of package of Race ???
        countries.add(new Country("Austria", "AUT" , getImageIconFlag24(flagImageName), getImageIconFlag16(flagImageName)));

        flagImageName = "Czech Republic.png";     // todo From location of package of Race ???
        countries.add(new Country("Czech Republic", "CZE" , getImageIconFlag24(flagImageName), getImageIconFlag16(flagImageName)));

        flagImageName = "Slovenia.png";     // todo From location of package of Race ???
        countries.add(new Country("Slovenia", "SLO" , getImageIconFlag24(flagImageName), getImageIconFlag16(flagImageName)));

        flagImageName = "Germany.png";     // todo From location of package of Race ???
        countries.add(new Country("Germany", "GER" , getImageIconFlag24(flagImageName), getImageIconFlag16(flagImageName)));

        flagImageName = "Italy.png";     // todo From location of package of Race ???
        countries.add(new Country("Italy", "ITA" , getImageIconFlag24(flagImageName), getImageIconFlag16(flagImageName)));

        flagImageName = "United Kingdom(Great Britain).png";     // todo From location of package of Race ???
        countries.add(new Country("United Kingdom", "GBR" , getImageIconFlag24(flagImageName), getImageIconFlag16(flagImageName)));

        flagImageName = "Slovakia.png";     // todo From location of package of Race ???
        countries.add(new Country("Slovakia", "SVK" , getImageIconFlag24(flagImageName), getImageIconFlag16(flagImageName)));

        flagImageName = "United States of America(USA).png";     // todo From location of package of Race ???
        countries.add(new Country("United States of America", "USA" , getImageIconFlag24(flagImageName), getImageIconFlag16(flagImageName)));

        flagImageName = "NRC.png";     // todo From location of package of Race ???
        countries.add(new Country("Nantahala Racing Club", "NRC" , getImageIconFlag24(flagImageName), getImageIconFlag16(flagImageName)));
    }


    public ImageIcon getImageIcon(String countryAbbreviation) {
        ImageIcon ii = emptyII;
        for (Country c:countries) {
            if (c.getAbbreviation().compareTo(countryAbbreviation) == 0) {
                ii = c.getImageIcon();
            }
        }
        return(ii);
    }

    public ImageIcon getImageIconTiny(String countryAbbreviation) {
        ImageIcon ii = emptyII;
        for (Country c:countries) {
            if (c.getAbbreviation().compareTo(countryAbbreviation) == 0) {
                ii = c.getImageIconTiny();
            }
        }
        return(ii);
    }




    private void loadImages() {      // todo Load these ONCE !!! not on demand
        try {
            Class thisClass = getClass();
            // http://upload.wikimedia.org/wikipedia/de/d/de/TAG_Heuer_Logo.svg
            URL imgURL = thisClass.getResource("images/TAG_Heuer_Logo16.png");
            if (imgURL!=null)  {
                tagHeuerTinyII = new ImageIcon(imgURL, "TagHeuer");
            }

            imgURL = thisClass.getResource("images/TAG_Heuer_Logo25.png");
            if (imgURL!=null)  {
                tagHeuerII = new ImageIcon(imgURL, "TagHeuer");
            }

            imgURL = thisClass.getResource("images/SlalomCourseCardiffSmall.jpg");
            if (imgURL!=null)
                slalomCourseSmall = new ImageIcon(imgURL, "slalomCourse");

            imgURL = thisClass.getResource("images/upstreamGateSmall.jpg");     // todo From location of package of Race ???
            if (imgURL!=null)
                upstreamSmallII = new ImageIcon(imgURL, "upstreamSmall");

            BufferedImage image = ImageIO.read(new File("images/upstreamGateSmall.jpg"));
            Icon i = new ImageIcon(image);

            imgURL = thisClass.getResource("images/upstreamGateTiny.jpg");     // todo From location of package of Race ???
            if (imgURL!=null)
                upstreamTinyII = new ImageIcon(imgURL, "upstreamSmall");

            imgURL = thisClass.getResource("images/downstreamGateSmall.jpg");     // todo From location of package of Race ???
            if (imgURL!=null)
                downstreamSmallII = new ImageIcon(imgURL, "downStreamSmall");

            imgURL = thisClass.getResource("images/downstreamGateTiny.jpg");     // todo From location of package of Race ???
            if (imgURL!=null)
                downstreamTinyII = new ImageIcon(imgURL, "downStreamtiny");

            imgURL = thisClass.getResource("images/stopWatch25.jpg");     // todo From location of package of Race ???
            if (imgURL!=null)
                stopWatchII = new ImageIcon(imgURL, "stopWatch");
        }
        catch (Exception e) {
            log.error("BAD ImageLoad");
            log.write(e);
        }
    }


    public ImageIcon getStopWatchII() {
        return stopWatchII;
    }

    public ImageIcon getDownstreamTinyII() {
        return downstreamTinyII;
    }

    public ImageIcon getUpstreamTinyII() {
        return upstreamTinyII;
    }

    public ImageIcon getUpstreamSmallII() {
        return upstreamSmallII;
    }

    public ImageIcon getDownstreamSmallII() {
        return downstreamSmallII;
    }

    public ImageIcon getTagHeuerTinyII() {
        return tagHeuerTinyII;
    }

    public ImageIcon getTagHeuerII() {
        return tagHeuerII;
    }

    public ImageIcon getSlalomCourseSmall() {
        // http://upload.wikimedia.org/wikipedia/commons/7/78/Cardiff_International_White_Water_%282012_gates%29.svg
        // use ok per license  http://creativecommons.org/licenses/by-sa/3.0/deed.en
        return slalomCourseSmall;
    }
}
