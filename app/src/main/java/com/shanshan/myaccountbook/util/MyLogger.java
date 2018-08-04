package com.shanshan.myaccountbook.util;

import android.os.Environment;

import com.shanshan.myaccountbook.activity.MainActivity;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.chainsaw.Main;

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
            logDir = Environment.getExternalStorageDirectory() + File.separator + "myaccount" + File.separator + "logs";
        } else {
            logDir = MainActivity.getBaseDir() + File.separator + "myaccount" + File.separator + "logs";
        }
        File destDir = new File(logDir);
        if (!destDir.exists()) {
            Boolean isSuccess = destDir.mkdirs();
            if (!isSuccess)
                myLogger.debug("create dir " + destDir + " failed.");
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
