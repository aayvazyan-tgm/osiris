package at.pria.osiris.osiris.controllers.Hedgehog;

import at.pria.osiris.osiris.controllers.ControllerSetup;

/**
 * @author Ari Ayvazyan
 * @version 03.Dec.14
 */
public class HedgehogSetup implements ControllerSetup {

    @Override
    public void setup() {
        MyStateListener._l_state.add(new MyStateListener());
    }
}
