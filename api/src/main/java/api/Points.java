package api;

import java.io.Serializable;

/**
 * A point-object which stores x,y cordinats.
 *
 * Created by helmuthbrunner on 16/01/15.
 */
public class Points implements Serializable {

    private int x, y;

    public Points(int x, int y) {
        this.x= x;
        this.y= y;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public String toString() {
        return "["+this.x+ "," + this.y + "]";
    }
}
