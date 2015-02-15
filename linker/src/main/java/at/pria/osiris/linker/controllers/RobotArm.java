package at.pria.osiris.linker.controllers;

import at.pria.osiris.linker.communication.CommunicationInterface;
import at.pria.osiris.linker.controllers.components.Axis;
import at.pria.osiris.linker.controllers.components.Joint;
import at.pria.osiris.linker.kinematics.Kinematic;
import at.pria.osiris.linker.kinematics.Kinematics3D;
import org.andrix.low.NotConnectedException;
import org.andrix.motors.Motor;
import org.andrix.sensors.Analog;

public abstract class RobotArm {

	// Constants
	private final double basetoaxisone = 11;
	private final double axisonetoaxistwo = 18;
	private double[][] padding = {{0.0, 0.0, 0.0}, {0.0, 0.0, 0.0},
			{0.0, 0.0, 0.0}};
	private double[] fragmentlength = {basetoaxisone, axisonetoaxistwo};

	// Joints
	private Joint joint;

	// KinematicStrategies
	private Kinematic kinematics;

//	public RobotArm() {
//		joints = new Joint[3];
//		try {
//			joints[0] = new Joint(new Motor(0), new Analog(0),
//					api.Axis.BASE.getMinimumAngle(), api.Axis.BASE.getMaximumAngle());
//		} catch (NotConnectedException e) {
//			System.err.println("Something is not connected 0");
//			e.printStackTrace();
//		}
//		try{
//			joints[1] = new Joint(new Motor(1), new Analog(1),
//					api.Axis.AXISONE.getMinimumAngle(), api.Axis.AXISONE.getMaximumAngle());
//		} catch (NotConnectedException e) {
//			System.err.println("Something is not connected 1");
//			e.printStackTrace();
//		}
//		try{
//			joints[2] = new Joint(new Motor(2), new Analog(2),
//					api.Axis.AXISTWO.getMinimumAngle(), api.Axis.AXISTWO.getMaximumAngle());
//		} catch (NotConnectedException e) {
//			System.err.println("Something is not connected 2");
//			e.printStackTrace();
//		}
//
//		kinematics = new Kinematics3D();
//	}
	public Joint getJoint(Axis axis) {
		return joint;
	}



	public abstract Axis[] getAvailableAxes();

    public abstract CommunicationInterface getCommunicationInterface();

	public void turnAxis(Axis axis) {

	}

	public void stopAxis(Axis axis) {

	}

	public void moveTo() {

	}

}
