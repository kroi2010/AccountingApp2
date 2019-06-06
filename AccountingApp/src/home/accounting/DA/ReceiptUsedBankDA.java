package home.accounting.DA;

import java.util.ArrayList;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;

import home.accounting.MainApp;
import home.accounting.model.ReceiptUsedBank;

public class ReceiptUsedBankDA {
	private static MainApp mainApp;

	public static void setMainApp(MainApp app) {
		mainApp = app;
	}
	
	public static ArrayList<ReceiptUsedBank> getAll(){
		Session session = mainApp.getSessionFactory().openSession();
		ArrayList<ReceiptUsedBank> receiptsToBanks = null;
		Transaction tx = null;
		 try {
		     tx = session.beginTransaction();
		     
		     TypedQuery<ReceiptUsedBank> query = session.createQuery("FROM ReceiptUsedBank", ReceiptUsedBank.class);
		     receiptsToBanks = (ArrayList<ReceiptUsedBank>) query.getResultList();
		    
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
		return receiptsToBanks;
	}
}
