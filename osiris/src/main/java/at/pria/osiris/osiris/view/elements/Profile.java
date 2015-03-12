package at.pria.osiris.osiris.view.elements;

/**
 * Created by helmuthbrunner on 10/02/15.
 */
public class Profile {

    private int id;
    private String host;
    private int port;

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
     */
    public Profile(int id, String host, int port) {
        this.id= id;
        this.host= host;
        this.port= port;
    }

    /**
     * A method to which returns a String representing this object
     * @return a String which contains all attributes
     */
    @Override
    public String toString() {
        return  "Id: " + this.id +
                "\nHost: " + this.host +
                "\nPort: " + this.port;
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
}
