package at.pria.osiris.osiris.controllers.bedgehog;

import at.pria.osiris.osiris.controllers.ControllerSetup;

/**
 * @author Ari Ayvazyan
 * @version 03.Dec.14
 */
public class HedgehogSetup implements ControllerSetup {

    @Override
    public void setup() {
        //Add a Connection Listener that connects to the controller
        MyStateListener._l_state.add(new MyStateListener());
    }
}
