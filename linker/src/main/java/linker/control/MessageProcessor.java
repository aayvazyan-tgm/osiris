package linker.control;

import api.Axis;
import api.Robotarm;

/**
 * Sends a message via socket
 * @author Adrian Bergler
 * @version 2014-10-17
 */
public class MessageProcessor {
	
	private Robotarm robotarm;

	
	public MessageProcessor(Robotarm robotarm){
		this.robotarm = robotarm;
	}
	
	/**
	 * Combined with a message-"receiver" this calls a method of the robotarm
	 * @param message the message
	 */
	public void callMethod(String message){
		String[] splitted = message.split("/");
		for(int i = 0; i < splitted.length; i++){
			System.out.println("Splitted" + i + ": " + splitted[i]);
		}
		if(splitted[0].equals("turnaxis") && splitted.length == 3){
			try {
				System.out.println("turnaxis1");
				robotarm.turnAxis(Axis.values()[Integer.parseInt(splitted[1])],
						Integer.parseInt(splitted[2]));
				
			} catch (NumberFormatException nfe) {
				// TODO Auto-generated catch block
				nfe.printStackTrace();
			}
		}else if(splitted[0].equals("turnaxis") && splitted.length == 4){
			try {
				System.out.println("turnaxis2");
				robotarm.turnAxis(Axis.values()[Integer.parseInt(splitted[1])],
						Integer.parseInt(splitted[2]),
						Integer.parseInt(splitted[3]));
				
			} catch (NumberFormatException nfe) {
				// TODO Auto-generated catch block
				nfe.printStackTrace();
			}
		}else if(splitted[0].equals("stopaxis") && splitted.length == 2){
			try {
				System.out.println("stopaxis");
				robotarm.stopAxis(Axis.values()[Integer.parseInt(splitted[1])]);
				
			} catch (NumberFormatException nfe) {
				// TODO Auto-generated catch block
				nfe.printStackTrace();
			}
		}else if(splitted[0].equals("moveto") && splitted.length == 4){
			try {
				System.out.println("moveto");
				robotarm.moveTo(Integer.parseInt(splitted[1]),
						Integer.parseInt(splitted[2]),
						Integer.parseInt(splitted[3]));
				
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
