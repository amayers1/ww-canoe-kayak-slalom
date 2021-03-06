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
public class SlalomResultsHTTP {   /// For scrolling scoreboard


    private Log log;
/// Scroll in client browser Using http://www.jqueryscript.net/animation/jQuery-Plugin-For-Infinite-Any-Content-Scroller-scrollForever.html


    // object initialization
    {   // app instantion time initialize header  //todo make compile time & verify

        log = Log.getInstance();


    }


    static StringBuffer webColumnHeader1 = null;

    private String getWebColumnHeader() {
        if (webColumnHeader1 == null) {
            webColumnHeader1 = new StringBuffer();
            webColumnHeader1.append(
                    "<tr>" +
                            "<td style=\"min-width:30px\">Rank</td>" +
                            "<td style=\"min-width:30px\">Bib</td>" +
                            "<td style=\"min-width:220px\">Name</td>"+
                            "<td style=\"min-width:30px\" align=\"right\">Run</td>"+
                            "<td style=\"min-width:45px\" align=\"right\">Raw</td>");
            webColumnHeader1.append("<td style=\"min-width:45px\" align=\"right\">Pen</td>");
            webColumnHeader1.append("<td style=\"min-width:45px\" align=\"right\">Total</td>");
            webColumnHeader1.append("<td style=\"min-width:45px\" align=\"right\">Best</td>");
            webColumnHeader1.append("<td style=\"min-width:45px\" align=\"right\">%Class</td>");

   //         webColumnHeader1.append(gateNrsTableHeader());
            webColumnHeader1.append("</tr>\n");
        }

        return(webColumnHeader1.toString());

    }


    private void outputWebColumnHeader(BufferedWriter output,String boatClass, String status) {


        String columnHeader = getWebColumnHeader();
        try {

            StringBuffer sb = new StringBuffer();
            sb.append("<tr><td><br></td></tr><tr><td colspan=\"10\"><font size=\"+3\">" + Race.getInstance().getName() + "</font></td></tr>" +

                    "<tr><td colspan=\"6\"><font size=\"+1\">" + " " + Race.getInstance().getDate() + "</font></td></tr>" );


//                    "</font></td></tr>");

            sb.append("<tr><td><br></td></tr><tr><td colspan=\"10\"><font size=\"+2\">"        + boatClass + " " + status + " Results</font></td></tr>");
            output.write(sb.toString());
            writeEmptyTableRow(output);
            writeEmptyTableRow(output);


            sb = new StringBuffer();
            sb.append(columnHeader);
            output.write(sb.toString());
            writeEmptyTableRow(output);

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



    String httpFilePath = "/Library/Webserver/Documents/"; // + "Results_" + fileName + " .html");
    String resultsFilePrefix = "Results_";



    public void outputWeb(String title, ArrayList<RaceRun> runs, boolean breakOnClassChange, String boatClass, int runNbr) {
        //log.info("\n" + title + "Web Results to HTML");
        String lastBoatClass = null;


        BufferedWriter output = null;
        //     StringBuffer links = new StringBuffer();
        boolean firstTime = true;

        String fileName = boatClass + "_Run_" + runNbr;

        try {
            File file = new File(httpFilePath +  resultsFilePrefix + fileName + ".html");
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

                   // "border: 1px solid black" +
                   // "border: solid thin" +

                    "}\n" +
                    "\n</style>\n");

            sb.append("</head>\n");
//            sb.append("<body background=\"raceBackground.jpg\">");
            sb.append("<body>\n");
            output.write(sb.toString());


            ArrayList<Result> sorted = Race.getInstance().getTempResults();
//            RaceRun mostRecentRun = Race.getInstance().getNewestCompletedRun();
            // Write links to classes
            // Accumulate all the classes that are now have at leat 1 result



            sb = new StringBuffer();
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
                if ((bestTimeSoFar == 0) || bestTimeSoFar < bestTimeOverall) {
                    bestTimeOverall = bestTimeSoFar;
                }
            }


            lastBoatClass = null;
            lineCount=1;
            bestTimeForClass = (float)0.0;



            for (Result r : sorted) {


                if (true || breakOnClassChange) {     //Fixme  constant true

                    if (lastBoatClass == null || lastBoatClass.compareTo(r.getBoat().getBoatClass()) != 0) {
/*161004*/

                        if (boatClass.compareTo(r.getBoat().getBoatClass()) == 0) {


                          //  HTML for page break on printer  <p><!-- pagebreak --></p>

                                    writeEmptyTableRow(output);

                            StringBuffer status = new StringBuffer();

                            if (runNbr ==0 || runNbr==1 && r.getRun1() != null) {
                                if (runNbr == 0) {
                                    status.append("Run 1 ");
                                }
                                status.append(r.getRun1().getStatusString());
                            }

                            if (runNbr==2 && r.getRun2() != null) {
                                if (runNbr == 0) {
                                    status.append(" Run 2 ");
                                }

                                status.append(r.getRun2().getStatusString());
                            }


                            outputWebColumnHeader(output, r.getBoat().getBoatClass(), status.toString());  /// Added

                            rank = 1;
                            bestTimeForClass = r.getBestRun().getTotalPenalties() + r.getBestRun().getElapsed();

                        }
                    }
                    lastBoatClass = r.getBoat().getBoatClass();
                }

                if (boatClass.compareTo(r.getBoat().getBoatClass()) == 0) {

                    int runCnt = (r.getRun1() != null ? 1 : 0) + (r.getRun2() != null ? 1 : 0);
                    if (r.getRun1() != null && (runNbr >= 1 || runNbr == 0)) {
                        writeRunResult(output, rank, r.getRun1(), true, r.getBestRun() == r.getRun1(), runCnt, bestTimeForClass, bestTimeOverall);
                    }
                    if (r.getRun2() != null && (runNbr == 2 || runNbr == 0)) {
                        writeRunResult(output, rank, r.getRun2(), false, r.getBestRun() == r.getRun2(), runCnt, bestTimeForClass, bestTimeOverall);
                    }
                    rank++;  /// todo Fix this for 1st run display only ... after 2nd runs have occured (1st run ranking will differ from after 2 runs)
                }
            }

// add blank rows for jquery scrolling bug
            // todo accumulate in SB and then output

            output.write("</table>\n");//div>");

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

        updateTableOfContents();


    }





    private void updateTableOfContents() {

            ArrayList<String> fileList = new ArrayList();

            //String path = ".";

            String files;
            File folder = new File(httpFilePath);
            File[] listOfFiles = folder.listFiles();

            for (int i = 0; i < listOfFiles.length; i++)
            {

                if (listOfFiles[i].isFile())
                {
                    files = listOfFiles[i].getName();
                    if (files.startsWith(resultsFilePrefix))
                    {
                        String fileName = null;
                      //  if (files.compareTo("last.ser") != 0 ) {
                            log.trace(files);
                            String s[]  = files.split("([\\.])");
                            fileName = s[0];
                            fileList.add(s[0]);//files.toString());
                    //    }
                        if (fileName != null) {
                            System.out.println(fileName);
                        }
                    }
                }
            }


        BufferedWriter output=null;

        File file = new File(httpFilePath + "list.html");


        try {
            output = new BufferedWriter(new FileWriter(file));
            StringBuffer sb = new StringBuffer();

            sb.append("<!DOCTYPE html>");
            sb.append("<html>");
            sb.append("<head>");
            sb.append("</head>");

            sb.append("<body><ul>");

            output.write(sb.toString());

            for (String s:fileList) {
                sb = new StringBuffer();

                s.trim();

                sb.append("<li><a href=\"" +  s  + ".html\">"   + s  +  "</a></li>" );

                output.write(sb.toString());
            }


            sb = new StringBuffer();
            sb.append("</ul></body>");
            sb.append("</html>");
            output.write(sb.toString());


            output.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
        try {
            output.close();
        } catch (Exception ex) {
            log.write(ex);

        }
    }


//            return fileList;

    }


    private static String emptyRow = "<tr><td>&nbsp;</td></tr>";// +
    // "<td><br></td><td><br></td><td><br></td><td><br></td><td><br></td><td><br></td><td><br></td><td><br></td><td><br></td></tr>";
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

                sb.append("<td style=\"min-width:220px\">");
                s1 = r.getBoat().getRacer().getShortName();
                sb.append(s1);

                sb.append("</td>\n");
            } else {
                sb.append("<td><br></td>\n");
                sb.append("<td><br></td>\n");
                sb.append("<td><br></td>\n");
            }

            //

            if (r!= null) {

                sb.append("<td>" + (firstRun?"1":"2") + "</td>");
                sb.append("<td align=\"right\">");
                sb.append(" " + r.getResultString() + " ");
                sb.append("</td>");

//                sb.append("<td align=\"right\">");
//                sb.append(r.getTotalPenaltiesHTML_BR());
//                sb.append("</td>\n");

                sb.append("<td align=\"right\">"+r.getTotalPenaltiesHTML_BR()+"</td>");



                sb.append("<td align=\"right\">");
                sb.append(" " +r.getTotalTimeString() + " ");
                sb.append("</td>\n");

                sb.append("<td align=\"right\">");
                sb.append(best&&runCnt>1?r.getTotalTimeString():"<br>");
                sb.append("</td>\n");
//                sb.append(r.penaltyStringHTMLExtendedForScroll());


                try {
                    if (bestTimeForClass != 0.0) {  /// We don't know on the most recent run, send 0.0 TODO FIX THIS TO GET bestTime
                        percentage = (r.getElapsed() + r.getTotalPenalties()) / bestTimeForClass;


                        sb.append("<td>" + String.format("%.3f", percentage) + "</td>\n");
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
                        sb.append("<td>" + String.format("%.3f", percentage) + "</td>\n");
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

    int width = 1200;   //todo determine width appropriate

    private void writeScrollHeader(BufferedWriter output) {


        try {

            StringBuffer sb = new StringBuffer();

            sb.append("<style type=\"text/css\">\n");
            sb.append("* {margin: 1; padding: 2;}\n");
            sb.append("ul { list-style: none; }\n");

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
