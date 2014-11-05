package at.pria.osiris.osiris;

import android.view.MotionEvent;
import android.view.View;
import api.Axis;
import api.Robotarm;
import at.pria.osiris.osiris.network.RemoteRobotarm;

import java.io.IOException;

/**
 * @author Ari Ayvazyan
 * @version 27.10.2014
 */
public class Demo {
    private int power = 100;
    private static Demo demo;
    private Robotarm robotarm;

    public Demo(Robotarm robotarm) {
        this.robotarm = robotarm;
    }

    public static Demo getInstance() throws IOException {
        if (demo == null) {
            RemoteRobotarm rm = RemoteRobotarm.getInstance();
            demo = new Demo(rm);
        }
        return demo;
    }

    public void showSomething() {
        try {
            //Axis 2
            //forth
            robotarm.turnAxis(Axis.AXISTWO, power);
            Thread.sleep(500);
            robotarm.stopAxis(Axis.AXISTWO);
            //back
            robotarm.turnAxis(Axis.AXISTWO, -power);
            Thread.sleep(500);
            robotarm.stopAxis(Axis.AXISTWO);
            //Axis 1
            //forth
            robotarm.turnAxis(Axis.AXISONE, power);
            Thread.sleep(500);
            robotarm.stopAxis(Axis.AXISONE);
            //back
            robotarm.turnAxis(Axis.AXISONE, -power);
            Thread.sleep(500);
            robotarm.stopAxis(Axis.AXISONE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
