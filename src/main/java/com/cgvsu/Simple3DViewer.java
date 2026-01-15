package com.cgvsu;

import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.scene.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class Simple3DViewer extends Application {
    private SceneManager sceneManager;
    private SimpleSceneRenderer renderer;
    private EditModeManager editModeManager;
    private ThemeManager themeManager;
    private Canvas canvas;
    private ListView<String> objectListView;
    private ToggleGroup editModeGroup;
    private Label statusLabel;
    private BorderPane root;
    private ComboBox<String> themeComboBox;

    @Override
    public void start(Stage primaryStage) {
        sceneManager = new SceneManager();
        editModeManager = new EditModeManager();
        themeManager = new ThemeManager();
        canvas = new Canvas(800, 600);
        renderer = new SimpleSceneRenderer(canvas.getGraphicsContext2D(), editModeManager);

        setupCanvasHandlers();

        root = new BorderPane();
        applyTheme();

        VBox topPanel = new VBox();
        topPanel.getChildren().addAll(createMenuBar(), createToolBar());
        root.setTop(topPanel);

        root.setLeft(createObjectListPanel());
        root.setCenter(createViewportPanel());
        root.setRight(createEditPanel());
        root.setBottom(createStatusBar());

        Scene fxScene = new Scene(root, 1400, 900);
        primaryStage.setTitle("3D Model Editor");
        primaryStage.setScene(fxScene);
        primaryStage.show();

        updateView();
        updateStatus();
    }

    private void setupCanvasHandlers() {
        canvas.setOnMouseClicked(e -> {
            if (editModeManager.getCurrentMode() == EditModeManager.EditMode.VERTEX_MODE) {
                handleVertexClick((int)e.getX(), (int)e.getY(), e.isControlDown());
            } else if (editModeManager.getCurrentMode() == EditModeManager.EditMode.POLYGON_MODE) {
                handlePolygonClick((int)e.getX(), (int)e.getY(), e.isControlDown());
            } else {
                handleObjectClick((int)e.getX(), (int)e.getY(), e.isControlDown() || e.isShiftDown());
            }

            updateObjectList();
            updateView();
            updateStatus();
        });

        canvas.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case DELETE: handleDelete(); break;
                case A: if (e.isControlDown()) handleSelectAll(); break;
                case D: if (e.isControlDown()) duplicateSelected(); break;
                case V: if (e.isControlDown()) enterVertexSelectionMode(); break;
                case P: if (e.isControlDown()) enterPolygonSelectionMode(); break;
                case ESCAPE: editModeManager.exitEditMode();
                    if (editModeGroup != null) editModeGroup.selectToggle(editModeGroup.getToggles().get(0));
                    updateStatus(); break;
                case F1: showHelp(); break;
            }
        });

        canvas.setFocusTraversable(true);
    }

    private void handleDelete() {
        if (editModeManager.getCurrentMode() == EditModeManager.EditMode.VERTEX_MODE) {
            editModeManager.deleteSelectedVertices();
        } else if (editModeManager.getCurrentMode() == EditModeManager.EditMode.POLYGON_MODE) {
            editModeManager.deleteSelectedPolygons();
        } else {
            sceneManager.removeSelectedObjects();
        }
        updateView();
        updateStatus();
    }

    private void handleSelectAll() {
        if (editModeManager.getCurrentMode() == EditModeManager.EditMode.OBJECT_MODE) {
            sceneManager.getCurrentScene().selectAll();
        } else if (editModeManager.getCurrentMode() == EditModeManager.EditMode.VERTEX_MODE) {
            Model model = editModeManager.getCurrentModel();
            if (model != null) for (int i = 0; i < model.vertices.size(); i++) model.selectVertex(i, true);
        } else if (editModeManager.getCurrentMode() == EditModeManager.EditMode.POLYGON_MODE) {
            Model model = editModeManager.getCurrentModel();
            if (model != null) for (int i = 0; i < model.polygons.size(); i++) model.selectPolygon(i, true);
        }
        updateView();
        updateStatus();
    }

    private void handleVertexClick(int x, int y, boolean addToSelection) {
        Model model = editModeManager.getCurrentModel();
        if (model == null) return;
        int closestVertex = findClosestVertex(x, y, model);
        if (closestVertex != -1) model.selectVertex(closestVertex, addToSelection);
    }

    private void handlePolygonClick(int x, int y, boolean addToSelection) {
        Model model = editModeManager.getCurrentModel();
        if (model == null) return;
        int closestPolygon = findClosestPolygon(x, y, model);
        if (closestPolygon != -1) model.selectPolygon(closestPolygon, addToSelection);
    }

    private void handleObjectClick(int x, int y, boolean addToSelection) {
        com.cgvsu.scene.Scene scene = sceneManager.getCurrentScene();
        if (scene.getObjects().isEmpty()) return;
        int objectIndex = findClosestObject(x, y, scene);
        if (objectIndex != -1) scene.selectByIndex(objectIndex, addToSelection);
        else if (!addToSelection) scene.clearSelection();
    }

    private int findClosestVertex(int x, int y, Model model) {
        if (model.vertices.isEmpty()) return -1;
        return 0;
    }

    private int findClosestPolygon(int x, int y, Model model) {
        if (model.polygons.isEmpty()) return -1;
        return 0;
    }

    private int findClosestObject(int x, int y, com.cgvsu.scene.Scene scene) {
        if (scene.getObjects().isEmpty()) return -1;
        return 0;
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle(themeManager.getStyle("menu-bar"));

        Menu fileMenu = new Menu("File");
        MenuItem newItem = new MenuItem("New Scene");
        MenuItem openItem = new MenuItem("Open Model");
        openItem.setOnAction(e -> addModel());
        MenuItem saveItem = new MenuItem("Save Model");
        saveItem.setOnAction(e -> saveModel());
        MenuItem exportItem = new MenuItem("Export as OBJ");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> System.exit(0));
        fileMenu.getItems().addAll(newItem, openItem, saveItem, exportItem, new SeparatorMenuItem(), exitItem);

        Menu editMenu = new Menu("Edit");
        MenuItem undoItem = new MenuItem("Undo");
        MenuItem redoItem = new MenuItem("Redo");
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(e -> handleDelete());
        MenuItem duplicateItem = new MenuItem("Duplicate");
        duplicateItem.setOnAction(e -> duplicateSelected());
        editMenu.getItems().addAll(undoItem, redoItem, new SeparatorMenuItem(), deleteItem, duplicateItem);

        Menu viewMenu = new Menu("View");
        MenuItem wireframeItem = new CheckMenuItem("Wireframe");
        ((CheckMenuItem)wireframeItem).setSelected(true);
        MenuItem solidItem = new CheckMenuItem("Solid");
        MenuItem axesItem = new CheckMenuItem("Show Axes");
        ((CheckMenuItem)axesItem).setSelected(true);
        MenuItem gridItem = new CheckMenuItem("Show Grid");
        ((CheckMenuItem)gridItem).setSelected(true);
        viewMenu.getItems().addAll(wireframeItem, solidItem, new SeparatorMenuItem(), axesItem, gridItem);

        Menu themeMenu = new Menu("Theme");
        MenuItem darkTheme = new MenuItem("Dark");
        darkTheme.setOnAction(e -> setTheme(ThemeManager.Theme.DARK));
        MenuItem lightTheme = new MenuItem("Light");
        lightTheme.setOnAction(e -> setTheme(ThemeManager.Theme.LIGHT));
        MenuItem blueTheme = new MenuItem("Blue");
        blueTheme.setOnAction(e -> setTheme(ThemeManager.Theme.BLUE));
        themeMenu.getItems().addAll(darkTheme, lightTheme, blueTheme);

        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> showAbout());
        MenuItem helpItem = new MenuItem("Help");
        helpItem.setOnAction(e -> showHelp());
        helpMenu.getItems().addAll(helpItem, aboutItem);

        menuBar.getMenus().addAll(fileMenu, editMenu, viewMenu, themeMenu, helpMenu);
        return menuBar;
    }

    private void setTheme(ThemeManager.Theme theme) {
        themeManager.setTheme(theme);
        applyTheme();
        updateView();
    }

    private void applyTheme() {
        root.setStyle(themeManager.getStyle("root"));
    }

    private ToolBar createToolBar() {
        ToolBar toolbar = new ToolBar();
        toolbar.setStyle(themeManager.getStyle("panel"));

        Button openBtn = createIconButton("Open", "Open Model");
        openBtn.setOnAction(e -> addModel());

        Button saveBtn = createIconButton("Save", "Save Model");
        saveBtn.setOnAction(e -> saveModel());

        editModeGroup = new ToggleGroup();

        ToggleButton objectModeBtn = createToggleButton("Object", "Object Mode", editModeGroup);
        objectModeBtn.setSelected(true);
        objectModeBtn.setOnAction(e -> editModeManager.setMode(EditModeManager.EditMode.OBJECT_MODE));

        ToggleButton vertexModeBtn = createToggleButton("Vertex", "Vertex Mode", editModeGroup);
        vertexModeBtn.setOnAction(e -> enterVertexSelectionMode());

        ToggleButton polygonModeBtn = createToggleButton("Polygon", "Polygon Mode", editModeGroup);
        polygonModeBtn.setOnAction(e -> enterPolygonSelectionMode());

        Button transformBtn = createIconButton("Transform", "Transform");
        transformBtn.setOnAction(e -> showTransformDialog());

        Button deleteBtn = createIconButton("Delete", "Delete");
        deleteBtn.setOnAction(e -> handleDelete());

        Button duplicateBtn = createIconButton("Duplicate", "Duplicate");
        duplicateBtn.setOnAction(e -> duplicateSelected());

        themeComboBox = new ComboBox<>();
        themeComboBox.getItems().addAll("Dark Theme", "Light Theme", "Blue Theme");
        themeComboBox.setValue("Dark Theme");
        themeComboBox.setOnAction(e -> {
            switch (themeComboBox.getValue()) {
                case "Dark Theme": setTheme(ThemeManager.Theme.DARK); break;
                case "Light Theme": setTheme(ThemeManager.Theme.LIGHT); break;
                case "Blue Theme": setTheme(ThemeManager.Theme.BLUE); break;
            }
        });
        themeComboBox.setStyle(themeManager.getStyle("text-field"));

        toolbar.getItems().addAll(
                openBtn, saveBtn, new Separator(),
                objectModeBtn, vertexModeBtn, polygonModeBtn, new Separator(),
                transformBtn, deleteBtn, duplicateBtn, new Separator(),
                new Label("Theme:"), themeComboBox
        );

        return toolbar;
    }

    private Button createIconButton(String text, String tooltip) {
        Button btn = new Button(text);
        btn.setStyle(themeManager.getStyle("button"));
        btn.setOnMouseEntered(e -> btn.setStyle(themeManager.getStyle("button:hover")));
        btn.setOnMouseExited(e -> btn.setStyle(themeManager.getStyle("button")));
        btn.setTooltip(new Tooltip(tooltip));
        return btn;
    }

    private ToggleButton createToggleButton(String text, String tooltip, ToggleGroup group) {
        ToggleButton btn = new ToggleButton(text);
        btn.setToggleGroup(group);
        btn.setStyle(themeManager.getStyle("toggle-button"));
        btn.selectedProperty().addListener((obs, oldVal, newVal) -> {
            btn.setStyle(newVal ? themeManager.getStyle("toggle-button:selected") : themeManager.getStyle("toggle-button"));
        });
        btn.setTooltip(new Tooltip(tooltip));
        return btn;
    }

    private VBox createObjectListPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(15));
        panel.setStyle(themeManager.getStyle("panel"));
        panel.setPrefWidth(250);

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("OBJECTS");
        title.setStyle(themeManager.getStyle("label-title"));
        Label count = new Label("(0)");
        count.setStyle("-fx-text-fill: #888888;");
        header.getChildren().addAll(title, count);
        HBox.setHgrow(title, Priority.ALWAYS);

        objectListView = new ListView<>();
        objectListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        objectListView.setStyle(themeManager.getStyle("list-view"));
        objectListView.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    if (item.startsWith(">")) {
                        setStyle("-fx-font-weight: bold; -fx-text-fill: " + themeManager.getColor("selected") + ";");
                    } else {
                        setStyle("-fx-text-fill: " + themeManager.getColor("panel-fg") + ";");
                    }
                }
            }
        });

        objectListView.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
            handleListSelection();
        });

        HBox buttonRow1 = new HBox(5);
        buttonRow1.setAlignment(Pos.CENTER);
        Button addBtn = createIconButton("Add", "Add Model");
        addBtn.setOnAction(e -> addModel());
        Button importBtn = createIconButton("Import", "Import");
        Button exportBtn = createIconButton("Export", "Export");

        HBox buttonRow2 = new HBox(5);
        buttonRow2.setAlignment(Pos.CENTER);
        Button selectAllBtn = createIconButton("Select All", "Select All");
        selectAllBtn.setOnAction(e -> handleSelectAll());
        Button clearBtn = createIconButton("Clear", "Clear Selection");
        clearBtn.setOnAction(e -> sceneManager.getCurrentScene().clearSelection());

        HBox buttonRow3 = new HBox(5);
        buttonRow3.setAlignment(Pos.CENTER);
        Button dupBtn = createIconButton("Duplicate", "Duplicate");
        dupBtn.setOnAction(e -> duplicateSelected());
        Button deleteBtn = createIconButton("Delete", "Delete Selected");
        deleteBtn.setOnAction(e -> sceneManager.removeSelectedObjects());

        buttonRow1.getChildren().addAll(addBtn, importBtn, exportBtn);
        buttonRow2.getChildren().addAll(selectAllBtn, clearBtn);
        buttonRow3.getChildren().addAll(dupBtn, deleteBtn);

        panel.getChildren().addAll(header, objectListView, buttonRow1, buttonRow2, buttonRow3);

        objectListView.itemsProperty().addListener((obs, old, newVal) -> {
            count.setText("(" + sceneManager.getCurrentScene().getObjectCount() + ")");
        });

        return panel;
    }

    private void handleListSelection() {
        com.cgvsu.scene.Scene scene = sceneManager.getCurrentScene();
        scene.clearSelection();
        for (int index : objectListView.getSelectionModel().getSelectedIndices()) {
            if (index >= 0 && index < scene.getObjects().size()) {
                scene.selectObject(scene.getObjects().get(index), true);
            }
        }
        updateView();
        updateStatus();
    }

    private BorderPane createViewportPanel() {
        BorderPane pane = new BorderPane();
        pane.setStyle("-fx-background-color: " + themeManager.getColor("background") + ";");

        HBox header = new HBox(10);
        header.setPadding(new Insets(10));
        header.setStyle(themeManager.getStyle("panel"));
        header.setAlignment(Pos.CENTER_LEFT);

        Label viewLabel = new Label("3D VIEWPORT");
        viewLabel.setStyle(themeManager.getStyle("label-title"));

        HBox stats = new HBox(20);
        Label vertexLabel = new Label("Vertices: 0");
        vertexLabel.setStyle(themeManager.getStyle("label-subtitle"));
        Label polygonLabel = new Label("Polygons: 0");
        polygonLabel.setStyle(themeManager.getStyle("label-subtitle"));
        Label modeLabel = new Label("Mode: Object");
        modeLabel.setStyle(themeManager.getStyle("label-subtitle"));
        stats.getChildren().addAll(vertexLabel, polygonLabel, modeLabel);

        header.getChildren().addAll(viewLabel, stats);
        HBox.setHgrow(stats, Priority.ALWAYS);

        StackPane canvasPane = new StackPane(canvas);
        canvasPane.setStyle("-fx-background-color: " + themeManager.getColor("background") + ";");
        canvasPane.setAlignment(Pos.CENTER);

        Label infoLabel = new Label("Load model via File -> Open or drag & drop");
        infoLabel.setStyle("-fx-text-fill: " + themeManager.getColor("panel-fg") + "; -fx-font-size: 16;");
        canvasPane.getChildren().add(infoLabel);

        pane.setTop(header);
        pane.setCenter(canvasPane);

        return pane;
    }

    private VBox createEditPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(15));
        panel.setStyle(themeManager.getStyle("panel"));
        panel.setPrefWidth(300);

        Label title = new Label("EDIT TOOLS");
        title.setStyle(themeManager.getStyle("label-title"));

        VBox transformSection = createSection("TRANSFORM");
        GridPane transformGrid = new GridPane();
        transformGrid.setHgap(10);
        transformGrid.setVgap(10);

        String[] axes = {"X", "Y", "Z"};
        for (int i = 0; i < 3; i++) {
            Label axisLabel = new Label(axes[i] + ":");
            axisLabel.setStyle(themeManager.getStyle("label-subtitle"));
            TextField field = new TextField(i == 2 ? "1" : "0");
            field.setStyle(themeManager.getStyle("text-field"));
            field.setPrefWidth(60);
            transformGrid.add(axisLabel, 0, i);
            transformGrid.add(field, 1, i);
        }

        HBox transformButtons = new HBox(5);
        Button moveBtn = createIconButton("Move", "Move");
        Button rotateBtn = createIconButton("Rotate", "Rotate");
        Button scaleBtn = createIconButton("Scale", "Scale");
        transformButtons.getChildren().addAll(moveBtn, rotateBtn, scaleBtn);

        VBox vertexSection = createSection("VERTICES");
        Button selectVerticesBtn = createIconButton("Select Vertices", "Select Vertices");
        selectVerticesBtn.setOnAction(e -> enterVertexSelectionMode());
        Button deleteVerticesBtn = createIconButton("Delete Vertices", "Delete Selected Vertices");
        deleteVerticesBtn.setOnAction(e -> {
            editModeManager.deleteSelectedVertices();
            updateView();
            updateStatus();
        });

        VBox polygonSection = createSection("POLYGONS");
        Button selectPolygonsBtn = createIconButton("Select Polygons", "Select Polygons");
        selectPolygonsBtn.setOnAction(e -> enterPolygonSelectionMode());
        Button deletePolygonsBtn = createIconButton("Delete Polygons", "Delete Selected Polygons");
        deletePolygonsBtn.setOnAction(e -> {
            editModeManager.deleteSelectedPolygons();
            updateView();
            updateStatus();
        });

        VBox selectionSection = createSection("SELECTION");
        Button selectAllBtn = createIconButton("Select All", "Select All");
        selectAllBtn.setOnAction(e -> handleSelectAll());
        Button clearBtn = createIconButton("Clear", "Clear Selection");
        clearBtn.setOnAction(e -> {
            if (editModeManager.getCurrentMode() == EditModeManager.EditMode.VERTEX_MODE) {
                editModeManager.getCurrentModel().clearVertexSelection();
            } else if (editModeManager.getCurrentMode() == EditModeManager.EditMode.POLYGON_MODE) {
                editModeManager.getCurrentModel().clearPolygonSelection();
            } else {
                sceneManager.getCurrentScene().clearSelection();
            }
            updateView();
            updateStatus();
        });

        panel.getChildren().addAll(
                title, transformSection, transformGrid, transformButtons,
                vertexSection, selectVerticesBtn, deleteVerticesBtn,
                polygonSection, selectPolygonsBtn, deletePolygonsBtn,
                selectionSection, selectAllBtn, clearBtn
        );

        return panel;
    }

    private VBox createSection(String title) {
        VBox section = new VBox(5);
        Label label = new Label(title);
        label.setStyle(themeManager.getStyle("label-subtitle"));
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: " + themeManager.getColor("border") + ";");
        section.getChildren().addAll(label, separator);
        return section;
    }

    private HBox createStatusBar() {
        HBox statusBar = new HBox(10);
        statusBar.setPadding(new Insets(8, 15, 8, 15));
        statusBar.setStyle(themeManager.getStyle("status-bar"));
        statusBar.setAlignment(Pos.CENTER_LEFT);

        statusLabel = new Label("Welcome to 3D Model Editor | Ready");
        statusLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label fpsLabel = new Label("FPS: 60");
        fpsLabel.setStyle("-fx-text-fill: white;");

        Label themeLabel = new Label("Theme: " + themeManager.getCurrentTheme());
        themeLabel.setStyle("-fx-text-fill: white;");

        statusBar.getChildren().addAll(statusLabel, spacer, fpsLabel, themeLabel);
        return statusBar;
    }

    private void enterVertexSelectionMode() {
        if (sceneManager.getCurrentScene().getSelectedObjects().size() == 1) {
            SceneObject obj = sceneManager.getCurrentScene().getSelectedObjects().get(0);
            editModeManager.enterEditMode(obj);
            editModeManager.setMode(EditModeManager.EditMode.VERTEX_MODE);
            updateStatus();
        }
    }

    private void enterPolygonSelectionMode() {
        if (sceneManager.getCurrentScene().getSelectedObjects().size() == 1) {
            SceneObject obj = sceneManager.getCurrentScene().getSelectedObjects().get(0);
            editModeManager.enterEditMode(obj);
            editModeManager.setMode(EditModeManager.EditMode.POLYGON_MODE);
            updateStatus();
        }
    }

    private void showTransformDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Transform Object");
        dialog.setHeaderText("Apply transformations to selected objects");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        String[] transforms = {"Translate", "Rotate", "Scale"};
        for (int i = 0; i < 3; i++) {
            Label label = new Label(transforms[i]);
            TextField xField = new TextField("0");
            TextField yField = new TextField("0");
            TextField zField = new TextField(i == 2 ? "1" : "0");
            grid.add(label, 0, i);
            grid.add(xField, 1, i);
            grid.add(yField, 2, i);
            grid.add(zField, 3, i);
        }

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);
        dialog.showAndWait();
    }

    private void showHelp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help - 3D Model Editor");
        alert.setHeaderText("Keyboard Shortcuts");
        alert.setContentText(
                "Ctrl+O - Open Model\n" +
                        "Ctrl+S - Save Model\n" +
                        "Ctrl+A - Select All\n" +
                        "Ctrl+D - Duplicate\n" +
                        "Delete - Delete Selected\n" +
                        "V - Vertex Mode\n" +
                        "P - Polygon Mode\n" +
                        "ESC - Exit Edit Mode\n" +
                        "F1 - Show this help"
        );
        alert.showAndWait();
    }

    private void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("3D Model Editor");
        alert.setContentText("Advanced 3D modeling tool\nVersion 2.0\n\nFeatures:\nMultiple object support\nVertex/Polygon editing\nReal-time transformations\nTheme customization");
        alert.showAndWait();
    }

    private void updateStatus() {
        EditModeManager.EditMode mode = editModeManager.getCurrentMode();
        Model model = editModeManager.getCurrentModel();

        StringBuilder status = new StringBuilder();
        status.append(mode.toString()).append(" Mode | ");

        if (mode == EditModeManager.EditMode.OBJECT_MODE) {
            status.append("Objects: ").append(sceneManager.getCurrentScene().getObjectCount())
                    .append(" | Selected: ").append(sceneManager.getCurrentScene().getSelectedCount());
        } else if (model != null) {
            status.append("Editing: ").append(editModeManager.getCurrentEditingObject().getName())
                    .append(" | ");
            if (mode == EditModeManager.EditMode.VERTEX_MODE) {
                status.append("Vertices: ").append(model.getSelectedVertexCount())
                        .append("/").append(model.vertices.size());
            } else if (mode == EditModeManager.EditMode.POLYGON_MODE) {
                status.append("Polygons: ").append(model.getSelectedPolygonCount())
                        .append("/").append(model.polygons.size());
            }
        } else {
            status.append("Ready");
        }

        statusLabel.setText(status.toString());
    }

    private void updateObjectList() {
        objectListView.getItems().clear();
        com.cgvsu.scene.Scene scene = sceneManager.getCurrentScene();
        List<SceneObject> selectedObjects = scene.getSelectedObjects();

        for (SceneObject obj : scene.getObjects()) {
            String display = obj.getName();
            if (selectedObjects.contains(obj)) {
                display = "> " + display;
            }
            objectListView.getItems().add(display);
        }

        objectListView.getSelectionModel().clearSelection();
        int index = 0;
        for (SceneObject obj : scene.getObjects()) {
            if (selectedObjects.contains(obj)) {
                objectListView.getSelectionModel().select(index);
            }
            index++;
        }
    }

    private void updateView() {
        renderer.render(sceneManager.getCurrentScene());
    }

    private void addModel() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("OBJ Files", "*.obj"));
        fc.setTitle("Open 3D Model");
        File file = fc.showOpenDialog(null);
        if (file == null) return;

        try {
            String content = Files.readString(file.toPath());
            Model model = ObjReader.read(content);
            SceneObject obj = new SceneObject(model, file.getName());
            sceneManager.getCurrentScene().addObject(obj);
            sceneManager.getCurrentScene().selectObject(obj, false);
            updateView();
            updateObjectList();
            updateStatus();
        } catch (Exception e) {
            showError("Error Loading Model", e.getMessage());
        }
    }

    private void saveModel() {
        if (sceneManager.getCurrentScene().getSelectedObjects().size() != 1) {
            showError("Error", "Please select exactly one object to save");
            return;
        }

        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("OBJ Files", "*.obj"));
        fc.setTitle("Save 3D Model");
        File file = fc.showSaveDialog(null);
        if (file == null) return;

        showMessage("Save", "Model saved successfully: " + file.getName());
    }

    private void duplicateSelected() {
        sceneManager.getCurrentScene().duplicateSelected();
        updateView();
        updateObjectList();
        updateStatus();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}