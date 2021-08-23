package com.s2h.capture.Helpers;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.s2h.capture.Webcam;

public class Settings {

    public Boolean captureAll;
    public String captureAllKey, captureAllKeyCode, captureAllSavePath, captureAllNameComplete, captureAllNameCropped;

    public Boolean captureComplete;
    public String captureCompleteKey, captureCompleteKeyCode, captureCompleteSavePath, captureCompleteName;

    public Boolean captureCropped;
    public String captureCroppedKey, captureCroppedKeyCode, captureCroppedSavePath, captureCroppedName;

    public String ip;
    public Boolean flipHorizontal;

    public Boolean cropImage;

    public String cropUpperLeftX, cropUpperLeftY, cropWidth, cropHeight;

    public Boolean prespectiveCorrection;
    public String fromUpperLeftX, fromUpperLeftY, toUpperLeftX, toUpperLeftY, fromUpperRightX, fromUpperRightY,
            toUpperRightX, toUpperRightY, fromDownRightX, fromDownRightY, toDownRightX, toDownRightY, fromDownLeftX,
            fromDownLeftY, toDownLeftX, toDownLeftY;

    public Double notificationHideAfter;
    public String notificationPosition;

    public int timeoutCaptureImage;
    public int timeoutPrespectiveCorrection;
    private final String propFileName = "config.properties";

    public Settings() {
        read();
    }

    public void read() {
        Properties prop = new Properties();
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(Defaults.getUserFolder() + propFileName);
            prop.load(inputStream);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            // e1.printStackTrace();
            try {
                inputStream = Webcam.class.getClassLoader().getResourceAsStream(propFileName);
                prop.load(inputStream);
            } catch (Exception e) {
                Notification.error("Error Reading config file " + e.getMessage());
            }
        }

        captureAll = Boolean.parseBoolean(prop.getProperty("captureAll", "false"));

        captureAllKey = prop.getProperty("captureAllKey", "F10");
        captureAllKeyCode = prop.getProperty("captureAllKeyCode", "121");
        captureAllSavePath = prop.getProperty("captureAllSavePath", "");
        captureAllNameComplete = prop.getProperty("captureAllNameComplete", "Complete_");
        captureAllNameCropped = prop.getProperty("captureAllNameCropped", "Cropped_");

        captureComplete = Boolean.parseBoolean(prop.getProperty("captureComplete", "false"));
        captureCompleteKey = prop.getProperty("captureCompleteKey", "F11");
        captureCompleteKeyCode = prop.getProperty("captureCompleteKeyCode", "122");
        captureCompleteSavePath = prop.getProperty("captureCompleteSavePath", "");
        captureCompleteName = prop.getProperty("captureCompleteName", "Complete_");

        captureCropped = Boolean.parseBoolean(prop.getProperty("captureCropped", "false"));
        captureCroppedKey = prop.getProperty("captureCroppedKey", "F12");
        captureCroppedKeyCode = prop.getProperty("captureCroppedKeyCode", "123");
        captureCroppedSavePath = prop.getProperty("captureCroppedSavePath", "");
        captureCroppedName = prop.getProperty("captureCroppedName", "Cropped_");

        ip = prop.getProperty("ip", "rtsp://admin:admin@192.168.1.102");
        flipHorizontal = Boolean.parseBoolean(prop.getProperty("flipHorizontal", "false"));

        cropImage = Boolean.parseBoolean(prop.getProperty("cropImage", "false"));

        cropUpperLeftX = prop.getProperty("cropUpperLeftX", "0");
        cropUpperLeftY = prop.getProperty("cropUpperLeftY", "0");
        cropWidth = prop.getProperty("cropWidth", "0");
        cropHeight = prop.getProperty("cropHeight", "0");

        prespectiveCorrection = Boolean.parseBoolean(prop.getProperty("prespectiveCorrection", "false"));

        fromUpperLeftX = prop.getProperty("fromUpperLeftX", "0");
        fromUpperLeftY = prop.getProperty("fromUpperLeftY", "0");
        toUpperLeftX = prop.getProperty("toUpperLeftX", "0");
        toUpperLeftY = prop.getProperty("toUpperLeftY", "0");

        fromUpperRightX = prop.getProperty("fromUpperRightX", "0");
        fromUpperRightY = prop.getProperty("fromUpperRightY", "0");
        toUpperRightX = prop.getProperty("toUpperRightX", "0");
        toUpperRightY = prop.getProperty("toUpperRightY", "0");

        fromDownRightX = prop.getProperty("fromDownRightX", "0");
        fromDownRightY = prop.getProperty("fromDownRightY", "0");
        toDownRightX = prop.getProperty("toDownRightX", "0");
        toDownRightY = prop.getProperty("toDownRightY", "0");

        fromDownLeftX = prop.getProperty("fromDownLeftX", "0");
        fromDownLeftY = prop.getProperty("fromDownLeftY", "0");
        toDownLeftX = prop.getProperty("toDownLeftX", "0");
        toDownLeftY = prop.getProperty("toDownLeftY", "0");

        notificationHideAfter = Double.valueOf(prop.getProperty("notificationHideAfter", "4"));
        notificationPosition = prop.getProperty("notificationPosition", "BOTTOM_RIGHT");

        timeoutCaptureImage = Integer.parseInt(prop.getProperty("timeoutCaptureImage", "10"));
        timeoutPrespectiveCorrection = Integer.parseInt(prop.getProperty("timeoutPrespectiveCorrection", "10"));

        try {
            if (inputStream != null)
                inputStream.close();

        } catch (IOException e) {
            Notification.error("" + e.getMessage());
        }
    }

    public void update() {

        Properties prop = new Properties();

        prop.setProperty("captureAll", String.valueOf(captureAll));
        prop.setProperty("captureAllKey", captureAllKey);
        prop.setProperty("captureAllKeyCode", captureAllKeyCode);
        prop.setProperty("captureAllSavePath", captureAllSavePath);
        prop.setProperty("captureAllNameComplete", captureAllNameComplete);
        prop.setProperty("captureAllNameCropped", captureAllNameCropped);

        prop.setProperty("captureComplete", String.valueOf(captureComplete));
        prop.setProperty("captureCompleteKey", captureCompleteKey);
        prop.setProperty("captureCompleteKeyCode", captureCompleteKeyCode);

        prop.setProperty("captureCompleteSavePath", captureCompleteSavePath);
        prop.setProperty("captureCompleteName", captureCompleteName);

        prop.setProperty("captureCropped", String.valueOf(captureCropped));
        prop.setProperty("captureCroppedKey", captureCroppedKey);
        prop.setProperty("captureCroppedKeyCode", captureCroppedKeyCode);
        prop.setProperty("captureCroppedSavePath", captureCroppedSavePath);
        prop.setProperty("captureCroppedName", captureCroppedName);

        prop.setProperty("ip", ip);
        prop.setProperty("flipHorizontal", String.valueOf(flipHorizontal));

        prop.setProperty("cropImage", String.valueOf(cropImage));

        prop.setProperty("cropUpperLeftX", cropUpperLeftX);
        prop.setProperty("cropUpperLeftY", cropUpperLeftY);
        prop.setProperty("cropWidth", cropWidth);
        prop.setProperty("cropHeight", cropHeight);

        prop.setProperty("prespectiveCorrection", String.valueOf(prespectiveCorrection));

        prop.setProperty("fromUpperLeftX", fromUpperLeftX);
        prop.setProperty("fromUpperLeftY", fromUpperLeftY);
        prop.setProperty("toUpperLeftX", toUpperLeftX);
        prop.setProperty("toUpperLeftY", toUpperLeftY);

        prop.setProperty("fromUpperRightX", fromUpperRightX);
        prop.setProperty("fromUpperRightY", fromUpperRightY);
        prop.setProperty("toUpperRightX", toUpperRightX);
        prop.setProperty("toUpperRightY", toUpperRightY);

        prop.setProperty("fromDownRightX", fromDownRightX);
        prop.setProperty("fromDownRightY", fromDownRightY);
        prop.setProperty("toDownRightX", toDownRightX);
        prop.setProperty("toDownRightY", toDownRightY);

        prop.setProperty("fromDownLeftX", fromDownLeftX);
        prop.setProperty("fromDownLeftY", fromDownLeftY);
        prop.setProperty("toDownLeftX", toDownLeftX);
        prop.setProperty("toDownLeftY", toDownLeftY);

        prop.setProperty("notificationHideAfter", String.valueOf(notificationHideAfter));
        prop.setProperty("notificationPosition", notificationPosition);

        prop.setProperty("timeoutCaptureImage", String.valueOf(timeoutCaptureImage));
        prop.setProperty("timeoutPrespectiveCorrection", String.valueOf(timeoutPrespectiveCorrection));

        File f;
        try {
            f = new File(Defaults.getUserFolder() + propFileName);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            f = new File(propFileName);

        }

        OutputStream out;
        try {
            out = new FileOutputStream(f);
            prop.store(out, "Updated");
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public List<Point> getFromPoints() {
        List<Point> from = new ArrayList<Point>();

        from.add(new Point(Double.parseDouble(this.fromUpperLeftX), Double.parseDouble(this.fromUpperLeftY)));
        from.add(new Point(Double.parseDouble(this.fromUpperRightX), Double.parseDouble(this.fromUpperRightX)));
        from.add(new Point(Double.parseDouble(this.fromDownRightX), Double.parseDouble(this.fromDownRightY)));
        from.add(new Point(Double.parseDouble(this.fromDownLeftX), Double.parseDouble(this.fromDownLeftY)));

        return from;
    }

    public List<Point> getToPoints() {
        List<Point> to = new ArrayList<Point>();

        to.add(new Point(Double.parseDouble(this.toUpperLeftX), Double.parseDouble(this.toUpperLeftY)));
        to.add(new Point(Double.parseDouble(this.toUpperRightX), Double.parseDouble(this.toUpperRightX)));
        to.add(new Point(Double.parseDouble(this.toDownRightX), Double.parseDouble(this.toDownRightY)));
        to.add(new Point(Double.parseDouble(this.toDownLeftX), Double.parseDouble(this.toDownLeftY)));

        return to;
    }
}
