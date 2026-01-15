package com.cgvsu.model;

import java.util.*;

public class Polygon {
    private List<Integer> vertexIndices;
    private List<Integer> textureVertexIndices;
    private List<Integer> normalIndices;

    public Polygon() {
        vertexIndices = new ArrayList<>();
        textureVertexIndices = new ArrayList<>();
        normalIndices = new ArrayList<>();
    }

    public Polygon(Polygon other) {
        this.vertexIndices = new ArrayList<>(other.vertexIndices);
        this.textureVertexIndices = new ArrayList<>(other.textureVertexIndices);
        this.normalIndices = new ArrayList<>(other.normalIndices);
    }

    public void addVertexIndex(int vertexIndex) {
        vertexIndices.add(vertexIndex);
    }

    public void addVertexIndices(int... indices) {
        for (int index : indices) {
            vertexIndices.add(index);
        }
    }

    public void setVertexIndices(List<Integer> vertexIndices) {
        this.vertexIndices = new ArrayList<>(vertexIndices);
    }

    public List<Integer> getVertexIndices() {
        return vertexIndices;
    }

    public int getVertexIndex(int position) {
        if (position >= 0 && position < vertexIndices.size()) {
            return vertexIndices.get(position);
        }
        return -1;
    }

    public int getVertexCount() {
        return vertexIndices.size();
    }

    public boolean containsVertexIndex(int vertexIndex) {
        return vertexIndices.contains(vertexIndex);
    }

    public void removeVertexIndex(int position) {
        if (position >= 0 && position < vertexIndices.size()) {
            vertexIndices.remove(position);
        }
    }

    public void updateVertexIndex(int oldIndex, int newIndex) {
        for (int i = 0; i < vertexIndices.size(); i++) {
            if (vertexIndices.get(i) == oldIndex) {
                vertexIndices.set(i, newIndex);
            }
        }
    }

    public void addTextureVertexIndex(int textureVertexIndex) {
        textureVertexIndices.add(textureVertexIndex);
    }

    public void addTextureVertexIndices(int... indices) {
        for (int index : indices) {
            textureVertexIndices.add(index);
        }
    }

    public void setTextureVertexIndices(List<Integer> textureVertexIndices) {
        this.textureVertexIndices = new ArrayList<>(textureVertexIndices);
    }

    public List<Integer> getTextureVertexIndices() {
        return textureVertexIndices;
    }

    public int getTextureVertexIndex(int position) {
        if (position >= 0 && position < textureVertexIndices.size()) {
            return textureVertexIndices.get(position);
        }
        return -1;
    }

    public boolean hasTextureCoordinates() {
        return !textureVertexIndices.isEmpty();
    }

    public void addNormalIndex(int normalIndex) {
        normalIndices.add(normalIndex);
    }

    public void addNormalIndices(int... indices) {
        for (int index : indices) {
            normalIndices.add(index);
        }
    }

    public void setNormalIndices(List<Integer> normalIndices) {
        this.normalIndices = new ArrayList<>(normalIndices);
    }

    public List<Integer> getNormalIndices() {
        return normalIndices;
    }

    public int getNormalIndex(int position) {
        if (position >= 0 && position < normalIndices.size()) {
            return normalIndices.get(position);
        }
        return -1;
    }

    public boolean hasNormals() {
        return !normalIndices.isEmpty();
    }

    public boolean isValid() {
        // Полигон должен иметь хотя бы 3 вершины
        if (vertexIndices.size() < 3) {
            return false;
        }

        for (int index : vertexIndices) {
            if (index < 0) return false;
        }

        if (!textureVertexIndices.isEmpty() && textureVertexIndices.size() != vertexIndices.size()) {
            return false;
        }

        if (!normalIndices.isEmpty() && normalIndices.size() != vertexIndices.size()) {
            return false;
        }

        return true;
    }

    public static Polygon createTriangle(int v1, int v2, int v3) {
        Polygon polygon = new Polygon();
        polygon.addVertexIndices(v1, v2, v3);
        return polygon;
    }

    public static Polygon createQuad(int v1, int v2, int v3, int v4) {
        Polygon polygon = new Polygon();
        polygon.addVertexIndices(v1, v2, v3, v4);
        return polygon;
    }

    public static Polygon createTriangleWithTexture(int v1, int t1, int v2, int t2, int v3, int t3) {
        Polygon polygon = new Polygon();
        polygon.addVertexIndices(v1, v2, v3);
        polygon.addTextureVertexIndices(t1, t2, t3);
        return polygon;
    }

    public static Polygon createTriangleWithNormal(int v1, int n1, int v2, int n2, int v3, int n3) {
        Polygon polygon = new Polygon();
        polygon.addVertexIndices(v1, v2, v3);
        polygon.addNormalIndices(n1, n2, n3);
        return polygon;
    }

    public static Polygon createTriangleWithAll(int v1, int t1, int n1, int v2, int t2, int n2, int v3, int t3, int n3) {
        Polygon polygon = new Polygon();
        polygon.addVertexIndices(v1, v2, v3);
        polygon.addTextureVertexIndices(t1, t2, t3);
        polygon.addNormalIndices(n1, n2, n3);
        return polygon;
    }

    public void triangulate() {
        // Триангуляция полигона (разбиение на треугольники)
        if (vertexIndices.size() <= 3) return;

        List<Polygon> triangles = new ArrayList<>();
        for (int i = 1; i < vertexIndices.size() - 1; i++) {
            Polygon triangle = new Polygon();
            triangle.addVertexIndex(vertexIndices.get(0));
            triangle.addVertexIndex(vertexIndices.get(i));
            triangle.addVertexIndex(vertexIndices.get(i + 1));

            if (hasTextureCoordinates()) {
                triangle.addTextureVertexIndex(textureVertexIndices.get(0));
                triangle.addTextureVertexIndex(textureVertexIndices.get(i));
                triangle.addTextureVertexIndex(textureVertexIndices.get(i + 1));
            }

            if (hasNormals()) {
                triangle.addNormalIndex(normalIndices.get(0));
                triangle.addNormalIndex(normalIndices.get(i));
                triangle.addNormalIndex(normalIndices.get(i + 1));
            }

            triangles.add(triangle);
        }
    }

    public List<Polygon> getTriangles() {
        List<Polygon> triangles = new ArrayList<>();

        if (vertexIndices.size() == 3) {
            triangles.add(this);
        } else if (vertexIndices.size() > 3) {
            for (int i = 1; i < vertexIndices.size() - 1; i++) {
                Polygon triangle = new Polygon();
                triangle.addVertexIndex(vertexIndices.get(0));
                triangle.addVertexIndex(vertexIndices.get(i));
                triangle.addVertexIndex(vertexIndices.get(i + 1));

                if (hasTextureCoordinates()) {
                    triangle.addTextureVertexIndex(textureVertexIndices.get(0));
                    triangle.addTextureVertexIndex(textureVertexIndices.get(i));
                    triangle.addTextureVertexIndex(textureVertexIndices.get(i + 1));
                }

                if (hasNormals()) {
                    triangle.addNormalIndex(normalIndices.get(0));
                    triangle.addNormalIndex(normalIndices.get(i));
                    triangle.addNormalIndex(normalIndices.get(i + 1));
                }

                triangles.add(triangle);
            }
        }

        return triangles;
    }

    public String getType() {
        int vertexCount = vertexIndices.size();
        switch (vertexCount) {
            case 1: return "Point";
            case 2: return "Line";
            case 3: return "Triangle";
            case 4: return "Quad";
            default: return "Polygon (" + vertexCount + " vertices)";
        }
    }

    public boolean isTriangle() {
        return vertexIndices.size() == 3;
    }

    public boolean isQuad() {
        return vertexIndices.size() == 4;
    }

    public boolean isNgon() {
        return vertexIndices.size() > 4;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("f ");

        for (int i = 0; i < vertexIndices.size(); i++) {
            sb.append(vertexIndices.get(i) + 1);

            if (hasTextureCoordinates() || hasNormals()) {
                sb.append("/");

                if (hasTextureCoordinates()) {
                    sb.append(textureVertexIndices.get(i) + 1);
                }

                if (hasNormals()) {
                    sb.append("/");
                    sb.append(normalIndices.get(i) + 1);
                }
            }

            if (i < vertexIndices.size() - 1) {
                sb.append(" ");
            }
        }

        return sb.toString();
    }

    public String toDetailedString() {
        return String.format(
                "Polygon[vertices=%d, hasTex=%b, hasNorm=%b, valid=%b]",
                vertexIndices.size(),
                hasTextureCoordinates(),
                hasNormals(),
                isValid()
        );
    }

    public void shiftIndices(int shift) {
        for (int i = 0; i < vertexIndices.size(); i++) {
            vertexIndices.set(i, vertexIndices.get(i) + shift);
        }

        for (int i = 0; i < textureVertexIndices.size(); i++) {
            textureVertexIndices.set(i, textureVertexIndices.get(i) + shift);
        }

        for (int i = 0; i < normalIndices.size(); i++) {
            normalIndices.set(i, normalIndices.get(i) + shift);
        }
    }

    public void decrementIndices() {
        for (int i = 0; i < vertexIndices.size(); i++) {
            if (vertexIndices.get(i) > 0) {
                vertexIndices.set(i, vertexIndices.get(i) - 1);
            }
        }

        for (int i = 0; i < textureVertexIndices.size(); i++) {
            if (textureVertexIndices.get(i) > 0) {
                textureVertexIndices.set(i, textureVertexIndices.get(i) - 1);
            }
        }

        for (int i = 0; i < normalIndices.size(); i++) {
            if (normalIndices.get(i) > 0) {
                normalIndices.set(i, normalIndices.get(i) - 1);
            }
        }
    }

    public Polygon copy() {
        return new Polygon(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Polygon polygon = (Polygon) o;

        if (!vertexIndices.equals(polygon.vertexIndices)) return false;
        if (!textureVertexIndices.equals(polygon.textureVertexIndices)) return false;
        return normalIndices.equals(polygon.normalIndices);
    }

    @Override
    public int hashCode() {
        int result = vertexIndices.hashCode();
        result = 31 * result + textureVertexIndices.hashCode();
        result = 31 * result + normalIndices.hashCode();
        return result;
    }
}