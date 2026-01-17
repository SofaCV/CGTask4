package com.cgvsu.render_engine;

import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.scene.SceneObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class RenderEngine {

    public static void render(GraphicsContext gc, Camera camera,
                              SceneObject object, int width, int height) {

        gc.clearRect(0, 0, width, height);
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, width, height);

        if (object == null) {
            drawNoModel(gc, width, height);
            return;
        }

        Model model = object.getModel();
        if (model == null) {
            drawNoModel(gc, width, height);
            return;
        }

        List<Vector3f> vertices = model.getVertices();
        List<Polygon> polygons = model.getPolygons();

        if (vertices == null || vertices.isEmpty()) {
            drawNoModel(gc, width, height);
            return;
        }

        if (polygons == null || polygons.isEmpty()) {
            drawNoModel(gc, width, height);
            return;
        }

        float minX = Float.MAX_VALUE;
        float maxX = -Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        float maxY = -Float.MAX_VALUE;

        for (Vector3f vertex : vertices) {
            if (vertex.x < minX) minX = vertex.x;
            if (vertex.x > maxX) maxX = vertex.x;
            if (vertex.y < minY) minY = vertex.y;
            if (vertex.y > maxY) maxY = vertex.y;
        }

        float centerX = (minX + maxX) / 2.0f;
        float centerY = (minY + maxY) / 2.0f;

        float modelWidth = maxX - minX;
        float modelHeight = maxY - minY;

        float scale = 50.0f;
        if (modelWidth > 0 && modelHeight > 0) {
            float scaleX = (width * 0.7f) / modelWidth;
            float scaleY = (height * 0.7f) / modelHeight;
            scale = Math.min(scaleX, scaleY);
            scale = Math.max(10.0f, Math.min(500.0f, scale));
        }

        gc.setStroke(Color.BLACK);
        gc.setFill(Color.rgb(200, 200, 200));
        gc.setLineWidth(1.0);

        int drawnPolygons = 0;
        for (Polygon polygon : polygons) {
            List<Integer> vertexIndices = polygon.getVertexIndices();
            if (vertexIndices == null || vertexIndices.size() < 3) {
                continue;
            }

            double[] xPoints = new double[vertexIndices.size()];
            double[] yPoints = new double[vertexIndices.size()];
            boolean valid = true;

            for (int i = 0; i < vertexIndices.size(); i++) {
                int vertexIndex = vertexIndices.get(i);
                if (vertexIndex < 0 || vertexIndex >= vertices.size()) {
                    valid = false;
                    break;
                }

                Vector3f vertex = vertices.get(vertexIndex);

                float x = (vertex.x - centerX) * scale;
                float y = (vertex.y - centerY) * scale;

                xPoints[i] = x + width / 2.0;
                yPoints[i] = height / 2.0 - y;
            }

            if (valid) {
                gc.fillPolygon(xPoints, yPoints, vertexIndices.size());
                gc.strokePolygon(xPoints, yPoints, vertexIndices.size());
                drawnPolygons++;
            }
        }

        gc.setFill(Color.RED);
        for (Vector3f vertex : vertices) {
            float x = (vertex.x - centerX) * scale + width / 2.0f;
            float y = height / 2.0f - (vertex.y - centerY) * scale;
            gc.fillOval(x - 2, y - 2, 4, 4);
        }

        gc.setFill(Color.BLUE);
        gc.fillText("Модель: " + object.getName(), 10, 20);
        gc.fillText("Вершин: " + vertices.size(), 10, 40);
        gc.fillText("Полигонов: " + drawnPolygons, 10, 60);
        gc.fillText("Масштаб: " + String.format("%.1f", scale), 10, 80);

        gc.setFill(Color.GREEN);
        gc.fillOval(width/2 - 3, height/2 - 3, 6, 6);
    }

    private static void drawNoModel(GraphicsContext gc, int width, int height) {
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, width, height);
        gc.setFill(Color.BLACK);
        gc.fillText("Нет модели для отображения", width/2 - 100, height/2);
    }
}