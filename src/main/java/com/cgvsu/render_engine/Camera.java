package com.cgvsu.render_engine;

import com.cgvsu.math.Vector3f;

public class Camera {
    private Vector3f position;
    private Vector3f target;
    private float fov;
    private float aspectRatio;
    private float nearPlane;
    private float farPlane;

    public Camera(Vector3f position, Vector3f target,
                  float fov, float aspectRatio,
                  float nearPlane, float farPlane) {
        this.position = position;
        this.target = target;
        this.fov = fov;
        this.aspectRatio = aspectRatio;
        this.nearPlane = nearPlane;
        this.farPlane = farPlane;
    }

    public Vector3f getPosition() { return position; }
    public void setPosition(Vector3f position) { this.position = position; }

    public Vector3f getTarget() { return target; }
    public void setTarget(Vector3f target) { this.target = target; }

    public float getFov() { return fov; }
    public void setFov(float fov) { this.fov = fov; }

    public float getAspectRatio() { return aspectRatio; }
    public void setAspectRatio(float aspectRatio) { this.aspectRatio = aspectRatio; }

    public float getNearPlane() { return nearPlane; }
    public float getFarPlane() { return farPlane; }

    public void move(Vector3f translation) {
        position = position.add(translation);
        target = target.add(translation);
    }

    public Vector3f getDirection() {
        Vector3f dir = target.subtract(position);
        dir.normalize();
        return dir;
    }

    public void moveForward(float distance) {
        Vector3f dir = getDirection();
        position = position.add(dir.multiply(distance));
        target = target.add(dir.multiply(distance));
    }

    public void moveRight(float distance) {
        Vector3f dir = getDirection();
        Vector3f right = new Vector3f(dir.getZ(), 0, -dir.getX()).normalize();
        position = position.add(right.multiply(distance));
        target = target.add(right.multiply(distance));
    }
}
