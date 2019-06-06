package home.accounting.DA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;

import home.accounting.MainApp;
import home.accounting.model.Receipt;

public class ReceiptsDA {
	private static MainApp mainApp;

	public static void setMainApp(MainApp app) {
		mainApp = app;
	}
	public static ArrayList<Receipt> getAll(){
		Session session = mainApp.getSessionFactory().openSession();
		ArrayList<Receipt> receipts = null;
		Transaction tx = null;
		 try {
		     tx = session.beginTransaction();
		     
		     TypedQuery<Receipt> query = session.createQuery("FROM Receipt", Receipt.class);
		     receipts = (ArrayList<Receipt>) query.getResultList();
		    
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
		return receipts;
	}
	
	public Map<String, String> getMapped(){
		Map<String, String> dataMap = new HashMap<>();
		return dataMap;
	}
}
