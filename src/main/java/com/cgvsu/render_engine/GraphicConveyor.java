package com.cgvsu.render_engine;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;

public class GraphicConveyor {

    private static final float SCALE_FACTOR = 2.0f; // Увеличил масштаб

    public static Vector2f vertexToScreen(Vector3f vertex, int screenWidth, int screenHeight) {
        // Упрощенная изометрическая проекция
        float x = vertex.getX() * SCALE_FACTOR;
        float y = vertex.getY() * SCALE_FACTOR;
        float z = vertex.getZ() * SCALE_FACTOR;

        // Изометрическая проекция
        float screenX = (x - z) * 0.7071f; // cos 45°
        float screenY = y + (x + z) * 0.4082f; // sin 30°

        // Центрирование
        screenX += screenWidth / 2.0f;
        screenY = screenHeight / 2.0f - screenY;

        return new Vector2f(screenX, screenY);
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