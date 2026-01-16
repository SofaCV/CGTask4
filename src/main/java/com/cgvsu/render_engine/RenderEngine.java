package com.cgvsu.render_engine;

import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.scene.SceneObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class RenderEngine {

    // Базовый рендеринг без камеры
    public static void render(GraphicsContext gc, SceneObject object, int width, int height) {

        if (object == null) {
            gc.setFill(Color.LIGHTGRAY);
            gc.fillRect(0, 0, width, height);
            gc.setFill(Color.BLACK);
            gc.fillText("Выберите модель в Outliner", width / 2.0 - 100, height / 2.0);
            return;
        }

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, width, height);

        Model model = object.getModel();
        if (model == null || model.getVertices().isEmpty() || model.getPolygons().isEmpty()) {
            return;
        }

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1.0);

        List<Vector3f> vertices = model.getVertices();
        List<Polygon> polygons = model.getPolygons();

        for (Polygon polygon : polygons) {
            List<Integer> indices = polygon.getVertexIndices();
            if (indices.size() < 3) continue;

            double[] xPoints = new double[indices.size()];
            double[] yPoints = new double[indices.size()];

            for (int i = 0; i < indices.size(); i++) {
                int idx = indices.get(i);
                if (idx < 0 || idx >= vertices.size()) continue;

                Vector3f vertex = vertices.get(idx);
                xPoints[i] = vertex.getX();
                yPoints[i] = vertex.getY();
            }

            gc.strokePolygon(xPoints, yPoints, indices.size());
        }
    }

    // Перегрузка для совместимости с Scene.java
    public static void render(GraphicsContext gc, Camera camera, SceneObject object, int width, int height) {
        render(gc, object, width, height);
    }

    // Продвинутый рендеринг: Z-буфер, текстура, освещение, wireframe
    public static void renderAdvanced(GraphicsContext gc, Model model,
                                      int width, int height,
                                      EnumSet<RenderMode> modes,
                                      Vector3f lightDir,
                                      Texture texture) {

        if (model == null || model.getVertices().isEmpty() || model.getPolygons().isEmpty()) {
            return;
        }

        ZBuffer zBuffer = new ZBuffer(width, height);
        zBuffer.clear();
        gc.clearRect(0, 0, width, height);

        List<Polygon> triangles = new ArrayList<>();
        for (Polygon poly : model.getPolygons()) {
            triangles.addAll(Triangulator.triangulate(poly));
        }

        List<Vector3f> vertices = model.getVertices();

        for (Polygon tri : triangles) {
            List<Integer> idxList = tri.getVertexIndices();
            if (idxList.size() != 3) continue;

            int[] idx = idxList.stream().mapToInt(i -> i).toArray();
            Vector3f v1 = vertices.get(idx[0]);
            Vector3f v2 = vertices.get(idx[1]);
            Vector3f v3 = vertices.get(idx[2]);

            Vector3f normal = Vector3f.cross(v2.subtract(v1), v3.subtract(v1)).normalize();

            float intensity = 1f;
            if (modes.contains(RenderMode.LIGHTING)) {
                intensity = Lighting.lambert(normal, lightDir);
            }

            Color color;
            if (modes.contains(RenderMode.TEXTURED) && texture != null) {
                float u = 0.5f, v = 0.5f; // TODO: передавать реальные UV
                color = texture.sample(u, v).interpolate(Color.BLACK, 1 - intensity);
            } else {
                color = Color.LIGHTGRAY.interpolate(Color.BLACK, 1 - intensity);
            }

            if (modes.contains(RenderMode.WIREFRAME)) {
                gc.setStroke(Color.BLACK);
                gc.strokeLine(v1.x, v1.y, v2.x, v2.y);
                gc.strokeLine(v2.x, v2.y, v3.x, v3.y);
                gc.strokeLine(v3.x, v3.y, v1.x, v1.y);
            } else {
                double[] xs = {v1.x, v2.x, v3.x};
                double[] ys = {v1.y, v2.y, v3.y};
                double avgDepth = (v1.z + v2.z + v3.z) / 3.0;
                int px = (int)((xs[0]+xs[1]+xs[2])/3);
                int py = (int)((ys[0]+ys[1]+ys[2])/3);
                if(px >= 0 && px < width && py >= 0 && py < height) {
                    if(zBuffer.testAndSet(px, py, (float)avgDepth)) {
                        gc.setFill(color);
                        gc.fillPolygon(xs, ys, 3);
                    }
                }
            }
        }
    }
}
