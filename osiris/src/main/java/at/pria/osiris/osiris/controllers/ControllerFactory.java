package at.pria.osiris.osiris.controllers;

import at.pria.osiris.osiris.controllers.botball.BotballController;
import at.pria.osiris.osiris.controllers.hedgehog.HedgehogController;

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
        }
        throw new RuntimeException("Controller not found: " + controllerType.toString());
    }
}
