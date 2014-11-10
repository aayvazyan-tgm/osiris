package client.userinterface;

import javax.swing.*;

import client.model.Vendor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import api.Robotarm;

/**
 * A controller for the Button
 *
 * @author Helmuth Brunner
 * @version Oct 25, 2014
 *          Current project: 00SIRISPrototype01
 */
public class ButtonController implements ActionListener {

    private JTextField x, y, z;
    private JFrame f;
    private Robotarm robotarm;

    public ButtonController(JFrame f) {
        this.f = f;
        robotarm = Vendor.get().getRobotarm();
    }

    public ButtonController(JTextField x, JTextField y, JTextField z, JFrame f) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.f = f;
        robotarm = Vendor.get().getRobotarm();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JButton but = (JButton) e.getSource();
        if (but.getName().equals("MoveTo")) {

            System.out.println("x: " + x.getText() + "\ny: " + y.getText() + "\nz: " + z.getText());

            robotarm.moveTo(Integer.parseInt(x.getText()),Integer.parseInt(y.getText()),Integer.parseInt(z.getText()));
            
            f.requestFocus();
        }

        //To fix the key-input interrupts
        if (but.getName().equals("Refresh")) {

            f.requestFocus();

        }

    }
}
