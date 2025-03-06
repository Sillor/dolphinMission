package a2;

import net.java.games.input.Component;
import tage.*;
import tage.input.InputManager;
import tage.input.action.AbstractInputAction;
import tage.nodeControllers.HideController;
import tage.nodeControllers.RotationController;
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
	private ManualDiamond manualDiamondS;
	private GameObject manualDiamond1, manualDiamond2, manualDiamond3;
	private boolean paused;
	private CameraOrbit3D cameraOrbit;
	private OverheadCamera overheadCamera;
	private Plane planeS;
	private GameObject plane;
	private RotationController rc1;
	private RotationController rc2;
	private RotationController rc3;
	private HideController hc;

	Camera mainCam, overheadCam;

	Line linxS, linyS, linzS;

	TextureImage carbon, stainedsurface, greywall, detonated, disarmedCube, disarmedSphere, disarmedTorus, reddot, driedlava, redtextile, manualDiamondT;

	MyHUDmanager hud;

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
	public void createViewports()
	{ (engine.getRenderSystem()).addViewport("LEFT",0,0,1f,1f);
		(engine.getRenderSystem()).addViewport("RIGHT",.75f,0,.25f,.25f);
		Viewport rightVp =
				(engine.getRenderSystem()).getViewport("RIGHT");
		rightVp.setHasBorder(true);
		rightVp.setBorderWidth(4);
		rightVp.setBorderColor(0.0f, 1.0f, 0.0f);
	}

	@Override
	public void loadShapes() {
		myDolphin.loadShape();
		satellite1.loadShape(new Cube());
		satellite2.loadShape(new Sphere());
		satellite3.loadShape(new Torus());
		manualDiamondS = new ManualDiamond();
		planeS = new Plane();

		linxS = new Line(new Vector3f(0f,0f,0f), new Vector3f(12f,0f,0f));
		linyS = new Line(new Vector3f(0f,0f,0f), new Vector3f(0f,12f,0f));
		linzS = new Line(new Vector3f(0f,0f,0f), new Vector3f(0f,0f,-12f));
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
		manualDiamondT = new TextureImage("manualDiamond.jpg");

		myDolphin.loadTexture();
		satellite1.loadTexture(greywall, reddot, disarmedCube, detonated);
		satellite2.loadTexture(carbon, redtextile, disarmedSphere, detonated);
		satellite3.loadTexture(stainedsurface, driedlava, disarmedTorus, detonated);
	}

	@Override
	public void buildObjects() {
		myPlayer.buildObject(3.0f,0,0,1.0f);
		myDolphin.buildObject(0,0,0,3.0f);
		satellite1.buildObject(0.5f, 15, 20);
		satellite2.buildObject(1.0f, 15, 20);
		satellite3.buildObject(1.5f, 15, 20);
		satellite3.satellite.getRenderStates().setTiling(2);

		manualDiamond1 = new GameObject(GameObject.root(), manualDiamondS, manualDiamondT);
		manualDiamond1.setLocalTranslation(new Matrix4f().translation(satellite1.satellite.getLocalLocation().x, satellite1.satellite.getLocalLocation().y + 1.0f, satellite1.satellite.getLocalLocation().z));
		manualDiamond1.setLocalScale(new Matrix4f().scaling(0.2f));
		manualDiamond2 = new GameObject(GameObject.root(), manualDiamondS, manualDiamondT);
		manualDiamond2.setLocalTranslation(new Matrix4f().translation(satellite2.satellite.getLocalLocation().x, satellite2.satellite.getLocalLocation().y + 1.0f, satellite2.satellite.getLocalLocation().z));
		manualDiamond2.setLocalScale(new Matrix4f().scaling(0.2f));
		manualDiamond3 = new GameObject(GameObject.root(), manualDiamondS, manualDiamondT);
		manualDiamond3.setLocalTranslation(new Matrix4f().translation(satellite3.satellite.getLocalLocation().x, satellite3.satellite.getLocalLocation().y + 1.0f, satellite3.satellite.getLocalLocation().z));
		manualDiamond3.setLocalScale(new Matrix4f().scaling(0.2f));

		// add X,Y,-Z axes
		GameObject x = new GameObject(GameObject.root(), linxS);
		GameObject y = new GameObject(GameObject.root(), linyS);
		GameObject z = new GameObject(GameObject.root(), linzS);
		(x.getRenderStates()).setColor(new Vector3f(1f,0f,0f));
		(y.getRenderStates()).setColor(new Vector3f(0f,1f,0f));
		(z.getRenderStates()).setColor(new Vector3f(0f,0f,1f));

		plane = new GameObject(GameObject.root(), planeS, carbon);
		plane.getRenderStates().setTiling(2);
		plane.setLocalScale(new Matrix4f().scaling(100f));
		plane.setLocalTranslation(new Matrix4f().translation(0, -5, 0));
	}

	@Override
	public void initializeLights() {
		Light.setGlobalAmbient(0.4f, 0.4f, 0.4f);

		Light light1 = new Light();
		Light light2 = new Light();
		Light light3 = new Light();

		Vector3f offset = new Vector3f(0.0f, 2.0f, 0.0f);

		light1.setLocation(satellite1.satellite.getLocalLocation().add(offset));
		light1.setAmbient(0.1f, 0.02f, 0.02f);
		light1.setDiffuse(0.8f, 0.25f, 0.25f);
		light1.setSpecular(0.5f, 0.15f, 0.15f);

		light2.setLocation(satellite2.satellite.getLocalLocation().add(offset));
		light2.setAmbient(0.02f, 0.1f, 0.02f);
		light2.setDiffuse(0.25f, 0.8f, 0.25f);
		light2.setSpecular(0.15f, 0.5f, 0.15f);

		light3.setLocation(satellite3.satellite.getLocalLocation().add(offset));
		light3.setAmbient(0.02f, 0.02f, 0.1f);
		light3.setDiffuse(0.25f, 0.25f, 0.8f);
		light3.setSpecular(0.15f, 0.15f, 0.5f);

		engine.getSceneGraph().addLight(light1);
		engine.getSceneGraph().addLight(light2);
		engine.getSceneGraph().addLight(light3);
	}

	private void initInputs() {
		float moveSpeed = 5.0f;
		float turnSpeed = 1.0f;

		// Movement actions
		AvatarMoveAction moveForwardKb = new AvatarMoveAction(this, MyDolphin.dol, false, moveSpeed, true);
		AvatarMoveAction moveBackwardKb = new AvatarMoveAction(this,MyDolphin.dol, false, moveSpeed, false);
		AvatarTurnAction turnLeftKb = new AvatarTurnAction(this, false, turnSpeed, true);
		AvatarTurnAction turnRightKb = new AvatarTurnAction(this, false, turnSpeed, false);
		AvatarMoveAction moveGp = new AvatarMoveAction(this, MyDolphin.dol, true, moveSpeed, true);
		AvatarTurnAction turnGp = new AvatarTurnAction(this, true, turnSpeed, true);
//		AvatarRotateAction rotateUp = new AvatarRotateAction(this, turnSpeed, true);
//		AvatarRotateAction rotateDown = new AvatarRotateAction(this, turnSpeed, false);

		// Special action
		RideDolphinAction rideDolphin = new RideDolphinAction(this, MyDolphin.dol, MyPlayer.player);

		// Key bindings
		associateKeyAction(Component.Identifier.Key.W, moveForwardKb, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		associateKeyAction(Component.Identifier.Key.S, moveBackwardKb, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		associateKeyAction(Component.Identifier.Key.A, turnLeftKb, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		associateKeyAction(Component.Identifier.Key.D, turnRightKb, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
//		associateKeyAction(Component.Identifier.Key.UP, rotateUp, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
//		associateKeyAction(Component.Identifier.Key.DOWN, rotateDown, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		associateKeyAction(Component.Identifier.Key.SPACE, rideDolphin, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

		// Gamepad bindings
		associateGamepadAction(Component.Identifier.Axis.Y, moveGp, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		associateGamepadAction(Component.Identifier.Axis.X, turnGp, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	}

	private void associateKeyAction(Component.Identifier.Key key, AbstractInputAction action, InputManager.INPUT_ACTION_TYPE type) {
		im.associateActionWithAllKeyboards(key, action, type);
	}

	private void associateGamepadAction(Component.Identifier.Axis axis, AbstractInputAction action, InputManager.INPUT_ACTION_TYPE type) {
		im.associateActionWithAllGamepads(axis, action, type);
	}

	@Override
	public void initializeGame() {
		im = engine.getInputManager();
		lastFrameTime = System.currentTimeMillis();
		currFrameTime = System.currentTimeMillis();
		elapsedTime = 0.0;
		onDolphin = true;
		engine.getRenderSystem().setWindowDimensions(1900, 1000);
		mainCam = engine.getRenderSystem().getViewport("LEFT").getCamera();
		overheadCam = engine.getRenderSystem().getViewport("RIGHT").getCamera();
		cameraOrbit = new CameraOrbit3D(mainCam, MyDolphin.dol, im.getKeyboardName(), null, engine);
		overheadCamera = new OverheadCamera(overheadCam, im.getKeyboardName(), null, engine);
		initInputs();
		hud = new MyHUDmanager(engine, this);
		rc1 = new RotationController(engine, new Vector3f(0,1,0), 0.001f);
		rc2 = new RotationController(engine, new Vector3f(0,1,0), 0.001f);
		rc3= new RotationController(engine, new Vector3f(0,1,0), 0.001f);
		rc1.addTarget(satellite1.satellite);
		rc2.addTarget(satellite2.satellite);
		rc3.addTarget(satellite3.satellite);
		hc = new HideController();
		hc.addTarget(myDolphin.dol);
		(engine.getSceneGraph()).addNodeController(rc1);
		(engine.getSceneGraph()).addNodeController(rc2);
		(engine.getSceneGraph()).addNodeController(rc3);
		(engine.getSceneGraph()).addNodeController(hc);
	}

	@Override
	public void update() {
		lastFrameTime = currFrameTime;
		currFrameTime = System.currentTimeMillis();
		double deltaTime = 0;
		if (!paused) {
			deltaTime = (currFrameTime - lastFrameTime) / 1000.0;
			elapsedTime += deltaTime;
		}
		myDolphin.update(deltaTime);
		manualDiamond1.setLocalRotation(new Matrix4f().rotationY((float)elapsedTime));
		manualDiamond2.setLocalRotation(new Matrix4f().rotationY((float)elapsedTime));
		manualDiamond3.setLocalRotation(new Matrix4f().rotationY((float)elapsedTime));
		im.update((float)deltaTime);
		updateSatelliteStates();
		updatePlayerCoords();
		cameraOrbit.updateCameraPosition();
		overheadCamera.updateCameraPosition();
		hud.update();
	}

	private void updatePlayerCoords() { if (onDolphin) MyPlayer.player.setLocalLocation(MyDolphin.dol.getLocalLocation()); }

	private void updateSatelliteStates() {
		MySatellite[] satellites = {satellite1, satellite2, satellite3};
		GameObject[] manualDiamonds = {manualDiamond1, manualDiamond2, manualDiamond3};
		RotationController[] rcs = {rc1, rc2, rc3};
		float playerDistance;

		for (int i = 0; i < satellites.length; i++) {
			MySatellite satellite = satellites[i];
			playerDistance = MyPlayer.player.getLocalLocation().distance(satellite.satellite.getLocalLocation());

			if (playerDistance < 2.5f) {
				if (!satellite.isDisarmed() && !satellite.isDetonated()) {
					if (onDolphin) {
						satellite.setDetonated(true);
						satellite.setDisarmed(false);
						hud.setGameOver();
						hc.toggle();
						paused = true;
					} else {
						satellite.setDetonated(false);
						satellite.setDisarmed(true);
						hud.incrementScore();
						rcs[i].toggle();
						manualDiamonds[i].setParent(myDolphin.dol);
						manualDiamonds[i].setLocalTranslation(myDolphin.dol.getLocalTranslation().translation(1f, i * 0.3f + 0.2f, 0));
						manualDiamonds[i].setLocalScale(new Matrix4f().scaling(0.02f));
						manualDiamonds[i].propagateTranslation(true);
					}
				}
			}
			satellite.setClose(playerDistance < 7.0f);

			satellite.updateTexture();
		}
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused() {
		this.paused = true;
	}

	public MySatellite getSatellite1() {
		return satellite1;
	}

	public MySatellite getSatellite2() {
		return satellite2;
	}

	public MySatellite getSatellite3() {
		return satellite3;
	}
}
