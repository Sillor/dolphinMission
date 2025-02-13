package a1;

import com.jogamp.opengl.util.texture.Texture;
import org.joml.Matrix4f;
import org.w3c.dom.Text;
import tage.GameObject;
import tage.ObjShape;
import tage.TextureImage;

import java.util.Objects;

public class MySatellite {
    ObjShape obj;
    TextureImage texture;
    GameObject satellite;
    private boolean isClose = false;
    private boolean isDisarmed = false;
    private boolean isDetonated = false;
    private TextureImage satelliteImage;
    private TextureImage disarmedImage;
    private TextureImage detonatedImage;
    private TextureImage closeImage;

    MySatellite() {
    }

    void loadShape(ObjShape shape) {
        obj = shape;
    }

    void loadTexture(TextureImage satelliteImage, TextureImage closeImage, TextureImage disarmedImage, TextureImage detonatedImage) {
        this.texture = satelliteImage;
        this.satelliteImage = satelliteImage;
        this.disarmedImage = disarmedImage;
        this.detonatedImage = detonatedImage;
        this.closeImage = closeImage;
    }

    void buildObject(float x, float y, float z, float scale) {
        Matrix4f initialTranslation, initialScale;

        satellite = new GameObject(GameObject.root(), obj, texture);
        initialTranslation = (new Matrix4f()).translation(x,y,z);
        initialScale = (new Matrix4f()).scaling(scale);
        satellite.setLocalTranslation(initialTranslation);
        satellite.setLocalScale(initialScale);
    }

    void updateTexture() {
        TextureImage newTexture;

        if (isDetonated) {
            newTexture = detonatedImage;
        } else if (isDisarmed) {
            newTexture = disarmedImage;
        } else if (isClose) {
            newTexture = closeImage;
        } else {
            newTexture = satelliteImage;
        }

        if (newTexture != texture) {
            texture = newTexture;
            satellite.setTextureImage(newTexture);
        }
    }

    public boolean isClose() {
        return isClose;
    }

    public void setClose(boolean close) {
        isClose = close;
    }

    public boolean isDisarmed() {
        return isDisarmed;
    }

    public void setDisarmed(boolean disarmed) {
        isDisarmed = disarmed;
    }

    public boolean isDetonated() {
        return isDetonated;
    }

    public void setDetonated(boolean detonated) {
        isDetonated = detonated;
    }
}
