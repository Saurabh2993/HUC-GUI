/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test_package;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

/**
 *
 * @author saurabh
 */
public class HUC_UDP_Connection {

    static private DatagramSocket gcsSocket;
    static private DatagramSocket NAPSocket;
    static byte status = 0x00;
    
    public HUC_UDP_Connection() throws SocketException, UnknownHostException {
        System.out.println("Listening at port 3000");
        gcsSocket = new DatagramSocket(3000);
        //NAPSocket = new DatagramSocket(3000, InetAddress.getByName("192.168.0.1"));
    }

    public static byte[] receiveFromRpi(int timeout) throws InterruptedException {

        byte[] input = new byte[10];
        try {
            DatagramPacket receivePacket = new DatagramPacket(input, input.length);
            gcsSocket.receive(receivePacket);
            input = receivePacket.getData();
            gcsSocket.setSoTimeout(timeout);

        } catch (SocketTimeoutException ste) {
            System.out.println("Connection lost");
            HUC_GroundStation_GUI_V2.connectionLost();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }

    public void sendToRpi(byte[] sendData) {
        try {
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("localhost"), 2000);
            gcsSocket.send(sendPacket);
            System.out.println("Ack sent");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void sendToSpaceX(byte[] sendData) {
        try {
            byte team_id = 0x0E;
            int acceleration;
            int stripe;
            int position;
            int velocity;

            if (sendData[0] != 3) {
            	
            	//code for status will change based on imu data and kalman filtering
                if (sendData[0] != 1) {
                    acceleration = (int) Math.sqrt(Math.pow((((sendData[15] & 0xFF) << 8 | (sendData[16]) & 0xFF) * 100), 2));
                    stripe = sendData[2];
                    if (stripe >= 1 && stripe <= 9) {
                        status = 3;
                    } else if (stripe > 9) {
                        status = 5;
                    }
                    position = stripe;
                    velocity = ((sendData[3] & 0xFF) << 8 | (sendData[4]) & 0xFF) * 100;
                } else {
                    acceleration = 0;
                    position = 0;
                    velocity = 0;
                }
                System.out.println(status);
                
                ByteBuffer sendDataInByte = ByteBuffer.allocate(34);
                sendDataInByte.put(team_id);
                sendDataInByte.put(status);
                sendDataInByte.put(ByteBuffer.allocate(4).putInt(acceleration).array());
                sendDataInByte.put(ByteBuffer.allocate(4).putInt(position).array());
                sendDataInByte.put(ByteBuffer.allocate(4).putInt(velocity).array());
                sendDataInByte.put(ByteBuffer.allocate(20).putInt(0).array());
                DatagramPacket sendPacket = new DatagramPacket(sendDataInByte.array(), sendDataInByte.array().length, InetAddress.getByName("192.168.0.1"), 3000);
                
                gcsSocket.send(sendPacket);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public byte[] listen(int timeout) {
        byte[] input = new byte[10];
        byte[] buf = {0x05, 0x0D, 0x0A};
        try {
            input = receiveFromRpi(timeout);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
//        sendToRpi(buf);
//        sendToSpaceX(input);
        return input;
    }

    public void close() {
        gcsSocket.close();
    }

    static void setStatus(byte input_status) {
        status = input_status;
    }

    public static void main(String[] args) throws UnknownHostException, IOException {

        //HUC_UDP_Connection huc = new HUC_UDP_Connection();
//        DatagramSocket s = new DatagramSocket();
//        byte team_id = 0x0E;
//        status = 1;
//
//        int acceleration = -5;
//        int velocity = 8;
//        int position = 4;
//
//        ByteBuffer sendDataInByte = ByteBuffer.allocate(2);
//        byte[] spacex_data = new byte[34];
        
        
        //acceleration = convert(acceleration);
        //System.out.println(Integer.toHexString(acceleration));
        //System.out.println(Integer.SIZE);
        //osition = convert(position);
        //velocity = convert(velocity);

        
//        sendDataInByte.put(team_id);
//        sendDataInByte.put(status);
//        sendDataInByte.put(ByteBuffer.allocate(4).putInt(acceleration).array());
//        sendDataInByte.put(ByteBuffer.allocate(4).putInt(position).array());
//        sendDataInByte.put(ByteBuffer.allocate(4).putInt(velocity).array());
//        sendDataInByte.put(ByteBuffer.allocate(20).putInt(0).array());
//        System.out.println(new String(sendDataInByte.array()));

//        DatagramPacket sendPacket = new DatagramPacket(sendDataInByte.array(), sendDataInByte.array().length, InetAddress.getByName("192.168.200.172"), 3000);
//        
//        for (int i = 0; i < 8; i++) {
//            System.out.println("-----------------------------" + i);
//            s.send(sendPacket);
//        }
    }
}