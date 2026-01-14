package com.cgvsu.render_engine;

import com.cgvsu.Math.Matrix.Matrix4;
import com.cgvsu.Math.Vector.Vector3;
import com.cgvsu.Math.Vector.Vector4;

import java.awt.geom.Point2D;

public class GraphicConveyor {

    public static Matrix4 rotateScaleTranslate(Vector3 s, Vector3 angle, Vector3 t) {
        Matrix4 result = scaleMatrix(s);
        result = rotationMatrix(angle).mult(result);
        result = translationMatrix(t).mult(result);
        return result;
    }

    public static Matrix4 lookAt(Vector3 eye, Vector3 target) {

        return lookAt(eye, target, new Vector3(0F, 1.0F, 0F));
    }

    //мировая система координат -> система координат камеры
    public static Matrix4 lookAt(Vector3 eye, Vector3 target, Vector3 up) {

        //получаем оси системы координат камеры и сразу их нормализуем
        Vector3 resultZ = (target.subtract(eye)).normalization();
        Vector3 resultX = (up.vectorMult(resultZ)).normalization();
        Vector3 resultY = (resultX.vectorMult(resultZ)).normalization();

        float[] X = resultX.getVector();
        float[] Y = resultY.getVector();
        float[] Z = resultZ.getVector();
        float[] e = eye.getVector();

        //видовая матрица (view matrix)
        return new Matrix4(
                X[0], X[1], X[2], -e[0] * X[0] - e[1] * X[1] - e[2] * X[2],
                Y[0], Y[1], Y[2], -e[0] * Y[0] - e[1] * Y[1] - e[2] * Y[2],
                Z[0], Z[1], Z[2], -e[0] * Z[0] - e[1] * Z[1] - e[2] * Z[2],
                0, 0, 0, 1
        );
    }

    //система координат камеры -> плоскость проецирования
    public static Matrix4 perspective(
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane) {
        Matrix4 result = new Matrix4();
        float tangentMinusOnDegree = (float) (1.0F / (Math.tan(fov * 0.5F)));
        result.m00 = tangentMinusOnDegree / aspectRatio;
        result.m11 = tangentMinusOnDegree;
        result.m22 = (farPlane + nearPlane) / (farPlane - nearPlane);
        result.m23 = 1.0F;
        result.m32 = 2 * (nearPlane * farPlane) / (nearPlane - farPlane);
        return result;
    }

    //аффинные преобразования

    //матрица масштабирования
    private static Matrix4 scaleMatrix(Vector3 s){
        return new Matrix4(
                s.getX(), 0, 0,0,
                0, s.getY(), 0,0,
                0,0,s.getZ(),0,
                0,0,0,1
        );
    }

    //матрица поворота сразу вокруг всех осей
    private static Matrix4 rotationMatrix(Vector3 angle){
        float cosX = (float) Math.cos(angle.getX());
        float sinX = (float) Math.sin(angle.getX());
        float cosY = (float) Math.cos(angle.getY());
        float sinY = (float) Math.sin(angle.getY());
        float cosZ = (float) Math.cos(angle.getZ());
        float sinZ = (float) Math.sin(angle.getZ());


        return new Matrix4(
                cosZ * cosY, cosZ * sinY * sinX - sinZ * cosX, cosZ * sinY * cosX + sinZ * sinX, 0,
                sinZ * cosY, sinZ * sinY * sinX + cosZ * cosX, sinZ * sinY * cosX - cosZ * sinX, 0,
                -sinY, cosY * sinX, cosY * cosX, 0,
                0,0,0,1
        );
    }

    //матрица переноса
    private static Matrix4 translationMatrix(Vector3 t){
        return new Matrix4(
                1,0,0,t.getX(),
                0,1,0,t.getY(),
                0,0,1,t.getZ(),
                0,0,0,1
        );
    }

    public static Vector3 multiplyMatrix4ByVector3(final Matrix4 matrix, final Vector3 vertex) {
        float[] vector = vertex.getVector();
        Vector4 vector4 = new Vector4(vector[0], vector[1], vector[2], 1);

        Vector4 res = matrix.multiplyByVector(vector4);

        return new Vector3(
                res.getX() / res.getW(),
                res.getY() / res.getW(),
                res.getZ() / res.getW()
        );
    }

    public static Point2D vertexToPoint(final Vector3 vertex, final int width, final int height) {
        return new Point2D(vertex.getX() * width + width / 2.0F, -vertex.getY() * height + height / 2.0F);
    }
}
