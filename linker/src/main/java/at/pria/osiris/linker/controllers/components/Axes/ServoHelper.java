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
public class ServoHelper {

    private boolean interrupt = false;
    private Servo s;
    private int steps;
//    private Logger logger = org.apache.log4j.Logger.getLogger(ServoHelper.class);

    /**
     * A Constructor which allows the User to define the servo and the power in which said servo should spin.
     *
     * @param s     The Servo that is supposed to spin
     * @param steps A modifier which decides how fast the servo will run
     */
    public ServoHelper(Servo s, int steps) {
        this.s = s;
        this.steps = steps;
    }

    public void moveAtPower(int power) {
        System.out.println("moveAtPower: "+power);
        this.interrupt = true;
        if (power == 0) return;
        final int powerFinal = power;
        new Thread(new Runnable() {
            @Override
            public void run() {
                pwm(powerFinal);
            }
        }).start();
    }

    /**
     * A method which changes the speed of the specified Servo.
     * The more power it gets the more steps it actually moves.
     *
     * @param power The power(speed) it should use
     */
    private synchronized void pwm(int power) {
        System.out.println("starting pwm: "+power);
        this.interrupt = false;
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
        for (int i = 0; !interrupt; i++) {
            if (i == (int) (count * mod)) {
                //System.out.println("Stopping ...");
                try {
                    if (moving) {
                        s.moveToAngle(startPosition);
                        moving = false;
                    }
                    Thread.sleep(s.getTimePerDegreeInMilli());
                    //It stops
                    count++;
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }

            }
            //Moving either to the maximum angle for the time being or the minimum
            else {
                //It Moves
                if (pos) {
                    //Defining a softwarebased limit for the rotationdegree
                    if (startPosition < s.getMaximumAngle() - 3) {
                        //System.out.println("Next Position: "+(startPosition + 1));
                        s.moveToAngle(startPosition + 1);
                        startPosition += 1;
                        moving = true;
                        try {
                            Thread.sleep(s.getTimePerDegreeInMilli() * steps);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else
                        break;
                } else {
                    //Defining a softwarebased limit for the rotationdegree
                    if (startPosition > 3) {
                        System.out.println("Next Position: "+(startPosition - 1));
                        s.moveToAngle(startPosition - 1);
                        startPosition -= 1;
                        moving = true;
                        try {
                            Thread.sleep(s.getTimePerDegreeInMilli() * steps);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else
                        break;
                }
            }
        }
        if (interrupt) System.out.println("interrupted");
    }

    /**
     * A Method which stops the Thread by stopping the PWM method
     */
    public void stop() {
        interrupt = true;
    }

    /**
     * A Method which returns the Servo currently selected in the Thread
     *
     * @return The currently selected Servo
     */
    public Servo getServo() {
        return s;
    }

    /**
     * A Method which changes the currently selected Servo in the Thread
     *
     * @param s The new Servo which should be selected
     */
    public void setServo(Servo s) {
        this.s = s;
    }

    /**
     * A Method which returns the modifier of the speed
     *
     * @return The speed modifier
     */
    public int getSteps() {
        return steps;
    }

    /**
     * A Method which changes the modifier of the speed
     *
     * @param steps The new speed modifier
     */
    public void setSteps(int steps) {
        this.steps = steps;
    }
}
