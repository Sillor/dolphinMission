package a2;

import org.joml.Vector3f;
import tage.*;
import tage.input.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;

public class CameraOrbit3D {
    private final Engine engine;
    private final Camera camera;
    private final GameObject avatar;
    private float cameraAzimuth = 0.0f;
    private float cameraElevation = 20.0f;
    private float cameraRadius = 3.0f;

    private static final float ROTATION_SPEED = 2.0f;
    private static final float ZOOM_SPEED = 0.1f;
    private static final float ELEVATION_SPEED = 1.0f;
    private static final float JOYSTICK_DEADZONE = 0.2f;
    private static final float MIN_ZOOM = 1.0f;
    private static final float MAX_ZOOM = 10.0f;

    public CameraOrbit3D(Camera cam, GameObject av, String kbName, String gpName, Engine e) {
        engine = e;
        camera = cam;
        avatar = av;
        setupInputs(kbName, gpName);
        updateCameraPosition();
    }

    private void setupInputs(String kb, String gp) {
        InputManager im = engine.getInputManager();

        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.I,
                new OrbitAzimuthAction(true), InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.O,
                new OrbitAzimuthAction(false), InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.K,
                new OrbitRadiusAction(true), InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.L,
                new OrbitRadiusAction(false), InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.U,
                new OrbitElevationAction(true), InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.J,
                new OrbitElevationAction(false), InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
    }

    public void updateCameraPosition() {
        Vector3f avatarRot = avatar.getWorldForwardVector();
        double avatarAngle = Math.toDegrees(avatarRot.angleSigned(
                new Vector3f(0, 0, -1), new Vector3f(0, 1, 0)));
        float totalAz = cameraAzimuth - (float) avatarAngle;
        double theta = Math.toRadians(totalAz);
        double phi = Math.toRadians(cameraElevation);
        float x = cameraRadius * (float) (Math.cos(phi) * Math.sin(theta));
        float y = cameraRadius * (float) (Math.sin(phi));
        float z = cameraRadius * (float) (Math.cos(phi) * Math.cos(theta));
        camera.setLocation(new Vector3f(x, y, z).add(avatar.getWorldLocation()));
        camera.lookAt(avatar);
    }

    class OrbitAzimuthAction extends AbstractInputAction {
        private boolean moveLeft;

        public OrbitAzimuthAction(boolean moveLeft) {
            this.moveLeft = moveLeft;
        }

        @Override
        public void performAction(float time, Event event) {
            cameraAzimuth += moveLeft ? -ROTATION_SPEED : ROTATION_SPEED;
            cameraAzimuth = cameraAzimuth % 360;
            updateCameraPosition();
        }
    }

    class OrbitRadiusAction extends AbstractInputAction {
        private boolean zoomIn;

        public OrbitRadiusAction(boolean zoomIn) {
            this.zoomIn = zoomIn;
        }

        @Override
        public void performAction(float time, Event event) {
            cameraRadius += zoomIn ? -ZOOM_SPEED : ZOOM_SPEED;
            cameraRadius = Math.max(MIN_ZOOM, Math.min(MAX_ZOOM, cameraRadius));
            updateCameraPosition();
        }
    }

    class OrbitElevationAction extends AbstractInputAction {
        private boolean moveUp;

        public OrbitElevationAction(boolean moveUp) {
            this.moveUp = moveUp;
        }

        @Override
        public void performAction(float time, Event event) {
            cameraElevation += moveUp ? ELEVATION_SPEED : -ELEVATION_SPEED;
            cameraElevation = Math.max(0, Math.min(90, cameraElevation));
            updateCameraPosition();
        }
    }

    private class OrbitAzimuthControllerAction extends AbstractInputAction {
        @Override
        public void performAction(float time, Event event) {
            float value = event.getValue();
            if (Math.abs(value) > JOYSTICK_DEADZONE) {
                cameraAzimuth += value * ROTATION_SPEED * 2;
                cameraAzimuth = cameraAzimuth % 360;
                updateCameraPosition();
            }
        }
    }

    private class OrbitElevationControllerAction extends AbstractInputAction {
        @Override
        public void performAction(float time, Event event) {
            float value = event.getValue();
            if (Math.abs(value) > JOYSTICK_DEADZONE) {
                cameraElevation -= value * ELEVATION_SPEED * 2;
                cameraElevation = Math.max(0, Math.min(90, cameraElevation));
                updateCameraPosition();
            }
        }
    }

    private class OrbitRadiusControllerAction extends AbstractInputAction {
        @Override
        public void performAction(float time, Event event) {
            float value = event.getValue();
            if (Math.abs(value) > JOYSTICK_DEADZONE) {
                cameraRadius += value * ZOOM_SPEED;
                cameraRadius = Math.max(MIN_ZOOM, Math.min(MAX_ZOOM, cameraRadius));
                updateCameraPosition();
            }
        }
    }
}
