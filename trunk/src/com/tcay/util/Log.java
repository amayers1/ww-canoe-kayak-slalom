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

package com.tcay.util;

import com.tcay.slalom.UI.SlalomApp;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.text.SimpleDateFormat;

/**
 * ${PROJECT_NAME}
 *
 * Teton Cay Group Inc. ${YEAR}
 *

 * User: allen
 * Date: 10/25/13
 * Time: 5:19 PM
 *
 */
public class Log {

    private static String fileName;
    private static String newLine = System.getProperty("line.separator");
    // beware Java 1.6 doesNOT support YYYY, java 1.7 does
    private static SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd HH:mm:SS");//.format(new Date())

    private FileOutputStream file;
    private int currentLevel = LOG_TRACE;//INFO;

    protected static final int LOG_TRACE = 6;
    protected static final int LOG_DEBUG = 5;
    protected static final int LOG_INFO = 4;
    protected static final int LOG_WARN = 3;
    protected static final int LOG_ERROR = 2;
    protected static final int LOG_FATAL = 1;
    protected static final int LOG_EXCEPTION = 0;
    protected static final int HIGHEST_LEVEL = LOG_TRACE;

    private int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }


    // protected used for testNG which cannot use static methods like getInstance
    protected Log() {
        int start = SlalomApp.class.getName().lastIndexOf(".") + 1;
        String name = SlalomApp.class.getName().substring(start);
        fileName = name + ".log";

        try {
            file = new FileOutputStream(fileName, true);
            //ref https://www.securecoding.cert.org/confluence/display/java/FIO14-J.+Perform+proper+cleanup+at+program+termination
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                public void run() {
                    info("Execution ends");
                    System.out.println("Closing Logger");
                    try {
                        file.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }));
            info("Execution begins");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static Log instance = null;
    public synchronized static Log getInstance() {
        if (instance == null) {
            instance = new Log();
        }
        return instance;
    }

    private String getLevelString(int level) {
        String levelString;
        switch (level) {
            case LOG_TRACE:
                levelString = "   trace:";
                break;
            case LOG_DEBUG:
                levelString = "  debug:";
                break;
            case LOG_INFO:
                levelString = " info:";
                break;
            case LOG_WARN:
                levelString = "warn:";
                break;
            case LOG_ERROR:
                levelString = "err:";
                break;
            case LOG_FATAL:
                levelString = "fatal:";
                break;
            case LOG_EXCEPTION:
                levelString = "Exception:";
                break;
            default:
                levelString = "???:";
                break;
        }
        return levelString;
    }



    private String formatIt(String message, int level) {
        String prefix;
        StringBuffer completeMessage = new StringBuffer();

        completeMessage.append(fmt.format(System.currentTimeMillis()));
        completeMessage.append(" ");
        prefix = getLevelString(level);
        completeMessage.append(prefix);
        completeMessage.append("(t:").append(Thread.currentThread().getId()).append(")");
        completeMessage.append(message);
        completeMessage.append(newLine);
        return completeMessage.toString();
    }

    // fixme This locking SHOULD NOT BE NECESSARY, OR MAYBE IT IS ... test
    // Thread safe version.
    private void writeOrig(int level, String message) {
        boolean written = false;
        do {
            try {
                // Assemble the message first.
                byte[] bytes = formatIt(message,level).getBytes();

                // Then Lock the file
                try {
                    FileLock lock = file.getChannel().lock();
                    try {
                        // Write the bytes.
                        file.write(bytes);
                        file.flush();
                        written = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        // Release the lock.
                        lock.release();
                    }
                    if (level <=LOG_WARN)
                        System.out.print(new String(bytes));

                } catch (OverlappingFileLockException ofle) {
                    try {
                        // Wait a bit
                        Thread.sleep(0);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                        throw new InterruptedIOException("Interrupted waiting for a file lock.");
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("Failed to lock " + fileName + "Exception: " + ex);
            }
        } while (!written);
    }


    private void write(int level, String message) {
        writeOrig(level, message);

        /*
        boolean written = false;

        do {
            byte[] bytes = formatIt(message,level).getBytes();
            try {
                file.write(bytes);
                file.flush();
                written = true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
        } while (!written);
        */
    }


    public void trace(String message) {
        if (getCurrentLevel() >= LOG_TRACE) {
            write(LOG_TRACE, message);
            System.out.println(message);
        }
    }

    public void debug(String message) {
        if (getCurrentLevel() >= LOG_DEBUG) {
            write(LOG_DEBUG, message);
            System.out.println(message);
        }
    }

    public void info(String message) {
        if (getCurrentLevel() >= LOG_INFO) {
            write(LOG_INFO, message);
            System.out.println(message);
        }
    }

    public void warn(String message) {
        if (getCurrentLevel() >= LOG_WARN) {
            write(LOG_WARN, message);
        }
    }

    public void error(String message) {
        if (getCurrentLevel() >= LOG_ERROR) {
            writeOrig(LOG_ERROR, message);
        }
    }

    public void fatal(String message) {
        if (getCurrentLevel() >= LOG_FATAL) {
            write(LOG_FATAL, message);
        }
    }

    public void write(Exception e) {
        write(LOG_EXCEPTION, e.getMessage());
        e.printStackTrace();
    }
}
