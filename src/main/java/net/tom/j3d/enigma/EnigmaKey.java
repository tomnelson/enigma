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
import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.PositionInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Cylinder;

/**
 * 
 * @author Tom Nelson - tomnelson@dev.java.net
 *
 */
public class EnigmaKey extends TransformGroup {
   
   Alpha alpha;
   float capRadius   = 5.f;
   float capHeight   = 2.f;
   float shaftRadius = 1.f;
   float shaftHeight = 10.f;
   char keyChar;

   public EnigmaKey(char keyChar, Appearance tex, Appearance look) {
      
      this.keyChar = keyChar;

      Transform3D t = new Transform3D();

      setCapability(TransformGroup.ENABLE_PICK_REPORTING);
      t.setTranslation(new Vector3f(0.f, shaftHeight+capHeight/2.f, 0.f));
      TransformGroup capGroup = new TransformGroup(t);
      Cylinder keyCap = new Cylinder(capRadius, capHeight, 
				     Cylinder.GENERATE_NORMALS |
				     Cylinder.GENERATE_TEXTURE_COORDS |
				     Cylinder.ENABLE_GEOMETRY_PICKING,
				     16, 16, look);
      keyCap.getShape(Cylinder.TOP).setAppearance(tex);
      capGroup.addChild(keyCap);
      
      t.setTranslation(new Vector3f(0.f, shaftHeight/2.f, 0.f));
      TransformGroup shaftGroup = new TransformGroup(t);
      Cylinder keyShaft = new Cylinder(shaftRadius, shaftHeight, 
				       Cylinder.GENERATE_NORMALS |
				       Cylinder.ENABLE_GEOMETRY_PICKING,
			      10, 10, look);
      shaftGroup.addChild(keyShaft);

      addChild(capGroup);
      addChild(shaftGroup);

   }

   public void addInterpolator() {
      alpha = new Alpha(1, Alpha.INCREASING_ENABLE |
			      Alpha.DECREASING_ENABLE,
			      0, 0,
			      500, 250, 20,
			      500, 250, 20);
      // make a transform to rotate in Z
      Transform3D rot = new Transform3D();
      rot.rotZ(Math.PI/2);

      TransformGroup parent;
      parent = (TransformGroup)getParent();

      PositionInterpolator pi = 
	 new PositionInterpolator(alpha, parent, rot, 0.f, -10.f);
      addChild(pi);
      pi.setSchedulingBounds(new BoundingSphere(new Point3d(0.0,0.0,0.0), 1000.0));

   }

   public void moveKey() {
      if(alpha != null) {
	 alpha.setStartTime(System.currentTimeMillis());
      }
   }

   public char getKeyChar() { return keyChar; }

}
