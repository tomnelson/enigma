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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.jogamp.java3d.Alpha;
import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.RotationInterpolator;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;

/**
 * 
 * @author Tom Nelson - tomnelson@dev.java.net
 *
 */
public class LightPanelGroup extends TransformGroup implements PropertyChangeListener {

   Alpha alpha;
   RotationInterpolator rotationInterpolator;
   LightPanel panel;

   public LightPanelGroup(Appearance appearance) {
      Transform3D t = new Transform3D();
      panel = new LightPanel(appearance);
      panel.setTransform(t);
      TransformGroup panelxform = new TransformGroup();
      panelxform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
      panelxform.addChild(panel);
      addChild(panelxform);
      panel.addInterpolator();
   }

   public void propertyChange(PropertyChangeEvent e) {
      if(e.getPropertyName().equals("LightPanelSelection")) {
	 panel.movePanel();
      }
      else if(e.getPropertyName().equals("EnigmaFrontSelection")) {
	 panel.closePanel();
      }
      else if(e.getPropertyName().equals("EnigmaLidSelection")) {
	 panel.closePanel();
      }
   }
}
