package at.pria.osiris.osiris.controllers;

import api.Robotarm;
import at.pria.osiris.osiris.communication.DataListener;

import javax.xml.crypto.Data;

/**
 * @author Ari Ayvazyan
 * @version 03.Dec.14
 */
public interface ControllerSetup {
    public void setup(Robotarm robotarm);
    public DataListener getDataListener();
}
