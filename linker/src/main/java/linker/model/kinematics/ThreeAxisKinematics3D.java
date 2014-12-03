package linker.model.kinematics;

import linker.model.Joint;

import java.util.ArrayList;
import java.util.List;

/**
 * Kinematics for a 3-Axis robotarm that is within a 3D-coordinate system
 *
 * @author Adrian Bergler
 * @version 2014-11-20
 */
public class ThreeAxisKinematics3D implements Kinematics {

    @Override
    public List<Double> moveTo(double x, double y, double z,
                               double[] fragmentLengths, double[][] padding) {
        double a = fragmentLengths[0];
        double b = fragmentLengths[1];

        double r = Math.sqrt(x * x + y * y);

        double phi = Math.toDegrees(Math.asin(y / r));

        double c = Math.sqrt(r * r + z * z);
        double beta = Math.toDegrees(Math.acos((b * b - a * a - c * c)
                / (-2 * a * c)));
        double gamma = Math.toDegrees(Math.acos((c * c - a * a - b * b)
                / (-2 * a * b)));
        double delta = beta + Math.toDegrees(Math.atan(z / c));
        double eta = 90 - delta;

        System.out.println("Angles: BASE(" + phi + ") AXISONE(" + eta
                + "*) AXISTWO(" + gamma + ")");

        ArrayList<Double> solution = new ArrayList<Double>();
        solution.add(phi); // Base
        solution.add(eta); // AxisOne
        solution.add(gamma); // AxisTwo

        return solution;
    }

}
