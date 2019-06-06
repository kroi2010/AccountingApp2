package home.accounting.controller;

import home.accounting.controller.custom.transitions.ResizeGridTransition;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class GridAnimationThread extends Thread{
	
	protected Duration duration;
	protected Object gridObject;
	protected int passedValue;
	
	public GridAnimationThread(Duration dur, Object c, Integer newValue){
		this.duration = dur;
		this.gridObject = c;
		this.passedValue = newValue;
	}
	
	public void run(){
		ResizeGridTransition gridTransition = new ResizeGridTransition(duration, gridObject, passedValue);
		gridTransition.play();
		gridTransition.setOnFinished(EventHandler -> {
			//System.out.println("Animation finished for "+this.gridObject);
		});
	}
	
}
