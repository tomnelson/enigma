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
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.media.j3d.Texture;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.ImageIcon;
import javax.vecmath.Vector3f;

/**
 * 
 * @author Tom Nelson - tomnelson@dev.java.net
 *
 */
public class EnigmaLightboard extends TransformGroup implements PropertyChangeListener {
   

    PropertyChangeSupport propertyChangeSupport;

    char[] letter = {'Q','W','E','R','T','Z','U','I','O',
		     'A','S','D','F','G','H','J','K',
		     'P','Y','X','C','V','B','N','M','L'};
    
    EnigmaLightSwitch[] lights;
    
    public EnigmaLightboard(Texture[] tex) {

      Transform3D t = new Transform3D();
      lights = new EnigmaLightSwitch[26];

      for(int i=0; i<9; i++) {
	 Vector3f vec = new Vector3f((i*12.f), 0.f, 0.f);
	 t.setTranslation(vec);
	 TransformGroup kt = new TransformGroup();
	 kt.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	 EnigmaLightSwitch light = new EnigmaLightSwitch(letter[i], 
					     tex[letter[i]-'A']);
	 lights[(int)(letter[i]-'A')] = light;
	 light.setTransform(t);
	 kt.addChild(light);
	 addChild(kt);
      }
      for(int i=0; i<8; i++) {
	 Vector3f vec = new Vector3f((6.f+i*12.f), 0.f, 12.f);
	 t.setTranslation(vec);
	 TransformGroup kt = new TransformGroup();
	 kt.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	 EnigmaLightSwitch light = new EnigmaLightSwitch(letter[9+i], 
					     tex[letter[9+i]-'A']);
	 lights[(int)(letter[9+i]-'A')] = light;
	 light.setTransform(t);
	 kt.addChild(light);
	 addChild(kt);
      }
      for(int i=0; i<9; i++) {
	 Vector3f vec = new Vector3f((i*12.f), 0.f, 24.f);
	 t.setTranslation(vec);
	 TransformGroup kt = new TransformGroup();
	 kt.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	 EnigmaLightSwitch light = new EnigmaLightSwitch(letter[17+i], 
					     tex[letter[17+i]-'A']);
	 lights[(int)(letter[17+i]-'A')] = light;
	 light.setTransform(t);
	 kt.addChild(light);
	 addChild(kt);
      }
   }

    Image grabImage(String name) {
	try {
	    ImageIcon icon = new ImageIcon(getClass().getResource(name));
	    return icon.getImage();
	}
	catch(Exception ex) {
	    System.out.println("Exception getting image: "+ex);
	    return null;
	}
    }


   public void propertyChange(PropertyChangeEvent evt) {
//      if(evt.getPropertyName().equals("KeySelection")) {
//	 EnigmaKey key = (EnigmaKey)evt.getNewValue();
//	 char c = key.getKeyChar();
//      }
      if(evt.getPropertyName().equals("LightSelectionRequest")) {
	 int val = ((Integer)evt.getNewValue()).intValue();
	 lights[val].illuminateLight();
      }
   }
}
