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
import java.applet.AudioClip;

import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Cylinder;

/**
 * 
 * @author Tom Nelson - tomnelson@dev.java.net
 *
 */public class EnigmaRotor extends TransformGroup {
   
   Alpha alpha;
   int currentSetting;
   float delta = -(float)(Math.PI/13.);
   int [] letters;
   int [] rletters;
   String name;
   int length;
   RotationInterpolator rotationInterpolator;
//   Sound sound;
   AudioClip sound;

   public EnigmaRotor(String name, float radius, float height,
		      Appearance look, Appearance texturedApp, AudioClip sound) {
      
      this.sound = sound;
      setArrays(name);
      setCapability(TransformGroup.ENABLE_PICK_REPORTING);

      Cylinder cylinder = new Cylinder(radius, height, 
			      Cylinder.GENERATE_NORMALS |
			      Cylinder.GENERATE_TEXTURE_COORDS |
			Cylinder.ENABLE_GEOMETRY_PICKING,
			      26, 26, texturedApp);

      cylinder.getShape(Cylinder.BOTTOM).setAppearance(look);
      cylinder.getShape(Cylinder.TOP).setAppearance(look);
      addChild(cylinder);

      Transform3D t = new Transform3D();
      t.setTranslation(new Vector3f(0.f, -height/2+.2f, 0.f));
      TransformGroup grp = new TransformGroup(t);
      RotorGear gear = new RotorGear(0,0,radius+0.5f,radius+0.1f,
				     0.4f,26,look);
      grp.addChild(gear.getShape());
      grp.addChild(gear.getTop());
      grp.addChild(gear.getBottom());
      addChild(grp);
   }

   public void addInterpolator() {
      alpha = new Alpha(1, Alpha.INCREASING_ENABLE,
			      0, 0,
			      500, 250, 20,
			      0, 0, 0);
      // make a transform to rotate in Z
      Transform3D rot = new Transform3D();
      //rot.rotZ(Math.PI/2);

      TransformGroup parent;
      parent = (TransformGroup)getParent();

      rotationInterpolator = 
	 new RotationInterpolator(alpha, parent, rot, 0.0f, 0.0f);
      addChild(rotationInterpolator);
      rotationInterpolator.setSchedulingBounds(new BoundingSphere(new Point3d(0.0,0.0,0.0), 1000.0));

   }

   public int getCurrentSetting() { return mod(currentSetting, length); }

   // encoding and decoding behavior code follows....

   /** Puts the string name into the two arrays used
     * to encode and decode.
     */
   void setArrays(String name) {
      this.name = name;
      length = name.length();
      char [] chars = new char[length];
      name.getChars(0, length, chars, 0);
      letters = new int[length];
      rletters = new int[length];
      
      for(int i=0; i<length; i++) {
	 letters[i] = chars[i]-(int)'A';
	 rletters[letters[i]] = i;
      }
   }

   public int rotate() { return rotate(1); }

   public int rotate(int amt) {

      int newSetting = currentSetting + amt;
      int carry = newSetting/length;
      
//      System.out.println("rotate("+amt+") from "+currentSetting*delta+" to "+
//			 newSetting*delta);
      rotationInterpolator.setMinimumAngle(currentSetting*delta);
      rotationInterpolator.setMaximumAngle(newSetting*delta);
//      sound.setEnable(true);
      sound.play();
      try{
	 if(alpha != null) {
	    alpha.setStartTime(System.currentTimeMillis());
	 }
      }
      catch(Exception ex) {
	 System.out.println("alpha Exception: " +ex);
      }
      currentSetting = mod(newSetting, length);
      return carry;
   }

   public int mod(int val, int len) {
      while(val < 0) { val += len; }
      return val % len;
   }
   
   public void reset() { currentSetting = 0; }

   public String toString() { return name; }

   public int value(int c) {
      int setting = mod(currentSetting, length);
      return mod(letters[mod(c+setting, length)]-setting,
		 length);
   }

   public int rvalue(int c) {
      int setting = mod(currentSetting, length);
      return mod(rletters[mod(c+setting, length)]-setting, 
		 length);
   }
}
