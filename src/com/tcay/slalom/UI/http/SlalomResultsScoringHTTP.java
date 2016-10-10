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
import com.tcay.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by allen on 10/4/16.
 */
public class SlalomResultsScoringHTTP {   /// For scrolling scoreboard


    private Log log;
/// Scroll in client browser Using http://www.jqueryscript.net/animation/jQuery-Plugin-For-Infinite-Any-Content-Scroller-scrollForever.html


    // object initialization
    {   // app instantion time initialize header  //todo make compile time & verify

        log = Log.getInstance();


    }

    public String gateNrsTableHeader() {
        StringBuffer sb = new StringBuffer();

        for (int iGate = 1; iGate<=Race.getInstance().getNbrGates(); iGate++) {
            sb.append("<td style=\"min-width:20px\" align=\"right\">"+iGate+"</td>");  /// empty table data not comnpatible with HTML scrolling JQuery code
        }
        return sb.toString();

    }


    static StringBuffer webColumnHeader1 = null;
    static StringBuffer webColumnHeader2 = null;

    private String getMostRecentColumnHeader() {
        if (webColumnHeader2 == null) {
            webColumnHeader2 = new StringBuffer();
            webColumnHeader2.append("<tr>");
            webColumnHeader2.append("<td colspan=\"9\" style=\"min-width:300px\">");

            webColumnHeader2.append("Most Recent Runs");
            webColumnHeader2.append("</td></tr>\n");
        }

        return(webColumnHeader2.toString());
    }

    private String getWebColumnHeader() {
        return(getWebColumnHeader(true));

    }

    private String getWebColumnHeader(boolean showPercentage) {
       // if (webColumnHeader1 == null) {
            webColumnHeader1 = new StringBuffer();
            webColumnHeader1.append(
                    "<tr>" +
                            "<td style=\"min-width:30px\">Rank</td>" +
                            "<td style=\"min-width:30px\">Bib</td>" +
                            "<td style=\"min-width:200px\">Name</td>"+
                            "<td style=\"min-width:30px\" align=\"right\">Run</td>"+
                            "<td style=\"min-width:45px\" align=\"right\">Raw</td>");
            webColumnHeader1.append("<td style=\"min-width:45px\" align=\"right\">Pen</td>");
            webColumnHeader1.append("<td style=\"min-width:45px\" align=\"right\">Total</td>");
            webColumnHeader1.append("<td style=\"min-width:45px\" align=\"right\">Best</td>");

            webColumnHeader1.append(gateNrsTableHeader());

            if (showPercentage) {
                webColumnHeader1.append("<td style=\"min-width:45px\" align=\"right\">%Class</td>");
                webColumnHeader1.append("<td style=\"min-width:45px\" align=\"right\">%All</td>");
                webColumnHeader1.append("<td style=\"min-width:45px\">Status</td>");
            }


            webColumnHeader1.append("</tr>\n");
       // }

        return(webColumnHeader1.toString());

    }


    private void outputWebColumnHeader(BufferedWriter output,String boatClass) {


        String columnHeader = getWebColumnHeader();
        try {

            StringBuffer sb = new StringBuffer();
            sb.append("<tr><td><br></td></tr><tr><td>" + boatClass + "</td></tr>");
            sb.append(columnHeader);
            output.write(sb.toString());
        } catch (Exception e) {
            log.write(e);
        }

    }



    public void outputWeb(String title, ArrayList<RaceRun> runs, boolean breakOnClassChange) {
        //log.info("\n" + title + "Web Results to HTML");
        String lastBoatClass = null;


        BufferedWriter output = null;
   //     StringBuffer links = new StringBuffer();
        boolean firstTime = true;
        try {
            File file = new File("/Library/Webserver/Documents/index.html");
            output = new BufferedWriter(new FileWriter(file));

            StringBuffer sb = new StringBuffer();


            int refresh = 15;
            refresh += runs.size() * 3;

            sb.append("<!DOCTYPE html>");
            sb.append("<html>");
            sb.append("<head>");
            sb.append("<meta http-equiv=\"refresh\" content=\"" + refresh + "\" >");   /// todo change to variable based on # results
            output.write(sb.toString());

            //A161003
            writeScrollHeader(output);
            //A161003 end

///was BG efefef

            sb = new StringBuffer();
            sb.append(" <style> .odd {" +
                    "background-color: #B0EDEA;" +

                    "}\n" +

                    ".even {" +
                    "background-color: #ffffff;" +
                    "}\n" +
//                    ".narrow {width: 20px;}\n" +
//                    ".mediumWide {width: 40px;}\n" +
//                    ".racerName {width: 120px;}\n" +
                    "\n</style>\n");

            sb.append("</head>\n");
//            sb.append("<body background=\"raceBackground.jpg\">");
            sb.append("<body>\n");
            output.write(sb.toString());


            ArrayList<Result> sorted = Race.getInstance().getTempResults();
            RaceRun mostRecentRun = Race.getInstance().getNewestCompletedRun();
            // Write links to classes
            // Accumulate all the classes that are now have at leat 1 result



            sb = new StringBuffer();
//            sb.append("<table width="+width+">");//div class=\"table\">");
            sb.append("<table>");//div class=\"table\">");
            sb.append(getMostRecentColumnHeader());
            sb.append(getWebColumnHeader(false));
            output.write(sb.toString());

            //writeRunResult(output, 0, mostRecentRun,true, false, 0, (float)0.0) ;

            RaceRun recentRun;
            for (int i=1; i<4;i++) {
                recentRun = Race.getInstance().getRecentCompletedRun(i);
                if (recentRun != null) {
                    writeRunResult(output, 0, recentRun,true, false, 0, (float)0.0,(float)0.0) ;

                }
            }




            writeEmptyTableRow(output);
            sb = new StringBuffer();
            sb.append("</table>");
            output.write(sb.toString());


            sb = new StringBuffer();
            sb.append("<div class=\"b\" id=\"b1\">");  //A161003
            sb.append("<div class=\"b-con\">\n"); //A161003

            sb.append("<table>");//div class=\"table\">");
            output.write(sb.toString());

            int rank = 1;

            lastBoatClass = null;
            lineCount=1;
            float bestTimeForClass = (float)0.0;
            float bestTimeOverall = (float)0.0;
            float bestTimeSoFar = (float)0.0;


            for (Result r : sorted) {
                bestTimeSoFar = r.getBestRun().getTotalPenalties() + r.getBestRun().getElapsed();
                if ((bestTimeOverall == 0) || bestTimeSoFar < bestTimeOverall) {
                    bestTimeOverall = bestTimeSoFar;
                }
            }


            lastBoatClass = null;
            lineCount=1;
            bestTimeForClass = (float)0.0;



            for (Result r : sorted) {


                if (true || breakOnClassChange) {     //Fixme  constant true

                    if (lastBoatClass == null || lastBoatClass.compareTo(r.getBoat().getBoatClass()) != 0) {
/*161004*/              writeEmptyTableRow(output);
                        outputWebColumnHeader(output, r.getBoat().getBoatClass());  /// Added

                        rank = 1;
                        bestTimeForClass = r.getBestRun().getTotalPenalties() + r.getBestRun().getElapsed();
                    }
                    lastBoatClass = r.getBoat().getBoatClass();
                }


                int runCnt = (r.getRun1()!=null?1:0) + (r.getRun2()!=null?1:0);
                if (r.getRun1()!=null) {

                    writeRunResult(output, rank, r.getRun1(),true, r.getBestRun()==r.getRun1(), runCnt, bestTimeForClass, bestTimeOverall) ;
                }
                if (r.getRun2()!=null) {
                    writeRunResult(output, rank, r.getRun2(),false, r.getBestRun()==r.getRun2(), runCnt, bestTimeForClass, bestTimeOverall) ;
                }
                rank++;
            }


// add blank rows for jquery scrolling bug
            for (int i=0;i<20;i++) {
                writeEmptyTableRow(output);
            }

            // todo accumulate in SB and then output

            output.write("</table>\n");//div>");
            output.write("</div>\n");  //A161003
            output.write("</div>\n"); //A161003

            writeScrollBodyTrailer(output);

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



    private String ahref(String link, String linkText) {
        StringBuffer sb = new StringBuffer();
        sb.append("<a href=\"");
        sb.append(link).append(".html");
        sb.append("\">" +linkText + "</a>");
        return sb.toString();
    }
 //   <a href="http://www.w3schools.com">Visit W3Schools</a>


    private static String emptyRow = "<tr><td>&nbsp;</td></tr>";// +
    private String tableEmptyRow() {
        return(emptyRow);
    }


    static int lineCount = 1;
    private void writeRunResult(BufferedWriter output, int rank, RaceRun r, boolean firstRun, boolean best, int runCnt, float bestTimeForClass, float bestTimeOverall) {
        //                output.write(r.getBoat().getBoatClass() + ", ");
        String s1;
        String bib;
        float percentage;
        //output.write("<tr>");
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("<tr class=" + "\"" + (lineCount%2==0 ? "even" : "odd") + "\">");

            if (firstRun && r!= null) {
                sb.append("<td align=\"right\">" + (rank>0?rank:"") + "</td>\n");

                sb.append("<td align=\"right\">");




                bib = r.getBoat().getRacer().getBibNumber();
                sb.append(bib);
                sb.append("</td>\n");

                sb.append("<td style=\"min-width:200px\">");
                s1 = r.getBoat().getRacer().getShortName();


                sb.append(ahref(bib, s1));
                //sb.append(s1);

                sb.append("</td>\n");
            } else {
                sb.append("<td><br></td>\n");
                sb.append("<td><br></td>\n");
                sb.append("<td><br></td>\n");
            }

            //

            if (r!= null) {

//                sb.append("<td>" + (firstRun?"1":"2") + "</td>");

                sb.append("<td>" + r.getRunNumber() + "</td>");


                sb.append("<td align=\"right\">");
                sb.append(" " + r.getResultString() + " ");
                sb.append("</td>");

                sb.append("<td align=\"right\">");
                sb.append(r.getTotalPenaltiesHTML_BR());
                sb.append("</td>\n");

                sb.append("<td align=\"right\">");
                sb.append(" " +r.getTotalTimeString() + " ");
                sb.append("</td>\n");

                sb.append("<td align=\"right\">");
                sb.append(best&&runCnt>1?r.getTotalTimeString():"<br>");
                sb.append("</td>\n");
                sb.append(r.penaltyStringHTMLExtendedForScroll());


                try {
                    if (bestTimeForClass != 0.0) {  /// We don't know on the most recent run, send 0.0 TODO FIX THIS TO GET bestTime
                        percentage = (r.getElapsed() + r.getTotalPenalties()) / bestTimeForClass;


                        sb.append("<td align=\"right\">" + String.format("%.3f", percentage) + "</td>\n");
                    }
                    else {
                        sb.append("<td><br></td>\n");
                    }
                } catch (Exception e) {
                    sb.append("<td><br></td>\n");
                    e.printStackTrace();
                }


                try {
                    if (bestTimeOverall != 0.0) {  /// We don't know on the most recent run, send 0.0 TODO FIX THIS TO GET bestTime
                        percentage = (r.getElapsed() + r.getTotalPenalties()) / bestTimeOverall;
                        sb.append("<td align=\"right\">" + String.format("%.3f", percentage) + "</td>\n");
                    }
                    else {
                        sb.append("<td><br></td>\n");
                    }
                } catch (Exception e) {
                    sb.append("<td><br></td>\n");
                    e.printStackTrace();
                }










                sb.append("<td>"+ r.getStatusString()+"</td>\n");



                //sb.append(r.getStatus());


            } //else sb.append(",,,");
            else {
                sb.append("<td><br></td>\n");
                sb.append("<td><br></td>\n");
                sb.append("<td><br></td>\n");
                //                    sb.append("<td></td>\n<td></td>\n<td></td>\n");
            }

            sb.append("</tr>\n");


            output.write(sb.toString());//s1);

            lineCount++;
            // D161004  output.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    private void writeEmptyTableRow(BufferedWriter output) {
        try {
            output.write(tableEmptyRow()+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    int width = 1280;   //todo determine width appropriate

    private void writeScrollHeader(BufferedWriter output) {


        try {

            StringBuffer sb = new StringBuffer();

            sb.append("<style type=\"text/css\">\n");
            sb.append("* {margin: 1; padding: 2;}\n");
            sb.append("ul { list-style: none; }\n");


            sb.append(".b {\n");
            sb.append("    height: 800px;\n");
            sb.append("overflow: hidden;\n");
            sb.append("width: "+ width +"px;\n");
         //   sb.append("    margin-left: auto;\n");
         //   sb.append("    margin-right: auto;\n");
            sb.append("}\n");

            sb.append(".b-con div {\n");
            sb.append("    width: " + (width-2) + "px;\n");
//            sb.append("    height: 2798px;\n");
            sb.append("    height: 60px;\n");
            sb.append("    border: 1px solid #ddd;\n");
            sb.append("    line-height: 2.4;\n");
            sb.append("    font-size: 30px;\n");
            sb.append("    text-align: center;\n");
            sb.append("}\n");


            sb.append("</style>\n");
                output.write(sb.toString());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void writeScrollBodyTrailer(BufferedWriter output) {
        try {

                output.write("<script src = \"http://code.jquery.com/jquery-1.11.1.min.js\" ></script >\n");
                output.write("<script type = \"text/javascript\" src = \"scrollForever.js\" ></script >\n");
                output.write("<script type = \"text/javascript\" >\n");
                output.write("    $(function() {");
                // var time1 = new Date;
                //output.write("$(\"#b1\").scrollForever({dir:\"top\", container:\".b-con\", inner:\"div\"});");
                output.write("$(\"#b1\").scrollForever({dir:\"top\", container:\".b-con\", inner:\"tr\", delayTime: 40});\n");

                output.write("});\n");
                output.write("</script>\n");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
