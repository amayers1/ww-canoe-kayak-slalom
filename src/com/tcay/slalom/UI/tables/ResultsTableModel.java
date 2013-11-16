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

package com.tcay.slalom.UI.tables;

import com.tcay.slalom.Race;
import com.tcay.slalom.Result;
import com.tcay.util.Log;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * SlalomApp
 *
 * Teton Cay Group Inc. 2013
 *

 * User: allen
 * Date: 9/28/13
 * Time: 7:43 PM
 *
 */
public class ResultsTableModel extends AbstractTableModel
{
    static ResultsTableModel instance = null;

    public synchronized static ResultsTableModel getInstance() {
        if (instance==null)  {
            instance = new ResultsTableModel();
        }
        return instance;
    }


    Race race;
    Log log;

    private final String[] columnNames = {// "Class", "Pos", "Bib", "Club", "Name", "Run1 Raw", "tm1", "Pen",  "Run2 Raw", "tm2", "Pen", "Best Run Total", "tmb" };

        ResultsTable.COL_BOATCLASS_NAME,
        ResultsTable.COL_POSITION_NAME,
        ResultsTable.COL_BIB_NAME,

        ResultsTable.COL_CLUB_OR_COUNTRY_NAME,
        ResultsTable.COL_RACERNAME_NAME,
        ResultsTable.COL_RAW1_NAME,
        ResultsTable.COL_TIMIMGMODE1_NAME,
        ResultsTable.COL_PEN1_NAME,


        ResultsTable.COL_RAW2_NAME,
        ResultsTable.COL_TIMIMGMODE2_NAME,
        ResultsTable.COL_PEN2_NAME,

        ResultsTable.COL_BESTRUN_TOTAL_NAME,
        ResultsTable.COL_TIMINGMODE_BEST_NAME
    };

    /* protected and not private ONLY to be used by subclass ResultsTableModelAutoScroll */
    protected ResultsTableModel() {
        log = Log.getInstance();
        race = Race.getInstance();
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
        return getValueAt(0, column).getClass();
    }
    public int getColumnCount()
    {
        return columnNames.length;
    }
    public String getColumnName( int column )
    {
        return columnNames[column];
    }
    public int getRowCount()
    {
        return Race.getInstance().getTempResults().size();
        //return Race.getInstance().getCompletedRunsByClassTime().size();
    }

    protected ArrayList<Result> getSortedResults() {
        ArrayList<Result> sorted = Race.getInstance().getTempResults();
        return sorted;
    }


    public Object getValueAt( int row, int column )
    {
        ArrayList<Result> sortedResults = getSortedResults();
        Object o = "..";

        if (row < sortedResults.size()) {

            o = "--";
            Result r = sortedResults.get(row);
            if (r==null)
                log.warn("Empty result");

            try {
                ImageIcon icon;// =  Race.getInstance().getTagHeuerTinyII();
                //if (row%17==0) {
                //    icon = Race.getInstance().getStopWatchII();
                //}



                switch (column) {
                    case ResultsTable.COL_BOATCLASS:        // class
                        o = r.getBoat().getBoatClass();
                        break;
                    case ResultsTable.COL_POSITION:        // bib
                        o = r.getBestRun().getPlaceInClass();
                        break;
                    case ResultsTable.COL_BIB:        // bib
                        o = r.getBoat().getRacer().getBibNumber();
                        break;
                    case ResultsTable.COL_CLUB_OR_COUNTRY:        // get Flag or club logo
                        icon = r.getBoat().getImageIcon();
                        o = icon;
                        break;
                    case ResultsTable.COL_RACERNAME:        // name
                        o = r.getBoat().getRacer().getShortName();
                        break;
                    case ResultsTable.COL_RAW1:        // run 1 raw
                        o = r.getRun1().getResultString();
                        break;
                    case ResultsTable.COL_TIMIMGMODE1:        // run 1 timing mode
                        icon =  Race.getInstance().getTagHeuerTinyII();
                        if (r.getRun1().getTagHeuerRaceRun()==null) {
                            icon = Race.getInstance().getStopWatchII();
                        }
                        o = icon;
                                     //ResultsTable.TIMINGMODE_MANUAL:ResultsTable.TIMINGMODE_AUTOMATIC;
                        break;
                    case ResultsTable.COL_PEN1:        // run 1 penalties
                        o = r.getRun1().getTotalPenalties();
                        break;
                    case ResultsTable.COL_RAW2:        // run 2
                        o = r.getRun2().getResultString();
                        break;
                    case ResultsTable.COL_TIMIMGMODE2:
                        icon =  Race.getInstance().getTagHeuerTinyII();
                        if (r.getRun2().getTagHeuerRaceRun()==null) {
                            icon = Race.getInstance().getStopWatchII();
                        }
                        o = icon;
                        break;
                    case ResultsTable.COL_PEN2:        // run2 penalties
                        o = r.getRun2().getTotalPenalties();
                        break;
                    case ResultsTable.COL_BESTRUN_TOTAL:        // best time total
                        o = r.getBestRun().getTotalTimeString();
                        break;
                    case ResultsTable.COL_TIMINGMODE_BEST:
                        icon =  Race.getInstance().getTagHeuerTinyII();
                        if (r.getBestRun().getTagHeuerRaceRun()==null) {
                            icon = Race.getInstance().getStopWatchII();
                        }
                        o = icon;
                        break;
                    default:
                        break;
                }

            }
            catch (Exception e) {
//                e.printStackTrace();
            }

        }
        return(o);
    }
}
