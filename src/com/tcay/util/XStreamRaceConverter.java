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

package com.tcay.util;

import com.tcay.slalom.BoatEntry;
import com.tcay.slalom.Penalty;
import com.tcay.slalom.Race;
import com.tcay.slalom.RaceRun;
import com.tcay.slalom.UI.JudgingSection;
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

            writer.startNode("name");
            writer.setValue(race.getName());
            writer.endNode();

               writer.startNode("date");
               writer.setValue(race.getDate().toString());
               writer.endNode();

            writer.startNode("location");
            writer.setValue(race.getLocation());
            writer.endNode();

            writer.startNode("nbrGates");
            writer.setValue(race.getNbrGates().toString());
            writer.endNode();



            writer.startNode("judgingSections");
            for (JudgingSection section:race.getSections()) {
                writer.startNode("section");

                writer.startNode("number");
                writer.setValue(section.getSectionNbr().toString());
                writer.endNode();


                writer.startNode("startGate");
                writer.setValue(Integer.toString(section.getFirstGate()));
                writer.endNode();

                writer.startNode("endGate");
                writer.setValue(Integer.toString(section.getLastGate()));
                writer.endNode();

                writer.endNode();



            }
            writer.endNode();





            writer.startNode("startList");

            for (BoatEntry b:race.getRemainingStartList()) {
                writer.startNode("boat");


                writer.startNode("class");
                writer.setValue(b.getBoatClass());
                writer.endNode();


                writer.startNode("bib");
                writer.setValue(b.getRacer().getBibNumber());
                writer.endNode();

                writer.startNode("first");
                writer.setValue(b.getRacer().getFirstName());
                writer.endNode();

                writer.startNode("last");
                writer.setValue(b.getRacer().getLastName());
                writer.endNode();

                writer.startNode("club");
                writer.setValue(b.getRacer().getClub());
                writer.endNode();




                writer.endNode();


            }


            writer.endNode();


            writer.startNode("runsCompleted");

            for (RaceRun run:race.getCompletedRuns()) {
                writer.startNode("RaceRun");

                writer.startNode("bib");
                writer.setValue(run.getBoat().getRacer().getBibNumber());
                writer.endNode();

                writer.startNode("runNbr");
                writer.setValue(Integer.toString(run.getRunNumber()));
                writer.endNode();


                writer.startNode("Time");

                writer.startNode("Start");
                writer.setValue(Long.toString(run.getStopWatch().getStartTime()));
                writer.endNode();

                writer.startNode("End");
                writer.setValue(Long.toString(run.getStopWatch().getEndTime()));
                writer.endNode();


                writer.startNode("Penalties");

                for (Penalty p:run.getPenaltyList()) {
                    writer.startNode("Penalty");
                    writer.startNode("Gate");
                    writer.setValue(p.getGate().toString());
                    writer.endNode();
                    writer.startNode("Secs");
                    writer.setValue(p.getPenaltySeconds().toString());
                    writer.endNode();
                    writer.endNode();
                }

                writer.endNode();





                writer.startNode("RawTime");
                writer.setValue(run.getResultString());
                writer.endNode();


                writer.startNode("TotalTime");
                writer.setValue(run.getTotalTimeString());
                writer.endNode();





                writer.endNode();

                writer.endNode();



            }



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
