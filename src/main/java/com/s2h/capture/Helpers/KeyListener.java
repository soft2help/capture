package com.s2h.capture.Helpers;

import javafx.scene.image.Image;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.io.File;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.nio.file.Paths;
import java.nio.file.Path;

public class KeyListener implements NativeKeyListener {
    enum CaptureType {
        ALL, COMPLETE, CROPPED, NOTHING
    };

    public static Logger log = Log.getLog(KeyListener.class.getName());
    public Settings settings = new Settings();
    private Task task;
    private volatile static boolean capturing = false;

    public KeyListener() {
        settings.read();
        try {

            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);

            Handler[] handlers = Logger.getLogger("").getHandlers();
            for (int i = 0; i < handlers.length; i++) {
                handlers[i].setLevel(Level.OFF);
            }

            GlobalScreen.registerNativeHook();

        } catch (NativeHookException ex) {
            System.err.println(ex.getMessage());
        }

    }

    public void nativeKeyPressed(NativeKeyEvent e) {
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        if (!capturing) {
            CaptureType ct = CaptureType.NOTHING;
            if (settings.captureAllKeyCode.equals("" + e.getRawCode()) && settings.captureAll)
                ct = CaptureType.ALL;

            if (settings.captureCompleteKeyCode.equals("" + e.getRawCode()) && settings.captureComplete)
                ct = CaptureType.COMPLETE;

            if (settings.captureCroppedKeyCode.equals("" + e.getRawCode()) && settings.captureCropped)
                ct = CaptureType.CROPPED;

            if (ct != CaptureType.NOTHING) {
                log.info("Capturing from keyborad Shortcut " + ct);
                capturing = true;
                task = new Task(ct);
                task.start();
            }
        }

    }

    public void nativeKeyTyped(NativeKeyEvent e) {
    }

    public void close() {
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
    }

    public class Task extends Thread {
        private CaptureType ct;

        Task(CaptureType ct) {
            this.ct = ct;
        }

        public void run() {
            log.info("Trying to capture image from shortcut keyboard");

            Notification.info("We are trying to capture image");
            String completePath = "", croppedPath = "";

            if (ct == CaptureType.ALL) {
                completePath = Defaults.getPath(settings.captureAllSavePath, settings.captureAllNameComplete);
                croppedPath = Defaults.getPath(settings.captureAllSavePath, settings.captureAllNameCropped);
            }

            if (ct == CaptureType.COMPLETE)
                completePath = Defaults.getPath(settings.captureCompleteSavePath, settings.captureCompleteName);

            if (ct == CaptureType.CROPPED) {
                croppedPath = Defaults.getPath(settings.captureCroppedSavePath, settings.captureCroppedName);
                completePath = Defaults.getTemp();
            }

            try {
                CaptureImage capImage = new CaptureImage();
                Image imageFromCam = capImage.setPathImage(completePath).setUrlStream(settings.ip).getFromFFMPEG()
                        .getImage(settings.flipHorizontal);

                if ((ct == CaptureType.CROPPED || ct == CaptureType.ALL) && settings.cropImage) {
                    CropAndPrespectiveCorrection cropAndPrespective = new CropAndPrespectiveCorrection();

                    cropAndPrespective.setOriginal(imageFromCam)
                            .crop(Integer.parseInt(settings.cropUpperLeftX), Integer.parseInt(settings.cropUpperLeftY),
                                    Integer.parseInt(settings.cropWidth), Integer.parseInt(settings.cropHeight))
                            .getCropped();

                    if (settings.prespectiveCorrection) {
                        cropAndPrespective
                                .prespectiveCorrection(croppedPath, settings.getFromPoints(), settings.getToPoints())
                                .getImagePrespectiveCorrection();
                    }
                }

                Path path = Paths.get(completePath);

                String filePath = path.getParent().toString();
                log.info("The image(s) was saved in path: " + filePath);
                Notification.infoCreateFile("The image was saved in path: \n" + filePath, filePath);

                if (ct == CaptureType.CROPPED)
                    (new File(completePath)).delete();

            } catch (Exception e) {
                log.warning("Was an error in shortcut capture: " + e.getMessage());
                e.printStackTrace();
            }

            capturing = false;
        }
    }

}
