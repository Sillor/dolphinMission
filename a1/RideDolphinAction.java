package a1;

import net.java.games.input.Event;
import org.joml.Vector3f;
import tage.*;
import tage.input.action.AbstractInputAction;

public class RideDolphinAction extends AbstractInputAction {
    MyGame game;
    GameObject dol;
    GameObject player;
    public RideDolphinAction(MyGame g, GameObject dol, GameObject player) {
        game = g;
        this.dol = dol;
        this.player = player;
    }

    @Override
    public void performAction(float time, Event e) {
        if (!game.isPaused()) {
            if (game.onDolphin) {
                float x, y, z;

                x = dol.getLocalLocation().x - 3.5f;
                y = dol.getLocalLocation().y;
                z = dol.getLocalLocation().z;

                player.setLocalLocation(new Vector3f(x, y, z));
                player.setLocalRotation(dol.getLocalRotation());
            }
            game.onDolphin = !game.onDolphin;
        }
    }
}