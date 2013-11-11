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

package com.tcay.slalom;

import com.tcay.util.Log;

import java.io.*;

/**
 * ${PROJECT_NAME}
 *
 * Teton Cay Group Inc. ${YEAR}
 *

 * User: allen
 * Date: 10/20/13
 * Time: 3:19 PM
 *
 */
public class LastRace implements Serializable {
    private String name;

    private transient Log log;
    {
        log = Log.getInstance();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
//        this.name = "US Slalom Nationals Nantahala Falls 09 27 2013";
        this.name = name;
    }


    public LastRace() {
        loadSerializedData();
    }


    public LastRace(String name) {
        this.name = name;
    }

    public void saveSerializedData() {
        try
        {
            FileOutputStream fileOut = new FileOutputStream("last.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
            log.info("Serialized data is saved in last.ser");

        }
        catch(IOException i)
        {
            i.printStackTrace();
        }
    }


    public String loadSerializedData ()  {
        String lastRaceName = "";
        try
        {
            FileInputStream fileIn = new FileInputStream("last.ser");

            ObjectInputStream in = new ObjectInputStream(fileIn);

            Object o;
            while ((o = in.readObject()) != null) {

                LastRace last = (LastRace)o;
                lastRaceName = last.name;

            }
        }
        catch(FileNotFoundException fnfe) {

        }
        catch(InvalidClassException ice) {
            log.warn(".SER file has a different version, unable to load previous race data");
        }
        catch(ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        catch(EOFException eofe) {
        }
        catch(IOException i)
        {
            i.printStackTrace();
        }
        name = lastRaceName;

        return(lastRaceName);
    }
}
