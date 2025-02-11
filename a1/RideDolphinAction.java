package a1;

import net.java.games.input.Event;
import tage.*;
import tage.input.action.AbstractInputAction;

public class RideDolphinAction extends AbstractInputAction {
    MyGame game;

    public RideDolphinAction(MyGame g) {
        game = g;
    }

    @Override
    public void performAction(float time, Event e) {
        game.onDolphin = !game.onDolphin;
    }
}