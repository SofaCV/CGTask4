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

    // Простое отображение объекта без Z-буфера
    public static void render(GraphicsContext gc, Camera camera, SceneObject object, int width, int height) {
        if (object == null) {
            gc.setFill(Color.LIGHTGRAY);
            gc.fillRect(0, 0, width, height);
            gc.setFill(Color.BLACK);
            gc.fillText("Выберите модель в Outliner", width / 2 - 100, height / 2);
            return;
        }

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, width, height);

        Model model = object.getModel();
        if (model == null || model.getVertices().isEmpty() || model.getPolygons().isEmpty()) return;

        List<Vector3f> vertices = model.getVertices();
        List<Polygon> polygons = model.getPolygons();

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1.0);

        for (Polygon poly : polygons) {
            List<Integer> idxs = poly.getVertexIndices();
            if (idxs.size() < 3) continue;
            double[] xs = new double[idxs.size()];
            double[] ys = new double[idxs.size()];

            for (int i = 0; i < idxs.size(); i++) {
                Vector3f v = vertices.get(idxs.get(i));
                Vector3f t = applyObjectTransform(v, object);
                Vector3f camSpace = t.subtract(camera.getPosition());
                Vector3f projected = camSpace; // Пока без матрицы проекции
                Vector3f screen = projected;
                xs[i] = screen.getX();
                ys[i] = screen.getY();
            }
            gc.strokePolygon(xs, ys, idxs.size());
        }
    }

    // Расширенное рендеринг с Z-буфером, текстурами, освещением
    public static void renderAdvanced(GraphicsContext gc, Model model, int width, int height,
                                      EnumSet<RenderMode> modes, Vector3f lightDir, Texture texture) {

        if (model == null || model.getVertices().isEmpty() || model.getPolygons().isEmpty()) return;

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
            float intensity = modes.contains(RenderMode.LIGHTING) ? Lighting.lambert(normal, lightDir) : 1f;

            Color color;
            if (modes.contains(RenderMode.TEXTURED) && texture != null) {
                color = texture.sample(0.5f, 0.5f).interpolate(Color.BLACK, 1 - intensity);
            } else {
                color = Color.LIGHTGRAY.interpolate(Color.BLACK, 1 - intensity);
            }

            if (modes.contains(RenderMode.WIREFRAME)) {
                gc.setStroke(Color.BLACK);
                gc.strokeLine(v1.getX(), v1.getY(), v2.getX(), v2.getY());
                gc.strokeLine(v2.getX(), v2.getY(), v3.getX(), v3.getY());
                gc.strokeLine(v3.getX(), v3.getY(), v1.getX(), v1.getY());
            } else {
                double[] xs = {v1.getX(), v2.getX(), v3.getX()};
                double[] ys = {v1.getY(), v2.getY(), v3.getY()};
                double avgZ = (v1.getZ() + v2.getZ() + v3.getZ()) / 3.0;
                int px = (int) ((xs[0] + xs[1] + xs[2]) / 3);
                int py = (int) ((ys[0] + ys[1] + ys[2]) / 3);
                if (px >= 0 && px < width && py >= 0 && py < height) {
                    if (zBuffer.testAndSet(px, py, (float) avgZ)) {
                        gc.setFill(color);
                        gc.fillPolygon(xs, ys, 3);
                    }
                }
            }
        }
    }

    private static Vector3f applyObjectTransform(Vector3f v, SceneObject obj) {
        Vector3f tv = new Vector3f(v.getX(), v.getY(), v.getZ());
        tv.setX(tv.getX() * obj.getScale().getX());
        tv.setY(tv.getY() * obj.getScale().getY());
        tv.setZ(tv.getZ() * obj.getScale().getZ());

        tv = GraphicConveyor.rotateX(tv, obj.getRotation().getX());
        tv = GraphicConveyor.rotateY(tv, obj.getRotation().getY());
        tv = GraphicConveyor.rotateZ(tv, obj.getRotation().getZ());

        tv.setX(tv.getX() + obj.getPosition().getX());
        tv.setY(tv.getY() + obj.getPosition().getY());
        tv.setZ(tv.getZ() + obj.getPosition().getZ());

        return tv;
    }
}
