package com.tcay.util;


/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 11/3/13
 * Time: 10:56 AM
 * To change this template use File | Settings | File Templates.
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
