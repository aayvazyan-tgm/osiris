package oldLinker.model.kinematics;

import java.util.List;

/**
 * A kinematic's stategy
 * Contains algorithms for inverse and forward kinematics
 *
 * @author Adrian Bergler
 * @version 2014-11-20
 */
public interface Kinematics {

    /**
     * Moves to a point with the given coordinates
     *
     * @param x               the x-coordinate
     * @param y               the y-coordinate
     * @param z               the z-coordinate
     * @param fragmentLengths the length of all the arm fragments
     * @param padding         the distance in between the real joints and the theoretical ones
     * @return if the point can be reached
     */
    public List<Double> moveTo(double x, double y, double z, double[] fragmentLengths, double[][] padding);

}