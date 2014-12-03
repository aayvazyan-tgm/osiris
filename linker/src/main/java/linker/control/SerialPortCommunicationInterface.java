package linker.control;

import java.io.IOException;
import jssc.SerialPort;
import jssc.SerialPortException;
import org.andrix.low.CommunicationInterface;

public class SerialPortCommunicationInterface implements CommunicationInterface {

	private SerialPort serialPort;

	public void connect() throws IOException {
		try {
			serialPort = new SerialPort("/dev/ttyAMA0");
			serialPort.openPort();
			serialPort.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
		} catch (SerialPortException ex) {
			throw new IOException(ex);
		}
		return;
	}

	public void disconnect() throws IOException{
		if (serialPort != null) {
			try {
				serialPort.closePort();// Close serial port
			} catch (SerialPortException ex) {
				throw new IOException(ex);
			}
		}
		return;
	}
	
	public void fullReceive(byte[] buffer) throws IOException {
		try {
			System.arraycopy(serialPort.readBytes(buffer.length), 0, buffer, 0, buffer.length);
		} catch (SerialPortException ex) {
			throw new IOException(ex);
		}
	}
	
	public void fullSend(byte[] buffer) throws IOException {
		try {
			serialPort.writeBytes(buffer);// Write data to port
		} catch (SerialPortException ex) {
			throw new IOException(ex);
		}
	}

	public void fullSend(byte[] buffer, int length) throws IOException {
		byte[] toSend = new byte[length];
		System.arraycopy(buffer, 0, toSend, 0, length);
		fullSend(toSend);
	}
}
