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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.Vector3f;

/**
 * 
 * @author Tom Nelson - tomnelson@dev.java.net
 *
 */
public class EnigmaRotorGroup extends TransformGroup implements PropertyChangeListener  {

   EnigmaRotor rotorOne, rotorTwo, rotorThree;
   EnigmaReflector reflector;
   String rotorOneString   = "EKMFLGDQVZNTOWYHXUSPAIBRCJ";
   String rotorTwoString   = "AJDKSIRUXBLHWTMCQGZNPYFVOE";
   String rotorThreeString = "BDFHJLCPRTXVZNYEIWGAKMUSQO";
   String reflectorString  = "YRUHQSLDPXNGOKMIEBFZCWVJAT";
   PropertyChangeSupport propertyChangeSupport;

   public EnigmaRotorGroup(Appearance look, Appearance texturedApp, 
			   AudioClip sound) {
      // Controls the placement of each separate rotor
      Transform3D t = new Transform3D();
      t.rotY(-Math.PI/1.8);
      t.setTranslation(new Vector3f(0.f, -3.4f, 0.f));
      rotorOne = new EnigmaRotor(rotorOneString, 5.f, 3.f, 
				 look, texturedApp, sound);
      rotorOne.setTransform(t);
      TransformGroup r1xform = new TransformGroup();
      r1xform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
      r1xform.addChild(rotorOne);
      addChild(r1xform);
      rotorOne.addInterpolator();
      
      t.setTranslation(new Vector3f(0.f, 0.f, 0.f));
      rotorTwo = new EnigmaRotor(rotorTwoString, 5.f, 3.f, 
				 look, texturedApp, sound);
      rotorTwo.setTransform(t);
      TransformGroup r2xform = new TransformGroup();
      r2xform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
      r2xform.addChild(rotorTwo);
      addChild(r2xform);
      rotorTwo.addInterpolator();

      t.setTranslation(new Vector3f(0.f, 3.4f, 0.f));
      rotorThree = new EnigmaRotor(rotorThreeString, 5.f, 3.f, 
				   look, texturedApp, sound);
      rotorThree.setTransform(t);
      TransformGroup r3xform = new TransformGroup();
      r3xform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
      r3xform.addChild(rotorThree);
      addChild(r3xform);
      rotorThree.addInterpolator();

      reflector = new EnigmaReflector(reflectorString);
   }

   public void propertyChange(PropertyChangeEvent evt) {
      if(evt.getPropertyName().equals("KeySelection")) {
	 EnigmaKey key = (EnigmaKey)evt.getNewValue();
	 char c = key.getKeyChar();
	 propertyChangeSupport.firePropertyChange("LightSelectionRequest", 
						  null, 
						  new Integer(value(c-'A')));
	 rotate();
      }
      else if(evt.getPropertyName().equals("RotorSelection")) {
//	 System.out.println("RotorSelection");
	 EnigmaRotor rotor = (EnigmaRotor)evt.getNewValue();
	 rotor.rotate(1);
      }
      else if(evt.getPropertyName().equals("ReverseRotorSelection")) {
//	 System.out.println("ReverseRotorSelection");
	 EnigmaRotor rotor = (EnigmaRotor)evt.getNewValue();
	 rotor.rotate(-1);
      }
   }

   public void rotate () { 
      rotorThree.rotate(rotorTwo.rotate(rotorOne.rotate())); 
   }

   public char charValue(int i) {
      int offset = rotorOne.rvalue(rotorTwo.rvalue(rotorThree.rvalue(reflector.reflect(rotorThree.value(rotorTwo.value(rotorOne.value(i)))))));
      char val = (char)('A' + offset);
      return val;
   }
   
   public int value(int i) {
      int offset = rotorOne.rvalue(rotorTwo.rvalue(rotorThree.rvalue(reflector.reflect(rotorThree.value(rotorTwo.value(rotorOne.value(i)))))));
      return offset;
   }

   public char value(char c) {
      char output = charValue(c - 'A');
      return output;
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
