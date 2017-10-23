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
 * 
 * @author Tom Nelson - tomnelson@dev.java.net
 *
 */
public class EnigmaReflector {

  int [] letters;
  int [] rletters;
  int rotation;
  String name;
  int length;

  public EnigmaReflector(String name) {
    this.name = name;
    length = name.length();
    char [] chars = new char [length];
    name.getChars(0, length, chars, 0);
    letters = new int [ length ];
    rletters = new int [ length ];

    for(int i=0; i<length; i++) {
      letters[i] = chars[i]-(int)'A';
      rletters[letters[i]] = i;
    }
  }

  public int getRotation() { return rotation; }

  public int rotate() { return rotate(1); }

  public int rotate(int amt) {
    rotation += amt;
    int carry = rotation / length;
    rotation = mod(rotation,length);
    return carry;
  }

  public boolean setRotation(int i) {
    if(rotation == i) return false;
    rotation = i;
    return true;
  }

  public int value(int c) {
     return mod(letters[mod(c+rotation,length)]-rotation,length);
  }

  public int mod(int val, int len) {
    while(val < 0) { val += len; }
    return val % len;
  }

  public void reset() { rotation = 0; }

  public String toString() { return name; }

  public int reflect(int c) { return value(c); }

  public int rvalue(int c) {
    return mod(rletters[mod(c+rotation,length)]-rotation,length);
  }
}
