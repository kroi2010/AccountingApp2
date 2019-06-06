package home.accounting.DA;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.Transaction;

import home.accounting.MainApp;
import home.accounting.model.Flat;
import home.accounting.model.House;

public class FlatDA {

	private static MainApp mainApp;

	public static void setMainApp(MainApp app) {
		mainApp = app;
	}
	
	public static void save(Flat flat){
		Session session = mainApp.getSessionFactory().openSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.save(flat);
			tx.commit();
		}catch(Exception e){
			System.out.println("Couldn't save flat!");
			e.printStackTrace();
			if(tx != null){
				tx.rollback();
			}
		}finally {
			session.close();
		}
	}
	
	public static void update(Flat flat){
		Session session = mainApp.getSessionFactory().openSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.update(flat);
			tx.commit();
		}catch(Exception e){
			System.out.println("Couldn't update flat!");
			e.printStackTrace();
			if(tx != null){
				tx.rollback();
			}
		}finally {
			session.close();
		}
	}

	public static List<Flat> getAll(House house) {
		List<Flat> flatList = null;

		Session sess = mainApp.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = sess.beginTransaction();

			CriteriaBuilder builder = sess.getCriteriaBuilder();
			CriteriaQuery<Flat> query = builder.createQuery(Flat.class);
			Root<Flat> root = query.from(Flat.class);
			query.select(root).where(builder.equal(root.get("house"), house));
			flatList = sess.createQuery(query).getResultList();

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			System.out.println(e);
		} finally {
			sess.close();
		}

		return flatList;
	}
	
	public static ArrayList<Flat> getAll(){
		Session session = mainApp.getSessionFactory().openSession();
		ArrayList<Flat> flats = null;
		List<Object[]> fromDB;
		Transaction tx = null;
		 try {
		     tx = session.beginTransaction();
		     fromDB = (ArrayList<Object[]>) session.createNativeQuery("SELECT * FROM flat").getResultList();
		     for(Object[]o:fromDB){
		    	 System.out.println("***"+o[0]+o[1]);
		     }
		     tx.commit();
		 }
		 catch (Exception e) {
		     if (tx!=null) tx.rollback();
		     e.printStackTrace();
		 }
		 finally {
		     session.close();
		 }
		return flats;
	}
}
