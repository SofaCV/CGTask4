package com.cgvsu.model;
import com.cgvsu.Math.Vector.Vector2;
import com.cgvsu.Math.Vector.Vector3;

import java.util.*;

public class Model {

    public ArrayList<Vector3> vertices = new ArrayList<Vector3>();
    public ArrayList<Vector2> textureVertices = new ArrayList<Vector2>();
    public ArrayList<Vector3> normals = new ArrayList<Vector3>();
    public ArrayList<Polygon> polygons = new ArrayList<Polygon>();

    public ArrayList<Vector3> getVertices() {
        return vertices;
    }

    public ArrayList<Vector2> getTextureVertices() {
        return textureVertices;
    }

    public ArrayList<Vector3> getNormals() {
        return normals;
    }

    public ArrayList<Polygon> getPolygons() {
        return polygons;
    }
}
