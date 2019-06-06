package home.accounting.controller;

import java.util.List;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class GridAnimationHandler {

	private Duration duration;
	private GridPane gridPane;
	private List<Integer> newCol;
	private List<Integer> newRow;
	
	public GridAnimationHandler(Duration dur, GridPane grid, List<Integer> newCol, List<Integer> newRow){
		this.duration = dur;
		this.gridPane = grid;
		this.newCol = newCol;
		this.newRow = newRow;
		
		createThreads();
	}
	
	private void createThreads(){
		// check if all passed columns are in place
		if(this.newCol.size() == this.gridPane.getColumnConstraints().size()){
			int columnCounter = 0;
			for(ColumnConstraints columnConstraints : this.gridPane.getColumnConstraints()){
				GridAnimationThread columnThread = new GridAnimationThread(this.duration, columnConstraints, this.newCol.get(columnCounter));
				columnThread.start();
				columnCounter++;
			}
		}
		//check if all passed rows are in place
		if(this.newRow.size() == this.gridPane.getRowConstraints().size()){
			
		}
	}
	
}
