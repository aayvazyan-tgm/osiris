package at.pria.osiris.linker.kinematics;

import org.apache.log4j.Logger;

import java.util.ArrayList;

/**
 * @author Adrian Bergler
 * @version 2015-03-23
 */
public class ThreeAxisKinematics implements Kinematic {

    private static Logger logger = Logger.getLogger(ThreeAxisKinematics.class);
    private double a, b, beta, gamma, delta, eta, c, r, phi, x, y, z;
    private double[] fragmentLengths;
    private double[][] padding;

    public ThreeAxisKinematics(double x, double y, double z, double[] fragmentLengths, double[][] padding) {
        this.x = x;
        this.y = y;
        this.z = z;
        if (fragmentLengths.length != 0) {
            this.fragmentLengths = fragmentLengths;
            this.a = fragmentLengths[0];
            this.b = fragmentLengths[1];
        } else {
            throw new RuntimeException("ThreeAxisKinematics, das FragmentLengths-Array darf nicht leer sein!");
        }
        if (padding.length != 0) {
            this.padding = padding;
        } else {
            throw new RuntimeException("ThreeAxisKinematics, das Padding-Array darf nicht leer sein!");
        }
        logger.info("Angles: BASE(" + phi + ") AXISONE(" + eta + "*) AXISTWO(" + gamma + ")");
    }

    /**
     * Calculates values for a 3-Axis robotarm that is within a 3D-coordinate system
     *
     * @return solution, ArrayList with Double values(Base, AxisOne, AxisTwo)
     */
    public ArrayList<Double> calculateValues() {

        this.r = Math.sqrt(x * x + y * y);
        this.phi = Math.toDegrees(Math.asin(y / r));
        this.c = Math.sqrt(r * r + z * z);
        this.beta = Math.toDegrees(Math.acos((b * b - a * a - c * c) / (-2 * a * c)));
        this.gamma = Math.toDegrees(Math.acos((c * c - a * a - b * b) / (-2 * a * b)));
        this.delta = beta + Math.toDegrees(Math.atan(z / c));
        this.eta = 90 - delta;

        ArrayList<Double> solution = new ArrayList<>();
        solution.add(phi); // Base
        solution.add(eta); // AxisOne
        solution.add(gamma); // AxisTwo

        return solution;
    }
}
