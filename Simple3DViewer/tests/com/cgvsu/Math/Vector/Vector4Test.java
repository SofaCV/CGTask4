package Math.Vector;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector4Test {
    @Test
    void getVector() {
        Vector4 vector2 = new Vector4(2.5F,5.6F, 6.9F, 1);
        float[] vector = {2.5F, 5.6F, 6.9F, 1};

        float[] check = vector2.getVector();
        assertEquals(check[0], vector[0], 10e-8);
        assertEquals(check[1], vector[1],10e-8);
        assertEquals(check[2], vector[2], 10e-8);
        assertEquals(check[3], vector[3], 10e-8);
    }

    @Test
    void add() {
        Vector4 vector1 = new Vector4(2.5F,5.6F, 2, 0.5F);
        Vector4 vector2 = new Vector4(2.5F,5.6F, 2, 0.5F);
        float[] vector = {5, 11.2F, 4, 1};

        float[] check = vector1.add(vector2).getVector();
        assertEquals(check[0], vector[0],10e-8);
        assertEquals(check[1], vector[1], 10e-8);
        assertEquals(check[2], vector[2], 10e-8);
        assertEquals(check[3], vector[3], 10e-8);

        //проверка исключения
        Vector4 vector3 = null;
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> vector2.add(vector3)
        );
        assertEquals("аргумент не может быть null", exception.getMessage());
    }

    @Test
    void subtract() {
        Vector4 vector1 = new Vector4(5,11.2F, 4, 1);
        Vector4 vector2 = new Vector4(2.5F,5.6F, 2,1);
        float[] vector = {2.5F, 5.6F, 2, 0};

        float[] check = vector1.subtract(vector2).getVector();
        assertEquals(check[0], vector[0],10e-8);
        assertEquals(check[1], vector[1],10e-8);
        assertEquals(check[2], vector[2], 10e-8);
        assertEquals(check[3], vector[3], 10e-8);

        //проверка исключения
        Vector4 vector3 = null;
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> vector2.subtract(vector3)
        );
        assertEquals("аргумент не может быть null", exception.getMessage());
    }

    @Test
    void multiByScalar() {
        Vector4 vector1 = new Vector4(2,3, 4, 1);
        float[] vector = {4, 6, 8, 2};

        float[] check = vector1.multiByScalar(2).getVector();
        assertEquals(check[0], vector[0], 10e-8);
        assertEquals(check[1], vector[1], 10e-8);
        assertEquals(check[2], vector[2], 10e-8);
        assertEquals(check[3], vector[3], 10e-8);

        float[] check1 = vector1.multiByScalar(0).getVector();
        float[] vector2 = {0, 0, 0, 0};
        assertEquals(check1[0], vector2[0], 10e-8);
        assertEquals(check1[1], vector2[1], 10e-8);
        assertEquals(check1[2], vector2[2], 10e-8);
        assertEquals(check1[3], vector2[3], 10e-8);
    }

    @Test
    void divByScalar() {
        Vector4 vector1 = new Vector4(4.2F,8.8F, 12.6F, 16.4F);
        float[] vector = {2.1F, 4.4F, 6.3F, 8.2F};

        float[] check = vector1.divByScalar(2).getVector();
        assertEquals(check[0], vector[0], 10e-8);
        assertEquals(check[1], vector[1], 10e-8);
        assertEquals(check[2], vector[2], 10e-8);
        assertEquals(check[3], vector[3], 10e-8);

        //проверка исключений
        ArithmeticException exception1 = assertThrows(
                ArithmeticException.class,
                () -> vector1.divByScalar(0)
        );
        assertEquals("Деление на ноль невозможно", exception1.getMessage());
    }

    @Test
    void vectorLength() {
        Vector4 vector1 = new Vector4(2.5F,3.8F, 2.4F, 6);
        float length = 7.90253F;
        assertEquals(vector1.vectorLength(), length, 0.00001);
    }

    @Test
    void normalization() {
        Vector4 vector1 = new Vector4(3,4, 1, 4.5F);
        float[] normalization = {0.44112f, 0.58817f, 0.14704F, 0.66169F};

        float[] check = vector1.normalization().getVector();
        assertEquals(check[0], normalization[0], 0.00001);
        assertEquals(check[1], normalization[1], 0.00001);
        assertEquals(check[2], normalization[2], 0.00001);
        assertEquals(check[3], normalization[3], 0.00001);
    }

    @Test
    void scalarMultiplication() {
        Vector4 vector1 = new Vector4(2,3,1, 7.5F);
        Vector4 vector2 = new Vector4(3,2,1, 2);
        float vector = 28;

        float check = vector1.scalarMultiplication(vector2);
        assertEquals(check, vector, 10e-8);

        //проверка исключения
        Vector4 vector3 = null;
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> vector2.scalarMultiplication(vector3)
        );
        assertEquals("аргумент не может быть null", exception.getMessage());
    }
}