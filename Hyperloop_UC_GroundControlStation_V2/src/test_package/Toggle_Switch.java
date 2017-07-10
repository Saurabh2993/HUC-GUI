package test_package;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;


class Toggle_Switch extends Parent {
	
	private BooleanProperty switchedOn = new SimpleBooleanProperty(false);
	private TranslateTransition translateAnimation = new TranslateTransition(Duration.seconds(0.15));
	private FillTransition fillAnimation = new FillTransition(Duration.seconds(0.25));
	private ParallelTransition animation = new ParallelTransition(translateAnimation, fillAnimation);
	
	private BooleanProperty switchedOnProperty() {
		return switchedOn;
	}
	
	public Toggle_Switch() {
		Rectangle background = new Rectangle(50, 20);
		background.setFill(Color.RED);
		background.setStroke(Color.DARKGRAY);
		background.setArcHeight(20);
		background.setArcWidth(20);
		
		Circle trigger = new Circle(10);
		trigger.setCenterX(10);
		trigger.setCenterY(10);
		trigger.setFill(Color.WHITE);
		trigger.setStroke(Color.DARKGRAY);
		
		translateAnimation.setNode(trigger);
		fillAnimation.setShape(background);
		getChildren().addAll(background, trigger);
		
		switchedOn.addListener((obs, oldState, newState) -> {
			boolean isOn = newState.booleanValue();
			translateAnimation.setToX(isOn ? 50 - 20 : 0);
			fillAnimation.setFromValue(isOn ? Color.RED : Color.GREEN);
			fillAnimation.setToValue(isOn ? Color.GREEN : Color.RED);
			animation.play();
		});
	}
	
	public void setSwitchedOn(boolean toggle) {
		this.switchedOn.set(toggle);
	}
}