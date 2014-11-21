/*
 * COPYRIGHT 2014 Christoph Krofitsch
 * Practical Robotics Institute Austria
 */

package org.andrix.low;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import org.andrix.AXCP;
import org.andrix.listeners.StateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class Server<br>
 * Is the main part of the low communication to Ardiuno. Handles the connection
 * and all sending and incoming data.
 * 
 * @author Christoph Krofitsch
 * @version 17.09.2012
 */
public class AXCPServer {
	private static final Logger log = LoggerFactory.getLogger(AXCPServer.class);

	// private static final int AXCP_SOCKET_CONNECT_TIMEOUT = 500;
	// private static final int AXCP_SOCKET_READ_TIMEOUT = 500;
	// private static final int AXCP_COMMAND_COMPLETE_TIMEOUT = 4000;

	private static final int TCP_UDP_PORT = 12321;
	private HardwareController currentDestination;

	private AXCPAccessor accessor;
	private ConnectionState connectionState;

//	private DatagramSocket udpSocket = null;
//	private Socket tcpSocket;

	private FileInputStream communicationInputStream;
	private FileOutputStream communicationOutputStream;

	private boolean stop = false;

	public AXCPServer(AXCPAccessor accessor) {
		this.accessor = accessor;
		connectionState = ConnectionState.DISCONNECTED;
	}

	/**
	 * Starts the server and the thread which is waiting for connections.
	 *
	 * @throws java.io.FileNotFoundException
	 */
	public synchronized void start() throws FileNotFoundException {
//		udpSocket = new DatagramSocket(TCP_UDP_PORT);
//		udpSocket.setBroadcast(true);
//		tcpSocket = new Socket();
//		tcpSocket.setReuseAddress(true);
//		new Thread(new ScanSender()).start();
//		new Thread(new ScanReceiver()).start();
		String communicationFileLocation = "/dev/ttyAMA0"; ///dev/tty/AMA0  FLAGS: O_RDWR | O_NOCTTY
		File communicationFile = new File(communicationFileLocation);
		communicationInputStream = new FileInputStream(communicationFile);
		communicationOutputStream = new FileOutputStream(communicationFile);
	}

	public synchronized void closeCommunicationFile(){
		try {
			this.communicationOutputStream.close();
		} catch (IOException ex) {
			log.info("Could not close communicationOutputStream.", ex);
		}
		try {
			this.communicationInputStream.close();
		} catch (IOException ex) {
			log.info("Could not close communicationInputStream", ex);
		}
	}
	/**
	 * Stops the server and all threads running with it.
	 */
	public synchronized void stop() throws IOException {
//		stop = true;
//		if (udpSocket != null)
//			udpSocket.close();
//		if (tcpSocket != null)
//			tcpSocket.close();
		if (communicationInputStream != null)
			communicationInputStream.close();
		if (communicationOutputStream != null)
			communicationOutputStream.close();
	}

	private InputStream input;
	private OutputStream output;

	// private long lastOpcode = 0;

	/**
	 * @param buffer
	 * @return false if the complete timeout expired, true if succeeded
	 * @throws IOException
	 */
	private boolean fullReceive(byte[] buffer) throws IOException {
		int read = 0, temp = 0;
		while (read < buffer.length) {
			temp = input.read(buffer, read, buffer.length - read);
			if (temp == -1)
				throw new IOException("End of Stream");
			read += temp;
		}
		return true;
	}

	private void fullSend(byte[] buffer) throws IOException {
		output.write(buffer);
		output.flush();
	}

	private void fullSend(byte[] buffer, int length) throws IOException {
		output.write(buffer, 0, length);
		output.flush();
	}

//	private class ScanSender implements Runnable {
//		public void run() {
//
//			DatagramPacket packet = new DatagramPacket(new byte[] { AXCP.ENVIRONMENT_SCAN_SUBSCRIPTION }, 1);
//			packet.setPort(TCP_UDP_PORT);
//			try {
//				packet.setAddress(InetAddress
//						.getByAddress(new byte[] { (byte) 255, (byte) 255, (byte) 255, (byte) 255 }));
//			} catch (Exception e) {
//				log.error("Couldn't set destination address '255.255.255.255'");
//				return;
//			}
//			while (!stop) {
//				try {
//					Thread.sleep(2500);
//					udpSocket.send(packet);
//				} catch (Exception e) {
//					log.warn("Environment Scan failed: " + e.getMessage());
//				}
//			}
//		}
//	}
//
//	private class ScanReceiver implements Runnable {
//		public void run() {
//			DatagramPacket packet = new DatagramPacket(new byte[255], 255);
//			try {
//				while (!stop) {
//					udpSocket.receive(packet);
//					log.info("Receiced UDP Packet L=" + packet.getLength() + " from " + packet.getAddress().toString());
//					if (packet.getLength() <= 1)
//						continue;
//					byte[] payload = new byte[packet.getLength() - 1];
//					System.arraycopy(packet.getData(), 1, payload, 0, payload.length);
//					accessor.onReceive(packet.getData()[0], payload, packet.getAddress());
//				}
//			} catch (Exception e) {
//				log.error("Failed to receive UDP packets.", e);
//			}
//		}
//	}

	class Receiver implements Runnable {

		public void run() {

			try {
				byte[] read = new byte[0];
				byte[] opcodeBuf = new byte[1];
				byte[] plBuf = new byte[1];
				byte[] pBuf;

				while (true) {

					log.debug("Waiting for data...");
					fullReceive(opcodeBuf);
					// lastOpcode = System.currentTimeMillis();
					log.debug("Received Operation: " + opcodeBuf[0]);
					Integer len = AXCP.payloadLengths.get(opcodeBuf[0]);

					if (len == null) {
						log.warn("Unspecified Opcode: " + opcodeBuf[0]);
						// UNSPECIFIED OPCODE ERROR
						byte[] send = new byte[3];
						send[0] = AXCP.ERROR_ACTION;
						send[1] = AXCP.ERRORCODE_UNSPECIFIED_OPCODE;
						send[2] = opcodeBuf[0];
						synchronized (AXCPServer.this) {
							fullSend(send);
						}
						// TODO V
						// try {
						// Thread.sleep(600);
						// } catch (InterruptedException e) {
						// }
						// while (input.available() > 0)
						// input.read();
						continue;
					}

					if (len == -1) {
						if (!fullReceive(plBuf)) {
							log.warn("Command Complete Timeout! Continuing...");
							continue;
						}
						log.debug("Payload length: " + plBuf[0]);
						pBuf = new byte[0xFF & plBuf[0]];
						if (!fullReceive(pBuf)) {
							log.warn("Command Complete Timeout! Continuing...");
							continue;
						}
						read = pBuf;
						if (pBuf.length == 255) {
							ArrayList<byte[]> bufList = new ArrayList<byte[]>();
							bufList.add(pBuf);
							while ((0xFF & plBuf[0]) == 255) {
								if (!fullReceive(plBuf)) {
									log.warn("Command Complete Timeout! Continuing...");
									continue;
								}
								if ((0xFF & plBuf[0]) > 0) {
									pBuf = new byte[0xFF & plBuf[0]];
									if (!fullReceive(pBuf)) {
										log.warn("Command Complete Timeout! Continuing...");
										continue;
									}
									bufList.add(pBuf);
								}
							}
							int count = 0;
							for (byte[] temp : bufList)
								count += temp.length;
							read = new byte[count];
							int i = 0;
							for (byte[] temp : bufList) {
								for (byte b : temp) {
									read[i] = b;
									i++;
								}
							}
						}
					} else if (len > 0) {
						pBuf = new byte[len];
						if (!fullReceive(pBuf)) {
							log.warn("Command Complete Timeout! Continuing...");
							continue;
						}
						read = pBuf;
					}

					// lastOpcode = 0;

					StringBuffer debug = new StringBuffer("Data received end: ");
					for (byte b : read) {
						debug.append(b);
						debug.append(",");
					}
					System.out.println(debug.toString());
					accessor.onReceive(opcodeBuf[0], read, null);
				}
			} catch (IOException ex) {
				log.info("Receiver thread shutdown.", ex);
				closeCommunicationFile();
				connectionChanged(null);
			}
		}
	}

	/**
	 * Sends data to Arduino in case there is a connection established.
	 */
	public synchronized void send(byte opcode, byte[] payload) throws NotConnectedException {
		if (connectionState == ConnectionState.DISCONNECTED)
			throw new NotConnectedException();
		try {
			byte[] plBuf = new byte[] { opcode };
			byte[] buf;
			fullSend(plBuf);
			if (AXCP.payloadLengths.get(opcode) == -1) {
				int pl = payload.length;
				plBuf[0] = (byte) (pl < 255 ? pl : 255);
				fullSend(plBuf);
				fullSend(payload, plBuf[0] & 0xFF);
				pl -= (plBuf[0] & 0xFF);
				while (pl > 0) {
					plBuf[0] = (byte) (pl < 255 ? pl : 255);
					fullSend(plBuf);
					buf = new byte[plBuf[0] & 0xFF];
					for (int i = 0; i < buf.length; i++)
						buf[i] = payload[i + (payload.length - pl)];
					fullSend(buf);
					pl -= (plBuf[0] & 0xFF);
				}
				if ((plBuf[0] & 0xFF) == 255) {
					plBuf[0] = 0;
					fullSend(plBuf);
				}
			} else if (AXCP.payloadLengths.get(opcode) > 0) {
				fullSend(payload);
			}

			StringBuffer debug = new StringBuffer("Data sent " + opcode + ": ");
			for (byte b : payload) {
				debug.append(b);
				debug.append(",");
			}
			log.debug(debug.toString());
		} catch (IOException ex) {
			log.info("Sending failed.", ex);
			throw new NotConnectedException();
		}
	}

	public synchronized boolean connectController(HardwareController controller) {
		currentDestination = controller;
//		if (controller == null) {
//			try {
//				tcpSocket.close();
//			} catch (IOException e) {
//			}
//			connectionChanged(null);
//			return true;
//		} else {
//			try {
//				if (tcpSocket.isConnected())
//					tcpSocket.close();
//				tcpSocket = new Socket();
//				tcpSocket.setReuseAddress(true);
//				tcpSocket.connect(new InetSocketAddress(currentDestination.address, TCP_UDP_PORT));
//			} catch (Exception ex) {
//				log.debug("WiFi connection attempt failed: ", ex);
//				return false;
//			}
			input = communicationInputStream;
			output = communicationOutputStream;
			connectionChanged(currentDestination);
			new Thread(new Receiver()).start();
			return true;
//		}
	}

	private synchronized void connectionChanged(HardwareController hwc) {
		if (hwc == null) {
			connectionState = ConnectionState.DISCONNECTED;
			for (StateListener listener : StateListener._l_state)
				listener.connectionStateChange(connectionState, null);
		} else {
			connectionState = ConnectionState.CONNECTED_NOAUTH;
			for (StateListener listener : StateListener._l_state)
				listener.connectionStateChange(connectionState, hwc);
		}

	}

	public synchronized HardwareController getConnectedHWType() {
		if (connectionState == ConnectionState.DISCONNECTED)
			return null;
		return currentDestination;
	}
}
