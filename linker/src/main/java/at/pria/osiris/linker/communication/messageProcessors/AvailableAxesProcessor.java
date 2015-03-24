package at.pria.osiris.linker.communication.messageProcessors;

import at.pria.osiris.linker.controllers.RobotArm;
import at.pria.osiris.linker.controllers.components.Axes.Axis;
import messages.requests.AvailableAxesRequest;
import messages.responses.AvailableAxesResponse;

/**
 * Created by helmuthbrunner on 24/03/15.
 */
public class AvailableAxesProcessor implements MessageProcessor {
    private RobotArm robotArm;

    public AvailableAxesProcessor(RobotArm robotArm) {
        this.robotArm= robotArm;
    }

    @Override
    public void processMessage(Object msg) {
        if(msg instanceof AvailableAxesRequest) {

            Axis[] axises= robotArm.getAvailableAxes();
            String[] axes_name= new String[axises.length];
            for(int i=0;i<axises.length;i++) {
                axes_name[i]= axises[i].getName();
            }

            AvailableAxesResponse availableAxesResponse= new AvailableAxesResponse(axes_name);
            robotArm.getCommunicationInterface().sendMessage(availableAxesResponse);
        }
    }
}
