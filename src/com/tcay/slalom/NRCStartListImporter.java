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
 * Created by allen on 10/8/16.
 */
public class NRCStartListImporter {


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
        race.setName("Imported Race");

        try {
            BufferedReader reader = new BufferedReader(new FileReader("import.txt"));
            String first;
            String last;
            String bib;
            String club;
            String ageGroup;
            String sex;
            String /*Buffer*/ boatClass;

            while ((line = reader.readLine()) != null) {


                first = null;
                last = null;
                bib = "";
                club = "USA";
                ageGroup = "";
                sex = "M";
                boatClass = null;new StringBuffer();

                String s;
                StringTokenizer st = new StringTokenizer(line, ",\t");
                int i = 0;

                while(st.hasMoreTokens()) {
                    i++;
                    s = st.nextToken();

                    switch (i) {

                        case 2:
                            boatClass = s.trim();
                            break;




                        case 1:
                            bib = s.trim();
                            break;

                        case 3:
                            first = s.trim();
                            break;

                        case 4:
                            last = s.trim();
                            break;


                        case 5:
                            if (s.contains("F"))
                                sex = "F";
                            else sex = "M";
                            break;






                        case 6:
                            String tok;
                            StringTokenizer stName = new StringTokenizer(s, " (/");
                            if (stName.hasMoreTokens()) {
                                tok = stName.nextToken();
                                ageGroup = tok;
                            }
                            else ageGroup = "";
                            break;

                       // case 7:
                       //     boatClass.append(s);
                       //     break;

                        //case 8:
                        //    if (s.toLowerCase().contains("rec")) {
                        //        boatClass.append(" Rec");
                        //    }
                        //    break;

                        default:
                            break;

                    }

                }


                if ( !bib.toLowerCase().contains("bib")) {
                    if (last != null && first != null && (last.length() > 0  || first.length() > 0)) {
                        if (i >= 4  && first.compareTo("0.00") != 0) {
                            Racer racer = new Racer(bib, last, first, sex, club, ageGroup);
                            race.addBoat(racer, boatClass );//://.toString());
                        }
                        else {
                            log.error("Invalid Format in NRC CSV file \"" + line + "\", " + "expected bib, first, last, sex, agegroup, boattype, super class (rec or race)");

                        }
                    }
                }
            }
        }
        catch (Exception e) {
            log.write(e);
        }
    }


    public static void main(String[] args) {
        NRC_StartListImporter2016August importer = new NRC_StartListImporter2016August();
        importer.readImportFile();

    }


}
