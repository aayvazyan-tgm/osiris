package linker.model;

import api.Axis;
import api.Robotarm;
import linker.control.RobotarmSensorWatchdog;
import linker.model.kinematics.Kinematics;
import linker.model.kinematics.ThreeAxisKinematics;
import linkjvm.Botball;
import linkjvm.motors.Motor;
import linkjvm.sensors.analog.AnalogSensor;

/**
 * Implementation of a robotarm
 *
 * @author Adrian Bergler
 * @version 2014-11-17
 */
public class RobotarmImpl implements Robotarm {

    //Constants
    private final double basetoaxisone = 11;
    private final double axisonetoaxistwo = 17;	
	private double[][] padding = {{0.0, 0.0, 0.0},{0.0, 0.0, 0.0},{0.0, 0.0, 0.0}};
	private double[] fragmentlength = {basetoaxisone, axisonetoaxistwo};
    
	//Joints
    private Joint[] joints;
    
    //Watchdog
    private RobotarmSensorWatchdog rsws1, rsws2;

    //Threads for Watchdogs
    private Thread watchdogAxis1, watchdogAxis2;

    //KinematicStategies
    private Kinematics kinematics;

    public RobotarmImpl() {

    	joints = new Joint[3];
    	
        joints[0] = new Joint(new Motor(0), new AnalogSensor(0), Axis.BASE.getMinimumAngle(), Axis.BASE.getMaximumAngle());
        joints[1] = new Joint(new Motor(1), new AnalogSensor(1), Axis.AXISONE.getMinimumAngle(), Axis.AXISONE.getMaximumAngle());
        joints[2] = new Joint(new Motor(2), new AnalogSensor(2), Axis.AXISTWO.getMinimumAngle(), Axis.AXISTWO.getMaximumAngle());

        kinematics = new ThreeAxisKinematics();
        
        //defining the first watchdog. in this case AXISONE with an interval of 50ms
        rsws1 = new RobotarmSensorWatchdog(this, Axis.AXISONE, 50);
        this.watchdogAxis1 = new Thread(rsws1);
        watchdogAxis1.start();

        //defining the first watchdog. in this case AXISTWO with an interval of 50ms
        rsws2 = new RobotarmSensorWatchdog(this, Axis.AXISTWO, 50);
        this.watchdogAxis2 = new Thread(rsws2);
        watchdogAxis2.start();
    }

    public Joint getAxis(Axis axis) {
        return joints[axis.ordinal()];
    }

    public void setAxis(Axis axis, Joint joint) {
        this.joints[axis.ordinal()] = joint;
    }

    public Joint[] getJoints(){
    	return joints;
    }
    
    public void setJoints(Joint[] joints){
    	this.joints = joints;
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

    @Override
    public void turnAxis(Axis axis, int power) {
        if (power >= -100 && power <= 100) {
            joints[axis.ordinal()].run(power);
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
        System.out.println("Stopping " + axis.name());
        joints[axis.ordinal()].off();
    }

    @Override
    public boolean moveTo(double x, double y, double z) {
    	return kinematics.moveTo(x, y, z, joints, fragmentlength, padding);
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
        for(int i = 0; i < joints.length; i++){
        	joints[i].off();
        }
    }

    @Override
    public void exit() {
        stopAll();
        close();
    }

}
