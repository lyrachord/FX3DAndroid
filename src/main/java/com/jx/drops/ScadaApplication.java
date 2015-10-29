package com.jx.drops;

import static eu.mihosoft.vrl.v3d.Transform.unity;
import static javafx.scene.paint.Color.*;
import static javafx.scene.transform.Rotate.*;
import static java.lang.Math.*;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

import org.fxyz.cameras.CameraTransformer;
import org.fxyz.geometry.Point3D;
import org.fxyz.shapes.Capsule;
import org.fxyz.shapes.Torus;
import org.fxyz.shapes.primitives.BezierMesh;
import org.fxyz.shapes.primitives.CSGMesh;
import org.fxyz.shapes.primitives.FrustumMesh;
import org.fxyz.shapes.primitives.Text3DMesh;
import org.fxyz.shapes.primitives.helper.BezierHelper;
import org.fxyz.utils.DefaultColorPalette;
import org.fxyz.utils.Patterns.CarbonPatterns;

import com.jx.ui.fx.FXUtil;
import eu.mihosoft.vrl.v3d.CSG;
import eu.mihosoft.vrl.v3d.Cube;
import eu.mihosoft.vrl.v3d.Extrude;
import eu.mihosoft.vrl.v3d.Sphere;
import eu.mihosoft.vrl.v3d.Transform;
import eu.mihosoft.vrl.v3d.Vector3d;
import java8.lang.Iterables;
import javafx.collections.ObservableList;
import javafx.scene.AmbientLight;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

public class ScadaApplication extends ThreedApplication {
	public static void main(String[] args) {
		launch(args);
	}

	private ArrayList<BezierMesh> beziers = new ArrayList<>();
	private Translate needleTranslate;
	private Rotate needleRotate;

	@Override
	protected void makeAnimation(AtomicInteger count) {
		int n = count.get();
		double deg = n * 10;
		needleRotate.setAngle(deg);
		double rad = Math.toRadians(deg);
		needleTranslate.setY(-Math.cos(rad) * 0.6);
		needleTranslate.setX(Math.sin(rad) * 0.6);

		Function<Number, Number> func = t -> {
			// System.out.println(Math.pow(t.doubleValue(), (count.get() % 5d)));
			return Math.pow(t.doubleValue(), (n % 10));
			// 0.2-0.7
			// (0.2+t.doubleValue()/2)+
			// return (count.get() % 6);
		};
		Iterables.forEach(beziers, b -> b.setFunction(func));
		// Consumer<? super BezierMesh> consumer = bezier -> bezier.setFunction(func);
		// Consumer<? super BezierMesh> consumer = bezier -> bezier.setTextureModeVertices1D(ColorPalette.RGB, 1000,
		// func);
		// t->t.doubleValue()*(count.get() %1000));
		// t->bezier.getTau(t.doubleValue()));
		// int power = count.get() % 10;
		// Consumer<? super BezierMesh> consumer = bezier -> bezier.setDensity(p -> power * (-p.x + power * p.y * p.y));
		// beziers.stream().forEach(consumer);
	}

	// b0c4de steelblue
	Color materialColor = rgb(220, 220, 220);
	PhongMaterial material = new PhongMaterial();
	PhongMaterial material2 = new PhongMaterial();
	PhongMaterial material3 = new PhongMaterial();
	double hue = 0;
	double saturation = 0;
	double brightness = 0.05;
	double opacity = 0.5;
	Consumer<Color> operation;

	@Override
	protected void onKeyCode(KeyCode keyCode) {
		if (keyCode == KeyCode.P) operation = c -> pointLight.setColor(c);
		else if (keyCode == KeyCode.I) operation = c -> ambientLight.setColor(c);
		else if (keyCode == KeyCode.M) operation = c -> setMaterial(material, c);
		else if (keyCode == KeyCode.B) {
			brightness -= 0.05;
			if (brightness < 0) brightness = 1;
		} else if (keyCode == KeyCode.T) {
			saturation += 0.1;
			if (saturation > 1) saturation = 0;
		} else if (keyCode == KeyCode.H) {
			hue += 3;
			if (hue > 360) hue %= 360;
		}
		Color color = Color.hsb(hue, saturation, brightness, opacity);
		System.out.printf("hsb: %.2f %.2f %.2f\n", hue, saturation, brightness);
		if (operation != null) operation.accept(color);
		// else// setMaterial(material, color);
	}

	@Override
	protected void configSceneColor(Scene scene, PointLight point, AmbientLight ambient, CameraTransformer lights) {
		scene.setFill(GRAY);
		// int c = 128;
		// int d = 192;
		// point.setColor(rgb(c, c, c + 32, 0.2));
		// ambient.setColor(rgb(d, d, d, 0.9));
		// point.setColor(rgb(221, 221, 221));
		// point.setColor(rgb(240, 240, 240));
		// ambient.setColor(rgb(51, 51, 51));
		// ambient.setColor(rgb(201, 201, 201));
		// ambient.setColor(rgb(78, 78, 78));// 0.3f
		ambient.setColor(rgb(240, 240, 240, 0.95));
		point.setColor(rgb(179, 204, 204)); // 0.7, 0.8, 0.8
		// point.setColor(BLACK);
		// ambient.setColor(BLUE);

		// AmbientLight ambient2 = new AmbientLight(Color.WHITE);
		// ambient2.setTranslateZ(1);
		// ambient2.setTranslateY(3);
		// lights.getChildren().add(ambient2);
		// scene.setOnKeyPressed(e -> {
		// KeyCode keyCode = e.getCode();
		// });
		// scene.setOnMouseClicked(e -> {
		// if (e.isSecondaryButtonDown()) {
		// brightness -= 0.1;
		// if (brightness < 0.1) brightness = 1;
		// } else {
		// saturation += 0.1;
		// if (saturation > 1) {
		// saturation = 0;
		// hue += 10;
		// }
		// }
		// // setMaterial(material, Color.hsb(hue, saturation, brightness));
		// });
	}

	@Override
	protected void makeObjects(Group group) throws Exception {
		Group child = new Group();
		child.setTranslateY(-1.5);
		group.getChildren().add(child);
		ObservableList<Node> g = child.getChildren();
		setMaterial(material, GRAY);
		// Image image = FXUtil.loadImage("/images/Polished-Stainless-Steel_0.jpg");
		// Image image = FXUtil.loadImage("/images/water2.jpg");
		// material.setSelfIlluminationMap(image);
		// material.setSpecularMap(image);

		// material.setDiffuseMap(image); //prevent transparenting
		// material.setBumpMap(image); //have a split

		setMaterial(material2, ALICEBLUE);
		setMaterial(material3, ROYALBLUE);

		makePedestal(g);
		makeTank(g);
		// makeBox(g);
		makePump(g);
		// BUT need set transform
		// Text text=new Text("2D text");
		// BorderPane d2=new BorderPane(text);
		// Node d2=text;
		// d2.setCache(true);
		// d2.setCacheHint(CacheHint.SCALE_AND_ROTATE);
		// g.add(d2);
	}

	private void setMaterial(PhongMaterial material, Color color) {
		// material.setDiffuseColor(web("#cccccc"));
		material.setDiffuseColor(color);
		material.setSpecularColor(color);

		// Color.rgb((int) color.getRed(), (int) color.getGreen(), (int) color.getBlue(), 128 / 256d));
		material.setSpecularPower(80);
	}

	private void makePedestal(ObservableList<Node> g) {
		// pedestal
		Box box = new Box(5.5, 0.2, 5);
		box.setMaterial(material2);
		// box.setTranslateY(3);

		double length1 = 4.2;
		// outlet dispatch tube
		Capsule t1 = new Capsule(0.3, length1);
		t1.setCullFace(CullFace.BACK);
		t1.setMaterial(material);
		t1.getTransforms().addAll(new Rotate(90, X_AXIS), new Translate(0, -length1 / 2, -0.4));

		double length2 = 4.4;
		// outlet endpoint supply tube
		Group tg = makePump(length2, 0.3, 0.35, material);
		tg.getTransforms().addAll(new Rotate(90, X_AXIS), new Translate(-2.8, 0, -1.5));

		g.addAll(t1, tg, box);

		// TriangleMesh mesh = createToroidMesh(2f, 0.5f, 100, 100);
		// g.addAll(new MeshView(mesh));
	}

	private void makeTank(ObservableList<Node> g) {
		// tank
		double radius = 1;
		double height = 2.5;
		Capsule capsule = new Capsule(radius, height);
		capsule.setMaterial(material);
		capsule.setRotate(90);
		capsule.setRotationAxis(X_AXIS);
		double r2 = radius + 0.05;
		Torus t1 = makeTorus(r2, 0.05, materialColor);
		Torus t2 = makeTorus(r2, 0.05, materialColor);

		// two end rings
		double h2 = height / 2;
		double y = r2 + 0.2;
		t1.setTranslateY(y);
		t2.setTranslateY(y);
		t1.setTranslateZ(-h2);
		t2.setTranslateZ(h2);

		Group tank = new Group(capsule, t1, t2);

		// vertical set
		Box b1 = new Box(1.9, 1.1, 0.25);
		Box b2 = new Box(1.9, 1.1, 0.25);
		b1.setMaterial(material);
		b2.setMaterial(material);
		double h3 = h2 - 0.2;
		b1.setTranslateZ(-h3);
		b2.setTranslateZ(h3);
		b1.setTranslateY(0.1);
		b2.setTranslateY(0.1);

		float tankx = 1.7f;
		Group tankGroup = new Group(tank, b1, b2);
		tankGroup.setTranslateX(tankx);
		tankGroup.setTranslateY(0.5);

		// outlet tube from tank
		float height2 = 0.4f;
		BezierHelper bezier1 = new BezierHelper(new Point3D(tankx, 0.8f, 0), new Point3D(tankx, height2, 0),
				new Point3D(tankx, height2, 0), new Point3D(0, height2, 0));
		BezierMesh bend = new BezierMesh(bezier1, 0.25, 300, 20, 0, 0);
		// bend.setTextureModePattern(CarbonPatterns.LIGHT_CARBON, 2);
		bend.setMaterial(material);
		// bend.setClip( new BezierMesh(bezier1, 0.2, 300, 20, 0, 0));

		// There's no depth sort algorithm though, meaning that order of how 3D shapes are added to the scene matters.
		BezierMesh bendInner = new BezierMesh(bezier1, 0.15, 300, 20, 0, 0);
		// bendInner.setTextureModeVertices1D(1530, t -> bezier1.getKappa(t.doubleValue()));
		bendInner.setTextureModeVertices1D(DefaultColorPalette.BLUEWHITE, 100, t -> bezier1.getKappa(t.doubleValue()));
		// bendInner.setTextureModeVertices3D(1530, p -> 1);
		beziers.add(bendInner);

		// two inlet tubes
		double z1 = 0.7;
		double y1 = 2.9;
		double y2 = y1 + 0.2;
		Group inlet1 = makePump1(0.6, 0.3, 0.35, material);
		inlet1.setTranslateX(tankx);
		inlet1.setTranslateY(y1);
		inlet1.setTranslateZ(z1);
		Group inlet2 = makePump1(0.6, 0.3, 0.35, material);
		inlet2.setTranslateX(tankx);
		inlet2.setTranslateY(y1);
		inlet2.setTranslateZ(-z1);
		Group inlet3 = makePump1(0.8, 0.1, 0.15, material);
		inlet3.setTranslateX(tankx);
		inlet3.setTranslateY(y2);

		// gauge
		Group gauge = makeGauge(2);
		gauge.setTranslateX(tankx);
		gauge.setTranslateY(y2 + 0.85);
		gauge.setTranslateZ(-0.7);
		gauge.setScaleX(0.2);
		gauge.setScaleY(0.2);
		gauge.setScaleZ(0.2);
		// gauge.setRotate(90);
		// gauge.setRotationAxis(Y_AXIS);

		g.addAll(tankGroup, gauge, inlet1, inlet2, inlet3, bendInner, bend);
		// g.addAll(bendInner);
	}

	private Torus makeTorus(double radius, double tRadius, Color color) {
		Torus torus = new Torus(radius, tRadius, color);
		torus.setMaterial(material);
		return torus;
	}

	private void makePump(ObservableList<Node> g) {
		double z = -1.8;
		for (int i = 0; i < 4; i++) {
			// pump part
			Group tg = makePump(2.4, 0.3, 0.35, material3);
			tg.setTranslateZ(z);
			tg.setTranslateX(-1.4);
			tg.setTranslateY(1.4);
			g.add(tg);

			double toothLength = 0.34;
			double toothWidth = 0.52;
			double toothHeight = 0.15;
			int toothCount = 20;
			double headHeight = 1;
			double headDiameter = 0.2;

			CSG shell = servoHeadMale(headHeight, headDiameter, toothCount, toothHeight, toothWidth, toothLength);
			CSGMesh mesh = new CSGMesh(shell);
			mesh.setMaterial(material3);
			g.add(mesh);
			mesh.setRotate(90);
			mesh.setRotationAxis(X_AXIS);
			mesh.setTranslateZ(z - toothWidth);
			mesh.setTranslateY(2.1);
			mesh.setTranslateX(-1.4);

			// horizontal tube part
			Group tube = makeBaseTube(1.8, 0.25, 0.35);
			tube.setRotate(90);
			tube.setRotationAxis(Z_AXIS);
			tube.setTranslateY(0.35);
			tube.setTranslateZ(z);
			tube.setTranslateX(-1.1);
			g.add(tube);

			// vertical bend tube part
			BezierMesh bend = makeBendTube(0.25, 0, -0.8f, 0.45f, 1.5f, 0);
			g.add(bend);
			bend.setTranslateZ(z);
			bend.setTranslateX(-2);

			z += 1.2;
		}
	}

	private Group makePump(double height, double r1, double r2, PhongMaterial headMaterial) {
		double half = height / 2;
		Cylinder t1 = new Cylinder(r1, height);
		Cylinder t2 = new Cylinder(r2, 0.2);
		Cylinder t3 = new Cylinder(r2, 0.2);
		t2.setTranslateY(half);
		t3.setTranslateY(-half);
		t1.setMaterial(material);
		t2.setMaterial(headMaterial);
		t3.setMaterial(material);
		return new Group(t1, t2, t3);
	}

	private Group makePump1(double height, double r1, double r2, PhongMaterial headMaterial) {
		double half = height / 2;
		Cylinder t1 = new Cylinder(r1, height);
		Cylinder t2 = new Cylinder(r2, 0.2);
		t2.setTranslateY(half);
		t1.setMaterial(material);
		t2.setMaterial(headMaterial);
		return new Group(t1, t2);
	}

	private Group makeBaseTube(double height, double r1, double r2) {
		// horizontal tube with heads
		double half = height / 2;
		Cylinder t0 = new Cylinder(r1, height);
		Cylinder t1 = new Cylinder(r2, 0.12);
		Cylinder t2 = new Cylinder(r2, 0.12);
		Cylinder t3 = new Cylinder(r2, 0.12);
		Cylinder t4 = new Cylinder(r2, 0.12);
		t0.setMaterial(material);
		t1.setMaterial(material);
		t2.setMaterial(material);
		t3.setMaterial(material);
		t4.setMaterial(material);
		t1.setTranslateY(half + 0.13);
		t2.setTranslateY(half);
		t3.setTranslateY(-half + 0.7);
		t4.setTranslateY(-half + 0.57);

		return new Group(t0, t1, t2, t3, t4);
	}

	private BezierMesh makeBendTube(double radius, float x0, float x1, float y1, float y2, float z) {
		BezierHelper bezier1 = new BezierHelper(new Point3D(x0, y1, z), new Point3D(x1, y1, z), new Point3D(x1, y1, z),
				new Point3D(x1, y2, z));
		BezierMesh bend = new BezierMesh(bezier1, radius, 300, 20, 0, 0);
		bend.setMaterial(material);
		return bend;
	}

	public CSG servoHeadMale(double headHeight, double headDiameter, int toothCount, double toothHeight,
			double toothWidth, double toothLength) {
		double clear = 0.3;
		double radius = headDiameter / 2 - toothHeight + clear;
		CSG cylinder = new eu.mihosoft.vrl.v3d.Cylinder(new Vector3d(0, 0, 0), new Vector3d(0, 0, headHeight),
				radius + 0.03, toothCount * 2).toCSG();

		CSG t = Extrude.points(new Vector3d(0, 0, headHeight), new Vector3d(-toothLength / 2, 0),
				new Vector3d(-toothWidth / 2, toothHeight), new Vector3d(toothWidth / 2, toothHeight),
				new Vector3d(toothLength / 2, 0));

		double delta = 360.0 / toothCount;
		CSG result = makeTooth(radius, 0, t);
		for (int i = 1; i < toothCount; i++) {
			CSG tooth = makeTooth(radius, i * delta, t);
			result = result.union(tooth);
		}
		return result.union(cylinder);
	}

	private CSG makeTooth(double radius, double angle, CSG tooth) {
		Transform translate = Transform.unity().translateY(radius);
		Transform rot = Transform.unity().rotZ(angle);
		tooth = tooth.transformed(rot.apply(translate));
		return tooth;
	}

//	static Font font = FXUtil.loadFont("Museo_Slab.otf", 11);

	private Group makeGauge(double ratio) {
		Group group = new Group();
		ObservableList<Node> children = group.getChildren();
		CSG outer = new eu.mihosoft.vrl.v3d.Cylinder(1 * ratio, 1.5, 100).toCSG();
		eu.mihosoft.vrl.v3d.Cylinder inner = new eu.mihosoft.vrl.v3d.Cylinder(0.93 * ratio, 0.3, 100);
		CSG sg = outer.difference(inner.toCSG());

		double scales = 12;
		double dt = 360 / scales;
		// if do cylinder union verrrry slow
		CSG c = new Cube(0.1, 0.93 * 2 * ratio, 0.1).toCSG();
		CSG result = null;
		// because the cube bar is symmetrical
		for (int i = 0; i < scales / 2; i++) {
			if (result == null) result = c.transformed(unity().rotZ(dt * i));
			else result = result.union(c.transformed(unity().rotZ(dt * i)));
		}
		// remove inner of scale label
		inner = new eu.mihosoft.vrl.v3d.Cylinder(0.6 * ratio, 0.3, 100);
		result = result.transformed(unity().translateZ(0.15)).difference(inner.toCSG());
		// center pol

		CSGMesh pol = new CSGMesh(sg);
		// PhongMaterial mat = new PhongMaterial();
		// mat.setDiffuseColor(materialColor);
		// Image image = FXUtil.loadImage("/images/cw.png");
		// // mat.setBumpMap(image);
		// mat.setSpecularMap(image);
		// mat.setSpecularPower(64);
		// // mat.setSelfIlluminationMap(image);//no effect
		// pol.setMaterial(mat);
		pol.setMaterial(material);
		children.add(pol);

		CSGMesh scal = new CSGMesh(result);
		scal.setMaterial(material2);
		children.add(scal);

		Torus ring = new Torus(0.885 * ratio, 0.21);
		ring.mesh.setMaterial(material2);
		ring.getTransforms().addAll(new Translate(0, 0, 0.1));
		children.add(ring);

		String fontName=		Font.getFontNames().get(0);

		double radius = 0.93 * ratio * 14;
		for (int i = 1; i <= 12; i++) {
			// Museo Slab 500
			// Museo Sans 500
			// Museo 500
			Text3DMesh scaleLabel = new Text3DMesh(String.valueOf(i), fontName, 11, true, 0.1, 0, 1);
			// final int count=i;
			// scaleLabel.setTextureModeVertices3D(1530,p->(double)(p.y/(20+count))*(p.x/(10+count)));
			scaleLabel.setTextureModeNone(Color.ROYALBLUE);
			scaleLabel.getTransforms().add(new Scale(0.040346851, 0.040346851));
			double rad = Math.toRadians(dt * i);
			double deltax = i < 10 ? 3 : 5;
			double deltay = i == 10 ? 6 : 4;
			scaleLabel.getTransforms().add(new Translate(-radius * Math.sin(rad) + deltax, //
					radius * Math.cos(rad) * 0.95 - deltay, 0.1));
			scaleLabel.getTransforms().add(new Rotate(180, Rotate.Z_AXIS));
			children.add(scaleLabel);
		}

		CSGMesh center = new CSGMesh(new Sphere(0.2, 40, 10).toCSG().transformed(unity().translateZ(0.15)));
		center.setMaterial(material2);
		children.add(center);

		FrustumMesh needle = new FrustumMesh(0.1, 0.035, 0.6 * ratio);
		needle.setMaterial(material2);

		needleTranslate = new Translate(0, -0.6, 0.05);
		needleRotate = new Rotate(0, Rotate.Z_AXIS);
		needle.getTransforms().addAll(needleTranslate, needleRotate);

		children.add(needle);

		return group;
	}

	/**
	 * Let the radius from the center of the hole to the center of the torus tube be "c", and the radius of the tube be
	 * "a". Then the equation in Cartesian coordinates for a torus azimuthally symmetric about the z-axis is
	 * (c-sqrt(x^2+y^2))^2+z^2=a^2 and the parametric equations are x = (c + a * cos(v)) * cos(u) y = (c + a * cos(v)) *
	 * sin(u) z = a * sin(v) (for u,v in [0,2pi).
	 *
	 * Three types of torus, known as the standard tori, are possible, depending on the relative sizes of a and c. c>a
	 * corresponds to the ring torus (shown above), c=a corresponds to a horn torus which is tangent to itself at the
	 * point (0, 0, 0), and c<a corresponds to a self-intersecting spindle torus (Pinkall 1986).
	 */
	public static TriangleMesh createToroidMesh(float radius, float tubeRadius, int tubeDivisions,
			int radiusDivisions) {
		int POINT_SIZE = 3, TEXCOORD_SIZE = 2, FACE_SIZE = 6;
		int numVerts = tubeDivisions * radiusDivisions;
		int faceCount = numVerts * 2;
		float[] points = new float[numVerts * POINT_SIZE], texCoords = new float[numVerts * TEXCOORD_SIZE];
		int[] faces = new int[faceCount * FACE_SIZE];

		int pointIndex = 0, texIndex = 0, faceIndex = 0;
		float tubeFraction = 1.0f / tubeDivisions;
		float radiusFraction = 1.0f / radiusDivisions;
		// float x, y, z;

		int p0 = 0, p1 = 0, p2 = 0, p3 = 0, t0 = 0, t1 = 0, t2 = 0, t3 = 0;
		float pi2 = (float) (2.0 * Math.PI);
		float ringUnitAngle = radiusFraction * pi2;
		float tubeUnitAngle = tubeFraction * pi2;

		// create points
		for (int tubeIndex = 0; tubeIndex < tubeDivisions; tubeIndex++) {
			float radian = tubeIndex * tubeUnitAngle;
			for (int radiusIndex = 0; radiusIndex < radiusDivisions; radiusIndex++) {
				float localRadian = radiusIndex * ringUnitAngle;
				float tubeCos = (float) Math.cos(radian);
				float tubeSin = (float) Math.sin(radian);
				float ringCos = (float) Math.cos(localRadian);
				float ringSin = (float) Math.sin(localRadian);
				float pointRadius = radius + tubeRadius * tubeCos;
				// x
				points[pointIndex] = pointRadius * ringCos;
				// y
				points[pointIndex + 1] = pointRadius * ringSin;
				// z
				points[pointIndex + 2] = (tubeRadius * tubeSin);
				pointIndex += 3;

				float r = radiusIndex < tubeDivisions ? radiusIndex * tubeUnitAngle : 0.0f;
				texCoords[texIndex] = (0.5F + (float) (Math.sin(r) * 0.5D));
				texCoords[texIndex + 1] = (0.5F + (float) (Math.cos(r) * 0.5D));
				texIndex += 2;
			}
		}
		// create faces
		for (int point = 0; point < (tubeDivisions); point++) {
			for (int crossSection = 0; crossSection < (radiusDivisions); crossSection++) {
				p0 = point * radiusDivisions + crossSection;
				p1 = p0 >= 0 ? p0 + 1 : p0 - (radiusDivisions);
				p1 = p1 % (radiusDivisions) != 0 ? p0 + 1 : p0 - (radiusDivisions - 1);
				p2 = (p0 + radiusDivisions) < ((tubeDivisions * radiusDivisions)) ? p0 + radiusDivisions
						: p0 - (tubeDivisions * radiusDivisions) + radiusDivisions;
				p3 = p2 < ((tubeDivisions * radiusDivisions) - 1) ? p2 + 1 : p2 - (tubeDivisions * radiusDivisions) + 1;
				p3 = p3 % (radiusDivisions) != 0 ? p2 + 1 : p2 - (radiusDivisions - 1);

				t0 = point * (radiusDivisions) + crossSection;
				t1 = t0 >= 0 ? t0 + 1 : t0 - (radiusDivisions);
				t1 = t1 % (radiusDivisions) != 0 ? t0 + 1 : t0 - (radiusDivisions - 1);
				t2 = (t0 + radiusDivisions) < ((tubeDivisions * radiusDivisions)) ? t0 + radiusDivisions
						: t0 - (tubeDivisions * radiusDivisions) + radiusDivisions;
				t3 = t2 < ((tubeDivisions * radiusDivisions) - 1) ? t2 + 1 : t2 - (tubeDivisions * radiusDivisions) + 1;
				t3 = t3 % (radiusDivisions) != 0 ? t2 + 1 : t2 - (radiusDivisions - 1);

				try {
					faces[faceIndex] = (p2);
					faces[faceIndex + 1] = (t3);
					faces[faceIndex + 2] = (p0);
					faces[faceIndex + 3] = (t2);
					faces[faceIndex + 4] = (p1);
					faces[faceIndex + 5] = (t0);

					faceIndex += FACE_SIZE;

					faces[faceIndex] = (p2);
					faces[faceIndex + 1] = (t3);
					faces[faceIndex + 2] = (p1);
					faces[faceIndex + 3] = (t0);
					faces[faceIndex + 4] = (p3);
					faces[faceIndex + 5] = (t1);
					faceIndex += FACE_SIZE;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		TriangleMesh localTriangleMesh = new TriangleMesh();
		localTriangleMesh.getPoints().setAll(points);
		localTriangleMesh.getTexCoords().setAll(texCoords);
		localTriangleMesh.getFaces().setAll(faces);

		return localTriangleMesh;
	}

	/**
	 * // * Draw polygonal 3D box. // *
	 *
	 * <pre>
	 * // * INPUT
	 * // * - footprint: 2D polygon coordinates;
	 * // * closed path (i.e. first and last coordinates are identical); ex:
	 * // * float[] footprint = {
	 * // * 10, -1,
	 * // * -1, -1,
	 * // * -1, 5,
	 * // * 10, 5,
	 * // * 10, -1
	 * // * };
	 * // * - zLevel: z-coordinate of actual floor level: int k; float zLevel = k * HEIGHT - HEIGHT;
	 * // * - HEIGHT: height of the box: private static final float HEIGHT = (float) 50;
	 * // *
	 * </pre>
	 *
	 * // * NOTE: we have to use the mesh method since the straightforward way // * to construct a rectangle -
	 * "rectangle" method - produces blurry edges. // * //
	 */
	private void makeBox(ObservableList<Node> g) {
		float HEIGHT = (float) 50;
		int k = 3;
		float zLevel = k * HEIGHT - HEIGHT;
		float[] footprint = { 10, -1, -1, -1, -1, 5, 10, 5, 10, -1 };
		g.add(draw(footprint, zLevel, HEIGHT));
	}

	// Draw polygonal 3D box.
	public static Group draw(float[] footprint, float zLevel, float HEIGHT) {

		Group box = new Group();
		int y = 0;

		// for each footprint coordinate make a rectangle
		int n = footprint.length - 2;

		// one side of the box
		for (int k = 0; k < n; k = k + 2) {

			float[] points = { footprint[k], y + zLevel, footprint[k + 1], footprint[k + 2], y + zLevel,
					footprint[k + 3], footprint[k + 2], y + zLevel + HEIGHT, footprint[k + 3], footprint[k],
					y + zLevel + HEIGHT, footprint[k + 1] };
			float[] texCoords = { 1, 1, 1, 0, 0, 1, 0, 0 };
			int[] faces = { 0, 0, 2, 2, 1, 1, 0, 0, 3, 3, 2, 2 };
			int[] faces2 = { 0, 0, 1, 1, 2, 2, 0, 0, 2, 2, 3, 3 };

			TriangleMesh mesh1 = new TriangleMesh();
			mesh1.getPoints().setAll(points);
			mesh1.getTexCoords().setAll(texCoords);
			mesh1.getFaces().setAll(faces);

			TriangleMesh mesh2 = new TriangleMesh();
			mesh2.getPoints().setAll(points);
			mesh2.getTexCoords().setAll(texCoords);
			mesh2.getFaces().setAll(faces2);

			final MeshView rectangle1 = new MeshView(mesh1);
			rectangle1.setMaterial(new PhongMaterial(Color.web("#FF0000", 0.25)));
			rectangle1.setCullFace(CullFace.BACK);

			final MeshView rectangle2 = new MeshView(mesh2);
			rectangle2.setMaterial(new PhongMaterial(Color.web("#FF0000", 0.25)));
			rectangle2.setCullFace(CullFace.BACK);

			final MeshView wire1 = new MeshView(mesh1);
			wire1.setMaterial(new PhongMaterial(Color.web("#000000", 0.5)));
			wire1.setCullFace(CullFace.BACK);
			wire1.setDrawMode(DrawMode.LINE);

			final MeshView wire2 = new MeshView(mesh2);
			wire2.setMaterial(new PhongMaterial(Color.web("#000000", 0.5)));
			wire2.setCullFace(CullFace.BACK);
			wire2.setDrawMode(DrawMode.LINE);

			// add to group
			box.getChildren().addAll(rectangle1, wire1, rectangle2, wire2);
		}

		return box;
	}
}