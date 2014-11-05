package client;

import client.network.RemoteRobotarm;
import client.userinterface.ExitController;
import client.userinterface.UserInterface;
import client.userinterface.control.ClientMessageProcessor;
import client.userinterface.control.KeyInput;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Starts the remote control program
 * Run this on a computer within the network of the LinkJVM-Controller
 * @author Adrian Bergler
 * @version 0.1
 */
public class ClientStarter {
	
	public static void main(String[] args){
		
        RemoteRobotarm ra = new RemoteRobotarm();
        ObjectInputStream ois = ra.getOis();


        //Key input
		KeyInput ki = new KeyInput(ra);
		
		//Close Controller
		ExitController ec= new ExitController(ra);
		
		UserInterface ui = new UserInterface();
		
		ui.addWindowListener(ec);
		ui.addKeyListener(ki);
		
		ui.setVisible(true);

		ui.setLocationRelativeTo(null);
		ui.setSize(300, 250);
		ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ClientMessageProcessor cmp = new ClientMessageProcessor(ra,ui);

        boolean running = true;

        while(running){

            //convert ObjectInputStream object to String
            String message = "failmessage";
            try {
                try {
                    message = (String) ois.readObject();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("Message Received: " + message);

            cmp.callMethod(message);
        }

        try {
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
	
}
