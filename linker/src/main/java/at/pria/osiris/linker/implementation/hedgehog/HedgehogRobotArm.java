package at.pria.osiris.linker.implementation.hedgehog;

import at.pria.osiris.linker.controllers.RobotArm;
import at.pria.osiris.linker.controllers.components.Axis;

public class HedgehogRobotArm extends RobotArm {

    @Override
    public Axis[] getAvailableAxes() {
        return new Axis[0];
    }


}
