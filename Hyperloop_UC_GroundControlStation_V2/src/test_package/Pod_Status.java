package test_package;

import javafx.animation.FillTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

class Pod_Status extends Parent {
	
	private BooleanProperty isActive = new SimpleBooleanProperty(false);
	private FillTransition fillAnimation = new FillTransition(Duration.seconds(0.5));
	
	public Pod_Status(String txt) {
		Pane pane = new Pane();
		
		Rectangle rect = new Rectangle(200, 50);
		rect.setArcHeight(25);
		rect.setArcWidth(25);
		rect.setFill(Color.WHITE);
		rect.setStroke(Color.DARKGRAY);
		
		Label text = new Label(txt);
		text.setPrefSize(200, 50);
		text.setAlignment(Pos.CENTER);
		text.setFont(new Font(16));
		
		fillAnimation.setShape(rect);
		
		pane.getChildren().addAll(rect, text);
		getChildren().add(pane);
		
		isActive.addListener((obs, oldState, newState) -> {
			boolean isOn = newState.booleanValue();
			fillAnimation.setFromValue(isOn ? Color.WHITE : Color.GREEN);
			fillAnimation.setToValue(isOn ? Color.GREEN : Color.WHITE);
			fillAnimation.play();
		});
	}
	
	public void set(boolean toggle) {
		this.isActive.set(toggle);
	}
}