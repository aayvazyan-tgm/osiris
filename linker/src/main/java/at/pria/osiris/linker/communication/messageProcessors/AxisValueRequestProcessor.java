package at.pria.osiris.linker.communication.messageProcessors;

import at.pria.osiris.linker.controllers.RobotArm;
import messages.requests.AxisValueRequest;
import messages.responses.AxisValueResponse;
import messages.responses.SensorValueResponse;

/**
 * @author Ari Michael Ayvazyan
 * @version 16.02.2015
 */
public class AxisValueRequestProcessor implements MessageProcessor {
    private RobotArm robotArm;

    public AxisValueRequestProcessor(RobotArm robotArm) {
        this.robotArm = robotArm;
    }

    @Override
    public void processMessage(Object msg) {
        if (msg instanceof AxisValueRequest) {
            AxisValueRequest request = (AxisValueRequest) msg;
            int sensorVal = robotArm.getAxis(request.getAxisNumber()).getSensorValue();
            AxisValueResponse response = new AxisValueResponse(request.getAxisNumber(), sensorVal);
            robotArm.getCommunicationInterface().sendMessage(response);
        }
    }
}
