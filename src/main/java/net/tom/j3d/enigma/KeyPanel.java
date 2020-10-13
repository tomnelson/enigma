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
import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.Geometry;
import org.jogamp.java3d.QuadArray;
import org.jogamp.java3d.Shape3D;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.Vector3f;

/**
 * 
 * @author Tom Nelson - tomnelson@dev.java.net
 *
 */
public class KeyPanel extends Shape3D {

   Point3f p0 = new Point3f(1.f, -.4f, 2.f);
   Point3f p1 = new Point3f(1.f, -.4f, 1.1f);
   Point3f p2 = new Point3f(1.f, -.2f, 1.f);
   Point3f p3 = new Point3f(1.f, -.2f, .6f);
   Point3f p4 = new Point3f(1.f, 0.f, .5f);
   Point3f p5 = new Point3f(1.f, 0.f, 0.f);
   Point3f p6 = new Point3f(-1.f, 0.f, 0.f);
   Point3f p7 = new Point3f(-1.f, 0.f, .5f);
   Point3f p8 = new Point3f(-1.f, -.2f, .6f);
   Point3f p9 = new Point3f(-1.f, -.2f, 1.f);
   Point3f p10 = new Point3f(-1.f, -.4f, 1.1f);
   Point3f p11 = new Point3f(-1.f, -.4f, 2.f);

   Point3f[] verts = new Point3f[20];
   Vector3f[] norms = new Vector3f[20];

   private static final float[] textCoords = {
      1.0f,  0.0f,            1.0f,  1.0f,
      0.0f,  1.0f,            0.0f,  0.0f
   };

   

   public KeyPanel(Appearance app) {

      verts[0] = p0;
      verts[1] = p1;
      verts[2] = p10;
      verts[3] = p11;

      verts[4] = p1;
      verts[5] = p2;
      verts[6] = p9;
      verts[7] = p10;

      verts[8] = p2;
      verts[9] = p3;
      verts[10] = p8;
      verts[11] = p9;

      verts[12] = p3;
      verts[13] = p4;
      verts[14] = p7;
      verts[15] = p8;

      verts[16] = p4;
      verts[17] = p5;
      verts[18] = p6;
      verts[19] = p7;
       
      QuadArray quad = new QuadArray(20, QuadArray.COORDINATES | 
				     QuadArray.NORMALS |
				     QuadArray.TEXTURE_COORDINATE_2);

      Vector3f tempVector1 = new Vector3f(0.0f, 0.0f, 0.0f);
      Vector3f tempVector2 = new Vector3f(0.0f, 0.0f, 0.0f);
      Vector3f upNormal = new Vector3f(0.f, 1.f, 0.f);
      Vector3f normal2 = new Vector3f(0.f, 1.f, 0.f);
      Vector3f normal3 = new Vector3f(0.f, 1.f, 0.f);

      tempVector1.sub(p1, p10);
      tempVector2.sub(p2, p1);

      normal2.cross(tempVector1, tempVector2);
      normal2.normalize();

      tempVector1.sub(p3, p8);
      tempVector2.sub(p4, p3);
      normal3.cross(tempVector1, tempVector2);
      normal3.normalize();

//      System.out.println(""+upNormal);
//      System.out.println(""+normal2);
//      System.out.println(""+normal3);

      norms[0] = upNormal;
      norms[1] = upNormal;
      norms[2] = upNormal;
      norms[3] = upNormal;
      norms[4] = normal2;
      norms[5] = normal2;
      norms[6] = normal2;
      norms[7] = normal2;
      norms[8] = upNormal;
      norms[9] = upNormal;
      norms[10] = upNormal;
      norms[11] = upNormal;
      norms[12] = normal3;
      norms[13] = normal3;
      norms[14] = normal3;
      norms[15] = normal3;
      norms[16] = upNormal;
      norms[17] = upNormal;
      norms[18] = upNormal;
      norms[19] = upNormal;

      quad.setCoordinates(0, verts);
      quad.setNormals(0,norms);
      quad.setTextureCoordinates(0, 0, textCoords);
      quad.setCapability(Geometry.ALLOW_INTERSECT);
      setGeometry(quad);
      setAppearance(app);

   }

//   public static void main(String[] args) {
//      KeyPanel kp = new KeyPanel(new Appearance());
//   }
}

