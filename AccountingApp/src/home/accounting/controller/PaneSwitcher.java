package home.accounting.controller;

import home.accounting.MainApp;
import home.accounting.view.ErrorPageController;
import home.accounting.view.MainSideMenuController;
import home.accounting.view.ReceiptPageController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class PaneSwitcher {
	private MainApp mainApp;

	public PaneSwitcher() {
	}

	public void redirect(String newPage, String position) {
		try{
			switch (position) {
				case "top":{
					BorderPane rootLayout=mainApp.getRootLayout();
					Node node=rootLayout.getTop();
					rootLayout.getChildren().remove(node);
					
					if(newPage.equals(PaneNavigator.pathBase + PaneNavigator.clear)){
				 		node=null;
				 		rootLayout.setTop(node);
					}else{
						FXMLLoader loader = new FXMLLoader(); //Loads an object hierarchy from an XML document.
				        loader.setLocation(MainApp.class.getResource(newPage)); // which document
				        AnchorPane pane = (AnchorPane) loader.load();
				        rootLayout.setTop(pane);
					}
					break;
				}
				case "bottom":{
					BorderPane rootLayout=mainApp.getRootLayout();
					Node node=rootLayout.getBottom();
					rootLayout.getChildren().remove(node);
					
					if(newPage.equals(PaneNavigator.pathBase + PaneNavigator.clear)){
				 		node=null;
				 		rootLayout.setBottom(node);
					}else{
						FXMLLoader loader = new FXMLLoader(); //Loads an object hierarchy from an XML document.
				        loader.setLocation(MainApp.class.getResource(newPage)); // which document
				        AnchorPane pane = (AnchorPane) loader.load();
				        rootLayout.setBottom(pane);
					}
					break;
				}
				case "left":{
					BorderPane rootLayout=mainApp.getRootLayout();
					Node node=rootLayout.getLeft();
					rootLayout.getChildren().remove(node);
					
					if(newPage.equals(PaneNavigator.pathBase + PaneNavigator.clear)){
				 		node=null;
				 		rootLayout.setLeft(node);
					}else{
						FXMLLoader loader = new FXMLLoader(); //Loads an object hierarchy from an XML document.
				        loader.setLocation(MainApp.class.getResource(newPage)); // which document
				        AnchorPane pane = (AnchorPane) loader.load();
				        rootLayout.setLeft(pane);
				        if(newPage.equals(PaneNavigator.pathBase + PaneNavigator.sideMenu)){
				        	 MainSideMenuController controller = loader.<MainSideMenuController>getController();
				        	 ControllerConnector.setSideMenuController(controller);
				        }
				  
					}
					break;
				}
				case "right":{
					BorderPane rootLayout=mainApp.getRootLayout();
					Node node=rootLayout.getRight();
					rootLayout.getChildren().remove(node);
					
					if(newPage.equals(PaneNavigator.pathBase + PaneNavigator.clear)){
				 		node=null;
				 		rootLayout.setRight(node);
					}else{
						FXMLLoader loader = new FXMLLoader(); //Loads an object hierarchy from an XML document.
				        loader.setLocation(MainApp.class.getResource(newPage)); // which document
				        AnchorPane pane = (AnchorPane) loader.load();
				        rootLayout.setRight(pane);
					}
					break;
				}
				case "center":{
					BorderPane rootLayout=mainApp.getRootLayout();
					Node node=rootLayout.getCenter();
					rootLayout.getChildren().remove(node);
					
					if(newPage.equals(PaneNavigator.pathBase + PaneNavigator.clear)){
				 		node=null;
				 		rootLayout.setCenter(node);
					}else{
						FXMLLoader loader = new FXMLLoader(); //Loads an object hierarchy from an XML document.
				        loader.setLocation(MainApp.class.getResource(newPage)); // which document
				        AnchorPane pane = (AnchorPane) loader.load();
				        rootLayout.setCenter(pane);
				        
				        if(newPage.equals(PaneNavigator.pathBase+PaneNavigator.registration)){
				        	ControllerConnector.setSideMenuController(null);
				        }else if(newPage.equals(PaneNavigator.pathBase + PaneNavigator.error)){
				        	 ErrorPageController controller = loader.<ErrorPageController>getController();
				        	 ControllerConnector.setErrorPageController(controller);
				        }else if(newPage.equals(PaneNavigator.pathBase + PaneNavigator.receipts)){
				        	 ReceiptPageController controller = loader.<ReceiptPageController>getController();
				        	 controller.checkForRedirect();
				        }
				        
				        if(ControllerConnector.getSideMenuController()!=null){
				        	 //ControllerConnector.getSideMenuController().selectPage(newPage.replace(PaneNavigator.pathBase, ""));
				        }
					}
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	public MainApp getMainApp() {
		return mainApp;
	}
}
