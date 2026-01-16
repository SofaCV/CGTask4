package com.cgvsu.render_engine;

import com.cgvsu.model.Polygon;
import java.util.ArrayList;
import java.util.List;

public class Triangulator {

    public static List<Polygon> triangulate(Polygon polygon) {
        List<Polygon> triangles = new ArrayList<>();
        List<Integer> v = polygon.getVertexIndices();
        if (v.size() < 3) return triangles;

        for (int i = 1; i < v.size() - 1; i++) {
            List<Integer> tri = new ArrayList<>();
            tri.add(v.get(0));
            tri.add(v.get(i));
            tri.add(v.get(i + 1));
            Polygon p = new Polygon();
            p.setVertexIndices(tri);
            triangles.add(p);
        }
        return triangles;
    }
}
