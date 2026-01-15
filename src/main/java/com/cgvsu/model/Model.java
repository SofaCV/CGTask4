package com.cgvsu.model;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;

import java.util.*;

public class Model {
    public ArrayList<Vector3f> vertices = new ArrayList<>();
    public ArrayList<Vector2f> textureVertices = new ArrayList<>();
    public ArrayList<Vector3f> normals = new ArrayList<>();
    public ArrayList<Polygon> polygons = new ArrayList<>();

    private Set<Integer> selectedVertexIndices = new HashSet<>();
    private Set<Integer> selectedPolygonIndices = new HashSet<>();

    public void deleteSelectedVertices() {
        if (selectedVertexIndices.isEmpty()) return;

        List<Integer> sortedIndices = new ArrayList<>(selectedVertexIndices);
        sortedIndices.sort(Collections.reverseOrder());

        for (int vertexIndex : sortedIndices) {
            if (vertexIndex >= 0 && vertexIndex < vertices.size()) {
                vertices.remove(vertexIndex);

                if (vertexIndex < textureVertices.size()) {
                    textureVertices.remove(vertexIndex);
                }
                if (vertexIndex < normals.size()) {
                    normals.remove(vertexIndex);
                }
            }
        }

        updatePolygonIndicesAfterVertexDeletion(sortedIndices);
        selectedVertexIndices.clear();
    }

    private void updatePolygonIndicesAfterVertexDeletion(List<Integer> deletedIndices) {
        for (Polygon polygon : polygons) {
            for (int i = 0; i < polygon.getVertexIndices().size(); i++) {
                int vertexIndex = polygon.getVertexIndices().get(i);
                int adjustment = 0;

                for (int deletedIdx : deletedIndices) {
                    if (vertexIndex > deletedIdx) {
                        adjustment++;
                    }
                }

                polygon.getVertexIndices().set(i, vertexIndex - adjustment);
            }

            if (!polygon.getTextureVertexIndices().isEmpty()) {
                for (int i = 0; i < polygon.getTextureVertexIndices().size(); i++) {
                    int texIndex = polygon.getTextureVertexIndices().get(i);
                    int adjustment = 0;

                    for (int deletedIdx : deletedIndices) {
                        if (texIndex > deletedIdx) {
                            adjustment++;
                        }
                    }

                    polygon.getTextureVertexIndices().set(i, texIndex - adjustment);
                }
            }

            if (!polygon.getNormalIndices().isEmpty()) {
                for (int i = 0; i < polygon.getNormalIndices().size(); i++) {
                    int normIndex = polygon.getNormalIndices().get(i);
                    int adjustment = 0;

                    for (int deletedIdx : deletedIndices) {
                        if (normIndex > deletedIdx) {
                            adjustment++;
                        }
                    }

                    polygon.getNormalIndices().set(i, normIndex - adjustment);
                }
            }
        }

        polygons.removeIf(polygon -> {
            for (int idx : polygon.getVertexIndices()) {
                if (idx < 0 || idx >= vertices.size()) {
                    return true;
                }
            }
            return polygon.getVertexIndices().size() < 3;
        });
    }

    public void deleteSelectedPolygons() {
        if (selectedPolygonIndices.isEmpty()) return;

        List<Integer> sortedIndices = new ArrayList<>(selectedPolygonIndices);
        sortedIndices.sort(Collections.reverseOrder());

        for (int polygonIndex : sortedIndices) {
            if (polygonIndex >= 0 && polygonIndex < polygons.size()) {
                polygons.remove(polygonIndex);
            }
        }

        selectedPolygonIndices.clear();
    }

    public void selectVertex(int index, boolean addToSelection) {
        if (!addToSelection) {
            selectedVertexIndices.clear();
        }
        if (index >= 0 && index < vertices.size()) {
            selectedVertexIndices.add(index);
        }
    }

    public void deselectVertex(int index) {
        selectedVertexIndices.remove(index);
    }

    public void clearVertexSelection() {
        selectedVertexIndices.clear();
    }

    public Set<Integer> getSelectedVertexIndices() {
        return Collections.unmodifiableSet(selectedVertexIndices);
    }

    public boolean isVertexSelected(int index) {
        return selectedVertexIndices.contains(index);
    }

    public void selectPolygon(int index, boolean addToSelection) {
        if (!addToSelection) {
            selectedPolygonIndices.clear();
        }
        if (index >= 0 && index < polygons.size()) {
            selectedPolygonIndices.add(index);
        }
    }

    public void deselectPolygon(int index) {
        selectedPolygonIndices.remove(index);
    }

    public void clearPolygonSelection() {
        selectedPolygonIndices.clear();
    }

    public Set<Integer> getSelectedPolygonIndices() {
        return Collections.unmodifiableSet(selectedPolygonIndices);
    }

    public boolean isPolygonSelected(int index) {
        return selectedPolygonIndices.contains(index);
    }

    public List<Vector3f> getSelectedVertices() {
        List<Vector3f> selected = new ArrayList<>();
        for (int idx : selectedVertexIndices) {
            if (idx >= 0 && idx < vertices.size()) {
                selected.add(vertices.get(idx));
            }
        }
        return selected;
    }

    public List<Polygon> getSelectedPolygons() {
        List<Polygon> selected = new ArrayList<>();
        for (int idx : selectedPolygonIndices) {
            if (idx >= 0 && idx < polygons.size()) {
                selected.add(polygons.get(idx));
            }
        }
        return selected;
    }

    public int getSelectedVertexCount() {
        return selectedVertexIndices.size();
    }

    public int getSelectedPolygonCount() {
        return selectedPolygonIndices.size();
    }

    public List<Vector3f> getVertices() {
        return vertices;
    }

    public List<Vector2f> getTextureVertices() {
        return textureVertices;
    }

    public List<Vector3f> getNormals() {
        return normals;
    }

    public List<Polygon> getPolygons() {
        return polygons;
    }
}