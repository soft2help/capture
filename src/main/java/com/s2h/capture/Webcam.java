package com.s2h.capture;

import com.s2h.capture.Helpers.KeyListener;
import com.s2h.capture.Helpers.Log;
import com.s2h.capture.Helpers.Settings;
import com.s2h.capture.gui.configs.SettingsShortcutsPathNaming;
import com.s2h.capture.gui.main.CaptureManual;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javafx.application.*;
import org.jnativehook.GlobalScreen;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.InputStream;
import com.s2h.capture.Helpers.Defaults;

public class Webcam extends Application {
    private Stage main, settingsSPN, settingsCrop, manualCrop;
    private SystemTray tray;
    private TrayIcon trayIcon;
    private CaptureManual captureManual;
    private KeyListener keyListener;
    public SettingsShortcutsPathNaming settingsSPNController;

    public static Settings preferencias = new Settings();
    public static Logger log = Log.getLog(Webcam.class.getName());

    public Stage mainStage() throws Exception {
        main = new Stage(StageStyle.TRANSPARENT);
        main.initStyle(StageStyle.UTILITY);

        StackPane rootMain = new StackPane();
        rootMain.setStyle("-fx-background-color: TRANSPARENT");

        Scene mainScene = new Scene(rootMain, 1, 1);
        mainScene.getStylesheets().add(Defaults.getMainCss());
        mainScene.setFill(Color.TRANSPARENT);
        main.setScene(mainScene);
        main.setWidth(1);
        main.setHeight(1);
        main.setX(Double.MAX_VALUE);
        main.toBack();

        return main;

    }

    public Stage settingsSPNStage() throws Exception {
        // configurador

        settingsSPN = new Stage();
        FXMLLoader settingsSPNLoader = new FXMLLoader(
                getClass().getClassLoader().getResource("fxml/configs/SettingsShortcutsPathNaming.fxml"));
        Parent rootSPN = settingsSPNLoader.load();

        Scene sceneSPN = new Scene(rootSPN);
        settingsSPNController = settingsSPNLoader.getController();
        settingsSPNController.setWebcamController(this);

        sceneSPN.getStylesheets().add(Defaults.getMainCss());

        settingsSPN.initStyle(StageStyle.UNDECORATED);
        settingsSPN.setScene(sceneSPN);
        settingsSPN.setTitle("Settings Shortcuts Path Naming");
        settingsSPN.getIcons().add(Defaults.getIcon());

        return settingsSPN;

    }

    public Stage settingsCropStage() throws Exception {
        // configurador
        settingsCrop = new Stage();
        FXMLLoader settingsCropLoader = new FXMLLoader(
                getClass().getClassLoader().getResource("fxml/configs/SettingsCrop.fxml"));
        Parent rootSettingsCrop = settingsCropLoader.load();

        Scene sceneSettingsCrop = new Scene(rootSettingsCrop);

        sceneSettingsCrop.getStylesheets().add(Defaults.getMainCss());

        settingsCrop.initStyle(StageStyle.UNDECORATED);
        settingsCrop.setScene(sceneSettingsCrop);
        settingsCrop.setTitle("Settings Crop");
        settingsCrop.getIcons().add(Defaults.getIcon());

        return settingsCrop;

    }

    public Stage manualCropStage() throws Exception {
        // configurador
        manualCrop = new Stage();
        FXMLLoader manualCropLoader = new FXMLLoader(
                getClass().getClassLoader().getResource("fxml/main/CaptureManual.fxml"));
        Parent rootManualCrop = manualCropLoader.load();

        captureManual = manualCropLoader.getController();

        Scene sceneManualCrop = new Scene(rootManualCrop);

        sceneManualCrop.getStylesheets().add(Defaults.getMainCss());

        manualCrop.initStyle(StageStyle.UNDECORATED);
        manualCrop.setScene(sceneManualCrop);
        manualCrop.setTitle("Manual Crop");
        manualCrop.getIcons().add(Defaults.getIcon());

        return manualCrop;
    }

    public void setKeyListener() {
        if (keyListener != null)
            GlobalScreen.removeNativeKeyListener(keyListener);

        keyListener = new KeyListener();
        GlobalScreen.addNativeKeyListener(keyListener);
    }

    @Override
    public void start(Stage stage) throws Exception {

        Platform.setImplicitExit(false);
        mainStage();

        settingsSPNStage();
        settingsCropStage();
        manualCropStage();

        javax.swing.SwingUtilities.invokeLater(this::addAppToTray);

        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false);

        setKeyListener();

    }

    @Override
    public void stop() throws Exception {
        tray.remove(trayIcon);
        Platform.exit();
        keyListener.close();
        super.stop();
    }

    private void addAppToTray() {
        try {
            Toolkit.getDefaultToolkit();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("img/icon.png");
            Image image = ImageIO.read(inputStream);

            tray = SystemTray.getSystemTray();
            trayIcon = new TrayIcon(image);

            // Abrir en doble click
            trayIcon.addActionListener(event -> Platform.runLater(this::showManualStage));

            MenuItem manualCropMenu = new MenuItem("   Manual Crop ");
            manualCropMenu.addActionListener(event -> Platform.runLater(this::showManualStage));
            manualCropMenu.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

            // Abrir configurar
            MenuItem settingsSPNMenu = new MenuItem("   Global Settings ");
            settingsSPNMenu.addActionListener(event -> Platform.runLater(this::showSettingsSPN));
            settingsSPNMenu.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

            // Abrir preferencias
            MenuItem cropSettingsMenu = new MenuItem("   Crop Settings");
            cropSettingsMenu.addActionListener(event -> Platform.runLater(this::showSettingsCrop));
            cropSettingsMenu.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

            // Tomar captura
            MenuItem captureMenu = new MenuItem("   Take a Capture");
            captureMenu.addActionListener(event -> {
                captureManual.getImages();
            });
            captureMenu.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

            // Salir
            MenuItem exitMenu = new MenuItem("   Exit");
            exitMenu.addActionListener(event -> {
                Platform.exit();
                tray.remove(trayIcon);
                keyListener.close();
            });
            exitMenu.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

            final PopupMenu popup = new PopupMenu();
            popup.add(manualCropMenu);
            popup.add(captureMenu);
            popup.addSeparator();
            popup.add(settingsSPNMenu);
            popup.add(cropSettingsMenu);
            popup.add(exitMenu);

            trayIcon.setPopupMenu(popup);
            tray.add(trayIcon);
        } catch (AWTException | IOException e) {
            e.printStackTrace();
        }
    }

    private void showManualStage() {

        if (manualCrop != null) {
            manualCrop.show();
            manualCrop.toFront();
        }
    }

    private void showSettingsCrop() {
        if (settingsCrop != null) {
            settingsCrop.show();
            settingsCrop.toFront();
        }
    }

    private void showSettingsSPN() {
        if (settingsSPN != null) {
            settingsSPN.show();
            settingsSPN.toFront();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
