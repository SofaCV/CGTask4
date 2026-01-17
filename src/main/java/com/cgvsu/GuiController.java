package com.cgvsu;

import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.objreader.ObjReaderException;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class GuiController {

    @FXML
    private Canvas canvas;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label statusLabel;

    // Новые поля для работы с моделью
    private GraphicsContext gc;
    private Model currentModel;
    private AnimationTimer renderTimer;
    private float scale = 100.0f;
    private float offsetX = 0;
    private float offsetY = 0;
    private double mousePrevX, mousePrevY;

    @FXML
    private void onOpenModelMenuItemClick(ActionEvent event) {
        statusLabel.setText("Opening 3D model...");
        System.out.println("=== OPEN MODEL ===");

        // Диалог выбора файла
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open 3D Model File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("OBJ Files", "*.obj")
        );

        // Указываем начальную папку
        File modelsDir = new File("models");
        if (!modelsDir.exists()) {
            modelsDir.mkdir();
        }
        fileChooser.setInitialDirectory(modelsDir);

        Stage stage = (Stage) canvas.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            loadModelFromFile(file);
        } else {
            statusLabel.setText("File selection cancelled");
        }
    }

    private void loadModelFromFile(File file) {
        try {
            System.out.println("Loading file: " + file.getAbsolutePath());
            System.out.println("File size: " + file.length() + " bytes");

            // Проверяем файл
            if (!file.exists()) {
                statusLabel.setText("File not found");
                System.err.println("File does not exist");
                return;
            }

            if (file.length() == 0) {
                statusLabel.setText("File is empty");
                System.err.println("File is empty");
                return;
            }

            // Читаем содержимое
            String content = Files.readString(file.toPath());

            // Простая проверка на пустой файл
            if (content.trim().isEmpty()) {
                statusLabel.setText("File is empty");
                System.err.println("File contains only whitespace");
                return;
            }

            // Выводим первые 10 строк для отладки
            System.out.println("=== FILE CONTENT (first 10 lines) ===");
            String[] lines = content.split("\n");
            for (int i = 0; i < Math.min(10, lines.length); i++) {
                System.out.println((i+1) + ": " + lines[i].trim());
            }
            System.out.println("=== END FILE CONTENT ===");

            // Пробуем загрузить модель
            System.out.println("Calling ObjReader.read()...");
            currentModel = ObjReader.read(content);

            // Успешно загружено
            statusLabel.setText("Loaded: " + file.getName());
            System.out.println("=== MODEL LOADED SUCCESSFULLY ===");
            System.out.println("Model name: " + file.getName());
            System.out.println("Vertices: " + currentModel.vertices.size());
            System.out.println("Polygons: " + currentModel.polygons.size());

            // Выводим информацию о первых вершинах
            if (!currentModel.vertices.isEmpty()) {
                System.out.println("First 3 vertices:");
                for (int i = 0; i < Math.min(3, currentModel.vertices.size()); i++) {
                    com.cgvsu.math.Vector3f v = currentModel.vertices.get(i);
                    System.out.println("  v" + (i+1) + ": (" + v.x + ", " + v.y + ", " + v.z + ")");
                }
            }

            if (!currentModel.polygons.isEmpty()) {
                System.out.println("First polygon vertex indices: " +
                        currentModel.polygons.get(0).getVertexIndices());
            }

            // Автоматически центрируем модель
            centerModel();

        } catch (IOException e) {
            statusLabel.setText("Error reading file");
            System.err.println("IO Error: " + e.getMessage());
            e.printStackTrace();
        } catch (ObjReaderException e) {
            statusLabel.setText("Invalid OBJ format");
            System.err.println("OBJ Error: " + e.getMessage());

            // Показываем сообщение об ошибке
            showErrorMessage("OBJ Parsing Error",
                    "Cannot parse the OBJ file: " + file.getName(),
                    e.getMessage());
        } catch (Exception e) {
            statusLabel.setText("Unexpected error");
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void centerModel() {
        if (currentModel == null || currentModel.vertices.isEmpty()) {
            System.out.println("Cannot center: model is null or has no vertices");
            return;
        }

        System.out.println("=== CENTERING MODEL ===");

        // Находим границы модели
        float minX = Float.MAX_VALUE, maxX = Float.MIN_VALUE;
        float minY = Float.MAX_VALUE, maxY = Float.MIN_VALUE;

        for (com.cgvsu.math.Vector3f vertex : currentModel.vertices) {
            minX = Math.min(minX, vertex.x);
            maxX = Math.max(maxX, vertex.x);
            minY = Math.min(minY, vertex.y);
            maxY = Math.max(maxY, vertex.y);
        }

        System.out.println("Model bounds:");
        System.out.println("  X: " + minX + " to " + maxX + " (width: " + (maxX - minX) + ")");
        System.out.println("  Y: " + minY + " to " + maxY + " (height: " + (maxY - minY) + ")");

        // Вычисляем центр
        float centerX = (minX + maxX) / 2;
        float centerY = (minY + maxY) / 2;

        System.out.println("Model center: (" + centerX + ", " + centerY + ")");

        // Центрируем (смещаем модель так, чтобы центр был в 0,0)
        offsetX = -centerX;
        offsetY = -centerY;

        System.out.println("Offsets: X=" + offsetX + ", Y=" + offsetY);

        // Подбираем масштаб - УВЕЛИЧИВАЕМ МАСШТАБ В 2 РАЗА
        float width = maxX - minX;
        float height = maxY - minY;
        float maxSize = Math.max(width, height);

        System.out.println("Max model size: " + maxSize);
        System.out.println("Canvas size: " + canvas.getWidth() + "x" + canvas.getHeight());

        if (maxSize > 0) {
            // Увеличиваем масштаб! Раньше было 0.8, теперь 0.4 = в 2 раза крупнее
            scale = (float) Math.min(canvas.getWidth(), canvas.getHeight()) * 0.4f / maxSize;
            System.out.println("Auto scale calculated: " + scale);
        } else {
            // Если модель очень маленькая, используем большой масштаб
            scale = 200.0f;
            System.out.println("Using fixed scale: " + scale);
        }

        // Если масштаб слишком маленький, увеличиваем его
        if (scale < 10.0f) {
            scale = 100.0f;
            System.out.println("Scale too small, using: " + scale);
        }

        System.out.println("Final scale: " + scale);
        System.out.println("=== CENTERING COMPLETE ===");
    }

    private void showErrorMessage(String title, String header, String content) {
        try {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.showAndWait();
        } catch (Exception e) {
            System.err.println("Failed to show error dialog: " + e.getMessage());
        }
    }

    @FXML
    private void onSaveModelMenuItemClick(ActionEvent event) {
        statusLabel.setText("Saving 3D model...");
        System.out.println("Save Model menu item clicked");
    }

    @FXML
    private void handleExit(ActionEvent event) {
        System.out.println("Exit menu item clicked");
        if (renderTimer != null) {
            renderTimer.stop();
        }
        Platform.exit();
    }

    @FXML
    private void handleCameraForward(ActionEvent event) {
        if (currentModel != null) {
            offsetY -= 10.0f / scale;
            statusLabel.setText("Moving forward");
            System.out.println("Move forward - OffsetY: " + offsetY);
        }
    }

    @FXML
    private void handleCameraBackward(ActionEvent event) {
        if (currentModel != null) {
            offsetY += 10.0f / scale;
            statusLabel.setText("Moving backward");
            System.out.println("Move backward - OffsetY: " + offsetY);
        }
    }

    @FXML
    private void handleCameraLeft(ActionEvent event) {
        if (currentModel != null) {
            offsetX += 10.0f / scale;
            statusLabel.setText("Moving left");
            System.out.println("Move left - OffsetX: " + offsetX);
        }
    }

    @FXML
    private void handleCameraRight(ActionEvent event) {
        if (currentModel != null) {
            offsetX -= 10.0f / scale;
            statusLabel.setText("Moving right");
            System.out.println("Move right - OffsetX: " + offsetX);
        }
    }

    @FXML
    private void handleCameraUp(ActionEvent event) {
        if (currentModel != null) {
            scale *= 1.1f;
            statusLabel.setText("Zooming in");
            System.out.println("Zoom in - Scale: " + scale);
        }
    }

    @FXML
    private void handleCameraDown(ActionEvent event) {
        if (currentModel != null) {
            scale *= 0.9f;
            statusLabel.setText("Zooming out");
            System.out.println("Zoom out - Scale: " + scale);
        }
    }

    // Добавляем метод для сброса вида
    @FXML
    private void handleResetView(ActionEvent event) {
        if (currentModel != null) {
            centerModel();
            statusLabel.setText("View reset");
            System.out.println("View reset - Scale: " + scale + ", OffsetX: " + offsetX + ", OffsetY: " + offsetY);
        }
    }

    @FXML
    private void initialize() {
        System.out.println("=== GUI CONTROLLER INITIALIZED ===");
        System.out.println("Canvas loaded: " + canvas.getWidth() + "x" + canvas.getHeight());

        if (canvas != null) {
            // Получаем GraphicsContext
            gc = canvas.getGraphicsContext2D();

            // Настраиваем обработку мыши
            setupMouseHandlers();

            // Запускаем цикл рендеринга
            startRenderLoop();
        }

        if (statusLabel != null) {
            statusLabel.setText("Ready to load 3D models");
        }

        System.out.println("=== INITIALIZATION COMPLETE ===");
    }

    private void setupMouseHandlers() {
        canvas.setOnMousePressed(event -> {
            mousePrevX = event.getX();
            mousePrevY = event.getY();
            System.out.println("Mouse pressed at: (" + mousePrevX + ", " + mousePrevY + ")");
        });

        canvas.setOnMouseDragged(event -> {
            if (currentModel != null) {
                double dx = event.getX() - mousePrevX;
                double dy = event.getY() - mousePrevY;

                offsetX += dx / scale;
                offsetY -= dy / scale;

                mousePrevX = event.getX();
                mousePrevY = event.getY();
                statusLabel.setText("Moving view");

                System.out.println("Mouse drag - OffsetX: " + offsetX + ", OffsetY: " + offsetY);
            }
        });

        canvas.setOnScroll(event -> {
            if (currentModel != null) {
                double delta = event.getDeltaY();
                if (delta > 0) {
                    scale *= 1.1f;
                    statusLabel.setText("Zooming in");
                    System.out.println("Scroll zoom in - Scale: " + scale);
                } else {
                    scale *= 0.9f;
                    statusLabel.setText("Zooming out");
                    System.out.println("Scroll zoom out - Scale: " + scale);
                }
            }
        });
    }

    private void startRenderLoop() {
        renderTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Очищаем холст
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

                // Рисуем темный фон
                gc.setFill(Color.rgb(25, 25, 25));
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                // Рисуем сетку
                drawGrid();

                // Рисуем оси координат
                drawAxes();

                // Рисуем модель, если она есть
                if (currentModel != null) {
                    drawModel();
                } else {
                    // Показываем сообщение, если модель не загружена
                    gc.setFill(Color.WHITE);
                    gc.setFont(javafx.scene.text.Font.font(14));
                    gc.fillText("No model loaded. Use File → Open Model",
                            canvas.getWidth()/2 - 120, canvas.getHeight()/2);
                }
            }
        };
        renderTimer.start();
        System.out.println("Render loop started");
    }

    private void drawGrid() {
        gc.setStroke(Color.rgb(40, 40, 40));
        gc.setLineWidth(0.5);

        double gridSize = 50;
        double centerX = canvas.getWidth() / 2;
        double centerY = canvas.getHeight() / 2;

        // Вертикальные линии
        for (double x = centerX % gridSize; x < canvas.getWidth(); x += gridSize) {
            gc.strokeLine(x, 0, x, canvas.getHeight());
        }

        // Горизонтальные линии
        for (double y = centerY % gridSize; y < canvas.getHeight(); y += gridSize) {
            gc.strokeLine(0, y, canvas.getWidth(), y);
        }
    }

    private void drawAxes() {
        double centerX = canvas.getWidth() / 2;
        double centerY = canvas.getHeight() / 2;

        // Ось X (красная)
        gc.setStroke(Color.RED);
        gc.setLineWidth(2);
        gc.strokeLine(centerX, centerY, centerX + 80, centerY);
        gc.setFill(Color.RED);
        gc.fillText("X", centerX + 85, centerY + 5);

        // Ось Y (зеленая)
        gc.setStroke(Color.GREEN);
        gc.strokeLine(centerX, centerY, centerX, centerY - 80);
        gc.setFill(Color.GREEN);
        gc.fillText("Y", centerX + 5, centerY - 85);

        // Ось Z (синяя)
        gc.setStroke(Color.BLUE);
        gc.strokeLine(centerX, centerY, centerX - 56, centerY + 56);
        gc.setFill(Color.BLUE);
        gc.fillText("Z", centerX - 61, centerY + 61);
    }

    private void drawModel() {
        if (currentModel == null) {
            return;
        }

        System.out.println("=== DRAWING MODEL ===");
        System.out.println("Scale: " + scale + ", OffsetX: " + offsetX + ", OffsetY: " + offsetY);

        // Проверяем данные модели
        if (currentModel.vertices.isEmpty()) {
            System.out.println("No vertices in model!");
            return;
        }

        if (currentModel.polygons.isEmpty()) {
            System.out.println("No polygons in model!");
            return;
        }

        // Рисуем полигоны как каркас - ТОЛЩЕ И ЯРЧЕ
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(2.0); // Увеличили толщину

        int polygonsDrawn = 0;
        int linesDrawn = 0;

        for (com.cgvsu.model.Polygon polygon : currentModel.polygons) {
            int linesInPolygon = drawPolygon(polygon);
            linesDrawn += linesInPolygon;
            if (linesInPolygon > 0) {
                polygonsDrawn++;
            }
        }

        System.out.println("Polygons drawn: " + polygonsDrawn + "/" + currentModel.polygons.size());
        System.out.println("Lines drawn: " + linesDrawn);

        // Рисуем вершины как точки - КРУПНЕЕ
        gc.setFill(Color.YELLOW);
        int verticesDrawn = 0;

        for (int i = 0; i < currentModel.vertices.size(); i++) {
            com.cgvsu.math.Vector3f vertex = currentModel.vertices.get(i);

            // Преобразуем координаты
            double screenX = canvas.getWidth() / 2 + (vertex.x + offsetX) * scale;
            double screenY = canvas.getHeight() / 2 - (vertex.y + offsetY) * scale;

            // Рисуем точку (увеличили размер)
            gc.fillOval(screenX - 3, screenY - 3, 6, 6);
            verticesDrawn++;

            // Выводим координаты первых 3 вершин для отладки
            if (i < 3) {
                System.out.println("Vertex " + i + " world: (" + vertex.x + ", " + vertex.y +
                        ") screen: (" + screenX + ", " + screenY + ")");
            }
        }

        System.out.println("Vertices drawn: " + verticesDrawn + "/" + currentModel.vertices.size());

        // Если ничего не нарисовалось, рисуем тестовую фигуру
        if (polygonsDrawn == 0 && linesDrawn == 0) {
            System.out.println("WARNING: Model not visible! Drawing test shape...");
            drawTestShape();
        }

        // Рисуем информацию о модели
        drawModelInfo();

        System.out.println("=== DRAWING COMPLETE ===");
    }

    private int drawPolygon(com.cgvsu.model.Polygon polygon) {
        java.util.List<Integer> vertexIndices = polygon.getVertexIndices();
        if (vertexIndices == null || vertexIndices.isEmpty()) {
            return 0;
        }

        int linesDrawn = 0;
        int vertexCount = vertexIndices.size();

        for (int i = 0; i < vertexCount; i++) {
            int idx1 = vertexIndices.get(i);
            int idx2 = vertexIndices.get((i + 1) % vertexCount);

            // Проверяем индексы
            if (idx1 < 0 || idx1 >= currentModel.vertices.size() ||
                    idx2 < 0 || idx2 >= currentModel.vertices.size()) {
                continue;
            }

            com.cgvsu.math.Vector3f v1 = currentModel.vertices.get(idx1);
            com.cgvsu.math.Vector3f v2 = currentModel.vertices.get(idx2);

            // Преобразуем координаты
            double x1 = canvas.getWidth() / 2 + (v1.x + offsetX) * scale;
            double y1 = canvas.getHeight() / 2 - (v1.y + offsetY) * scale;
            double x2 = canvas.getWidth() / 2 + (v2.x + offsetX) * scale;
            double y2 = canvas.getHeight() / 2 - (v2.y + offsetY) * scale;

            // Рисуем линию
            gc.strokeLine(x1, y1, x2, y2);
            linesDrawn++;
        }

        return linesDrawn;
    }

    private void drawTestShape() {
        // Рисуем красный треугольник в центре, чтобы убедиться, что рисование работает
        gc.setStroke(Color.RED);
        gc.setLineWidth(3.0);

        double centerX = canvas.getWidth() / 2;
        double centerY = canvas.getHeight() / 2;
        double size = 100;

        gc.strokeLine(centerX, centerY - size, centerX + size, centerY + size);
        gc.strokeLine(centerX + size, centerY + size, centerX - size, centerY + size);
        gc.strokeLine(centerX - size, centerY + size, centerX, centerY - size);

        // Подпись
        gc.setFill(Color.RED);
        gc.fillText("TEST SHAPE - Model not visible", centerX - 80, centerY - size - 10);
    }

    private void drawModelInfo() {
        if (currentModel == null) return;

        // Рисуем информацию о модели в углу экрана
        gc.setFill(Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font(12));

        String info = String.format("Vertices: %d  Polygons: %d  Scale: %.1f",
                currentModel.vertices.size(),
                currentModel.polygons.size(),
                scale);

        gc.fillText(info, 10, 20);

        // Если модель есть, но не видна - подсказка
        if (currentModel.vertices.size() > 0 && currentModel.polygons.size() > 0) {
            gc.fillText("Use mouse: drag to move, scroll to zoom", 10, 40);
            gc.fillText("Buttons: WASD to move, Q/E to zoom", 10, 60);
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