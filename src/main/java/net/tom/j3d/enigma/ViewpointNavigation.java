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

import javax.media.j3d.*;
import javax.vecmath.*;
import java.util.*;

/**
 * 
 * @author Tom Nelson - tomnelson@dev.java.net
 *
 */
public class ViewpointNavigation {
    Transform3D[]  viewpoints;
    Point3f[]      positions = new Point3f[2];
    Quat4f[]       quats = new Quat4f[2];
    float[]        knots = {0.f, 1.f};
    Transform3D    axis = new Transform3D();
    Alpha          alpha;
    TransformGroup target;
    Group          root;
    BranchGroup    motionBranch;
    ViewpointInterpolator path;

    /**
     * Create an instance
     * @param root - a place to attach this behavior
     * @param target - the transform group this behavior
     * operates on
     * @param viewpoints - an array of Transform3D objects
     *                    that hold the location/rotation
     *                    of the viewpoints
     */
    public ViewpointNavigation(BranchGroup root,
			       TransformGroup target, 
			       Transform3D[] viewpoints) {
	this.viewpoints = viewpoints;
	this.target = target;
	this.root = root;
	alpha = new Alpha(1, Alpha.INCREASING_ENABLE,
			  0, 0, 5000, 250, 20, 0, 0, 0);
    }

    /**
     * get the Point3f representing the translation part
     * of the current location of the object
     */
    private Point3f getCurrentPoint() {
	// get the current Transform3D
	Transform3D curr = new Transform3D();
	target.getTransform(curr);
	// get the translational part of the current transform
	Vector3f cv = new Vector3f();
	curr.get(cv);
	return new Point3f(cv);
    }

    /**
     * get the Quat3f representing the rotational part of the
     * current location/position of the object
     */
    private Quat4f getCurrentQuat() {
	// get the current Transform3D
	Transform3D curr = new Transform3D();
	target.getTransform(curr);
	// get the rotational part of the current transform
	Matrix4d cm = new Matrix4d();
	curr.get(cm);
	Quat4f quat = new Quat4f();
	quat.set(cm);
	return quat;
    }

    /**
     * Interpolate the object from wherever it is now
     * to the viewpoint of the provided index.
     * @param - the index into the viewpoints array
     */
    public void interpolateToPosition(int index) {

	// get rid of the old one, if its still around
	if(motionBranch != null) {
	    motionBranch.detach();
	    path = null;
	}

	// get theQuat4f for the viewpoint:
	Matrix4f mat = new Matrix4f();
	Transform3D viewpoint = viewpoints[index];
	viewpoint.get(mat);
	Quat4f newQuat = new Quat4f();
	newQuat.set(mat);

	// get the Point3f for the viewpoint:
	Vector3f vec = new Vector3f();
	viewpoint.get(vec);
	Point3f newPoint = new Point3f(vec);

	positions[0] = getCurrentPoint();
	positions[1] = newPoint;
	quats[0] = getCurrentQuat();
	quats[1] = newQuat;

	motionBranch = new BranchGroup();
	motionBranch.setCapability(BranchGroup.ALLOW_DETACH);

	path = new ViewpointInterpolator(alpha, target, axis, 
					 knots, quats, positions);
	
	path.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
						 1000.0));
	motionBranch.addChild(path);
	root.addChild(motionBranch);
	alpha.setStartTime(System.currentTimeMillis());
    }

    /**
     * A subclass of RotPosPathInterpolator that will
     * cause the interpolator to be detached and garbage
     * collected when the Alpha has completed.
     */
    class ViewpointInterpolator extends RotPosPathInterpolator {
	public ViewpointInterpolator(Alpha alpha, TransformGroup target,
				     Transform3D axis, float[] knots, 
				     Quat4f[] quats, Point3f[] positions) {
	    super(alpha, target, axis, knots, quats, positions);
	}
	
	public void processStimulus(Enumeration criteria) {
	    Alpha thisAlpha = getAlpha();
	    if(thisAlpha.finished()) {
		if(motionBranch != null) {
		    motionBranch.detach();
		    path = null;
		}
		wakeupOn(defaultWakeupCriterion);
	    }
	    else {
		super.processStimulus(criteria);
	    }
	}
    }
}
