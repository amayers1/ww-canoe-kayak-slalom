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


/**
 * ${PROJECT_NAME}
 *
 * Teton Cay Group Inc. ${YEAR}
 *

 * User: allen
 * Date: 11/3/13
 * Time: 10:56 AM
 *
 */

/**
 * This does not work in TestNG ... thread support ???
 */
public class LogTestConcurrency {

    private class MyRunnable implements Runnable {

        int times;
        String message;
        int level;
        Log log;
        MyRunnable(int times, String message, int level) {
            this.times = times;
            this.message = message;
            this.level = level;
            log = Log.getInstance();
        }

        @Override
        public void run() {
            for (int i=0; i<times; i++) {
                log.error(message + " " + i);
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }
    }

    void testThreadSafe() {
        Runnable t1 = new MyRunnable(10000, "Some message", Log.LOG_ERROR);
        Runnable t2 = new MyRunnable(10000, "Some other message", Log.LOG_ERROR);
        Runnable t3 = new MyRunnable(10000, "More messages", Log.LOG_ERROR);
        Runnable t4 = new MyRunnable(10000, "Jim's message", Log.LOG_ERROR);
        Runnable t5 = new MyRunnable(10000, "Jane's message", Log.LOG_ERROR);
        Runnable t6 = new MyRunnable(10000, "Jane's message", Log.LOG_ERROR);
        Runnable t7 = new MyRunnable(10000, "Jane's message", Log.LOG_ERROR);
        Runnable t8 = new MyRunnable(10000, "Jane's message", Log.LOG_ERROR);
        Runnable t9 = new MyRunnable(10000, "Jane's message", Log.LOG_ERROR);
        Runnable t10 = new MyRunnable(10000, "Jane's message", Log.LOG_ERROR);

        new Thread(t5).start();
        new Thread(t3).start();
        new Thread(t2).start();
        new Thread(t4).start();
        new Thread(t1).start();

        new Thread(t10).start();
        new Thread(t7).start();
        new Thread(t8).start();
        new Thread(t6).start();
        new Thread(t9).start();
    }


    static public void main(String[] args) {
        LogTestConcurrency lt = new LogTestConcurrency();
        lt.testThreadSafe();
    }

}
