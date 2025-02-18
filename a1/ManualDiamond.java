package a1;

import tage.shapes.ManualObject;

public class ManualDiamond extends ManualObject {
    private float[] vertices = new float[] {
            // Top pyramid (apex at +1 Y)
            0.0f, 1.0f, 0.0f,  -1.0f, 0.0f, 1.0f,  1.0f, 0.0f, 1.0f,  // Front face
            0.0f, 1.0f, 0.0f,   1.0f, 0.0f, 1.0f,  1.0f, 0.0f, -1.0f, // Right face
            0.0f, 1.0f, 0.0f,   1.0f, 0.0f, -1.0f, -1.0f, 0.0f, -1.0f, // Back face
            0.0f, 1.0f, 0.0f,  -1.0f, 0.0f, -1.0f, -1.0f, 0.0f, 1.0f,  // Left face

            // Bottom pyramid (apex at -1 Y)
            0.0f, -1.0f, 0.0f, -1.0f, 0.0f, 1.0f,  1.0f, 0.0f, 1.0f,  // Front face
            0.0f, -1.0f, 0.0f,  1.0f, 0.0f, 1.0f,  1.0f, 0.0f, -1.0f, // Right face
            0.0f, -1.0f, 0.0f,  1.0f, 0.0f, -1.0f, -1.0f, 0.0f, -1.0f, // Back face
            0.0f, -1.0f, 0.0f, -1.0f, 0.0f, -1.0f, -1.0f, 0.0f, 1.0f   // Left face
    };

    private float[] texcoords = new float[] {
            0.5f, 1.0f,  0.0f, 0.0f,  1.0f, 0.0f,
            0.5f, 1.0f,  0.0f, 0.0f,  1.0f, 0.0f,
            0.5f, 1.0f,  0.0f, 0.0f,  1.0f, 0.0f,
            0.5f, 1.0f,  0.0f, 0.0f,  1.0f, 0.0f,

            0.5f, 0.0f,  0.0f, 1.0f,  1.0f, 1.0f,
            0.5f, 0.0f,  0.0f, 1.0f,  1.0f, 1.0f,
            0.5f, 0.0f,  0.0f, 1.0f,  1.0f, 1.0f,
            0.5f, 0.0f,  0.0f, 1.0f,  1.0f, 1.0f
    };

    private float[] normals = new float[] {
            0.0f, 1.0f, 1.0f,  0.0f, 1.0f, 1.0f,  0.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 0.0f,  1.0f, 1.0f, 0.0f,  1.0f, 1.0f, 0.0f,
            0.0f, 1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f,

            0.0f, -1.0f, 1.0f,  0.0f, -1.0f, 1.0f,  0.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 0.0f,  1.0f, -1.0f, 0.0f,  1.0f, -1.0f, 0.0f,
            0.0f, -1.0f, -1.0f, 0.0f, -1.0f, -1.0f, 0.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, 0.0f, -1.0f, -1.0f, 0.0f, -1.0f, -1.0f, 0.0f
    };

    public ManualDiamond() {
        super();
        setNumVertices(24);
        setVertices(vertices);
        setTexCoords(texcoords);
        setNormals(normals);
    }
}
