package com.cgvsu.render_engine;

import com.cgvsu.math.Vector2f;
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
                Vector3f transformed = applyObjectTransform(vertex, object);
                Vector2f screenPoint = GraphicConveyor.vertexToScreen(transformed, width, height);

                xPoints[i] = screenPoint.getX();
                yPoints[i] = screenPoint.getY();
            }

            gc.strokePolygon(xPoints, yPoints, indices.size());
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