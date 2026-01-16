package com.cgvsu.render_engine;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;

/**
 * Чисто рендерный конвейер: перевод вершин в экранные координаты
 * без математики трансформаций
 */
public class GraphicConveyor {

    private static final float SCALE_FACTOR = 50.0f;

    public static Vector2f vertexToScreen(Vector3f vertex, int screenWidth, int screenHeight) {
        // Простейшее проецирование для 2D отображения (можно использовать для GUI preview)
        float x = vertex.getX() * SCALE_FACTOR + screenWidth / 2.0f;
        float y = -vertex.getY() * SCALE_FACTOR + screenHeight / 2.0f;
        return new Vector2f(x, y);
    }
}
