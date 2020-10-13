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
public class FlatPanel extends Shape3D {

   Point3f p0 = new Point3f( 1.f, 0.f, 1.f);
   Point3f p1 = new Point3f( 1.f, 0.f, -1.f);
   Point3f p2 = new Point3f(-1.f, 0.f, -1.f);
   Point3f p3 = new Point3f(-1.f, 0.f, 1.f);

   Point3f[] verts = new Point3f[4];
   Vector3f[] norms = new Vector3f[4];

   private static final float[] textCoords = {
      1.0f,  0.0f,            1.0f,  1.0f,
      0.0f,  1.0f,            0.0f,  0.0f
   };

   public FlatPanel(Appearance app) {

      verts[0] = p0;
      verts[1] = p1;
      verts[2] = p2;
      verts[3] = p3;

      QuadArray quad = new QuadArray(4, QuadArray.COORDINATES | 
				     QuadArray.NORMALS |
				     QuadArray.TEXTURE_COORDINATE_2);

      Vector3f upNormal = new Vector3f(0.f, 1.f, 0.f);

      norms[0] = upNormal;
      norms[1] = upNormal;
      norms[2] = upNormal;
      norms[3] = upNormal;

      quad.setCoordinates(0, verts);
      quad.setNormals(0,norms);
      quad.setTextureCoordinates(0, 0, textCoords);
      quad.setCapability(Geometry.ALLOW_INTERSECT);
      setGeometry(quad);
      setAppearance(app);
   }

//   public static void main(String[] args) {
//      FlatPanel kp = new FlatPanel(new Appearance());
//   }
}

