package at.pria.osiris.linker.controllers.components.Axes;


import at.pria.osiris.linker.controllers.components.systemDependent.Servo;

/**
 * A class which provides methods that changes the behavior of
 * the Servo
 *
 * @author Wolfgang Mair
 * @version 02.03.2015
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
    public static void pwm(Servo s, int power, int steps){

        //Defining important Variables
        int maxPower = 100;
        int count = 1;
        boolean pos = true;

        //Checking if the power is negativ of positiv
        if(power < 0) {
            power = power * -1;
            pos = false;
        }

        //Calculating the distance between each stop and go
        double divider = (maxPower - power);
        double mod = maxPower / divider;

        //looping through the given steps with different wait and go times
        for (int i = 1; i <= steps; i++) {
            if(i == (int)(count*mod)) {
                try {
                    s.moveToExactPosition(s.getPosition());
                    Thread.sleep(Math.round(s.getTimePerDegreeInMilli()*50));
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
                if(pos)
                    s.moveToExactPosition(s.getPosition()+1);
                else
                    s.moveToExactPosition(s.getPosition()-1);
            }
        }
    }
}
