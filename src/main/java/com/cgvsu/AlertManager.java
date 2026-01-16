package com.cgvsu;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.StageStyle;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

public class AlertManager {

    public static void showError(String title, String message) {
        showAlert(AlertType.ERROR, "Error", title, message, false);
    }

    public static void showWarning(String title, String message) {
        showAlert(AlertType.WARNING, "Warning", title, message, false);
    }

    public static void showInfo(String title, String message) {
        showAlert(AlertType.INFORMATION, "Information", title, message, false);
    }

    public static boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.initStyle(StageStyle.UTILITY);
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(yesButton, noButton);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == yesButton;
    }

    public static void showObjReaderError(String fileName, int lineNumber, String errorMessage) {
        String header = "OBJ Format Error";
        String content;
        if (lineNumber > 0) {
            content = String.format(
                    "Error in file: %s\n\nLine: %d\nProblem: %s\n\nPlease check vertex and face format.",
                    fileName, lineNumber, errorMessage
            );
        } else {
            content = String.format(
                    "Error in file: %s\n\nProblem: %s\n\nThe file format is not valid.",
                    fileName, errorMessage
            );
        }
        showAlert(AlertType.ERROR, "3D Model Load Error", header, content, true);
    }

    public static void showDetailedError(String title, String header, String content, Exception exception) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initStyle(StageStyle.UTILITY);
        if (exception != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            exception.printStackTrace(pw);
            String exceptionText = sw.toString();
            Label label = new Label("Technical details:");
            label.setStyle("-fx-font-weight: bold; -fx-padding: 5 0 5 0;");
            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setStyle("-fx-font-family: 'Monospace'; -fx-font-size: 11px;");
            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);
            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);
            expContent.setPrefWidth(600);
            expContent.setPrefHeight(300);
            alert.getDialogPane().setExpandableContent(expContent);
            alert.getDialogPane().setExpanded(false);
        }
        alert.showAndWait();
    }

    public static boolean showOkCancel(String title, String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Action");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.initStyle(StageStyle.UTILITY);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private static void showAlert(AlertType type, String title, String header,
                                  String content, boolean withDetails) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initStyle(StageStyle.UTILITY);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStyleClass().add("custom-dialog");
        if (withDetails) {
            switch (type) {
                case ERROR:
                    dialogPane.getStyleClass().add("error-dialog");
                    break;
                case WARNING:
                    dialogPane.getStyleClass().add("warning-dialog");
                    break;
                case INFORMATION:
                    dialogPane.getStyleClass().add("info-dialog");
                    break;
            }
        }
        alert.showAndWait();
    }
}