package com.cgvsu.render_engine;

import java.util.ArrayList;
import java.util.List;

public class CameraManager {
    private final List<Camera> cameras = new ArrayList<>();
    private int activeIndex = 0;

    public void addCamera(Camera camera) { cameras.add(camera); }
    public void removeCamera(Camera camera) { cameras.remove(camera); }
    public Camera getActiveCamera() {
        if (cameras.isEmpty()) return null;
        return cameras.get(activeIndex);
    }

    public void nextCamera() {
        if (!cameras.isEmpty()) activeIndex = (activeIndex + 1) % cameras.size();
    }

    public void previousCamera() {
        if (!cameras.isEmpty()) activeIndex = (activeIndex - 1 + cameras.size()) % cameras.size();
    }

    public List<Camera> getAllCameras() { return cameras; }
}
