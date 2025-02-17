package a1;

import net.java.games.input.Event;
import org.joml.Vector3f;
import tage.GameObject;
import tage.input.action.AbstractInputAction;

public class AvatarMoveAction extends AbstractInputAction {
    private final MyGame game;
    private final float distance;
    private final boolean forward;
    private final GameObject dol;

    public AvatarMoveAction(MyGame g, float distance, boolean forward, GameObject dol) {
        this.game = g;
        this.distance = distance;
        this.forward = forward;
        this.dol = dol;
    }

    @Override
    public void performAction(float time, Event e) {
        GameObject av = game.getAvatar();
        float distance = this.distance * time * (game.onDolphin ? 3 : 1);
        Vector3f prevLocation = new Vector3f(av.getLocalLocation());

        if (forward) {
            av.moveForward(distance);
        } else {
            av.moveBackward(distance);
        }

        float avToDol = av.getLocalLocation().distance(dol.getLocalLocation());
        if (!game.onDolphin && avToDol > 10.0f) {
            av.setLocalLocation(prevLocation);
        }
    }
}