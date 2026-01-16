package com.cgvsu.render_engine;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;

public class GraphicConveyor {
    private static final float SCALE_FACTOR = 50.0f;

    public static Vector2f vertexToScreen(Vector3f vertex, int screenWidth, int screenHeight) {
        float x = (vertex.getX() - vertex.getZ()) * SCALE_FACTOR + screenWidth / 2f;
        float y = screenHeight / 2f - (vertex.getY() - (vertex.getX() + vertex.getZ()) / 2f) * SCALE_FACTOR;
        return new Vector2f(x, y);
    }

    public static Vector3f rotateX(Vector3f v, float angleDegrees) {
        float rad = (float) Math.toRadians(angleDegrees);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        float ny = v.getY() * cos - v.getZ() * sin;
        float nz = v.getY() * sin + v.getZ() * cos;
        return new Vector3f(v.getX(), ny, nz);
    }

    public static Vector3f rotateY(Vector3f v, float angleDegrees) {
        float rad = (float) Math.toRadians(angleDegrees);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        float nx = v.getX() * cos + v.getZ() * sin;
        float nz = -v.getX() * sin + v.getZ() * cos;
        return new Vector3f(nx, v.getY(), nz);
    }

    public static Vector3f rotateZ(Vector3f v, float angleDegrees) {
        float rad = (float) Math.toRadians(angleDegrees);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        float nx = v.getX() * cos - v.getY() * sin;
        float ny = v.getX() * sin + v.getY() * cos;
        return new Vector3f(nx, ny, v.getZ());
    }
}
