package test_package;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import eu.hansolo.enzo.led.Led;
import eu.hansolo.enzo.led.LedBuilder;
import eu.hansolo.enzo.led.skin.LedSkin;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.Gauge.LedType;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.skins.BatterySkin;
import eu.hansolo.medusa.skins.FlatSkin;
import eu.hansolo.medusa.skins.SpaceXSkin;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.gauge.linear.elements.Indicator;
import test_package.Pod_Status;

public class HUC_GroundStation_GUI_V2 extends Application {
	
	final Gauge velocity_gauge = GaugeBuilder.create().title("Speed").unit("m/s").build();
	final TextArea system_log_area = new TextArea();
	
	Toggle_Switch rpi_switch;
	Toggle_Switch arduino_1_switch;
	Toggle_Switch arduino_2_switch;
	
	TextField acceleration_x_field;
	TextField acceleration_net_field;
	TextField timer_field;
	TextField distance_covered_field;
	
	Gauge battery_temperature[] = new Gauge[4];
	Gauge battery_voltage[] = new Gauge[4];
	
	ColumnConstraints main_panel_col_constraints[] = new ColumnConstraints[2];
	RowConstraints main_panel_row_constraints[] = new RowConstraints[4];
	ColumnConstraints battery_panel_col_constraints[] = new ColumnConstraints[4];
	RowConstraints battery_panel_row_constraints[] = new RowConstraints[4];
	
	Led friction_brakes_led_1;
	Led friction_brakes_led_2;
	Led magnetic_brakes_led_1;
	Led magnetic_brakes_led_2;
	
	Pod_Status SB[] = new Pod_Status[5];
	int x = 0;
	int status = 0;
	
	boolean exit_thread; 
	
	public static void main(String args[]) throws IOException {
		launch(args);
	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		
		GridPane main_panel = new GridPane();
		GridPane speedometer_status_panel = new GridPane();
		GridPane switch_panel = new GridPane();
		GridPane distance_timer_panel = new GridPane();
		GridPane acceleration_panel = new GridPane();
		TabPane tab_panel = new TabPane();
		GridPane text_field_panel = new GridPane();
		GridPane buttons_brakes_panel = new GridPane();
		GridPane buttons_panel = new GridPane();
		GridPane brakes_panel = new GridPane();
		GridPane battery_status_panel = new GridPane();
		HBox pod_status_panel = new HBox();
		StackPane slider_panel = new StackPane();
		
		
		velocity_gauge.setMaxValue(140);
		velocity_gauge.setSkin(new SpaceXSkin(velocity_gauge));
		velocity_gauge.setBarBackgroundColor(Color.DARKGRAY);
		velocity_gauge.setBarColor(Color.rgb(224, 1, 34));
		velocity_gauge.setValue(105.9);
		
		
		Label acceleration_label = new Label("Acceleration (m/s^2):");
		Label acceleration_x_label = new Label("Ax");
		Label acceleration_net_label = new Label("Anet");
		Label timer_label = new Label("Timer (sec):");
		Label distance_label = new Label("Distance Covered (m):");
		acceleration_x_field = new TextField();
		acceleration_net_field = new TextField();
		timer_field = new TextField();
		distance_covered_field = new TextField();
		
		acceleration_x_field.setEditable(false);
		acceleration_net_field.setEditable(false);
		timer_field.setEditable(false);
		distance_covered_field.setEditable(false);
		
		distance_covered_field.setText("0");
		acceleration_net_field.setText("0");
		acceleration_x_field.setText("0");
		timer_field.setText("0");
		
		timer_label.setPadding(new Insets(8, 0, 0, 0));
		distance_label.setPadding(new Insets(12, 0, 0, 0));
		
		distance_timer_panel.add(timer_label, 0, 0);
		distance_timer_panel.add(timer_field, 0, 1);
		distance_timer_panel.add(distance_label, 0, 2);
		distance_timer_panel.add(distance_covered_field, 0, 3);
		acceleration_panel.add(acceleration_label, 0, 0);
		acceleration_panel.add(acceleration_x_label, 0, 1);
		acceleration_panel.add(acceleration_x_field, 0, 2);
		acceleration_panel.add(acceleration_net_label, 0, 3);
		acceleration_panel.add(acceleration_net_field, 0, 4);
		
		
		Label rpi_status_label = new Label("Raspberry Pi");
		rpi_switch = new Toggle_Switch();
		rpi_switch.setSwitchedOn(false);
		Label arduino_1_status_label = new Label("Arduino 1");
		arduino_1_switch = new Toggle_Switch();
		arduino_1_switch.setSwitchedOn(false);
		Label arduino_2_status_label = new Label("Arduino 2");
		arduino_2_switch = new Toggle_Switch();
		arduino_2_switch.setSwitchedOn(false);
		
		
		switch_panel.add(rpi_status_label, 0, 0);
		switch_panel.add(rpi_switch, 1, 0);
		switch_panel.add(arduino_1_status_label, 0, 1);
		switch_panel.add(arduino_1_switch, 1, 1);
		switch_panel.add(arduino_2_status_label, 0, 2);
		switch_panel.add(arduino_2_switch, 1, 2);
		switch_panel.setVgap(8);
		switch_panel.setHgap(8);
		switch_panel.setPadding(new Insets(12, 0, 0, 32));
		
		
		speedometer_status_panel.setVgap(8);
		speedometer_status_panel.setHgap(8);
		speedometer_status_panel.setPadding(new Insets(12, 12, 12, 12));
		speedometer_status_panel.add(velocity_gauge, 0, 0, 2, 4);
		speedometer_status_panel.add(switch_panel, 0, 4, 2, 1);
		speedometer_status_panel.add(distance_timer_panel, 2, 0, 2, 1);
		speedometer_status_panel.add(acceleration_panel, 2, 4, 1, 1);
		speedometer_status_panel.setId("speedometer_panel");
		
		
		Tab systemTab = new Tab("System Status");
		Tab graphTab = new Tab("Graph");
		
		
		system_log_area.setText("The UI is up and running....\nWaiting for RPi for connection....\n");

		
		systemTab.setContent(system_log_area);
		system_log_area.setEditable(false);
		system_log_area.setPrefSize(752, 400);
		
		NumberAxis xAxis = new NumberAxis();
		NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Distance");
		yAxis.setLabel("Velocity");
		LineChart<Number, Number> distVelo = new LineChart<Number, Number>(xAxis, yAxis);
		
		XYChart.Series series = new XYChart.Series();
		series.setName("Actual Values");
//		series.getData().add(new XYChart.Data(1, 1));
//		series.getData().add(new XYChart.Data(2, 5));
//		series.getData().add(new XYChart.Data(4, 10));
//		series.getData().add(new XYChart.Data(7, 20));
//		series.getData().add(new XYChart.Data(11, 40));
//		series.getData().add(new XYChart.Data(15, 100));
//		series.getData().add(new XYChart.Data(20, 175));
//		series.getData().add(new XYChart.Data(26, 280));
//		series.getData().add(new XYChart.Data(37, 400));
//		series.getData().add(new XYChart.Data(50, 550));
		
		distVelo.getData().add(series);
		graphTab.setContent(distVelo);
		
		tab_panel.setId("tab_panel");
		tab_panel.getTabs().addAll(systemTab, graphTab);
		
		
		text_field_panel.setHgap(20);
		text_field_panel.setVgap(8);
		text_field_panel.setPadding(new Insets(12, 44, 12, 12));
		text_field_panel.add(tab_panel, 0, 0, 1, 1);
		
		
		Button start_prepare_for_launch = new Button("Start Communication");
		start_prepare_for_launch.setId("pfl_btn");
		start_prepare_for_launch.setMinWidth(216);
		Button pod_stop = new Button("Pod Stop");
		pod_stop.setId("pod_stop_btn");
		pod_stop.setMinWidth(216);
		
		Button left_spm_btn = new Button("<");
		left_spm_btn.setId("left_spm_btn");
		left_spm_btn.setDisable(true);
		Button stop_spm_btn = new Button("o");
		stop_spm_btn.setId("stop_spm_btn");
		stop_spm_btn.setDisable(true);
		Button right_spm_btn = new Button(">");
		right_spm_btn.setId("right_spm_btn");
		right_spm_btn.setDisable(true);
		Button help_btn = new Button("?");
		help_btn.setId("help_btn");
		
		
		Label friction_brakes_status_label = new Label("Friction Brakes");
		friction_brakes_status_label.setFont(Font.font(20));
		friction_brakes_led_1 = LedBuilder.create().ledColor(Color.MEDIUMVIOLETRED).build();
		friction_brakes_led_1.setPrefWidth(40);
		friction_brakes_led_1.setPrefHeight(40);
//		friction_brakes_led_1.setOn(true);
		friction_brakes_led_2 = LedBuilder.create().ledColor(Color.MEDIUMVIOLETRED).build();
		friction_brakes_led_2.setPrefWidth(40);
		friction_brakes_led_2.setPrefHeight(40);
//		friction_brakes_led_2.setOn(true);
		
		Label magnetic_brakes_status_label = new Label("Magnetic Brakes");
		magnetic_brakes_status_label.setFont(Font.font("Lato", 20));
		magnetic_brakes_status_label.setPadding(new Insets(10, 0, 0, 0));
		magnetic_brakes_led_1 = LedBuilder.create().ledColor(Color.MEDIUMVIOLETRED).build();
		magnetic_brakes_led_1.setPrefWidth(40);
		magnetic_brakes_led_1.setPrefHeight(40);
//		magnetic_brakes_led_1.setOn(true);
		magnetic_brakes_led_2 = LedBuilder.create().ledColor(Color.MEDIUMVIOLETRED).build();
		magnetic_brakes_led_2.setPrefWidth(40);
		magnetic_brakes_led_2.setPrefHeight(40);
//		magnetic_brakes_led_2.setOn(true);
		
		
		buttons_panel.setVgap(15);
		buttons_panel.setHgap(11);
		buttons_panel.add(start_prepare_for_launch, 0, 0, 6, 1);
		buttons_panel.add(pod_stop, 0, 1, 6, 1);
		buttons_panel.add(left_spm_btn, 0, 2, 1, 1);
		buttons_panel.add(stop_spm_btn, 1, 2, 1, 1);
		buttons_panel.add(right_spm_btn, 2, 2, 1, 1);
		buttons_panel.add(help_btn, 5, 2, 1, 1);
		buttons_panel.setPadding(new Insets(21, 0, 21, 44));
		
		brakes_panel.setVgap(12);
		brakes_panel.setHgap(32);
		brakes_panel.add(friction_brakes_status_label, 0, 0, 2, 1);
		brakes_panel.add(friction_brakes_led_1, 0, 1, 1, 1);
		brakes_panel.add(friction_brakes_led_2, 1, 1, 1, 1);
		brakes_panel.add(magnetic_brakes_status_label, 0, 2, 2, 1);
		brakes_panel.add(magnetic_brakes_led_1, 0, 3, 1, 1);
		brakes_panel.add(magnetic_brakes_led_2, 1, 3, 1, 1);
		

		buttons_brakes_panel.add(buttons_panel, 0, 0);
		buttons_brakes_panel.add(brakes_panel, 1, 0);
		buttons_brakes_panel.setHgap(48);
		buttons_brakes_panel.setPadding(new Insets(8, 0, 24, 0));
//		buttons_brakes_panel.setAlignment(Pos.CENTER_LEFT);
		
		
		battery_temperature[0] = GaugeBuilder.create().title("Battery P1").unit("Celsius").build();
		battery_temperature[1] = GaugeBuilder.create().title("Battery P2").unit("Celsius").build();
		battery_temperature[2] = GaugeBuilder.create().title("Battery A1").unit("Celsius").build();
		battery_temperature[3] = GaugeBuilder.create().title("Battery A2").unit("Celsius").build();
		
		
		for (int i = 0; i < battery_temperature.length; i++) {
			
			battery_temperature[i].setMaxValue(60);
			battery_temperature[i].setSkin(new FlatSkin(battery_temperature[i]));
			battery_temperature[i].setBarColor(Color.CORNFLOWERBLUE);
			battery_temperature[i].setAnimated(true);
//			battery_temperature[i].setValue(24);
			battery_temperature[i].setMaxSize(92, 92);
			
			
			battery_voltage[i] = GaugeBuilder.create().animated(true).title("Battery " + (i + 1)).unit("Volts").sectionTextVisible(false).build();
			battery_voltage[i].setSkin(new BatterySkin(battery_voltage[i]));
			battery_voltage[i].setBarColor(Color.MEDIUMSEAGREEN);
			battery_voltage[i].setMaxValue(25.2);
			battery_voltage[i].setAnimated(true);
//			battery_voltage[i].setValue(24);
			battery_voltage[i].setMaxSize(92, 92);
			
			battery_panel_col_constraints[i] = new ColumnConstraints();
			battery_panel_col_constraints[i].setPercentWidth(25);
			
			battery_status_panel.add(battery_temperature[i], i, 0);
			battery_status_panel.add(battery_voltage[i], i, 1);
			
			battery_status_panel.getColumnConstraints().add(battery_panel_col_constraints[i]);
			
		}
		
//		battery_temperature[0].setValue(30);
//		battery_temperature[1].setValue(26);
//		battery_temperature[2].setValue(26);
//		battery_temperature[3].setValue(26);
//		
//		battery_voltage[0].setValue(12.5);
//		battery_voltage[1].setValue(25.2);
//		battery_voltage[2].setValue(25.2);
//		battery_voltage[3].setValue(25.2);
		
		battery_status_panel.setPadding(new Insets(12, 0, 0, 20));
		battery_status_panel.setHgap(72);
		
//		pod_status_panel.setAlignment(Pos.CENTER);
		SB[0] = new Pod_Status("IDLE");
		SB[1] = new Pod_Status("READY FOR LAUNCH");
		SB[2] = new Pod_Status("PUSHER");
		SB[3] = new Pod_Status("CRUISE");
		SB[4] = new Pod_Status("BRAKING");
		pod_status_panel.getChildren().addAll(SB);
		pod_status_panel.setSpacing(50);
		pod_status_panel.setPadding(new Insets(10, 10, 64, 10));
		pod_status_panel.setAlignment(Pos.TOP_CENTER);
		
//		SB[0].set();
//		SB[1].set();
//		SB[2].set();
//		SB[3].set();
//		SB[4].set();
		
		
		Slider slider_pod = new Slider(0, 1280, 0);
		slider_pod.setId("slider_pod");
		slider_pod.setDisable(true);
		slider_pod.setOpacity(1);
		slider_pod.setShowTickLabels(true);
		slider_pod.setShowTickMarks(true);
		slider_pod.setMajorTickUnit(250);
		slider_pod.setMinorTickCount(4);
		//slider.setBlockIncrement(500);
		slider_pod.setValue(15);
		slider_pod.setPadding(new Insets(25, 44, 25, 44));

		
		Slider slider_pusher = new Slider(0, 1280, 0);
		slider_pusher.setId("slider_pusher");
		slider_pusher.setDisable(true);
//		slider_pusher.setShowTickLabels(true);
//		slider_pusher.setShowTickMarks(true);
//		slider_pusher.setMajorTickUnit(250);
//		slider_pusher.setMinorTickCount(4);
		//slider.setBlockIncrement(500);
		slider_pusher.setPadding(new Insets(25, 44, 49, 44));
		slider_pusher.setOpacity(1);
//		slider_pusher.setValue(244);
		
		slider_panel.getChildren().add(slider_pod);
		slider_panel.getChildren().add(slider_pusher);
		
		
		main_panel_col_constraints[0] = new ColumnConstraints();
		main_panel_col_constraints[0].setPercentWidth(38);
		main_panel_col_constraints[1] = new ColumnConstraints();
		main_panel_col_constraints[1].setPercentWidth(62);
		
		main_panel_row_constraints[0] = new RowConstraints();
		main_panel_row_constraints[0].setPercentHeight(40);
		main_panel_row_constraints[1] = new RowConstraints();
		main_panel_row_constraints[1].setPercentHeight(40);
		main_panel_row_constraints[2] = new RowConstraints();
		main_panel_row_constraints[2].setPercentHeight(10);
		main_panel_row_constraints[3] = new RowConstraints();
		main_panel_row_constraints[3].setPercentHeight(10);
		
		main_panel.setId("main_panel");
		main_panel.setPadding(new Insets(0, 0, 12, 0));
		main_panel.getColumnConstraints().add(main_panel_col_constraints[0]);
		main_panel.getColumnConstraints().add(main_panel_col_constraints[1]);
		main_panel.getRowConstraints().add(main_panel_row_constraints[0]);
		main_panel.getRowConstraints().add(main_panel_row_constraints[1]);
		main_panel.getRowConstraints().add(main_panel_row_constraints[2]);
		main_panel.getRowConstraints().add(main_panel_row_constraints[3]);
		main_panel.add(speedometer_status_panel, 0, 0);
		main_panel.add(text_field_panel, 1, 0);
		main_panel.add(buttons_brakes_panel, 0, 1);
		main_panel.add(battery_status_panel, 1, 1);
		main_panel.add(pod_status_panel, 0, 2, 2, 1);
		main_panel.add(slider_panel, 0, 3, 2, 1);
		
		
		Scene scene = new Scene(main_panel);
		scene.getStylesheets().add(getClass().getResource("UI_CSS.css").toExternalForm());
		
        primaryStage.setScene(scene);
        
        primaryStage.getIcons().add(new Image(new File("src\\Resources\\Hyperloop_Icon.png").toURI().toString()));
        primaryStage.setMaximized(true);
        primaryStage.show();
        
        
        start_prepare_for_launch.setOnAction(new EventHandler <ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String button_text = start_prepare_for_launch.getText();
				
				if(button_text.equals("Start Communication")) {
					start_prepare_for_launch.setText("Prepare for Launch");
					start_communication();
				}
				
			}
        });
        pod_stop.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
			public void handle(ActionEvent arg0) {
        		stop_communication();
        	}
        });
    }
	
	
	public void start_communication() {
		
		System.out.println("Communication Start");
//		system_log_area.setText("The UI is up and running....\nWaiting for RPi for connection....\nCommunication Start....\n");
		// Create a Runnable
		Runnable task = new Runnable() {
		
			@Override
			public void run() {
				try {
//					system_log_area.setText("Thread created....\n");
					Data_Processor_Unit data_processor = new Data_Processor_Unit();
//					system_log_area.setText("Data Processor created....\n");
					double[] parsed_data = new double[10];
					exit_thread = false;
					
					while (!exit_thread) {
						
						parsed_data = data_processor.getData();
						updateUI(parsed_data);
						
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		};
			
		// Run the task in a background thread
		Thread backgroundThread = new Thread(task);
		// Terminate the running thread if the application exits
		backgroundThread.setDaemon(true);
		// Start the thread
		backgroundThread.start();
	}
	
	public void stop_communication() {
		// thread for start communication is still running
//		friction_brakes_led_1.setLedColor(Color.RED);
		friction_brakes_led_1.setOn(true);
		friction_brakes_led_2.setOn(true);
		magnetic_brakes_led_1.setOn(true);
		magnetic_brakes_led_2.setOn(true);
		double[] parsed_data = new double[10];
		updateUI(parsed_data);
		exit_thread = true;
	}
	
	public void updateUI(double[] parsed_data) 
	{
		Platform.runLater(new Runnable() 
		{
			@Override
			public void run() {
				// parsed_data[0] is msgType
				system_log_area.appendText(Arrays.toString(parsed_data)+"\n");
				if (parsed_data[0] == 0) {
					rpi_switch.setSwitchedOn(true);
//					system_log_area.appendText("Connected to RPi\n");
					
//					//Setting appropriate status
					for (int i = 0; i < SB.length; i++) {
						if(parsed_data[1] == (i + 1))
							SB[i].set(true);
						else
							SB[i].set(false);
					}
					SB[0].set(true);
					if(status != (int) parsed_data[1]) {
						if (parsed_data[1] == 1) {
							system_log_area.appendText("Pod is in Idle State\n");
						} else if (parsed_data[1] == 2) {
							system_log_area.appendText("Pod is Ready For Launch\nPress the Ready for Launch button to initiate Pusher Phase\n");
						} else if (parsed_data[1] == 3) {
							system_log_area.appendText("Ready For Launch Button Clicked\nPod is in Pusher Phase\n");
						} else if (parsed_data[1] == 4) {
							system_log_area.appendText("Pod is in Cruise State\n");
						} else if(parsed_data[1] == 5) {
							system_log_area.appendText("Pod is in Braking Phase\n");
							system_log_area.appendText("Pod has come to a halt at "+ parsed_data[6] +"m\n");
						}
						status = (int) parsed_data[1];
					}
					if(parsed_data[3] == 1) {
						arduino_1_switch.setSwitchedOn(true);
					} else {
						arduino_1_switch.setSwitchedOn(false);
					}				
					if(parsed_data[4] == 1) {
						arduino_2_switch.setSwitchedOn(true);
					} else {
						arduino_2_switch.setSwitchedOn(false);
					}
					
					//Set timer value
					timer_field.setText(Double.toString(parsed_data[5]));
					distance_covered_field.setText(Double.toString(parsed_data[6]));
					acceleration_x_field.setText(Double.toString(parsed_data[7]));
					acceleration_net_field.setText(Double.toString(parsed_data[8]));
					velocity_gauge.setValue(parsed_data[9]);					
					for (int i = 0; i < battery_temperature.length; i++) {						
						battery_temperature[i].setValue(parsed_data[i]);
						battery_voltage[i].setValue(parsed_data[i+4]);					
					}	
				}		
			}
		}); // end of Platform.runLater
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	public void updateUI_old(double[] parsed_data) 
	{
		Platform.runLater(new Runnable() 
		{
			@Override
			public void run() {
				// parsed_data[0] is msgType
				if (parsed_data[0] == 3) { // arduinos fail but RPi is still on while running
					
					//Setting Rpi status
					rpi_switch.setSwitchedOn(true);
					
					//Setting arduinos switched off
					arduino_1_switch.setSwitchedOn(false);
					arduino_2_switch.setSwitchedOn(false);
					
					//Setting all statuses off to indicate fault state 
					for (int i = 0; i < SB.length; i++) {
						SB[i].set(false);
					}
					// Have to write the code for the case when RPi is off -> everything else is also off
				} else { // Rpi and arduinos are on
					
					//Setting Rpi status
					rpi_switch.setSwitchedOn(true);
					system_log_area.appendText("Connected to RPi\n");
					
					//Setting appropriate status
					for (int i = 0; i < SB.length; i++) {
						if(parsed_data[1] == (i + 1))
							SB[i].set(true);
						else
							SB[i].set(false);
					}
					if(status != (int) parsed_data[1]) {
						if (parsed_data[1] == 1) {
							system_log_area.appendText("Pod is in Idle State\n");
						} else if (parsed_data[1] == 2) {
							system_log_area.appendText("Pod is Ready For Launch\nPress the Ready for Launch button to initiate Pusher Phase\n");
						} else if (parsed_data[1] == 3) {
							system_log_area.appendText("Ready For Launch Button Clicked\nPod is in Pusher Phase\n");
						} else if (parsed_data[1] == 4) {
							system_log_area.appendText("Pod is in Cruise State\n");
						} else if(parsed_data[1] == 5) {
							system_log_area.appendText("Pod is in Braking Phase\n");
							system_log_area.appendText("Pod has come to a halt at "+ parsed_data[6] +"m\n");
						}
						status = (int) parsed_data[1];
					}
					
					//Setting arduino 1 health status
					if(parsed_data[3] == 1) {
						arduino_1_switch.setSwitchedOn(true);
					} else {
						arduino_1_switch.setSwitchedOn(false);
					}
					
					//Setting arduino 2 health status					
					if(parsed_data[4] == 1) {
						arduino_2_switch.setSwitchedOn(true);
					} else {
						arduino_2_switch.setSwitchedOn(false);
					}
					
					//Set timer value
					timer_field.setText(Double.toString(parsed_data[5]));
					
					//Set distance value
					distance_covered_field.setText(Double.toString(parsed_data[6]));
					
					//Set Ax value
					acceleration_x_field.setText(Double.toString(parsed_data[7]));
					
					//Set Anet value
					acceleration_net_field.setText(Double.toString(parsed_data[8]));
					
					//Set Velocity value
					velocity_gauge.setValue(parsed_data[9]);
					
					for (int i = 0; i < battery_temperature.length; i++) {
						
						battery_temperature[i].setValue(parsed_data[i + 10]);
						battery_voltage[i].setValue(parsed_data[i + 14]);
						
					}
					
				}
//				velocity_gauge.setValue(x++);
//				System.out.println(velocity_gauge.getValue());
				
			}
		}); // end of Platform.runLater
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * Connection to RPi is lost. 
	 */
	public static void connectionLost() {
		// Call UpdateUI() and create a new value in parsed_data to signify lost connection to RPi
		
	}
}
