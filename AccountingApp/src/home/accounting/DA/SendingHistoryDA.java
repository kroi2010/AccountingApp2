package home.accounting.DA;

import java.util.ArrayList;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;

import home.accounting.MainApp;
import home.accounting.model.SendingHistory;

public class SendingHistoryDA {
	private static MainApp mainApp;

	public static void setMainApp(MainApp app) {
		mainApp = app;
	}
	
	public static ArrayList<SendingHistory> getAll(){
		Session session = mainApp.getSessionFactory().openSession();
		ArrayList<SendingHistory> history = null;
		Transaction tx = null;
		 try {
		     tx = session.beginTransaction();
		     
		     TypedQuery<SendingHistory> query = session.createQuery("FROM SendingHistory", SendingHistory.class);
		     history = (ArrayList<SendingHistory>) query.getResultList();
		    
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
		return history;
	}
}
