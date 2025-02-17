package a1;

import net.java.games.input.Component;
import tage.*;
import tage.input.InputManager;
import tage.input.action.AbstractInputAction;
import tage.shapes.*;
import org.joml.*;

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

	TextureImage carbon, stainedsurface, greywall, close, detonated, disarmedCube, disarmedSphere, disarmedTorus, reddot, driedlava, redtextile;

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
		carbon = new TextureImage("carbon.jpg");
		stainedsurface = new TextureImage("stainedsurface.jpg");
		greywall = new TextureImage("greywall.jpg");
		detonated = new TextureImage("explosion.jpg");
		disarmedCube = new TextureImage("disarmedCube.jpg");
		disarmedSphere = new TextureImage("disarmedSphere.jpg");
		disarmedTorus = new TextureImage("disarmedTorus.jpg");
		reddot = new TextureImage("reddot.jpg");
		driedlava = new TextureImage("driedlava.jpg");
		redtextile = new TextureImage("redtextile.jpg");

		myDolphin.loadTexture();
		satellite1.loadTexture(greywall, reddot, disarmedCube, detonated);
		satellite2.loadTexture(carbon, redtextile, disarmedSphere, detonated);
		satellite3.loadTexture(stainedsurface, driedlava, disarmedTorus, detonated);
	}

	@Override
	public void buildObjects() {
		myPlayer.buildObject(3.0f,0,0,1.0f);
		myDolphin.buildObject(0,0,0,3.0f);
\
		satellite1.buildObject(0.5f, 10, 10);
		satellite2.buildObject(1.0f, 10, 10);
		satellite3.buildObject(1.5f, 10, 10);

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

		float moveSpeed = 5.0f;
		float turnSpeed = 1.0f;

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
		im.update((float)deltaTime);
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

			if (playerDistance < 2.52f) {
				if (!satellite.isDisarmed() && !satellite.isDetonated()) {
					satellite.setDetonated(onDolphin);
					satellite.setDisarmed(!onDolphin);
				}
			}
			satellite.setClose(playerDistance < 5.0f);

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
