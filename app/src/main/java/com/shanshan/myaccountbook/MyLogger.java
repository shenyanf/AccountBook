package com.shanshan.myaccountbook;

import android.os.Environment;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;

import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * Created by heshanshan on 2015/12/21.
 */
public class MyLogger {
    private static Logger myLogger;
    private static String logDir = null;

    public static Logger getMyLogger(String className) {
        if (MyAccountUtil.isSDCardMounted()) {
            System.out.println("SD card has already mounted");
            logDir = Environment.getExternalStorageDirectory() + File.separator + "myaccount" + File.separator + "logs";
        } else {
            System.out.println("SD card hasn't already mounted");
            logDir = Environment.getDataDirectory() + File.separator + "myaccount" + File.separator + "logs";
        }
        File destDir = new File(logDir);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }


        if (myLogger == null) {
            final LogConfigurator logConfigurator = new LogConfigurator();
            logConfigurator.setFileName(logDir + File.separator + "myaccount.log");
            // Set the root log level
            logConfigurator.setRootLevel(Level.DEBUG);
            // Set log level of a specific logger
            logConfigurator.setLevel("org.apache", Level.ERROR);
            logConfigurator.configure();

            //gLogger = Logger.getLogger(this.getClass());
            myLogger = Logger.getLogger(className != null ? className : MainActivity.class.getName());

        }
        return myLogger;
    }

    private MyLogger() {

    }
}
