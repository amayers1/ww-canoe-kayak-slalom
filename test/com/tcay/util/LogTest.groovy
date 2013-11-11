package com.tcay.util

import org.testng.Assert
import org.testng.annotations.Test

/**
 * ${PROJECT_NAME}
 *
 * Teton Cay Group Inc. ${YEAR}
 *

 * User: allen
 * Date: 11/3/13
 * Time: 7:38 AM
 *
 */
class LogTest {
    void setUp() {

    }

    void tearDown() {

    }

    @Test(groups = [ "functional", "unit" ])
    void testGetInstance() {
        Log log = Log.getInstance();
        Assert.assertNotNull(log);
    }

    @Test(groups = [ "functional", "unit" ])
    void testTrace() {
        Log log = Log.getInstance();
        log.setCurrentLevel(Log.LOG_TRACE);

        log.trace("SINGLE TEST a trace message");
    }

    @Test(groups = [ "functional", "unit" ])
    void testDebug() {
        Log log = Log.getInstance();
        log.setCurrentLevel(Log.LOG_DEBUG);
        log.debug("SINGLE TEST a debug message");

    }

    @Test(groups = [ "functional", "unit" ])
    void testInfo() {
        Log log = Log.getInstance();
        log.info("SINGLE TEST an Info message");
    }

    @Test(groups = [ "functional", "unit" ])
    void testWarn() {
        Log log = Log.getInstance();
        log.warn("SINGLE TEST a WARNING message");

    }





    @Test(groups = [ "functional", "unit" ])
    void testError() {
        Log log = Log.getInstance();
        log.setCurrentLevel(Log.LOG_ERROR);
        log.error("SINGLE TEST an ERROR message");

    }

    @Test(groups = [ "functional", "unit" ])
    void testFatal() {
        Log log = Log.getInstance();
        log.fatal("SINGLE TEST a FATAL message");

    }

    @Test(groups = [ "functional", "unit" ])
    void testWrite() {
        Log log = Log.getInstance();
        log.write(10, "SINGLE TEST a uncategorized  message");
    }



    private static final int THREADSAFE_ITERATIONS = 200;
    @Test(groups = [ "functional", "unit", "stress" ], threadPoolSize = 27, invocationCount = 1000, timeOut = 10000L)
    void testThreadSafe() {
        Log log = new Log();
        int i=0;
        try {
            for (i=0; i<THREADSAFE_ITERATIONS; i++) {
                log.error("Another Error");

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertEquals(i,200)
    }

    @Test(groups = [ "functional", "unit", "stress" ], invocationCount = 1000, timeOut = 10000L)
    void testMixedLevels() {
        Log log = new Log();
        log.setCurrentLevel(Log.LOG_INFO);
        int level  = (int)(Math.random()*Log.HIGHEST_LEVEL+1);  /// range 1-highest logging level

        switch(level) {
            case Log.LOG_TRACE:
               log.trace("a Trace Message");
               break;
            case Log.LOG_DEBUG:
                log.debug("debug message");
                break;
            case Log.LOG_INFO:
                log.info("an info message")
                break;
            case Log.LOG_WARN:
                log.warn("a warning message")
                break;
            case Log.LOG_ERROR:
                log.error("an error message")
                break;
            case Log.LOG_FATAL:
                log.fatal("a fatal message")
                break;
            default:
                break;
        }

    }
}
