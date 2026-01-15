package com.cgvsu.scene;

import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;

public class SceneObject {
    private Model model;
    private String name;
    private Vector3f position = new Vector3f(0, 0, 0);
    private Vector3f rotation = new Vector3f(0, 0, 0);
    private Vector3f scale = new Vector3f(1, 1, 1);
    private boolean visible = true;
    private boolean selected = false;

    public SceneObject(Model model, String name) {
        this.model = model;
        this.name = name;
    }

    public SceneObject(Model model, String name, Vector3f position, Vector3f rotation, Vector3f scale) {
        this.model = model;
        this.name = name;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void move(Vector3f delta) {
        this.position = new Vector3f(
                position.getX() + delta.getX(),
                position.getY() + delta.getY(),
                position.getZ() + delta.getZ()
        );
    }

    public void rotate(Vector3f delta) {
        this.rotation = new Vector3f(
                rotation.getX() + delta.getX(),
                rotation.getY() + delta.getY(),
                rotation.getZ() + delta.getZ()
        );
    }

    public void scale(Vector3f factor) {
        this.scale = new Vector3f(
                scale.getX() * factor.getX(),
                scale.getY() * factor.getY(),
                scale.getZ() * factor.getZ()
        );
    }

    public void setPosition(float x, float y, float z) {
        this.position = new Vector3f(x, y, z);
    }

    public void setRotation(float x, float y, float z) {
        this.rotation = new Vector3f(x, y, z);
    }

    public void setScale(float x, float y, float z) {
        this.scale = new Vector3f(x, y, z);
    }

    public void resetTransform() {
        this.position = new Vector3f(0, 0, 0);
        this.rotation = new Vector3f(0, 0, 0);
        this.scale = new Vector3f(1, 1, 1);
    }

    public SceneObject copy() {
        SceneObject copy = new SceneObject(
                this.model,
                this.name + " Copy",
                new Vector3f(this.position),
                new Vector3f(this.rotation),
                new Vector3f(this.scale)
        );
        copy.setVisible(this.visible);
        copy.setSelected(this.selected);
        return copy;
    }

    public boolean hasModel() {
        return model != null;
    }

    public int getVertexCount() {
        return model != null ? model.vertices.size() : 0;
    }

    public int getPolygonCount() {
        return model != null ? model.polygons.size() : 0;
    }

    @Override
    public String toString() {
        return name + " (V:" + getVertexCount() + ", P:" + getPolygonCount() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        SceneObject other = (SceneObject) obj;
        return this.name.equals(other.name) &&
                this.position.equals(other.position) &&
                this.rotation.equals(other.rotation) &&
                this.scale.equals(other.scale) &&
                this.visible == other.visible &&
                this.selected == other.selected &&
                this.model == other.model;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + position.hashCode();
        result = 31 * result + rotation.hashCode();
        result = 31 * result + scale.hashCode();
        result = 31 * result + (visible ? 1 : 0);
        result = 31 * result + (selected ? 1 : 0);
        return result;
    }
}