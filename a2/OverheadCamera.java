package a2;

import net.java.games.input.Component;
import org.joml.Vector3f;
import tage.*;
import tage.input.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;

public class OverheadCamera {
    private final Engine engine;
    private final Camera camera;
    private Vector3f cameraPosition; // Independent camera position

    private float cameraHeight = 10.0f; // Initial height
    private float cameraXOffset = 0.0f; // Panning X
    private float cameraZOffset = 0.0f; // Panning Z

    private static final float PAN_SPEED = 0.2f;
    private static final float ZOOM_SPEED = 0.1f;
    private static final float MIN_ZOOM = 2.0f;
    private static final float MAX_ZOOM = 15.0f;
    private static final float JOYSTICK_DEADZONE = 0.2f;

    public OverheadCamera(Camera cam, String kbName, String gpName, Engine e) {
        engine = e;
        camera = cam;
        cameraPosition = new Vector3f(0, cameraHeight, 0); // Start at (0, height, 0)
        setupInputs(kbName, gpName);
        updateCameraPosition();
    }

    private void setupInputs(String kb, String gp) {
        InputManager im = engine.getInputManager();

        // Keyboard Controls for Panning
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.UP,
                new CameraPanAction(0, -PAN_SPEED), InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.DOWN,
                new CameraPanAction(0, PAN_SPEED), InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.LEFT,
                new CameraPanAction(-PAN_SPEED, 0), InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.RIGHT,
                new CameraPanAction(PAN_SPEED, 0), InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

        // Keyboard Controls for Zooming
        im.associateActionWithAllKeyboards(Component.Identifier.Key.LBRACKET,
                new CameraZoomAction(true), InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(Component.Identifier.Key.RBRACKET,
                new CameraZoomAction(false), InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

        // Gamepad Controls
        if (gp != null) {
            im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.X,
                    new CameraPanGamepadAction(true), InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
            im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.Y,
                    new CameraPanGamepadAction(false), InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
            im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.Z,
                    new CameraZoomGamepadAction(), InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        }
    }

    public void updateCameraPosition() {
        camera.setLocation(new Vector3f(cameraXOffset, cameraHeight, cameraZOffset));
        camera.lookAt(new Vector3f(cameraXOffset, 0, cameraZOffset)); // Look downward
    }

    // Keyboard-based Panning
    private class CameraPanAction extends AbstractInputAction {
        private final float moveX;
        private final float moveZ;

        public CameraPanAction(float moveX, float moveZ) {
            this.moveX = moveX;
            this.moveZ = moveZ;
        }

        @Override
        public void performAction(float time, Event evt) {
            cameraXOffset += moveX;
            cameraZOffset += moveZ;
            updateCameraPosition();
        }
    }

    // Keyboard-based Zooming
    private class CameraZoomAction extends AbstractInputAction {
        private final boolean zoomIn;

        public CameraZoomAction(boolean zoomIn) {
            this.zoomIn = zoomIn;
        }

        @Override
        public void performAction(float time, Event evt) {
            cameraHeight += zoomIn ? -ZOOM_SPEED : ZOOM_SPEED;
            cameraHeight = Math.max(MIN_ZOOM, Math.min(MAX_ZOOM, cameraHeight));
            updateCameraPosition();
        }
    }

    // Gamepad-based Panning
    private class CameraPanGamepadAction extends AbstractInputAction {
        private final boolean isXAxis;

        public CameraPanGamepadAction(boolean isXAxis) {
            this.isXAxis = isXAxis;
        }

        @Override
        public void performAction(float time, Event evt) {
            float value = evt.getValue();
            if (Math.abs(value) > JOYSTICK_DEADZONE) {
                if (isXAxis) {
                    cameraXOffset += value * PAN_SPEED;
                } else {
                    cameraZOffset += value * PAN_SPEED;
                }
                updateCameraPosition();
            }
        }
    }

    // Gamepad-based Zooming
    private class CameraZoomGamepadAction extends AbstractInputAction {
        @Override
        public void performAction(float time, Event evt) {
            float value = evt.getValue();
            if (Math.abs(value) > JOYSTICK_DEADZONE) {
                cameraHeight += value * ZOOM_SPEED;
                cameraHeight = Math.max(MIN_ZOOM, Math.min(MAX_ZOOM, cameraHeight));
                updateCameraPosition();
            }
        }
    }
}
