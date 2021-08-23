package com.s2h.capture.gui.configs;

import com.jfoenix.controls.JFXButton;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.s2h.capture.Webcam;
import com.s2h.capture.Helpers.Notification;
import com.s2h.capture.Helpers.Settings;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jfoenix.controls.JFXCheckBox;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

import com.s2h.capture.Helpers.Defaults;
import com.s2h.capture.Helpers.Log;

public class SettingsCrop extends Application implements Initializable {
    public static Settings settings = new Settings();
    public GetCropSettings getCropSettings;
    public Stage cropSettingsStage;
    public static Logger log = Log.getLog(SettingsCrop.class.getName());

    @FXML
    private AnchorPane anchor;

    @FXML
    private TextField ip;

    @FXML
    private JFXCheckBox flipHorizontal;

    @FXML
    private AnchorPane cropAreaSettings;

    @FXML
    private TextField cropUpperLeftX;

    @FXML
    private TextField cropUpperLeftY;

    @FXML
    private TextField cropWidth;

    @FXML
    private TextField cropHeight;

    @FXML
    private AnchorPane selectCropPanel;

    @FXML
    private JFXCheckBox cropImage;

    @FXML
    private AnchorPane prespectiveAreaSettings;

    @FXML
    private TextField fromUpperLeftX;

    @FXML
    private TextField fromUpperLeftY;

    @FXML
    private TextField toUpperLeftX;

    @FXML
    private TextField toUpperLeftY;

    @FXML
    private TextField fromUpperRightX;

    @FXML
    private TextField fromUpperRightY;

    @FXML
    private TextField toUpperRightX;

    @FXML
    private TextField toUpperRightY;

    @FXML
    private TextField fromDownRightX;

    @FXML
    private TextField fromDownRightY;

    @FXML
    private TextField toDownRightX;

    @FXML
    private TextField toDownRightY;

    @FXML
    private TextField fromDownLeftX;

    @FXML
    private TextField fromDownLeftY;

    @FXML
    private TextField toDownLeftX;

    @FXML
    private TextField toDownLeftY;

    @FXML
    private AnchorPane selectPrespectivePanel;

    @FXML
    private JFXCheckBox prespectiveCorrection;

    @FXML
    private AnchorPane saveSettingsPanel;

    @FXML
    private JFXButton saveSettings;

    @FXML
    private JFXButton needHelp;

    @FXML
    void needHelp(ActionEvent event) {
        if (cropSettingsStage == null) {
            try {
                getCropSettingStage();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cropSettingsStage.toFront();
        cropSettingsStage.show();

    }

    public Stage getCropSettingStage() throws Exception {
        // configurador

        cropSettingsStage = new Stage();
        FXMLLoader cropSettingsLoader = new FXMLLoader(
                getClass().getClassLoader().getResource("fxml/configs/GetCropSettings.fxml"));
        Parent rootCropSettings = cropSettingsLoader.load();
        getCropSettings = cropSettingsLoader.getController();
        Scene sceneCropSettings = new Scene(rootCropSettings);

        sceneCropSettings.getStylesheets().add(Defaults.getMainCss());

        cropSettingsStage.initStyle(StageStyle.UNDECORATED);
        cropSettingsStage.setScene(sceneCropSettings);
        cropSettingsStage.setTitle("Get Crop Settings");
        cropSettingsStage.getIcons().add(Defaults.getIcon());

        cropSettingsStage.setOnHiding(event -> {
            log.info("Closing Get Crop Settings frame, and update Crop Settings Frame");
            initFields();
        });

        return cropSettingsStage;

    }

    @FXML
    void saveSettings(ActionEvent event) {
        saveIpAndFlipHorizontal();
        saveCropSettings();
        settings.update();
    }

    void checkCropped() {
        cropAreaSettings.setVisible(cropImage.isSelected());
        selectPrespectivePanel.setVisible(cropImage.isSelected());
        checkPrespective();
        prespectiveAreaSettings.setVisible(cropImage.isSelected());
    }

    void checkPrespective() {
        prespectiveAreaSettings.setVisible(prespectiveCorrection.isSelected());
    }

    void saveIpAndFlipHorizontal() {
        String append = "";
        if (ip.getText().isEmpty()) {
            ip.getStyleClass().addAll("inputError");
            Notification.error("Error saving ip");
        } else {
            ip.getStyleClass().removeAll("inputError");
            settings.ip = ip.getText();
            append = " and ip";
        }

        settings.flipHorizontal = flipHorizontal.isSelected();
        Notification.success("Flip Horizontal " + append + " was saved ");
    }

    void saveCropSettings() {
        settings.cropImage = cropImage.isSelected();
        String errors = "";
        Boolean errorCrop = false;
        if (cropImage.isSelected()) {
            try {
                Integer.parseInt(cropUpperLeftX.getText());
            } catch (NumberFormatException e) {
                errorCrop = true;
                errors += "Upper Left X should be a integer\n";
            }

            try {
                Integer.parseInt(cropUpperLeftY.getText());
            } catch (NumberFormatException e) {
                errorCrop = true;
                errors += "Upper Left Y should be a integer\n";
            }
            try {
                Integer.parseInt(cropWidth.getText());
            } catch (NumberFormatException e) {
                errorCrop = true;
                errors += "Width should be a integer\n";
            }

            try {
                Integer.parseInt(cropHeight.getText());
            } catch (NumberFormatException e) {
                errorCrop = true;
                errors += "Crop Height should be a integer\n";
            }

            if (!errorCrop) {
                settings.cropUpperLeftX = cropUpperLeftX.getText();
                settings.cropUpperLeftY = cropUpperLeftY.getText();
                settings.cropWidth = cropWidth.getText();
                settings.cropHeight = cropHeight.getText();
                Notification.success("The Crop Area Setting Was Saved");
            } else {
                Notification.error(errors);
            }
            savePrespectiveSettings();
        }

    }

    String saveUpperLeft() {
        String errors = "";
        try {
            Integer.parseInt(fromUpperLeftX.getText());
        } catch (NumberFormatException e) {
            errors += "From Upper Left X should be a integer\n";
        }

        try {
            Integer.parseInt(fromUpperLeftY.getText());
        } catch (NumberFormatException e) {
            errors += "From Upper Left Y should be a integer\n";
        }

        try {
            Integer.parseInt(toUpperLeftX.getText());
        } catch (NumberFormatException e) {
            errors += "To Upper Left X should be a integer\n";
        }

        try {
            Integer.parseInt(toUpperLeftY.getText());
        } catch (NumberFormatException e) {
            errors += "To Upper Left Y should be a integer\n";
        }

        if (errors.isEmpty()) {
            settings.fromUpperLeftX = fromUpperLeftX.getText();
            settings.fromUpperLeftY = fromUpperLeftY.getText();
            settings.toUpperLeftX = toUpperLeftX.getText();
            settings.toUpperLeftY = toUpperLeftY.getText();
        }

        return errors;
    }

    String saveUpperRight() {
        String errors = "";
        try {
            Integer.parseInt(fromUpperRightX.getText());
        } catch (NumberFormatException e) {
            errors += "From Upper Right X should be a integer\n";
        }

        try {
            Integer.parseInt(fromUpperRightY.getText());
        } catch (NumberFormatException e) {
            errors += "From Upper Right Y should be a integer\n";
        }

        try {
            Integer.parseInt(toUpperRightX.getText());
        } catch (NumberFormatException e) {
            errors += "To Upper Right X should be a integer\n";
        }

        try {
            Integer.parseInt(toUpperRightY.getText());
        } catch (NumberFormatException e) {
            errors += "To Upper Right Y should be a integer\n";
        }

        if (errors.isEmpty()) {
            settings.fromUpperRightX = fromUpperRightX.getText();
            settings.fromUpperRightY = fromUpperRightY.getText();
            settings.toUpperRightX = toUpperRightX.getText();
            settings.toUpperRightY = toUpperRightY.getText();
        }

        return errors;
    }

    String saveDownRight() {
        String errors = "";
        try {
            Integer.parseInt(fromDownRightX.getText());
        } catch (NumberFormatException e) {
            errors += "From Down Right X should be a integer\n";
        }

        try {
            Integer.parseInt(fromDownRightY.getText());
        } catch (NumberFormatException e) {
            errors += "From Down Right Y should be a integer\n";
        }

        try {
            Integer.parseInt(toDownRightX.getText());
        } catch (NumberFormatException e) {
            errors += "To Down Right X should be a integer\n";
        }

        try {
            Integer.parseInt(toDownRightY.getText());
        } catch (NumberFormatException e) {
            errors += "To Down Right Y should be a integer\n";
        }

        if (errors.isEmpty()) {
            settings.fromDownRightX = fromDownRightX.getText();
            settings.fromDownRightY = fromDownRightY.getText();
            settings.toDownRightY = toDownRightY.getText();
            settings.toDownRightY = toDownRightY.getText();
        }

        return errors;
    }

    String saveDownLeft() {
        String errors = "";
        try {
            Integer.parseInt(fromDownLeftX.getText());
        } catch (NumberFormatException e) {
            errors += "From Down Left X should be a integer\n";
        }

        try {
            Integer.parseInt(fromDownLeftY.getText());
        } catch (NumberFormatException e) {
            errors += "From Down Left Y should be a integer\n";
        }

        try {
            Integer.parseInt(toDownLeftX.getText());
        } catch (NumberFormatException e) {
            errors += "To Down Left X should be a integer\n";
        }

        try {
            Integer.parseInt(toDownLeftY.getText());
        } catch (NumberFormatException e) {
            errors += "To Down Left Y should be a integer\n";
        }

        if (errors.isEmpty()) {
            settings.fromDownLeftX = fromDownLeftX.getText();
            settings.fromDownLeftY = fromDownLeftY.getText();
            settings.toDownLeftX = toDownLeftX.getText();
            settings.toDownLeftY = toDownLeftY.getText();
        }

        return errors;
    }

    void savePrespectiveSettings() {
        settings.prespectiveCorrection = prespectiveCorrection.isSelected();
        String errors = "";

        if (prespectiveCorrection.isSelected()) {
            errors += saveUpperLeft();
            errors += saveUpperRight();
            errors += saveDownRight();
            errors += saveDownLeft();

            if (!errors.isEmpty()) {
                Notification.error(errors);
            }
        }

        Notification.success("Prepective Area Settings was saved");
    }

    public void initFields() {
        log.info("Init Crop Settings ");
        settings.read();
        ip.setText(settings.ip);
        flipHorizontal.setSelected(settings.flipHorizontal);

        cropImage.setSelected(settings.cropImage);

        cropUpperLeftX.setText(settings.cropUpperLeftX);
        cropUpperLeftY.setText(settings.cropUpperLeftY);
        cropWidth.setText(settings.cropWidth);
        cropHeight.setText(settings.cropHeight);

        prespectiveCorrection.setSelected(settings.prespectiveCorrection);

        fromUpperLeftX.setText(settings.fromUpperLeftX);
        fromUpperLeftY.setText(settings.fromUpperLeftY);
        toUpperLeftX.setText(settings.toUpperLeftX);
        toUpperLeftY.setText(settings.toUpperLeftY);

        fromUpperRightX.setText(settings.fromUpperRightX);
        fromUpperRightY.setText(settings.fromUpperRightY);
        toUpperRightX.setText(settings.toUpperRightX);
        toUpperRightY.setText(settings.toUpperRightY);

        fromDownRightX.setText(settings.fromDownRightX);
        fromDownRightY.setText(settings.fromDownRightY);
        toDownRightX.setText(settings.toDownRightX);
        toDownRightY.setText(settings.toDownRightY);

        fromDownLeftX.setText(settings.fromDownLeftX);
        fromDownLeftY.setText(settings.fromDownLeftY);
        toDownLeftX.setText(settings.toDownLeftX);
        toDownLeftY.setText(settings.toDownLeftY);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initFields();
        checkCropped();

        cropImage.selectedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                checkCropped();
            }
        });

        prespectiveCorrection.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                checkPrespective();
            }
        });

    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SettingsCrop.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Defaults.getMainCss());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.setTitle("Configs");
        stage.getIcons().add(Defaults.getIcon());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
