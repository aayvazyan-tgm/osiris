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

    /**
     * Default constructor never use this
     */
    public Profile() {
    }

    /**
     * Constructor
     * @param id the unique id for this object
     * @param host the hostname e.q. to 192.168.0.10 or localhost
     * @param port the port
     * @param type the controller type, Hedgehog or Botball
     */
    public Profile(int id, String host, int port, ControllerType type) {
        this.id= id;
        this.host= host;
        this.port= port;
        this.type= type;
    }

    /**
     * A method to which returns a String representing this object
     * @return a String which contains all attributes
     */
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
