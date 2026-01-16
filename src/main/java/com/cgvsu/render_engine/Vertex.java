package com.cgvsu.render_engine;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;

public class Vertex {
    public Vector3f position;
    public Vector3f normal;
    public Vector2f texCoord;

    public Vertex(Vector3f position, Vector3f normal, Vector2f texCoord) {
        this.position = position;
        this.normal = normal;
        this.texCoord = texCoord;
    }
}
