package a1;

import net.java.games.input.Event;
import org.joml.Vector3f;
import org.joml.Vector4f;
import tage.GameObject;
import tage.input.action.AbstractInputAction;

public class AvatarMoveAction extends AbstractInputAction {
    private final MyGame game;
    private final float speed;
    private final boolean forward;

    public AvatarMoveAction(MyGame g, float speed, boolean forward) {
        this.game = g;
        this.speed = speed;
        this.forward = forward;
    }

    @Override
    public void performAction(float time, Event e) {
        GameObject av = game.getAvatar();
        Vector3f oldPosition = av.getWorldLocation();
        Vector4f movementDirection = new Vector4f(0f, 0f, 1f, 1f);
        movementDirection.mul(av.getWorldRotation());
        movementDirection.mul(forward ? speed : -speed);
        Vector3f newPosition = oldPosition.add(movementDirection.x(),
                movementDirection.y(),
                movementDirection.z());
        av.setLocalLocation(newPosition);
    }
}