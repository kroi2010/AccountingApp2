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
import home.accounting.model.Flat;
import home.accounting.model.House;
import home.accounting.model.Mail;

public class MailDA {
	private static MainApp mainApp;

	public static void setMainApp(MainApp app) {
		mainApp = app;
	}
	
	public static void save(Mail mail){
		Session session = mainApp.getSessionFactory().openSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.save(mail);
			tx.commit();
		}catch(Exception e){
			System.out.println("Couldn't save mail!");
			e.printStackTrace();
			if(tx != null){
				tx.rollback();
			}
		}finally {
			session.close();
		}
	}
	
	public static void update(Mail mail){
		Session session = mainApp.getSessionFactory().openSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.update(mail);
			tx.commit();
		}catch(Exception e){
			System.out.println("Couldn't update mail!");
			e.printStackTrace();
			if(tx != null){
				tx.rollback();
			}
		}finally {
			session.close();
		}
	}
	
	public static void delete(Mail mail){
		Session session = mainApp.getSessionFactory().openSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.delete(mail);
			tx.commit();
		}catch(Exception e){
			System.out.println("Couldn't delete mail!");
			e.printStackTrace();
			if(tx != null){
				tx.rollback();
			}
		}finally {
			session.close();
		}
	}
	
	public static List<Mail> getAll(Flat flat) {
		List<Mail> mailList = null;

		Session sess = mainApp.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = sess.beginTransaction();

			CriteriaBuilder builder = sess.getCriteriaBuilder();
			CriteriaQuery<Mail> query = builder.createQuery(Mail.class);
			Root<Mail> root = query.from(Mail.class);
			query.orderBy(builder.asc(root.get("active")));	//first active mails
			query.select(root).where(builder.equal(root.get("flat"), flat));
			mailList = sess.createQuery(query).getResultList();

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			System.out.println(e);
		} finally {
			sess.close();
		}

		return mailList;
	}
	
	public static ArrayList<Mail> getAll(){
		Session session = mainApp.getSessionFactory().openSession();
		ArrayList<Mail> mails = null;
		Transaction tx = null;
		 try {
		     tx = session.beginTransaction();
		     
		     TypedQuery<Mail> query = session.createQuery("FROM Mail", Mail.class);
		     mails = (ArrayList<Mail>) query.getResultList();
		    
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
		return mails;
	}
}
