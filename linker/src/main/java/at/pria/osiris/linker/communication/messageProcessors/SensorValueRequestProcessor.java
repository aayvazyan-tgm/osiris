package at.pria.osiris.linker.communication.messageProcessors;

import at.pria.osiris.linker.controllers.RobotArm;
import messages.requests.SensorValueRequest;
import messages.responses.SensorValueResponse;

/**
 * @author Ari Michael Ayvazyan
 * @version 16.02.2015
 */
public class SensorValueRequestProcessor implements MessageProcessor {
    private RobotArm robotArm;

    public SensorValueRequestProcessor(RobotArm robotArm) {
        this.robotArm = robotArm;
    }

    @Override
    public void processMessage(Object msg) {
        if (msg instanceof SensorValueRequest) {
            SensorValueRequest request = (SensorValueRequest) msg;
            int sensorVal=robotArm.getAxis(request.getSensorPort())
                    .getSensorValue();
            System.out.println(sensorVal);
            SensorValueResponse response = new SensorValueResponse(request.getSensorPort(),sensorVal);
            robotArm.getCommunicationInterface().sendMessage(response);
        }
    }
}
