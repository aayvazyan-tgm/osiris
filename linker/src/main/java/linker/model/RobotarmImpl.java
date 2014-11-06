package linker.model;

import api.Axis;
import api.Robotarm;
import linker.control.RobotarmSensorWatchdog;
import linkjvm.Botball;
import linkjvm.motors.Motor;
import linkjvm.sensors.analog.AnalogSensor;

/**
 * Implementation of a robotarm
 *
 * @author Adrian Bergler, Christian Janeczek
 * @version 2014-10-25
 */
public class RobotarmImpl implements Robotarm {

    //Constants
    private final double basetoaxisone = 11;
    private final double axisonetoaxisto = 17;

    //Joints
    private Joint base;

    private Joint axis1;

    private Joint axis2;


    //Watchdog
    private RobotarmSensorWatchdog rsws1, rsws2;


    //Threads for Watchdogs
    private Thread watchdogAxis1, watchdogAxis2;

    //TODO

    public RobotarmImpl() {

        base = new Joint(new Motor(0), new AnalogSensor(0), Axis.BASE.getMinimumAngle(), Axis.BASE.getMaximumAngle());
        axis1 = new Joint(new Motor(1), new AnalogSensor(1), Axis.AXISONE.getMinimumAngle(), Axis.AXISONE.getMaximumAngle());
        axis2 = new Joint(new Motor(2), new AnalogSensor(2), Axis.AXISTWO.getMinimumAngle(), Axis.AXISTWO.getMaximumAngle());

        //defining the first watchdog. in this case AXISONE with an interval of 200ms
        rsws1 = new RobotarmSensorWatchdog(this, Axis.AXISONE, 50);
        this.watchdogAxis1 = new Thread(rsws1);
        watchdogAxis1.start();

        //defining the first watchdog. in this case AXISTWO with an interval of 200ms
        rsws2 = new RobotarmSensorWatchdog(this, Axis.AXISTWO, 50);
        this.watchdogAxis2 = new Thread(rsws2);
        watchdogAxis2.start();
    }

    public Joint getAxis(Axis axis) {
        switch (axis) {
            case BASE:
                return base;

            case AXISONE:
                return axis1;

            case AXISTWO:
                return axis2;
        }
        return null;
    }

    public Joint setAxis(Axis axis, Joint joint) {
        switch (axis) {
            case BASE:
                return base;

            case AXISONE:
                return axis1;

            case AXISTWO:
                return axis2;
        }
        return null;
    }

    public Thread getWatchdogThread(Axis axis) {
        switch (axis) {
            case BASE:
                return null;    //there is no watchdog for the base

            case AXISONE:
                return watchdogAxis1;

            case AXISTWO:
                return watchdogAxis2;
        }

        return null;
    }

    /*
     * TODO:
     * Allow movements that move back inside the minimum/maximum range
     * by defining positive power-direction and negative power-direction or using the Joint Object
     */
    @Override
    public void turnAxis(Axis axis, int power) {
        switch (axis) {
            case BASE:
                if (power >= -100 && power <= 100) {
                    base.run(power);
                }
                break;

            case AXISONE:
                if (power >= -100 && power <= 100) {
                    axis1.run(power);
                }
                break;

            case AXISTWO:
                if (power >= -100 && power <= 100) {
                    axis2.run(power);
                }
                break;
        }
    }

    @Override
    public void turnAxis(Axis axis, int power, long timemillis) {
        turnAxis(axis, power);
        Botball.msleep(timemillis);
        stopAxis(axis);
    }

    @Override
    public void stopAxis(Axis axis) {
        switch (axis) {
            case BASE:
                System.out.println("Stop Base");
                base.off();
                break;

            case AXISONE:
                System.out.println("Stop Axis One");
                axis1.off();
                break;

            case AXISTWO:
                System.out.println("Stop Axis Two");
                axis2.off();
                break;
        }
    }

    @Override
    public boolean moveTo(double x, double y, double z) {
        double a = basetoaxisone;
        double b = axisonetoaxisto;
        double r = Math.sqrt(x * x + y * y);
        double phi = Math.toDegrees(Math.asin(y / r));
        double c = Math.sqrt(r * r + z * z);
        double beta = Math.toDegrees(Math.acos((b * b - a * a - c * c) / (-2 * a * c)));
        double gamma = Math.toDegrees(Math.acos((c * c - a * a - b * b) / (-2 * a * b)));
        double delta = beta + Math.toDegrees(Math.atan(z / c));
        double eta = 90 - delta;

        System.out.println("Angles: BASE(" + phi + "�) AXISONE(" + eta + "*) AXISTWO(" + gamma + "�)");

//		base.moveToDegree(phi);
//		axis1.moveToDegree(eta);
//		axis2.moveToDegree(gamma);

        return true;
    }

    @Override
    public void close() {
        rsws1.stop();
        rsws2.stop();
    }

    @Override
    public void test() {
        System.out.println("Test successfull");
    }

    @Override
    public void stopAll() {
        base.off();
        axis1.off();
        axis2.off();
    }

    @Override
    public void exit() {
        stopAll();
        close();
    }

}
