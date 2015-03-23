package at.pria.osiris.linker.kinematics;

import org.apache.log4j.Logger;

import java.util.ArrayList;

/**
 * @author Adrian Bergler
 * @version 23.Mar.2015.
 */
public class ApproxKinematics implements Kinematic {

    private static Logger logger = Logger.getLogger(ThreeAxisKinematics.class);
    private double a, b, beta, gamma, delta, eta, c, r, phi, x, y, z;
    private double[] fragmentLengths;
    private double[][] padding;

    public ApproxKinematics(double x, double y, double z, double[] fragmentLengths, double[][] padding) {

    }

    /**
     * Calculates values for a 3-Axis robotarm that is within a 3D-coordinate system
     *
     * @return solution, ArrayList with Double values(Base, AxisOne, AxisTwo)
     */
    public ArrayList<Double> calculateValues() {
        return null;
    }

}
