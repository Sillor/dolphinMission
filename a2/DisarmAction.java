package a2;

import net.java.games.input.Event;
import org.joml.Matrix4f;
import tage.*;
import tage.input.action.AbstractInputAction;

import java.util.Arrays;

public class DisarmAction extends AbstractInputAction {
    MyGame game;
    GameObject dol;
    GameObject player;
    MySatellite[] satellites;

    public DisarmAction(MyGame g, GameObject dol) {
        game = g;
        this.dol = dol;
        this.satellites = new MySatellite[]{ g.getSatellite1(), g.getSatellite2(), g.getSatellite3() };
    }

    @Override
    public void performAction(float time, Event e) {
        NodeController[] rcs = game.getRcs();
        GameObject[] diamonds = game.getManualDiamonds();

        for (int i = 0; i < satellites.length; i++) {
            if (satellites[i].isClose() && !satellites[i].isDisarmed()) {
                satellites[i].setDisarmed(true);
                game.getHUDmanager().incrementScore();
                rcs[i].toggle();
                diamonds[i].setParent(game.getAvatar());
                diamonds[i].setLocalTranslation(game.getAvatar().getLocalTranslation().translation(1f, i * 0.3f + 0.2f, 0));
                diamonds[i].setLocalScale(new Matrix4f().scaling(0.02f));
                diamonds[i].propagateTranslation(true);
            }
        }
    }
}