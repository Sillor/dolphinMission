package a2;

import org.joml.Vector3f;
import tage.Engine;
import tage.GameObject;

public class MyHUDmanager {
    Engine engine;
    int score;
    boolean isGameOver;
    MyGame g;

    public MyHUDmanager(Engine engine, MyGame g) {
        this.engine = engine;
        score = 0;
        this.g = g;
    }

    public void update() {
        MySatellite closestSat = getClosestSatellite();

        String newScore = "Score: " + score;
        String satelliteInfo = getString(closestSat);

        Vector3f hudWhite = new Vector3f(1, 1, 1);
        Vector3f hudRed = new Vector3f(1, 0, 0);
        Vector3f hudGreen = new Vector3f(0, 1, 0);
        Vector3f hudYellow = new Vector3f(1, 1, 0);

        // Get LEFT viewport properties
        int leftViewportX = 0;
        int leftViewportY = 0;

        // Get RIGHT viewport properties
        int rightViewportX = (int) engine.getRenderSystem().getViewport("LEFT").getActualWidth() - (int) engine.getRenderSystem().getViewport("RIGHT").getActualWidth();
        int rightViewportY = (int) engine.getRenderSystem().getViewport("RIGHT").getRelativeBottom() +
                (int) engine.getRenderSystem().getViewport("RIGHT").getRelativeHeight();

        // Set HUD1 in the upper-left corner of the LEFT viewport
        engine.getHUDmanager().setHUD1(
                satelliteInfo,
                hudYellow,
                leftViewportX + 20,   // X position: Start from left
                leftViewportY + 10       // Y position: Move down from top
        );

        // Set HUD2 in the upper-left corner of the RIGHT viewport
        engine.getHUDmanager().setHUD2(
                newScore,
                hudWhite,
                rightViewportX + 10,  // X position: Start from right viewport's left
                rightViewportY + 10      // Y position: Move down from top
        );

        if (score >= 3) {
            engine.getHUDmanager().setHUD1("You win!", hudGreen, (int) engine.getRenderSystem().getViewport("LEFT").getActualWidth() / 2 - 50, (int) engine.getRenderSystem().getViewport("LEFT").getActualHeight() / 2 - 50);
            g.setPaused();
        }

        if (isGameOver) {
            engine.getHUDmanager().setHUD1("You lost!", hudRed, (int) engine.getRenderSystem().getViewport("LEFT").getActualWidth() / 2 - 50, (int) engine.getRenderSystem().getViewport("LEFT").getActualHeight() / 2 - 50);
        }
    }

    private String getString(MySatellite closestSat) {
        String satelliteInfo = "";

        if (closestSat == g.getSatellite1()) {
            satelliteInfo = "Closest Satellite: Satellite 1 - " + g.getSatellite1().getSatelliteInfo();
        } else if (closestSat == g.getSatellite2()) {
            satelliteInfo = "Closest Satellite: Satellite 2 - " + g.getSatellite2().getSatelliteInfo();
        } else if (closestSat == g.getSatellite3()) {
            satelliteInfo = "Closest Satellite: Satellite 3 - " + g.getSatellite3().getSatelliteInfo();
        }
        return satelliteInfo;
    }

    public MySatellite getClosestSatellite() {
        GameObject av = g.getAvatar();
        MySatellite closestSat = null;

        float sat1Dist = av.getLocalLocation().distance(g.getSatellite1().satellite.getLocalLocation());
        float sat2Dist = av.getLocalLocation().distance(g.getSatellite2().satellite.getLocalLocation());
        float sat3Dist = av.getLocalLocation().distance(g.getSatellite3().satellite.getLocalLocation());

        if (sat1Dist < sat2Dist && sat1Dist < sat3Dist) {
            closestSat = g.getSatellite1();
        } else if (sat2Dist < sat1Dist && sat2Dist < sat3Dist) {
            closestSat = g.getSatellite2();
        } else {
            closestSat = g.getSatellite3();
        }

        return closestSat;
    }

    public void incrementScore() {
        score++;
    }

    public void setGameOver() {
        isGameOver = true;
    }
}
