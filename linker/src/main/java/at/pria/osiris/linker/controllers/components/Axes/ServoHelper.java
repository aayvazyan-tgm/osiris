package at.pria.osiris.linker.controllers.components.Axes;


import at.pria.osiris.linker.controllers.components.systemDependent.Servo;

/**
 * A class which provides methods that changes the behavior of
 * the Servo.
 *
 * @author Wolfgang Mair
 * @version 11.03.2015
 */
public class ServoHelper {

    public ServoHelper(){}

    /**
     * A method which changes the speed of the specified Servo
     * It uses the time it takes the servo to move 1 degree and splits
     * this time into 100 steps.
     * The more power it gets the more steps it actually moves.
     *
     * @param s The Servo which should move in the end
     * @param power The power it should use
     * @param steps The amount of steps
     */
    public static void pwm(Servo s, int power, int steps) {

        //Defining important Variables
        int maxPower = 100;
        int count = 1;
        boolean pos = true;
        boolean interrupt = false;
        boolean moving = false;

        //Checking if the power is negativ of positiv
        if (power < 0) {
            power = power * -1;
            pos = false;
        }

        //Calculating the distance between each stop and go
        double divider = (maxPower - power);
        double mod = maxPower / divider;

        //looping through the given steps with different wait and go times
        while (!interrupt) {
            for (int i = 1; i <= steps; i++) {
                if (i == (int) (count * mod)) {
                    try {
                        if(moving == true) {
                            s.moveToAngle(s.getPositionInDegrees());
                            moving = false;
                        }
                        Thread.sleep(s.getTimePerDegreeInMilli());
                        //System.out.println(i + ": Stop ...");
                        count++;
                    }
                    //Not useable catch, because it cant see the possibility of an invalid Argument
                    //catch (InvalidArgumentException iae) {
                    //    iae.printStackTrace();
                    //}
                    catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }

                }
                //Moving either to the maximum angle for the time being or the minimum
                else {
                    //System.out.println(i + ": Dreh dich!");
                    if (pos) {
                        if(s.getPositionInDegrees() < s.getMaximumAngle()) {
                            s.moveToAngle(s.getPositionInDegrees() + 1);
                            moving = true;
                        }
                    }
                    else{
                        if(s.getPositionInDegrees() > 0) {
                            s.moveToAngle(s.getPositionInDegrees() - 1);
                            moving = true;
                        }
                    }
                }
            }
        }
    }
}
