package com.s2h.capture.Helpers;

import java.io.File;
import java.io.IOException;
import javafx.scene.image.Image;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;
import java.util.List;

public class CaptureImage {
    public Settings settings;
    public static Logger log = Log.getLog(CaptureImage.class.getName());
    private String pathImage;
    private String urlStream;
    private Process processCaptureImage;
    private Process processPrespectiveCorrection;
    private Timer timer;

    public CaptureImage() {
        settings = new Settings();
        log = Log.getLog(getClass().getName());

    }

    public CaptureImage getFromFFMPEG() throws Exception {

        if (pathImage == null && urlStream == null)
            throw new Exception("You must set urlStream and pathToSaveImage");

        log.info("Timeout to capture image " + settings.timeoutCaptureImage + " (seconds)");
        Long timeout = Long.valueOf(settings.timeoutCaptureImage) * 1000;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                if (processCaptureImage != null)
                    processCaptureImage.destroy();

                log.warning("Canceling capture image from ip device, its not available");
                timer.cancel();
            }
        }, timeout, timeout);

        log.warning("Trying to capture image from ip device");
        String command = "ffmpeg -y -i " + urlStream + " -vframes 1 " + pathImage;

        processCaptureImage = Runtime.getRuntime().exec(command);
        processCaptureImage.waitFor();
        timer.cancel();
        processCaptureImage = null;

        return this;
    }

    public CaptureImage setPathImage(String pathImage) {
        this.pathImage = pathImage;
        return this;
    }

    public CaptureImage setUrlStream(String urlStream) {
        this.urlStream = urlStream;
        return this;
    }

    public Image getImage(Boolean flipHorizontal) {

        File file = new File(this.pathImage);
        Image image = new Image(file.toURI().toString());
        if (flipHorizontal)
            image = flipHorizontal(image, file);

        return image;
    }

    private Image flipHorizontal(Image image, File file) {
        log.info("Flip Horizontal image");
        BufferedImage imageb = SwingFXUtils.fromFXImage(image, null);

        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-imageb.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        imageb = op.filter(imageb, null);

        try {
            ImageIO.write(imageb, "jpg", new File(this.pathImage));
        } catch (IOException ex) {
            log.warning("Error trying to flip horizontal image: " + ex.getMessage());
            ex.printStackTrace();
        }

        return new Image(file.toURI().toString());
    }

    public CaptureImage prespectiveCorrection(List<Point> from, List<Point> to) throws Exception {
        if (pathImage == null)
            throw new Exception("You must set pathToSaveImage");

        Long timeout = Long.valueOf(settings.timeoutPrespectiveCorrection) * 1000;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                if (processPrespectiveCorrection != null)
                    processPrespectiveCorrection.destroy();

                log.warning("Canceling prespective correction, image its not available or process take too long");
                timer.cancel();
            }
        }, timeout, (1000 * 10));

        // String resize=" +repage -resize "+dim+"! ";
        String command = "powershell -command \"convert " + this.pathImage
                + " -matte -virtual-pixel transparent -distort Perspective" + " '" + from.get(0).x + "," + from.get(0).y
                + " " + to.get(0).x + "," + to.get(0).y + "  " + " " + from.get(1).x + "," + from.get(1).y + " "
                + to.get(1).x + "," + to.get(1).y + "  " + " " + from.get(2).x + "," + from.get(2).y + " " + to.get(2).x
                + "," + to.get(2).y + "  " + " " + from.get(3).x + "," + from.get(3).y + " " + to.get(3).x + ","
                + to.get(3).y + "' " + this.pathImage + "\"";
        try {
            processPrespectiveCorrection = Runtime.getRuntime().exec(command);
            processPrespectiveCorrection.waitFor();
            timer.cancel();
            processPrespectiveCorrection = null;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

}
