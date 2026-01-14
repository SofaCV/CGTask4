package Math.Vector;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector2Test {

    @Test
    void getVector() {
        Vector2 vector2 = new Vector2(2.5F,5.6F);
        float[] vector = {2.5F, 5.6F};

        float[] check = vector2.getVector();
        assertEquals(check[0], vector[0], 10e-8);
        assertEquals(check[1], vector[1], 10e-8);
    }

    @Test
    void add() {
        Vector2 vector1 = new Vector2(2.5F,5.6F);
        Vector2 vector2 = new Vector2(2.5F,5.6F);
        float[] vector = {5, 11.2F};

        float[] check = vector1.add(vector2).getVector();
        assertEquals(check[0], vector[0],10e-8);
        assertEquals(check[1], vector[1], 10e-8);

        //проверка исключения
        Vector2 vector3 = null;
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> vector1.add(vector3)
        );
        assertEquals("аргумент не может быть null", exception.getMessage());
    }

    @Test
    void subtract() {
        Vector2 vector1 = new Vector2(5,11.2F);
        Vector2 vector2 = new Vector2(2.5F,5.6F);
        float[] vector = {2.5F, 5.6F};

        float[] check = vector1.subtract(vector2).getVector();
        assertEquals(check[0], vector[0],10e-8);
        assertEquals(check[1], vector[1],10e-8);

        //проверка исключения
        Vector2 vector3 = null;
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> vector1.subtract(vector3)
        );
        assertEquals("аргумент не может быть null", exception.getMessage());
    }

    @Test
    void multiByScalar() {
        Vector2 vector1 = new Vector2(2,3);
        float[] vector = {4, 6};

        float[] check = vector1.multiByScalar(2).getVector();
        assertEquals(check[0], vector[0], 10e-8);
        assertEquals(check[1], vector[1], 10e-8);

        float[] check1 = vector1.multiByScalar(0).getVector();
        float[] vector2 = {0, 0};
        assertEquals(check1[0], vector2[0], 10e-8);
        assertEquals(check1[1], vector2[1], 10e-8);
    }

    @Test
    void divByScalar() {
        Vector2 vector1 = new Vector2(4.2F,8.8F);
        float[]vector = {2.1F, 4.4F};

        float[] check = vector1.divByScalar(2).getVector();
        assertEquals(check[0], vector[0], 10e-8);
        assertEquals(check[1], vector[1], 10e-8);

        //проверка исключений
        ArithmeticException exception1 = assertThrows(
                ArithmeticException.class,
                () -> vector1.divByScalar(0)
        );
        assertEquals("Деление на ноль невозможно", exception1.getMessage());
    }

    @Test
    void vectorLength() {
        Vector2 vector1 = new Vector2(2.5F,3.8F);
        float length = 4.54863F;
        assertEquals(vector1.vectorLength(), length, 0.00001);
    }

    @Test
    void normalization() {
        Vector2 vector1 = new Vector2(3,4);
        float[] normalization = {0.6f, 0.8f};

        float[] check = vector1.normalization().getVector();
        assertEquals(check[0], normalization[0], 0.00001);
        assertEquals(check[1], normalization[1], 0.00001);
    }

    @Test
    void scalarMultiplication() {
        Vector2 vector1 = new Vector2(2,3);
        Vector2 vector2 = new Vector2(3,2);
        float vector = 12;

        float check = vector1.scalarMultiplication(vector2);
        assertEquals(check, vector, 10e-8);

        //проверка исключения
        Vector2 vector3 = null;
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> vector1.scalarMultiplication(vector3)
        );
        assertEquals("аргумент не может быть null", exception.getMessage());
    }
}