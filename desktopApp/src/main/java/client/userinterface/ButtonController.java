package client.userinterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A controller for the Button
 * @author Helmuth Brunner
 * @version Oct 25, 2014
 * Current project: 00SIRISPrototype01
 */
public class ButtonController implements ActionListener {

	private JTextField x,y,z;
	private JFrame f;
	
	public ButtonController(JFrame f) {
		this.f= f;
	}
	
	public ButtonController(JTextField x, JTextField y, JTextField z, JFrame f) {
		this.x= x;
		this.y= y;
		this.z= z;
		this.f= f;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
	
		JButton but= (JButton) e.getSource();
 		if(but.getName().equals("MoveTo")) {
			
			System.out.println("x: "+x.getText() + "\ny: " + y.getText() + "\nz: " + z.getText());
			
			f.requestFocus();
		}
 		
 		//To fix the key-input interrupts
 		if(but.getName().equals("Refresh")) {
 			
 			f.requestFocus();
 			
 		}
		
	}
}
