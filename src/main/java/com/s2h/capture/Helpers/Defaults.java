package com.s2h.capture.Helpers;

import java.io.File;

import javax.swing.filechooser.FileSystemView;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class Defaults {

    public static Image getIcon() {
        Image img = new javafx.scene.image.Image("img/appicon.png");
        return img;
    }

    public static String getMainCss() {
        return "style/style.css";
    }

    public static Stage getMagicScene() {
        Stage main = new Stage(StageStyle.UNDECORATED);
        main.initStyle(StageStyle.TRANSPARENT);

        StackPane rootMain = new StackPane();
        rootMain.setStyle("-fx-background-color: TRANSPARENT");

        Scene mainScene = new Scene(rootMain, Screen.getPrimary().getBounds().getWidth(),
                Screen.getPrimary().getBounds().getHeight());
        mainScene.getStylesheets().add(getMainCss());
        mainScene.setFill(Color.TRANSPARENT);
        main.setScene(mainScene);
        main.setWidth(Screen.getPrimary().getBounds().getWidth());
        main.setHeight(Screen.getPrimary().getBounds().getHeight());
        main.getIcons().add(getIcon());
        // main.setX(Double.MAX_VALUE);
        main.toFront();
        main.show();

        return main;
    }

    public static String getDesktop() {
        FileSystemView home = FileSystemView.getFileSystemView();
        return home.getHomeDirectory().getAbsolutePath() + "\\";
    }

    public static String getPath(String path, String fileName) {
        File f;
        int repeats = 0;
        while (true) {
            String savePath = path + fileName;
            if (repeats == 0) {
                f = new File(savePath + ".jpg");
                if (!f.exists() && !f.isDirectory())
                    return savePath + ".jpg";
            } else {
                f = new File(savePath + repeats + ".jpg");
                if (!f.exists() && !f.isDirectory())
                    return savePath + repeats + ".jpg";
            }
            repeats++;
        }

    }

    public static String getTemp() {
        return System.getProperty("java.io.tmpdir") + "/" + System.currentTimeMillis() + ".jpg";
    }

    public static Attributes getManifestAtributtes() {
        Manifest manifest;
        try {
            manifest = new Manifest(Defaults.class.getClassLoader().getResourceAsStream("META-INF/MANIFEST.MF"));

            return manifest.getMainAttributes();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static String getAppVendor() {
        String vendor = getManifestAtributtes().getValue("vendor");
        if (vendor == null)
            vendor = "soft2help";

        return vendor;
    }

    public static String getAppName() {
        String name = getManifestAtributtes().getValue("name");
        if (name == null)
            name = "capture";

        return name;
    }

    public static String getUserFolder() throws Exception {

        String path = System.getProperty("user.home") + File.separator + "." + getAppVendor() + File.separator
                + getAppName();

        File customDir = new File(path);

        if (customDir.exists()) {
            return path + File.separator;
        } else if (customDir.mkdirs()) {
            return path + File.separator;

        } else {
            throw new IOException("Check your Permissions");
        }
    }

}
