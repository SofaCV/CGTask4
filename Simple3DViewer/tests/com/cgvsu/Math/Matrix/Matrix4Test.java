package Math.Matrix;

import Math.Vector.Vector2;
import Math.Vector.Vector3;
import Math.Vector.Vector4;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Matrix4Test {

    @Test
    void getMatrix() {
        Matrix4 matrix = new Matrix4 (
                1,2,3, 4,
                5,6,7, 8,
                9,10,11, 12,
                13, 14, 15, 16
        );
        float[][] check = {{1,2,3,4}, {5,6,7,8}, {9,10,11,12}, {13,14,15,16}};

        float[][] res = matrix.getMatrix();
        assertEquals(res[0][0], check[0][0], 0.0001);
        assertEquals(res[0][1], check[0][1], 0.0001);
        assertEquals(res[0][2], check[0][2], 0.0001);
        assertEquals(res[0][3], check[0][3], 0.0001);
        assertEquals(res[1][0], check[1][0], 10e-8);
        assertEquals(res[1][1], check[1][1], 10e-8);
        assertEquals(res[1][2], check[1][2], 10e-8);
        assertEquals(res[1][3], check[1][3], 10e-8);
        assertEquals(res[2][0], check[2][0], 10e-8);
        assertEquals(res[2][1], check[2][1], 10e-8);
        assertEquals(res[2][2], check[2][2], 10e-8);
        assertEquals(res[2][3], check[2][3], 10e-8);
        assertEquals(res[3][0], check[3][0], 10e-8);
        assertEquals(res[3][1], check[3][1], 10e-8);
        assertEquals(res[3][2], check[3][2], 10e-8);
        assertEquals(res[3][3], check[3][3], 10e-8);
    }

    @Test
    void add() {
        Matrix4 matrix1 = new Matrix4 (
                2,2,2, 2,
                2,2,2, 2,
                2,2,2, 2,
                2, 2, 2, 2
        );
        Matrix4 matrix2 = new Matrix4 (
                2.5F,2.5F,2.51F, 1.472F,
                2,0,2, 6,
                2,-2,0.5F,6.00006F,
                2,1,1,1.5F
        );
        float[][] check = {{4.5F,4.5F,4.51F, 3.472F}, {4,2,4,8}, {4,0,2.5F, 8.00006F}, {4,3,3,3.5F}};

        float[][] res = matrix1.add(matrix2).getMatrix();
        assertEquals(res[0][0], check[0][0], 0.0001);
        assertEquals(res[0][1], check[0][1], 0.0001);
        assertEquals(res[0][2], check[0][2], 0.0001);
        assertEquals(res[0][3], check[0][3], 0.0001);
        assertEquals(res[1][0], check[1][0], 10e-8);
        assertEquals(res[1][1], check[1][1], 10e-8);
        assertEquals(res[1][2], check[1][2], 10e-8);
        assertEquals(res[1][3], check[1][3], 10e-8);
        assertEquals(res[2][0], check[2][0], 10e-8);
        assertEquals(res[2][1], check[2][1], 10e-8);
        assertEquals(res[2][2], check[2][2], 10e-8);
        assertEquals(res[2][3], check[2][3], 10e-8);
        assertEquals(res[3][0], check[3][0], 10e-8);
        assertEquals(res[3][1], check[3][1], 10e-8);
        assertEquals(res[3][2], check[3][2], 10e-8);
        assertEquals(res[3][3], check[3][3], 10e-8);

        //проверка исключения
        Matrix4 matrix3 = null;
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> matrix2.add(matrix3)
        );
        assertEquals("аргумент не может быть null", exception.getMessage());
    }

    @Test
    void subtract() {
        Matrix4 matrix1 = new Matrix4 (
                2,2,2, 2,
                2,2,2, 2,
                2,2,2, 2,
                2, 2, 2, 2
        );
        Matrix4 matrix2 = new Matrix4 (
                2.5F,2.5F,2, 1.472F,
                2,0,2, 6,
                2,-2,0.5F,6.00006F,
                2,1,1,1.5F
        );
        float[][] check = {{-0.5F,-0.5F,0, 0.528F}, {0,2,0,-4}, {0,4,1.5F, -4.00006F}, {0,1,1,0.5F}};

        float[][] res = matrix1.subtract(matrix2).getMatrix();
        assertEquals(res[0][0], check[0][0], 10e-8);
        assertEquals(res[0][1], check[0][1], 10e-8);
        assertEquals(res[0][2], check[0][2], 10e-8);
        assertEquals(res[0][3], check[0][3], 10e-8);
        assertEquals(res[1][0], check[1][0], 10e-8);
        assertEquals(res[1][1], check[1][1], 10e-8);
        assertEquals(res[1][2], check[1][2], 10e-8);
        assertEquals(res[1][3], check[1][3], 10e-8);
        assertEquals(res[2][0], check[2][0], 10e-8);
        assertEquals(res[2][1], check[2][1], 10e-8);
        assertEquals(res[2][2], check[2][2], 10e-8);
        assertEquals(res[2][3], check[2][3], 10e-8);
        assertEquals(res[3][0], check[3][0], 10e-8);
        assertEquals(res[3][1], check[3][1], 10e-8);
        assertEquals(res[3][2], check[3][2], 10e-8);
        assertEquals(res[3][3], check[3][3], 10e-8);

        //проверка исключения
        Matrix4 matrix3 = null;
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> matrix1.add(matrix3)
        );
        assertEquals("аргумент не может быть null", exception.getMessage());
    }

    @Test
    void multiByScalar() {
        Matrix4 matrix = new Matrix4 (
                2,2,2,2,
                2,2,2,2,
                2,2,2,2,
                2,2,2,2
        );
        float scalar = 0.5F;
        float[][] check = {{1,1,1,1}, {1,1,1,1}, {1,1,1,1}, {1,1,1,1}};

        float[][] res = matrix.multiByScalar(scalar).getMatrix();
        assertEquals(res[0][0], check[0][0], 10e-8);
        assertEquals(res[0][1], check[0][1], 10e-8);
        assertEquals(res[0][2], check[0][2], 10e-8);
        assertEquals(res[0][3], check[0][3], 10e-8);
        assertEquals(res[1][0], check[1][0], 10e-8);
        assertEquals(res[1][1], check[1][1], 10e-8);
        assertEquals(res[1][2], check[1][2], 10e-8);
        assertEquals(res[1][3], check[1][3], 10e-8);
        assertEquals(res[2][0], check[2][0], 10e-8);
        assertEquals(res[2][1], check[2][1], 10e-8);
        assertEquals(res[2][2], check[2][2], 10e-8);
        assertEquals(res[2][3], check[2][3], 10e-8);
        assertEquals(res[3][0], check[3][0], 10e-8);
        assertEquals(res[3][1], check[3][1], 10e-8);
        assertEquals(res[3][2], check[3][2], 10e-8);
        assertEquals(res[3][3], check[3][3], 10e-8);
    }

    @Test
    void divByScalar() {
        Matrix4 matrix = new Matrix4 (
                2,2,2, 2,
                2,2,2, 2,
                2,2,2, 2,
                2,2,2,2
        );
        float scalar = 0.5F;
        float[][] check = {{4,4,4,4}, {4,4,4,4}, {4,4,4,4}, {4,4,4,4}};

        float[][] res = matrix.divByScalar(scalar).getMatrix();
        assertEquals(res[0][0], check[0][0], 10e-8);
        assertEquals(res[0][1], check[0][1], 10e-8);
        assertEquals(res[0][2], check[0][2], 10e-8);
        assertEquals(res[0][3], check[0][3], 10e-8);
        assertEquals(res[1][0], check[1][0], 10e-8);
        assertEquals(res[1][1], check[1][1], 10e-8);
        assertEquals(res[1][2], check[1][2], 10e-8);
        assertEquals(res[1][3], check[1][3], 10e-8);
        assertEquals(res[2][0], check[2][0], 10e-8);
        assertEquals(res[2][1], check[2][1], 10e-8);
        assertEquals(res[2][2], check[2][2], 10e-8);
        assertEquals(res[2][3], check[2][3], 10e-8);
        assertEquals(res[3][0], check[3][0], 10e-8);
        assertEquals(res[3][1], check[3][1], 10e-8);
        assertEquals(res[3][2], check[3][2], 10e-8);
        assertEquals(res[3][3], check[3][3], 10e-8);

        //проверка исключения
        ArithmeticException exception2 = assertThrows(
                ArithmeticException.class,
                () -> matrix.divByScalar(0)
        );
        assertEquals("Деление на ноль невозможно", exception2.getMessage());
    }

    @Test
    void transposition() {
        Matrix4 matrix = new Matrix4(
                1,2,3, 6,
                4,5,6, 9,
                7,8,9,12,
                3,5,7,8
        );
        float[][] check = {{1,4,7,3}, {2,5,8,5},{3,6,9,7}, {6,9,12,8}};

        float[][] res = matrix.transposition().getMatrix();
        assertEquals(res[0][0], check[0][0], 10e-8);
        assertEquals(res[0][1], check[0][1], 10e-8);
        assertEquals(res[0][2], check[0][2], 10e-8);
        assertEquals(res[0][3], check[0][3], 10e-8);
        assertEquals(res[1][0], check[1][0], 10e-8);
        assertEquals(res[1][1], check[1][1], 10e-8);
        assertEquals(res[1][2], check[1][2], 10e-8);
        assertEquals(res[1][3], check[1][3], 10e-8);
        assertEquals(res[2][0], check[2][0], 10e-8);
        assertEquals(res[2][1], check[2][1], 10e-8);
        assertEquals(res[2][2], check[2][2], 10e-8);
        assertEquals(res[2][3], check[2][3], 10e-8);
        assertEquals(res[3][0], check[3][0], 10e-8);
        assertEquals(res[3][1], check[3][1], 10e-8);
        assertEquals(res[3][2], check[3][2], 10e-8);
        assertEquals(res[3][3], check[3][3], 10e-8);
    }

    @Test
    void identityMatrix(){
        float[][] check = {
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 1, 1, 1}
        };

        float[][] res = Matrix4.identityMatrix().getMatrix();
        assertEquals(res[0][0], check[0][0], 10e-8);
        assertEquals(res[0][1], check[0][1], 10e-8);
        assertEquals(res[0][2], check[0][2], 10e-8);
        assertEquals(res[0][3], check[0][3], 10e-8);
        assertEquals(res[1][0], check[1][0], 10e-8);
        assertEquals(res[1][1], check[1][1], 10e-8);
        assertEquals(res[1][2], check[1][2], 10e-8);
        assertEquals(res[1][3], check[1][3], 10e-8);
        assertEquals(res[2][0], check[2][0], 10e-8);
        assertEquals(res[2][1], check[2][1], 10e-8);
        assertEquals(res[2][2], check[2][2], 10e-8);
        assertEquals(res[2][3], check[2][3], 10e-8);
        assertEquals(res[3][0], check[3][0], 10e-8);
        assertEquals(res[3][1], check[3][1], 10e-8);
        assertEquals(res[3][2], check[3][2], 10e-8);
        assertEquals(res[3][3], check[3][3], 10e-8);
    }

    @Test
    void zeroMatrix(){
        float[][] check = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        float[][] res = Matrix4.zeroMatrix().getMatrix();
        assertEquals(res[0][0], check[0][0], 10e-8);
        assertEquals(res[0][1], check[0][1], 10e-8);
        assertEquals(res[0][2], check[0][2], 10e-8);
        assertEquals(res[0][3], check[0][3], 10e-8);
        assertEquals(res[1][0], check[1][0], 10e-8);
        assertEquals(res[1][1], check[1][1], 10e-8);
        assertEquals(res[1][2], check[1][2], 10e-8);
        assertEquals(res[1][3], check[1][3], 10e-8);
        assertEquals(res[2][0], check[2][0], 10e-8);
        assertEquals(res[2][1], check[2][1], 10e-8);
        assertEquals(res[2][2], check[2][2], 10e-8);
        assertEquals(res[2][3], check[2][3], 10e-8);
        assertEquals(res[3][0], check[3][0], 10e-8);
        assertEquals(res[3][1], check[3][1], 10e-8);
        assertEquals(res[3][2], check[3][2], 10e-8);
        assertEquals(res[3][3], check[3][3], 10e-8);
    }
    @Test
    void multiplyByVector(){
        Matrix4 matrix = new Matrix4(
                1,2,3,-1,
                4,5,6,0,
                7,0,9,1.5F,
                1,2,4,5
        );
        Vector4 vector = new Vector4(1,2,3,-5);
        float[] check = {19, 32, 26.5F, -8};

        float[] res = matrix.multiplyByVector(vector).getVector();
        assertEquals(res[0], check[0], 10e-8);
        assertEquals(res[1], check[1], 10e-8);
        assertEquals(res[2], check[2], 10e-8);
        assertEquals(res[3], check[3], 10e-8);

        //проверка исключения
        Vector4 vector1 = null;
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> matrix.multiplyByVector(vector1)
        );
        assertEquals("Вектор не может быть null", exception.getMessage());

        Vector2 vector2 = new Vector2(1,2);
        int size = matrix.getMatrix().length;
        int length = vector2.getVector().length;
        IllegalArgumentException exception1 = assertThrows(
                IllegalArgumentException.class,
                () -> matrix.multiplyByVector(vector2)
        );
        String expectedMessage = String.format(
                "Несовместимые размеры: матрица %dx%d, вектор длины %d",
                size, size, length
        );
        assertEquals(expectedMessage, exception1.getMessage());
    }
}