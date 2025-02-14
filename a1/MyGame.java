package a1;

import net.java.games.input.Component;
import tage.*;
import tage.input.InputManager;
import tage.input.action.AbstractInputAction;
import tage.shapes.*;
import org.joml.*;
import java.util.ArrayList;
import java.util.Random;

public class MyGame extends VariableFrameRateGame {
	private static Engine engine;
	private double lastFrameTime, currFrameTime, elapsedTime;
	private InputManager im;
	public boolean onDolphin;
	private final MyPlayer myPlayer;
    private final MyDolphin myDolphin;
	private final MySatellite satellite1, satellite2, satellite3;

	Camera cam;

	Line linxS, linyS, linzS;

	TextureImage satellite, close, detonated, disarmed;

	public MyGame() {
		super();
		myDolphin = new MyDolphin();
		myPlayer = new MyPlayer();
		satellite1 = new MySatellite();
		satellite2 = new MySatellite();
		satellite3 = new MySatellite();
	}

	public GameObject getAvatar() { return onDolphin ? MyDolphin.dol : MyPlayer.player; }

	public static void main(String[] args) {
		MyGame game = new MyGame();
		engine = new Engine(game);
		game.initializeSystem();
		game.game_loop();
	}

	@Override
	public void loadShapes() {
		myDolphin.loadShape();
		satellite1.loadShape(new Cube());
		satellite2.loadShape(new Sphere());
		satellite3.loadShape(new Torus());

		linxS = new Line(new Vector3f(0f,0f,0f), new Vector3f(3f,0f,0f));
		linyS = new Line(new Vector3f(0f,0f,0f), new Vector3f(0f,3f,0f));
		linzS = new Line(new Vector3f(0f,0f,0f), new Vector3f(0f,0f,-3f));
	}

	@Override
	public void loadTextures() {
		satellite = new TextureImage("satellite.jpg");
		close = new TextureImage("close.jpg");
		detonated = new TextureImage("explosion.jpg");
		disarmed = new TextureImage("emoji.jpg");

		myDolphin.loadTexture();
		satellite1.loadTexture(satellite, close, disarmed, detonated);
		satellite2.loadTexture(satellite, close, disarmed, detonated);
		satellite3.loadTexture(satellite, close, disarmed, detonated);
	}

	@Override
	public void buildObjects() {
		myPlayer.buildObject(3.0f, 0, 0, 1.0f);
		myDolphin.buildObject(0, 0, 0, 3.0f);

		float maxDistance = 25.0f;
		int numSatellites = 3;
		ArrayList<Vector3f> satellitePositions = new ArrayList<>();
		Random random = new Random();

		for (int i = 0; i < numSatellites; i++) {
			Vector3f newPosition;
			boolean collision;

			do {
				float x = (random.nextFloat() * 2 - 1) * maxDistance;
				float y = (random.nextFloat() * 2 - 1) * maxDistance;
				float z = (random.nextFloat() * 2 - 1) * maxDistance;
				newPosition = new Vector3f(x, y, z);

				collision = false;
				for (Vector3f pos : satellitePositions) {
					if (newPosition.distance(pos) < 2.0f) { // Ensuring no collision
						collision = true;
						break;
					}
				}
			} while (collision);

			satellitePositions.add(newPosition);
		}

		// Assign positions to satellites
		satellite1.buildObject(satellitePositions.get(0).x, satellitePositions.get(0).y, satellitePositions.get(0).z, 0.5f);
		satellite2.buildObject(satellitePositions.get(1).x, satellitePositions.get(1).y, satellitePositions.get(1).z, 1.0f);
		satellite3.buildObject(satellitePositions.get(2).x, satellitePositions.get(2).y, satellitePositions.get(2).z, 1.5f);

		// Add X, Y, -Z axes
		GameObject x = new GameObject(GameObject.root(), linxS);
		GameObject y = new GameObject(GameObject.root(), linyS);
		GameObject z = new GameObject(GameObject.root(), linzS);
		(x.getRenderStates()).setColor(new Vector3f(1f, 0f, 0f));
		(y.getRenderStates()).setColor(new Vector3f(0f, 1f, 0f));
		(z.getRenderStates()).setColor(new Vector3f(0f, 0f, 1f));
	}


	@Override
	public void initializeLights() {
		Light.setGlobalAmbient(0.5f, 0.5f, 0.5f);
        Light light1 = new Light();
		light1.setLocation(new Vector3f(5.0f, 4.0f, 2.0f));
		engine.getSceneGraph().addLight(light1);
	}

	private void initInputs() {
		im = engine.getInputManager();

		float moveSpeed = 0.05f;
		float turnSpeed = 0.03f;

		// Movement actions
		AvatarMoveAction moveForward = new AvatarMoveAction(this, moveSpeed, true);
		AvatarMoveAction moveBackward = new AvatarMoveAction(this, moveSpeed, false);
		AvatarTurnAction turnLeft = new AvatarTurnAction(this, turnSpeed, true);
		AvatarTurnAction turnRight = new AvatarTurnAction(this, turnSpeed, false);
		AvatarRotateAction rotateUp = new AvatarRotateAction(this, turnSpeed, true);
		AvatarRotateAction rotateDown = new AvatarRotateAction(this, turnSpeed, false);

		// Special action
		RideDolphinAction rideDolphin = new RideDolphinAction(this, MyDolphin.dol, MyPlayer.player);

		// Key bindings
		associateKeyAction(Component.Identifier.Key.W, moveForward, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		associateKeyAction(Component.Identifier.Key.S, moveBackward, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		associateKeyAction(Component.Identifier.Key.A, turnLeft, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		associateKeyAction(Component.Identifier.Key.D, turnRight, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		associateKeyAction(Component.Identifier.Key.UP, rotateUp, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		associateKeyAction(Component.Identifier.Key.DOWN, rotateDown, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		associateKeyAction(Component.Identifier.Key.SPACE, rideDolphin, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
	}

	private void associateKeyAction(Component.Identifier.Key key, AbstractInputAction action, InputManager.INPUT_ACTION_TYPE type) {
		im.associateActionWithAllKeyboards(key, action, type);
	}

	@Override
	public void initializeGame() {
		lastFrameTime = System.currentTimeMillis();
		currFrameTime = System.currentTimeMillis();
		elapsedTime = 0.0;
		onDolphin = false;
		engine.getRenderSystem().setWindowDimensions(1900, 1000);
		cam = engine.getRenderSystem().getViewport("MAIN").getCamera();
		cam.setLocation(new Vector3f(0, 0, 5));
		initInputs();
	}

	@Override
	public void update() {
		lastFrameTime = currFrameTime;
		currFrameTime = System.currentTimeMillis();
		double deltaTime = (currFrameTime - lastFrameTime) / 1000.0;
		elapsedTime += deltaTime;
		myDolphin.update(deltaTime);
		im.update((float)elapsedTime);
		updateCamera();
		updateSatelliteStates();
		updatePlayerCoords();
	}

	private void updatePlayerCoords() { if (onDolphin) MyPlayer.player.setLocalLocation(MyDolphin.dol.getLocalLocation()); }

	private void updateSatelliteStates() {
		MySatellite[] satellites = {satellite1, satellite2, satellite3};
		float playerDistance;

		for (MySatellite satellite : satellites) {
			playerDistance = MyPlayer.player.getLocalLocation().distance(satellite.satellite.getLocalLocation());

			if (playerDistance < 2.0f) {
				if (!satellite.isDisarmed() && !satellite.isDetonated()) {
					satellite.setDetonated(onDolphin);
					satellite.setDisarmed(!onDolphin);
				}
			}
			satellite.setClose(playerDistance < 4.0f);

			satellite.updateTexture();
		}
	}


	private void updateCamera() {
		cam = engine.getRenderSystem().getViewport("MAIN").getCamera();
		Vector3f loc, fwd, up, right;

		loc = onDolphin ? myDolphin.getLocation() : myPlayer.getLocation();
		fwd = onDolphin ? myDolphin.getForwardVector() : myPlayer.getForwardVector();
		up = onDolphin ? myDolphin.getUpVector() : myPlayer.getUpVector();
		right = onDolphin ? myDolphin.getRightVector() : myPlayer.getRightVector();

		cam.setU(right);
		cam.setV(up);
		cam.setN(fwd);
		if (onDolphin) {
			cam.setLocation(loc.add(up.mul(1.3f)).add(fwd.mul(-2.5f)));
		} else {
			cam.setLocation(loc);
		}
	}
}
