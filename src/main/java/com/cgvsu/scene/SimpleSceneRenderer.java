package com.cgvsu.scene;

import com.cgvsu.math.Vector3f;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SimpleSceneRenderer {
    private final GraphicsContext gc;
    private final EditModeManager editModeManager;
    private final double viewportWidth;
    private final double viewportHeight;

    public SimpleSceneRenderer(GraphicsContext gc, EditModeManager editModeManager) {
        this.gc = gc;
        this.editModeManager = editModeManager;
        this.viewportWidth = gc.getCanvas().getWidth();
        this.viewportHeight = gc.getCanvas().getHeight();
    }

    public void render(Scene scene) {
        clearCanvas(Color.rgb(45, 45, 48));
        drawGrid(Color.rgb(64, 64, 64), Color.rgb(80, 80, 80));
        drawAxes();

        for (SceneObject obj : scene.getObjects()) {
            renderObject(obj, Color.LIGHTGRAY, Color.BLACK);
            if (obj.isSelected()) {
                renderObjectSelection(obj, Color.RED);
            }
        }

        if (editModeManager.getCurrentMode() != EditModeManager.EditMode.OBJECT_MODE) {
            renderEditModeSelection();
        }
    }

    private void clearCanvas(Color backgroundColor) {
        gc.clearRect(0, 0, viewportWidth, viewportHeight);
        gc.setFill(backgroundColor);
        gc.fillRect(0, 0, viewportWidth, viewportHeight);
    }

    private void drawGrid(Color gridColor, Color axisColor) {
        gc.setStroke(gridColor);
        gc.setLineWidth(0.5);

        double gridSize = 50;
        for (double x = 0; x < viewportWidth; x += gridSize) {
            gc.strokeLine(x, 0, x, viewportHeight);
        }
        for (double y = 0; y < viewportHeight; y += gridSize) {
            gc.strokeLine(0, y, viewportWidth, y);
        }

        gc.setStroke(axisColor);
        gc.setLineWidth(1);
        double centerX = viewportWidth / 2;
        double centerY = viewportHeight / 2;
        gc.strokeLine(centerX, 0, centerX, viewportHeight);
        gc.strokeLine(0, centerY, viewportWidth, centerY);
    }

    private void drawAxes() {
        double centerX = viewportWidth / 2;
        double centerY = viewportHeight / 2;

        gc.setStroke(Color.RED);
        gc.setLineWidth(2);
        gc.strokeLine(centerX, centerY, centerX + 100, centerY);

        gc.setStroke(Color.GREEN);
        gc.strokeLine(centerX, centerY, centerX, centerY - 100);

        gc.setStroke(Color.BLUE);
        gc.strokeLine(centerX, centerY, centerX - 70, centerY + 70);
    }

    private void renderObject(SceneObject obj, Color fillColor, Color strokeColor) {
        gc.setStroke(strokeColor);
        gc.setFill(fillColor);

        Vector3f pos = obj.getPosition();
        float baseSize = 30 * obj.getScale().getX();

        float[] xPoints = {
                pos.getX() - baseSize,
                pos.getX(),
                pos.getX() + baseSize,
                pos.getX(),
                pos.getX() - baseSize
        };

        float[] yPoints = {
                pos.getY(),
                pos.getY() - baseSize,
                pos.getY(),
                pos.getY() + baseSize,
                pos.getY()
        };

        gc.fillPolygon(
                new double[]{xPoints[0], xPoints[1], xPoints[2], xPoints[3], xPoints[4]},
                new double[]{yPoints[0], yPoints[1], yPoints[2], yPoints[3], yPoints[4]},
                5
        );

        gc.strokePolygon(
                new double[]{xPoints[0], xPoints[1], xPoints[2], xPoints[3], xPoints[4]},
                new double[]{yPoints[0], yPoints[1], yPoints[2], yPoints[3], yPoints[4]},
                5
        );
    }

    private void renderObjectSelection(SceneObject obj, Color selectionColor) {
        gc.setStroke(selectionColor);
        gc.setLineWidth(2);

        Vector3f pos = obj.getPosition();
        float size = 50 * Math.max(Math.max(obj.getScale().getX(), obj.getScale().getY()), obj.getScale().getZ());

        gc.strokeLine(pos.getX() - size, pos.getY(), pos.getX() + size, pos.getY());
        gc.strokeLine(pos.getX(), pos.getY() - size, pos.getX(), pos.getY() + size);
        gc.strokeOval(pos.getX() - size/2, pos.getY() - size/2, size, size);
        gc.setLineWidth(1);
    }

    private void renderEditModeSelection() {
        com.cgvsu.model.Model model = editModeManager.getCurrentModel();
        if (model == null) return;

        EditModeManager.EditMode mode = editModeManager.getCurrentMode();

        if (mode == EditModeManager.EditMode.VERTEX_MODE) {
            renderSelectedVertices(model);
        } else if (mode == EditModeManager.EditMode.POLYGON_MODE) {
            renderSelectedPolygons(model);
        }
    }

    private void renderSelectedVertices(com.cgvsu.model.Model model) {
        gc.setFill(Color.RED);
        gc.setStroke(Color.RED);

        for (Integer vertexIndex : model.getSelectedVertexIndices()) {
            if (vertexIndex >= 0 && vertexIndex < model.vertices.size()) {
                Vector3f vertex = model.vertices.get(vertexIndex);
                double screenX = viewportWidth / 2 + vertex.x;
                double screenY = viewportHeight / 2 - vertex.y;

                gc.fillOval(screenX - 4, screenY - 4, 8, 8);
                gc.strokeOval(screenX - 6, screenY - 6, 12, 12);
            }
        }
    }

    private void renderSelectedPolygons(com.cgvsu.model.Model model) {
        gc.setStroke(Color.YELLOW);
        gc.setLineWidth(2);

        for (Integer polygonIndex : model.getSelectedPolygonIndices()) {
            if (polygonIndex >= 0 && polygonIndex < model.polygons.size()) {
                renderPolygonOutline(model, polygonIndex);
            }
        }

        gc.setLineWidth(1);
    }

    private void renderPolygonOutline(com.cgvsu.model.Model model, int polygonIndex) {
        com.cgvsu.model.Polygon polygon = model.polygons.get(polygonIndex);

        double[] xPoints = new double[polygon.getVertexCount()];
        double[] yPoints = new double[polygon.getVertexCount()];

        for (int i = 0; i < polygon.getVertexCount(); i++) {
            int vertexIndex = polygon.getVertexIndex(i);
            if (vertexIndex >= 0 && vertexIndex < model.vertices.size()) {
                Vector3f vertex = model.vertices.get(vertexIndex);
                xPoints[i] = viewportWidth / 2 + vertex.x;
                yPoints[i] = viewportHeight / 2 - vertex.y;
            }
        }

        gc.strokePolygon(xPoints, yPoints, polygon.getVertexCount());
    }
}