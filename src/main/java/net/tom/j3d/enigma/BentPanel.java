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
public class BentPanel extends Shape3D {

   Point3f p0 = new Point3f( 20.f, 0.f, 12.f);
   Point3f p1 = new Point3f( 20.f, 0.f, -12.f);
   Point3f p2 = new Point3f(-20.f, 0.f, -12.f);
   Point3f p3 = new Point3f(-20.f, 0.f, 12.f);
   Point3f p4 = new Point3f(20.f, -.6f, 13.2f);
   Point3f p5 = new Point3f(-20.f, -.6f, 13.2f);

   Point3f[] verts = new Point3f[16];
   Vector3f[] norms = new Vector3f[16];

   private static final float[] textCoords = {
      // top
      1.0f,  0.1f,            1.0f,  1.0f,
      0.0f,  1.0f,            0.0f,  0.1f,

      1.0f,  0.0f,            1.0f,  0.1f,
      0.0f,  0.1f,            0.0f,  0.0f,

      // bottom 
      1.0f,  0.1f,            0.0f,  0.1f,
      0.0f,  1.0f,            1.0f,  1.0f,

      1.0f,  0.0f,            0.0f,  0.0f,
      0.0f,  0.1f,            1.0f,  0.1f
   };

   public BentPanel(Appearance app) {

      verts[0] = p0;
      verts[1] = p1;
      verts[2] = p2;
      verts[3] = p3;

      verts[4] = p4;
      verts[5] = p0;
      verts[6] = p3;
      verts[7] = p5;

      verts[8] = p0;
      verts[9] = p3;
      verts[10] = p2;
      verts[11] = p1;

      verts[12] = p4;
      verts[13] = p5;
      verts[14] = p3;
      verts[15] = p0;

      QuadArray quad = new QuadArray(16, QuadArray.COORDINATES | 
				     QuadArray.NORMALS |
				     QuadArray.TEXTURE_COORDINATE_2);

      Vector3f upNormal = new Vector3f(0.f, 1.f, 0.f);
      Vector3f downNormal = new Vector3f(0.f, -1.f, 0.f);
      Vector3f tempVector1 = new Vector3f(0.0f, 0.0f, 0.0f);
      Vector3f tempVector2 = new Vector3f(0.0f, 0.0f, 0.0f);
      Vector3f normal2 = new Vector3f(0.f, 1.f, 0.f);
      Vector3f normal1 = new Vector3f(0.f, 1.f, 0.f);
      tempVector1.sub(p4, p5);
      tempVector2.sub(p0, p4);

      normal2.cross(tempVector1, tempVector2);
      normal2.normalize();

      normal1.cross(tempVector2, tempVector1);
      normal1.normalize();

      

      norms[0] = upNormal;
      norms[1] = upNormal;
      norms[2] = upNormal;
      norms[3] = upNormal;

      norms[4] = normal2;
      norms[5] = normal2;
      norms[6] = normal2;
      norms[7] = normal2;

      norms[8] = downNormal;
      norms[9] = downNormal;
      norms[10] = downNormal;
      norms[11] = downNormal;

      norms[12] = normal1;
      norms[13] = normal1;
      norms[14] = normal1;
      norms[15] = normal1;

      quad.setCoordinates(0, verts);
      quad.setNormals(0,norms);
      quad.setTextureCoordinates(0, 0, textCoords);
      quad.setCapability(Geometry.ALLOW_INTERSECT);
      setGeometry(quad);
      setAppearance(app);
   }

//   public static void main(String[] args) {
//      BentPanel kp = new BentPanel(new Appearance());
//   }
}

