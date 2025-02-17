package a1;

import org.joml.Vector3f;
import org.joml.Matrix4f;
import tage.GameObject;
import tage.*;

import java.util.Random;

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
    private Vector3f position;

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

    void buildObject(float scale, float maxDistance, float gap) {
        this.position = generateRandomPosition(maxDistance, gap);
        satellite = new GameObject(GameObject.root(), obj, texture);
        satellite.setLocalTranslation(new Matrix4f().translation(position.x, position.y, position.z));
        satellite.setLocalScale(new Matrix4f().scaling(scale));
    }

    private Vector3f generateRandomPosition(float maxDistance, float gap) {
        Random random = new Random();
        return new Vector3f(
                (random.nextFloat() * 2 - 1) * maxDistance + gap,
                (random.nextFloat() * 2 - 1) * maxDistance + gap,
                (random.nextFloat() * 2 - 1) * maxDistance + gap
        );
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
