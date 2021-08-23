package com.s2h.capture.gui.includes;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Header {
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private AnchorPane header;

    @FXML
    private JFXButton close;

    @FXML
    private JFXButton minimize;

    Stage getStage() {
        return (Stage) header.getScene().getWindow();
    }

    @FXML
    void close(ActionEvent event) {
        getStage().close();
    }

    @FXML
    void minimize(ActionEvent event) {
        getStage().setIconified(true);
    }

    @FXML
    void moveHeader(MouseEvent event) {
        Stage stage = getStage();
        stage.setX(event.getScreenX() + xOffset);
        stage.setY(event.getScreenY() + yOffset);
    }

    @FXML
    void startDragHeader(MouseEvent event) {
        Stage stage = getStage();
        xOffset = stage.getX() - event.getScreenX();
        yOffset = stage.getY() - event.getScreenY();
    }
}
