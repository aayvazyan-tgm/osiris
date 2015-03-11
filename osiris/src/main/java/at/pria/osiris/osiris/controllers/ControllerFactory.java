package at.pria.osiris.osiris.controllers;

import at.pria.osiris.osiris.controllers.botball.BotballController;
import at.pria.osiris.osiris.controllers.hedgehog.HedgehogController;
import at.pria.osiris.osiris.controllers.hedgehogdirect.HedgehogDirectController;

/**
 * @author Ari Ayvazyan
 * @version 03.Dec.14
 */
public class ControllerFactory {
    public static Controller getController(ControllerType controllerType){
        switch (controllerType){
            case Hedgehog:
                return new HedgehogController();
            case Botball:
                return new BotballController();
            case HedgehogDirect:
                return  new HedgehogDirectController();
        }
        throw new RuntimeException("Controller not found: " + controllerType.toString());
    }
}
