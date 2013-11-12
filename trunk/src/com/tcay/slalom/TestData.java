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

package com.tcay.slalom;

import com.tcay.slalom.UI.JudgingSection;
import com.tcay.slalom.UI.RaceTimingUI;
import com.tcay.slalom.UI.SlalomApp;
import com.tcay.util.DuplicateBibException;
import com.tcay.util.Log;
import com.tcay.slalom.UI.SlalomApp;

import java.util.Arrays;
import java.util.List;

/**
 * ${PROJECT_NAME}
 *
 * Teton Cay Group Inc. ${YEAR}
 *

 * User: allen
 * Date: 8/28/13
 * Time: 9:56 PM
 */
public class TestData {

    private static Log log = Log.getInstance();
    public static final String LOCATION = "Virtual Augsburg";
    public static final String RACE_NAME = "SROA Test Race 2";
    public static final int    NBR_GATES = 18;
    public static final List<Integer> UPSTREAMS = Arrays.asList(3,7,9,12,14,16);

    public void simulateRace() {
        SlalomApp app = SlalomApp.getInstance();

        RaceTimingUI timingUI = app.createAndDisplayTimingPanel();
        timingUI.simulateRace();
    }



    public void load() {
        Race race = Race.getInstance();
        race.clearRace();
        String boatClass;

        boatClass = "K1";
        try {
        race.addBoat( new Racer("1",   "DAILLE",      "Etienne",   "M","FRA"), boatClass);
    //            race.addBoat( new Racer("2",   "SCHUBERT",    "Sebastian", "M","GER"), boatClass);
            race.addBoat( new Racer("2",   "HUFF",        "Spencer",   "M","USA"), boatClass);
            race.addBoat( new Racer("3",   "DOERFLER",    "Fabian",	   "M","GER"), boatClass);
            race.addBoat( new Racer("4",   "DAVIDSON",    "Drew",      "M","USA"), boatClass);
    //            race.addBoat( new Racer("5",   "KAUZER",      "Peter",	   "M","SLO"), boatClass);
            race.addBoat( new Racer("5",   "RUDNITSKI",   "Michael",   "M","USA"), boatClass);
            //race.addBoat( new Racer("6",   "MOLMENTI",    "Daniele",   "M","ITA"), boatClass);
            race.addBoat( new Racer("6",   "McEWAN",      "Devin",   "M","USA"), boatClass);
            race.addBoat( new Racer("7",   "AIGNER",      "Hannes",	   "M","GER"), boatClass);
            race.addBoat( new Racer("8",   "BOECKELMANN", "Paul",	   "M","GER"), boatClass);
            race.addBoat( new Racer("9",   "NEVEU",       "Boris",	   "M","FRA"), boatClass);
            race.addBoat( new Racer("10",  "HRADILEK",    "Vavrinec",  "M","CZE"), boatClass);

            boatClass = "K1W";
            race.addBoat( new Racer("27",  "LIEBFARTH",  "Evy",     "F", "USA"), boatClass);
            race.addBoat( new Racer("28",  "DAVIS",      "Avery",   "F", "USA"), boatClass);
            race.addBoat( new Racer("29",  "VANHA",      "Zuzana",  "F", "USA"), boatClass);
            race.addBoat( new Racer("30",  "HILGERTOVA", "Stepanka","F", "CZE"), boatClass);
            race.addBoat( new Racer("31",  "FER",        "Emilie",  "F", "FRA"), boatClass);
            race.addBoat( new Racer("32",  "CHOURRAUT",  "Maialen", "F", "ESP"), boatClass);
            race.addBoat( new Racer("33",  "DUKATOVA",   "Jana",    "F", "SVK"), boatClass);
            race.addBoat( new Racer("34",  "FOX",        "Jessica", "F", "AUS"), boatClass);
            race.addBoat( new Racer("35",  "KUHNLE",     "Corinna", "F", "AUT"), boatClass);
/*            race.addBoat( new Racer("36",  "KUDEJOVA",   "Katerina","F", "CZE"), boatClass);
            race.addBoat( new Racer("38",  "KRAGELJ",    "Ursa",    "F", "SLO"), boatClass);
            race.addBoat( new Racer("39",  "POESCHEL",   "Cindy",   "F", "GER"), boatClass);
            race.addBoat( new Racer("40",  "TERCELJ",    "Eva",     "F", "SLO"), boatClass);
            race.addBoat( new Racer("41",  "SCHORNBERG", "Jasmin",  "F", "GER"), boatClass);
            race.addBoat( new Racer("42",  "PFEIFER",    "Melanie", "F", "GER"), boatClass);
*/


            boatClass = "C1";
            race.addBoat( new Racer("21", "ESTANGUET",      "Tony",	    "M", "FRA"), boatClass);
            race.addBoat( new Racer("22", "FLORENCE",       "David",	"M", "GBR"), boatClass);
            race.addBoat( new Racer("23", "BENUS",          "Matej",	"M", "SVK"), boatClass);
            race.addBoat( new Racer("24", "SLAFKOVSKY",     "Alexander","M","SVK"), boatClass);
            race.addBoat( new Racer("25", "TASIADIS",       "Sideris",	"M","GER"), boatClass);
            race.addBoat( new Racer("26", "GARGAUD CHANUT", "Denis",	"M", "FRA"), boatClass);
            race.addBoat( new Racer("27", "BERCIC",         "Anze",	    "M", "SLO"), boatClass);


        } catch (DuplicateBibException e) {
            String msg = e.getMessage();
            log.error(msg);
        }

        race.setName(RACE_NAME);
        race.setNbrGates(NBR_GATES);
        race.setUpstreamGates(UPSTREAMS);
        race.setLocation(LOCATION);
        race.setTagHeuerEmulation(true);
        race.setIcfPenalties(true);

        JudgingSection section = new JudgingSection(1, 1,6);
        race.addSection(section);
        section = new JudgingSection(2, 7,12);
        race.addSection(section);
        section = new JudgingSection(3, 13,18);
        race.addSection(section);
        race.saveSerializedData();
    }

    public static void main(String[] args) {
        TestData td = new TestData();
        td.load();
        td.simulateRace();
    }



}
