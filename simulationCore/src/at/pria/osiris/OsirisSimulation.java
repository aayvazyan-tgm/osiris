package at.pria.osiris;

import at.pria.osiris.linker.controllers.components.Axes.ServoHelper;
import at.pria.osiris.linker.controllers.components.systemDependent.Servo;
import at.pria.osiris.osiris.controllers.RobotArm;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author Samuel Schmidt
 * @version 27.04.2015
 */
public class OsirisSimulation implements ApplicationListener, RobotArm {
    public PerspectiveCamera cam;
    public CameraInputController camController;
    public Model model;
    public ModelBatch modelBatch;
    public AssetManager assets;
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public Environment environment;
    public boolean loading;

    private int axis1Angle = 0;
    private int axis2Angle = 0;
    private double axis0Angle = 0;

    public ModelInstance baseplate, turntable, arm1, arm2;
    public InputMultiplexer inputMultiplexer;
//    public Robotarm robotarm;

    //the Servo helpers for fluent movement
    private static HashMap<Integer, ServoHelper> ServoHelperINSTANCES = new HashMap<>();

    private ServoHelper getServoHelperInstance(int axis, OsirisSimulation osirisSimulation) {
        if (!ServoHelperINSTANCES.containsKey(axis))
            ServoHelperINSTANCES.put(axis, new ServoHelper(new SimulatedServo(axis, osirisSimulation), 1));
        return ServoHelperINSTANCES.get(axis);
    }

    @Override
    public void create() {
        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(20f, -25f, -55f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        inputMultiplexer = new InputMultiplexer();
        camController = new CameraInputController(cam);

        inputMultiplexer.addProcessor(0, camController);
//        inputMultiplexer.addProcessor(1, new MyInputProcessor(robotarm));

        Gdx.input.setInputProcessor(inputMultiplexer);

        assets = new AssetManager();
        assets.load("data/osiris-arm.g3db", Model.class);

        loading = true;
    }

    private void doneLoading() {
        model = assets.get("data/osiris-arm.g3db", Model.class);
        for (int i = 0; i < model.nodes.size; i++) {
            String id = model.nodes.get(i).id;
            ModelInstance instance = new ModelInstance(model, id);
            Node node = instance.getNode(id);

            instance.transform.set(node.globalTransform);
            node.translation.set(0, 0, 0);
            node.scale.set(1, 1, 1);
            node.rotation.idt();
            instance.calculateTransforms();

            if (id.equals("grundplatte")) {
                baseplate = instance;
            } else if (id.equals("drehteller")) {
                turntable = instance;
            } else if (id.equals("arm1")) {
                arm1 = instance;
            } else if (id.equals("arm2")) {
                arm2 = instance;
            }
            instances.add(instance);
        }
        loading = false;
        Node arm1Atchm = new Node();
        Node arm2Atchm = new Node();
        Node arm22Atchm = new Node();

        arm1Atchm.globalTransform.translate(-5f, 0f, 5f);
        arm1Atchm.globalTransform.rotate(Vector3.Y, 90f);
        arm1Atchm.globalTransform.rotate(Vector3.X, 90f);
        arm1Atchm.globalTransform.translate(0f, -0.5f, -1.3f);
        arm1Atchm.id = "arm1Attachment";
        model.nodes.add(arm1Atchm);

        arm2Atchm.globalTransform.translate(-5f, 0f, 30f);
        arm2Atchm.globalTransform.rotate(Vector3.Y, 90f);
        arm2Atchm.globalTransform.rotate(Vector3.X, -90f);
        arm2Atchm.globalTransform.translate(-0.8f, 0.5f, -3.4f);
        arm2Atchm.id = "arm2Attachment";
        model.nodes.add(arm2Atchm);

        arm22Atchm.globalTransform.set(arm22Atchm.globalTransform);
        arm22Atchm.globalTransform.translate(-26f, 0f, 5f);
        arm22Atchm.globalTransform.rotate(Vector3.X, 180f);
//        arm22Atchm.globalTransform.rotate(Vector3.Y, -90f);
        arm22Atchm.id = "arm22Attachment";
        model.nodes.add(arm22Atchm);

//        robotarm = new Robotarm(baseplate, turntable, arm1, arm2, model);
//        inputMultiplexer.addProcessor(1, new MyInputProcessor(robotarm));
    }

    @Override
    public void render() {
        if (loading && assets.update())
            doneLoading();
        camController.update();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(236 / 255f, 240 / 255f, 241 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(cam);
        modelBatch.render(instances, environment);
        modelBatch.end();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        instances.clear();
        assets.dispose();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void turnAxis(final int axis, int power) {
        // 0.1 has to be adjusted probably
        int simulationAdjustedPower = (int)(power * 0.1);

        ServoHelper servoHelper = getServoHelperInstance(axis, this);
        servoHelper.moveAtPower(power);

        switch(axis){
            case 1:
                turntable.transform.rotate(Vector3.Z, simulationAdjustedPower);
                arm1.transform.set(turntable.transform).mul(model.getNode("arm1Attachment").globalTransform);
                arm2.transform.set(turntable.transform).mul(model.getNode("arm2Attachment").globalTransform);
                break;
            case 2:
                arm1.transform.rotate(Vector3.Z, simulationAdjustedPower);
                arm2.transform.set(arm1.transform).mul(model.getNode("arm22Attachment").globalTransform);
                break;
            case 3:
                arm2.transform.rotate(Vector3.Z, simulationAdjustedPower);
                model.getNode("arm2Attachment").globalTransform.rotate(Vector3.Z, simulationAdjustedPower);
                break;
        }
    }

    @Override
    public void stopAxis(int axis) {
        ServoHelper servoHelper = getServoHelperInstance(axis, this);
        servoHelper.moveAtPower(0);
    }

    // TODO: (re)euse inverse kinematics
    @Override
    public void moveToAngle(int axis, int angle) {
        switch (axis) {
            case 0:
                axis0Angle = angle;
                break;
            case 1:
                axis1Angle = angle;
                break;
            case 2:
                axis2Angle = angle;
                break;
        }
    }

    @Override
    public double getMaximumAngle(int axis) {
        switch (axis) {
            case 0:
                return 360;
            case 1:
                return 90;
            case 2:
                return 180;
        }
        return 0;
    }

    @Override
    public boolean moveTo(double x, double y, double z) {
        return false;
    }

    @Override
    public void sendMessage(Serializable msg) {
    }

    @Override
    public double getPosition(int axis) {
        switch (axis) {
            case 0:
                return axis0Angle;
            case 1:
                return axis1Angle;
            case 2:
                return axis2Angle;
        }
        return -1;
    }

    @Override
    public String getConnectionState() {
        return "Connected... its a simulation";
    }

    private class SimulatedServo implements Servo {
        int axis;
        private OsirisSimulation osirisSimulation;

        public SimulatedServo(int axis, OsirisSimulation osirisSimulation) {
            this.axis = axis;
            this.osirisSimulation = osirisSimulation;
        }

        @Override
        public void moveToAngle(int position) {
            osirisSimulation.moveToAngle(axis, position);
        }

        @Override
        public int getPositionInDegrees() {
            return (int) osirisSimulation.getPosition(axis);
        }

        @Override
        public int getMaximumAngle() {
            return (int) osirisSimulation.getMaximumAngle(axis);
        }

        @Override
        public long getTimePerDegreeInMilli() {
            return 2;
        }
    }
}