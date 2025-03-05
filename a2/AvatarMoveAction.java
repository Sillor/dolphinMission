package a2;

import net.java.games.input.Event;
import org.joml.Vector3f;
import tage.GameObject;
import tage.input.action.AbstractInputAction;

public class AvatarMoveAction extends AbstractInputAction {
    private final MyGame game;
    private final GameObject dol;
    private final boolean isGamepad;
    private final float distance;
    private final boolean forward;

    public AvatarMoveAction(MyGame g, GameObject dol, boolean isGamepad, float distance, boolean forward) {
        this.game = g;
        this.dol = dol;
        this.isGamepad = isGamepad;
        this.distance = distance;
        this.forward = forward;
    }

    @Override
    public void performAction(float time, Event e) {
        GameObject av = game.getAvatar();
        float axisValue;

        if (isGamepad) {
            axisValue = -e.getValue();
            if (Math.abs(axisValue) < 0.1) {
                return;
            }
        } else {
            axisValue = forward ? 1 : -1;
        }

        float moveDistance = axisValue * time * 5.0f * (game.onDolphin ? 3 : 1);
        Vector3f prevLocation = new Vector3f(av.getLocalLocation());

        if (axisValue > 0) {
            av.moveForward(moveDistance);
        } else {
            av.moveBackward(-moveDistance);
        }

        float avToDol = av.getLocalLocation().distance(dol.getLocalLocation());
        if (!game.onDolphin && avToDol > 10.0f) {
            av.setLocalLocation(prevLocation);
        }
    }
}
