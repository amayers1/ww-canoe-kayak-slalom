package com.tcay.slalom.UI.tables;

import com.tcay.slalom.Race;
import com.tcay.slalom.RaceRun;
import com.tcay.slalom.Result;
import com.tcay.util.Log;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 9/28/13
 * Time: 7:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class RunScoringTableModel extends AbstractTableModel
{
    static RunScoringTableModel instance = null;

    public synchronized static RunScoringTableModel getInstance() {
        if (instance==null)  {
            instance = new RunScoringTableModel();
        }
        return instance;
    }


    Race race;
    Log log;

    private ArrayList<String> columnNames;

    private RunScoringTableModel() {
        log = Log.getInstance();
        race = Race.getInstance();

        columnNames = new ArrayList<String>();

        columnNames.add( "Class");
        columnNames.add( "Bib" );
        columnNames.add( "Name" );
        columnNames.add( "Run" );

        columnNames.add( "Raw" );
        columnNames.add( "Total" );
        int nbrGates = race.getNbrGates();

        for (Integer i = 1; i<nbrGates+1; i++) {
            columnNames.add(i.toString());
        }
    }


    // todo problem when assigning penalties after race results are sorted - not dynamic

    // SAMPLE OF HOW TO UPDATE TABLE UI ON MODEL CHANGE
//    public void removeStock(int row) {
//        stocks.remove(row);
//        fireTableDataChanged();
//    }


    public void updateResults() {
        fireTableDataChanged();
    }


    public Class getColumnClass( int column )
    {

//        String s=null;
//        //lie about column 10, with custom image renderer
//        if (column == 10)
//            return(s.getClass());

        return getValueAt(0, column).getClass();
    }
    public int getColumnCount()
    {
        return columnNames.size();
    }
    public String getColumnName( int column )
    {
        return columnNames.get(column);
    }
    public int getRowCount()
    {
        return Race.getInstance().getTempResults().size()*2;
        //return Race.getInstance().getCompletedRunsByClassTime().size();
    }


    static final String RUN1 = "1";
    static final String RUN2 = "2";

    public Object getValueAt( int row, int column )
    {


        row--;
        if (row < 0 ) {
            /*
            /// do the gate images
            if (column > RunScoringTable.COL_TOTAL) {
                int gate = column - RunScoringTable.COL_TOTAL;
                if (Race.getInstance().isUpstream(gate)) {
                    Icon  ii = Race.getInstance().getUpstreamTinyII();
                    //return(new JLabel(ii));
                    return(ii);
                }
                else {
                    Icon  ii = Race.getInstance().getDownstreamTinyII();
                    //return(new JLabel(ii));
                    return(ii);
                }
            }
            */
            return " ";
        }



        ArrayList<Result> sortedResults = Race.getInstance().getTempResults();
        Object o = "..";
        int originalRow = row;
        row = row / 2;

        if (row < sortedResults.size() && row >=0 ) {

            o = "  ";

            Result r = sortedResults.get(row);
            if (r==null)
                log.warn("Empty result");

            try {
                RaceRun run;
                String runNbr;
                if(originalRow%2==0) {
                    runNbr=RUN1;
                    run = r.getRun1();
                }
                else {
                    runNbr=RUN2;
                    run = r.getRun2();
                }

                switch (column) {
                    case RunScoringTable.COL_BOATCLASS:        // class
                        o = "";
                        if (originalRow%2==0) {// name {
                            o = (Object)r.getBoat().getBoatClass();
                        }
                        break;
                    case RunScoringTable.COL_BIB:        // bib
                        o = "";
                        if (originalRow%2==0) {// name {

                            o = (Object)r.getBoat().getRacer().getBibNumber();
                        }
                        break;
                    case RunScoringTable.COL_RACERNAME:
                        o = "";
                        if (originalRow%2==0) {// name {
                            o = (Object)r.getBoat().getRacer().getShortName();
                        }
                        break;

                    case RunScoringTable.COL_RUN_NBR:
                        o = (Object)runNbr;
                        break;

                    case RunScoringTable.COL_RAW:        // run 1 raw
                        if (run!= null) {
                            o = (Object)run.getResultString();//getElapsed();
                        }
                        break;
                    case RunScoringTable.COL_TOTAL:
                        if (run!= null) {
                           o = (Object)run.getTotalTimeString();
                        }
                        break;
                    default:
                        if (column>RunScoringTable.COL_TOTAL) {
                            if (run!= null) {
                                o = run.getPenaltyString(column-RunScoringTable.COL_TOTAL);
                                if (((String)o).compareTo(" 0") == 0 || ((String)o).compareTo("0") == 0) {
                                    o = "-";
                                }
                            }
                        }
                        break;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
        return(o);
    }
}
