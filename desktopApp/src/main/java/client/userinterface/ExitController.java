package client.userinterface;

import client.network.RemoteRobotarm;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Controller for the close event
 *
 * @author Helmuth Brunner
 * @version Oct 27, 2014
 *          Current project: 00SIRISPrototype01
 */
public class ExitController implements WindowListener {

    private RemoteRobotarm robot;

    public ExitController(RemoteRobotarm robot) {
        this.robot = robot;
    }

    @Override
    public void windowOpened(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowClosing(WindowEvent e) {
        try {
            robot.exit();
        }catch (Exception ex){
            System.err.println(ex);
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
        robot.exit();
    }

    @Override
    public void windowIconified(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowActivated(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        // TODO Auto-generated method stub

    }

}
