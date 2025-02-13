package a1;

import tage.*;
import tage.input.InputManager;
import tage.input.action.AbstractInputAction;
import tage.shapes.*;
import org.joml.*;

public class MyGame extends VariableFrameRateGame {
	private static Engine engine;
	private double lastFrameTime;
	private double currFrameTime;
	private double elapsedTime;
	private InputManager im;
	public boolean onDolphin;
	private final MyPlayer myPlayer;
    private final MyDolphin myDolphin;
	private final MySatellite satellite1;
	private final MySatellite satellite2;
	private final MySatellite satellite3;
	Camera cam;

	Line linxS;
	Line linyS;
	Line linzS;

	TextureImage satellite;
	TextureImage close;
	TextureImage detonated;
	TextureImage disarmed;

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
		myPlayer.buildObject(3.0f,0,0,1.0f);
		myDolphin.buildObject(0,0,0,3.0f);

		float distance = 10.0f;
		satellite1.buildObject(-distance, 0, -distance, 0.5f);
		satellite2.buildObject(distance, 0, -distance, 1.0f);
		satellite3.buildObject(0, 0, distance, 1.5f);

		// add X,Y,-Z axes
		GameObject x = new GameObject(GameObject.root(), linxS);
		GameObject y = new GameObject(GameObject.root(), linyS);
		GameObject z = new GameObject(GameObject.root(), linzS);
		(x.getRenderStates()).setColor(new Vector3f(1f,0f,0f));
		(y.getRenderStates()).setColor(new Vector3f(0f,1f,0f));
		(z.getRenderStates()).setColor(new Vector3f(0f,0f,1f));
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

		// Movement actions
		AvatarMoveAction moveForward = new AvatarMoveAction(this, 0.05f, true);
		AvatarMoveAction moveBackward = new AvatarMoveAction(this, 0.05f, false);
		AvatarTurnAction turnLeft = new AvatarTurnAction(this, 0.03f, true);
		AvatarTurnAction turnRight = new AvatarTurnAction(this, 0.03f, false);
		AvatarRotateAction rotateUp = new AvatarRotateAction(this, 0.01f, true);
		AvatarRotateAction rotateDown = new AvatarRotateAction(this, 0.01f, false);

		// Special action
		RideDolphinAction rideDolphin = new RideDolphinAction(this, MyDolphin.dol, MyPlayer.player);

		// Key bindings
		associateKeyAction(net.java.games.input.Component.Identifier.Key.W, moveForward, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		associateKeyAction(net.java.games.input.Component.Identifier.Key.S, moveBackward, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		associateKeyAction(net.java.games.input.Component.Identifier.Key.A, turnLeft, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		associateKeyAction(net.java.games.input.Component.Identifier.Key.D, turnRight, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		associateKeyAction(net.java.games.input.Component.Identifier.Key.UP, rotateUp, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		associateKeyAction(net.java.games.input.Component.Identifier.Key.DOWN, rotateDown, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		associateKeyAction(net.java.games.input.Component.Identifier.Key.SPACE, rideDolphin, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
	}

	private void associateKeyAction(net.java.games.input.Component.Identifier.Key key, AbstractInputAction action, InputManager.INPUT_ACTION_TYPE type) {
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

	private void updatePlayerCoords() {
		if (onDolphin) {
			MyPlayer.player.setLocalLocation(MyDolphin.dol.getLocalLocation());
		}
	}

	private void updateSatelliteStates() {
		MySatellite[] satellites = {satellite1, satellite2, satellite3};
		float playerDistance;

		for (MySatellite satellite : satellites) {
			playerDistance = MyPlayer.player.getLocalLocation().distance(satellite.satellite.getLocalLocation());

			if (playerDistance < 2.0f) {
				if (!satellite.isDisarmed()) {
					satellite.setDetonated(onDolphin && !satellite.isDetonated());
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
		if (onDolphin) {
			loc = myDolphin.getLocation();
			fwd = myDolphin.getForwardVector();
			up = myDolphin.getUpVector();
			right = myDolphin.getRightVector();
		} else {
			loc = myPlayer.getLocation();
			fwd = myPlayer.getForwardVector();
			up = myPlayer.getUpVector();
			right = myPlayer.getRightVector();
		}
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
