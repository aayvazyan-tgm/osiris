package at.pria.osiris.osiris;

import api.Axis;
import api.Robotarm;
import at.pria.osiris.osiris.network.RemoteRobotarm;

import java.io.IOException;

/**
 * @author Ari Ayvazyan
 * @version 27.10.2014
 */
public class Demo {
    private static Demo demo;
    private int power = 100;
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
            //Forth
            robotarm.turnAxis(Axis.AXISONE, power);
            robotarm.turnAxis(Axis.AXISTWO, 10);
            robotarm.turnAxis(Axis.BASE, 70);
            Thread.sleep(500);
            //Stop
            robotarm.stopAxis(Axis.AXISTWO);
            robotarm.stopAxis(Axis.AXISONE);
            robotarm.stopAxis(Axis.BASE);
            Thread.sleep(500);
            //Back
            robotarm.turnAxis(Axis.AXISONE, -power);
            robotarm.turnAxis(Axis.AXISTWO, -10);
            robotarm.turnAxis(Axis.BASE, -70);
            Thread.sleep(500);
            //Stop
            robotarm.stopAxis(Axis.AXISTWO);
            robotarm.stopAxis(Axis.AXISONE);
            robotarm.stopAxis(Axis.BASE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
