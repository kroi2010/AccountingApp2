package home.accounting.view;

import home.accounting.controller.CurrentUser;
import home.accounting.controller.PaneNavigator;
import home.accounting.controller.Word;
import home.accounting.model.House;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class MainSideMenuController {
	
	@FXML
	private Label firstName;
	
	@FXML
	private Label lastName;
	
	@FXML
	private Label houseNameCity;
	
	@FXML
	private Label postCode;
	
	@FXML
	private Label country;

	/******************************************************************
	 * STRINGS THAT NEED TRANSLATION
	 ******************************************************************/
	/******************************************************************
	 * ACTION OBJECTS
	 ******************************************************************/
	@FXML
	private Label homeButton;
	
	@FXML
	private HBox homeWrapperStackPane;
	
	@FXML
	private Label receiptsButton;
	
	@FXML 
	private HBox receiptsWrapperStackPane;
	
	@FXML
	private Label costsButton;
	
	@FXML
	private HBox costsWrapperStackPane;
	
	@FXML
	private Label waterListButton;
	
	@FXML
	private HBox waterListWrapperStackPane;
	
	@FXML
	private Button changeHouseButton;
	
	@FXML
	private VBox houseInfoVBox;
	
	@FXML
	private VBox buttonContainerVBox;
	
	@FXML
	private Region firstFlowPage;
	
	@FXML
	private Region secondFlowPage;
	
	/******************************************************************
	 * PRIVATE VARIABLES
	 ******************************************************************/


	@FXML
	private void initialize() {
		translateStrings();
		firstName.setText(CurrentUser.getUser().getName());
		lastName.setText(CurrentUser.getUser().getSurname());
		refreshHouseInfo();
		initialLinkSetup();
		startMenuAnimation();
	}
	
	private void translateStrings(){
		homeButton.setText(Word.translate("side_menu_home"));
		receiptsButton.setText(Word.translate("side_menu_receipts"));
		costsButton.setText(Word.translate("side_menu_costs"));
		waterListButton.setText(Word.translate("side_menu_water"));
		changeHouseButton.setText(Word.translate("house_information"));
	}
	
	@FXML
	private void openUserSettings(){
		PaneNavigator.loadPane(PaneNavigator.clear, PaneNavigator.center);
		PaneNavigator.loadPane(PaneNavigator.userSettings, PaneNavigator.center);
	}
	
	@FXML
	private void openHomePage(){
		PaneNavigator.loadPane(PaneNavigator.clear, PaneNavigator.center);
		PaneNavigator.loadPane(PaneNavigator.home, PaneNavigator.center);
		selectPage(PaneNavigator.home);
	}
	
	@FXML
	private void openReceiptPage(){
		PaneNavigator.loadPane(PaneNavigator.clear, PaneNavigator.center);
		PaneNavigator.loadPane(PaneNavigator.receipts, PaneNavigator.center);
		selectPage(PaneNavigator.receipts);
	}

	@FXML
	private void openCostsPage(){
		PaneNavigator.loadPane(PaneNavigator.clear, PaneNavigator.center);
		PaneNavigator.loadPane(PaneNavigator.costs, PaneNavigator.center);
		selectPage(PaneNavigator.costs);
	}
	
	@FXML
	private void openWaterListPage(){
		PaneNavigator.loadPane(PaneNavigator.clear, PaneNavigator.center);
		PaneNavigator.loadPane(PaneNavigator.waterList, PaneNavigator.center);
		selectPage(PaneNavigator.waterList);
	}
	
	@FXML
	private void changeHouse(){
		PaneNavigator.loadPane(PaneNavigator.clear, PaneNavigator.center);
		PaneNavigator.loadPane(PaneNavigator.changeHouse, PaneNavigator.center);
	}
	
	public void refreshHouseInfo(){
		House house = CurrentUser.getHouse();
		if(house==null){
			houseInfoVBox.setVisible(false);
		}else{
			houseInfoVBox.setVisible(true);
			String houseStreet = house.getStreet()+" "+house.getHouseNumber()+", "+house.getCity();
			houseNameCity.setText(houseStreet);
			postCode.setText(house.getPostCode());
			country.setText(house.getCountry());
		}
	}
	
	public void selectPage(String page){
		System.out.println("Select page is called");
		buttonContainerVBox.getChildren().forEach(child->{
			child.getStyleClass().remove("selected");
		});
		
		if(page.equals(PaneNavigator.home)){
			homeWrapperStackPane.getStyleClass().add("selected");
		}else if(page.equals(PaneNavigator.receipts)){
			receiptsWrapperStackPane.getStyleClass().add("selected");
		}else if(page.equals(PaneNavigator.waterList)){
			waterListWrapperStackPane.getStyleClass().add("selected");
		}else if(page.equals(PaneNavigator.costs)){
			costsWrapperStackPane.getStyleClass().add("selected");
		}
		
		addLinkColours();
	}
	
	private void addLinkColours(){
		for(Node wrapper : buttonContainerVBox.getChildren()){
			if(!wrapper.getStyleClass().contains("selected")){
				linkAnimation(wrapper, "close");
			}else if(wrapper.getStyleClass().contains("selected")){
				linkAnimation(wrapper, "open");
			}
		}
	}
	
	private void initialLinkSetup(){
		final Rectangle clippingPath = new Rectangle(300, 500);
		buttonContainerVBox.setClip(clippingPath);
		for(Node wrapper : buttonContainerVBox.getChildren()){
			if(wrapper == homeWrapperStackPane){
				wrapper.getStyleClass().add("selected");
				Region rect = (Region) wrapper.lookup(".bottom-line");
				rect.setMinHeight(3);
			}
			wrapper.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
				linkAnimation(wrapper, "open");});
			wrapper.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
				if(!wrapper.getStyleClass().contains("selected")){
					linkAnimation(wrapper, "close");
				}});
		}
	}
	
	private void linkAnimation(Node node, String action){
		double height = 1;
		double duration = 0.1;
		Region rect = (Region) node.lookup(".bottom-line");
		
		switch (action) {
		case "open":
			height = 3;
			break;
		case "close":
			break;
		}
		ResizeHeightTranslation resize = new ResizeHeightTranslation(Duration.seconds(duration), rect, height);
		resize.play();
	}
	
	private void startMenuAnimation(){
		firstFlowPage.setTranslateY(8);
		secondFlowPage.setTranslateY(14);
		
		PagesTransition transition1 = new PagesTransition(Duration.seconds(1.4), firstFlowPage, 8, 274);
		PagesTransition transition2 = new PagesTransition(Duration.seconds(1.8), secondFlowPage, 14, 245);
		transition1.setDelay(Duration.seconds(0.8));
		transition2.setDelay(Duration.seconds(0.4));
		transition1.setInterpolator(Interpolator.EASE_OUT);
		transition2.setInterpolator(Interpolator.EASE_OUT);
		
		ParallelTransition parallelTransition = new ParallelTransition(transition1, transition2);
		parallelTransition.play();
	}
}


class ResizeHeightTranslation extends Transition {
    
    protected Region rectangle;
    protected double startHeight;
    protected double newHeight;
    protected double heightDiff;
    
    public ResizeHeightTranslation( Duration duration, Region region, double newHeight ) {
        setCycleDuration(duration);
        this.rectangle = region;
        this.newHeight = newHeight;
        this.startHeight = region.getHeight();
        this.heightDiff = newHeight - startHeight;
    }
    
    @Override
    protected void interpolate(double fraction) {
        rectangle.setMinHeight( startHeight + ( heightDiff * fraction ) );
    }
}

class PagesTransition extends Transition {
    
    protected Region rectangle;
    protected double startPoint;
    protected double endPoint;
    protected double diff;
    protected double initialWidth;
    protected double newWidth;
    protected double widthDiff;
    
    public PagesTransition( Duration duration, Region region, double startPoint, double endWidth) {
        setCycleDuration(duration);
        this.rectangle = region;
        this.startPoint = startPoint;
        this.endPoint = 0;
        this.diff = endPoint - startPoint;
        this.initialWidth = 300;
        this.newWidth = endWidth;
        this.widthDiff = endWidth - initialWidth;
    }
    
    @Override
    protected void interpolate(double fraction) {
        rectangle.setTranslateY(startPoint + (diff*fraction));
        rectangle.setPrefWidth(initialWidth + (widthDiff*fraction));
    }
}
