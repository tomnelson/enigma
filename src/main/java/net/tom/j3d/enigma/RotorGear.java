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
import javax.media.j3d.Appearance;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TriangleFanArray;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * 
 * @author Tom Nelson - tomnelson@dev.java.net
 *
 */
public class RotorGear {
   
   float verts[];
   float normals[];
   QuadArray quad = null;
   float div = 3.0f;
   Shape3D shape;
   Shape3D top;
   Shape3D bottom;
   float length;
   float radius1;
   float radius2;
   Appearance app;

   public RotorGear(float x, float z, float radius1, float radius2, 
		    float length, int toothCount, Appearance a) {
      this.length = length;
      this.app = a;
      this.radius1 = radius1;
      this.radius2 = radius2;
      if (toothCount < 3) toothCount = 3;

	// Temporary variables for storing coordinates and vectors 
      Point3f tempCoordinate1 = new Point3f(0.0f, 0.0f, 0.0f);
      Point3f tempCoordinate2 = new Point3f(0.0f, 0.0f, 0.0f);
      Vector3f tempVector1 = new Vector3f(0.0f, 0.0f, 0.0f);
      Vector3f leftNormal = new Vector3f(-1.0f, 0.0f, 0.0f);
      Vector3f rightNormal = new Vector3f(1.0f, 0.0f, 0.0f);
      Vector3f upNormal = new Vector3f(0.0f, 1.0f, 0.0f);
//      Vector3f downNormal = new Vector3f(0.0f, -1.0f, 0.0f);

      div = (float) toothCount*4.0f;
      
      verts = new float[toothCount*12*4];
      normals = new float[toothCount*12*4];

//      float prev_radius = radius1;
//      float radius = radius2;

      float inc = 2.f*(float)Math.PI/div;
      for (int i=0; i< toothCount*4; i+=4){
	 
	 //top of tooth
	 float z1 = radius1 * (float)Math.sin(i*inc) + z;
	 float x1 = radius1 * (float)Math.cos(i*inc) + x;
	 float z2 = radius1 * (float)Math.sin((i+1)*inc) + z;
	 float x2 = radius1 * (float)Math.cos((i+1)*inc) + x;

	 // edge of tooth
	 float z3 = radius1 * (float)Math.sin((i+1)*inc) + z;
	 float x3 = radius1 * (float)Math.cos((i+1)*inc) + x;
	 float z4 = radius2 * (float)Math.sin((i+2)*inc) + z;
	 float x4 = radius2 * (float)Math.cos((i+2)*inc) + x;

	 // valley of tooth
	 float z5 = radius2 * (float)Math.sin((i+2)*inc) + z;
	 float x5 = radius2 * (float)Math.cos((i+2)*inc) + x;
	 float z6 = radius2 * (float)Math.sin((i+3)*inc) + z;
	 float x6 = radius2 * (float)Math.cos((i+3)*inc) + x;

	 // edge of tooth
	 float z7 = radius2 * (float)Math.sin((i+3)*inc) + z;
	 float x7 = radius2 * (float)Math.cos((i+3)*inc) + x;
	 float z8 = radius1 * (float)Math.sin((i+4)*inc) + z;
	 float x8 = radius1 * (float)Math.cos((i+4)*inc) + x;

	 verts[12*i]    = x1;
	 verts[12*i+1]  = -length/2.f;
	 verts[12*i+2]  = z1;
	 verts[12*i+3]  = x1;
	 verts[12*i+4]  = length/2.f;
	 verts[12*i+5]  = z1;
	 verts[12*i+6]  = x2;
	 verts[12*i+7]  = length/2.f;
	 verts[12*i+8]  = z2;
	 verts[12*i+9]  = x2;
	 verts[12*i+10] = -length/2.f;
	 verts[12*i+11] = z2;
	 
	 verts[12*i+12]  = x3;
	 verts[12*i+13]  = -length/2.f;
	 verts[12*i+14]  = z3;
	 verts[12*i+15]  = x3;
	 verts[12*i+16]  = length/2.f;
	 verts[12*i+17]  = z3;
	 verts[12*i+18]  = x4;
	 verts[12*i+19]  = length/2.f;
	 verts[12*i+20]  = z4;
	 verts[12*i+21]  = x4;
	 verts[12*i+22] = -length/2.f;
	 verts[12*i+23] = z4;
	 
	 verts[12*i+24]    = x5;
	 verts[12*i+25]  = -length/2.f;
	 verts[12*i+26]  = z5;
	 verts[12*i+27]  = x5;
	 verts[12*i+28]  = length/2.f;
	 verts[12*i+29]  = z5;
	 verts[12*i+30]  = x6;
	 verts[12*i+31]  = length/2.f;
	 verts[12*i+32]  = z6;
	 verts[12*i+33]  = x6;
	 verts[12*i+34] = -length/2.f;
	 verts[12*i+35] = z6;
	 
	 verts[12*i+36]    = x7;
	 verts[12*i+37]  = -length/2.f;
	 verts[12*i+38]  = z7;
	 verts[12*i+39]  = x7;
	 verts[12*i+40]  = length/2.f;
	 verts[12*i+41]  = z7;
	 verts[12*i+42]  = x8;
	 verts[12*i+43]  = length/2.f;
	 verts[12*i+44]  = z8;
	 verts[12*i+45]  = x8;
	 verts[12*i+46] = -length/2.f;
	 verts[12*i+47] = z8;
	 
	 float nz1 = (float)Math.sin(i*inc);
	 float nx1 = (float)Math.cos(i*inc);
	 float nz2 = (float)Math.sin((i+1)*inc);
	 float nx2 = (float)Math.cos((i+1)*inc);

	 float nz5 = (float)Math.sin((i+4)*inc);
	 float nx5 = (float)Math.cos((i+4)*inc);
	 float nz6 = (float)Math.sin((i+5)*inc);
	 float nx6 = (float)Math.cos((i+5)*inc);

	 //top - 
	 normals[12*i]   = nx1;
	 normals[12*i+1] = 0.0f;
	 normals[12*i+2] = nz1;
	 normals[12*i+3] = nx1;
	 normals[12*i+4] = 0.0f;
	 normals[12*i+5] = nz1;
	 normals[12*i+6] = nx2;
	 normals[12*i+7] = 0.0f;
	 normals[12*i+8] = nz2;
	 normals[12*i+9] = nx2;
	 normals[12*i+10] = 0.0f;
	 normals[12*i+11] = nz2;

	 // decline
	 tempCoordinate1.set(x3, 0.f, z3);
	 tempCoordinate2.set(x4, 0.f, z4);
	 tempVector1.sub(tempCoordinate2, tempCoordinate1);
	 leftNormal.cross(upNormal, tempVector1);
	 leftNormal.normalize();

	 normals[12*i+12] = leftNormal.x;
	 normals[12*i+13] = leftNormal.y;
	 normals[12*i+14] = leftNormal.z;
	 normals[12*i+15] = leftNormal.x;
	 normals[12*i+16] = leftNormal.y;
	 normals[12*i+17] = leftNormal.z;
	 normals[12*i+18] = leftNormal.x;
	 normals[12*i+19] = leftNormal.y;
	 normals[12*i+20] = leftNormal.z;
	 normals[12*i+21] = leftNormal.x;
	 normals[12*i+22] = leftNormal.y;
	 normals[12*i+23] = leftNormal.z;

	 //valley
	 normals[12*i+24] = nx5;
	 normals[12*i+25] = 0.0f;
	 normals[12*i+26] = nz5;
	 normals[12*i+27] = nx5;
	 normals[12*i+28] = 0.0f;
	 normals[12*i+29] = nz5;
	 normals[12*i+30] = nx6;
	 normals[12*i+31] = 0.0f;
	 normals[12*i+32] = nz6;
	 normals[12*i+33] = nx6;
	 normals[12*i+34] = 0.0f;
	 normals[12*i+35] = nz6;

	 // incline
	 tempCoordinate1.set(x7, 0.f, z7);
	 tempCoordinate2.set(x8, 0.f, z8);
	 tempVector1.sub(tempCoordinate2, tempCoordinate1);
	 rightNormal.cross(upNormal, tempVector1);
	 rightNormal.normalize();

	 normals[12*i+36] = rightNormal.x;
	 normals[12*i+37] = rightNormal.y;
	 normals[12*i+38] = rightNormal.z;
	 normals[12*i+39] = rightNormal.x;
	 normals[12*i+40] = rightNormal.y;
	 normals[12*i+41] = rightNormal.z;
	 normals[12*i+42] = rightNormal.x;
	 normals[12*i+43] = rightNormal.y;
	 normals[12*i+44] = rightNormal.z;
	 normals[12*i+45] = rightNormal.x;
	 normals[12*i+46] = rightNormal.y;
	 normals[12*i+47] = rightNormal.z;

      }
      
      quad = new QuadArray(toothCount*4*4, QuadArray.COORDINATES |
                                      QuadArray.NORMALS );
      quad.setCoordinates(0, verts);
      quad.setNormals(0, normals);
      quad.setCapability(Geometry.ALLOW_INTERSECT);
      shape = new Shape3D(quad, a);
   }

   public void addTop() {
      Vector3f topNormal = new Vector3f(0.0f, 1.0f, 0.0f);
      TriangleFanArray fan = 
	new TriangleFanArray((int)div+2, TriangleFanArray.COORDINATES |
			      GeometryArray.NORMALS, 
			     new int[]{ (int)div+2 });
      float topY = length/2;
      Point3f p = new Point3f(0.0f, topY, 0.0f);
      fan.setCoordinate(0, p);
      fan.setNormal(0, topNormal);
      float incr = 2.f*(float)Math.PI/div;
      float prev_radius = radius2;
      float radius = radius1;
      for(int i=0; i<div+1; i++) {
	 if ( i % 2 != 0 ) {
	    float temp = radius;
	    radius = prev_radius;
	    prev_radius = temp;
	 }
	 p = new Point3f(radius*(float)Math.cos((i)*incr), topY, 
			 -radius*(float)Math.sin((i)*incr));
	 fan.setCoordinate(i+1, p);
	 fan.setNormal(i+1, topNormal);
      }
      fan.setCapability(Geometry.ALLOW_INTERSECT);
      top = new Shape3D(fan,app);
   }

   public void addBottom() {
      Vector3f bottomNormal = new Vector3f(0.0f, -1.0f, 0.0f);
      TriangleFanArray fan = 
	 new TriangleFanArray((int)div+2, GeometryArray.COORDINATES |
			      GeometryArray.NORMALS, 
			      new int[]{ (int)div+2 });
      float bottomY = -length/2;
      Point3f p = new Point3f(0.0f, bottomY, 0.0f);
      fan.setCoordinate(0, p);
      fan.setNormal(0, bottomNormal);
      float incr = 2.f*(float)Math.PI/div;
      float prev_radius = radius1;
      float radius = radius2;
      for(int i=0; i<div+1; i++) {
	 if ( i % 2 == 0 ) {
	    float temp = radius;
	    radius = prev_radius;
	    prev_radius = temp;
	 }
	 p = new Point3f(radius*(float)Math.cos((i)*incr), bottomY, 
			 radius*(float)Math.sin((i)*incr));
	 fan.setCoordinate(i+1, p);
	 fan.setNormal(i+1, bottomNormal);
      }
      fan.setCapability(Geometry.ALLOW_INTERSECT);
      bottom = new Shape3D(fan,app);
   }

   Shape3D getShape(){
      return shape;
   }
   Shape3D getBottom(){
      if (bottom == null) addBottom();
      return bottom;
   }

   Shape3D getTop(){
      if (top == null) addTop();
      return top;
   }
}
