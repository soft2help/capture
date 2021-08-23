package com.s2h.capture.gui.configs;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;

import com.jfoenix.controls.JFXButton;

import javafx.application.Application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import com.jfoenix.controls.JFXCheckBox;

import com.s2h.capture.Helpers.CaptureImage;
import com.s2h.capture.Helpers.CropAndPrespectiveCorrection;
import com.s2h.capture.Helpers.Defaults;
import com.s2h.capture.Helpers.Notification;
import com.s2h.capture.Helpers.Point;
import com.s2h.capture.Helpers.PointsHelper;
import com.s2h.capture.Helpers.Settings;
import com.s2h.capture.Helpers.Log;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import com.s2h.capture.gui.includes.Header;
import java.util.logging.Logger;

public class GetCropSettings extends Application implements Initializable {
    public static Settings settings = new Settings();
    public static Logger log = Log.getLog(GetCropSettings.class.getName());

    private GetImage threadGetImage;
    private Stage principal;
    Timer timer;
    private Circle actualPoint;
    private List<Point> points = new ArrayList<Point>();
    private int selectedPoint = -1;
    private double scaleX, scaleY;
    private Point upperLeft, downRight;
    private int widthCropped, heightCropped;
    private int startX, startY;
    private SettingsCrop settingsCrop;

    List<Point> areaConverted = new ArrayList<Point>();
    List<Point> toPrespective = new ArrayList<Point>();

    @FXML
    private AnchorPane header;

    @FXML
    private Header headerController;

    @FXML
    private AnchorPane separator1;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Pane contenedor;

    @FXML
    private ImageView imageViewer;

    @FXML
    private Pane drawer;

    @FXML
    private TextField txtRtspIpCam;

    @FXML
    private JFXButton check;

    @FXML
    private JFXButton saveIp;

    @FXML
    private JFXCheckBox prespectiveCorrection;

    @FXML
    private JFXButton saveConfiguration;

    @FXML
    private JFXButton crop;

    @FXML
    private JFXCheckBox flipHorizontal;

    @FXML
    private AnchorPane cropPanel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private JFXButton deleteSelection;

    public void setSettingsCrop(SettingsCrop settingsCrop) {
        this.settingsCrop = settingsCrop;
    }

    void captureImage() {
        if (threadGetImage != null && threadGetImage.isAlive())
            threadGetImage.interrupt();

        threadGetImage = new GetImage();
        threadGetImage.start();

    }

    @FXML
    void check(ActionEvent event) {
        captureImage();
    }

    void checkPoints() {
        if (points.size() == 4) {
            prespectiveCorrection.setVisible(true);
            crop.setVisible(true);
            saveConfiguration.setVisible(true);
        } else {
            prespectiveCorrection.setVisible(false);
            crop.setVisible(false);
            saveConfiguration.setVisible(false);
        }
    }

    void cleanSelectionPoints() {
        drawer.getChildren().clear();
        points.clear();
        checkPoints();
    }

    @FXML
    void deleteSelection(ActionEvent event) {
        cleanSelectionPoints();
    }

    @FXML
    void saveConfiguration(ActionEvent event) {
        settings.ip = txtRtspIpCam.getText();
        settings.flipHorizontal = flipHorizontal.isSelected();

        if (points.size() > 0) {
            settings.cropImage = true;
            settings.prespectiveCorrection = prespectiveCorrection.isSelected();

            settings.cropUpperLeftX = String.valueOf(this.startX);
            settings.cropUpperLeftY = String.valueOf(this.startY);
            settings.cropWidth = String.valueOf(this.widthCropped);
            settings.cropHeight = String.valueOf(this.heightCropped);

            if (settings.prespectiveCorrection && this.areaConverted.size() > 0) {
                settings.fromUpperLeftX = String.valueOf((int) this.areaConverted.get(0).x);
                settings.fromUpperLeftY = String.valueOf((int) this.areaConverted.get(0).y);

                settings.toUpperLeftX = String.valueOf((int) this.toPrespective.get(0).x);
                settings.toUpperLeftY = String.valueOf((int) this.toPrespective.get(0).y);

                /////////////////////////////////////////////////////

                settings.fromUpperRightX = String.valueOf((int) this.areaConverted.get(1).x);
                settings.fromUpperRightY = String.valueOf((int) this.areaConverted.get(1).y);

                settings.toUpperRightX = String.valueOf((int) this.toPrespective.get(1).x);
                settings.toUpperRightY = String.valueOf((int) this.toPrespective.get(1).y);
                /////////////////////////////////////

                settings.fromDownRightX = String.valueOf((int) this.areaConverted.get(2).x);
                settings.fromDownRightY = String.valueOf((int) this.areaConverted.get(2).y);

                settings.toDownRightX = String.valueOf((int) this.toPrespective.get(2).x);
                settings.toDownRightY = String.valueOf((int) this.toPrespective.get(2).y);

                settings.fromDownLeftX = String.valueOf((int) this.areaConverted.get(3).x);
                settings.fromDownLeftY = String.valueOf((int) this.areaConverted.get(3).y);

                settings.toDownLeftX = String.valueOf((int) this.toPrespective.get(3).x);
                settings.toDownLeftY = String.valueOf((int) this.toPrespective.get(3).y);
            }
        }

        log.info("Crop Settings was saved");
        Notification.success("Crop Setting was saved");
        settings.update();

    }

    Boolean cropImage() {
        Image original = imageViewer.getImage();

        double imWidth = imageViewer.getLayoutBounds().getWidth();
        double imHeight = imageViewer.getLayoutBounds().getHeight();

        this.scaleX = original.getHeight() / imHeight;
        this.scaleY = original.getWidth() / imWidth;

        this.upperLeft = PointsHelper.findUpperLeftPoint(points);
        this.downRight = PointsHelper.findDownRightPoint(points);

        this.widthCropped = (int) (PointsHelper.widthBetweenPoints(upperLeft, downRight) * scaleX);
        this.heightCropped = (int) (PointsHelper.heigthBetweenPoints(upperLeft, downRight) * scaleY);

        this.startX = (int) (upperLeft.x * scaleX);
        this.startY = (int) (upperLeft.y * scaleY);

        CropAndPrespectiveCorrection cropAndPrespective = new CropAndPrespectiveCorrection();

        Image cropped = cropAndPrespective.setOriginal(original)
                .crop(this.startX, this.startY, this.widthCropped, this.heightCropped).getCropped();

        imageViewer.setImage(cropped);
        log.info("Cropping image");
        if (prespectiveCorrection.isSelected())
            return prespectiveCorrection(cropAndPrespective);

        return true;
    }

    Boolean prespectiveCorrection(CropAndPrespectiveCorrection cropAndPrespective) {
        List<Point> maxAreaCropped = PointsHelper.cropPolygon(this.upperLeft, this.downRight, this.scaleX, this.scaleY);
        String savePathCroppedFile = Defaults.getTemp();

        this.areaConverted = PointsHelper.convert(maxAreaCropped, points, scaleX, scaleY);
        this.toPrespective = PointsHelper.toPrespective(this.widthCropped, this.heightCropped);

        Image imagePrespectiveCorrection = cropAndPrespective
                .prespectiveCorrection(savePathCroppedFile, this.areaConverted, this.toPrespective)
                .getImagePrespectiveCorrection();

        progressBar.setVisible(false);
        log.info("Prespective Correction in image");

        if (imagePrespectiveCorrection.getHeight() <= 0.0) {
            log.warning("There was an error, trying to correct prespective");
            Notification.error("There was an error, trying to correct prespective");
            return false;
        }
        (new File(savePathCroppedFile)).delete();
        imageViewer.setImage(imagePrespectiveCorrection);

        return true;
    }

    @FXML
    void crop(ActionEvent event) {
        if (cropImage()) {
            drawer.getChildren().clear();
            middleImageViewer();
            cropPanel.setVisible(false);
            saveConfiguration.setVisible(true);
        }

    }

    public void drawAll() {
        drawer.getChildren().clear();
        for (int i = 0; i < points.size(); i++) {
            actualPoint = new Circle();
            actualPoint.setId("" + i);
            actualPoint.setRadius(10.0f);
            actualPoint.setFill(Color.DARKCYAN);
            actualPoint.setStrokeWidth(8.0);
            actualPoint.setStroke(Color.DARKSLATEGREY);

            actualPoint.setOnMousePressed(e -> {

                if (points.size() > 3)
                    selectedPoint = Integer.parseInt(((Circle) e.getSource()).getId());
            });

            drawer.getChildren().add(actualPoint);

            actualPoint.setLayoutX(points.get(i).x);
            actualPoint.setLayoutY(points.get(i).y);
            actualPoint.toFront();

            if (i > 0) {
                Line line = new Line();
                line.setStrokeWidth(3);
                line.setStroke(Color.web("#eda678"));

                line.setStartX(points.get(i - 1).x);
                line.setStartY(points.get(i - 1).y);

                line.setEndX(points.get(i).x);
                line.setEndY(points.get(i).y);
                drawer.getChildren().add(line);
                if (i == 3) {
                    Line lastLine = new Line();
                    lastLine.setStrokeWidth(3);
                    lastLine.setStroke(Color.web("#eda678"));
                    lastLine.setStartX(points.get(i).x);
                    lastLine.setStartY(points.get(i).y);

                    lastLine.setEndX(points.get(0).x);
                    lastLine.setEndY(points.get(0).y);
                    drawer.getChildren().add(lastLine);

                }

                line.toFront();
            }

        }

    }

    @FXML
    void initSelection(MouseEvent event) {
        if (points.size() > 3)
            return;

        points.add(new Point(event.getX(), event.getY()));
        drawAll();
        checkPoints();
    }

    @FXML
    void doSelection(MouseEvent event) {
        int actual = points.size() - 1;
        if (selectedPoint >= 0)
            actual = selectedPoint;

        Point point = points.get(actual);
        point.x = event.getX();
        point.y = event.getY();
        points.set(actual, point);
        drawAll();

    }

    @FXML
    void endSelection(MouseEvent event) {
        if (points.size() == 4) {
            points = PointsHelper.sortVerticies(points);
            drawAll();
        }

    }

    void middleImageViewer() {
        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            double middleX = (contenedor.getWidth() / 2.0) - (imageViewer.getLayoutBounds().getWidth() / 2.0);
            double middleY = (contenedor.getHeight() / 2.0) - (imageViewer.getLayoutBounds().getHeight() / 2.0);

            Platform.runLater(() -> {
                drawer.setPrefWidth(imageViewer.getLayoutBounds().getWidth());
                drawer.setPrefHeight(imageViewer.getLayoutBounds().getHeight());
                drawer.setLayoutX(middleX);
                drawer.setLayoutY(middleY);

                imageViewer.setLayoutX(middleX);
                imageViewer.setLayoutY(middleY);

            });
        }).start();
    }

    void loadSettings() {
        log.info("Read settings...");
        txtRtspIpCam.setText(settings.ip);
        flipHorizontal.setSelected(settings.flipHorizontal);
        prespectiveCorrection.setSelected(settings.prespectiveCorrection);

        if (!settings.ip.isEmpty()) {
            log.info("Trying to automatic get image from device (because ip settings its configured)...");
            captureImage();
        }

    }

    void startGetGropSettings() {
        loadSettings();
        saveConfiguration.setVisible(false);

        imageViewer.setImage(null);
        progressBar.setVisible(false);
        cropPanel.setVisible(false);
        checkPoints();
        drawer.toFront();
        drawer.setOpacity(1.0);
        imageViewer.setPreserveRatio(true);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        startGetGropSettings();
    }

    public Stage stage(Stage stage) throws Exception {
        Boolean setClose = true;

        if (stage == null) {
            setClose = false;
            stage = new Stage();
        }
        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("fxml/configs/GetCropSettings.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Defaults.getMainCss());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.getIcons().add(Defaults.getIcon());
        if (setClose) {
            stage.setOnCloseRequest(e -> System.exit(0));
        } else {
            if (settingsCrop != null) {
                stage.setOnHiding(event -> {
                    log.info("Closing Get Crop Settings frame, and update Crop Settings Frame");
                    settingsCrop.initFields();
                });

                stage.setOnShowing(event -> {
                    log.info("Showing Get Crop Settings frame and restart configuration");
                    // startGetGropSettings();
                });
            }
        }
        return stage;

    }

    @Override
    public void start(Stage stage) throws Exception {
        principal = stage(stage);
        principal.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public class GetImage extends Thread {
        public void run() {
            imageViewer.setImage(null);
            progressBar.setVisible(true);
            cropPanel.setVisible(false);
            saveConfiguration.setVisible(false);

            CaptureImage capImage = new CaptureImage();
            String savePathCapture = Defaults.getTemp();

            try {
                Image imageFromCam = capImage.setPathImage(savePathCapture).setUrlStream(settings.ip).getFromFFMPEG()
                        .getImage(flipHorizontal.isSelected());

                progressBar.setVisible(false);

                if (imageFromCam.getHeight() <= 0.0) {
                    Notification.error("It was not posible capture image from ip device");
                    return;
                }

                imageViewer.setImage(imageFromCam);
                middleImageViewer();

                saveConfiguration.setVisible(true);

                cropPanel.setVisible(true);
                prespectiveCorrection.setSelected(false);
                if (areaConverted != null)
                    areaConverted.clear();
                if (toPrespective != null)
                    toPrespective.clear();
                if (points != null)
                    points.clear();
                (new File(savePathCapture)).delete();
            } catch (Exception e) {
                progressBar.setVisible(false);
                e.printStackTrace();
                Notification.error("Not posible capture image from ip device " + e.getMessage());
            }

        }
    }

}
