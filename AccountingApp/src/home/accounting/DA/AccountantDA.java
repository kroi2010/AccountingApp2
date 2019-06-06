package home.accounting.DA;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;

import home.accounting.MainApp;
import home.accounting.controller.LoginController;
import home.accounting.model.Accountant;
import home.accounting.model.Bank;
import home.accounting.model.Currency;
import home.accounting.model.House;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AccountantDA {

	private static MainApp mainApp;
	
	public static void setMainApp(MainApp app) {
		mainApp = app;
	}
	
	public static void save(Accountant accountant) {
		Session sess = mainApp.getSessionFactory().openSession();
		System.out.println("Session opened!");
		Transaction tx = null;
		 try {
		     tx = sess.beginTransaction();
		     sess.save(accountant);
		     tx.commit();
		 }
		 catch (Exception e) {
		     if (tx!=null) tx.rollback();
		     System.out.println(e);
		     throw e;
		 }
		 finally {
		     sess.close();
		     System.out.println("Session closed!");
		 }
	}
	
	public static void register(Accountant accountant){
		AccountantDA.save(accountant);
		LoginController.login(accountant);
	}
	
	public static ObservableList<Accountant> getAll(){
		Session session = mainApp.getSessionFactory().openSession();
		ObservableList<Accountant> users = null;
		Transaction tx = null;
		 try {
		     tx = session.beginTransaction();
		     
		     TypedQuery<Accountant> query = session.createQuery("FROM Accountant", Accountant.class);
		     users = FXCollections.observableArrayList(query.getResultList());
		    
		     tx.commit();
		 }
		 catch (Exception e) {
		     if (tx!=null) tx.rollback();
		     System.out.println(e);
		     throw e;
		 }
		 finally {
		     session.close();
		 }
		return users;
	}
}
