package at.pria.osiris.linker.controllers;

import at.pria.osiris.linker.communication.CommunicationInterface;
import at.pria.osiris.linker.communication.messageProcessors.MessageProcessor;
import at.pria.osiris.linker.controllers.components.Axes.Axis;
import at.pria.osiris.linker.kinematics.Kinematics;

import java.util.ArrayList;

/**
 * @author Ari Ayvazyan
 * @version 2015-01-26
 */
public abstract class RobotArm {

    private CommunicationInterface communicationInterface;
    // Joints
    private Axis joint;
    // KinematicStrategies
    private Kinematics kinematics;

    /**
     * Sets up the communication interface with the given messageProcessor
     *
     * @param communicationInterface
     * @param messageProcessor
     */
    public RobotArm(CommunicationInterface communicationInterface, MessageProcessor messageProcessor) {
        this.communicationInterface = communicationInterface;
        this.communicationInterface.setupCommunication(messageProcessor);
    }

    public abstract Axis[] getAvailableAxes();

    public abstract Axis getAxis(int i);

    public CommunicationInterface getCommunicationInterface() {
        return this.communicationInterface;
    }

    public void moveTo(double x, double y, double z) {

    }

    public abstract void moveSynchronized(ArrayList<Double> angle);

    public abstract int getSensorValue(int sensorPort);
}
