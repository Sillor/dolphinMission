package a1;

import tage.*;
import tage.input.InputManager;
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
	}

	@Override
	public void loadTextures() {
		myDolphin.loadTexture();
		TextureImage brickTexture = new TextureImage("brick1.jpg");
		satellite1.loadTexture(brickTexture);
		satellite2.loadTexture(brickTexture);
		satellite3.loadTexture(brickTexture);
	}

	@Override
	public void buildObjects() {
		myPlayer.buildObject(3.0f,0,0,1.0f);
		myDolphin.buildObject(0,0,0,3.0f);

		float distance = 10.0f;
		satellite1.buildObject(-distance, 0, -distance, 0.5f);
		satellite2.buildObject(distance, 0, -distance, 1.0f);
		satellite3.buildObject(0, 0, distance, 1.5f);
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
		AvatarMoveAction avatarFwdAction = new AvatarMoveAction(this, 0.05f, true);
		AvatarMoveAction avatarBckAction = new AvatarMoveAction(this, 0.05f, false);
		AvatarTurnAction dolphinLeft = new AvatarTurnAction(this, 0.01f, true);
		AvatarTurnAction dolphinRight = new AvatarTurnAction(this, 0.01f, false);
		RideDolphinAction rideDolphinAction = new RideDolphinAction(this, MyDolphin.dol, MyPlayer.player);

		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.W, avatarFwdAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.S, avatarBckAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.A, dolphinLeft,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.D, dolphinRight,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.E, rideDolphinAction,
				InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
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
