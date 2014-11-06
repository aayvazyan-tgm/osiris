package linker;

import linker.control.MessageProcessor;
import linker.model.RobotarmImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Starts the link-controller program
 * Run this on the LinkJVM-Controller
 *
 * @author Adrian Bergler
 * @version 0.1
 */
public class Starter {
    public static void main(String[] args) {
        try {
            Thread t;
            ServerSocket serverSocket = new ServerSocket(8889);    //Used to speak with the controller
            System.out.println("Waiting for client");
            Socket client = serverSocket.accept();

            System.out.println("Waiting for remote input");

            RobotarmImpl robotarm = new RobotarmImpl();

            MessageProcessor mp = new MessageProcessor(robotarm);

            ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());

            boolean running = true;

            // sends sensordata in ~1s intervall
            SimpleThread st = new SimpleThread(oos, robotarm);
            t = new Thread(st);
            t.start();

            while (running) {

                //convert ObjectInputStream object to String
                String message = "failmessage";
                try {
                    message = (String) ois.readObject();
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println("Message Received: " + message);

                mp.callMethod(message);

                //terminate the server if client sends exit request
                if (message.equalsIgnoreCase("exit")) {
                    robotarm.exit();
                    System.out.println("Shutting down.");
                    running = false;
                }
            }

            //close resources
            ois.close();
            oos.close();
            client.close();
            serverSocket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.exit(0);
    }
}
