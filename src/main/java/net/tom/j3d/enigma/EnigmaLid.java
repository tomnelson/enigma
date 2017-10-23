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
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Box;

/**
 * 
 * @author Tom Nelson - tomnelson@dev.java.net
 *
 */
public class EnigmaLid extends TransformGroup {

   Alpha alpha;
   boolean panelIsOpen=true;
   RotationInterpolator rotationInterpolator;

   public EnigmaLid(Appearance tex, Appearance tex2, Appearance appearance) {
      Transform3D t = new Transform3D();
      t.setTranslation(new Vector3f(0.f,3.f,22.f)); // perfect!

      Transform3D tr = new Transform3D();
      tr.rotX(-(float)Math.PI/3.f);

      tr.mul(t);
      TransformGroup tg = new TransformGroup(tr);
      Box lid = new Box(21.f, 3.f, 22.f, Box.GENERATE_NORMALS |
			Box.GENERATE_TEXTURE_COORDS |
			Box.ENABLE_GEOMETRY_PICKING, appearance);
      lid.getShape(Box.BOTTOM).setAppearance(tex);
      lid.getShape(Box.LEFT).setAppearance(tex2);
      lid.getShape(Box.RIGHT).setAppearance(tex2);
      lid.getShape(Box.FRONT).setAppearance(tex2);
      lid.getShape(Box.BACK).setAppearance(tex2);
      setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
      setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
      setCapability(TransformGroup.ENABLE_PICK_REPORTING);
      tg.addChild(lid);
      addChild(tg);
   }

   public void addInterpolator() {
      alpha = new Alpha(1, Alpha.INCREASING_ENABLE,
			      0, 0,
			      500, 250, 20,
			      0, 0, 0);
      // make a transform to rotate in Z
      Transform3D rot = new Transform3D();
      rot.rotZ(Math.PI/2);

      TransformGroup parent;
      parent = (TransformGroup)getParent();
      rotationInterpolator = 
	 new RotationInterpolator(alpha, parent, rot, 0.0f, 0.0f);
      addChild(rotationInterpolator);
      rotationInterpolator.setSchedulingBounds(new BoundingSphere(new Point3d(0.0,0.0,0.0), 1000.0));

   }

   public void movePanel() {
      if(panelIsOpen) {
	 rotationInterpolator.setMinimumAngle(0.0f);
	 rotationInterpolator.setMaximumAngle(-(float)Math.PI/3.f);
	 panelIsOpen = false;
      }
      else {
	 rotationInterpolator.setMinimumAngle(-(float)Math.PI/3.f);
	 rotationInterpolator.setMaximumAngle(0.0f);
	 panelIsOpen = true;
      }
      try{
	 if(alpha != null) {
	    alpha.setLoopCount(1);
	    alpha.setStartTime(System.currentTimeMillis());
	 }
      }
      catch(Exception ex) {
	 System.out.println("alpha Exception: " +ex);
      }
   }
}
