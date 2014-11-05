package client.userinterface;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A Controller for the Slider
 * @author Helmuth Brunner
 * @version Oct 24, 2014
 * Current project: 00SIRISPrototype01
 */
public class SliderController implements ChangeListener {

	private JLabel value;
	
	public SliderController(JLabel value) {
		this.value= value;
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		
		JSlider js= (JSlider) e.getSource();
		
		value.setText( "Slider-Value: "+ String.valueOf(js.getValue()) );
		
	}

}
