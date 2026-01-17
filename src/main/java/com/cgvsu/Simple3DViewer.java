package com.cgvsu;

import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.objreader.ObjReaderException;
import com.cgvsu.render_engine.Camera;
import com.cgvsu.scene.SceneManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javafx.scene.input.*;

public class Simple3DViewer extends Application {
    private SceneManager sceneManager;
    private Scene currentScene;
    private Camera camera;
    private GraphicsContext gc;
    private Timeline renderLoop;
    private UIManager uiManager;
    private ThemeManager themeManager;
    private AnimationManager animationManager;
    private BorderPane root;
    private Canvas canvas;
    private ListView<String> objectListView;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            Platform.runLater(() -> {
                AlertManager.showError("Unexpected Error",
                        "An unexpected error occurred:\n" + throwable.getMessage());
            });
        });
        try {
            themeManager = new ThemeManager();
            animationManager = new AnimationManager();
            uiManager = new UIManager(themeManager, animationManager);
            root = new BorderPane();
            buildInterface();
            Scene scene = new Scene(root, 1400, 900);
            themeManager.applyThemeToScene(scene);
            animationManager.setupAnimations(root);
            primaryStage.setTitle("3D Model Editor - Professional Interface");
            primaryStage.setScene(scene);
            primaryStage.show();
            demonstrateUIEffects();
        } catch (Exception e) {
            AlertManager.showError("Application failed to start",
                    "Fatal error during application startup:\n" +
                            e.getClass().getSimpleName() + ": " + e.getMessage());
            e.printStackTrace();
            Platform.exit();
        }
    }

    private void buildInterface() {
        root.setTop(createTopPanel());
        root.setLeft(createObjectListPanel());
        root.setCenter(createViewportPanel());
        root.setRight(createToolsPanel());
        root.setBottom(createStatusPanel());
    }

    private HBox createTopPanel() {
        HBox topPanel = new HBox(10);
        topPanel.setPadding(new Insets(10));
        topPanel.setAlignment(Pos.CENTER_LEFT);
        topPanel.getStyleClass().add("top-panel");
        Label title = new Label("3D MODEL EDITOR");
        title.getStyleClass().add("app-title");
        ToolBar quickAccess = new ToolBar();
        Button newBtn = uiManager.createIconButton("New", "Create new scene", "Ctrl+N");
        newBtn.setOnAction(e -> createNewScene());
        Button openBtn = uiManager.createIconButton("Open", "Open 3D model", "Ctrl+O");
        openBtn.setOnAction(e -> openModelFromExplorer());
        Button saveBtn = uiManager.createIconButton("Save", "Save model", "Ctrl+S");
        quickAccess.getItems().addAll(newBtn, openBtn, saveBtn, new Separator());
        Button darkThemeBtn = uiManager.createThemeButton("Dark", ThemeManager.Theme.DARK);
        Button lightThemeBtn = uiManager.createThemeButton("Light", ThemeManager.Theme.LIGHT);
        HBox themeBox = new HBox(5, darkThemeBtn, lightThemeBtn);
        topPanel.getChildren().addAll(title, new Separator(), quickAccess, new Separator(), themeBox);
        HBox.setHgrow(quickAccess, Priority.ALWAYS);
        return topPanel;
    }

    private VBox createObjectListPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(15));
        panel.getStyleClass().add("object-list-panel");
        panel.setPrefWidth(300);
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("SCENE OBJECTS");
        title.getStyleClass().add("panel-title");
        Label count = new Label("(0)");
        count.getStyleClass().add("object-count");
        header.getChildren().addAll(title, count);
        HBox.setHgrow(title, Priority.ALWAYS);
        objectListView = new ListView<>();
        objectListView.getStyleClass().add("object-list");
        objectListView.setPrefHeight(400);
        HBox objectButtons = new HBox(5);
        objectButtons.setAlignment(Pos.CENTER);
        Button addBtn = uiManager.createIconButton("Add", "Add primitive object");
        addBtn.setOnAction(e -> safeAddPrimitive("Sphere"));
        Button importBtn = uiManager.createIconButton("Import", "Import 3D model from file");
        importBtn.setOnAction(e -> openModelFromExplorer());
        Button exportBtn = uiManager.createIconButton("Export", "Export scene to file");
        objectButtons.getChildren().addAll(addBtn, importBtn, exportBtn);
        HBox selectButtons = new HBox(5);
        selectButtons.setAlignment(Pos.CENTER);
        selectButtons.setPadding(new Insets(10, 0, 0, 0));
        Button selectAllBtn = uiManager.createIconButton("Select All", "Select all objects");
        Button clearBtn = uiManager.createIconButton("Clear", "Clear selection");
        selectButtons.getChildren().addAll(selectAllBtn, clearBtn);
        HBox editButtons = new HBox(5);
        editButtons.setAlignment(Pos.CENTER);
        editButtons.setPadding(new Insets(10, 0, 0, 0));
        Button duplicateBtn = uiManager.createIconButton("Duplicate", "Duplicate object");
        Button deleteBtn = uiManager.createIconButton("Delete", "Delete object");
        deleteBtn.setOnAction(e -> safeDeleteObject());
        editButtons.getChildren().addAll(duplicateBtn, deleteBtn);
        VBox propertiesPanel = createObjectPropertiesPanel();
        panel.getChildren().addAll(header, objectListView, objectButtons, selectButtons, editButtons, propertiesPanel);
        return panel;
    }

    private VBox createObjectPropertiesPanel() {
        VBox properties = new VBox(10);
        properties.setPadding(new Insets(10, 0, 0, 0));
        properties.getStyleClass().add("properties-panel");
        Label title = new Label("OBJECT PROPERTIES");
        title.getStyleClass().add("properties-title");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        String[] propertiesList = {"Position", "Rotation", "Scale", "Visibility", "Material"};
        for (int i = 0; i < propertiesList.length; i++) {
            Label propLabel = new Label(propertiesList[i] + ":");
            TextField propValue = new TextField();
            propValue.setPromptText("Value...");
            grid.add(propLabel, 0, i);
            grid.add(propValue, 1, i);
        }
        properties.getChildren().addAll(title, new Separator(), grid);
        return properties;
    }

    private BorderPane createViewportPanel() {
        BorderPane viewport = new BorderPane();
        viewport.getStyleClass().add("viewport-panel");
        HBox viewportTools = new HBox(10);
        viewportTools.setPadding(new Insets(10));
        viewportTools.setAlignment(Pos.CENTER_LEFT);
        ToggleGroup viewModeGroup = new ToggleGroup();
        ToggleButton wireframeBtn = uiManager.createToggleButton("Wireframe", "Wireframe view");
        wireframeBtn.setToggleGroup(viewModeGroup);
        ToggleButton solidBtn = uiManager.createToggleButton("Solid", "Solid view");
        solidBtn.setToggleGroup(viewModeGroup);
        solidBtn.setSelected(true);
        ToggleButton texturedBtn = uiManager.createToggleButton("Textured", "Textured view");
        texturedBtn.setToggleGroup(viewModeGroup);
        CheckBox gridCheck = new CheckBox("Show Grid");
        gridCheck.setSelected(true);
        CheckBox axesCheck = new CheckBox("Show Axes");
        axesCheck.setSelected(true);
        viewportTools.getChildren().addAll(
                new Label("View Mode:"), wireframeBtn, solidBtn, texturedBtn,
                new Separator(), gridCheck, axesCheck
        );
        canvas = new Canvas(800, 600);
        canvas.getStyleClass().add("3d-canvas");
        setupDragAndDrop();
        StackPane canvasContainer = new StackPane(canvas);
        Label demoLabel = new Label("3D Viewport\n(Drag & drop models here)\n\nControls:\n• Mouse: Rotate view\n• Scroll: Zoom\n• WASD: Move camera");
        demoLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 14px; -fx-text-alignment: center;");
        canvasContainer.getChildren().add(demoLabel);
        viewport.setTop(viewportTools);
        viewport.setCenter(canvasContainer);
        return viewport;
    }

    private void setupDragAndDrop() {
        canvas.setOnDragOver(event -> {
            if (event.getGestureSource() != canvas &&
                    event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });
        canvas.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                File file = db.getFiles().get(0);
                if (file.exists() && file.isFile()) {
                    open3DModelFile(file);
                    success = true;
                } else {
                    AlertManager.showError("Invalid file",
                            "Dropped item is not a valid file.");
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private VBox createToolsPanel() {
        VBox toolsPanel = new VBox(15);
        toolsPanel.setPadding(new Insets(15));
        toolsPanel.getStyleClass().add("tools-panel");
        toolsPanel.setPrefWidth(350);
        VBox editModes = createEditModesPanel();
        VBox transformTools = createTransformToolsPanel();
        VBox selectionTools = createSelectionToolsPanel();
        VBox displaySettings = createDisplaySettingsPanel();
        toolsPanel.getChildren().addAll(editModes, transformTools, selectionTools, displaySettings);
        return toolsPanel;
    }

    private VBox createEditModesPanel() {
        VBox panel = uiManager.createSectionPanel("EDIT MODES");
        ToggleGroup editModeGroup = new ToggleGroup();
        ToggleButton objectMode = uiManager.createToggleButton("OBJECT", "Edit objects");
        objectMode.setToggleGroup(editModeGroup);
        objectMode.setSelected(true);
        ToggleButton vertexMode = uiManager.createToggleButton("VERTEX", "Edit vertices");
        vertexMode.setToggleGroup(editModeGroup);
        ToggleButton edgeMode = uiManager.createToggleButton("EDGE", "Edit edges");
        edgeMode.setToggleGroup(editModeGroup);
        ToggleButton faceMode = uiManager.createToggleButton("FACE", "Edit faces");
        faceMode.setToggleGroup(editModeGroup);
        HBox modeButtons = new HBox(5);
        modeButtons.setAlignment(Pos.CENTER);
        modeButtons.getChildren().addAll(objectMode, vertexMode, edgeMode, faceMode);
        panel.getChildren().add(modeButtons);
        return panel;
    }

    private VBox createTransformToolsPanel() {
        VBox panel = uiManager.createSectionPanel("TRANSFORM TOOLS");
        GridPane transformGrid = new GridPane();
        transformGrid.setHgap(10);
        transformGrid.setVgap(10);
        String[] axes = {"X", "Y", "Z"};
        String[] transforms = {"Move", "Rotate", "Scale"};
        for (int i = 0; i < transforms.length; i++) {
            Label transformLabel = new Label(transforms[i]);
            transformLabel.getStyleClass().add("transform-label");
            HBox axisFields = new HBox(5);
            for (int j = 0; j < 3; j++) {
                TextField field = new TextField();
                field.setPrefWidth(60);
                field.setPromptText(axes[j]);
                axisFields.getChildren().add(field);
            }
            transformGrid.add(transformLabel, 0, i);
            transformGrid.add(axisFields, 1, i);
        }
        HBox applyButtons = new HBox(5);
        applyButtons.setAlignment(Pos.CENTER);
        Button applyBtn = uiManager.createIconButton("Apply", "Apply transformations");
        Button resetBtn = uiManager.createIconButton("Reset", "Reset transformations");
        applyButtons.getChildren().addAll(applyBtn, resetBtn);
        panel.getChildren().addAll(transformGrid, applyButtons);
        return panel;
    }

    private VBox createSelectionToolsPanel() {
        VBox panel = uiManager.createSectionPanel("SELECTION");
        HBox selectionButtons = new HBox(5);
        selectionButtons.setAlignment(Pos.CENTER);
        Button selectAllBtn = uiManager.createIconButton("Select All", "Select all objects");
        Button clearBtn = uiManager.createIconButton("Clear", "Clear selection");
        Button invertBtn = uiManager.createIconButton("Invert", "Invert selection");
        selectionButtons.getChildren().addAll(selectAllBtn, clearBtn, invertBtn);
        VBox selectionOptions = new VBox(5);
        CheckBox additiveSelect = new CheckBox("Additive selection (Ctrl+Click)");
        additiveSelect.setSelected(true);
        CheckBox boxSelect = new CheckBox("Box selection");
        CheckBox lassoSelect = new CheckBox("Lasso selection");
        selectionOptions.getChildren().addAll(additiveSelect, boxSelect, lassoSelect);
        panel.getChildren().addAll(selectionButtons, new Separator(), selectionOptions);
        return panel;
    }

    private VBox createDisplaySettingsPanel() {
        VBox panel = uiManager.createSectionPanel("DISPLAY SETTINGS");
        Slider brightnessSlider = new Slider(0, 100, 50);
        brightnessSlider.setShowTickLabels(true);
        brightnessSlider.setShowTickMarks(true);
        brightnessSlider.setMajorTickUnit(25);
        Slider contrastSlider = new Slider(0, 100, 50);
        contrastSlider.setShowTickLabels(true);
        contrastSlider.setShowTickMarks(true);
        ColorPicker bgColorPicker = new ColorPicker(Color.GRAY);
        bgColorPicker.setPromptText("Background Color");
        VBox sliders = new VBox(10,
                new Label("Brightness:"), brightnessSlider,
                new Label("Contrast:"), contrastSlider,
                new Label("Background:"), bgColorPicker
        );
        panel.getChildren().add(sliders);
        return panel;
    }

    private HBox createStatusPanel() {
        HBox statusPanel = new HBox(10);
        statusPanel.setPadding(new Insets(8, 15, 8, 15));
        statusPanel.getStyleClass().add("status-panel");
        statusPanel.setAlignment(Pos.CENTER_LEFT);
        statusLabel = new Label("Ready to load 3D models");
        statusLabel.getStyleClass().add("status-label");
        HBox indicators = new HBox(20);
        Label fpsLabel = new Label("FPS: 60");
        fpsLabel.getStyleClass().add("fps-label");
        Label memoryLabel = new Label("Memory: 256MB");
        memoryLabel.getStyleClass().add("memory-label");
        ProgressBar memoryBar = new ProgressBar(0.5);
        memoryBar.setPrefWidth(100);
        indicators.getChildren().addAll(fpsLabel, memoryLabel, memoryBar);
        statusPanel.getChildren().addAll(statusLabel, new Separator(), indicators);
        HBox.setHgrow(statusLabel, Priority.ALWAYS);
        return statusPanel;
    }

    private void demonstrateUIEffects() {
        animationManager.playWelcomeAnimation(root);
        uiManager.showThemePreview();
        uiManager.showTooltipDemo();
    }

    private void openModelFromExplorer() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open 3D Model File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("3D Model Files", "*.obj", "*.stl", "*.ply"),
                new FileChooser.ExtensionFilter("OBJ Files", "*.obj"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File selectedFile = fileChooser.showOpenDialog(root.getScene().getWindow());
        if (selectedFile != null) {
            open3DModelFile(selectedFile);
        }
    }

    private void open3DModelFile(File file) {
        try {
            if (!file.exists()) {
                AlertManager.showError("File not found",
                        "The file does not exist:\n" + file.getAbsolutePath());
                return;
            }

            if (file.length() == 0) {
                AlertManager.showError("Empty file",
                        "The file is empty:\n" + file.getName());
                return;
            }

            String fileName = file.getName().toLowerCase();
            if (!fileName.endsWith(".obj")) {
                boolean proceed = AlertManager.showConfirmation(
                        "Unsupported file format",
                        "The file does not have .obj extension.\n" +
                                "Do you want to try to open it anyway?\n\n" +
                                "File: " + file.getName()
                );
                if (!proceed) return;
            }

            statusLabel.setText("Loading: " + file.getName() + "...");

            // Проверяем первые несколько строк файла для валидации
            String fileContent = Files.readString(file.toPath());

            // Дополнительная проверка формата файла
            if (!isValidObjFormat(fileContent)) {
                AlertManager.showError("Invalid OBJ Format",
                        "The file does not appear to be a valid OBJ file.\n" +
                                "OBJ files should start with vertex definitions (v x y z).\n\n" +
                                "Try opening a different OBJ file.");
                statusLabel.setText("Invalid OBJ Format");
                statusLabel.setStyle("-fx-text-fill: #ff6b6b; -fx-font-weight: bold;");
                Timeline timeline = new Timeline(new KeyFrame(
                        Duration.seconds(3),
                        ae -> statusLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-weight: normal;")
                ));
                timeline.play();
                return;
            }

            Model model = ObjReader.read(fileContent);
            onModelLoadedSuccessfully(file, model);

        } catch (NullPointerException e) {
            // Более детальное сообщение об ошибке
            AlertManager.showError("Parser Error - Null Pointer",
                    "Cannot parse the OBJ file.\n" +
                            "Possible issues:\n" +
                            "1. The file might be empty or corrupted\n" +
                            "2. Incorrect face format (e.g., faces without vertex indices)\n" +
                            "3. Missing vertex definitions before faces\n\n" +
                            "Try opening a different OBJ file.");
            statusLabel.setText("Parser Error - Null Pointer");
            statusLabel.setStyle("-fx-text-fill: #ff6b6b; -fx-font-weight: bold;");
            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.seconds(3),
                    ae -> statusLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-weight: normal;")
            ));
            timeline.play();
            e.printStackTrace(); // Для отладки
        } catch (ObjReaderException e) {
            int lineNumber = extractLineNumber(e);
            AlertManager.showObjReaderError(
                    file.getName(),
                    lineNumber,
                    e.getMessage()
            );
            statusLabel.setText("OBJ Error at line " + lineNumber);
            statusLabel.setStyle("-fx-text-fill: #ff6b6b; -fx-font-weight: bold;");
            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.seconds(3),
                    ae -> statusLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-weight: normal;")
            ));
            timeline.play();
        } catch (IOException e) {
            AlertManager.showError("File read error",
                    "Cannot read the file:\n" + file.getAbsolutePath() +
                            "\n\nError: " + e.getMessage());
            statusLabel.setText("Error reading file");
        } catch (Exception e) {
            AlertManager.showError("Unexpected error",
                    "Failed to load model:\n" + file.getName() +
                            "\n\nError: " + e.getClass().getSimpleName() +
                            "\nMessage: " + e.getMessage());
            statusLabel.setText("Load failed");
            e.printStackTrace(); // Для отладки
        }
    }

    // Добавьте этот вспомогательный метод для проверки формата OBJ
    private boolean isValidObjFormat(String content) {
        if (content == null || content.trim().isEmpty()) {
            return false;
        }

        // Проверяем, содержит ли файл хотя бы одну вершину
        String[] lines = content.split("\n");
        boolean hasVertex = false;

        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("v ")) { // Вершина
                hasVertex = true;
                break;
            }
        }

        return hasVertex;
    }

    private int extractLineNumber(ObjReaderException e) {
        try {
            java.lang.reflect.Method getLineMethod = e.getClass().getMethod("getLineIndex");
            Object result = getLineMethod.invoke(e);
            if (result instanceof Integer) {
                return ((Integer) result) + 1;
            }
        } catch (Exception ex) {
        }
        try {
            java.lang.reflect.Method getLineMethod = e.getClass().getMethod("getLineNumber");
            Object result = getLineMethod.invoke(e);
            if (result instanceof Integer) {
                return (Integer) result;
            }
        } catch (Exception ex) {
        }
        String message = e.getMessage();
        if (message != null) {
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("line[\\s:]+(\\d+)",
                    java.util.regex.Pattern.CASE_INSENSITIVE);
            java.util.regex.Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                try {
                    return Integer.parseInt(matcher.group(1));
                } catch (NumberFormatException nfe) {
                }
            }
        }
        return 0;
    }

    private void onModelLoadedSuccessfully(File file, Model model) {
        try {
            int vertices = 0;
            int polygons = 0;

            try {
                java.lang.reflect.Field verticesField = model.getClass().getDeclaredField("vertices");
                verticesField.setAccessible(true);
                java.util.List<?> verticesList = (java.util.List<?>) verticesField.get(model);
                vertices = verticesList.size();

                java.lang.reflect.Field facesField = model.getClass().getDeclaredField("polygons");
                facesField.setAccessible(true);
                java.util.List<?> facesList = (java.util.List<?>) facesField.get(model);
                polygons = facesList.size();
            } catch (Exception e) {
                try {
                    vertices = model.getVertices().size();
                    polygons = model.getPolygons().size();
                } catch (Exception e2) {
                    vertices = 0;
                    polygons = 0;
                }
            }

            String modelName = file.getName().replaceFirst("[.][^.]+$", "");

            if (objectListView != null) {
                objectListView.getItems().add("✓ " + modelName);
                updateObjectCount();
            }

            statusLabel.setText(String.format(
                    "✓ Loaded: %s | Vertices: %d | Polygons: %d",
                    modelName, vertices, polygons
            ));
            statusLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");

            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.seconds(3),
                    ae -> statusLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-weight: normal;")
            ));
            timeline.play();

            AlertManager.showInfo("Success",
                    String.format("3D Model loaded successfully!\n\n" +
                                    "Name: %s\n" +
                                    "Vertices: %d\n" +
                                    "Polygons: %d\n" +
                                    "Size: %.2f KB",
                            modelName, vertices, polygons,
                            file.length() / 1024.0));

        } catch (Exception e) {
            AlertManager.showError("Display Error",
                    "Failed to display model information:\n" + e.getMessage());
            statusLabel.setText("Error displaying model");
        }
    }

    private void updateObjectCount() {
        if (objectListView != null) {
            int count = objectListView.getItems().size();
            for (javafx.scene.Node node : ((VBox)root.getLeft()).getChildren()) {
                if (node instanceof HBox) {
                    HBox header = (HBox) node;
                    for (javafx.scene.Node child : header.getChildren()) {
                        if (child instanceof Label &&
                                ((Label)child).getText().startsWith("(")) {
                            ((Label)child).setText("(" + count + ")");
                            break;
                        }
                    }
                }
            }
        }
    }

    private void createNewScene() {
        try {
            if (objectListView != null) {
                objectListView.getItems().clear();
                updateObjectCount();
            }
            if (canvas != null) {
                var gc = canvas.getGraphicsContext2D();
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                gc.fillText("New Scene - Ready for 3D models", 50, 50);
            }
            statusLabel.setText("✓ New scene created | Ready to load models");
            statusLabel.setStyle("-fx-text-fill: #4CAF50;");
            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.seconds(2),
                    ae -> statusLabel.setStyle("-fx-text-fill: #ffffff;")
            ));
            timeline.play();
            AlertManager.showInfo("New Scene", "New empty scene created successfully.");
        } catch (Exception e) {
            AlertManager.showError("Scene Creation Failed",
                    "Failed to create new scene:\n" + e.getMessage());
            statusLabel.setText("✗ Failed to create new scene");
        }
    }

    private void safeDeleteObject() {
        try {
            int selectedIndex = objectListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                String objectName = objectListView.getItems().get(selectedIndex);
                objectListView.getItems().remove(selectedIndex);
                updateObjectCount();
                statusLabel.setText("Deleted: " + objectName);
                AlertManager.showInfo("Object Deleted",
                        "Object '" + objectName + "' was successfully removed from the scene.");
            } else {
                AlertManager.showWarning("No Selection",
                        "Please select an object to delete.");
            }
        } catch (IndexOutOfBoundsException e) {
            AlertManager.showError("Deletion Error",
                    "Invalid selection. Please try again.");
        } catch (Exception e) {
            AlertManager.showError("Operation Failed",
                    "Failed to delete object:\n" + e.getMessage());
        }
    }

    private void safeAddPrimitive(String primitiveType) {
        try {
            String primitiveName = primitiveType + " " + (objectListView.getItems().size() + 1);
            objectListView.getItems().add(primitiveName);
            updateObjectCount();
            statusLabel.setText("Added: " + primitiveName);
            AlertManager.showInfo("Primitive Added",
                    String.format("%s added to scene successfully.", primitiveType));
        } catch (Exception e) {
            AlertManager.showError("Add Primitive Failed",
                    String.format("Failed to add %s:\n%s", primitiveType, e.getMessage()));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}