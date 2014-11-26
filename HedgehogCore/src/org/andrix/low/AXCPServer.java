/*
 * COPYRIGHT 2014 Christoph Krofitsch
 * Practical Robotics Institute Austria
 */

package org.andrix.low;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import jssc.SerialPort;
import jssc.SerialPortException;

import org.andrix.AXCP;
import org.andrix.listeners.StateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class Server<br>
 * Is the main part of the low communication to Ardiuno. Handles the connection and all sending and incoming data.
 * 
 * @author Christoph Krofitsch
 * @version 17.09.2012
 */
public class AXCPServer {
    private static final Logger log = LoggerFactory.getLogger(AXCPServer.class);
    
    private HardwareController  currentDestination;
    
    private AXCPAccessor        accessor;
    private ConnectionState     connectionState;
    
    SerialPort                  serialPort;
    
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
        try {
            //Connect to Serial Port
            serialPort = new SerialPort("/dev/ttyAMA0");
            serialPort.openPort();//Open serial port
            serialPort.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);//Set params. Also you can set params by this string: serialPort.setParams(9600, 8, 1, 0);
        } catch(SerialPortException ex) {
            System.out.println(ex);
        }
    }
    
    /**
     * Stops the server and all threads running with it.
     */
    public synchronized void stop() throws IOException {
        if(serialPort != null) {
            try {
                serialPort.closePort();//Close serial port
            } catch(SerialPortException e) {
                throw new IOException(e);
            }
        }
    }
    
    // private long lastOpcode = 0;
    
    /**
     * @param buffer
     * @return false if the complete timeout expired, true if succeeded
     * @throws IOException
     */
    private boolean fullReceive(byte[] buffer) throws IOException {
        try {
            System.arraycopy(serialPort.readBytes(buffer.length), 0, buffer, 0, buffer.length);
            return true;
        } catch(SerialPortException e) {
            throw new IOException(e);
        }
    }
    
    private void fullSend(byte[] buffer) throws IOException {
        try {
            serialPort.writeBytes(buffer);//Write data to port
        } catch(SerialPortException e) {
            throw new IOException(e.getMessage());
        }
    }
    
    private void fullSend(byte[] buffer, int length) throws IOException {
        byte[] toSend = new byte[length];
        System.arraycopy(buffer, 0, toSend, 0, length);
        fullSend(toSend);
    }
    
    class Receiver implements Runnable {
        
        public void run() {
            
            try {
                byte[] read = new byte[0];
                byte[] opcodeBuf = new byte[1];
                byte[] plBuf = new byte[1];
                byte[] pBuf;
                
                while(true) {
                    
                    log.debug("Waiting for data...");
                    fullReceive(opcodeBuf);
                    // lastOpcode = System.currentTimeMillis();
                    log.debug("Received Operation: " + opcodeBuf[0]);
                    Integer len = AXCP.payloadLengths.get(opcodeBuf[0]);
                    
                    if(len == null) {
                        log.warn("Unspecified Opcode: " + opcodeBuf[0]);
                        // UNSPECIFIED OPCODE ERROR
                        byte[] send = new byte[3];
                        send[0] = AXCP.ERROR_ACTION;
                        send[1] = AXCP.ERRORCODE_UNSPECIFIED_OPCODE;
                        send[2] = opcodeBuf[0];
                        synchronized(AXCPServer.this) {
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
                    
                    if(len == -1) {
                        if(!fullReceive(plBuf)) {
                            log.warn("Command Complete Timeout! Continuing...");
                            continue;
                        }
                        log.debug("Payload length: " + plBuf[0]);
                        pBuf = new byte[0xFF & plBuf[0]];
                        if(!fullReceive(pBuf)) {
                            log.warn("Command Complete Timeout! Continuing...");
                            continue;
                        }
                        read = pBuf;
                        if(pBuf.length == 255) {
                            ArrayList<byte[]> bufList = new ArrayList<byte[]>();
                            bufList.add(pBuf);
                            while((0xFF & plBuf[0]) == 255) {
                                if(!fullReceive(plBuf)) {
                                    log.warn("Command Complete Timeout! Continuing...");
                                    continue;
                                }
                                if((0xFF & plBuf[0]) > 0) {
                                    pBuf = new byte[0xFF & plBuf[0]];
                                    if(!fullReceive(pBuf)) {
                                        log.warn("Command Complete Timeout! Continuing...");
                                        continue;
                                    }
                                    bufList.add(pBuf);
                                }
                            }
                            int count = 0;
                            for(byte[] temp:bufList)
                                count += temp.length;
                            read = new byte[count];
                            int i = 0;
                            for(byte[] temp:bufList) {
                                for(byte b:temp) {
                                    read[i] = b;
                                    i++;
                                }
                            }
                        }
                    } else if(len > 0) {
                        pBuf = new byte[len];
                        if(!fullReceive(pBuf)) {
                            log.warn("Command Complete Timeout! Continuing...");
                            continue;
                        }
                        read = pBuf;
                    }
                    
                    // lastOpcode = 0;
                    
                    StringBuffer debug = new StringBuffer("Data received end: ");
                    for(byte b:read) {
                        debug.append(b);
                        debug.append(",");
                    }
                    System.out.println(debug.toString());
                    accessor.onReceive(opcodeBuf[0], read, null);
                }
            } catch(IOException ex) {
                log.info("Receiver thread shutdown.", ex);
                try {
                    stop();
                } catch(IOException ex1) {}
                connectionChanged(null);
            }
        }
    }
    
    /**
     * Sends data to Arduino in case there is a connection established.
     */
    public synchronized void send(byte opcode, byte[] payload) throws NotConnectedException {
        if(connectionState == ConnectionState.DISCONNECTED) throw new NotConnectedException();
        try {
            byte[] plBuf = new byte[] {opcode};
            byte[] buf;
            fullSend(plBuf);
            if(AXCP.payloadLengths.get(opcode) == -1) {
                int pl = payload.length;
                plBuf[0] = (byte) (pl < 255? pl:255);
                fullSend(plBuf);
                fullSend(payload, plBuf[0] & 0xFF);
                pl -= (plBuf[0] & 0xFF);
                while(pl > 0) {
                    plBuf[0] = (byte) (pl < 255? pl:255);
                    fullSend(plBuf);
                    buf = new byte[plBuf[0] & 0xFF];
                    for(int i = 0; i < buf.length; i++)
                        buf[i] = payload[i + (payload.length - pl)];
                    fullSend(buf);
                    pl -= (plBuf[0] & 0xFF);
                }
                if((plBuf[0] & 0xFF) == 255) {
                    plBuf[0] = 0;
                    fullSend(plBuf);
                }
            } else if(AXCP.payloadLengths.get(opcode) > 0) {
                fullSend(payload);
            }
            
            StringBuffer debug = new StringBuffer("Data sent " + opcode + ": ");
            for(byte b:payload) {
                debug.append(b);
                debug.append(",");
            }
            log.debug(debug.toString());
        } catch(IOException ex) {
            log.info("Sending failed.", ex);
            throw new NotConnectedException();
        }
    }
    
    public synchronized boolean connectController(HardwareController controller) {
        currentDestination = controller;
        connectionChanged(currentDestination);
        new Thread(new Receiver()).start();
        return true;
    }
    
    private synchronized void connectionChanged(HardwareController hwc) {
        if(hwc == null) {
            connectionState = ConnectionState.DISCONNECTED;
            for(StateListener listener:StateListener._l_state)
                listener.connectionStateChange(connectionState, null);
        } else {
            connectionState = ConnectionState.CONNECTED_NOAUTH;
            for(StateListener listener:StateListener._l_state)
                listener.connectionStateChange(connectionState, hwc);
        }
        
    }
    
    public synchronized HardwareController getConnectedHWType() {
        if(connectionState == ConnectionState.DISCONNECTED) return null;
        return currentDestination;
    }
}
