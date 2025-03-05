package a2;

import net.java.games.input.Event;
import org.joml.Math;
import tage.GameObject;
import tage.input.action.AbstractInputAction;

public class AvatarTurnAction extends AbstractInputAction {
    private final MyGame game;
    private final boolean isGamepad;
    private final boolean isLeftTurn;
    private final float speed;

    public AvatarTurnAction(MyGame game, boolean isGamepad, float speed, boolean isLeftTurn) {
        this.game = game;
        this.isGamepad = isGamepad;
        this.isLeftTurn = isLeftTurn;
        this.speed = speed;
    }

    @Override
    public void performAction(float time, Event e) {
        GameObject av = game.getAvatar();
        float turnSpeed;

        if (isGamepad) {
            turnSpeed = -e.getValue();
            if (Math.abs(turnSpeed) < 0.1f) {
                return;
            }
        } else {
            turnSpeed = 1;
        }

        float rotationAmount = turnSpeed * speed * time;
        av.localYaw(isLeftTurn ? rotationAmount : -rotationAmount);
    }
}
