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
    public static final int    NBR_GATES = 21;
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
            race.addBoat( new Racer("2",   "SCHUBERT",    "Sebastian", "M","GER"), boatClass);
            race.addBoat( new Racer("3",   "DOERFLER",    "Fabian",	   "M","GER"), boatClass);
            race.addBoat( new Racer("5",   "KAUZER",      "Peter",	   "M","SLO"), boatClass);


            race.addBoat( new Racer("6",   "MOLMENTI",    "Daniele",   "M","ITA"), boatClass);
            race.addBoat( new Racer("7",   "AIGNER",      "Hannes",	   "M","GER"), boatClass);
            race.addBoat( new Racer("8",   "BOECKELMANN", "Paul",	   "M","GER"), boatClass);
            race.addBoat( new Racer("9",   "NEVEU",       "Boris",	   "M","FRA"), boatClass);
            race.addBoat( new Racer("10",  "HRADILEK",    "Vavrinec",  "M","CZE"), boatClass);

            boatClass = "K1W";
/*            race.addBoat( new Racer("70",  "LIEBFARTH",  "Evy",     "F", "NRC"), boatClass);
            race.addBoat( new Racer("71",  "DAVIS",      "Avery",   "F", "NRC"), boatClass);
            race.addBoat( new Racer("72",  "VANHA",      "Zuzana",  "F", "NRC"), boatClass);
*/
            race.addBoat( new Racer("30",  "HILGERTOVA", "Stepanka","F", "CZE"), boatClass);
            race.addBoat( new Racer("31",  "FER",        "Emilie",  "F", "FRA"), boatClass);
            race.addBoat( new Racer("32",  "CHOURRAUT",  "Maialen", "F", "ESP"), boatClass);
            race.addBoat( new Racer("33",  "DUKATOVA",   "Jana",    "F", "SVK"), boatClass);
            race.addBoat( new Racer("34",  "FOX",        "Jessica", "F", "AUS"), boatClass);
            race.addBoat( new Racer("35",  "KUHNLE",     "Corinna", "F", "AUT"), boatClass);
            race.addBoat( new Racer("36",  "KUDEJOVA",   "Katerina","F", "CZE"), boatClass);
            race.addBoat( new Racer("38",  "KRAGELJ",    "Ursa",    "F", "SLO"), boatClass);
            race.addBoat( new Racer("39",  "POESCHEL",   "Cindy",   "F", "GER"), boatClass);
            race.addBoat( new Racer("40",  "TERCELJ",    "Eva",     "F", "SLO"), boatClass);
            race.addBoat( new Racer("41",  "SCHORNBERG", "Jasmin",  "F", "GER"), boatClass);
            race.addBoat( new Racer("42",  "PFEIFER",    "Melanie", "F", "GER"), boatClass);



            boatClass = "C1";
            race.addBoat( new Racer("21", "ESTANGUET",      "Tony",	    "M", "FRA"), boatClass);
            race.addBoat( new Racer("22", "FLORENCE",       "David",	"M", "GBR"), boatClass);
            race.addBoat( new Racer("23", "BENUS",          "Matej",	"M", "SVK"), boatClass);


            race.addBoat( new Racer("24", "SLAFKOVSKY",     "Alexander","M","SVK"), boatClass);

            race.addBoat( new Racer("25", "TASIADIS",       "Sideris",	"M","GER"), boatClass);
            race.addBoat( new Racer("26", "GARGAUD CHANUT", "Denis",	"M", "FRA"), boatClass);
            race.addBoat( new Racer("27", "BERCIC",         "Anze",	    "M", "SLO"), boatClass);




/*
            race.addBoat( new Racer("100",  "PFEIFER",    "Aelanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("101",  "PFEIFER",    "Belanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("102",  "PFEIFER",    "Celanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("103",  "MORE",    "Delanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("104",  "GAAP",    "Eelanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("105",  "SNAP",    "Felanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("106",  "DEAND",    "Gelanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("107",  "ORORKE",    "Helanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("108",  "PELLY",    "Ielanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("109",  "JOPES",    "Jelanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("100",  "MANN",    "Kelanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("110",  "DORMAN",    "Lelanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("111",  "PO",    "Nelanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("112",  "GORD",    "Oelanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("113",  "PINEE",    "Pelanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("114",  "REWA",    "Qelanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("115",  "FORTNER",    "Relanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("116",  "LAW",    "Selanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("117",  "SMEDLEY",    "Telanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("118",  "TANKS",    "Uelanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("119",  "BORWA",    "Velanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("120",  "GORDON",    "Welanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("121",  "APFEIFER",    "Xelanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("122",  "APFEIFER",    "Yelanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("123",  "JONES",    "Aelanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("124",  "JONES",    "Belanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("125",  "JONES",    "Celanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("126",  "JONES",    "Delanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("127",  "JONES",    "Eelanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("128",  "JONES",    "Felanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("129",  "JONES",    "Gelanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("130",  "JONES",    "Helanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("131",  "JONES",    "Ielanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("132",  "JONES",    "Jelanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("133",  "JONES",    "Kelanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("134",  "HOOD",    "Delanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("135",  "HOOD",    "Eelanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("136",  "HOOD",    "Felanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("137",  "HOOD",    "Gelanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("138",  "HOOD",    "Helanie", "F", "GER"), boatClass);
            race.addBoat( new Racer("139",  "HOOD",    "Kelanie", "F", "GER"), boatClass);
*/


        } catch (DuplicateBibException e) {
            String msg = e.getMessage();
            log.error(msg);
        }

        race.setName(RACE_NAME);
        race.setNbrGates(NBR_GATES);
        race.setUpstreamGates(UPSTREAMS);
        race.setLocation(LOCATION);
//        race.setTagHeuerEmulation(true);
        race.setIcfPenalties(true);

        JudgingSection section = new JudgingSection(1, 1,5);
        race.addSection(section);
        section = new JudgingSection(2, 6,9);
        race.addSection(section);
        section = new JudgingSection(3, 10,12);
        race.addSection(section);
        section = new JudgingSection(4, 13,17);
        race.addSection(section);
        section = new JudgingSection(5, 18,21);
        race.addSection(section);
        race.saveSerializedData();
    }

    public static void main(String[] args) {
        TestData td = new TestData();
        td.load();
        td.simulateRace();
    }



}
