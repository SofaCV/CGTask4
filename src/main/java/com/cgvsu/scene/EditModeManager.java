package com.cgvsu.scene;

import com.cgvsu.model.Model;
import java.util.HashSet;
import java.util.Set;

public class EditModeManager {
    public enum EditMode {
        OBJECT_MODE,
        VERTEX_MODE,
        POLYGON_MODE
    }

    private EditMode currentMode = EditMode.OBJECT_MODE;
    private SceneObject currentEditingObject;

    public void enterEditMode(SceneObject object) {
        this.currentEditingObject = object;
        this.currentMode = EditMode.VERTEX_MODE;
    }

    public void exitEditMode() {
        this.currentEditingObject = null;
        this.currentMode = EditMode.OBJECT_MODE;
    }

    public EditMode getCurrentMode() {
        return currentMode;
    }

    public void setMode(EditMode mode) {
        this.currentMode = mode;
    }

    public SceneObject getCurrentEditingObject() {
        return currentEditingObject;
    }

    public Model getCurrentModel() {
        return currentEditingObject != null ? currentEditingObject.getModel() : null;
    }

    public void deleteSelectedVertices() {
        if (currentMode == EditMode.VERTEX_MODE && currentEditingObject != null) {
            currentEditingObject.getModel().deleteSelectedVertices();
        }
    }

    public void deleteSelectedPolygons() {
        if (currentMode == EditMode.POLYGON_MODE && currentEditingObject != null) {
            currentEditingObject.getModel().deleteSelectedPolygons();
        }
    }
}