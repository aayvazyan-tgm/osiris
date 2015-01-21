package at.pria.osiris.linker.controllers.components;

import org.andrix.low.NotConnectedException;
import org.andrix.low.RequestTimeoutException;
import org.andrix.motors.Motor;
import org.andrix.sensors.Analog;

/**
 * @author Christian Janeczek
 * @version 2015-01-21
 */
public class Joint {

    private Motor motor;
    private Analog sensor;
    private int min;
    private int max;

    private boolean running = false;


    public Joint(Motor motor, Analog sensor, int min, int max) {
        this.motor = motor;
        this.sensor = sensor;
        this.min = min;
        this.max = max;
    }

    /**
     * A method which turns the motor with a certain power
     *
     * @param power The Power in percent
     */
    public synchronized void run(int power) {
        if (running) return;

        running = true;
        try {
            if (sensor.getValue() < max && power > 0) {
                motor.moveAtPower(power);
                System.out.println("Starting Motor with power " + power);
            } else {
                if (sensor.getValue() > min && power < 0) {
                    motor.moveAtPower(power);
                    System.out.println("Starting Motor with power " + power);
                } else {
                    if (sensor.getValue() > min && power < 0) {
                        motor.moveAtPower(power);
                        System.out.println("Starting Motor with power " + power);
                    } else {
                        System.out.println("You are trying to move outside the threshold!");
                    }
                }
            }
        } catch (RequestTimeoutException e) {
            e.printStackTrace();
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }
    }

    public void off() {
        try {
            motor.off();
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }
        running = false;
        System.out.println("Stopping Motor");
    }

    public synchronized boolean moveToPosition(int pos, int power) {
        if (pos > max || pos < min) return false;

        int posmax = (int) ((double) pos + ((double) pos * 0.02));
        int posmin = (int) ((double) pos - ((double) pos * 0.02));
        try {
            if ((power > 0 && pos < sensor.getValue()) || (power < 0 && pos > sensor.getValue())) {
                power = power * (-1);
            }

            motor.moveAtPower(power);
            System.out.println("Moving to position: " + pos);
            while (posmax < sensor.getValue() || posmin > sensor.getValue()) {
                Thread.sleep(50);
            }
            motor.off();

            Thread.sleep(100);

            if (posmax < sensor.getValue() || posmin > sensor.getValue()) return moveToPosition(pos, power);
        } catch (RequestTimeoutException e) {
            e.printStackTrace();
        } catch (NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * moveToAngle invokes the moveToPosition method and uses the converted angle as the new position parameter
     *
     * @param angle The specific angle
     * @param power range -100 , 100
     * @return
     */
    public synchronized boolean moveToAngle(double angle, int power) {
        return moveToPosition(transATS(angle), power);
    }

    /**
     * transATS - transform AngleToSensor
     * A method, which converts the given angle value into a value, which can be compared to the sensor values
     *
     * @param aValue The specific angle, which should be converted
     */
    public int transATS(double aValue) {
        //1 degree equals to 4.8 in the sensor value
        double sValue = aValue * 4.8;
        return (int) sValue;
    }

    //Setter-Getter

    public Motor getMotor() {
        return motor;
    }

    public void setMotor(Motor motor) {
        this.motor = motor;
    }

    public Analog getSensor() {
        return sensor;
    }

    public void setSensor(Analog sensor) {
        this.sensor = sensor;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

}
