package home.accounting.DA;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;

import home.accounting.MainApp;
import home.accounting.model.Languages;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LanguagesDA {

	private static MainApp mainApp;

	public static void save(Languages language) {
		Session sess = mainApp.getSessionFactory().openSession();
		Transaction tx = null;
		 try {
		     tx = sess.beginTransaction();
		     sess.save(language);
		     tx.commit();
		 }
		 catch (Exception e) {
		     if (tx!=null) tx.rollback();
		 }
		 finally {
		     sess.close();
		 }
	}
	
	public static ObservableList<Languages> getAll(){
		Session session = mainApp.getSessionFactory().openSession();
		ObservableList<Languages> languages = null;
		Transaction tx = null;
		 try {
		     tx = session.beginTransaction();
		     
		     TypedQuery<Languages> query = session.createQuery("FROM Languages", Languages.class);
		     languages = FXCollections.observableArrayList(query.getResultList());
		     
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
		return languages;
	}
	
	/*public static Languages get(String code){
		
	}*/

	public static void setMainApp(MainApp app) {
		mainApp = app;
	}
}
