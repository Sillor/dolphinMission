package a1;

import org.joml.Vector3f;
import tage.Engine;

public class HUDmanager {
    Engine engine;
    int score;
    boolean isGameOver;

    public HUDmanager(Engine engine) {
        this.engine = engine;
        score = 0;

    }

    public void update() {
        String newScore = "Score: " + score;
        Vector3f hudWhite = new Vector3f(1, 1, 1);
        Vector3f hudRed = new Vector3f(1, 0, 0);
        Vector3f hudGreen = new Vector3f(0, 1, 0);

        engine.getHUDmanager().setHUD1(newScore, hudWhite,  10, 10);

        if (score >= 3) {
            engine.getHUDmanager().setHUD1("You win!", hudGreen, 1900 / 2 - 50, 1200 / 2 - 50);
        }

        if (isGameOver) {
            engine.getHUDmanager().setHUD1("You lost!", hudRed, 1900 / 2 - 50, 1200 / 2 - 50);
        }
    }

    public void incrementScore() {
        score++;
    }

    public void setGameOver() {
        isGameOver = true;
    }
}
