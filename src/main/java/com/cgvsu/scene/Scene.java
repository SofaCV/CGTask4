package com.cgvsu.scene;

import com.cgvsu.render_engine.Camera;
import com.cgvsu.render_engine.RenderEngine;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private final List<SceneObject> objects = new ArrayList<>();
    private final List<SceneObject> selectedObjects = new ArrayList<>();

    public void addObject(SceneObject object) {
        objects.add(object);
    }

    public void removeObject(SceneObject object) {
        objects.remove(object);
        selectedObjects.remove(object);
        if (object != null) {
            object.setSelected(false);
        }
    }

    public void selectObject(SceneObject object, boolean addToSelection) {
        if (!addToSelection) {
            for (SceneObject obj : selectedObjects) {
                obj.setSelected(false);
            }
            selectedObjects.clear();
        }

        if (objects.contains(object) && !selectedObjects.contains(object)) {
            selectedObjects.add(object);
            object.setSelected(true);
        }
    }

    public void selectByIndex(int index, boolean addToSelection) {
        if (!addToSelection) {
            for (SceneObject obj : selectedObjects) {
                obj.setSelected(false);
            }
            selectedObjects.clear();
        }

        if (index >= 0 && index < objects.size()) {
            SceneObject obj = objects.get(index);
            if (!selectedObjects.contains(obj)) {
                selectedObjects.add(obj);
                obj.setSelected(true);
            }
        }
    }

    public void deselectObject(SceneObject object) {
        selectedObjects.remove(object);
        if (object != null) {
            object.setSelected(false);
        }
    }

    public void selectAll() {
        for (SceneObject obj : selectedObjects) {
            obj.setSelected(false);
        }
        selectedObjects.clear();

        selectedObjects.addAll(objects);
        for (SceneObject obj : objects) {
            obj.setSelected(true);
        }
    }

    public void clearSelection() {
        for (SceneObject obj : selectedObjects) {
            obj.setSelected(false);
        }
        selectedObjects.clear();
    }

    public List<SceneObject> getSelectedObjects() {
        return new ArrayList<>(selectedObjects);
    }

    public List<SceneObject> getObjects() {
        return new ArrayList<>(objects);
    }

    public SceneObject getObjectByName(String name) {
        for (SceneObject obj : objects) {
            if (obj.getName().equals(name)) {
                return obj;
            }
        }
        return null;
    }

    public SceneObject getObjectByIndex(int index) {
        if (index >= 0 && index < objects.size()) {
            return objects.get(index);
        }
        return null;
    }

    public int getObjectCount() {
        return objects.size();
    }

    public int getSelectedCount() {
        return selectedObjects.size();
    }

    public boolean isEmpty() {
        return objects.isEmpty();
    }

    public boolean hasSelection() {
        return !selectedObjects.isEmpty();
    }

    public void clear() {
        for (SceneObject obj : objects) {
            obj.setSelected(false);
        }
        objects.clear();
        selectedObjects.clear();
    }

    public void removeSelectedObjects() {
        for (SceneObject obj : selectedObjects) {
            obj.setSelected(false);
        }
        objects.removeAll(selectedObjects);
        selectedObjects.clear();
    }

    public void duplicateSelected() {
        if (selectedObjects.isEmpty()) {
            return;
        }

        List<SceneObject> duplicated = new ArrayList<>();
        for (SceneObject obj : selectedObjects) {
            SceneObject copy = new SceneObject(
                    obj.getModel(),
                    obj.getName() + " Copy",
                    obj.getPosition(),
                    obj.getRotation(),
                    obj.getScale()
            );
            copy.setVisible(obj.isVisible());
            duplicated.add(copy);
        }

        for (SceneObject copy : duplicated) {
            objects.add(copy);
            selectedObjects.add(copy);
            copy.setSelected(true);
        }
    }

    public void render(GraphicsContext gc, Camera camera, int width, int height) {
        if (gc == null || camera == null) {
            return;
        }

        gc.clearRect(0, 0, width, height);

        if (objects.isEmpty()) {
            return;
        }

        for (SceneObject object : objects) {
            if (object.isVisible()) {
                RenderEngine.render(gc, camera, object, width, height);
            }
        }
    }

    public void renderAll(GraphicsContext gc, Camera camera, int width, int height) {
        if (gc == null || camera == null) {
            return;
        }

        gc.clearRect(0, 0, width, height);

        for (SceneObject object : objects) {
            if (object.isVisible()) {
                RenderEngine.render(gc, camera, object, width, height);
            }
        }
    }

    public void renderOnlySelected(GraphicsContext gc, Camera camera, int width, int height) {
        if (gc == null || camera == null || selectedObjects.isEmpty()) {
            return;
        }

        gc.clearRect(0, 0, width, height);

        for (SceneObject object : selectedObjects) {
            if (object.isVisible()) {
                RenderEngine.render(gc, camera, object, width, height);
            }
        }
    }
}