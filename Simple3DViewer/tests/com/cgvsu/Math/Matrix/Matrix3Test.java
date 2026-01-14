package Math.Matrix;

import Math.Vector.Vector2;
import Math.Vector.Vector3;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Matrix3Test {

    @Test
    void getMatrix() {
        Matrix3 matrix = new Matrix3 (
                1,2,3,
                4,5,6,
                7,8,9
        );
        float[][] check = {{1,2,3}, {4,5,6}, {7,8,9}};

        float[][] res = matrix.getMatrix();
        assertEquals(res[0][0], check[0][0], 10e-8);
        assertEquals(res[0][1], check[0][1], 10e-8);
        assertEquals(res[0][2], check[0][2], 10e-8);
        assertEquals(res[1][0], check[1][0], 10e-8);
        assertEquals(res[1][1], check[1][1], 10e-8);
        assertEquals(res[1][2], check[1][2], 10e-8);
        assertEquals(res[2][0], check[2][0], 10e-8);
        assertEquals(res[2][1], check[2][1], 10e-8);
        assertEquals(res[2][2], check[2][2], 10e-8);
    }

    @Test
    void add() {
        Matrix3 matrix1 = new Matrix3 (
                2,2,2,
                2,2,2,
                2,2,2
        );
        Matrix3 matrix2 = new Matrix3 (
                2.5F,2.5F,2.51F,
                2,0,2,
                2,2,0.5F
        );
        float[][] check = {{4.5F,4.5F,4.51F}, {4,2,4}, {4,4,2.5F}};

        float[][] res = matrix1.add(matrix2).getMatrix();
        assertEquals(res[0][0], check[0][0], 10e-8);
        assertEquals(res[0][1], check[0][1], 10e-8);
        assertEquals(res[0][2], check[0][2], 10e-8);
        assertEquals(res[1][0], check[1][0], 10e-8);
        assertEquals(res[1][1], check[1][1], 10e-8);
        assertEquals(res[1][2], check[1][2], 10e-8);
        assertEquals(res[2][0], check[2][0], 10e-8);
        assertEquals(res[2][1], check[2][1], 10e-8);
        assertEquals(res[2][2], check[2][2], 10e-8);

        //проверка исключения
        Matrix3 matrix3 = null;
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> matrix1.add(matrix3)
        );
        assertEquals("аргумент не может быть null", exception.getMessage());
    }

    @Test
    void subtract() {
        Matrix3 matrix1 = new Matrix3 (
                2,2,2,
                2,2,2,
                2,2,2
        );
        Matrix3 matrix2 = new Matrix3 (
                2.5F,2.5F,2F,
                -3.5F,0,2,
                2,2,0.5F
        );
        float[][] check = {{-0.5F,-0.5F,0}, {5.5F,2,0}, {0,0,1.5F}};

        float[][] res = matrix1.subtract(matrix2).getMatrix();
        assertEquals(res[0][0], check[0][0], 10e-8);
        assertEquals(res[0][1], check[0][1], 10e-8);
        assertEquals(res[0][2], check[0][2], 10e-8);
        assertEquals(res[1][0], check[1][0], 10e-8);
        assertEquals(res[1][1], check[1][1], 10e-8);
        assertEquals(res[1][2], check[1][2], 10e-8);
        assertEquals(res[2][0], check[2][0], 10e-8);
        assertEquals(res[2][1], check[2][1], 10e-8);
        assertEquals(res[2][2], check[2][2], 10e-8);

        //проверка исключения
        Matrix3 matrix3 = null;
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> matrix2.add(matrix3)
        );
        assertEquals("аргумент не может быть null", exception.getMessage());
    }

    @Test
    void multiByScalar() {
        Matrix3 matrix = new Matrix3 (
                2,2,2,
                2,2,2,
                2,2,2
        );
        float scalar = 0.5F;
        float[][] check = {{1,1,1}, {1,1,1}, {1,1,1}};

        float[][] res = matrix.multiByScalar(scalar).getMatrix();
        assertEquals(res[0][0], check[0][0], 10e-8);
        assertEquals(res[0][1], check[0][1], 10e-8);
        assertEquals(res[0][2], check[0][2], 10e-8);
        assertEquals(res[1][0], check[1][0], 10e-8);
        assertEquals(res[1][1], check[1][1], 10e-8);
        assertEquals(res[1][2], check[1][2], 10e-8);
        assertEquals(res[2][0], check[2][0], 10e-8);
        assertEquals(res[2][1], check[2][1], 10e-8);
        assertEquals(res[2][2], check[2][2], 10e-8);
    }

    @Test
    void divByScalar() {
        Matrix3 matrix = new Matrix3 (
                2,2,2,
                2,2,2,
                2,2,2
        );
        float scalar = 0.5F;
        float[][] check = {{4,4,4}, {4,4,4}, {4,4,4}};

        float[][] res = matrix.divByScalar(scalar).getMatrix();
        assertEquals(res[0][0], check[0][0], 10e-8);
        assertEquals(res[0][1], check[0][1], 10e-8);
        assertEquals(res[0][2], check[0][2], 10e-8);
        assertEquals(res[1][0], check[1][0], 10e-8);
        assertEquals(res[1][1], check[1][1], 10e-8);
        assertEquals(res[1][2], check[1][2], 10e-8);
        assertEquals(res[2][0], check[2][0], 10e-8);
        assertEquals(res[2][1], check[2][1], 10e-8);
        assertEquals(res[2][2], check[2][2], 10e-8);

        //проверка исключения
        ArithmeticException exception2 = assertThrows(
                ArithmeticException.class,
                () -> matrix.divByScalar(0)
        );
        assertEquals("Деление на ноль невозможно", exception2.getMessage());
    }

    @Test
    void transposition() {
        Matrix3 matrix = new Matrix3(
                1,2,3,
                4,5,6,
                7,8,9
        );
        float[][] check = {{1,4,7}, {2,5,8}, {3,6,9}};

        float[][] res = matrix.transposition().getMatrix();
        assertEquals(res[0][0], check[0][0], 10e-8);
        assertEquals(res[0][1], check[0][1], 10e-8);
        assertEquals(res[0][2], check[0][2], 10e-8);
        assertEquals(res[1][0], check[1][0], 10e-8);
        assertEquals(res[1][1], check[1][1], 10e-8);
        assertEquals(res[1][2], check[1][2], 10e-8);
        assertEquals(res[2][0], check[2][0], 10e-8);
        assertEquals(res[2][1], check[2][1], 10e-8);
        assertEquals(res[2][2], check[2][2], 10e-8);
    }

    @Test
    void multiplication() {
        Matrix3 matrix1 = new Matrix3(
                1,2,4,
                1,2,4,
                1,2,4
        );
        Matrix3 matrix2 = new Matrix3(
                1,2,4,
                1,2,4,
                0,-1,0.5F
        );
        float[][] check = {{3,2,14}, {3,2,14}, {3,2,14}};

        float[][] res = matrix1.mult(matrix2).getMatrix();
        assertEquals(res[0][0], check[0][0], 10e-8);
        assertEquals(res[0][1], check[0][1], 10e-8);
        assertEquals(res[0][2], check[0][2], 10e-8);
        assertEquals(res[1][0], check[1][0], 10e-8);
        assertEquals(res[1][1], check[1][1], 10e-8);
        assertEquals(res[1][2], check[1][2], 10e-8);
        assertEquals(res[2][0], check[2][0], 10e-8);
        assertEquals(res[2][1], check[2][1], 10e-8);
        assertEquals(res[2][2], check[2][2], 10e-8);

        //проверка исключения
        Matrix3 matrix3 = null;
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> matrix2.add(matrix3)
        );
        assertEquals("аргумент не может быть null", exception.getMessage());
    }

    @Test
    void identityMatrix(){
        float[][] check = {
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1}
        };

        float[][] res = Matrix3.identityMatrix().getMatrix();
        assertEquals(res[0][0], check[0][0], 10e-8);
        assertEquals(res[0][1], check[0][1], 10e-8);
        assertEquals(res[0][2], check[0][2], 10e-8);
        assertEquals(res[1][0], check[1][0], 10e-8);
        assertEquals(res[1][1], check[1][1], 10e-8);
        assertEquals(res[1][2], check[1][2], 10e-8);
        assertEquals(res[2][0], check[2][0], 10e-8);
        assertEquals(res[2][1], check[2][1], 10e-8);
        assertEquals(res[2][2], check[2][2], 10e-8);
    }

    @Test
    void zeroMatrix(){
        float[][] check = {
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };

        float[][] res = Matrix3.zeroMatrix().getMatrix();
        assertEquals(res[0][0], check[0][0], 10e-8);
        assertEquals(res[0][1], check[0][1], 10e-8);
        assertEquals(res[0][2], check[0][2], 10e-8);
        assertEquals(res[1][0], check[1][0], 10e-8);
        assertEquals(res[1][1], check[1][1], 10e-8);
        assertEquals(res[1][2], check[1][2], 10e-8);
        assertEquals(res[2][0], check[2][0], 10e-8);
        assertEquals(res[2][1], check[2][1], 10e-8);
        assertEquals(res[2][2], check[2][2], 10e-8);
    }

    @Test
    void multiplyByVector(){
        Matrix3 matrix = new Matrix3(
                1,2,3,
                4,5,6,
                7,8,9
        );
        Vector3 vector = new Vector3(1,2,3);
        float[] check = {14, 32, 50};

        float[] res = matrix.multiplyByVector(vector).getVector();
        assertEquals(res[0], check[0], 10e-8);
        assertEquals(res[1], check[1], 10e-8);
        assertEquals(res[2], check[2], 10e-8);

        //проверка исключения
        Vector3 vector1 = null;
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