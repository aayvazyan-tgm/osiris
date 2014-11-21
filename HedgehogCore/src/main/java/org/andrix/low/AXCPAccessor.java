/*
 * COPYRIGHT 2014 Christoph Krofitsch
 * Practical Robotics Institute Austria
 */

package org.andrix.low;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

import org.andrix.AXCP;
import org.andrix.listeners.AXCPListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class ArduinoAccessor<br>
 * This class is part of the communication library between phone and Arduino. It
 * is between the server and the controller. providing an additional abstraction
 * layer which cares of concurrency and scheduling.
 * 
 * @author Christoph Krofitsch
 * @version 17.09.2012
 */
public class AXCPAccessor {

	private static final Logger log = LoggerFactory.getLogger(AXCPServer.class);

	private static final int AXCP_REQUEST_REPLY_TIMEOUT = 10000;

	private static AXCPAccessor instance;

	public static AXCPAccessor getInstance() {
		if (instance == null) {
			try {
				instance = new AXCPAccessor();
			} catch (FileNotFoundException e) {
				throw new RuntimeException("AXCPAccessor not startable: " + e.getMessage());
			}
		}
		return instance;
	}

	private AXCPServer server;
	private byte replyOpcode = -1;
	private byte[] value;

	/**
	 * Creates the {@link AXCPAccessor} instance
	 * 
	 * @param listener
	 *            the listener which listens for Arduino messages
	 * @param server
	 *            the underlying server
	 * @throws SocketException
	 */
	private AXCPAccessor() throws FileNotFoundException {
		server = new AXCPServer(this);
		server.start();
	}

	public void asynchronousCommand(byte opcode, byte[] payload) throws NotConnectedException {
		server.send(opcode, payload);
	}

	public synchronized byte[] synchronousCommand(byte opcode, byte[] payload, byte replyOpcode)
			throws NotConnectedException, RequestTimeoutException {
		synchronized (server) {
			value = null;
			this.replyOpcode = replyOpcode;
			server.send(opcode, payload);
			try {
				server.wait(AXCP_REQUEST_REPLY_TIMEOUT);
			} catch (InterruptedException ex) {
				throw new NotConnectedException("connection lost while waiting");
			}
			if (value == null) {
				log.warn("Timeout for reply " + replyOpcode);
				throw new RequestTimeoutException("Reply " + replyOpcode + " timed out.");
			}
			replyOpcode = -1;
			return value;
		}
	}

	public synchronized boolean connectController(final HardwareController controller) {
		return server.connectController(controller);
	}

	public void onReceive(byte opcode, byte[] payload, InetAddress source) {
		synchronized (server) {
			for (AXCPListener listener : AXCPListener._l_AXCP)
				listener.received(opcode, payload);
			if (opcode == replyOpcode) {
				value = payload;
				server.notify();
			} else {
				AXCP.received(opcode, payload, source);
			}
		}
	}

	public HardwareController getConnectedHWType() {
		return server.getConnectedHWType();
	}

	public void shutdown() throws IOException {
		server.stop();
	}

}
