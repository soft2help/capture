package com.s2h.capture.Helpers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import com.s2h.capture.Webcam;

public class Log {

    public static Logger getLog(String loggerName) {
        // String path =
        // Webcom.s2h.capture.class.getResource("resources/logging.properties").getFile();
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(Defaults.getUserFolder() + "logging.properties");
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            
            inputStream = Log.class.getClassLoader().getResourceAsStream("logging.properties");
        }

        try {
            LogManager.getLogManager().readConfiguration(inputStream);
            inputStream.close();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // System.setProperty("java.util.logging.config.file", path);

        Logger logger = Logger.getGlobal();

        // Handler[] handlers = logger.getHandlers();

        // // array of registered handlers
        // for (Handler handler : handlers) {
        // logger.removeHandler(handler);
        // }

        // logger.addHandler(new ConsoleHandler());

        return logger;
    }

}
