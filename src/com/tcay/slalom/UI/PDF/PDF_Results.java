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

package com.tcay.slalom.UI.PDF;


import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
//import com.itextpdf.layout.element.Table.Cell;
//import com.itextpdf.samples.GenericTest;

import com.itextpdf.layout.property.TextAlignment;







import com.itextpdf.test.annotations.type.SampleTest;
import com.tcay.slalom.Race;
import com.tcay.slalom.RaceRun;
import com.tcay.slalom.Result;
import com.tcay.slalom.UI.tables.ResultsTable;
import org.junit.experimental.categories.Category;








import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;


/**
 * Created by allen on 10/6/16.
 */


@Category(SampleTest.class)
public class PDF_Results {//} extends GenericTest {
    public static final String DEST = "/Users/allen/Race.pdf"; //"./target/test/resources/sandbox/tables/simple_table.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new PDF_Results().manipulatePdf(DEST);
    }

    //@Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(8);
        for (int i = 0; i < 16; i++) {
            table.addCell("hi");
        }
        doc.add(table);

        doc.close();
    }


    public void doitTest(String title, ArrayList<RaceRun> runs, boolean breakOnClassChange) {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        try {
            new PDF_Results().manipulatePdf(DEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void doit(String title, ArrayList<RaceRun> runs, boolean breakOnClassChange) {
        Document doc = null;
        Table table = null;

        try {
            File file = new File(DEST);
            file.getParentFile().mkdirs();
           // new PDF_Results().manipulatePdf(DEST);

            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
            //Document
                    doc = new Document(pdfDoc);

            table = new Table(9);  // # of columns
           // for (int i = 0; i < 16; i++) {
           //     table.addCell("hi");
           // }



        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }





        String lastBoatClass = null;
        for (RaceRun r : runs) {


            float totalTime;

            if (breakOnClassChange) {
                if (lastBoatClass != null) {
                    if (lastBoatClass.compareTo(r.getBoat().getBoatClass()) != 0)
                        ;
                     //   log.info("---");
                }
                lastBoatClass = r.getBoat().getBoatClass();
            }
            totalTime = (float) r.getTotalPenalties();
            totalTime += r.getElapsed();
        }

        try {

            ArrayList<Result> sorted = Race.getInstance().getTempResults();
            for (Result r : sorted) {
                if (true || breakOnClassChange) {     //Fixme  constant true
                    if (lastBoatClass != null) {
                        if (lastBoatClass.compareTo(r.getBoat().getBoatClass()) != 0)
                            ;
//                            output.newLine();
                    }
                    if (lastBoatClass == null || lastBoatClass.compareTo(r.getBoat().getBoatClass()) != 0) {
//                        output.write(r.getBoat().getBoatClass());
//                        output.newLine();
                    }
                    lastBoatClass = r.getBoat().getBoatClass();
                }
                String s1;

//                Cell cell23 = new Cell(1, 6).add("a Class ???multi 1,3 and 1,4");
//                table.addCell(cell23);
//                table.startNewRow();




                s1 = String.format("%1$3s", r.getBoat().getRacer().getBibNumber());
                table.addCell(s1);

                String s = r.getBoat().getRacer().getShortName();
                s1 = String.format("%1$-15s", s);
                table.addCell(s1);



                s1 = r.getRun1().getResultString();
                table.addCell(s1);

                s1 = r.getRun1().getPenaltyString();
                table.addCell(s1);

                s1 = r.getRun1().getTotalTimeString();
                table.addCell(s1);


if (r.getRun2()!=null) {
    table.startNewRow();

    table.addCell(".");
    table.addCell("..");
    s1 = r.getRun2().getResultString();
    table.addCell(s1);

    s1 = r.getRun2().getPenaltyString();
    table.addCell(s1);

    s1 = r.getRun2().getTotalTimeString();
    table.addCell(s1);

}
//                else {
//    table.addCell("")//;


//}





                table.startNewRow();



                /// todo must integrate TH results before sort  - DONE VERIFY 10/11/2013
//                output.write("   best=");
//                s1 = r.getBestRun() != null ? r.getBestRun().formatTimeTotalOnly() : RaceRun.TIME_ONLY_FILL;
//                output.write(s1);


//                if (r.getBestRun().getPhotoCellRaceRun() != null) {    /// todo must integrate TH results before sort
//                    output.write(ResultsTable.TIMINGMODE_AUTOMATIC);
//                }
//                output.newLine();
            }

            PdfFont f = PdfFontFactory.createFont(FontConstants.HELVETICA);
            Cell cell = new Cell(1, 3);

            cell
                    .add(new Paragraph("Class K1 ... or whateever"))
                    .setFont(f)
                    .setFontSize(13)
                    .setFontColor(DeviceGray.WHITE)
                    .setBackgroundColor(DeviceGray.BLACK)    ;
                  //  .setTextAlignment(TextAlignment.CENTER);

            table.addHeaderCell(cell);


/*            for (int i = 0; i < 2; i++) {
                Cell[] headerFooter = new Cell[] {
                        new Cell().setBackgroundColor(new DeviceGray(0.75f)).add("#"),
                        new Cell().setBackgroundColor(new DeviceGray(0.75f)).add("Key"),
                        new Cell().setBackgroundColor(new DeviceGray(0.75f)).add("Value")
                };
                for (Cell hfCell : headerFooter) {
                    if (i == 0) {
                        table.addHeaderCell(hfCell);
                    } else {
                        table.addFooterCell(hfCell);
                    }
                }
            }
            for (int counter = 1; counter < 20; counter++) {
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(String.valueOf(counter)));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add("key " + counter));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add("value " + counter));
            }

*/

            doc.add(table);

            doc.close();



        } catch (Exception e) {
            System.out.println(e);
            System.out.println(e.getStackTrace());
            e.printStackTrace();

            //           log.write(e);
        } finally {
            try {
//                output.close();
            } catch (Exception ex) {
                System.out.println(ex);
                System.out.println(ex.getStackTrace());

                ex.printStackTrace();


            }
        }

    }

}