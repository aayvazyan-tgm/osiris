package at.pria.osiris.osiris.view.elements;

import at.pria.osiris.osiris.controllers.ControllerType;

/**
 * Created by helmuthbrunner on 10/02/15.
 */
public class Profile {

    private int id;
    private String host;
    private int port;
    private ControllerType type;

    public Profile() {
    }

    public Profile(int id, String host, int port, ControllerType type) {
        this.id= id;
        this.host= host;
        this.port= port;
        this.type= type;
    }


    @Override
    public String toString() {
        return  "Id: " + this.id +
                "\nHost: " + this.host +
                "\nPort: " + this.port +
                "\nType: " + this.type.toString();
    }

    /*
     * Getter and setter methods
     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ControllerType getType() {
        return type;
    }

    public void setType(ControllerType type) {
        this.type = type;
    }
}
