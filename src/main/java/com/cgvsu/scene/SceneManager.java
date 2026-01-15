package com.cgvsu.scene;

import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class SceneManager {
    private Scene currentScene = new Scene();
    private final Map<Integer, SceneObject> objectMap = new HashMap<>();
    private static int objectCounter = 0;

    public Scene getCurrentScene() {
        return currentScene;
    }

    public SceneObject loadModel(File file, ObjReader reader) {
        try {
            String content = Files.readString(file.toPath());
            Model model = reader.read(content);

            String name = file.getName().replace(".obj", "");
            SceneObject obj = new SceneObject(model, name);

            currentScene.addObject(obj);
            objectMap.put(objectCounter++, obj);

            currentScene.clearSelection();
            currentScene.selectObject(obj, false);

            return obj;
        } catch (Exception e) {
            System.err.println("Failed to load model: " + e.getMessage());
            return null;
        }
    }

    public void removeSelectedObjects() {
        currentScene.removeSelectedObjects();
    }

    public Map<String, Object> getSceneStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalObjects", currentScene.getObjectCount());
        stats.put("selectedObjects", currentScene.getSelectedCount());
        return stats;
    }
}