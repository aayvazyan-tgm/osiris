package linker.model;

import linkjvm.Botball;
import linkjvm.motors.Motor;
import linkjvm.sensors.analog.AnalogSensor;

/**
 * @author Christian Janeczek
 * @version 2014-11-04
 */
public class Joint {

    private Motor motor;
    private AnalogSensor sensor;
    private int min;
    private int max;

    private boolean running = false;


    public Joint(Motor motor, AnalogSensor sensor, int min, int max) {
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

        if (sensor.getValue() < max && power > 0) {
            motor.run(power);
            System.out.println("Starting Motor with power " + power);
        } else {
            if (sensor.getValue() > min && power < 0) {
                motor.run(power);
                System.out.println("Starting Motor with power " + power);
            } else {
                System.out.println("You are trying to move outside the threshold!");
            }
        }

    }

    public void off() {
        motor.off();
        running = false;
        System.out.println("Stopping Motor");
    }

    public boolean moveToPosition(int pos, int power) {
        if (pos > max || pos < min) return false;

        int posmax = (int) ((double) pos + ((double) pos * 0.05));
        int posmin = (int) ((double) pos - ((double) pos * 0.05));

        if ((power > 0 && pos < sensor.getValue()) || (power < 0 && pos > sensor.getValue())) {
            power = power * (-1);
        }

        motor.run(power);
        System.out.println("Moving to position: " + pos);
        while (posmax < sensor.getValue() || posmin > sensor.getValue()) {
            Botball.msleep(50);
        }
        motor.off();

        return true;
    }

    /**
     * moveToAngle invokes the moveToPosition method and uses the converted angle as the new position parameter
     *
     * @param angle The specific angle
     * @param power range -100 , 100
     * @return
     */
    public boolean moveToAngle(double angle, int power) {
        return moveToPosition(transATS(angle), power);
    }

    /**
     * transATS - transform AngleToSensor
     * A method, which converts the given angle value into a value, which can be compared to the sensor values
     *
     * @param aValue The specific angle, which should be converted
     */
    public int transATS(double aValue) {
        //1 degree equals to 5.1 in the sensor value
        double sValue = aValue * 5.1;
        return (int) sValue;
    }

    //Setter-Getter

    public Motor getMotor() {
        return motor;
    }

    public void setMotor(Motor motor) {
        this.motor = motor;
    }

    public AnalogSensor getSensor() {
        return sensor;
    }

    public void setSensor(AnalogSensor sensor) {
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