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

package com.tcay.slalom.UI.http;

import com.tcay.slalom.BoatEntry;
import com.tcay.slalom.Race;
import com.tcay.slalom.RaceRun;
import com.tcay.slalom.Result;
import com.tcay.slalom.UI.tables.ResultsTable;
import com.tcay.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * Created by allen on 3/31/16.
 */
public class SlalomRacerResultsHTTP {

    static final StringBuffer webColumnHeader = new StringBuffer();
    static final StringBuffer webColumnGroupWidths = new StringBuffer();
    private Log log;
    private boolean inited = false;



    // object initialization
    public SlalomRacerResultsHTTP()  {   // app instantion time initialize header  //todo make compile time & verify
        log = Log.getInstance();


    }



    private void init() {

        if (webColumnHeader.length()==0) {

            webColumnHeader.append(


                    "<tr>" +
                    "<td>Run</td>" +
                    "<td>Bib</td>" +
                    "<td>Name</td>" +
                    "<td>Raw</td>" +
                    "<td>Pen</td>" +

                    Race.getInstance().getHTTPGateHeadersString() +


                    "<td>Total</td>" +
                    "</tr>");

        }

        if (webColumnGroupWidths.length()==0) {

            webColumnGroupWidths.append("<colgroup>" +
                    "<col span=\"1\" style=\"width: 7%;\">" +    //rank
                    "<col span=\"1\" style=\"width: 8%;\">" +    /// bib
                    "<col span=\"1\" style=\"width: 30%;\">" +   // name

                    Race.getInstance().getHTTPGateHeadersWidthString() +


                    "<col span=\"1\" style=\"width: 10%;\">" +   //raw run
                    "<col span=\"1\" style=\"width: 30%;\">" +    /// pen
                    "<col span=\"1\" style=\"width: 10%;\">" +   //  tot

                    "</colgroup>");
        }
        inited = true;
    }


    private void outputWebColumnGroup(BufferedWriter output) {
        try {
            output.write(webColumnGroupWidths.toString());
        } catch (Exception e) {
            log.write(e);
        }


    }

    private void outputWebColumnHeader(BufferedWriter output) {
        try {

            output.write("<a href=\"index.html\">");// + r.getBoat().getBoatClass());
            output.write("Full Results</a>");


            output.write(webColumnHeader.toString());
            output.newLine();

        } catch (Exception e) {
            log.write(e);
        }

    }


    private String ahref(String wrapMe) {
        StringBuffer sb = new StringBuffer();
        sb.append("<a href=\"#");

        sb.append("\">");
        return sb.toString();
    }

    public void outputWeb(BoatEntry boat) {
        ArrayList list = new ArrayList();
        Race race = Race.getInstance();

        for (RaceRun raceRun:race.getCompletedRuns()) {
            if (boat.getRacer().getBibNumber().equals(raceRun.getBoat().getRacer().getBibNumber())) {
                list.add(raceRun);
            }
        }

        for (RaceRun raceRun:race.getActiveRuns()) {
            if (boat.getRacer().getBibNumber().equals(raceRun.getBoat().getRacer().getBibNumber())) {
                list.add(raceRun);
            }
        }
        outputWeb(list);



    }

    public void outputWeb(File file) {


    }



    //public void outputWebForHTML_Scrolling(RaceRun run) {
    public void outputWeb(ArrayList<RaceRun> runs) {


        //    log.info("\n" + run.getBoat().getRacer().getShortName() + " Web Results to HTML");
        BufferedWriter output = null;
        StringBuffer links = new StringBuffer();


        if (!inited) {
            init();
        }


        try {
            File file = new File("/Library/Webserver/Documents/" + runs.get(0).getBoat().getRacer().getBibNumber() + ".html");
            output = new BufferedWriter(new FileWriter(file));


            output.write("<!DOCTYPE html>");
            output.write("<html>");
            output.write("<head>");
            output.write("<meta http-equiv=\"refresh\" content=\"10\" >");

///was BG efefef
            output.write(" <style> .odd {" +
                    "background-color: #B0EDEA;" +

                    "}" +

                    ".even {" +
                    "background-color: #ffffff;" +
                    "}            </style>");

            output.write("</head>");
            output.write("<body>");

            // Write links to classes
            output.write("<div id=\"button\">");

            // Accumulate all the classes that are now have at leat 1 result


            // links.append("<tr>");
            links.append("<td colspan=\"8\">");

          //  if (firstTime) {
          //      //links.append("<td>");

//                links.append("<a href=\"#Top\">");
//                links.append("Home");
//                links.append("</a></td>");


                // links.append("<div>");
//                firstTime=false;
//            }
            //links.append("</tr>\n");

            //  if (firstTime) {
            //      output.write(links.toString());
            //      firstTime=false;

//            }
            output.write("</div>");

            output.write("<table width=\"100%\">");//div class=\"table\">");


            outputWebColumnGroup(output);
            outputWebColumnHeader(output);








//        } catch (Exception e) {

//        }

            for (RaceRun run : runs) {


                float totalTime = 0;

                totalTime = (float) run.getTotalPenalties();
                totalTime += run.getElapsed();

                //  log.info(String.format("HTTP RacerRun Run# %1s ", run.getRunNumber()) +
                //          String.format("%3s ", run.getBoat().getBoatClass()) +
                //          String.format("%-25s ", run.getBoat().getRacer().getShortName()) +
                //          String.format("%7s ", run.getResultString()) +
                //          String.format("+%-3s ", run.getTotalPenalties()) +
                //          String.format("%8.2f", totalTime));


               // StringBuffer links = new StringBuffer();
                boolean firstTime = true;
//                try {
                //              File file = new File("/Library/Webserver/Documents/"+run.getBoat().getRacer().getBibNumber()+".html");
                //              output = new BufferedWriter(new FileWriter(file));
                ///////
                int rank = 1;

                output.write("<tr><td></td></tr>");
                output.newLine();

                output.write("<tr></tr>");

                output.write("<tr class=" + "\"" + (rank % 2 == 0 ? "even" : "odd") + "\">");
                //output.write("<td></td>");

                output.write("<td>" + run.getRunNumber() + "</td>");
                output.write("<td>" + run.getBoat().getRacer().getBibNumber() + "</td>");
                output.write("<td>" + run.getBoat().getRacer().getShortName() + "</td>");
                output.write("<td>" + run.getElapsed() + "</td>");


                String pen = run.penaltyStringHTMLExtended();
                output.write("<td>" + pen + "</td>");
                //            output.write("<td>"+run.getTotalPenalties()+"</td>");
                output.write("<td>" + run.getTotalTimeString() + "</td>");

                output.write("</tr>");

                output.newLine();

                //output.write( links.toString());


                rank = 1;

                // output.write("<div  class=\"td\">");


                //                output.write(r.getBoat().getBoatClass() + ", ");
                String s1;
                //output.write("<tr>");

                output.newLine();


//                } catch (Exception e) {
//                    log.write(e);
//                } finally {
//                    try {
//                        output.close();
//                    } catch (Exception ex) {
//                    }
//                }
            }
        } catch (Exception e) {
            log.write(e);
        } finally {
        try {
            output.write("</table>");//div>");
            output.write("</body>");
            output.write("</html>");

            output.close();
            } catch (Exception ex) {
            }
        }

    }

/*
    public void outputWebForHTML_Scrolling(RaceRun run) {


        float totalTime;

        totalTime = (float) run.getTotalPenalties();
        totalTime += run.getElapsed();

        //  log.info(String.format("HTTP RacerRun Run# %1s ", run.getRunNumber()) +
        //          String.format("%3s ", run.getBoat().getBoatClass()) +
        //          String.format("%-25s ", run.getBoat().getRacer().getShortName()) +
        //          String.format("%7s ", run.getResultString()) +
        //          String.format("+%-3s ", run.getTotalPenalties()) +
        //          String.format("%8.2f", totalTime));


        BufferedWriter output = null;
        StringBuffer links = new StringBuffer();
        boolean firstTime = true;
        try {
            File file = new File("/Library/Webserver/Documents/"+run.getBoat().getRacer().getBibNumber()+".html");
            output = new BufferedWriter(new FileWriter(file));


            output.write("<!DOCTYPE html>");
            output.write("<html>");
            output.write("<head>");
            output.write("<meta http-equiv=\"refresh\" content=\"10\" >");

///was BG efefef
            output.write(" <style> .odd {" +
                    "background-color: #B0EDEA;" +

                    "}" +

                    ".even {" +
                    "background-color: #ffffff;" +
                    "}            </style>");

            output.write("</head>");
            output.write("<body>");

            // Write links to classes
            output.write("<div id=\"button\">");

            // Accumulate all the classes that are now have at leat 1 result


            // links.append("<tr>");
            links.append("<td colspan=\"8\">");

            if (firstTime) {
                //links.append("<td>");

                links.append("<a href=\"#Top\">");
                links.append("Home");
                links.append("</a></td>");


                // links.append("<div>");
//                firstTime=false;
            }
            //links.append("</tr>\n");

            //  if (firstTime) {
            //      output.write(links.toString());
            //      firstTime=false;

//            }
            output.write("</div>");

            output.write("<table width=\"100%\">");//div class=\"table\">");


            outputWebColumnGroup(output);
            outputWebColumnHeader(output);

            ///////
            int rank = 1;



            output.write("<tr><td></td></tr>");
            output.newLine();

            output.write("<tr></tr>");

            output.write("<tr class=" + "\"" + (rank % 2 == 0 ? "even" : "odd") + "\">");
            //output.write("<td></td>");

            output.write("<td>"+run.getRunNumber()+"</td>");
            output.write("<td>"+run.getBoat().getRacer().getBibNumber()+"</td>");
            output.write("<td>"+run.getBoat().getRacer().getShortName()+"</td>");
            output.write("<td>"+run.getElapsed()+"</td>");







            String pen = run.penaltyStringHTMLExtended();
            output.write("<td>"+pen+"</td>");
//            output.write("<td>"+run.getTotalPenalties()+"</td>");
            output.write("<td>"+run.getTotalTimeString()+"</td>");

            output.write("</tr>");

            output.newLine();

            //output.write( links.toString());


            rank = 1;

            // output.write("<div  class=\"td\">");


//                output.write(r.getBoat().getBoatClass() + ", ");
            String s1;
            //output.write("<tr>");

            output.newLine();

            output.write("</table>");//div>");
            output.write("</body>");
            output.write("</html>");

            output.close();
        } catch (Exception e) {
            log.write(e);
        } finally {
            try {
                output.close();
            } catch (Exception ex) {
            }
        }

    }
    */
}
