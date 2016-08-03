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

import com.tcay.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * ${PROJECT_NAME}
 *
 * Teton Cay Group Inc. ${YEAR}
 *

 * User: allen
 * Date: 9/27/13
 * Time: 8:47 AM
 *
 */
public class USNWC_StartListImporter {


    // Hand import prep, remove all columns not used,  remove title row,  save as MS DOS delimited file

    Log log;

    {
        log = Log.getInstance();
    }


    public void readImportFile() {
        String line = null;

        ArrayList<BoatEntry> boats = new  ArrayList<BoatEntry>();
        Race race = Race.getInstance();
        race.clearRace();
        race.setName("USNWC Race");

        try {
            BufferedReader reader = new BufferedReader(new FileReader("USNWC.csv"));
            String first;
            String last;
            String bib;
            String club;
            String ageGroup;
            String dob;
            String sex;
            String boatClass;
            String email;
            String age;
            int lineCount = 0;

            while ((line = reader.readLine()) != null) {
                 lineCount++;

                 first = null;
                 last = null;
                 bib = "";
                 club = "USA";
                 ageGroup = "";
                age = "";
                 sex = "M";
                 boatClass = "";
                email = "";
                dob = "";

                String s;
                StringTokenizer st = new StringTokenizer(line, ",\t");  /// TODO RIO take care of missing tokens with ",,"
                                                                         //// no data between the ","'s
                int i = 0;

                while(st.hasMoreTokens()) {
                    i++;
                    s = st.nextToken();

                    switch (i) {
                        case 1:
                            bib = s;
                            break;
                        case 2:
                            first = s;
                            break;
                        case 3:
                            last=s;
                            break;
                        case 4:
                            boatClass = s;
                            if (boatClass.contains("M")) {

                                sex = "M";

                            } else {
                                sex = "W";

                            }

                            break;


/*  OLDER FORMAT FROM FERGUS

                        case 4:
                            sex  = s.substring(1);

//                            ageGroup = s;
//                            bib = s.trim();
                            break;
                        case 5:
                            dob = s;
                            break;
                        case 6:
                            age = s;
                            break;
                        case 7:
                            email = s;
                            break;
                        case 8:
                            boatClass = s;
                            break;

                        case 9:
                            ageGroup = s;
                            if (ageGroup.contains(".")) {
                                club = "CAN";
                            }
                            break;

*/

                        default:
                            break;

                    }

                }

                if (lineCount == 1 && (first.toUpperCase().contains("FIRST")||first.toUpperCase().contains("LAST"))) /// skip column header line
                    continue;

                if (last != null && first != null) {
                    Racer racer = new Racer(bib, last, first, sex, club, ageGroup);
//                    BoatEntry boat = new BoatEntry(racer, boatClass);
//                boats.add(boat);
                    race.addBoat(racer, boatClass);

                }
            }
        }
        catch (Exception e) {
            log.write(e);
        }
    }


    public static void main(String[] args) {
        USNWC_StartListImporter importer = new USNWC_StartListImporter();
        importer.readImportFile();

    }

}
