package client.userinterface;

import javax.swing.*;
import java.awt.*;

/**
 * The Panel
 * @author Helmuth Brunner
 * @version Oct 25, 2014
 * Current project: 00SIRISPrototype01
 */
public class View extends JPanel {

	//Attributes
	private JSlider slider;
	private JTextField xfield;
    private JTextField yfield;
    private JTextField zfield;
    private JTextField base;
    private JTextField axis1;
    private JTextField axis2;
	private JButton b, refresh;
	private JPanel buttonpanel;
	private JLabel x;
    private JLabel y;
    private JLabel z;
    private JLabel value;
    private JLabel sensor0;
    private JLabel sensor1;
    private JLabel sensor2;
    private JLabel s0;
    private JLabel s1;
    private JLabel s2;

	private JFrame f;

	/**
	 * Default-Constructor
	 */
	public View(JFrame f) {
		super();
		this.f= f;
		this.init();
	}

	/**
	 * Creates all graphic objects
	 */
	public void init() {

		this.buttonpanel= new JPanel();
		this.buttonpanel.setLayout(new GridLayout(8,1));

		this.slider= new JSlider(JSlider.VERTICAL, -100, 100, 0);
		// Slider( min-value, max-value, startpoint of the slider )

		this.slider.setFocusable(false);

		this.xfield = new JTextField();
		this.yfield = new JTextField();
		this.zfield = new JTextField();
        this.sensor0 = new JLabel();
        this.sensor1 = new JLabel();
        this.sensor2 = new JLabel();

		this.x= new JLabel("x-Value");
		this.y= new JLabel("y-Value");
		this.z= new JLabel("z-Values");
        this.s0 = new JLabel("Base");
        this.s1 = new JLabel("Axis 1");
        this.s2 = new JLabel("Axis 2");
		this.value= new JLabel("Slider-Value: " + "0");

		this.slider.addChangeListener(new SliderController(this.value));

		buttonpanel.add(this.x);
		buttonpanel.add(this.xfield);

		buttonpanel.add(this.y);
		buttonpanel.add(this.yfield);

		buttonpanel.add(this.z);
		buttonpanel.add(this.zfield);

        buttonpanel.add(this.s0);
        buttonpanel.add(this.getSensor0());

        buttonpanel.add(this.s1);
        buttonpanel.add(this.getSensor1());

        buttonpanel.add(this.s2);
        buttonpanel.add(this.getSensor2());

		this.b= new JButton("MoveTo");
		this.b.setName("MoveTo");
		this.b.addActionListener(new ButtonController(xfield, zfield, yfield, f));

		this.refresh= new JButton("EnableKeyInput");
		this.refresh.setName("Refresh");
		this.refresh.addActionListener(new ButtonController(f));

		buttonpanel.add(this.b);
		buttonpanel.add(this.value);

		buttonpanel.add(refresh);

		super.setLayout(new BorderLayout());

		super.add(slider, BorderLayout.WEST);
		super.add(buttonpanel);

	}

    public JLabel getSensor0() {
        return sensor0;
    }

    public JLabel getSensor1() {
        return sensor1;
    }

    public JLabel getSensor2() {
        return sensor2;
    }
}
