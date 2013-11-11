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
    private transient ImageIcon slalomBackgroundII;
    private transient ImageIcon icfPenaltyDescripton;
    private transient ImageIcon icfPenaltyDiagram;

    private transient ImageIcon icfPenaltyWrongDIrection;
    private transient ImageIcon icfPenaltyWashedBackThroughGateLine;
    private transient ImageIcon icfPenaltyUpsideDown;
    private transient ImageIcon icfPenaltyIntentionallyMovedGate;
    private transient ImageIcon icfPenaltyHeadAndBoatNotInGateTogether;
    private transient ImageIcon icfPenaltyDidntGoThroughas1Unit;
    private transient ImageIcon kayakSmall;
    private transient ImageIcon racerImg;
    private transient ImageIcon raceBibSmall;

    private transient ImageIcon medalGoldSmall;
    private transient ImageIcon medalSilverSmall;
    private transient ImageIcon medalBronzeSmall;

    //fixme Need empty and blank ??? Consolidate !!!
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


            imgURL = thisClass.getResource("images/SlalomLogoYelCyan.png");     // todo From location of package of Race ???
            if (imgURL!=null)
                slalomBackgroundII = new ImageIcon(imgURL, "slalomBkg");

            imgURL = thisClass.getResource("images/ICFPenaltyLegend.png");     // todo From location of package of Race ???
            if (imgURL!=null)
                icfPenaltyDescripton = new ImageIcon(imgURL, "slalomBkg");


            imgURL = thisClass.getResource("images/ICFPenaltyDiagram.png");     // todo From location of package of Race ???
            if (imgURL!=null)
                icfPenaltyDiagram = new ImageIcon(imgURL, "slalomBkg");


            imgURL = thisClass.getResource("images/ICFPenaltyWrongDirection.png");     // todo From location of package of Race ???
            if (imgURL!=null)
                icfPenaltyWrongDIrection = new ImageIcon(imgURL, "ICFPenaltyWrongDirection");


            imgURL = thisClass.getResource("images/ICFPenaltyWashedBackThroughGateLine.png");     // todo From location of package of Race ???
            if (imgURL!=null)
                icfPenaltyWashedBackThroughGateLine = new ImageIcon(imgURL, "ICFPenaltyWashedBackThroughGateLine");

            imgURL = thisClass.getResource("images/ICFPenaltyUpsideDown.png");     // todo From location of package of Race ???
            if (imgURL!=null)
                icfPenaltyUpsideDown = new ImageIcon(imgURL, "ICFPenaltyUpsideDown");


            imgURL = thisClass.getResource("images/ICFPenaltyIntentionallyMovedGate.png");     // todo From location of package of Race ???
            if (imgURL!=null)
                icfPenaltyIntentionallyMovedGate = new ImageIcon(imgURL, "ICFPenaltyIntentionallyMovedGate");


            imgURL = thisClass.getResource("images/ICFPenaltyHeadAndBoatNotInGateTogether.png");     // todo From location of package of Race ???
            if (imgURL!=null)
                icfPenaltyHeadAndBoatNotInGateTogether = new ImageIcon(imgURL, "ICFPenaltyHeadAndBoatNotInGateTogether");

            imgURL = thisClass.getResource("images/ICFPenaltyDidntGoThroughas1Unit.png");     // todo From location of package of Race ???
            if (imgURL!=null)
                icfPenaltyDidntGoThroughas1Unit = new ImageIcon(imgURL, "ICFPenaltyDidntGoThroughas1Unit");

            imgURL = thisClass.getResource("images/kayakSmall.jpg");     // todo From location of package of Race ???
            if (imgURL!=null)
                kayakSmall = new ImageIcon(imgURL, "kayakSmall");


            imgURL = thisClass.getResource("images/racer.jpg");     // todo From location of package of Race ???
            if (imgURL!=null)
                racerImg = new ImageIcon(imgURL, "racer");

            imgURL = thisClass.getResource("images/raceBibSmall.jpg");     // todo From location of package of Race ???
            if (imgURL!=null)
                raceBibSmall = new ImageIcon(imgURL, "raceBibSmall");


            imgURL = thisClass.getResource("images/medalGold.png");     // todo From location of package of Race ???
            if (imgURL!=null)
                medalGoldSmall = new ImageIcon(imgURL, "medalGoldSmall");

            imgURL = thisClass.getResource("images/medalSilver.png");     // todo From location of package of Race ???
            if (imgURL!=null)
                medalSilverSmall = new ImageIcon(imgURL, "medalSilverSmall");

            imgURL = thisClass.getResource("images/medalBronze.png");     // todo From location of package of Race ???
            if (imgURL!=null)
                medalBronzeSmall = new ImageIcon(imgURL, "medalBronzeSmall");

            // todo implement Mediamanager lazy load
            //http://vectormagic.com/home - make vector images out of bitmaps

        }
        catch (Exception e) {
            log.error("BAD ImageLoad");
            log.write(e);
        }
    }

    public ImageIcon getIcfPenaltyWrongDirection() {
        return icfPenaltyWrongDIrection;
    }

    public ImageIcon getIcfPenaltyWashedBackThroughGateLine() {
        return icfPenaltyWashedBackThroughGateLine;
    }

    public ImageIcon getIcfPenaltyUpsideDown() {
        return icfPenaltyUpsideDown;
    }

    public ImageIcon getIcfPenaltyIntentionallyMovedGate() {
        return icfPenaltyIntentionallyMovedGate;
    }

    public ImageIcon getIcfPenaltyHeadAndBoatNotInGateTogether() {
        return icfPenaltyHeadAndBoatNotInGateTogether;
    }

    public ImageIcon getIcfPenaltyDidntGoThroughas1Unit() {
        return icfPenaltyDidntGoThroughas1Unit;
    }

    public ImageIcon getIcfPenaltyDescripton() {
        return icfPenaltyDescripton;
    }

    public ImageIcon getIcfPenaltyDiagram() {
        return icfPenaltyDiagram;
    }

    public ImageIcon getSlalomBackgroundII() {
        return slalomBackgroundII;
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

    public ImageIcon getKayakSmall() {
        return kayakSmall;
    }
// todo add to test bucket


    public ImageIcon getRaceBibSmall() {
        return raceBibSmall;
    }

    public ImageIcon getRacerImg() {
        return racerImg;
    }

    public ImageIcon getMedalGoldSmall() {
        return medalGoldSmall;
    }

    public ImageIcon getMedalSilverSmall() {
        return medalSilverSmall;
    }

    public ImageIcon getMedalBronzeSmall() {
        return medalBronzeSmall;
    }

}
