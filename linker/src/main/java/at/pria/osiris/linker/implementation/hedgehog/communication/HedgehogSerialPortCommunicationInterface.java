package at.pria.osiris.linker.implementation.hedgehog.communication;

import jssc.SerialPort;
import jssc.SerialPortException;
import org.andrix.low.CommunicationInterface;

import java.io.IOException;

/*
 * This class needs to be set as the CommunicationHandler in the Hedgehog core project.-
 * This happens via:
 * AXCPServer.communicationInterface=new SerialPortCommunicationInterface();
 * (HedgehogCore 3.12.2014)
 *
 */
public class HedgehogSerialPortCommunicationInterface implements CommunicationInterface {

    private SerialPort serialPort;

    public void connect() throws IOException {
        try {
            //The serial port of the connected Hedgehog/Andrix Controller
            serialPort = new SerialPort("/dev/ttyAMA0");
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
        } catch (SerialPortException ex) {
            throw new IOException(ex);
        }
    }

    public void disconnect() throws IOException {
        if (serialPort != null) {
            try {
                serialPort.closePort();// Close serial port
            } catch (SerialPortException ex) {
                throw new IOException(ex);
            }
        }
    }

    /**
     * Wartet bis der buffer voll ist
     *
     * @param buffer
     * @throws java.io.IOException
     */
    public void fullReceive(byte[] buffer) throws IOException {
        try {
            System.arraycopy(serialPort.readBytes(buffer.length), 0, buffer, 0, buffer.length);
        } catch (SerialPortException ex) {
            throw new IOException(ex);
        }
    }

    /**
     * sendet den gesamten buffer
     *
     * @param buffer
     * @throws java.io.IOException
     */
    public void fullSend(byte[] buffer) throws IOException {
        try {
            serialPort.writeBytes(buffer);// Write data to port
        } catch (SerialPortException ex) {
            throw new IOException(ex);
        }
    }

    /**
     * @param buffer
     * @param length
     * @throws java.io.IOException
     */
    public void fullSend(byte[] buffer, int length) throws IOException {
        byte[] toSend = new byte[length];
        System.arraycopy(buffer, 0, toSend, 0, length);
        fullSend(toSend);
    }
}
