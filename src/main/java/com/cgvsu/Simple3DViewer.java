package com.cgvsu;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Simple3DViewer extends Application {
    private UIManager uiManager;
    private ThemeManager themeManager;
    private AnimationManager animationManager;

    private BorderPane root;
    private Canvas canvas;
    private ListView<String> objectListView;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
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
            System.err.println("Ошибка при запуске приложения: " + e.getMessage());
            e.printStackTrace();

            showErrorWindow(primaryStage, e);
        }
    }

    private void showErrorWindow(Stage stage, Exception e) {
        VBox errorBox = new VBox(10);
        errorBox.setPadding(new Insets(20));
        errorBox.setAlignment(Pos.CENTER);

        Label errorLabel = new Label("Ошибка запуска приложения");
        errorLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextArea errorDetails = new TextArea(e.toString());
        errorDetails.setEditable(false);
        errorDetails.setPrefSize(600, 300);

        Label solutionLabel = new Label("Решение: Убедитесь, что CSS файлы находятся в папке src/main/resources/styles/");
        solutionLabel.setStyle("-fx-text-fill: #007acc; -fx-font-size: 14px;");

        Button closeBtn = new Button("Закрыть");
        closeBtn.setOnAction(ev -> stage.close());

        errorBox.getChildren().addAll(errorLabel, errorDetails, solutionLabel, closeBtn);

        Scene errorScene = new Scene(errorBox, 700, 400);
        stage.setScene(errorScene);
        stage.setTitle("Ошибка");
        stage.show();
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
        Button openBtn = uiManager.createIconButton("Open", "Open model", "Ctrl+O");
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

        Label count = new Label("(3)");
        count.getStyleClass().add("object-count");

        header.getChildren().addAll(title, count);
        HBox.setHgrow(title, Priority.ALWAYS);

        objectListView = new ListView<>();
        objectListView.getItems().addAll(
                "> Cube (Selected)",
                "Sphere",
                "Torus",
                "Monkey",
                "Teapot"
        );
        objectListView.getStyleClass().add("object-list");
        objectListView.setPrefHeight(400);

        HBox objectButtons = new HBox(5);
        objectButtons.setAlignment(Pos.CENTER);

        Button addBtn = uiManager.createIconButton("Add", "Add object");
        Button importBtn = uiManager.createIconButton("Import", "Import object");
        Button exportBtn = uiManager.createIconButton("Export", "Export object");

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

        StackPane canvasContainer = new StackPane(canvas);

        Label demoLabel = new Label("3D Viewport\n(Drag & drop models here)\n\nControls:\n• Mouse: Rotate view\n• Scroll: Zoom\n• WASD: Move camera");
        demoLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 14px; -fx-text-alignment: center;");
        canvasContainer.getChildren().add(demoLabel);

        viewport.setTop(viewportTools);
        viewport.setCenter(canvasContainer);

        return viewport;
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

        ColorPicker bgColorPicker = new ColorPicker(javafx.scene.paint.Color.GRAY);
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

        statusLabel = new Label("Ready | Objects: 5 | Vertices: 2543 | Polygons: 5120");
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

    public static void main(String[] args) {
        launch(args);
    }
}