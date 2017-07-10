package test_package;

import java.io.File;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.skins.BatterySkin;
import eu.hansolo.medusa.skins.FlatSkin;
import eu.hansolo.medusa.skins.SpaceXSkin;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HUC_GroundStation_GUI_V1 extends Application {
	
	
	private ImageView IDLE;
	private ImageView RFL;
	private ImageView PUSHER;
	private ImageView CRUISE;
	private ImageView BRAKE;
	private Gauge velocity_gauge;
	private Gauge battery_temperature[] = new Gauge[4];
	private Gauge battery_voltage[] = new Gauge[4];
	
	
	public static void main(String args[]) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		BorderPane main_panel = new BorderPane();
		GridPane left_panel = new GridPane();
		VBox center_panel = new VBox();
		GridPane battery_panel = new GridPane();
		GridPane arduino_panel = new GridPane();
		GridPane pod_status_panel = new GridPane();
		GridPane right_panel = new GridPane();
		GridPane connection_panel = new GridPane();
		GridPane acceleration_panel = new GridPane();
		GridPane spm_panel = new GridPane();
		StackPane slider_panel = new StackPane();
		
		ColumnConstraints colConstraints[] = new ColumnConstraints[4];
		
		left_panel.setPadding(new Insets(20, 20, 20, 20));
		//left_panel.setVgap(48);
		//left_panel.setHgap(12);
		
		right_panel.setPadding(new Insets(20, 20, 20, 20));
		right_panel.setVgap(12);
		right_panel.setHgap(12);
		right_panel.setAlignment(Pos.TOP_CENTER);
		
		battery_panel.setPadding(new Insets(20, 0, 20, 100));
		battery_panel.setVgap(32);
		battery_panel.setVgap(32);
		
		//arduino_panel.setPadding(new Insets(20, 0, 20, 100));
		
		pod_status_panel.setPadding(new Insets(100, 0, 0, 132));
		
		velocity_gauge = GaugeBuilder.create().title("Speed").unit("m/s").build();
		velocity_gauge.setSkin(new SpaceXSkin(velocity_gauge));
		velocity_gauge.setBarBackgroundColor(Color.DARKGRAY);
		velocity_gauge.setBarColor(Color.rgb(224, 1, 34));
		
		velocity_gauge.setThreshold(300);
		velocity_gauge.setMaxValue(200);
		System.out.println(velocity_gauge.getMaxMeasuredValue());
		velocity_gauge.setValue(200);
		velocity_gauge.setMaxSize(160, 160);
		
//		Led connection_status_indicator = new Led();
//		connection_status_indicator.setId("connection_status_indicator");
//		connection_status_indicator.setPrefSize(60, 60);
//		connection_status_indicator.setOn(true);
//		connection_status_indicator.setLedColor(Color.LIMEGREEN);
		Label connection_status_label = new Label("Connection Status");
		connection_panel.setId("connection_panel");
		connection_panel.setPadding(new Insets(40, 0, 32, 0));
//		connection_panel.add(connection_status_indicator, 0, 0);
//		connection_panel.add(connection_status_label, 0, 1);
		
		Label acceleration_label = new Label("Acc: ");
		Label acceleration_x_label = new Label("Ax");
		Label acceleration_net_label = new Label("Anet");
		TextField acceleration_x_field = new TextField();
		TextField acceleration_net_field = new TextField();
		acceleration_x_field.setMaxWidth(40);
		acceleration_net_field.setMaxWidth(40);
		acceleration_panel.add(acceleration_label, 0, 1);
		acceleration_panel.add(acceleration_x_label, 1, 0);
		acceleration_panel.add(acceleration_net_label, 2, 0);
		acceleration_panel.add(acceleration_x_field, 1, 1);
		acceleration_panel.add(acceleration_net_field, 2, 1);
		
		for (int i = 0; i < battery_temperature.length; i++) {
			
			battery_temperature[i] = GaugeBuilder.create().title("Battery " + (i + 1)).unit("Celsius").build();
			battery_temperature[i].setSkin(new FlatSkin(battery_temperature[i]));
			battery_temperature[i].setBarColor(Color.CORNFLOWERBLUE);
			battery_temperature[i].setValue(24);
			battery_temperature[i].setMaxSize(92, 92);
			
			
			battery_voltage[i] = GaugeBuilder.create().animated(true).title("Battery " + (i + 1)).unit("Volts").sectionTextVisible(false).build();
			battery_voltage[i].setSkin(new BatterySkin(battery_voltage[i]));
			battery_voltage[i].setBarColor(Color.MEDIUMSEAGREEN);
			battery_voltage[i].setMaxValue(16.6);
			battery_voltage[i].setAnimated(true);
			battery_voltage[i].setValue(10);
			battery_voltage[i].setMaxSize(92, 92);
			
			colConstraints[i] = new ColumnConstraints();
			colConstraints[i].setPercentWidth(25);
			
			battery_panel.add(battery_temperature[i], i, 0);
			battery_panel.add(battery_voltage[i], i, 1);
			
			battery_panel.getColumnConstraints().add(colConstraints[i]);
			
		}
		
		Label arduion_health_status_label = new Label("Arduino Status:");
		arduion_health_status_label.setId("arduion_health_status_label");
		//arduion_health_status_label.setPadding(new Insets(0, 0, 0, 40));
		
		ColumnConstraints arduino_column_constraints[] = new ColumnConstraints[2];
		
//		Led arduino_health_led[] = new Led[2];
		
		for (int i = 0; i < 1; i++) {
			
//			arduino_health_led[i] = new Led();
//			arduino_health_led[i].setOn(true);
//			arduino_health_led[i].setLedColor(Color.MEDIUMBLUE);
//			arduino_health_led[i].setPrefSize(40, 40);
			
			arduino_column_constraints[i] = new ColumnConstraints();
			arduino_column_constraints[i].setPercentWidth(44);
			
//			arduino_panel.add(arduino_health_led[i], i, 1);
			arduino_panel.getColumnConstraints().add(arduino_column_constraints[i]);
			
		}
		
		IDLE = new ImageView(new Image(new File("src\\Resources\\IDLE.png").toURI().toString()));
		RFL = new ImageView(new Image(new File("src\\Resources\\RFL.png").toURI().toString()));
		PUSHER = new ImageView(new Image(new File("src\\Resources\\PUSHER.png").toURI().toString()));
		CRUISE = new ImageView(new Image(new File("src\\Resources\\CRUISE.png").toURI().toString()));
		BRAKE = new ImageView(new Image(new File("src\\Resources\\BRAKE.png").toURI().toString()));
		
		pod_status_panel.add(IDLE, 0, 0);
		pod_status_panel.add(RFL, 1, 0);
		pod_status_panel.add(PUSHER, 2, 0);
		pod_status_panel.add(CRUISE, 3, 0);
		pod_status_panel.add(BRAKE, 4, 0);
		
		Button prepare_for_launch = new Button("Prepare For Launch");
		prepare_for_launch.setMinWidth(150);
		Button pod_stop = new Button("Pod Stop");
		pod_stop.setMinWidth(150);
		
		Button left_spm_btn = new Button("<");
		left_spm_btn.setId("left_spm_btn");
		Button stop_spm_btn = new Button("o");
		stop_spm_btn.setId("stop_spm_btn");
		Button right_spm_btn = new Button(">");
		right_spm_btn.setId("right_spm_btn");
		
		
//		spm_panel.setPadding(new Insets(20, 20, 20, 20));
//		spm_panel.setVgap(48);
		spm_panel.setPadding(new Insets(250, 0, 0, 0));
		spm_panel.setHgap(12);
		spm_panel.add(left_spm_btn, 0, 0);
		spm_panel.add(stop_spm_btn, 1, 0);
		spm_panel.add(right_spm_btn, 2, 0);
		
		Slider slider_pod = new Slider(0, 1280, 0);
		slider_pod.setId("slider_pod");
		//slider_pod.setDisable(true);
		slider_pod.setShowTickLabels(true);
		slider_pod.setShowTickMarks(true);
		slider_pod.setMajorTickUnit(250);
		slider_pod.setMinorTickCount(4);
		//slider.setBlockIncrement(500);
		slider_pod.setPadding(new Insets(25, 100, 25, 100));

		
		Slider slider_pusher = new Slider(0, 1280, 0);
		slider_pusher.setId("slider_pusher");
		//slider_pusher.setDisable(true);
//		slider_pusher.setShowTickLabels(true);
//		slider_pusher.setShowTickMarks(true);
//		slider_pusher.setMajorTickUnit(250);
//		slider_pusher.setMinorTickCount(4);
		//slider.setBlockIncrement(500);
		slider_pusher.setPadding(new Insets(25, 100, 49, 100));
		slider_pusher.setOpacity(1);
		slider_pusher.setValue(250);
		//grid.add(slider, 0, 0, 10, 1);
		
		
		slider_panel.getChildren().add(slider_pod);
		slider_panel.getChildren().add(slider_pusher);
		
		
		
		left_panel.add(velocity_gauge, 0, 0);
		left_panel.add(connection_panel, 0, 1);
		left_panel.add(arduion_health_status_label, 0, 2);
		left_panel.add(arduino_panel, 0, 3);
		
		center_panel.getChildren().add(battery_panel);
		//center_panel.getChildren().add(arduino_panel);
		center_panel.getChildren().add(pod_status_panel);
		
		right_panel.add(prepare_for_launch, 0, 0);
		right_panel.add(pod_stop, 0, 1);
		right_panel.add(spm_panel, 0, 2);
		
		main_panel.setLeft(left_panel);
		main_panel.setCenter(center_panel);
		main_panel.setRight(right_panel);
		main_panel.setBottom(slider_panel);
		main_panel.getStyleClass().add("main_panel");
		
		Scene scene = new Scene(main_panel, 500, 500);
		scene.getStylesheets().add(getClass().getResource("GUI_stylesheet.css").toExternalForm());
		
        primaryStage.setScene(scene);
        
        primaryStage.getIcons().add(new Image(new File("src\\Resources\\Hyperloop_Icon.png").toURI().toString()));
        primaryStage.setMaximized(true);
        primaryStage.show();
		
	}
}