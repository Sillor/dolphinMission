package a1;

import tage.*;
import org.joml.*;
import tage.shapes.*;

public class MyDolphin {
    public static GameObject dol;
    private ObjShape dolS;
    private TextureImage doltx;
    private double elapsedTime;

    public MyDolphin() {
        elapsedTime = 0.0;
    }

    public void loadShape() {
        dolS = new ImportedModel("dolphinHighPoly.obj");
    }

    public void loadTexture() {
        doltx = new TextureImage("Dolphin_HighPolyUV.png");
    }

    public void buildObject(float x, float y, float z, float scale) {
        Matrix4f initialTranslation = new Matrix4f().translation(x, y, z);
        Matrix4f initialScale = new Matrix4f().scaling(scale);
        dol = new GameObject(GameObject.root(), dolS, doltx);
        dol.setLocalTranslation(initialTranslation);
        dol.setLocalScale(initialScale);
    }

    public void update(double deltaTime) {
        elapsedTime += deltaTime;
    }

    public Vector3f getLocation() {
        return dol.getWorldLocation();
    }

    public Vector3f getForwardVector() {
        return dol.getWorldForwardVector();
    }

    public Vector3f getUpVector() {
        return dol.getWorldUpVector();
    }

    public Vector3f getRightVector() {
        return dol.getWorldRightVector();
    }
}
