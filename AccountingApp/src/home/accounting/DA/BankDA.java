package home.accounting.DA;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.Transaction;

import home.accounting.MainApp;
import home.accounting.model.Bank;
import home.accounting.model.House;

public class BankDA {

	private static MainApp mainApp;
	
	public static void setMainApp(MainApp app) {
		mainApp = app;
	}
	
	public static void save(Bank bank) {
		Session sess = mainApp.getSessionFactory().openSession();
		Transaction tx = null;
		 try {
		     tx = sess.beginTransaction();
		     sess.save(bank);
		     tx.commit();
		 }
		 catch (Exception e) {
		     if (tx!=null) tx.rollback();
		     System.out.println(e);
		     throw e;
		 }
		 finally {
		     sess.close();
		 }
	}
	
	public static List<Bank> getAll(House house){
		Session sess = mainApp.getSessionFactory().openSession();
		Transaction tx = null;
		List<Bank> banks = new ArrayList<>();
		 try {
		     tx = sess.beginTransaction();
		     // create criteria builder
		     CriteriaBuilder builder = sess.getCriteriaBuilder();
		     // create criteria
		     CriteriaQuery<Bank> query = builder.createQuery(Bank.class);
		     // specify criteria root
		     Root<Bank> root = query.from(Bank.class);
		     query.select(root).where(builder.equal(root.get("house"), house));
		     banks = sess.createQuery(query).getResultList();
		     
		     tx.commit();
		 }
		 catch (Exception e) {
		     if (tx!=null) tx.rollback();
		     System.out.println("Couldn't find any banks for house "+house.getStreet()+" "+house.getHouseNumber());
		 }
		 finally {
		     sess.close();
		 }
		 return banks;
	}
	
	public static ArrayList<Bank> getAll(){
		Session session = mainApp.getSessionFactory().openSession();
		ArrayList<Bank> banks = null;
		Transaction tx = null;
		 try {
		     tx = session.beginTransaction();
		     
		     TypedQuery<Bank> query = session.createQuery("FROM Bank", Bank.class);
		     banks = (ArrayList<Bank>) query.getResultList();
		    
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
		return banks;
	}
}
