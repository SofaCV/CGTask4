package com.cgvsu.objreader;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ObjReader {

    private static final String OBJ_VERTEX_TOKEN = "v";
    private static final String OBJ_TEXTURE_TOKEN = "vt";
    private static final String OBJ_NORMAL_TOKEN = "vn";
    private static final String OBJ_FACE_TOKEN = "f";

    public static Model read(String fileContent) {
        Model result = new Model();

        int lineInd = 0;
        Scanner scanner = new Scanner(fileContent);
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine().trim();

            if (line.isEmpty() || line.startsWith("#")) {
                lineInd++;
                continue;
            }

            ArrayList<String> wordsInLine = new ArrayList<>(Arrays.asList(line.split("\\s+")));
            if (wordsInLine.isEmpty() || wordsInLine.get(0).isEmpty()) {
                lineInd++;
                continue;
            }

            final String token = wordsInLine.get(0);
            wordsInLine.remove(0);

            lineInd++;
            try {
                switch (token) {
                    case OBJ_VERTEX_TOKEN:
                        result.vertices.add(parseVertex(wordsInLine, lineInd));
                        break;
                    case OBJ_TEXTURE_TOKEN:
                        result.textureVertices.add(parseTextureVertex(wordsInLine, lineInd));
                        break;
                    case OBJ_NORMAL_TOKEN:
                        result.normals.add(parseNormal(wordsInLine, lineInd));
                        break;
                    case OBJ_FACE_TOKEN:
                        result.polygons.add(parseFace(wordsInLine, lineInd));
                        break;
                    default:
                        break;
                }
            } catch (ObjReaderException e) {
                throw new ObjReaderException("Error parsing OBJ file on line: " + lineInd + ". " + e.getMessage(), lineInd);
            }
        }

        return result;
    }

    protected static Vector3f parseVertex(final ArrayList<String> wordsInLineWithoutToken, int lineInd) {
        wordsInLineWithoutToken.removeIf(String::isEmpty);

        if (wordsInLineWithoutToken.size() < 3) {
            throw new ObjReaderException("Too few vertex arguments. Expected 3, got " + wordsInLineWithoutToken.size(), lineInd);
        }

        try {
            float x = parseFloatSafe(wordsInLineWithoutToken.get(0), lineInd);
            float y = parseFloatSafe(wordsInLineWithoutToken.get(1), lineInd);
            float z = parseFloatSafe(wordsInLineWithoutToken.get(2), lineInd);

            return new Vector3f(x, y, z);
        } catch(IndexOutOfBoundsException e) {
            throw new ObjReaderException("Too few vertex arguments.", lineInd);
        }
    }

    protected static Vector2f parseTextureVertex(final ArrayList<String> wordsInLineWithoutToken, int lineInd) {
        wordsInLineWithoutToken.removeIf(String::isEmpty);

        if (wordsInLineWithoutToken.size() < 2) {
            throw new ObjReaderException("Too few texture vertex arguments. Expected at least 2, got " + wordsInLineWithoutToken.size(), lineInd);
        }

        try {
            float u = parseFloatSafe(wordsInLineWithoutToken.get(0), lineInd);
            float v = parseFloatSafe(wordsInLineWithoutToken.get(1), lineInd);

            return new Vector2f(u, v);
        } catch(IndexOutOfBoundsException e) {
            throw new ObjReaderException("Too few texture vertex arguments.", lineInd);
        }
    }

    protected static Vector3f parseNormal(final ArrayList<String> wordsInLineWithoutToken, int lineInd) {
        wordsInLineWithoutToken.removeIf(String::isEmpty);

        if (wordsInLineWithoutToken.size() < 3) {
            throw new ObjReaderException("Too few normal arguments. Expected 3, got " + wordsInLineWithoutToken.size(), lineInd);
        }

        try {
            float x = parseFloatSafe(wordsInLineWithoutToken.get(0), lineInd);
            float y = parseFloatSafe(wordsInLineWithoutToken.get(1), lineInd);
            float z = parseFloatSafe(wordsInLineWithoutToken.get(2), lineInd);

            return new Vector3f(x, y, z);
        } catch(IndexOutOfBoundsException e) {
            throw new ObjReaderException("Too few normal arguments.", lineInd);
        }
    }

    private static float parseFloatSafe(String value, int lineInd) {
        try {
            String cleanedValue = value
                    .replace('Φ', ' ')
                    .replace('φ', ' ')
                    .replace(',', '.')
                    .trim();

            cleanedValue = cleanedValue.replaceAll("[^0-9.Ee-]", "");

            if (cleanedValue.isEmpty()) {
                throw new ObjReaderException("Empty coordinate value: '" + value + "'", lineInd);
            }

            return Float.parseFloat(cleanedValue);
        } catch (NumberFormatException e) {
            throw new ObjReaderException("Failed to parse coordinate value: '" + value + "'", lineInd);
        }
    }

    protected static Polygon parseFace(final ArrayList<String> wordsInLineWithoutToken, int lineInd) {
        wordsInLineWithoutToken.removeIf(String::isEmpty);

        if (wordsInLineWithoutToken.isEmpty()) {
            throw new ObjReaderException("Face definition is empty", lineInd);
        }

        ArrayList<Integer> vertexIndices = new ArrayList<>();
        ArrayList<Integer> textureIndices = new ArrayList<>();
        ArrayList<Integer> normalIndices = new ArrayList<>();

        for (String s : wordsInLineWithoutToken) {
            if (!s.trim().isEmpty()) {
                parseFaceWord(s, vertexIndices, textureIndices, normalIndices, lineInd);
            }
        }

        if (vertexIndices.size() < 3) {
            throw new ObjReaderException("Polygon must have at least 3 vertices. Found " + vertexIndices.size(), lineInd);
        }

        Polygon result = new Polygon();
        result.setVertexIndices(vertexIndices);
        result.setTextureVertexIndices(textureIndices);
        result.setNormalIndices(normalIndices);

        return result;
    }

    protected static void parseFaceWord(
            String wordInLine,
            ArrayList<Integer> vertexIndices,
            ArrayList<Integer> textureIndices,
            ArrayList<Integer> normalIndices,
            int lineInd) {
        try {
            wordInLine = wordInLine.trim();

            if (wordInLine.isEmpty()) {
                throw new ObjReaderException("Empty face element", lineInd);
            }

            String[] wordIndices = wordInLine.split("/", -1);

            if (wordIndices.length > 3) {
                throw new ObjReaderException("Invalid element size: " + wordIndices.length + " parts", lineInd);
            }

            if (wordIndices[0].isEmpty()) {
                throw new ObjReaderException("Missing vertex index in face element", lineInd);
            }

            int vertexIndex = parseIntSafe(wordIndices[0], lineInd) - 1;
            vertexIndices.add(vertexIndex);

            if (wordIndices.length >= 2 && !wordIndices[1].isEmpty()) {
                int textureIndex = parseIntSafe(wordIndices[1], lineInd) - 1;
                textureIndices.add(textureIndex);
            }

            if (wordIndices.length == 3 && !wordIndices[2].isEmpty()) {
                int normalIndex = parseIntSafe(wordIndices[2], lineInd) - 1;
                normalIndices.add(normalIndex);
            }

        } catch(NumberFormatException e) {
            throw new ObjReaderException("Failed to parse int value in face element: '" + wordInLine + "'", lineInd);
        } catch(IndexOutOfBoundsException e) {
            throw new ObjReaderException("Too few arguments in face element.", lineInd);
        } catch (Exception e) {
            throw new ObjReaderException("Unexpected error in face parsing: " + e.getMessage(), lineInd);
        }
    }

    private static int parseIntSafe(String value, int lineInd) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new ObjReaderException("Failed to parse index: '" + value + "'", lineInd);
        }
    }
}