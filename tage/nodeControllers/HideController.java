package tage.nodeControllers;

import tage.*;
import org.joml.Matrix4f;

public class HideController extends NodeController {
    Matrix4f scale;

    public HideController() {
        super();

    }

    public void apply(GameObject go) {
        if (go.getTextureImage() != null) {
            System.out.println("Hiding object");
            scale = go.getLocalScale();
            go.setLocalScale(new Matrix4f().scaling(0.0f));
        } else {
            System.out.println("Showing object");
            go.setLocalScale(scale);
        }
    }
}
