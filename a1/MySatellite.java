package a1;

import org.joml.Matrix4f;
import tage.GameObject;
import tage.ObjShape;
import tage.TextureImage;

import java.util.Objects;

public class MySatellite {
    ObjShape obj;
    TextureImage texture;
    GameObject satellite;

    MySatellite() {}

    void loadShape(ObjShape shape) {
        obj = shape;
    }

    void loadTexture(TextureImage texture) {
        this.texture = texture;
    }

    void buildObject(float x, float y, float z, float scale) {
        Matrix4f initialTranslation, initialScale;

        satellite = new GameObject(GameObject.root(), obj, texture);
        initialTranslation = (new Matrix4f()).translation(x,y,z);
        initialScale = (new Matrix4f()).scaling(scale);
        satellite.setLocalTranslation(initialTranslation);
        satellite.setLocalScale(initialScale);
    }

    void updateTexture(TextureImage texture) {
        if (!Objects.equals(this.texture.getTextureFile(), texture.getTextureFile())) {
            this.texture = texture;
            satellite.setTextureImage(texture);
        }
    }
}
