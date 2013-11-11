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
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 9/27/13
 * Time: 8:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class NRC_StartListImporter {


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
            String boatClass;

            while ((line = reader.readLine()) != null) {


                 first = null;
                 last = null;
                 bib = "";
                 club = "USA";
                 ageGroup = "";
                 sex = "M";
                 boatClass = "";

                String s;
                StringTokenizer st = new StringTokenizer(line, ",\t");
                int i = 0;
                while(st.hasMoreTokens()) {
                    i++;
                    s = st.nextToken();

                    switch (i) {
                        case 1:
                            boatClass = s;
                            if (boatClass.contains("W"))
                                sex = "F";
                            break;
                        case 2:
                            ageGroup = s;
                            break;
                        case 3:
                            bib = s;
                            break;
                        case 4:
                            //String name = s;
                            String tok;
                            StringTokenizer stName = new StringTokenizer(s, " /");
                            int j = 0;
                            while(stName.hasMoreTokens()) {
                                j++;
                                tok = stName.nextToken();
                                switch(j) {
                                    case 1:
                                        first = tok;
                                        break;
                                    case 2:
                                        last = tok;
                                        break;
                                    default:              // todo this is kludge to get Last when 3 or 4 part name 'Anna Marie Fergerson"
                                        last = last + " "   + tok;
                                        break;

                                }
                            }

                            break;

                        default:
                            break;

                    }

                }


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
        NRC_StartListImporter importer = new NRC_StartListImporter();
        importer.readImportFile();

    }

}
