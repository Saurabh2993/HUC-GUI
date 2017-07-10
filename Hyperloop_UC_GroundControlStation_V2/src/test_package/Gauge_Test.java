package test_package;

import java.awt.event.ActionListener;
import javax.swing.LayoutFocusTraversalPolicy;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.skins.FlatSkin;
import eu.hansolo.medusa.skins.SpaceXSkin;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;



public class Gauge_Test extends Application {
	
	Gauge gauge;
	int x = 1;
	
	public static void main(String args[]) {
		
		Gauge_Test gt = new Gauge_Test();
		//gt.startTask();
		launch(args);
		System.out.println("lllllllllllllllllllllllllllllllll");
	}
	
	@Override
    public void start(Stage primaryStage) {
		
		gauge = GaugeBuilder.create().title("Speed").unit("m/s").build();
		gauge.setSkin(new SpaceXSkin(gauge));
		gauge.setBarBackgroundColor(Color.WHITE);
		
		gauge.setBarBorderColor(Color.BLACK);
		gauge.setBarColor(Color.rgb(224, 1, 34));
		//gauge.setMaxSize(200, 200);
		
		BorderPane root = new BorderPane();
		
		Button start_button = new Button();
        start_button.setText("Start");
        
        root.setLeft(start_button);
        root.setCenter(gauge);

        Scene scene = new Scene(root, 500, 500);
        
        primaryStage.setScene(scene);
        primaryStage.show();
        
        
        start_button.setOnAction(new EventHandler <ActionEvent>() 
		{
            public void handle(ActionEvent event) 
            {
            	startTask();
            }
        });
    }
	
	public void startTask() 
	{
		// Create a Runnable
		Runnable task = new Runnable()
		{
			
			public void run()
			{
				runTask();
			}
		};

		// Run the task in a background thread
		Thread backgroundThread = new Thread(task);
		// Terminate the running thread if the application exits
		backgroundThread.setDaemon(true);
		// Start the thread
		backgroundThread.start();
	}
	
	public void runTask() {
		
		try {
			
//			Thread.sleep(10000);
			for (int i = 0; i < 101; i++) {
				
				final int x = i;
				
				Platform.runLater(new Runnable() 
				{

					@Override
					public void run() {
					
						gauge.setValue(10);

					}
					
				});
				
				Thread.sleep(100);
			}
		}catch (InterruptedException e1) {
			e1.printStackTrace();
		} 
	}
}
//https://examples.javacodegeeks.com/desktop-java/javafx/javafx-concurrency-example/