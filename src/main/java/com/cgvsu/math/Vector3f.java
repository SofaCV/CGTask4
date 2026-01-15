package com.cgvsu.math;

public class Vector3f {
    public float x;
    public float y;
    public float z;

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f(Vector3f other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    public Vector3f add(Vector3f other) {
        return new Vector3f(
                this.x + other.x,
                this.y + other.y,
                this.z + other.z
        );
    }

    public Vector3f subtract(Vector3f other) {
        return new Vector3f(
                this.x - other.x,
                this.y - other.y,
                this.z - other.z
        );
    }

    public Vector3f multiply(float scalar) {
        return new Vector3f(
                this.x * scalar,
                this.y * scalar,
                this.z * scalar
        );
    }

    public Vector3f divide(float scalar) {
        if (scalar == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return new Vector3f(
                this.x / scalar,
                this.y / scalar,
                this.z / scalar
        );
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3f normalize() {
        float len = length();
        if (len == 0) {
            return new Vector3f(0, 0, 0);
        }
        return new Vector3f(
                x / len,
                y / len,
                z / len
        );
    }

    public static float dot(Vector3f a, Vector3f b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    public static Vector3f cross(Vector3f a, Vector3f b) {
        return new Vector3f(
                a.y * b.z - a.z * b.y,
                a.z * b.x - a.x * b.z,
                a.x * b.y - a.y * b.x
        );
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void addInPlace(Vector3f other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
    }

    public void subtractInPlace(Vector3f other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
    }

    public void multiplyInPlace(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
    }

    public void normalizeInPlace() {
        float len = length();
        if (len > 0) {
            this.x /= len;
            this.y /= len;
            this.z /= len;
        }
    }

    @Override
    public String toString() {
        return "Vector3f{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}