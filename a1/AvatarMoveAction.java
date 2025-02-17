package a1;

import net.java.games.input.Event;
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

        float distance = this.distance * time;

        if (forward) {
            av.moveForward(game.onDolphin ? distance * 3 : distance);
        } else {
            av.moveBackward(game.onDolphin ? distance * 3 : distance);
        }
    }
}