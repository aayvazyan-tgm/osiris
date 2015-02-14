package messages;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * SerializableMessage Tester.
 *
 * @author Ari Ayvazyan
 * @version 1.0
 * @since <pre>Feb 13, 2015</pre>
 */
public class SerializableMessageTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getMessageID()
     */
    @Test
    public void testGetMessageID() throws Exception {
        SerializableMessageImplementation smi = new SerializableMessageImplementation();
        SerializableMessageImplementation smi2 = new SerializableMessageImplementation();
        Assert.assertFalse(smi.getMessageID()==smi2.getMessageID());
    }
    /**
     * Method: getMessageID()
     */
    @Test
    public void testThreadedGetMessageID() throws Exception {
        final SerializableMessageImplementation[] smi = new SerializableMessageImplementation[1];
        final SerializableMessageImplementation[] smi2 = new SerializableMessageImplementation[1];

        Thread t1=new Thread(new Runnable() {
            @Override
            public void run() {
                smi[0] = new SerializableMessageImplementation();
            }
        });

        Thread t2=new Thread(new Runnable() {
            @Override
            public void run() {
                smi2[0] = new SerializableMessageImplementation();
            }
        });
        t1.run();
        t2.run();
        Assert.assertFalse(smi[0].getMessageID()==smi2[0].getMessageID());
    }

    public static class SerializableMessageImplementation extends SerializableMessage {

    }
} 
