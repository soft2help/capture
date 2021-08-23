
package com.s2h.capture.gui.main;

import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.application.Application;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.File;
import javax.imageio.ImageIO;
import com.jfoenix.controls.JFXButton;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import com.s2h.capture.Helpers.CaptureImage;
import com.s2h.capture.Helpers.CropAndPrespectiveCorrection;
import com.s2h.capture.Helpers.Settings;
import com.s2h.capture.gui.includes.Header;

import com.jfoenix.controls.JFXCheckBox;
import com.s2h.capture.Helpers.Defaults;
import com.s2h.capture.Helpers.Notification;

/**
 * @author said
 */
public class CaptureManual extends Application implements Initializable {
    public Settings settings = new Settings();
    private Stage stage;
    private Stage principal;
    private String completeSavePath, croppedSavePath;
    public Task task;

    @FXML
    private AnchorPane header;

    @FXML
    private Header headerController;

    @FXML
    private AnchorPane anchor;

    @FXML
    private ImageView complete;

    @FXML
    private ImageView cropped;

    @FXML
    private JFXButton capture;

    @FXML
    private TextField completePath;

    @FXML
    private TextField croppedPath;

    @FXML
    private ProgressBar progressBarComplete;

    @FXML
    private ProgressBar progressBarCropped;

    @FXML
    private JFXCheckBox flipHorizontal;

    @FXML
    public void capture(ActionEvent event) {
        getImages();
    }

    public void getImages() {
        cropped.setImage(null);
        complete.setImage(null);
        progressBarComplete.setVisible(true);
        progressBarCropped.setVisible(true);
        task = new Task();
        task.start();
    }

    @FXML
    void completeExplorer(MouseEvent event) {
        File file = fileExplorer();
        if (file != null) {
            completeSavePath = file.getAbsolutePath() + "\\";
            completePath.setText(completeSavePath);
        }
    }

    @FXML
    void croppedExplorer(MouseEvent event) {
        File file = fileExplorer();
        if (file != null) {
            croppedSavePath = file.getAbsolutePath() + "\\";
            croppedPath.setText(croppedSavePath);
        }
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        completeSavePath = Defaults.getDesktop();
        croppedSavePath = Defaults.getDesktop();

        completePath.setText(completeSavePath);
        croppedPath.setText(croppedSavePath);
    }

    public Stage stage(Stage stage) throws Exception {
        Boolean setClose = false;

        if (stage == null) {
            setClose = true;
            stage = new Stage();
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("CaptureManual.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Defaults.getMainCss());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.setTitle("Manual Crop");
        stage.getIcons().add(Defaults.getIcon());
        if (setClose)
            stage.setOnCloseRequest(e -> System.exit(0));

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

    public class Task extends Thread {

        public void run() {

            CaptureImage capImage = new CaptureImage();

            String completeFilePath = Defaults.getPath(completeSavePath, "Complete");
            String croppedFilePath = Defaults.getPath(croppedSavePath, "Cropped");

            Image imageFromCam;
            try {

                imageFromCam = capImage.setPathImage(completeFilePath).setUrlStream(settings.ip).getFromFFMPEG()
                        .getImage(flipHorizontal.isSelected());

                progressBarComplete.setVisible(false);

                if (imageFromCam.getHeight() <= 0.0) {
                    progressBarComplete.setVisible(false);
                    progressBarCropped.setVisible(false);
                    Notification.error("There was an error, tryning to capture image");
                    return;
                }

                complete.setImage(imageFromCam);

                if (settings.cropImage) {
                    CropAndPrespectiveCorrection cropAndPrespective = new CropAndPrespectiveCorrection();

                    Image imageCropped = cropAndPrespective.setOriginal(imageFromCam)
                            .crop(Integer.parseInt(settings.cropUpperLeftX), Integer.parseInt(settings.cropUpperLeftY),
                                    Integer.parseInt(settings.cropWidth), Integer.parseInt(settings.cropHeight))
                            .getCropped();

                    cropped.setImage(imageCropped);

                    if (settings.prespectiveCorrection) {
                        Image imagePrespectiveCorrection = cropAndPrespective.prespectiveCorrection(croppedFilePath,
                                settings.getFromPoints(), settings.getToPoints()).getImagePrespectiveCorrection();

                        cropped.setImage(imagePrespectiveCorrection);
                    } else {
                        File outputFile = new File(croppedFilePath);

                        try {
                            ImageIO.write(cropAndPrespective.croppedBuffered, "jpg", outputFile);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Notification.error(ex.getMessage());
                        }
                    }
                } else {
                    Notification.info("The setting to crop image is disabled");
                }
                progressBarCropped.setVisible(false);
                Notification.infoCreateFile("The capture is done check your folder", completeSavePath + "\\");
            } catch (Exception e) {
                e.printStackTrace();
                progressBarCropped.setVisible(false);
                progressBarComplete.setVisible(false);
            }

        }
    }

}
