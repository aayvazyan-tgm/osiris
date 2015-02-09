package at.pria.osiris.osiris.controllers.botball;

import at.pria.osiris.osiris.controllers.ControllerSetup;

import java.io.IOException;

/**
 * Created by helmuthbrunner on 09/02/15.
 */
public class BotballSetup implements ControllerSetup {

    private String host;
    private int port;

    public BotballSetup(String host, int port) {
        this.host= host;
        this.port= port;
    }

    @Override
    public void setup() {
        try {
            BotballRemoteRobotarm.setup(this.host, this.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
