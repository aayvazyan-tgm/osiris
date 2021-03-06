package at.pria.osiris.linker.kinematics;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author Adrian Bergler
 * @version 2015-03-23
 */
public class GeometricKinematicsTest {

    private static Logger logger = Logger.getLogger(GeometricKinematicsTest.class);

    private double[] fragmentlengths = {26, 23};
    private double[][] padding = {{0,0,0},{0,0,0},{0,0,0}};

    @Before
    public void before() throws Exception {

    }

    /**
     * P1 is (
     */
    @Test
    public void testP1(){
        Kinematics kinematics =
                new GeometricKinematics(32, 0, 24, fragmentlengths, padding);

        ArrayList<Double> solution = kinematics.calculateValues();

        System.out.println(solution.toString());

    }

}
