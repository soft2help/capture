package com.s2h.capture.gui.configs;

import javafx.application.Application;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import com.s2h.capture.Helpers.Settings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import com.s2h.capture.Helpers.Notification;
import com.s2h.capture.Helpers.Defaults;
import com.s2h.capture.Helpers.Log;
import com.s2h.capture.Webcam;

public class SettingsShortcutsPathNaming extends Application implements Initializable {
    public Settings settings = new Settings();
    public Settings settingsAux = new Settings();
    public static Logger log = Log.getLog(SettingsShortcutsPathNaming.class.getName());

    private Webcam WebcamController;
    private Stage stage;

    @FXML
    private AnchorPane anchor;

    @FXML
    private JFXCheckBox captureAll;

    @FXML
    private TextField captureAllKey;

    @FXML
    private TextField captureAllSavePath;

    @FXML
    private TextField captureAllNameComplete;

    @FXML
    private TextField captureAllNameCropped;

    @FXML
    private JFXCheckBox captureComplete;

    @FXML
    private TextField captureCompleteKey;

    @FXML
    private TextField captureCompleteSavePath;

    @FXML
    private TextField captureCompleteName;

    @FXML
    private JFXCheckBox captureCropped;

    @FXML
    private TextField captureCroppedKey;

    @FXML
    private TextField captureCroppedSavePath;

    @FXML
    private TextField captureCroppedName;

    @FXML
    private JFXButton saveSettings;

    public void setWebcamController(Webcam WebcamController) {
        WebcamController = WebcamController;
    }

    private File fileExplorer() {
        stage = stage == null ? getStage() : stage;
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Path");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = directoryChooser.showDialog(stage);
        return file;
    }

    private Stage getStage() {
        return (Stage) anchor.getScene().getWindow();
    }

    @FXML
    void asignCaptureAllKey(KeyEvent event) {
        log.info("Asign capture all settings to a keyboard key");
        captureAllKey.setText(event.getCode().toString());
        settingsAux.captureAllKey = event.getCode().toString();
        settingsAux.captureAllKeyCode = "" + event.getCode();
    }

    @FXML
    void asignCaptureAllPath(MouseEvent event) {
        log.info("Asign capture all images to an folder to save");
        File file = fileExplorer();
        if (file != null) {
            settingsAux.captureAllSavePath = file.getAbsolutePath() + "\\";
            captureAllSavePath.setText(settingsAux.captureAllSavePath);
        }
    }

    @FXML
    void asignCaptureCompleteKey(KeyEvent event) {
        log.info("Asign capture Complete settings to a keyboard key");
        captureCompleteKey.setText(event.getCode().toString());
        settingsAux.captureCompleteKey = event.getCode().toString();
        settingsAux.captureCompleteKeyCode = "" + event.getCode();
    }

    @FXML
    void asignCaptureCompletePath(MouseEvent event) {
        log.info("Asign capture complete image to an folder to save");
        File file = fileExplorer();
        if (file != null) {
            settingsAux.captureCompleteSavePath = file.getAbsolutePath() + "\\";
            captureCompleteSavePath.setText(settingsAux.captureCompleteSavePath);
        }
    }

    @FXML
    void asignCaptureCroppedKey(KeyEvent event) {
        log.info("Asign capture cropped settings to a keyboard key");
        captureCroppedKey.setText(event.getCode().toString());
        settingsAux.captureCroppedKey = event.getCode().toString();
        settingsAux.captureCroppedKeyCode = "" + event.getCode();
    }

    @FXML
    void asignCaptureCroppedPath(MouseEvent event) {
        log.info("Asign capture cropped image to an folder to save");
        File file = fileExplorer();
        if (file != null) {
            settingsAux.captureCroppedSavePath = file.getAbsolutePath() + "\\";
            captureCroppedSavePath.setText(settingsAux.captureCroppedSavePath);
        }
    }

    @FXML
    void saveSettings(ActionEvent event) {
        log.info("Save settings");
        saveCaptureAll();
        saveCaptureComplete();
        saveCaptureCropped();
        if (WebcamController != null)
            WebcamController.setKeyListener();
    }

    void saveCaptureAll() {
        if (!captureAll.isSelected()) {
            settings.captureAll = false;
            settings.update();
            return;
        }

        Boolean isError = false;
        if (captureAllKey.getText().isEmpty()) {
            isError = true;
            captureAllKey.getStyleClass().add("errorInput");
        }

        if (captureAllSavePath.getText().isEmpty()) {
            isError = true;
            captureAllSavePath.getStyleClass().add("errorInput");
        }

        if (captureAllNameComplete.getText().isEmpty()) {
            isError = true;
            captureAllNameComplete.getStyleClass().add("errorInput");
        }

        if (captureAllNameCropped.getText().isEmpty()) {
            isError = true;
            captureAllNameCropped.getStyleClass().add("errorInput");
        }

        if (!isError) {
            captureAllKey.getStyleClass().removeAll("errorInput");
            captureAllSavePath.getStyleClass().removeAll("errorInput");
            captureAllNameComplete.getStyleClass().removeAll("errorInput");
            captureAllNameCropped.getStyleClass().removeAll("errorInput");

            settings.captureAll = true;
            if (settingsAux.captureAllKey != null)
                settings.captureAllKey = settingsAux.captureAllKey;

            if (settingsAux.captureAllKeyCode != null)
                settings.captureAllKeyCode = settingsAux.captureAllKeyCode;

            if (settingsAux.captureAllSavePath != null)
                settings.captureAllSavePath = settingsAux.captureAllSavePath;

            if (settingsAux.captureAllNameComplete != null)
                settings.captureAllNameComplete = settingsAux.captureAllNameComplete;

            if (settingsAux.captureAllNameCropped != null)
                settings.captureAllNameCropped = settingsAux.captureAllNameCropped;

            settings.update();
            log.info("Section Capture All Saved");
            Notification.info("Section Capture All Saved");
        } else {
            log.warning("There are errors in capture All Settings, this section was not saved");
            Notification.error("There are errors in capture All Settings, this section was not saved");
        }

    }

    void saveCaptureComplete() {
        if (!captureComplete.isSelected()) {
            settings.captureComplete = false;
            settings.update();
            return;
        }

        Boolean isError = false;
        if (captureCompleteKey.getText().isEmpty()) {
            isError = true;

            captureCompleteKey.getStyleClass().add("errorInput");
        }

        if (captureCompleteSavePath.getText().isEmpty()) {
            isError = true;

            captureCompleteSavePath.getStyleClass().add("errorInput");
        }

        if (captureCompleteName.getText().isEmpty()) {
            isError = true;
            captureCompleteName.getStyleClass().add("errorInput");
        }

        if (!isError) {
            captureCompleteKey.getStyleClass().removeAll("errorInput");
            captureCompleteSavePath.getStyleClass().removeAll("errorInput");

            captureCompleteName.getStyleClass().removeAll("errorInput");

            settings.captureComplete = true;

            if (settingsAux.captureCompleteKey != null)
                settings.captureCompleteKey = settingsAux.captureCompleteKey;

            if (settingsAux.captureCompleteKeyCode != null)
                settings.captureCompleteKeyCode = settingsAux.captureCompleteKeyCode;

            if (settingsAux.captureCompleteSavePath != null)
                settings.captureCompleteSavePath = settingsAux.captureCompleteSavePath;

            if (settingsAux.captureCompleteName != null)
                settings.captureCompleteName = settingsAux.captureCompleteName;

            settings.update();
            log.info("Section Capture Complete Saved");
            Notification.info("Section Capture Complete Saved");
        } else {
            log.warning("There are errors in capture Complete Settings, this section was not saved");
            Notification.error("There are errors in capture Complete Settings, this section was not saved");
        }

    }

    void saveCaptureCropped() {
        if (!captureCropped.isSelected()) {
            settings.captureCropped = false;
            settings.update();
            return;
        }

        Boolean isError = false;
        if (captureCroppedKey.getText().isEmpty()) {
            isError = true;
            captureCroppedKey.getStyleClass().add("errorInput");
        }

        if (captureCroppedSavePath.getText().isEmpty()) {
            isError = true;
            captureCroppedSavePath.getStyleClass().add("errorInput");
        }

        if (captureCroppedName.getText().isEmpty()) {
            isError = true;
            captureCroppedName.getStyleClass().add("errorInput");
        }

        if (!isError) {
            captureCroppedKey.getStyleClass().removeAll("errorInput");
            captureCroppedSavePath.getStyleClass().removeAll("errorInput");
            captureCroppedName.getStyleClass().removeAll("errorInput");

            settings.captureCropped = true;

            if (settingsAux.captureCroppedKey != null)
                settings.captureCroppedKey = settingsAux.captureCroppedKey;

            if (settingsAux.captureCroppedKeyCode != null)
                settings.captureCroppedKeyCode = settingsAux.captureCroppedKeyCode;

            if (settingsAux.captureCroppedSavePath != null)
                settings.captureCroppedSavePath = settingsAux.captureCroppedSavePath;

            if (settingsAux.captureCroppedName != null)
                settings.captureCroppedName = settingsAux.captureCroppedName;

            settings.update();
            log.info("Section Capture Cropped Saved");
            Notification.info("Section Capture Cropped Saved");
        } else {
            log.warning("There are errors in capture Cropped Settings, this section was not saved");
            Notification.error("There are errors in capture Cropped Settings, this section was not saved");
        }

    }

    void isEnabledCaptureAll() {
        captureAllKey.setDisable(!captureAll.isSelected());
        captureAllSavePath.setDisable(!captureAll.isSelected());
        captureAllNameComplete.setDisable(!captureAll.isSelected());
        captureAllNameCropped.setDisable(!captureAll.isSelected());
    }

    void isEnabledCaptureComplete() {
        captureCompleteKey.setDisable(!captureComplete.isSelected());
        captureCompleteSavePath.setDisable(!captureComplete.isSelected());
        captureCompleteName.setDisable(!captureComplete.isSelected());
    }

    void isEnabledCaptureCropped() {
        captureCroppedKey.setDisable(!captureCropped.isSelected());
        captureCroppedSavePath.setDisable(!captureCropped.isSelected());
        captureCroppedName.setDisable(!captureCropped.isSelected());
    }

    private void initFields() {
        log.info("Initialization Gui components from settings");

        captureAll.setSelected(settings.captureAll);
        captureAllKey.setText(settings.captureAllKey);
        captureAllSavePath.setText(settings.captureAllSavePath);
        captureAllNameComplete.setText(settings.captureAllNameComplete);
        captureAllNameCropped.setText(settings.captureAllNameCropped);

        captureComplete.setSelected(settings.captureComplete);
        captureCompleteKey.setText(settings.captureCompleteKey);
        captureCompleteSavePath.setText(settings.captureCompleteSavePath);
        captureCompleteName.setText(settings.captureCompleteName);

        captureCropped.setSelected(settings.captureCropped);
        captureCroppedKey.setText(settings.captureCroppedKey);
        captureCroppedSavePath.setText(settings.captureCroppedSavePath);
        captureCroppedName.setText(settings.captureCroppedName);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initFields();

        isEnabledCaptureAll();
        isEnabledCaptureComplete();
        isEnabledCaptureCropped();

        captureAll.selectedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                isEnabledCaptureAll();
            }
        });

        captureComplete.selectedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                isEnabledCaptureComplete();
            }
        });

        captureCropped.selectedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                isEnabledCaptureCropped();
            }
        });

    }

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("SettingsShortcutsPathNaming.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Defaults.getMainCss());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.setTitle("Settings Shortcuts Path Naming");
        stage.getIcons().add(Defaults.getIcon());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
