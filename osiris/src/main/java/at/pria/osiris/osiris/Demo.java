package at.pria.osiris.osiris;

import android.view.MotionEvent;
import android.view.View;
import at.pria.osiris.osiris.api.Axis;
import at.pria.osiris.osiris.api.Robotarm;
import at.pria.osiris.osiris.network.RemoteRobotarm;

/**
 * @author Ari Ayvazyan
 * @version 27.10.2014
 */
public class Demo{
    private int power = 100;
    private static Demo demo;
    private Robotarm robotarm;

    public Demo(Robotarm robotarm){
        this.robotarm = robotarm;
    }
    public static Demo getInstance(){
        if(demo==null) {
            RemoteRobotarm rm = new RemoteRobotarm();
            demo = new Demo(rm);
        }
        return demo;
    }
    public void showSomething() {
        try {
            robotarm.turnAxis(Axis.AXISTWO, power);
            Thread.sleep(1000);
            robotarm.stopAxis(Axis.AXISTWO);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
