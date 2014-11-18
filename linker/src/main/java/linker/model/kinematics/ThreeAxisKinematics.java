package linker.model.kinematics;

import linker.model.Joint;

public class ThreeAxisKinematics implements Kinematics {

	@Override
	public boolean moveTo(double x, double y, double z, Joint[] joints, double[] fragmentlengths, double[][] padding) {

		Joint base = joints[0];
		Joint axis1 = joints[1];
		Joint axis2 = joints[2];
		
		// double a = fragmentlengths[0];
		// double b = fragmentlengths[1];
		//
		// double r = Math.sqrt(x * x + y * y);
		// double r = x;
		// double phi = Math.toDegrees(Math.asin(y / r));
		//
		// double c = Math.sqrt(r * r + z * z);
		// double beta = Math.toDegrees(Math.acos((b * b - a * a - c * c) / (-2
		// * a * c)));
		// double gamma = Math.toDegrees(Math.acos((c * c - a * a - b * b) / (-2
		// * a * b)));
		// double delta = beta + Math.toDegrees(Math.atan(z / c));
		// double eta = 90 - delta;
		//
		// System.out.println("Angles: BASE(" + phi + ") AXISONE(" + eta +
		// "*) AXISTWO(" + gamma + ")");
		//
		// // base.moveToDegree(phi);
		// axis1.moveToAngle(92-eta, 100);
		// axis2.moveToAngle(8+gamma, 65);

		double a = fragmentlengths[0];
		double b = fragmentlengths[1];

		double c = Math.sqrt(x * x + y * y);
		double beta = Math.toDegrees(Math.acos((b * b - a * a - c * c)
				/ (-2 * a * c)));
		double gamma = Math.toDegrees(Math.acos((c * c - a * a - b * b)
				/ (-2 * a * b)));
		double alphaone = Math.toDegrees(Math.asin(y / c));
		double delta = beta + alphaone;
		double eta = 90 - delta;

		axis1.moveToAngle(92 - eta, 100);
		axis2.moveToAngle(8 + gamma, 65);

		return true;
	}

}
