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

import java.io.File;
import java.util.ArrayList;

/**
 * ${PROJECT_NAME}
 *
 * Teton Cay Group Inc. ${YEAR}
 *

 * User: allen
 * Date: 10/20/13
 * Time: 3:03 PM
 *
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
