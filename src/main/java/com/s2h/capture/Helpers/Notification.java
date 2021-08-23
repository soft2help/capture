package com.s2h.capture.Helpers;

import org.controlsfx.control.Notifications;

import javafx.application.Platform;
import javafx.util.Duration;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

public class Notification {
    public static Settings settings = new Settings();
    public static Logger log = Log.getLog(Notification.class.getName());

    enum icons {
        INFO("img/info.png"), ERROR("img/error.png"), SUCCESS("img/success.png");

        private String src;

        // getter method
        public String getSrc() {
            return this.src;
        }

        // enum constructor - cannot be public or protected
        private icons(String src) {
            this.src = src;
        }

    }

    public static Notifications notification(String txt) {
        Notifications builder = Notifications.create().text(txt)
                .hideAfter(Duration.seconds(settings.notificationHideAfter))
                .position(Pos.valueOf(settings.notificationPosition));

        return builder;
    }

    public static void show(String txt, String icon) {
        Platform.runLater(() -> {
            Notifications builder = notification(txt).graphic(new ImageView(icon));
            try {
                builder.show();
            } catch (java.lang.NullPointerException e) {
                log.warning("Trying to put notification with magic scene");
                Stage magic = Defaults.getMagicScene();
                builder.owner(magic).show();

                Thread thread = new Thread(() -> {
                    try {
                        Thread.sleep((int) ((settings.notificationHideAfter + 1.0) * 100.0));
                        Platform.runLater(() -> magic.close());
                    } catch (InterruptedException exc) {
                        // should not be able to get here...
                        throw new Error("Unexpected interruption");
                    }
                });
                thread.start();
            }
        });

    }

    public static void error(String txt) {
        show(txt, icons.ERROR.getSrc());
    }

    public static void success(String txt) {
        show(txt, icons.SUCCESS.getSrc());
    }

    public static void info(String txt) {
        show(txt, icons.INFO.getSrc());
    }

    public static void infoCreateFile(String txt, String path) {
        Platform.runLater(() -> {
            Notifications builder = notification(txt).onAction(event -> {
                try {
                    Runtime.getRuntime().exec("explorer.exe /select," + path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).graphic(new ImageView(icons.INFO.getSrc()));
            try {
                builder.show();
            } catch (java.lang.NullPointerException e) {
                Stage magic = Defaults.getMagicScene();
                builder.owner(magic).show();

                Thread thread = new Thread(() -> {
                    try {
                        Thread.sleep(5000);
                        Platform.runLater(() -> magic.close());
                    } catch (InterruptedException exc) {
                        // should not be able to get here...
                        throw new Error("Unexpected interruption");
                    }
                });
                thread.start();

            }

        });
    }

}
