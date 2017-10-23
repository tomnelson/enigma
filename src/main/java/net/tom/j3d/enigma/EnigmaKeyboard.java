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

import javax.media.j3d.Appearance;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

/**
 * 
 * @author Tom Nelson - tomnelson@dev.java.net
 *
 */
public class EnigmaKeyboard extends TransformGroup implements PropertyChangeListener {
   
   PropertyChangeSupport propertyChangeSupport;

   char[] letter = {'Q','W','E','R','T','Z','U','I','O',
		    'A','S','D','F','G','H','J','K',
		  'P','Y','X','C','V','B','N','M','L'};
   AudioClip sound;

   public EnigmaKeyboard(Appearance[] tex, Appearance look, AudioClip sound) {
      this.sound = sound;
      Transform3D t = new Transform3D();

      for(int i=0; i<9; i++) {
	 Vector3f vec = new Vector3f((i*12.f), 0.f, 0.f);
	 t.setTranslation(vec);
	 TransformGroup kt = new TransformGroup();
	 kt.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	 EnigmaKey key = new EnigmaKey(letter[i], 
				       tex[letter[i]-'A'], look);
	 key.setTransform(t);
	 kt.addChild(key);
	 addChild(kt);
	 key.addInterpolator();
      }
      for(int i=0; i<8; i++) {
	 Vector3f vec = new Vector3f((6.f+i*12.f), -2.f, 12.f);
	 t.setTranslation(vec);
	 TransformGroup kt = new TransformGroup();
	 kt.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	 EnigmaKey key = new EnigmaKey(letter[9+i], 
				       tex[letter[9+i]-'A'], look);
	 key.setTransform(t);
	 kt.addChild(key);
	 addChild(kt);
	 key.addInterpolator();
      }
      for(int i=0; i<9; i++) {
	 Vector3f vec = new Vector3f((i*12.f), -4.f, 24.f);
	 t.setTranslation(vec);
	 TransformGroup kt = new TransformGroup();
	 kt.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	 EnigmaKey key = new EnigmaKey(letter[17+i], 
				       tex[letter[17+i]-'A'], look);
	 key.setTransform(t);
	 kt.addChild(key);
	 addChild(kt);
	 key.addInterpolator();
      }
   }

   public void propertyChange(PropertyChangeEvent evt) {
      if(evt.getPropertyName().equals("KeySelection")) {
	 EnigmaKey key = (EnigmaKey)evt.getNewValue();
	 //System.out.println("You pressed "+key.getKeyChar());
	 sound.play();//setEnable(true);
	 key.moveKey();
      }
   }
}
