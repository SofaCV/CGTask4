package com.cgvsu.objreader;

import com.cgvsu.math.Vector3f;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Arrays;

public class ObjReaderTest {

    @Test
    public void testParseVertex01_Success() {
        ArrayList<String> words = new ArrayList<>(Arrays.asList("1.01", "1.02", "1.03"));

        Vector3f result = ObjReader.parseVertex(words, 5);

        Vector3f expected = new Vector3f(1.01f, 1.02f, 1.03f);
        Assertions.assertEquals(expected.getX(), result.getX(), 0.0001f);
        Assertions.assertEquals(expected.getY(), result.getY(), 0.0001f);
        Assertions.assertEquals(expected.getZ(), result.getZ(), 0.0001f);
    }

    @Test
    public void testParseVertex02_WrongValues() {
        ArrayList<String> words = new ArrayList<>(Arrays.asList("1.01", "1.02", "1.03"));
        Vector3f result = ObjReader.parseVertex(words, 5);
        Vector3f expected = new Vector3f(1.01f, 1.02f, 1.10f);
        Assertions.assertNotEquals(expected.getZ(), result.getZ(), 0.0001f);
    }

    @Test
    public void testParseVertex03_InvalidNumbers() {
        ArrayList<String> words = new ArrayList<>(Arrays.asList("ab", "o", "ba"));

        ObjReaderException exception = Assertions.assertThrows(
                ObjReaderException.class,
                () -> ObjReader.parseVertex(words, 10)
        );

        Assertions.assertTrue(exception.getMessage().contains("Error parsing OBJ file on line: 10"));
    }

    @Test
    public void testParseVertex04_TooFewArguments() {
        ArrayList<String> words = new ArrayList<>(Arrays.asList("1.0", "2.0"));

        ObjReaderException exception = Assertions.assertThrows(
                ObjReaderException.class,
                () -> ObjReader.parseVertex(words, 10)
        );

        Assertions.assertTrue(exception.getMessage().contains("Too few vertex arguments"));
    }

    @Test
    public void testParseVertex05_TooManyArguments() {
        ArrayList<String> words = new ArrayList<>(Arrays.asList("1.0", "2.0", "3.0", "4.0"));

        ObjReaderException exception = Assertions.assertThrows(
                ObjReaderException.class,
                () -> ObjReader.parseVertex(words, 10)
        );

        Assertions.assertTrue(exception.getMessage().contains("Too many vertex arguments"));
    }
}