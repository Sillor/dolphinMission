package a1;

import net.java.games.input.Event;
import org.joml.Vector3f;
import org.joml.Vector4f;
import tage.GameObject;
import tage.input.action.AbstractInputAction;

public class AvatarMoveAction extends AbstractInputAction {
    private final MyGame game;
    private final float distance;
    private final boolean forward;

    public AvatarMoveAction(MyGame g, float distance, boolean forward) {
        this.game = g;
        this.distance = distance;
        this.forward = forward;
    }

    @Override
    public void performAction(float time, Event e) {
        GameObject av = game.getAvatar();
        if (forward) {
            av.moveForward(distance);
        } else {
            av.moveBackward(distance);
        }
    }
}