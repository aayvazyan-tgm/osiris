package at.pria.osiris.linker.controllers.components.Axes;


import at.pria.osiris.linker.controllers.components.systemDependent.Servo;
import org.apache.log4j.Logger;

/**
 * A class which provides methods that changes the behavior of
 * the Servo.
 *
 * @author Wolfgang Mair
 * @version 16.03.2015
 */
public class ServoHelper implements Runnable{

    private boolean interrupt = false;
    private Servo s;
    private int power;
    private int steps;
    private Logger logger = org.apache.log4j.Logger.getLogger(ServoHelper.class);

    public ServoHelper() {

    }

    /**
     * A method which changes the speed of the specified Servo
     * It uses the time it takes the servo to move 1 degree and splits
     * this time into 100 steps.
     * The more power it gets the more steps it actually moves.
     *
     * @param s     The Servo which should move in the end
     * @param power The power it should use
     * @param steps The amount of steps
     */
    private void pwm(Servo s, int power, int steps) {

        //Defining important Variables
        int maxPower = 100;
        int count = 1;
        boolean pos = true;
        boolean moving = false;
        int startPosition = s.getPositionInDegrees();

        //Checking if the power is negativ of positiv
        if (power < 0) {
            power = power * -1;
            pos = false;
        }

        //Calculating the distance between each stop and go
        double divider = (maxPower - power);
        double mod = maxPower / divider;

        //looping through the given steps with different wait and go times
        for(int i = 0; !interrupt ;i++) {
            if (i == (int) (count * mod)) {
                //logger.info("Stopping ...");
                try {
                    if (moving == true) {
                        s.moveToAngle(startPosition);
                        moving = false;
                    }
                    Thread.sleep(s.getTimePerDegreeInMilli());
                    //It stops
                    count++;
                }
                catch (InterruptedException ie) {
                    ie.printStackTrace();
                }

            }
            //Moving either to the maximum angle for the time being or the minimum
            else {
                //It Moves
                if (pos) {
                    //Defining a softwarebased limit for the rotationdegree
                    if (startPosition < s.getMaximumAngle() - 3) {
                        //logger.info("Next Position: "+(startPosition + 1));
                        s.moveToAngle(startPosition + 1);
                        startPosition += 1;
                        moving = true;
                    }
                    else
                        break;
                } else {
                    //Defining a softwarebased limit for the rotationdegree
                    if (startPosition > 1) {
                        //logger.info("Next Position: "+(startPosition - 1));
                        s.moveToAngle(startPosition - 3);
                        startPosition -= 1;
                        moving = true;
                    }
                    else
                        break;
                }
            }
        }
    }

    public void stop(){
        interrupt = true;
    }

    @Override
    public void run() {
        interrupt = false;
        this.pwm(this.s,this.power,this.steps);
    }
}
