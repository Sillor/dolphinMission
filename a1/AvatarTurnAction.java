package a1;

import net.java.games.input.Event;
import tage.GameObject;
import tage.input.action.AbstractInputAction;
import org.joml.*;

public class AvatarTurnAction extends AbstractInputAction {
    private final MyGame game;
    private final boolean isLeftTurn;
    private final float speed;

    public AvatarTurnAction(MyGame game, float speed, boolean isLeftTurn) {
        this.game = game;
        this.isLeftTurn = isLeftTurn;
        this.speed = speed;
    }

    @Override
    public void performAction(float time, Event e) {
        GameObject av = game.getAvatar();
        Matrix4f oldRotation = new Matrix4f(av.getWorldRotation());
        Vector3f oldUp = av.getWorldUpVector();

        float rotationAmount = isLeftTurn ? speed : -speed;
        Matrix4fc rotAroundAvatarUp = new Matrix4f().rotation(rotationAmount,
                new Vector3f(oldUp.x(), oldUp.y(), oldUp.z()));

        oldRotation.mul(rotAroundAvatarUp);
        av.setLocalRotation(oldRotation);
    }
}
