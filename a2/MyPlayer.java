package a2;

import tage.*;
import org.joml.*;

public class MyPlayer {
    public static GameObject player;

    public MyPlayer() {}

    public void buildObject(float x, float y, float z, float scale) {
        Matrix4f initialTranslation = new Matrix4f().translation(x, y, z);
        Matrix4f initialScale = new Matrix4f().scaling(scale);
        player = new GameObject(GameObject.root());
        player.setLocalTranslation(initialTranslation);
        player.setLocalScale(initialScale);
    }

    public Vector3f getLocation() {
        return player.getWorldLocation();
    }

    public Vector3f getForwardVector() {
        return player.getWorldForwardVector();
    }

    public Vector3f getUpVector() {
        return player.getWorldUpVector();
    }

    public Vector3f getRightVector() {
        return player.getWorldRightVector();
    }
}
