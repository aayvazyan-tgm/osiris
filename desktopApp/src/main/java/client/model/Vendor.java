package client.model;

import api.Robotarm;
import client.network.RemoteRobotarm;

public class Vendor {

	private RemoteRobotarm robotarm;
	
	private static Vendor vendor = new Vendor();
	
	private Vendor(){
		robotarm = new RemoteRobotarm();
	}
	
	public static Vendor get(){
		return vendor;
	}

	public RemoteRobotarm getRobotarm() {
		return robotarm;
	}

	public void setRobotarm(RemoteRobotarm robotarm) {
		this.robotarm = robotarm;
	}
	
}
