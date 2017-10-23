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
 */
import java.util.Enumeration;

import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.ColorInterpolator;
import javax.media.j3d.Material;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Cylinder;

/**
 * 
 * @author Tom Nelson - tomnelson@dev.java.net
 *
 */
public class EnigmaLightSwitch extends TransformGroup {
   
    Alpha alpha;
    float capRadius   = 5.f;
    float capHeight   = .1f;
    char lightChar;
    Cylinder lightOnCap;
    Cylinder lightOffCap;

    static TextureAttributes textureAttributes;
    static Color3f black = new Color3f(0.f, 0.f, 0.f);
    static Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
    static Color3f paleYellow = new Color3f(1.0f, 1.0f, 0.7f);
    static Color3f gray  = new Color3f(.1f, .1f, .1f);
    
    static {
	textureAttributes = new TextureAttributes();
	textureAttributes.setTextureMode(TextureAttributes.DECAL);
    }

    public EnigmaLightSwitch(char lightChar, Texture texture) {
	this.lightChar = lightChar;
	
  	Material unlitMaterial = new Material(gray, black,
  					      gray, white, 100.0f);

	unlitMaterial.setCapability(Material.ALLOW_COMPONENT_READ);
	unlitMaterial.setCapability(Material.ALLOW_COMPONENT_WRITE);
	
	Transform3D t = new Transform3D();
	
	setCapability(TransformGroup.ENABLE_PICK_REPORTING);
	setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	t.setTranslation(new Vector3f(0.f, capHeight/2.f, 0.f));
	TransformGroup capGroup = new TransformGroup(t);
	
	Appearance cylinderAppearance = new Appearance();
	cylinderAppearance.setMaterial(unlitMaterial);

	lightOffCap = new Cylinder(capRadius, capHeight, 
				   Cylinder.GENERATE_NORMALS |
				   Cylinder.GENERATE_TEXTURE_COORDS |
				   Cylinder.ENABLE_GEOMETRY_PICKING, // |
				   //Cylinder.ENABLE_APPEARANCE_MODIFY,
				   16, 16, cylinderAppearance);
	Appearance offTex = new Appearance();
	offTex.setTextureAttributes(textureAttributes);
	offTex.setTexture(texture);
	offTex.setMaterial(unlitMaterial);
	lightOffCap.getShape(Cylinder.TOP).setAppearance(offTex);

	capGroup.addChild(lightOffCap);
	addChild(capGroup);
	alpha = new Alpha(0, Alpha.INCREASING_ENABLE|Alpha.DECREASING_ENABLE, 
			  10, 200, 10, 10, 1000, 10, 10, 1000);
	
	ColorInterpolator colorInterpolator = 
	    new EmissiveColorInterpolator(alpha, unlitMaterial, gray, paleYellow);
	colorInterpolator.setSchedulingBounds(new BoundingSphere(new Point3d(0.0,0.0,0.0), 1000.0));
	addChild(colorInterpolator);
    }

    class EmissiveColorInterpolator extends ColorInterpolator {
	Color3f newEmissiveColor;
	Color3f startEmissiveColor;
	Color3f endEmissiveColor;
	public EmissiveColorInterpolator(Alpha alpha, Material material, Color3f startColor, Color3f endColor) {
	    super(alpha, material, startColor, endColor);
	    this.startEmissiveColor = startColor;
	    this.endEmissiveColor = endColor;
	    newEmissiveColor = new Color3f();
	}
	public void processStimulus(Enumeration enumeration)  {
	    if(alpha != null)   {
		float f = alpha.value();
		newEmissiveColor.x = (1.0F - f) * startEmissiveColor.x + f * endEmissiveColor.x;
		newEmissiveColor.y = (1.0F - f) * startEmissiveColor.y + f * endEmissiveColor.y;
		newEmissiveColor.z = (1.0F - f) * startEmissiveColor.z + f * endEmissiveColor.z;
		((Material)getTarget()).setEmissiveColor(newEmissiveColor);
	    }
	    wakeupOn(defaultWakeupCriterion);
	}
    }
    
    public void illuminateLight() {
	alpha.setLoopCount(1);
	alpha.setStartTime(System.currentTimeMillis());
    }
}
