package com.s2h.capture.Helpers;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.logging.Logger;
import java.io.File;
import javax.imageio.ImageIO;

public class CropAndPrespectiveCorrection {
    public static Logger log = Log.getLog(CropAndPrespectiveCorrection.class.getName());
    Image original;
    Image cropped;
    public BufferedImage croppedBuffered;
    Image imagePrespectiveCorrection;

    public Image getCropped() {
        return this.cropped;
    }

    public Image getImagePrespectiveCorrection() {
        return this.imagePrespectiveCorrection;
    }

    public CropAndPrespectiveCorrection setOriginal(Image original) {
        this.original = original;
        return this;
    }

    public CropAndPrespectiveCorrection crop(int upperLeftX, int upperLeftY, int width, int height) {
        log.info("Cropping image - StartPoint: (" + upperLeftX + "," + upperLeftY + ") with Size: " + width + "x"
                + height);
        BufferedImage originalBuffered = SwingFXUtils.fromFXImage(this.original, null);
        this.croppedBuffered = originalBuffered.getSubimage(upperLeftX, upperLeftY, width, height);
        this.cropped = SwingFXUtils.toFXImage(croppedBuffered, null);

        return this;
    }

    public CropAndPrespectiveCorrection prespectiveCorrection(String savePathCroppedFile, List<Point> fromPoints,
            List<Point> toPoints) {
        log.info("Trying to prespective correction and save it in " + savePathCroppedFile);
        File outputFile = new File(savePathCroppedFile);

        try {
            ImageIO.write(this.croppedBuffered, "jpg", outputFile);

            CaptureImage capImage = new CaptureImage();

            this.imagePrespectiveCorrection = capImage.setPathImage(savePathCroppedFile)
                    .prespectiveCorrection(fromPoints, toPoints).getImage(false);

        } catch (Exception ex) {
            log.warning("Error in prespective correction image: " + ex.getMessage());
            ex.printStackTrace();
            Notification.error(ex.getMessage());
        }

        return this;
    }

}
