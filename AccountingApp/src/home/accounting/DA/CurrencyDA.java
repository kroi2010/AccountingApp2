package home.accounting.DA;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;

import home.accounting.MainApp;
import home.accounting.model.Currency;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CurrencyDA {

	private static MainApp mainApp;

	public static void save(Currency currency) {
		Session session = mainApp.getSessionFactory().openSession();
		Transaction tx = null;
		 try {
		     tx = session.beginTransaction();
		     session.save(currency);
		     tx.commit();
		 }
		 catch (Exception e) {
		     if (tx!=null) tx.rollback();
		 }
		 finally {
		     session.close();
		 }
	}

	public static void setMainApp(MainApp app) {
		mainApp = app;
	}
	
	
	public static ObservableList<Currency> getAll(){
		Session session = mainApp.getSessionFactory().openSession();
		ObservableList<Currency> currencies = null;
		Transaction tx = null;
		 try {
		     tx = session.beginTransaction();
		     
		     TypedQuery<Currency> query = session.createQuery("FROM Currency", Currency.class);
		     currencies = FXCollections.observableArrayList(query.getResultList());
		    
		     tx.commit();
		 }
		 catch (Exception e) {
		     if (tx!=null) tx.rollback();
		     e.printStackTrace();
		 }
		 finally {
		     session.close();
		 }
		return currencies;
	}
}
