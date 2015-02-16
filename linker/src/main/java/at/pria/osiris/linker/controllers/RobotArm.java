package at.pria.osiris.linker.controllers;

import at.pria.osiris.linker.communication.CommunicationInterface;
import at.pria.osiris.linker.controllers.components.Axis;
import at.pria.osiris.linker.kinematics.Kinematic;

public abstract class RobotArm {

    // Joints
    private Axis joint;

    // KinematicStrategies
    private Kinematic kinematics;

    public Axis getJoint(Axis axis) {
        return joint;
    }


    public abstract Axis[] getAvailableAxes();

    public abstract Axis getAxis(int i);

    public abstract CommunicationInterface getCommunicationInterface();

    public void turnAxis(Axis axis) {

    }

    public void stopAxis(Axis axis) {

    }

    public void moveTo() {

    }
}
