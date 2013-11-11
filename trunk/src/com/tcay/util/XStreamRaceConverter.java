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

package com.tcay.util;

import com.tcay.slalom.Race;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * SlalomScoring
 * Teton Cay Group Inc. 2013
 * <p/>
 * User: allen
 * Date: 11/6/13
 * Time: 2:17 PM
 */
public class XStreamRaceConverter implements Converter {

        public boolean canConvert(Class clazz) {
            return Race.class == clazz;
        }


///http://xstream.codehaus.org/converter-tutorial.html


        public void marshal(Object value, HierarchicalStreamWriter writer,
                            MarshallingContext context) {

            Race race  = (Race)value;
            writer.startNode("race");

               writer.startNode("date");
               writer.setValue(race.getDate().toString());
               writer.endNode();

            writer.startNode("location");
            writer.setValue(race.getLocation());
            writer.endNode();

            writer.startNode("nbrGates");
            writer.setValue(race.getNbrGates().toString());
            writer.endNode();


//            private List<Integer> upstreamGates;
//            private ArrayList<JudgingSection> judgingSections;
//            private List<Racer> racers;
//            private boolean tagHeuerEmulation = false;

            // Race Status
//            private RaceRun pendingRerun;// = null;
//            private List<BoatEntry> startList = null;     // todo on Crash recovery don't have an yet-to-be-started list, only all racers
//            private ArrayList<RaceRun> activeRuns = null;
//            private long runsStartedOrCompletedCnt = 0;
//            private int currentRunIteration;  // are we on 1st runs, or 2nd runs ?
//            private ArrayList<RaceRun> completedRuns = null;




            writer.endNode();


//            writer.addAttribute("gender", Character.toString(birthday.getGender()));
//            writer.startNode("person");
//            context.convertAnother(birthday.getPerson());
//            writer.endNode();
//            writer.startNode("birth");
//            context.convertAnother(birthday.getDate());
//            writer.endNode();
        }

        public Object unmarshal(HierarchicalStreamReader reader,
                                UnmarshallingContext context) {

              Race race  = Race.getInstance();

              String s = reader.getAttribute("date");

//            Birthday birthday = new Birthday();
//            if (reader.getAttribute("gender").charAt(0) == 'm') {
//                birthday.setGenderMale();
//            } else {
//                birthday.setGenderFemale();
//            }
//            reader.moveDown();
//            Person person = (Person)context.convertAnother(birthday, Person.class);
//            birthday.setPerson(person);
//            reader.moveUp();
//            reader.moveDown();
//            Calendar date = (Calendar)context.convertAnother(birthday, Calendar.class);
//            birthday.setDate(date);
//            reader.moveUp();
//            return birthday;


            return null;
        }

}
