package a1;

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

        engine.getHUDmanager().setHUD1(newScore, hudWhite, 10, 10);
        engine.getHUDmanager().setHUD2(satelliteInfo, hudYellow, 10, 920);

        if (score >= 3) {
            engine.getHUDmanager().setHUD1("You win!", hudGreen, 1900 / 2 - 50, 1000 / 2 - 50);
            g.setPaused();
        }

        if (isGameOver) {
            engine.getHUDmanager().setHUD1("You lost!", hudRed, 1900 / 2 - 50, 1200 / 2 - 50);
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
