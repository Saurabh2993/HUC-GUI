package test_package;

import java.io.IOException;

import javafx.scene.control.TextArea;

public class Data_Processor_Unit {
	
	HUC_UDP_Connection udp_conn;
	CSV_File_Writer csv_writer;
	
	int counter = 0; // number of times we have received packets from RPi
	int status = 1; // status of pod {IDLE, READY FOR LAUNCH, PUSHER, CRUISE, BRAKING}
	int health;
	String binary_health;
	int ard_1_status = 0;
	int ard_2_status = 0;
	double timer;
	short ax;
	short anet;
	double velocity;
	double battery_temperature[] = new double[4];
	double battery_voltage[] = new double[4];
	
	byte[] input_data;
	double[] parsed_data;
	
	public Data_Processor_Unit() throws IOException {
		udp_conn = new HUC_UDP_Connection();
		csv_writer = new CSV_File_Writer();
	}
	
	public double[] getData() {
		if (counter == 0) {
			// first time getting data from RPi, so wait indefinitely for the first data 
			input_data = udp_conn.listen(0); 
		} else {
			input_data = udp_conn.listen(4000); // time out, not port number
		}
		counter++;
		parsed_data = parse_input(input_data);	
		return parsed_data;
	}
	private double[] parse_input(byte[] input_data) {
		double parsed_data[] = new double[10];
		for (int i = 0; i < input_data.length; i++) parsed_data[i] = input_data[i];
		return parsed_data;
	}
	private double[] parse_input_old(byte[] input_data) {
		double parsed_data[] = new double[25];
		int msg_type = new Integer(input_data[0]);
		if (msg_type == 3) { // while running, arduinos fail but RPi is still on
			parsed_data[0] = msg_type; parsed_data[1] = status;	parsed_data[2] = 1;	//Rpi is on	 	
			//Arduinos are off
			ard_1_status = 0;
			parsed_data[3] = ard_1_status;
			ard_2_status = 0;
			parsed_data[4] = ard_2_status;			
			//Since Arduinos are off, we don't get any data from Arduino we leave rest of the fields unchanged
			return parsed_data;		
		} else if (msg_type == 1) { // pod is in IDLE state, all numbers are 0 		
			parsed_data[0] = msg_type;		
			parsed_data[1] = status;	//RPi health status	
			parsed_data[2] = 1;			
        } else { // everything is working properly       	
        	parsed_data[0] = msg_type;			
			//need to add code to compute current status of the pod from Kalman filter
			parsed_data[1] = status;	
			//RPi health status
			parsed_data[2] = 1;
        }
		
		//Set health status of arduinos
		int health = new Integer(input_data[1]);
        binary_health = Integer.toBinaryString(health);
//        binary_health = String.format("%1$08d", Integer.parseInt(binary_health, 2));
        
//        String individual_health_nodes[] = binary_health.split("");
        char[] individual_health_nodes = binary_health.toCharArray();
        for (int i = 0; i < 2; i++) {
            if (individual_health_nodes[7 - i] - '0' == 1) {
            	parsed_data[i + 3] = 1;
            } else if (individual_health_nodes[7 - i] - '0' == 0) {
            	parsed_data[i + 3] = 0;
            } else {
                continue;
            }
        }		
		//Timer
		parsed_data[5] = new Double((input_data[7] & 0xFF) | ((input_data[8]) & 0xFF) << 8);	
		//Although static values, arduinos will start sending data
		//Distance Covered
		int stripe = input_data[2];
		parsed_data[6] = new Double(stripe) * 100;
		//Ax and Anet
		ax = (short) (((input_data[5] & 0xFF) | (input_data[6] & 0xFF) << 8));
		parsed_data[7] = ax;
		anet = (short) Math.sqrt(Math.pow(ax, 2));
		parsed_data[8] = anet;
		//Velocity
		velocity = new Double(((input_data[3] & 0xFF) | ((input_data[4]) & 0xFF) << 8) * 100);
		parsed_data[9] = velocity;
		//Battery temperatures and voltages
		for (int i = 0; i < battery_temperature.length; i++) {
			battery_temperature[i] = ((input_data[i * 2 + 9] & 0xFF) | ((input_data[i * 2 + 10] & 0xFF)) << 8) / 10.0;
			parsed_data[i + 10] = battery_temperature[i]; 
			battery_voltage[i] = (input_data[i + 17] & 0xFF) / 10.0;
			parsed_data[i + 14] = battery_voltage[i];
		}
		//Brakes indicator variables. 
		// 0 or 4: retracted 0, 1 0r 2: actuating 1, 3: retracting back
		//Friction Brakes
		if (input_data[21] == 0 || input_data[21] == 4) {
            parsed_data[18] = 0;
        } else if (input_data[21] == 1 || input_data[21] == 2) {
        	parsed_data[18] = 1;
        } else if (input_data[21] == 3) {
        	parsed_data[18] = 2;
        } else {
        	parsed_data[18] = 0;
        }
		if (input_data[22] == 0 || input_data[22] == 4) {
            parsed_data[19] = 0;
        } else if (input_data[22] == 1 || input_data[22] == 2) {
        	parsed_data[19] = 1;
        } else if (input_data[22] == 3) {
        	parsed_data[19] = 2;
        } else {
        	parsed_data[19] = 0;
        }
		//Magnetic Brakes
		if (input_data[23] == 0 || input_data[23] == 4) {
            parsed_data[20] = 0;
        } else if (input_data[23] == 1 || input_data[23] == 2) {
        	parsed_data[20] = 1;
        } else if (input_data[23] == 3) {
        	parsed_data[20] = 2;
        } else {
        	parsed_data[20] = 0;
        }
		if (input_data[24] == 0 || input_data[24] == 4) {
            parsed_data[21] = 0;
        } else if (input_data[24] == 1 || input_data[24] == 2) {
        	parsed_data[21] = 1;
        } else if (input_data[24] == 3) {
        	parsed_data[21] = 2;
        } else {
        	parsed_data[21] = 0;
        }
		
		return parsed_data;
	}
}