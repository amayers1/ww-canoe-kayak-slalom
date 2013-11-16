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
import com.tcay.slalom.RaceRun;
import com.tcay.slalom.Result;
import com.tcay.slalom.UI.components.GateLabelSmall;
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

    public void updateResults() {
        fireTableDataChanged();
    }


    public Class getColumnClass( int column )
    {
        return getValueAt(0, column).getClass();                             // crash in simulation java.lang.NullPointerException
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
                    case RunScoringTable.COL_BOATCLASS:

                        if (originalRow%2==0) {
                            //o = r.getBoat().getBoatClass();

                            Icon icon = null;
                            /*
                            if (run.isGold()) {
                                icon = Race.getInstance().getMedalGoldSmall();
                            }
                            else if (run.isSilver()) {
                                icon = Race.getInstance().getMedalSilverSmall();

                            }
                            else if (run.isBronze()) {
                                icon = Race.getInstance().getMedalBronzeSmall();
                            }
                            */
                            o = icon;

                            if (o==null) {
                                o = run.getBoat().getBoatClass();
                                 //o = run.getPlaceInClass();                        // crash on simulation       java.lang.NullPointerException
                            }

                        }
                        break;
                    case RunScoringTable.COL_BIB:
                        o = "";
                        if (originalRow%2==0) {

                            o = r.getBoat().getRacer().getBibNumber();
                        }
                        break;
                    case RunScoringTable.COL_RACERNAME:
                        o = "";
                        if (originalRow%2==0) {
                            o = r.getBoat().getRacer().getShortName();    /// fixme NULL POinter exception  second fastest time DemoMode
                        }
                        break;

                    case RunScoringTable.COL_RUN_NBR:
                        o = runNbr;
                        break;

                    case RunScoringTable.COL_RAW:
                        if (run!= null) {
                            o = run.getResultString();;
                        }
                        break;
                    case RunScoringTable.COL_TOTAL:
                        if (run!= null) {
                           o = run.getTotalTimeString();
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
