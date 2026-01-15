package com.cgvsu;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.application.Platform;

public class GuiController {

    @FXML
    private Canvas canvas;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label statusLabel;

    @FXML
    private void onOpenModelMenuItemClick(ActionEvent event) {
        statusLabel.setText("Opening 3D model...");
        System.out.println("Open Model menu item clicked");
    }

    @FXML
    private void onSaveModelMenuItemClick(ActionEvent event) {
        statusLabel.setText("Saving 3D model...");
        System.out.println("Save Model menu item clicked");
    }

    @FXML
    private void handleExit(ActionEvent event) {
        System.out.println("Exit menu item clicked");
        Platform.exit();
    }

    @FXML
    private void handleCameraForward(ActionEvent event) {
        statusLabel.setText("Camera: Moving forward");
        System.out.println("Camera Forward clicked");
    }

    @FXML
    private void handleCameraBackward(ActionEvent event) {
        statusLabel.setText("Camera: Moving backward");
        System.out.println("Camera Backward clicked");
    }

    @FXML
    private void handleCameraLeft(ActionEvent event) {
        statusLabel.setText("Camera: Moving left");
        System.out.println("Camera Left clicked");
    }

    @FXML
    private void handleCameraRight(ActionEvent event) {
        statusLabel.setText("Camera: Moving right");
        System.out.println("Camera Right clicked");
    }

    @FXML
    private void handleCameraUp(ActionEvent event) {
        statusLabel.setText("Camera: Moving up");
        System.out.println("Camera Up clicked");
    }

    @FXML
    private void handleCameraDown(ActionEvent event) {
        statusLabel.setText("Camera: Moving down");
        System.out.println("Camera Down clicked");
    }


    @FXML
    private void initialize() {
        System.out.println("GuiController initialized");

        if (canvas != null) {
            System.out.println("Canvas loaded: " + canvas.getWidth() + "x" + canvas.getHeight());
        }

        if (statusLabel != null) {
            statusLabel.setText("Ready to load 3D models");
        }
    }


    public Canvas getCanvas() {
        return canvas;
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    public void setStatusText(String text) {
        if (statusLabel != null) {
            statusLabel.setText(text);
        }
    }
}