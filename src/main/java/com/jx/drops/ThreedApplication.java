package com.jx.drops;

import java.util.concurrent.atomic.AtomicInteger;

import org.fxyz.cameras.CameraTransformer;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.AmbientLight;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public abstract class ThreedApplication extends Application {
	// The CameraTransformer is Xform from JavaFX 3d tutorial
	// http://docs.oracle.com/javafx/8/3d_graphics/camera.htm
	private final CameraTransformer cameraTransform = new CameraTransformer();
	protected final Rotate rotateY = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);

	private PerspectiveCamera camera;

	private final double sceneWidth = 1024;
	private final double sceneHeight = 800;

	private long lastEffect;

	protected void addRotate(Node node) {
		node.getTransforms().addAll(new Rotate(0, Rotate.X_AXIS), rotateY);
	}

	protected void makeYUp(Camera camera) {
		// cameraTransform.rx.setAngle(180.0);
		// cameraTransform.rz.setAngle(180.0);
		// avoidAutoPivoting
		camera.getTransforms().add(new Rotate(180.0, Rotate.Z_AXIS));
	}

	@Override
	public void start(Stage stage) throws Exception {
		camera = new PerspectiveCamera(true);
		// setup camera transform for rotational support
		camera.setNearClip(0.1);
		camera.setFarClip(10000.0);
		camera.setTranslateZ(-20);
		makeYUp(camera);

		cameraTransform.setTranslate(0, 0, 0);
		cameraTransform.getChildren().add(camera);
		// cameraTransform.ry.setAngle(-45.0);
		// cameraTransform.rx.setAngle(-10.0);

		// add a Point Light for better viewing of the grid coordinate system
		PointLight point = new PointLight(Color.LIGHTSTEELBLUE);
		AmbientLight ambient = new AmbientLight(Color.LIGHTSKYBLUE);
		cameraTransform.getChildren().addAll(point, ambient);
		point.setTranslateX(camera.getTranslateX());
		point.setTranslateY(camera.getTranslateY());
		point.setTranslateZ(camera.getTranslateZ());

		Group group = new Group(cameraTransform);
		Group sceneRoot = new Group(group);
		Scene scene = new Scene(sceneRoot, sceneWidth, sceneHeight, true, SceneAntialiasing.BALANCED);
		this.pointLight = point;
		this.ambientLight = ambient;
		configSceneColor(scene, point, ambient, cameraTransform);

		scene.setCamera(camera);

		makeObjects(group);
		setActions(scene);
		lastEffect = System.nanoTime();
		AnimationTimer timerEffect = makeAnimation();
		stage.setScene(scene);
		stage.show();
		timerEffect.start();
	}

	protected PointLight pointLight;
	protected AmbientLight ambientLight;

	protected void configSceneColor(Scene scene, PointLight point, AmbientLight ambient, CameraTransformer lights) {
		scene.setFill(Color.BLACK);
	}

	private double mousePosX;
	private double mousePosY;
	private double mouseOldX;
	private double mouseOldY;
	private double mouseDeltaX;
	private double mouseDeltaY;

	protected void onKeyCode(KeyCode code) {}

	private void setActions(Scene scene) {
		// First person shooter keyboard movement
		scene.setOnKeyPressed(event -> {
			// double change = 10.0;
			double change = 2;
			// Add shift modifier to simulate "Running Speed"
			if (event.isShiftDown()) {
				change = 50.0;
			}
			// What key did the user press?
			KeyCode keycode = event.getCode();
			// Step 2c: Add Zoom controls
			if (keycode == KeyCode.W) {
				camera.setTranslateZ(camera.getTranslateZ() + change);
			}
			if (keycode == KeyCode.S) {
				camera.setTranslateZ(camera.getTranslateZ() - change);
			}
			// Step 2d: Add Strafe controls
			if (keycode == KeyCode.A) {
				camera.setTranslateX(camera.getTranslateX() - change);
			}
			if (keycode == KeyCode.D) {
				camera.setTranslateX(camera.getTranslateX() + change);
			}
			onKeyCode(keycode);
		});

		scene.setOnMousePressed((MouseEvent me) -> {
			mousePosX = me.getSceneX();
			mousePosY = me.getSceneY();
			mouseOldX = me.getSceneX();
			mouseOldY = me.getSceneY();
		});
		scene.setOnMouseDragged((MouseEvent me) -> {
			mouseOldX = mousePosX;
			mouseOldY = mousePosY;
			mousePosX = me.getSceneX();
			mousePosY = me.getSceneY();
			mouseDeltaX = (mousePosX - mouseOldX);
			mouseDeltaY = (mousePosY - mouseOldY);

			double modifier = 10.0;
			double modifierFactor = 0.1;

			if (me.isControlDown()) {
				modifier = 0.1;
			}
			if (me.isShiftDown()) {
				modifier = 50.0;
			}
			if (me.isPrimaryButtonDown()) {
				cameraTransform.ry.setAngle(
						((cameraTransform.ry.getAngle() + mouseDeltaX * modifierFactor * modifier * 2.0) % 360 + 540)
								% 360 - 180); // +
				cameraTransform.rx.setAngle(
						((cameraTransform.rx.getAngle() - mouseDeltaY * modifierFactor * modifier * 2.0) % 360 + 540)
								% 360 - 180); // -
			} else if (me.isSecondaryButtonDown()) {
				double z = camera.getTranslateZ();
				double newZ = z + mouseDeltaX * modifierFactor * modifier;
				camera.setTranslateZ(newZ);
			} else if (me.isMiddleButtonDown()) {
				cameraTransform.t.setX(cameraTransform.t.getX() + mouseDeltaX * modifierFactor * modifier * 0.3); // -
				cameraTransform.t.setY(cameraTransform.t.getY() + mouseDeltaY * modifierFactor * modifier * 0.3); // -
			}
		});
	}

	private AnimationTimer makeAnimation() {
		AtomicInteger count = new AtomicInteger();
		AnimationTimer timerEffect = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (now > lastEffect + 1_00_000_000l) {
					makeAnimation(count);
					count.getAndIncrement();
					lastEffect = now;
				}
			}
		};
		return timerEffect;
	}

	protected void makeAnimation(AtomicInteger count) {}

	abstract protected void makeObjects(Group group) throws Exception;
}
