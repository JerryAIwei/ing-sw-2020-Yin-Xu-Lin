package it.polimi.ingsw.xyl.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.VertexFormat;

/**
 * Loader.java
 * <p>
 * This class is responsible for loading any required assets for the game.
 * @author https://github.com/danielbarry/javafx-mesh-importer
 **/
public abstract class Loader {
    /**
     * loadObj()
     * <p>
     * Loads an OBJ file from disk and convert it to a mesh.
     *
     * @param path The file path to load the OBJ from.
     * @return The mesh of the selected file.
     **/
    public static Mesh loadMesh(String path) {
        TriangleMesh mesh = new TriangleMesh(VertexFormat.POINT_NORMAL_TEXCOORD);
        ArrayList<String> lines = readTextFile(path);
        for (int x = 0; x < lines.size(); x++) {
            String line = lines.get(x);
            if (line != null) {
                line = line.trim();
                switch (line.charAt(0)) {
                    /* Comment */
                    case '#':
                        /* Ignore comments */
                        break;
                    /* Polygonal face element */
                    case 'f':
                        String[] faces = line.replace("f", "").trim().split(" ");
                        for (int y = 0; y < faces.length; y++) {
                            String[] temp = faces[y].split("/");
                            /* NOTE: Java loads this in the wrong order. */
                            mesh.getFaces().addAll(Integer.parseInt(temp[0]) - 1);
                            mesh.getFaces().addAll(Integer.parseInt(temp[2]) - 1);
                            mesh.getFaces().addAll(Integer.parseInt(temp[1]) - 1);
                        }
                        break;
                    case 'v':
                        switch (line.charAt(1)) {
                            /* List of geometric vertices, with (x,y,z[,w]) coordinates */
                            case ' ':
                                String[] verts = line.replace("v", "").trim().split(" ");
                                for (int y = 0; y < verts.length; y++) {
                                    mesh.getPoints().addAll(Float.parseFloat(verts[y]));
                                }
                                break;
                            /* List of texture coordinates, in (u, v [,w]) coordinates */
                            case 't':
                                String[] texts = line.replace("vt", "").trim().split(" ");
                                for (int y = 0; y < texts.length; y++) {
                                    mesh.getTexCoords().addAll(Float.parseFloat(texts[y]));
                                }
                                break;
                            /* List of vertex normals in (x,y,z) form */
                            case 'n':
                                String[] norms = line.replace("vn", "").trim().split(" ");
                                for (int y = 0; y < norms.length; y++) {
                                    mesh.getNormals().addAll(Float.parseFloat(norms[y]));
                                }
                                break;
                        }
                        break;
                }
            }
        }
        return mesh;
    }


    /**
     * loadObj()
     * <p>
     * Loads an OBJ file from disk and convert it to a mesh.
     *
     * @param path The file path to load the OBJ from.
     * @return The mesh of the selected file.
     **/
    public static MeshView loadObj(String path) {
        return new MeshView(loadMesh(path));
    }

    /**
     * loadStl()
     * <p>
     * Loads an STL file from disk and convert it to a mesh.
     * <p>
     * NOTE: Textures do not map correctly! This is because STL files do not
     * preserve mapping data.
     * <p>
     * Source: https://stackoverflow.com/q/21997622
     *
     * @param path The file path to load the STL from.
     * @return The mesh of the selected file.
     **/
    public static MeshView loadStl(String path) {
        TriangleMesh mesh = new TriangleMesh();
        ArrayList<String> lines = readTextFile(path);
        int faceCnt = 0;
        for (int x = 0; x < lines.size(); x++) {
            String line = lines.get(x);
            if (!(
                    line == null ||
                            line.indexOf("solid") >= 0 ||
                            line.indexOf("outer") >= 0 ||
                            line.indexOf("end") >= 0
            )) {
                if (line.indexOf("facet") >= 0) {
                    String[] normals = line.replaceFirst("facet normal", "").trim().split(" ");
                    for (int y = 0; y < 3; y++) {
                        for (String n : normals) {
                            /* TODO: This does not and *cannot* work correctly. */
                            int facets = (int) Math.sqrt((lines.size() - 2) / 7);
                            mesh.getTexCoords().addAll(((Float.parseFloat(n) + 1) / -2));
                        }
                    }
                } else {
                    int target = x + 3;
                    for (; x < target; x++) {
                        line = lines.get(x);
                        String[] points = line.replaceFirst("vertex", "").trim().split(" ");
                        for (int y = 0; y < points.length; y++) {
                            mesh.getPoints().addAll(Float.parseFloat(points[y]));
                        }
                    }
                    mesh.getFaces().addAll(faceCnt, faceCnt, faceCnt + 1, faceCnt + 1, faceCnt + 2, faceCnt + 2);
                    faceCnt += 3;
                }
            }
        }
        return new MeshView(mesh);
    }

    public static ArrayList<String> readTextFile(String path) {
        try {
            BufferedReader br =
                    new BufferedReader(
                            new InputStreamReader
                                    (Loader.class.getResourceAsStream(path)));

            ArrayList<String> lines = new ArrayList<String>();
            lines.add(br.readLine());
            while (lines.get(lines.size() - 1) != null) {
                lines.add(br.readLine());
            }
            return lines;
        } catch (Exception e) {
            error("readTextFile", "Exception thrown when reading `" + path + "`");
        }
        return null;
    }

    /**
     * error()
     * <p>
     * Display an error message for a loading issue.
     *
     * @param mthd The method the failure happened in.
     * @param msg  The message to be displayed as a result.
     **/
    private static void error(String mthd, String msg) {
        System.out.println(
                System.currentTimeMillis() +
                        " [!!] Loader->" +
                        mthd +
                        "() " +
                        msg
        );
    }
}