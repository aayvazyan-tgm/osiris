package at.pria.osiris;

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
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class OsirisSimulation implements ApplicationListener {
    public PerspectiveCamera cam;
    public CameraInputController camController;
    public Model model;
    public ModelBatch modelBatch;
    public AssetManager assets;
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public Environment environment;
    public boolean loading;

    public ModelInstance baseplate, turntable, arm1, arm2;
    public InputMultiplexer inputMultiplexer;
    public Robotarm robotarm;

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
        inputMultiplexer.addProcessor(1, new MyInputProcessor(robotarm));

        Gdx.input.setInputProcessor(inputMultiplexer);

        assets = new AssetManager();
        assets.load("data/osiris-arm.g3db", Model.class);

        loading = true;
    }

    private void doneLoading() {
        model = assets.get("data/osiris-arm.g3db", Model.class);
//        Node rootNode = null;
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
//            rootNode.addChild(node);
        }
        loading = false;
        Node arm1Atchm = new Node();
        Node arm2Atchm = new Node();
        Node arm22Atchm = new Node();
        Matrix4 temp = new Matrix4();

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

        robotarm = new Robotarm(baseplate, turntable, arm1, arm2, model);
        inputMultiplexer.addProcessor(1, new MyInputProcessor(robotarm));
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
//        if (turntable != null)
//            modelBatch.render(turntable);
        modelBatch.end();

//        if(Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
//            System.out.println("A presed");
//            arm2.transform.setToRotation(Vector3.Y, 20);
//        }
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
}