package com.tcay.slalom;

import com.tcay.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 10/20/13
 * Time: 3:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class ListOfRaces {

    static Log log = Log.getInstance();

    static public ArrayList list(String path)
    {
        ArrayList<String> fileList = new ArrayList();

        //String path = ".";

        String files;
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++)
        {

            if (listOfFiles[i].isFile())
            {
                files = listOfFiles[i].getName();
                if (files.endsWith(".ser") || files.endsWith(".SER"))
                {
                    if (files.compareTo("last.ser") != 0 ) {
                        log.trace(files);
                        String s[]  = files.split("([\\.])");
                        fileList.add(s[0]);//files.toString());
                    }
                }
            }
        }
       return fileList;
    }



    public static void main(String[] args)
    {
        ListOfRaces.list(".");
        // Directory path here
    }
}
