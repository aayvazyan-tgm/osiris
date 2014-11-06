package client.userinterface.control;

import api.Axis;
import api.Robotarm;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Keyinput
 *
 * @author Adrian Bergler
 * @version 2014-10-17
 */
public class KeyInput implements KeyListener {

    private int power = 100;

    private Robotarm robotarm;

    public KeyInput(Robotarm robotarm) {
        this.robotarm = robotarm;
    }

    @Override
    public void keyPressed(KeyEvent kevent) {
        int keyCode = kevent.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_UP:
                robotarm.turnAxis(Axis.AXISTWO, power);
                break;
            case KeyEvent.VK_DOWN:
                robotarm.turnAxis(Axis.AXISTWO, -power);
                break;
            case KeyEvent.VK_LEFT:
                robotarm.turnAxis(Axis.BASE, power);
                break;
            case KeyEvent.VK_RIGHT:
                robotarm.turnAxis(Axis.BASE, -power);
                break;
            case KeyEvent.VK_W:
                robotarm.turnAxis(Axis.AXISONE, power);
                break;
            case KeyEvent.VK_S:
                robotarm.turnAxis(Axis.AXISONE, -power);
                break;
            case KeyEvent.VK_T:
                robotarm.test();
                break;
            case KeyEvent.VK_P:
                robotarm.stopAll();
                break;
            case KeyEvent.VK_ESCAPE:
                robotarm.exit();
        }
    }

    @Override
    public void keyReleased(KeyEvent kevent) {
        int keyCode = kevent.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_UP:
                robotarm.stopAxis(Axis.AXISTWO);
                break;
            case KeyEvent.VK_DOWN:
                robotarm.stopAxis(Axis.AXISTWO);
                break;
            case KeyEvent.VK_LEFT:
                robotarm.stopAxis(Axis.BASE);
                break;
            case KeyEvent.VK_RIGHT:
                robotarm.stopAxis(Axis.BASE);
                break;
            case KeyEvent.VK_W:
                robotarm.stopAxis(Axis.AXISONE);
                break;
            case KeyEvent.VK_S:
                robotarm.stopAxis(Axis.AXISONE);
                break;
        }

    }

    @Override
    public void keyTyped(KeyEvent kevent) {
        // TODO Auto-generated method stub

    }

}
