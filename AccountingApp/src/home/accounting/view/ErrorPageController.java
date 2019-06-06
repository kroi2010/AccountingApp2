package home.accounting.view;

import home.accounting.controller.CoreConstants;
import home.accounting.controller.PaneNavigator;
import home.accounting.controller.Word;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class ErrorPageController {
	@FXML
	private Button button;
	
	@FXML
	private Label firstLineLabel;
	
	@FXML
	private Label secondLineLabel;
	
	@FXML
	private Circle pageTransition;
	
	@FXML
	private AnchorPane container;
	
	@FXML
	private void initialize() {
		Rectangle mask = new Rectangle();
	    mask.widthProperty().bind(container.widthProperty());
	    mask.heightProperty().bind(container.heightProperty());
	    container.setClip(mask);
	}
	
	public void setupPage(String error){
		EventHandler<ActionEvent> addFlatInfo = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				//pageTransition();
				PaneNavigator.loadPane(PaneNavigator.clear, PaneNavigator.center);
				PaneNavigator.loadPane(PaneNavigator.flatEdit, PaneNavigator.center);
			}
			
		};
		
		
		switch (error) {
		case CoreConstants.MISSING_HOUSE_ERR:
			
			break;

		case CoreConstants.MISSING_FLATS_ERR:
			button.setText(Word.translate("fix_it"));
			button.setOnAction(addFlatInfo);
			firstLineLabel.setText(Word.translate("oh_no"));
			secondLineLabel.setText(Word.translate("receipts_info_is_missing"));
			break;
		}
	}
}