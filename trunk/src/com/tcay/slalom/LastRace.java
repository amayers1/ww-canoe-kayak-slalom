package com.tcay.slalom;

import com.tcay.util.Log;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 10/20/13
 * Time: 3:19 PM
 * To change this template use File | Settings | File Templates.
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
