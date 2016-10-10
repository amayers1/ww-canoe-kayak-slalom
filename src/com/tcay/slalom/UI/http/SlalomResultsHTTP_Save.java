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

import com.tcay.slalom.Race;
import com.tcay.slalom.RaceRun;
import com.tcay.slalom.Result;
import com.tcay.slalom.UI.tables.ResultsTable;
import com.tcay.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by allen on 10/4/16.
 */
public class SlalomResultsHTTP_Save {


    static final StringBuffer webColumnHeader = new StringBuffer();
    static final StringBuffer webColumnHeader1 = new StringBuffer();

    static final StringBuffer webColumnGroupWidths = new StringBuffer();
    private Log log;
    private boolean scroll = true;
/// Scroll in client browser Using http://www.jqueryscript.net/animation/jQuery-Plugin-For-Infinite-Any-Content-Scroller-scrollForever.html


    // object initialization
    {   // app instantion time initialize header  //todo make compile time & verify

        log = Log.getInstance();

        if (webColumnHeader.length()==0) {
            webColumnHeader.append( tableEmptyRow() + "\n");
            webColumnHeader.append("<tr>" +
                    "<td>Rank</td>" +
                    "<td>Bib</td>" +
                    "<td>Name</td>" +
                    "<td>Run1</td>" +
                    "<td>Pen</td>" +
                    "<td>Total</td>" +
                    "<td>Run2</td>" +
                    "<td>Pen</td>" +
                    "<td>Total</td>" +
                    "<td>Best</td>" +
                    "</tr>\n");
            webColumnHeader1.append(
                    //("<tr>" +


                    //"<td>Rank</td>" +
                    "<td>Bib</td>" +
                            "<td>Name</td>" +
                            "<td>Run1</td>" +
                            "<td>Pen</td>" +
                            "<td>Total</td>" +
                            "<td>Run2</td>" +
                            "<td>Pen</td>" +
                            "<td>Total</td>" +
                            "<td>Best</td>" );
            //        "</tr>\n");
        }

        if (webColumnGroupWidths.length()==0) {
            webColumnGroupWidths.append("<colgroup>" +
                    "<col span=\"1\" style=\"width: 5%;\">" +    //rank
                    "<col span=\"1\" style=\"width: 5%;\">" +    /// bib
                    "<col span=\"1\" style=\"width: 15%;\">" +   // name

                    "<col span=\"1\" style=\"width: 10%;\">" +   //run 1
                    "<col span=\"1\" style=\"width: 5%;\">" +    /// pen
                    "<col span=\"1\" style=\"width: 10%;\">" +   //  tot

                    "<col span=\"1\" style=\"width: 10%;\">" +   //run 2
                    "<col span=\"1\" style=\"width: 5%;\">" +   //pen
                    "<col span=\"1\" style=\"width: 10%;\">" +  // tot

                    "<col span=\"1\" style=\"width: 10%;\">" +   //best
                    "</colgroup>");
        }

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
            output.newLine();
            output.write(webColumnHeader.toString());
            output.newLine();

        } catch (Exception e) {
            log.write(e);
        }

    }

    private void outputWebColumnHeader(BufferedWriter output,String boatClass) {
        try {
            output.newLine();
            output.write( "<tr><td>"+boatClass+"</td>");
            output.write(webColumnHeader1.toString());
            output.write( "</tr>");
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

    public void outputWeb(String title, ArrayList<RaceRun> runs, boolean breakOnClassChange) {
        //log.info("\n" + title + "Web Results to HTML");
        String lastBoatClass = null;
        for (RaceRun r : runs) {


            float totalTime;

            if (breakOnClassChange) {
                if (lastBoatClass != null) {
                    //          if (lastBoatClass.compareTo(r.getBoat().getBoatClass()) != 0)
                    //               log.info("----");
                }
                lastBoatClass = r.getBoat().getBoatClass();
            }
            totalTime = (float) r.getTotalPenalties();
            totalTime += r.getElapsed();

            // log.info(String.format("Run# %1s ", r.getRunNumber()) +
            //        String.format("%3s ", r.getBoat().getBoatClass()) +
            //        String.format("%-25s ", r.getBoat().getRacer().getShortName()) +
            //        String.format("%7s ", r.getResultString()) +
            //        String.format("+%-3s ", r.getTotalPenalties()) +
            //        String.format("%8.2f", totalTime));

            //log.info("");
        }

        BufferedWriter output = null;
        StringBuffer links = new StringBuffer();
        boolean firstTime = true;
        try {
            File file = new File("/Library/Webserver/Documents/index.html");
            output = new BufferedWriter(new FileWriter(file));


            output.write("<!DOCTYPE html>");
            output.write("<html>");
            output.write("<head>");
            output.write("<meta http-equiv=\"refresh\" content=\"10\" >");

            //A161003
            writeScrollHeader(output);
            //A161003 end

///was BG efefef
            output.write(" <style> .odd {" +
                    "background-color: #B0EDEA;" +

                    "}" +

                    ".even {" +
                    "background-color: #ffffff;" +
                    "}            </style>\n");

            output.write("</head>\n");
//            output.write("<body background=\"raceBackground.jpg\">");
            output.write("<body>\n");

            //A161003
            writeScrollBodyHeader(output);
            //A161003 End
            ArrayList<Result> sorted = Race.getInstance().getTempResults();

            // Write links to classes
            if (!scroll) {

                output.write("<div id=\"button\">");
            }
            // Accumulate all the classes that are now have at leat 1 result


            // links.append("<tr>");
            links.append("<td colspan=\"8\">");

            for (Result r : sorted) {
                if (true || breakOnClassChange) {     //Fixme  constant true
                    if (lastBoatClass != null) {
                        if (lastBoatClass.compareTo(r.getBoat().getBoatClass()) != 0)
                            output.newLine();
                    }
                    if (lastBoatClass == null || lastBoatClass.compareTo(r.getBoat().getBoatClass()) != 0) {
//if (firstTime) {

                        links.append("<a href=\"#" + r.getBoat().getBoatClass() + "\">");
                        links.append(r.getBoat().getBoatClass());
                        links.append("</a>&nbsp;&nbsp;&nbsp;&nbsp;");
                        links.append(ahref(r.getBoat().getBoatClass()));

                    }
                    lastBoatClass = r.getBoat().getBoatClass();
                }

            }
            if (firstTime && !scroll) {
                //links.append("<td>");

                links.append("<a href=\"#Top\">");
                links.append("Home");
                links.append("</a></td>");
                outputWebColumnGroup(output);


                // links.append("<div>");
//                firstTime=false;
            }
            if (!scroll) {
                output.write("</div>");
            }

            if (scroll) {
                output.write("<div class=\"b\" id=\"b1\">");  //A161003
                output.write("<div class=\"b-con\">\n"); //A161003

            }
            output.write("<table width=\"100%\">");//div class=\"table\">");

            //outputWebColumnGroup(output);

            //outputWebColumnHeader(output);

            ///////
            int rank = 1;
//            output.write("<tr><a name=\"Top\">");// + r.getBoat().getBoatClass());
//            output.write("</a></tr>");
            if (!scroll) {  //screws up scrolling
                output.write("<a name=\"Top\">");// + r.getBoat().getBoatClass());
                output.write("</a>\n");

            }
            //         else {
            // Don't need ???             outputWebColumnHeader(output);
            //         }


//            ArrayList<Result> sorted = Race.getInstance().getTempResults();
            for (Result r : sorted) {


                if (true || breakOnClassChange) {     //Fixme  constant true
                    if (lastBoatClass != null) {


                        if (lastBoatClass.compareTo(r.getBoat().getBoatClass()) != 0) {
// Empty <td></td> cause jquery scroller to break a simple "<br>" element in the <td> pair will fix
                            //  output.write("<tr><td></td></tr>\n");
                            //  output.newLine();
                            //  output.write("<tr><td></td></tr>\n");
                            //  output.newLine();
                            if (scroll) {
                                writeEmptyTableRow(output);
                                outputWebColumnHeader(output, r.getBoat().getBoatClass());  /// Added
                            } else {
                                outputWebColumnHeader(output);
                            }

                        }

                    }
                    if (lastBoatClass == null || lastBoatClass.compareTo(r.getBoat().getBoatClass()) != 0) {
//                        output.write("<tr></tr>");
                        if (!scroll) {

                            output.write("<tr><td>");

                            output.write("<b><a name=\"" + r.getBoat().getBoatClass() + "\">");// + r.getBoat().getBoatClass());
                            output.write(r.getBoat().getBoatClass());
                            output.write("</a></b>");
                            output.write("</td>\n");
                            output.write("<td>" + links.toString()+"</td>");
                            output.write("</tr>\n");

                            output.newLine();

                            //output.write( links.toString());
                        }

                        rank = 1;
                    }
                    lastBoatClass = r.getBoat().getBoatClass();
                }

                // output.write("<div  class=\"td\">");

//AA

                writeRacerResult(output,  rank++,  r) ;
//BB

            }



            for (int i=0;i<25;i++) {
                writeEmptyTableRow(output);
            }

            output.write("</table>\n");//div>");


            output.write("</div>\n");  //A161003
            output.write("</div>\n"); //A161003

            //A161003
            writeScrollBodyTrailer(output);
            //A1612003 End
            output.write("</body>\n");

            output.write("</html>\n");

            output.close();
        } catch (Exception e) {
            log.write(e);
        } finally {
            try {
                output.close();
            } catch (Exception ex) {
                log.write(ex);

            }
        }
    }


    private static String emptyRow = "<tr><td><br></td><td><br></td><td><br></td><td><br></td><td><br></td><td><br></td><td><br></td><td><br></td><td><br></td><td><br></td></tr>";
    private String tableEmptyRow() {
        return(emptyRow);
    }


    private void writeRacerResult(BufferedWriter output, int rank, Result r) {
        //                output.write(r.getBoat().getBoatClass() + ", ");
        String s1;
        String bib;

        //output.write("<tr>");
        try {

            output.write("<tr class=" + "\"" + (rank % 2 == 0 ? "even" : "odd") + "\">");


            output.write("<td>" + (rank) + "</td>\n");


            output.write("<td>");

            bib = r.getBoat().getRacer().getBibNumber();
            output.write(bib);
            output.write("</td>\n");

            output.write("<td>");


            s1 = r.getBoat().getRacer().getShortName();

            if (!scroll) {

                StringBuffer sb1 = new StringBuffer("<a href=\"" + bib + ".html" + "\">" + s1);
                sb1.append("</a></td>");

                output.write(sb1.toString());
            }
            else {
                output.write(s1);
            }


            //output.write(s1);
            //output.write("</td>");


//                output.write( " run1=");
            //s1 = r.getRun1()!=null?r.getRun1().formatTimeExpanded():RaceRun.TIME_EXPANDED_FILL;


            //
            StringBuffer sb = new StringBuffer();

            if (r.getRun1() != null) {
                sb.append("<td>");
                sb.append(r.getRun1().getResultString());
                sb.append("</td>\n<td>");
                sb.append(r.getRun1().getPenaltyStringBlankIfNone());
                sb.append("</td>\n<td>");
                sb.append(r.getRun1().getTotalTimeString());
                sb.append("</td>\n");
            } //else sb.append(",,,");
            else {
    //                    sb.append("<td></td>\n<td></td>\n<td></td>\n");
            }


            if (r.getRun2() != null) {
                sb.append("<td>");
                sb.append(r.getRun2().getResultString());
                sb.append("</td>\n<td>");
                sb.append(r.getRun2().getPenaltyStringBlankIfNone());
                sb.append("</td>\n<td>");
                sb.append(r.getRun2().getTotalTimeString());
                sb.append("</td>\n");
            }// else sb.append(",,,");
            else {
                //                  sb.append("<td></td>\n<td></td>\n<td></td>\n");
            }


            //


            output.write(sb.toString());//s1);

            //output.write( " run2=");
            //s1 = r.getRun2()!=null?r.getRun2().formatTimeExpanded():RaceRun.TIME_EXPANDED_FILL;
            //output.write(s1);
            /// todo must integrate TH results before sort  - DONE VERIFY 10/11/2013
            //output.write(        "   best=");
            s1 = r.getBestRun() != null ? r.getBestRun().formatTimeTotalOnly() : RaceRun.TIME_ONLY_FILL;
            output.write("<td>");

            output.write(s1);
            output.write("</td>\n");


            if (r.getBestRun().getPhotoCellRaceRun() != null) {    /// todo must integrate TH results before sort
                output.write("<td>");

                output.write(ResultsTable.TIMINGMODE_AUTOMATIC);
                output.write("</td>\n");

            }

            output.write("</tr>\n");

            output.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    private void writeEmptyTableRow(BufferedWriter output) {
        try {
            output.write(emptyRow+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeScrollHeader(BufferedWriter output) {


        try {
            if (scroll) {

                output.write("<style type=\"text/css\">\n");
                output.write("* {margin: 0; padding: 0;}\n");
                output.write("ul { list-style: none; }\n");


                output.write(".b {\n");
                output.write("    height: 600px;\n");
                output.write("overflow: hidden;\n");
                output.write("width: 800px;\n");
                output.write("    margin-left: auto;\n");
                output.write("    margin-right: auto;\n");
                output.write("}\n");

                output.write(".b-con div {\n");
                output.write("    width: 798px;\n");
                output.write("    height: 198px;\n");
                output.write("    border: 1px solid #ddd;\n");
                output.write("    line-height: 2.4;\n");
                output.write("    font-size: 30px;\n");
                output.write("    text-align: center;\n");
                output.write("}\n");


                output.write("</style>\n");

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeScrollBodyHeader(BufferedWriter output) {


        // try {
        //     if (scroll) {

//  D161003 22:18              output.write("<div id = \"jquery-script-menu\" >");

// Not needed ??               output.write("<div class=\"jquery-script-center\" >");
//                output.write("<div class=\"jquery-script-clear\" ></div >");
//                output.write("</div >");
// D161003 22:18 output.write("</div >");
        //     }
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    }

    private void writeScrollBodyTrailer(BufferedWriter output) {
        try {
            if (scroll) {

                output.write("<script src = \"http://code.jquery.com/jquery-1.11.1.min.js\" ></script >\n");
                output.write("<script type = \"text/javascript\" src = \"scrollForever.js\" ></script >\n");
                output.write("<script type = \"text/javascript\" >\n");
                output.write("    $(function() {");
                // var time1 = new Date;
                //output.write("$(\"#b1\").scrollForever({dir:\"top\", container:\".b-con\", inner:\"div\"});");
                output.write("$(\"#b1\").scrollForever({dir:\"top\", container:\".b-con\", inner:\"tr\", delayTime: 150});\n");

                output.write("});\n");
                output.write("</script>\n");

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }






}
