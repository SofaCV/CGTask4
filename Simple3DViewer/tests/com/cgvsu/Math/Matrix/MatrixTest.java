package Math.Matrix;

import Math.Vector.Vector3;
import Math.Vector.Vector4;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatrixTest {

    //проверка на то, что функция createNewMatrix возвращает объект нужного класса
    @Test
    void testCreateNewVector() {
        Matrix3 m3 = new Matrix3(
                1, 1,1,
                1,1,1,
                1,1,1
        );
        assertInstanceOf(Matrix3.class, m3.createNewMatrix(new float[][]{
                {1,1,1},
                {1,1,1},
                {1,1,1}}
        ));

        Matrix4 m4 = new Matrix4(
                1, 1,1,1,
                1,1,1,1,
                1,1,1,1,
                1,1,1,1
        );
        assertInstanceOf(Matrix4.class, m4.createNewMatrix(new float[][]{
                {1,1,1,1},
                {1,1,1,1},
                {1,1,1,1},
                {1,1,1,1}}
        ));

        //проверка исключений
        IllegalArgumentException exception1 = assertThrows(
                IllegalArgumentException.class,
                () -> m3.createNewMatrix(new float[][]{{1},{1}})
        );
        assertEquals("Ошибка в количестве аргументов. Трехмерная матрица содержит 9 элементов", exception1.getMessage());

        IllegalArgumentException exception2 = assertThrows(
                IllegalArgumentException.class,
                () ->  m4.createNewMatrix(new float[][]{{1},{1}})
        );
        assertEquals("Ошибка в количестве аргументов. Четырехмерная матрица содержит 16 элементов", exception2.getMessage());
    }

    @Test
    void createVector(){
        Matrix3 m3 = new Matrix3(
                1, 1,1,
                1,1,1,
                1,1,1
        );
        Matrix4 m4 = new Matrix4(
                1, 1,1,1,
                1,1,1,1,
                1,1,1,1,
                1,1,1,1
        );

        assertInstanceOf(Vector3.class, m3.createVector(new float[]{1,1,1}));
        assertInstanceOf(Vector4.class, m4.createVector(new float[]{1,1,1,1}));

        //проверка исключений
        IllegalArgumentException exception1 = assertThrows(
                IllegalArgumentException.class,
                () -> m3.createVector(new float[]{1})
        );
        assertEquals("Ошибка в количестве координат. Трехмерный вектор имеет 3 координаты", exception1.getMessage());

        IllegalArgumentException exception2 = assertThrows(
                IllegalArgumentException.class,
                () ->  m4.createVector(new float[]{1,1})
        );
        assertEquals("Ошибка в количестве координат. Четырехмерный вектор имеет 4 координаты", exception2.getMessage());
    }
}