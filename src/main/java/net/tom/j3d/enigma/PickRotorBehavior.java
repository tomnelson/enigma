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
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.jogamp.java3d.Bounds;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Canvas3D;
import org.jogamp.java3d.TransformGroup;

import org.jogamp.java3d.utils.picking.PickResult;
import org.jogamp.java3d.utils.picking.PickTool;
import org.jogamp.java3d.utils.picking.behaviors.PickMouseBehavior;

/**
 * 
 * @author Tom Nelson - tomnelson@dev.java.net
 *
 */
public class PickRotorBehavior extends PickMouseBehavior {

   PropertyChangeSupport propertyChangeSupport;
   Bounds bounds;
   BranchGroup root;

   public PickRotorBehavior(BranchGroup root, Canvas3D canvas, Bounds bounds){
      super(canvas, root, bounds);
      this.setSchedulingBounds(bounds);
      this.bounds = bounds;
      this.root = root;
      pickCanvas.setMode(PickTool.GEOMETRY);
   }

   public void updateScene(int xpos, int ypos){

	   TransformGroup primitive = null;

	   if(!mevent.isAltDown() && !mevent.isMetaDown()) {  // ButtonOne
		   pickCanvas.setShapeLocation(xpos, ypos);
		   PickResult result = pickCanvas.pickClosest();
		   if(result != null) {
			   primitive = (TransformGroup)result.getNode(PickResult.TRANSFORM_GROUP);
		   }

		   if(primitive != null && primitive instanceof EnigmaRotor) {
			   EnigmaRotor rotor = (EnigmaRotor)primitive;
			   propertyChangeSupport.firePropertyChange("RotorSelection", null, rotor);
		   }
	   }
	   else if(!mevent.isAltDown() && mevent.isMetaDown()) {  // ButtonThree
		   pickCanvas.setShapeLocation(xpos, ypos);
		   PickResult result = pickCanvas.pickClosest();
		   if(result != null) {
			   primitive = (TransformGroup)result.getNode(PickResult.TRANSFORM_GROUP);
		   }

		   if(primitive != null && primitive instanceof EnigmaRotor) {
			   EnigmaRotor rotor = (EnigmaRotor)primitive;
			   propertyChangeSupport.firePropertyChange("ReverseRotorSelection", null, rotor);
		   }
	   }
   }

   public void addPropertyChangeListener(PropertyChangeListener l) {
      if(propertyChangeSupport == null) {
	 propertyChangeSupport = new PropertyChangeSupport(this);
      }
      propertyChangeSupport.addPropertyChangeListener(l);
   }
   
   public void removePropertyChangeListener(PropertyChangeListener l) {
      if(propertyChangeSupport == null) {
	 propertyChangeSupport = new PropertyChangeSupport(this);
      }
      propertyChangeSupport.removePropertyChangeListener(l);
   }
}

