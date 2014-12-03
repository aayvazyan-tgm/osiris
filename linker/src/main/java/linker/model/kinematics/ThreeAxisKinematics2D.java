package linker.model.kinematics;

import linker.model.Joint;

import java.util.ArrayList;
import java.util.List;

/**
 * Kinematics for a 3-Axis robotarm that is moving inside a 2D-coordinate system
 *
 * @author Adrian Bergler
 * @version 2014-11-20
 */
public class ThreeAxisKinematics2D implements Kinematics {

    @Override
    public List<Double> moveTo(double x, double y, double z,
                               double[] fragmentLengths, double[][] padding) {

        double a = fragmentLengths[0];
        double b = fragmentLengths[1];

        double c = Math.sqrt(x * x + y * y);
        double beta = Math.toDegrees(Math.acos((b * b - a * a - c * c)
                / (-2 * a * c)));
        double gamma = Math.toDegrees(Math.acos((c * c - a * a - b * b)
                / (-2 * a * b)));
        double alphaone = Math.toDegrees(Math.asin(y / c));
        double delta = beta + alphaone;
        double eta = 90 - delta;

        ArrayList<Double> solution = new ArrayList<Double>();
        solution.add(eta);
        solution.add(gamma);

        System.out.println(eta);
        System.out.println(gamma);

        return solution;
    }

}
