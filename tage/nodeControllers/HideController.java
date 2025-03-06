package tage.nodeControllers;

import tage.*;
import org.joml.Matrix4f;

/**
 * A HideController is a node controller that, when enabled, hides any object
 * it is attached to by setting its scale to zero if it is not already zero.
 * If the object's scale is already zero, it restores the previous scale.
 *
 * This can be useful for temporarily making objects disappear from the scene
 * without fully removing them from the scene graph.
 *
 * @author Egor Strakhov
 */
public class HideController extends NodeController {
    private Matrix4f scale;

    /**
     * Creates a HideController instance.
     */
    public HideController() {
        super();
    }

    /**
     * Applies the hide effect to the given GameObject.
     * If the object's scale is not zero, it is hidden by scaling it to zero.
     * Otherwise, it restores its original scale.
     *
     * @param go the GameObject to which this controller is applied
     */
    public void apply(GameObject go) {
        if (go.getLocalScale().m00() != 0.0f || go.getLocalScale().m11() != 0.0f || go.getLocalScale().m22() != 0.0f) {
            scale = go.getLocalScale();
            go.setLocalScale(new Matrix4f().scaling(0.0f));
        }
//        else {
//            go.setLocalScale(scale);
//        }
    }
}
