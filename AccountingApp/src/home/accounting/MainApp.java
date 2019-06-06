package home.accounting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import home.accounting.DA.AccountantDA;
import home.accounting.DA.LogInfoDA;
import home.accounting.DA.MailDA;
import home.accounting.DA.ReceiptsDA;
import home.accounting.DA.BankDA;
import home.accounting.DA.CostsDA;
import home.accounting.DA.CurrencyDA;
import home.accounting.DA.DBCheck;
import home.accounting.DA.FlatBankDA;
import home.accounting.DA.FlatDA;
import home.accounting.DA.HouseDA;
import home.accounting.DA.LanguagesDA;
import home.accounting.DA.SendingHistoryDA;
import home.accounting.DA.UsedBankDA;
import home.accounting.DA.WaterDA;
import home.accounting.DA.mySchemaComparison.DataTransfer;
import home.accounting.DA.mySchemaComparison.DatabaseConstants;
import home.accounting.DA.mySchemaComparison.SchemaCheck;
import home.accounting.controller.InterfaceController;
import home.accounting.controller.LoginController;
import home.accounting.controller.PaneNavigator;
import home.accounting.controller.PaneSwitcher;
import home.accounting.model.LogInfo;
import home.accounting.view.DialogOverlayController;
import home.accounting.view.MainSideMenuController;
import home.accounting.view.RegistrationFormController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApp extends Application {
	
	private Stage primaryStage;
	private BorderPane rootLayout;
	private SessionFactory sessionFactory;
	private Boolean sessionCreatedFlag = false;
	private Boolean endSession = false;

	@Override
	public void start(Stage primaryStage) {
		/******************************************************************SETTING CONNECTION WITH MAIN APP***********************************************************************/
		java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
		
		DBCheck.setMainApp(this);
		
		LogInfoDA.setMainApp(this);
		
		AccountantDA.setMainApp(this);
		BankDA.setMainApp(this);
		CostsDA.setMainApp(this);
		CurrencyDA.setMainApp(this);
		FlatBankDA.setMainApp(this);
		FlatDA.setMainApp(this);
		HouseDA.setMainApp(this);
		LanguagesDA.setMainApp(this);
		MailDA.setMainApp(this);
		ReceiptsDA.setMainApp(this);
		SendingHistoryDA.setMainApp(this);
		UsedBankDA.setMainApp(this);
		WaterDA.setMainApp(this);
		
		DataTransfer.setMainApp(this);
		
		PaneSwitcher paneSwitcher = new PaneSwitcher();
		paneSwitcher.setMainApp(this);
		PaneNavigator.setSwitcher(paneSwitcher);
		
		/******************************************************************SETTING CONNECTION WITH MAIN APP***********************************************************************/
		
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Little Accountant");
		
		buildSessionFactory();
		DBCheck.start();
        
		initRootLayout();
		firstRedirect();
		
		//checkForTermination();
	}
	
	 /**
     * Initialises the root layout.
     */
    public void initRootLayout() {
        try {
        	
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/MainContainer.fxml"));
            rootLayout = (BorderPane) loader.load();
            

            // Show the scene containing the root layout.
            if(endSession){
   		        // Load root layout from fxml file.
   		        FXMLLoader loader2 = new FXMLLoader();
   		        loader2.setLocation(MainApp.class.getResource("view/DialogOverlay.fxml"));
   		        AnchorPane newRootLayout = (AnchorPane) loader2.load();
   		        DialogOverlayController dialogController = loader2.<DialogOverlayController>getController();
   		        dialogController.setMainApp(this);
   		        StackPane pane = (StackPane) newRootLayout.getChildren().get(0);
   		        pane.getChildren().add(0, rootLayout);
   		        

   		        // Show the scene containing the root layout.
   		        Scene scene = new Scene(newRootLayout);
   		        primaryStage.setScene(scene);
   		        primaryStage.show();
            }else{
            	Scene scene = new Scene(rootLayout);
                primaryStage.setScene(scene);
                primaryStage.show();
            }
            //Scene scene = new Scene(rootLayout);
            //primaryStage.setScene(scene);
            //primaryStage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	private void firstRedirect(){
		LogInfo logInfo = LogInfoDA.get();
		if(logInfo!=null && logInfo.getUser()!=null){
			LoginController.login(logInfo.getUser());
			PaneNavigator.loadPane(PaneNavigator.sideMenu, PaneNavigator.left);
			PaneNavigator.loadPane(PaneNavigator.home, PaneNavigator.center);
			
		}else{
			InterfaceController.setDefaultLanguage();
			PaneNavigator.loadPane(PaneNavigator.registration, PaneNavigator.center);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public void buildSessionFactory(){
		if(!sessionCreatedFlag){
			final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
			        .configure("hibernate.cfg.xml") // configures settings from hibernate.cfg.xml
			        .build();
			try {
			    sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory(); 
			    sessionCreatedFlag = true;
			}
			catch (Exception e) {
			    // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
			    // so destroy it manually.
				System.out.println("Couldn't build sessionfactory: "+e);
			    StandardServiceRegistryBuilder.destroy( registry );
			    e.printStackTrace();
			}
		}
	}
	
	public SessionFactory getSessionFactory(){
    	return sessionFactory;
    }
	
	public void closeSessionFactory(){
		sessionFactory.close();
		sessionCreatedFlag = false;
	}
	
	public BorderPane getRootLayout(){
 		return rootLayout;
 	}
	
	/**
	 * if data error occurred on startup
	 */
	public void endSession(){
		endSession = true;
	}
	
	public void closeApp(){
		primaryStage.close();
	}
	
	@Override
	public void stop() throws Exception {
	    sessionFactory.close();
	    System.out.println("closing...");
	}
}
