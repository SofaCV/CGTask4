package Math.Vector;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector3Test {
    @Test
    void getVector() {
        Vector3 vector2 = new Vector3(2.5F,5.6F, 6.9F);
        float[] vector = {2.5F, 5.6F, 6.9F};

        float[] check = vector2.getVector();
        assertEquals(check[0], vector[0], 10e-8);
        assertEquals(check[1], vector[1], 10e-8);
        assertEquals(check[2], vector[2], 10e-8);
    }

    @Test
    void add() {
        Vector3 vector1 = new Vector3(2.5F,5.6F, 2);
        Vector3 vector2 = new Vector3(2.5F,5.6F, 2);
        float[] vector = {5, 11.2F,4};

        float[] check = vector1.add(vector2).getVector();
        assertEquals(check[0], vector[0],10e-8);
        assertEquals(check[1], vector[1], 10e-8);
        assertEquals(check[2], vector[2], 10e-8);

        //проверка исключения
        Vector3 vector3 = null;
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> vector1.add(vector3)
        );
        assertEquals("аргумент не может быть null", exception.getMessage());

    }

    @Test
    void subtract() {
        Vector3 vector1 = new Vector3(5,11.2F, 4);
        Vector3 vector2 = new Vector3(2.5F,5.6F, 2);
        float[] vector = {2.5F, 5.6F, 2};

        float[] check = vector1.subtract(vector2).getVector();
        assertEquals(check[0], vector[0],10e-8);
        assertEquals(check[1], vector[1],10e-8);
        assertEquals(check[2], vector[2], 10e-8);

        //проверка исключения
        Vector3 vector3 = null;
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> vector1.subtract(vector3)
        );
        assertEquals("аргумент не может быть null", exception.getMessage());
    }

    @Test
    void multiByScalar() {
        Vector3 vector1 = new Vector3(2,3, 4);
        float[] vector = {4,6, 8};

        float[] check = vector1.multiByScalar(2).getVector();
        assertEquals(check[0], vector[0], 10e-8);
        assertEquals(check[1], vector[1], 10e-8);
        assertEquals(check[2], vector[2], 10e-8);

        float[] check1 = vector1.multiByScalar(0).getVector();
        float[] vector2 = {0,0, 0};
        assertEquals(check1[0], vector2[0], 10e-8);
        assertEquals(check1[1], vector2[1], 10e-8);
        assertEquals(check1[2], vector2[2], 10e-8);
    }

    @Test
    void divByScalar() {
        Vector3 vector1 = new Vector3(4.2F,8.8F, 12.6F);
        float[] vector = {2.1F, 4.4F, 6.3F};

        float[] check = vector1.divByScalar(2).getVector();
        assertEquals(check[0], vector[0], 10e-8);
        assertEquals(check[1], vector[1], 10e-8);
        assertEquals(check[2], vector[2], 10e-8);

        //проверка исключений
        ArithmeticException exception1 = assertThrows(
                ArithmeticException.class,
                () -> vector1.divByScalar(0)
        );
        assertEquals("Деление на ноль невозможно", exception1.getMessage());
    }

    @Test
    void vectorLength() {
        Vector3 vector1 = new Vector3(2.5F,3.8F, 2.4F);
        float length = 5.14296F;
        assertEquals(vector1.vectorLength(), length, 0.00001);
    }

    @Test
    void normalization() {
        Vector3 vector1 = new Vector3(3,4, 1);
        float[] normalization = {0.58834f, 0.78446f, 0.19611F};

        float[] check = vector1.normalization().getVector();
        assertEquals(check[0], normalization[0], 0.00001);
        assertEquals(check[1], normalization[1], 0.00001);
        assertEquals(check[2], normalization[2], 0.00001);
    }

    @Test
    void scalarMultiplication() {
        Vector3 vector1 = new Vector3(2,3,1);
        Vector3 vector2 = new Vector3(3,2,1);
        float vector = 13;

        float check = vector1.scalarMultiplication(vector2);
        assertEquals(check, vector, 10e-8);

        //проверка исключения
        Vector3 vector3 = null;
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> vector1.scalarMultiplication(vector3)
        );
        assertEquals("аргумент не может быть null", exception.getMessage());
    }

    @Test
    void vectorMult(){
        Vector3 vector1 = new Vector3(1,2,3);
        Vector3 vector2 = new Vector3(-1,-2,-3);
        Vector3 vector3 = null;
        float[] res = {0, 0, 0};

        float[] check = vector1.vectorMult(vector2).getVector();
        assertEquals(check[0], res[0], 10e-8);
        assertEquals(check[1], res[1], 10e-8);
        assertEquals(check[2], res[2], 10e-8);

        //проверка исключений
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> vector1.scalarMultiplication(vector3)
        );
        assertEquals("аргумент не может быть null", exception.getMessage());
    }
}