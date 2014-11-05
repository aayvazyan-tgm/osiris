package client.network;

import api.Axis;
import api.Robotarm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * An object that allows remote-controlling a roboterarm
 * @author Adrian Bergler
 * @version 2014-10-17
 */
public class RemoteRobotarm implements Robotarm {

	private final String linkip = "192.168.43.241";		//IP of the JVM-Link-Controller
	private final int linkport = 8889;					//Port of the Server-program running on the Controller
	
	private Socket socket;
	private ObjectOutputStream oos;
    private ObjectInputStream ois;
	
	public RemoteRobotarm(){
		try {
			socket = new Socket(this.linkip, this.linkport);
			oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void turnAxis(Axis axis, int power) {
		sendMessage("turnaxis/" + axis.ordinal() + "/" + power);
	}

	@Override
	public void turnAxis(Axis axis, int power, long timemillis) {
		sendMessage("turnaxis/" + axis.ordinal() + "/" + power + "/" + timemillis);
		
	}

	@Override
	public void stopAxis(Axis axis) {
		sendMessage("stopaxis/" + axis.ordinal());
	}

	@Override
	public boolean moveTo(double x, double y, double z) {
		sendMessage("moveto/" + x + "/" + y + "/" + z);
		return true;
	}

	@Override
	public void stopAll() {
		sendMessage("stopall");
	}
	
	/**
	 * Close sockets, kill watchdogs
	 */
	@Override
	public void close() {
		sendMessage("close");
	}
	
	@Override
	public void test(){
		sendMessage("test");
	}
	
	public void exit(){
		sendMessage("exit");
	}
	
	private void sendMessage(String message){
        try {
        	System.out.println("Sending message to Socket Server: " + message);
        	oos.writeObject(message);
        	System.out.println("Message sent");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public ObjectInputStream getOis() {
        return ois;
    }
}
