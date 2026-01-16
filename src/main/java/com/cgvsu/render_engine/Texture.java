package com.cgvsu.render_engine;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Texture {
    private final Image image;

    public Texture(Image image) {
        this.image = image;
    }

    public Color sample(float u, float v) {
        int x = (int) (u * (image.getWidth() - 1));
        int y = (int) ((1 - v) * (image.getHeight() - 1));
        return image.getPixelReader().getColor(x, y);
    }
}
