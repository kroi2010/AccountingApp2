package home.accounting.DA;

import java.util.ArrayList;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;

import home.accounting.MainApp;
import home.accounting.model.UsedBank;

public class UsedBankDA {
	private static MainApp mainApp;

	public static void setMainApp(MainApp app) {
		mainApp = app;
	}
	
	public static ArrayList<UsedBank> getAll(){
		Session session = mainApp.getSessionFactory().openSession();
		ArrayList<UsedBank> usedBanks = null;
		Transaction tx = null;
		 try {
		     tx = session.beginTransaction();
		     
		     TypedQuery<UsedBank> query = session.createQuery("FROM UsedBank", UsedBank.class);
		     usedBanks = (ArrayList<UsedBank>) query.getResultList();
		    
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
		return usedBanks;
	}
}
