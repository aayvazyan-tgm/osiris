package at.pria.osiris.osiris.controllers;

import at.pria.osiris.osiris.controllers.Hedgehog.HedgehogController;
import at.pria.osiris.osiris.controllers.Hedgehog.HedgehogRemoteRobotarm;
import at.pria.osiris.osiris.controllers.Hedgehog.HedgehogSetup;

/**
 * @author Ari Ayvazyan
 * @version 03.Dec.14
 */
public class ControllerFactory {
    public static Controller getController(ControllerType controllerType){
        switch (controllerType){
            case Hedgehog:
                return new HedgehogController();
        }
        throw new RuntimeException("Controller not found: " + controllerType.toString());
    }
}
