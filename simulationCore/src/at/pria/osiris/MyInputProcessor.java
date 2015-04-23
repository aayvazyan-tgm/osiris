package at.pria.osiris;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 * @author Samuel Schmidt
 * @version 4/21/2015
 */
public class MyInputProcessor implements InputProcessor {

    Robotarm robotarm;

    public MyInputProcessor(Robotarm robotarm) {
        this.robotarm = robotarm;
    }


    /**
     * tap dem keys
     *
     * @param keycode
     * @return
     */
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
                robotarm.setLeftMove(true);
                break;
            case Input.Keys.RIGHT:
                robotarm.setRightMove(true);
                break;
            case Input.Keys.UP:
                robotarm.setUpMove(true);
                break;
            case Input.Keys.DOWN:
                robotarm.setDownMove(true);
                break;
            case Input.Keys.M:
                robotarm.setMMove(true);
                break;
            case Input.Keys.N:
                robotarm.setNMove(true);
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}