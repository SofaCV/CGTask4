package com.cgvsu;

import com.cgvsu.Math.Vector.Vector3;
import com.cgvsu.render_engine.RenderEngine;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.File;


import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.render_engine.Camera;

public class GuiController {

    final private float TRANSLATION = 0.5F;

    @FXML
    AnchorPane anchorPane;

    @FXML
    private Canvas canvas;

    private Model mesh = null;

    private Camera camera = new Camera(
            new Vector3(0, 0, 100),
            new Vector3(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private Timeline timeline;

    //параметры для аффинных преобразований
    private Vector3 scale = new Vector3(1.0f, 1.0f, 1.0f);;
    private Vector3 rotation = new Vector3(0,0,0);
    private Vector3 translation = new Vector3(0,0,0);


    private double mousePrevX, mousePrevY;    // Предыдущие координаты мыши
    private boolean isMousePressed = false;   // Нажата ли кнопка мыши

    //Работа с мышью. Если действия производятся правой кнопкой, то совершается поворот, если левой - перемещение,
    //движение колесика отвечает за масштабируемость
    private void setupMouseHandlers() {
        //первое нажатие
        canvas.setOnMousePressed(event -> {
            mousePrevX = event.getX();
            mousePrevY = event.getY();
            isMousePressed = true;
        });

        //второе нажатие
        canvas.setOnMouseDragged(event -> {
            if (!isMousePressed || mesh == null){return;}
            // На сколько сдвинули мышь
            double deltaX = event.getX() - mousePrevX;
            double deltaY = event.getY() - mousePrevY;

            //если нажатие было правой кнопкой
            if (event.isPrimaryButtonDown()) {
                float sensitivity = 0.01f;  // Чувствительность
                rotation = new Vector3(
                        rotation.getX() + (float) (deltaY * sensitivity),
                        rotation.getY() + (float) (deltaX * sensitivity),
                        rotation.getZ()
                );
            } else if (event.isSecondaryButtonDown()) { //если нажатие было левой кнопкой
                float sensitivity = 0.1f;
                translation = new Vector3(
                        translation.getX() + (float) (deltaX * sensitivity),
                        translation.getY() - (float) (deltaY * sensitivity),
                        translation.getZ()
                );
            }
            // Обновляем позицию для следующего кадра
            mousePrevX = event.getX();
            mousePrevY = event.getY();
        });

        //если крутится колесико
        canvas.setOnScroll(event -> {
            if (mesh == null) {return;}
            double delta = event.getDeltaY();
            float zoomSensitivity = 0.1f;

            // Масштабируем равномерно по всем осям
            float zoomFactor = 1 + (float)(delta * zoomSensitivity * 0.01);
            scale = new Vector3(
                    scale.getX() * zoomFactor,
                    scale.getY() * zoomFactor,
                    scale.getZ() * zoomFactor
            );
        });
    }

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));

            if (mesh != null) {
                RenderEngine.render(
                        canvas.getGraphicsContext2D(),
                        camera,
                        mesh,
                        (int) width,
                        (int) height,
                        scale,
                        rotation,
                        translation
                );
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    @FXML
    private void onOpenModelMenuItemClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");

        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());

        try {
            String fileContent = Files.readString(fileName);
            mesh = ObjReader.read(fileContent);
            // todo: обработка ошибок
        } catch (IOException exception) {

        }
    }

    @FXML
    public void handleCameraForward(ActionEvent actionEvent) {
        camera.movePosition(new Vector3(0, 0, -TRANSLATION));
    }

    @FXML
    public void handleCameraBackward(ActionEvent actionEvent) {
        camera.movePosition(new Vector3(0, 0, TRANSLATION));
    }

    @FXML
    public void handleCameraLeft(ActionEvent actionEvent) {
        camera.movePosition(new Vector3(TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraRight(ActionEvent actionEvent) {
        camera.movePosition(new Vector3(-TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraUp(ActionEvent actionEvent) {
        camera.movePosition(new Vector3(0, TRANSLATION, 0));
    }

    @FXML
    public void handleCameraDown(ActionEvent actionEvent) {
        camera.movePosition(new Vector3(0, -TRANSLATION, 0));
    }
}