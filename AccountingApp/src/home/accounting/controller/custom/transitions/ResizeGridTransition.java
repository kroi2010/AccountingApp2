package home.accounting.controller.custom.transitions;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.util.Duration;

public class ResizeGridTransition extends Transition{
	
	private Object constraint;
	private int startValue;
	private int newValue;
	private int distance;
	
	
	public ResizeGridTransition(Duration duration, Object c, int newValue){
		setCycleDuration(duration);
		this.constraint = c;
		this.newValue = newValue;
		if(this.constraint instanceof ColumnConstraints){
			this.startValue = (int) ((ColumnConstraints) this.constraint).getPercentWidth();
		}else if(this.constraint instanceof RowConstraints){
			this.startValue = (int) ((RowConstraints) constraint).getPercentHeight();
		}
		this.distance = this.newValue - this.startValue;
	}

	@Override
	protected void interpolate(double frac) {
		// TODO Auto-generated method stub
		if(this.constraint instanceof ColumnConstraints){
			((ColumnConstraints) this.constraint).setPercentWidth(this.startValue + (this.distance*frac));
			System.out.println("New value "+((ColumnConstraints)this.constraint).getPercentWidth());
		}else if(this.constraint instanceof RowConstraints){
			System.out.println("Row");
		}
	}
	
}
