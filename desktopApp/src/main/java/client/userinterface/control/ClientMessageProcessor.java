package client.userinterface.control;

import api.Robotarm;
import client.userinterface.UserInterface;

/**
 * Created by Samuel on 29.10.2014.
 */
public class ClientMessageProcessor {

    private Robotarm robotarm;
    private UserInterface userInterface;


    public ClientMessageProcessor(Robotarm robotarm, UserInterface userInterface){
        this.robotarm = robotarm;
        this.userInterface = userInterface;
    }

    /**
     * Changes Sensor Values in the GUI
     * @param message the message
     */
    public void callMethod(String message){
        String[] splitted = message.split("/");
        for(int i = 0; i < splitted.length; i++){
            System.out.println("Splitted" + i + ": " + splitted[i]);
        }
        if(splitted[0].equals("sensor0") && splitted.length == 2){
            try {
                userInterface.getV().getSensor0().setText(splitted[1]);
            } catch (NumberFormatException nfe) {
                // TODO Auto-generated catch block
                nfe.printStackTrace();
            }
        }else if(splitted[0].equals("sensor1") && splitted.length == 2){
            try {
                userInterface.getV().getSensor1().setText(splitted[1]);
            } catch (NumberFormatException nfe) {
                // TODO Auto-generated catch block
                nfe.printStackTrace();
            }
        }else if(splitted[0].equals("sensor2") && splitted.length == 2){
            try {
                System.out.println("sensor " + splitted[0] + " " + splitted[1]);
                userInterface.getV().getSensor2().setText(splitted[1]);
            } catch (NumberFormatException nfe) {
                // TODO Auto-generated catch block
                nfe.printStackTrace();
            }
        }else if(splitted[0].equals("sensor3") && splitted.length == 2){
            try {
                System.out.println("sensor " + splitted[0] + " " + splitted[1]);
            } catch (NumberFormatException nfe) {
                // TODO Auto-generated catch block
                nfe.printStackTrace();
            }
        }else if(splitted[0].equals("sensor4") && splitted.length == 2){
            try {
                System.out.println("sensor " + splitted[0] + " " + splitted[1]);
            } catch (NumberFormatException nfe) {
                // TODO Auto-generated catch block
                nfe.printStackTrace();
            }
        }else if(splitted[0].equals("sensor5") && splitted.length == 2){
            try {
                System.out.println("sensor " + splitted[0] + " " + splitted[1]);
            } catch (NumberFormatException nfe) {
                // TODO Auto-generated catch block
                nfe.printStackTrace();
            }
        }else if(splitted[0].equals("stopall") && splitted.length == 1){
            System.out.println("stopall");
            robotarm.stopAll();

        }else if(splitted[0].equals("close") && splitted.length == 1){
            System.out.println("close");
            robotarm.close();

        }else if(splitted[0].equals("test") && splitted.length == 1){
            System.out.println("test");
            robotarm.test();
        }
    }
}

