package com.cgvsu.math;

public class Vector2f {
    public float x;
    public float y;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f(Vector2f other) {
        this.x = other.x;
        this.y = other.y;
    }

    public Vector2f add(Vector2f other) {
        return new Vector2f(this.x + other.x, this.y + other.y);
    }

    public Vector2f subtract(Vector2f other) {
        return new Vector2f(this.x - other.x, this.y - other.y);
    }

    public Vector2f multiply(float scalar) {
        return new Vector2f(this.x * scalar, this.y * scalar);
    }

    public Vector2f divide(float scalar) {
        if (scalar == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return new Vector2f(this.x / scalar, this.y / scalar);
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public Vector2f normalize() {
        float len = length();
        if (len == 0) {
            return new Vector2f(0, 0);
        }
        return new Vector2f(x / len, y / len);
    }

    public static float dot(Vector2f a, Vector2f b) {
        return a.x * b.x + a.y * b.y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void addInPlace(Vector2f other) {
        this.x += other.x;
        this.y += other.y;
    }

    public void subtractInPlace(Vector2f other) {
        this.x -= other.x;
        this.y -= other.y;
    }

    public void multiplyInPlace(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
    }

    @Override
    public String toString() {
        return "Vector2f{" + "x=" + x + ", y=" + y + '}';
    }
}