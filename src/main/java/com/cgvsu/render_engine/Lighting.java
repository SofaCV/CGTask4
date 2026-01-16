package com.cgvsu.render_engine;

import com.cgvsu.math.Vector3f;

public class Lighting {

    public static float lambert(Vector3f normal, Vector3f lightDir) {
        Vector3f n = normal.normalize();
        Vector3f l = lightDir.normalize();
        return Math.max(0f, Vector3f.dot(n, l));
    }
}
