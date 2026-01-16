package com.cgvsu.render_engine;

public class ZBuffer {
    private final float[][] buffer;

    public ZBuffer(int width, int height) {
        buffer = new float[width][height];
        clear();
    }

    public void clear() {
        for (int x = 0; x < buffer.length; x++) {
            for (int y = 0; y < buffer[0].length; y++) {
                buffer[x][y] = Float.POSITIVE_INFINITY;
            }
        }
    }

    public boolean testAndSet(int x, int y, float depth) {
        if (depth < buffer[x][y]) {
            buffer[x][y] = depth;
            return true;
        }
        return false;
    }
}
