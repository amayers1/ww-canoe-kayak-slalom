package com.tcay;

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 10/11/13
 * Time: 6:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class Singleton {

    static Singleton instance = null;


    public synchronized static Singleton getInstance() {
        if (instance==null)
            instance = new Singleton();
        return instance;
    }
}
