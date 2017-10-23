/*
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package net.tom.j3d.enigma;

/**
 * This model is based on my VRML Enigma Cipher Machine.
 */
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Panel;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Group;
import javax.media.j3d.Material;
import javax.media.j3d.PathInterpolator;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix4d;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 * 
 * @author Tom Nelson - tomnelson@dev.java.net
 *
 */
public class Enigma extends Applet {
   
    Panel controls;
    URL clickURL;
    URL doorURL;
    AudioClip  clickSound;
    AudioClip  doorSound;
    BranchGroup objRoot;
    TransformGroup objTrans;
    PathInterpolator path;
    BranchGroup motionBranch;
    //    TransformGroup target;
    Point3f oldPoint = new Point3f(0.f, 0.f, -130.f);
    Quat4f oldQuat;
    public static final int ORIGINAL_VIEWPOINT = 0;
    public static final int TYPING_VIEWPOINT = 1;
    public static final int ROTOR_VIEWPOINT = 2;
    public static final int VIEWPOINT_COUNT = 3;

    private ViewpointNavigation viewpointNavigation;
    private Transform3D[] viewpoints;

    
    static String helpText = 
	"The Java3D Enigma Cipher Machine\nby Tom Nelson\ntnelso2@cs.umbc.edu\n\n"+
	"Instructions:\n"+
	"Mouse Button One rotates, mouse Button Two zooms,\n and Mouse Button Three translates\n\n"+
	"To encode a message, type on the keyboard by clicking with the mouse.\n"+
	"Read your cipher text from the lighted panel behind the keyboard.\n"+
	"You can vary the start setting of the rotors to apply a different\n"+
	"cipher key. Mouse click the rotors with button one or three to set them.\n\n"+
	"To decode a message, make sure that the rotors are set to the same\n"+
	"start position that was used to encode the message, then type the\n"+
	"cipher text on the keyboard, reading the plain text from the lighted\n"+
	"panel behind the keyboard.\n\n"+
	"You can also open and close the case and rotor cover by clicking on them.\n";
    

    public BranchGroup createSceneGraph(Canvas3D canvas) {
	
	viewpoints = createViewpoints();
	
	oldQuat = new Quat4f();
	Matrix4d mat = new Matrix4d();
	mat.rotX(Math.PI/6.);
	oldQuat.set(mat);
	objRoot = new BranchGroup();
	objRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND);
	objRoot.setCapability(Group.ALLOW_CHILDREN_WRITE);
	
	TransformGroup objScale = new TransformGroup();
	Transform3D t3d = new Transform3D();
	//  t3d.rotX(Math.PI/6.);
	t3d.setScale(0.3);
	//   t3d.setTranslation(new Vector3f(0.f, 0.f, -40.f));
	objScale.setTransform(t3d);
	objRoot.addChild(objScale);
	
	Transform3D tt = new Transform3D();
	tt.rotX(Math.PI/6.);
	tt.setTranslation(new Vector3f(0.f, 0.f, -130.f));
	objTrans = new TransformGroup(tt);
	objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ );
	objScale.addChild(objTrans);
	
	viewpointNavigation = new ViewpointNavigation(objRoot, objTrans,
						      viewpoints);
	
	// Create Colors, Materials,  and Appearances.
	Appearance look = new ModulateTextureAppearance();
	Color3f objColor = new Color3f(0.7f, 0.7f, 0.7f);
	Color3f black = new Color3f(0.f, 0.f, 0.f);
	Color3f white = new Color3f(1.0f, 1.0f, 0.6f);
	Color3f gray  = new Color3f(.2f, .2f, .2f);
//	Color3f darkGray  = new Color3f(.2f, .2f, .2f);
	
	Material objMaterial = new Material(objColor, black,
					    objColor, white, 100.0f);
	Material blackMaterial = new Material(objColor, black,
					      black, objColor, 10.0f);
	Material whiteMaterial = new Material(white, white,
					      white, white, 100.0f);
	Material grayMaterial = new Material(gray, black,
					     gray, gray, 100.0f);
	
//	Material darkGrayMaterial = new Material(darkGray, black,
//					     darkGray, white, 100.0f);
	
	look.setMaterial(new Material(objColor, black,
				      objColor, white, 100.0f));
	Appearance blackLook = new ModulateTextureAppearance();
	blackLook.setMaterial(blackMaterial);
	
	Appearance whiteLook = new ModulateTextureAppearance();
	whiteLook.setMaterial(whiteMaterial);
	
	Appearance grayLook = new ModulateTextureAppearance();
	grayLook.setMaterial(grayMaterial);
	grayLook.setCapability(Appearance.ALLOW_MATERIAL_READ);
	grayLook.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
	
	Appearance texturedApp = new ModulateTextureAppearance();
	texturedApp.setMaterial(objMaterial);
	TextureLoader tex = new TextureLoader(grabImage("numbers.gif"), this);
	texturedApp.setTexture(tex.getTexture());
	
	Appearance underLidApp = new ModulateTextureAppearance();
	underLidApp.setMaterial(objMaterial);
	tex = new TextureLoader(grabImage("enigma_under_lid.jpg"), this);
	underLidApp.setTexture(tex.getTexture());
	
	Appearance plugsApp = new ModulateTextureAppearance();
	plugsApp.setMaterial(objMaterial);
	tex = new TextureLoader(grabImage("enigma_plugs.jpg"), this);
	plugsApp.setTexture(tex.getTexture());
	
	Appearance woodLook = new ModulateTextureAppearance();
	woodLook.setMaterial(objMaterial);
	tex = new TextureLoader(grabImage("enigma_middle.jpg"), this);
	woodLook.setTexture(tex.getTexture());
	
	Appearance leftSideLook = new ModulateTextureAppearance();
	leftSideLook.setMaterial(objMaterial);
	tex = new TextureLoader(grabImage("enigma_left_side.jpg"), this);
	leftSideLook.setTexture(tex.getTexture());
	
	Appearance rightSideLook = new ModulateTextureAppearance();
	rightSideLook.setMaterial(objMaterial);
	tex = new TextureLoader(grabImage("enigma_right_side.jpg"), this);
	rightSideLook.setTexture(tex.getTexture());
	
	Appearance lidSideLook = new ModulateTextureAppearance();
	
	lidSideLook.setMaterial(objMaterial);
	tex = new TextureLoader(grabImage("enigma_lid_side.jpg"), this);
	lidSideLook.setTexture(tex.getTexture());
      
	Appearance handleLook = new ModulateTextureAppearance();
	handleLook.setMaterial(objMaterial);
	tex = new TextureLoader(grabImage("handle.jpg"), this);
	handleLook.setTexture(tex.getTexture());
	
	Appearance flapLook = new ModulateTextureAppearance();
	flapLook.setMaterial(objMaterial);
	tex = new TextureLoader(grabImage("enigma_flap.jpg"), this);
	flapLook.setTexture(tex.getTexture());
	
	Appearance flapTopLook = new ModulateTextureAppearance();
	flapTopLook.setMaterial(objMaterial);
	tex = new TextureLoader(grabImage("enigma_flap_top.jpg"), this);
	flapTopLook.setTexture(tex.getTexture());
	
	Appearance coverLook = new ModulateTextureAppearance();
	coverLook.setMaterial(objMaterial);
	tex = new TextureLoader(grabImage("enigma_cover.jpg"), this);
	coverLook.setTexture(tex.getTexture());
	
	Appearance enigmaLook = new ModulateTextureAppearance();
	enigmaLook.setMaterial(objMaterial);
	tex = new TextureLoader(grabImage("enigma_color.jpg"), this);
	enigmaLook.setTexture(tex.getTexture());
	
	BoundingSphere bounds =
	    new BoundingSphere(new Point3d(0.0,0.0,0.0), 2000.0);
	
	try {
	    clickURL = Enigma.class.getResource("/click.wav");
	    clickSound = Applet.newAudioClip(clickURL);
	    doorURL = Enigma.class.getResource("/Dooropen.wav");
	    doorSound = Applet.newAudioClip(doorURL);
	}
	catch (Exception ex) {
	    System.out.println("Exception: "+ex);
	}
	
	Transform3D t = new Transform3D();
	t.rotZ(Math.PI/2);
	t.setScale(1.2);
	t.setTranslation(new Vector3f(0.f, -5.2f, -12.f));
	Matrix3d m = new Matrix3d();
	m.rotZ(Math.PI/2);
	t.setRotation(m);
	EnigmaRotorGroup rotorGroup = 
	    new EnigmaRotorGroup(look, texturedApp, Applet.newAudioClip(clickURL));
	rotorGroup.setTransform(t);
	rotorGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	rotorGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	objTrans.addChild(rotorGroup);
	
	Transform3D keyPanelXform = new Transform3D();
	keyPanelXform.setScale(new Vector3d(20, 3, 8));
	keyPanelXform.setTranslation(new Vector3f(0.f,0.f,7.f));
	TransformGroup keyPanelGroup = new TransformGroup(keyPanelXform);
	KeyPanel keyPanel = new KeyPanel(enigmaLook);
	keyPanelGroup.addChild(keyPanel);
	objTrans.addChild(keyPanelGroup);
	
	Transform3D plugPanelXform = new Transform3D();
	plugPanelXform.rotX(Math.PI/2);
	plugPanelXform.setScale(new Vector3d(20, 1, 8));
	plugPanelXform.setTranslation(new Vector3f(0.f,-9.2f,23.f));
	TransformGroup plugPanelGroup = new TransformGroup(plugPanelXform);
	FlatPanel plugPanel = new FlatPanel(plugsApp);
	plugPanelGroup.addChild(plugPanel);
	objTrans.addChild(plugPanelGroup);
	
	Transform3D lightPanelXform = new Transform3D();
	lightPanelXform.setTranslation(new Vector3f(0.f,.6f,-18.f));
	LightPanelGroup lightPanel = new LightPanelGroup(coverLook);
	lightPanel.setTransform(lightPanelXform);
	lightPanel.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	lightPanel.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	objTrans.addChild(lightPanel);
	
	Transform3D lightBaseXform = new Transform3D();
	lightBaseXform.setScale(new Vector3d(20,1,7));
	lightBaseXform.setTranslation(new Vector3f(0.f,0.f,0.f));
	TransformGroup lightBaseGroup = new TransformGroup(lightBaseXform);
	FlatPanel lightBase = new FlatPanel(blackLook);
	lightBaseGroup.addChild(lightBase);
	objTrans.addChild(lightBaseGroup);
	
	Transform3D rightBaseXform = new Transform3D();
	rightBaseXform.setScale(new Vector3d(6,1,7));
	rightBaseXform.setTranslation(new Vector3f(14.f,0.f,-10.f));
	TransformGroup rightBaseGroup = new TransformGroup(rightBaseXform);
	FlatPanel rightBase = new FlatPanel(blackLook);
	rightBaseGroup.addChild(rightBase);
	objTrans.addChild(rightBaseGroup);
	
	Transform3D leftBaseXform = new Transform3D();
	leftBaseXform.setScale(new Vector3d(6,1,7));
	leftBaseXform.setTranslation(new Vector3f(-14.f,0.f,-10.f));
	TransformGroup leftBaseGroup = new TransformGroup(leftBaseXform);
	FlatPanel leftBase = new FlatPanel(blackLook);
	leftBaseGroup.addChild(leftBase);
	objTrans.addChild(leftBaseGroup);
	
	t = new Transform3D();
	t.setTranslation(new Vector3f(-15.f, 0.f, 10.f));
	t.setScale(.3f);
	EnigmaKeyboard keyboard = 
	    new EnigmaKeyboard(createKeyTextures(objMaterial),look,doorSound);
	keyboard.setTransform(t);
	keyboard.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	keyboard.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	objTrans.addChild(keyboard);
	
	t.setTranslation(new Vector3f(-15.f, 0.7f, -3.f));
	EnigmaLightboard lightboard = 
	    new EnigmaLightboard(getLightTextures());
	lightboard.setTransform(t);
	lightboard.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	lightboard.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	objTrans.addChild(lightboard);
	
	t = new Transform3D();
	t.setTranslation(new Vector3f(20.5f, -8.5f, 2.5f));
	TransformGroup rightSideGroup = new TransformGroup(t);
	Box rightSide = new Box(.5f, 10.f, 22.f, Box.GENERATE_NORMALS |
				      Box.GENERATE_TEXTURE_COORDS |
			Box.ENABLE_GEOMETRY_PICKING, woodLook);
	rightSide.setAppearance(Box.RIGHT, rightSideLook);
	rightSideGroup.addChild(rightSide);
	objTrans.addChild(rightSideGroup);
	
	t.setTranslation(new Vector3f(-20.5f, -8.5f, 2.5f));
	TransformGroup leftSideGroup = new TransformGroup(t);
	Box leftSide = new Box(.5f, 10.f, 22.f, Box.GENERATE_NORMALS | 
			       Box.GENERATE_TEXTURE_COORDS |
			Box.ENABLE_GEOMETRY_PICKING, 
			       woodLook);
	leftSide.setAppearance(Box.LEFT, leftSideLook);
	leftSideGroup.addChild(leftSide);
	objTrans.addChild(leftSideGroup);
	
	t.setTranslation(new Vector3f(0.f, -8.f, -19.f));
	TransformGroup backSideGroup = new TransformGroup(t);
	Box backSide = new Box(21.f, 9.5f, .5f, Box.GENERATE_NORMALS |
			       Box.GENERATE_TEXTURE_COORDS |
			Box.ENABLE_GEOMETRY_PICKING, woodLook);
	backSide.setAppearance(Box.BACK, handleLook);
	backSideGroup.addChild(backSide);
	objTrans.addChild(backSideGroup);
	
	t.setTranslation(new Vector3f(0.f, -18.2f, 2.5f));
	TransformGroup bottomSideGroup = new TransformGroup(t);
	Box bottom = new Box(20.f, .5f, 22.f, Box.GENERATE_NORMALS |
			     Box.GENERATE_TEXTURE_COORDS |
			     Box.ENABLE_GEOMETRY_PICKING, woodLook);
	bottomSideGroup.addChild(bottom);
	objTrans.addChild(bottomSideGroup);
	
	t.setTranslation(new Vector3f(0.f, 1.5f, -19.5f));
	TransformGroup lidGroup = new TransformGroup(t);
	EnigmaLidGroup lid = new EnigmaLidGroup(underLidApp, lidSideLook, woodLook);
	lidGroup.addChild(lid);
	objTrans.addChild(lidGroup);
	
	t.setTranslation(new Vector3f(0.f, -17.5f, 24.5f));
	TransformGroup frontGroup = new TransformGroup(t);
	EnigmaFrontGroup front = new EnigmaFrontGroup(flapLook, flapTopLook, woodLook);
	frontGroup.addChild(front);
	objTrans.addChild(frontGroup);
	
//	objTrans.addChild(clickSound);
//	objTrans.addChild(doorSound);
	
	PickKeyBehavior behavior = 
	    new PickKeyBehavior(objRoot, canvas, bounds);
	behavior.addPropertyChangeListener(keyboard);
	behavior.addPropertyChangeListener(rotorGroup);
	rotorGroup.addPropertyChangeListener(lightboard);
	objRoot.addChild(behavior);
	
	PickRotorBehavior rbehavior = 
	    new PickRotorBehavior(objRoot, canvas, bounds);
	rbehavior.addPropertyChangeListener(rotorGroup);
	objRoot.addChild(rbehavior);
	
	PickPanelBehavior pickPanelBehavior = 
	    new PickPanelBehavior(objRoot, canvas, bounds);
	pickPanelBehavior.addPropertyChangeListener(lightPanel);
	objRoot.addChild(pickPanelBehavior);
	
	PickLidBehavior pickLidBehavior = 
	    new PickLidBehavior(objRoot, canvas, bounds);
	pickLidBehavior.addPropertyChangeListener(lid);
	pickLidBehavior.addPropertyChangeListener(front);
	pickLidBehavior.addPropertyChangeListener(lightPanel);
	objRoot.addChild(pickLidBehavior);
	
	PickFrontBehavior pickFrontBehavior = 
	    new PickFrontBehavior(objRoot, canvas, bounds);
	pickFrontBehavior.addPropertyChangeListener(front);
	pickFrontBehavior.addPropertyChangeListener(lid);
	pickFrontBehavior.addPropertyChangeListener(lightPanel);
	objRoot.addChild(pickFrontBehavior);

	MouseRotate behavior1 = new MouseRotate();
	behavior1.setTransformGroup(objTrans);
	objTrans.addChild(behavior1);
	behavior1.setSchedulingBounds(bounds);
	
	MouseZoom behavior2 = new MouseZoom();
	behavior2.setTransformGroup(objTrans);
	objTrans.addChild(behavior2);
	behavior2.setSchedulingBounds(bounds);
	
	MouseTranslate behavior3 = new MouseTranslate();
	behavior3.setTransformGroup(objTrans);
	objTrans.addChild(behavior3);
	behavior3.setSchedulingBounds(bounds);

	//Shine it with two colored lights.
	Color3f lColor1 = new Color3f(.5f, .5f, .5f);
	Color3f lColor2 = new Color3f(1.0f, 1.0f, 1.0f);
	Vector3f lDir2  = new Vector3f(-1.0f, 0.0f, -1.0f);
	DirectionalLight lgt2 = new DirectionalLight(lColor2, lDir2);
	AmbientLight ambient = new AmbientLight(lColor1);
	lgt2.setInfluencingBounds(bounds);
	ambient.setInfluencingBounds(bounds);
	objRoot.addChild(lgt2);
	objRoot.addChild(ambient);
	
	// Let Java 3D perform optimizations on this scene graph.
	objRoot.compile();
	
	return objRoot;
    }

    public Transform3D[] createViewpoints() {

	Transform3D[] viewpoints = new Transform3D[Enigma.VIEWPOINT_COUNT];

	// original viewpoint:
	Matrix4f mat = new Matrix4f();
	mat.rotX((float)(Math.PI/6.f));
	mat.setTranslation(new Vector3f(0.f, 0.f, -130.f));
	viewpoints[Enigma.ORIGINAL_VIEWPOINT] = new Transform3D(mat);

	// typing viewpoint:
	mat = new Matrix4f();
	mat.rotX((float)(Math.PI/4.f));
	mat.setTranslation(new Vector3f(0.f, -5.f, -60.f));
	viewpoints[Enigma.TYPING_VIEWPOINT] = new Transform3D(mat);

	// rotor viewpoint:
	mat = new Matrix4f();
	mat.rotX((float)(Math.PI/3.f));
	mat.setTranslation(new Vector3f(0.f, -10.f, -40.f));
	viewpoints[Enigma.ROTOR_VIEWPOINT] = new Transform3D(mat);

	return viewpoints;
    }
    
    public void resetOriginalViewpoint() {
	viewpointNavigation.interpolateToPosition(Enigma.ORIGINAL_VIEWPOINT);
    }
    
    public void setTypingViewpoint() {
	viewpointNavigation.interpolateToPosition(Enigma.TYPING_VIEWPOINT);
    }
    
    public void setRotorViewpoint() {
	viewpointNavigation.interpolateToPosition(Enigma.ROTOR_VIEWPOINT);
    }
    
    public Enigma() {
	controls = createControls();
	setLayout(new BorderLayout());
	GraphicsConfiguration config = 
	    SimpleUniverse.getPreferredConfiguration();
	Canvas3D c = new Canvas3D(config);
	add(c, BorderLayout.CENTER);
	add(controls, BorderLayout.SOUTH);
	
	// Create a Enigma scene and attach it to the virtual universe
	BranchGroup scene = createSceneGraph(c);
	SimpleUniverse u = new SimpleUniverse(c);
        u.getViewer().getView().setUserHeadToVworldEnable(true);	
//	AudioDevice audioDev = u.getViewer().createAudioDevice();
	
	// This will move the ViewPlatform back a bit so the
	// objects in the scene can be viewed.
	u.getViewingPlatform().setNominalViewingTransform();
	
	u.addBranchGraph(scene);
    }
    
    Appearance[] createKeyTextures(Material m) {
	/* alphabet textures */
	TextureLoader tex;
	Appearance[] looks = new ModulateTextureAppearance[26];
	
	looks[0] = new ModulateTextureAppearance();
	tex = new TextureLoader(grabImage("enigma_a.jpg"), this);
	looks[0].setMaterial(m);
	looks[0].setTexture(tex.getTexture());
	
	looks[1] = new ModulateTextureAppearance();
	tex = new TextureLoader(grabImage("enigma_b.jpg"), this);
	looks[1].setMaterial(m);
	looks[1].setTexture(tex.getTexture());
	
	looks[2] = new ModulateTextureAppearance();
	tex = new TextureLoader(grabImage("enigma_c.jpg"), this);
	looks[2].setMaterial(m);
	looks[2].setTexture(tex.getTexture());
	
	looks[3] = new ModulateTextureAppearance();
	tex = new TextureLoader(grabImage("enigma_d.jpg"), this);
	looks[3].setMaterial(m);
	looks[3].setTexture(tex.getTexture());
	
	looks[4] = new ModulateTextureAppearance();
	tex = new TextureLoader(grabImage("enigma_e.jpg"), this);
	looks[4].setMaterial(m);
	looks[4].setTexture(tex.getTexture());
	
	looks[5] = new ModulateTextureAppearance();
	tex = new TextureLoader(grabImage("enigma_f.jpg"), this);
	looks[5].setMaterial(m);
	looks[5].setTexture(tex.getTexture());
	
	looks[6] = new ModulateTextureAppearance();
	tex = new TextureLoader(grabImage("enigma_g.jpg"), this);
	looks[6].setMaterial(m);
	looks[6].setTexture(tex.getTexture());
	
	looks[7] = new ModulateTextureAppearance();
	tex = new TextureLoader(grabImage("enigma_h.jpg"), this);
	looks[7].setMaterial(m);
	looks[7].setTexture(tex.getTexture());
	
	looks[8] = new ModulateTextureAppearance();
	tex = new TextureLoader(grabImage("enigma_i.jpg"), this);
	looks[8].setMaterial(m);
	looks[8].setTexture(tex.getTexture());
	
	looks[9] = new ModulateTextureAppearance();
	tex = new TextureLoader(grabImage("enigma_j.jpg"), this);
	looks[9].setMaterial(m);
	looks[9].setTexture(tex.getTexture());
	
	looks[10] = new ModulateTextureAppearance();
	tex = new TextureLoader(grabImage("enigma_k.jpg"), this);
	looks[10].setMaterial(m);
	looks[10].setTexture(tex.getTexture());
	
	looks[11] = new ModulateTextureAppearance();
	tex = new TextureLoader(grabImage("enigma_l.jpg"), this);
	looks[11].setMaterial(m);
	looks[11].setTexture(tex.getTexture());
	
	looks[12] = new ModulateTextureAppearance();
	tex = new TextureLoader(grabImage("enigma_m.jpg"), this);
	looks[12].setMaterial(m);
	looks[12].setTexture(tex.getTexture());
	
	looks[13] = new ModulateTextureAppearance();
	tex = new TextureLoader(grabImage("enigma_n.jpg"), this);
	looks[13].setMaterial(m);
	looks[13].setTexture(tex.getTexture());
	
	looks[14] = new ModulateTextureAppearance();
	tex = new TextureLoader(grabImage("enigma_o.jpg"), this);
	looks[14].setMaterial(m);
	looks[14].setTexture(tex.getTexture());
	
	looks[15] = new ModulateTextureAppearance();
	tex = new TextureLoader(grabImage("enigma_p.jpg"), this);
	looks[15].setMaterial(m);
	looks[15].setTexture(tex.getTexture());
	
	looks[16] = new ModulateTextureAppearance();
	tex = new TextureLoader(grabImage("enigma_q.jpg"), this);
	looks[16].setMaterial(m);
	looks[16].setTexture(tex.getTexture());
	
	looks[17] = new ModulateTextureAppearance();
	tex = new TextureLoader(grabImage("enigma_r.jpg"), this);
	looks[17].setMaterial(m);
	looks[17].setTexture(tex.getTexture());
	
	looks[18] = new ModulateTextureAppearance();
	tex = new TextureLoader(grabImage("enigma_s.jpg"), this);
	looks[18].setMaterial(m);
	looks[18].setTexture(tex.getTexture());
	
	looks[19] = new ModulateTextureAppearance();
	tex = new TextureLoader(grabImage("enigma_t.jpg"), this);
	looks[19].setMaterial(m);
	looks[19].setTexture(tex.getTexture());
	
	looks[20] = new ModulateTextureAppearance();
	tex = new TextureLoader(grabImage("enigma_u.jpg"), this);
	looks[20].setMaterial(m);
	looks[20].setTexture(tex.getTexture());
	
	looks[21] = new ModulateTextureAppearance();
	tex = new TextureLoader(grabImage("enigma_v.jpg"), this);
	looks[21].setMaterial(m);
	looks[21].setTexture(tex.getTexture());
	
	looks[22] = new ModulateTextureAppearance();
	tex = new TextureLoader(grabImage("enigma_w.jpg"), this);
	looks[22].setMaterial(m);
	looks[22].setTexture(tex.getTexture());
	
	looks[23] = new ModulateTextureAppearance();
	tex = new TextureLoader(grabImage("enigma_x.jpg"), this);
	looks[23].setMaterial(m);
	looks[23].setTexture(tex.getTexture());
	
	looks[24] = new ModulateTextureAppearance();
	tex = new TextureLoader(grabImage("enigma_y.jpg"), this);
	looks[24].setMaterial(m);
	looks[24].setTexture(tex.getTexture());
	
	looks[25] = new ModulateTextureAppearance();
	tex = new TextureLoader(grabImage("enigma_z.jpg"), this);
	looks[25].setMaterial(m);
	looks[25].setTexture(tex.getTexture());
	
	return looks;
    }

    Texture[] getLightTextures() {
	Texture[] textures = new Texture[] {
	    new TextureLoader(grabImage("lit_a.gif"), this).getTexture(),
	    new TextureLoader(grabImage("lit_b.gif"), this).getTexture(),
	    new TextureLoader(grabImage("lit_c.gif"), this).getTexture(),
	    new TextureLoader(grabImage("lit_d.gif"), this).getTexture(),
	    new TextureLoader(grabImage("lit_e.gif"), this).getTexture(),
	    new TextureLoader(grabImage("lit_f.gif"), this).getTexture(),
	    new TextureLoader(grabImage("lit_g.gif"), this).getTexture(),
	    new TextureLoader(grabImage("lit_h.gif"), this).getTexture(),
	    new TextureLoader(grabImage("lit_i.gif"), this).getTexture(),
	    new TextureLoader(grabImage("lit_j.gif"), this).getTexture(),
	    new TextureLoader(grabImage("lit_k.gif"), this).getTexture(),
	    new TextureLoader(grabImage("lit_l.gif"), this).getTexture(),
	    new TextureLoader(grabImage("lit_m.gif"), this).getTexture(),
	    new TextureLoader(grabImage("lit_n.gif"), this).getTexture(),
	    new TextureLoader(grabImage("lit_o.gif"), this).getTexture(),
	    new TextureLoader(grabImage("lit_p.gif"), this).getTexture(),
	    new TextureLoader(grabImage("lit_q.gif"), this).getTexture(),
	    new TextureLoader(grabImage("lit_r.gif"), this).getTexture(),
	    new TextureLoader(grabImage("lit_s.gif"), this).getTexture(),
	    new TextureLoader(grabImage("lit_t.gif"), this).getTexture(),
	    new TextureLoader(grabImage("lit_u.gif"), this).getTexture(),
	    new TextureLoader(grabImage("lit_v.gif"), this).getTexture(),
	    new TextureLoader(grabImage("lit_w.gif"), this).getTexture(),
	    new TextureLoader(grabImage("lit_x.gif"), this).getTexture(),
	    new TextureLoader(grabImage("lit_y.gif"), this).getTexture(),
	    new TextureLoader(grabImage("lit_z.gif"), this).getTexture()
	};
	return textures;
    }
    
    Image grabImage(String name) {
	try {
	    Image image = Toolkit.getDefaultToolkit().createImage((java.awt.image.ImageProducer)((getClass().getResource("/"+name)).getContent()));
	    return image;
	}
	catch(Exception ex) {
	    System.out.println("Exception getting image "+name+": "+ex);
	    return null;
	}
    }

    class ModulateTextureAppearance extends Appearance {
	public ModulateTextureAppearance() {
	    TextureAttributes textureAttributes = new TextureAttributes();
	    textureAttributes.setTextureMode(TextureAttributes.MODULATE);
	    setTextureAttributes(textureAttributes);
	}
    }

    class DecalTextureAppearance extends Appearance {
	public DecalTextureAppearance() {
	    TextureAttributes textureAttributes = new TextureAttributes();
	    textureAttributes.setTextureMode(TextureAttributes.DECAL);
	    setTextureAttributes(textureAttributes);
	}
    }

    private Panel createControls() {
	Panel controls = new Panel();
	Button reset = new Button("Reset View");
	reset.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		resetOriginalViewpoint();
	    }
	});
	Button typing = new Button("Typing View");
	typing.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		setTypingViewpoint();
	    }
	});
	Button rotor = new Button("Rotor View");
	rotor.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		setRotorViewpoint();
	    }
	});
	Button help = new Button("Help");
	help.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		InfoDialog d = 
		    new InfoDialog("Enigma","Instructions for the Java3d Enigma",15, 60);
		d.setText(helpText);
		d.setVisible(true);
	    }
	});
	controls.setBackground(SystemColor.control);
	controls.add(reset);
	controls.add(typing);
	controls.add(rotor);
	controls.add(help);
	return controls;
    }

    private void addQuitButton() {
	if(controls == null)
	    controls = createControls();
	Button quit = new Button("Quit");
	quit.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		System.exit(0);
	    }
	});
	controls.add(quit);
    }

    public static void main(String argv[])
    {
	final Enigma enigma = new Enigma();
	Frame f = new MainFrame(enigma, 500, 500);
	enigma.addQuitButton();
	
	//	f.add(controls, BorderLayout.SOUTH);
	f.pack();
    }
}

