package Math.Vector;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VectorTest {
    //проверка на то, что функция createNewVector возвращает объект нужного класса
    @Test
    void testCreateNewVector() {
        Vector2 v2 = new Vector2(1, 2);
        assertInstanceOf(Vector2.class, v2.createNewVector(new float[]{3, 4}));

        Vector3 v3 = new Vector3(1, 2, 3);
        assertInstanceOf(Vector3.class, v3.createNewVector(new float[]{4, 5, 6}));

        Vector4 v4 = new Vector4(1, 2, 3, 4);
        assertInstanceOf(Vector4.class, v4.createNewVector(new float[]{5, 6, 7, 8}));

        //проверка исключений
        IllegalArgumentException exception1 = assertThrows(
                IllegalArgumentException.class,
                () -> v2.createNewVector(new float[]{1})
        );
        assertEquals("Ошибка в количестве координат. Двумерный вектор имеет 2 координаты", exception1.getMessage());

        IllegalArgumentException exception2 = assertThrows(
                IllegalArgumentException.class,
                () ->  v3.createNewVector(new float[]{1,1})
        );
        assertEquals("Ошибка в количестве координат. Трехмерный вектор имеет 3 координаты", exception2.getMessage());

        IllegalArgumentException exception3 = assertThrows(
                IllegalArgumentException.class,
                () ->  v4.createNewVector(new float[]{1,1})
        );
        assertEquals("Ошибка в количестве координат. Четырехмерный вектор имеет 4 координаты", exception3.getMessage());
    }
}