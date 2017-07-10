package test_package;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class TestInput {

    private static DatagramSocket clientSocket;
    private static DatagramPacket sendPacket;
    private static DatagramPacket receivePacket;
    private static boolean flag = true;

    public static void main(String[] args) throws Exception {

        // UDP connection with server to send data
        clientSocket = new DatagramSocket(5000);

        Thread receiveThread = new Thread() {
            @Override
            public void run() {
                byte[] receiveData = new byte[3];
                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                try {
                    clientSocket.receive(receivePacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] incoming = receivePacket.getData();
                int[] incomingHex = new int[incoming.length];
                System.out.print("\nData received from server: ");
                for (int j = 0; j < incoming.length; j++) {
                    incomingHex[j] = (incoming[j] & 0xFF);
                    System.out.print("\t" + incomingHex[j]);
                }
            }
        };

        Thread sendThread = new Thread() {
            @Override
            public void run() {
                byte[] sendData = {0x00, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x10};//, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37};
                byte[] sendData2 = {(byte) 0x00, (byte) 0x02, (byte) 0xFD, (byte) 0x0C, (byte) 0x0B, (byte) 0xFA, (byte) 0xF9, (byte) 0xF8, (byte) 0xF7, (byte) 0xF6};//, (byte) 0xF5, (byte) 0xF4, (byte) 0xF3, (byte) 0xF2, (byte) 0xF1, (byte) 0xF0, (byte) 0xEF, (byte) 0xEE, (byte) 0xED, (byte) 0xEC, (byte) 0xEB, (byte) 0xEA, (byte) 0xE9, (byte) 0xE8, (byte) 0xE7, (byte) 0xE6, (byte) 0xE5, (byte) 0xE4, (byte) 0xE3, (byte) 0xE2, (byte) 0xE1, (byte) 0xE0, (byte) 0xDF, (byte) 0xDE, (byte) 0xDD, (byte) 0xDC, (byte) 0xDB};
                try {
                    if (flag) {
                        sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("localhost"), 3000);
                        flag = false;
                    } else {
                        sendPacket = new DatagramPacket(sendData2, sendData2.length, InetAddress.getByName("localhost"), 3000);
                        flag = true;
                    }
                    clientSocket.send(sendPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("--------------------------------- Data sent to server ---------------------------------");
            }
        };
        try {
            while (true) {
                sendThread.run();
                //receiveThread.run();
                Thread.sleep(200);
            }
        } catch (Exception e) {
            clientSocket.close();
            System.out.println("TestInput class ended");
            e.printStackTrace();
        }
    }
}
