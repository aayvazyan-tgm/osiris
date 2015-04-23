package at.pria.osiris;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

/**
 * @author Samuel Schmidt
 * @version 4/21/2015
 */
public class Robotarm {

    public Model model;
    public ModelInstance baseplate, turntable, arm1, arm2;
    boolean leftMove, rightMove, upMove, downMove, nMove, mMove;

    public Robotarm(ModelInstance baseplate, ModelInstance turntable, ModelInstance arm1, ModelInstance arm2, Model model) {
        this.baseplate = baseplate;
        this.turntable = turntable;
        this.arm1 = arm1;
        this.arm2 = arm2;
        this.model = model;
    }

    public void updateMotion() {
        // base
        if (leftMove) {
            turntable.transform.rotate(Vector3.Z, -5);
            arm1.transform.set(turntable.transform).mul(model.getNode("arm1Attachment").globalTransform);
            arm2.transform.set(turntable.transform).mul(model.getNode("arm2Attachment").globalTransform);

        }
        if (rightMove) {
            turntable.transform.rotate(Vector3.Z, 5);
            arm1.transform.set(turntable.transform).mul(model.getNode("arm1Attachment").globalTransform);
            arm2.transform.set(turntable.transform).mul(model.getNode("arm2Attachment").globalTransform);
        }
        // 1st axis
        if (nMove) {
            arm1.transform.rotate(Vector3.Z, 5);
            arm2.transform.set(arm1.transform).mul(model.getNode("arm22Attachment").globalTransform);
//            model.getNode("arm2Attachment").globalTransform.translate(10f,0f,0f);
//            model.getNode("arm1Attachment").globalTransform.rotate(Vector3.Z,5);
//            model.getNode("arm2Attachment").globalTransform.rotate(Vector3.Z,5);
//            model.getNode("arm22Attachment").globalTransform.rotate(Vector3.Z,5);


        }
        if (mMove) {
            arm1.transform.rotate(Vector3.Z, -5);
            arm2.transform.set(arm1.transform).mul(model.getNode("arm22Attachment").globalTransform);
        }
        // 2nd axis
        if (upMove) {
            arm2.transform.rotate(Vector3.Z, 5);
            model.getNode("arm2Attachment").globalTransform.rotate(Vector3.Z, 5);
//            model.getNode("arm22Attachment").globalTransform.rotate(Vector3.Z, 5);
        }
        if (downMove) {
            arm2.transform.rotate(Vector3.Z, -5);
            model.getNode("arm2Attachment").globalTransform.rotate(Vector3.Z, -5);
//            model.getNode("arm2Attachment").globalTransform.rotate(Vector3.Z, -5);
        }
        System.out.println("-------------------------------------");
        System.out.println("baseplate:\n" + baseplate.transform);
        System.out.println("turntable:\n" + turntable.transform);
        System.out.println("arm1:\n" + arm1.transform);
        System.out.println("arm2:\n" + arm2.transform);
        System.out.println("arm1atchm:\n" + model.getNode("arm1Attachment").globalTransform);
        System.out.println("arm2atchm:\n" + model.getNode("arm2Attachment").globalTransform);
        System.out.println("arm22atchm:\n" + model.getNode("arm22Attachment").globalTransform);
        System.out.println("-------------------------------------");
    }

    //    darkness lays beyond this comment
    public void setLeftMove(boolean t) {
        if (rightMove && t) rightMove = false;
        rightMove = false;
        upMove = false;
        downMove = false;
        nMove = false;
        mMove = false;

        leftMove = t;
        updateMotion();
    }

    public void setRightMove(boolean t) {
        if (leftMove && t) leftMove = false;
        leftMove = false;
        upMove = false;
        downMove = false;
        nMove = false;
        mMove = false;

        rightMove = t;
        updateMotion();
    }

    public void setUpMove(boolean t) {
        rightMove = false;
        leftMove = false;
        downMove = false;
        nMove = false;
        mMove = false;

        upMove = t;
        updateMotion();
    }

    public void setDownMove(boolean t) {
        rightMove = false;
        leftMove = false;
        upMove = false;
        nMove = false;
        mMove = false;

        downMove = t;
        updateMotion();
    }

    public void setNMove(boolean t) {
        rightMove = false;
        leftMove = false;
        downMove = false;
        upMove = false;
        mMove = false;

        nMove = t;
        updateMotion();
    }

    public void setMMove(boolean t) {
        rightMove = false;
        leftMove = false;
        downMove = false;
        upMove = false;
        nMove = false;

        mMove = t;
        updateMotion();
    }
}
