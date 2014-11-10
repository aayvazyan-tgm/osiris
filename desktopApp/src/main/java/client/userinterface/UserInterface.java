package client.userinterface;


import javax.swing.*;
import api.Robotarm;

/**
 * A GUI
 * Currently only used to provide key-input-options
 *
 * @author Adrian Bergler
 * @version 2014-10-17
 */
public class UserInterface extends JFrame {

    private View v;

    public UserInterface() {
        super();

        v = new View(this);

        super.add(getV());
    }

    public View getV() {
        return v;
    }
}
