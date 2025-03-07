package a2;

import net.java.games.input.Event;
import tage.GameObject;
import tage.input.action.AbstractInputAction;

public class AvatarRotateAction extends AbstractInputAction {
    private final MyGame game;
    private final float speed;
    private final boolean isUp;

    public AvatarRotateAction(MyGame game, float speed, boolean isUp) {
        this.game = game;
        this.speed = speed;
        this.isUp = isUp;
    }

    @Override
    public void performAction(float time, Event e) {
        GameObject av = game.getAvatar();
        float speed = this.speed * time;
        av.localPitch(isUp ? -speed : speed);
    }
}
