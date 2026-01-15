package com.cgvsu.scene;

import java.util.ArrayList;
import java.util.List;

public class SelectionManager {
    private final List<SceneObject> selectedObjects = new ArrayList<>();
    private final Scene scene;

    public SelectionManager(Scene scene) {
        this.scene = scene;
    }

    public void selectObject(SceneObject obj, boolean addToSelection) {
        if (obj == null) return;

        if (!addToSelection) {
            clearSelection();
        }

        obj.setSelected(true);
        if (!selectedObjects.contains(obj)) {
            selectedObjects.add(obj);
        }
    }

    public void selectByIndex(int index, boolean addToSelection) {
        if (index >= 0 && index < scene.getObjects().size()) {
            selectObject(scene.getObjects().get(index), addToSelection);
        }
    }

    public void clearSelection() {
        for (SceneObject obj : selectedObjects) {
            obj.setSelected(false);
        }
        selectedObjects.clear();
    }

    public void selectAll() {
        clearSelection();
        for (SceneObject obj : scene.getObjects()) {
            selectObject(obj, true);
        }
    }

    public List<SceneObject> getSelectedObjects() {
        return new ArrayList<>(selectedObjects);
    }

    public boolean isSelected(SceneObject obj) {
        return selectedObjects.contains(obj);
    }

    public void removeObject(SceneObject obj) {
        selectedObjects.remove(obj);
        if (obj != null) {
            obj.setSelected(false);
        }
    }

    public boolean hasSelection() {
        return !selectedObjects.isEmpty();
    }

    public int getSelectionCount() {
        return selectedObjects.size();
    }

    public SceneObject getFirstSelectedObject() {
        return selectedObjects.isEmpty() ? null : selectedObjects.get(0);
    }
}