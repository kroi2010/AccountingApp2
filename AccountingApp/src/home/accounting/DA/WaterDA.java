package home.accounting.DA;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
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
import home.accounting.model.Water;

public class WaterDA {
	private static MainApp mainApp;

	public static void setMainApp(MainApp app) {
		mainApp = app;
	}
	
	public static void save(Water water){
		Session sess = mainApp.getSessionFactory().openSession();
		Transaction tx = null;
		 try {
		     tx = sess.beginTransaction();
		     sess.save(water);
		     tx.commit();
		 }
		 catch (Exception e) {
		     if (tx!=null) tx.rollback();
		     System.out.println(e);
		     e.printStackTrace();
		 }
		 finally {
		     sess.close();
		 }
	}
	
	public static void update(Water water){
		Session sess = mainApp.getSessionFactory().openSession();
		Transaction tx = null;
		 try {
		     tx = sess.beginTransaction();
		     sess.update(water);
		     tx.commit();
		 }
		 catch (Exception e) {
		     if (tx!=null) tx.rollback();
		     System.out.println(e);
		     e.printStackTrace();
		 }
		 finally {
		     sess.close();
		 }
	}
	
	public static Water get(Flat flat, Date date){
		Water water = null;
		
		Calendar myCalendar = Calendar.getInstance();
		myCalendar.setTime(date);
		myCalendar.set(Calendar.DAY_OF_MONTH, 1);
		Date monthStart = new java.sql.Date(myCalendar.getTimeInMillis());
		myCalendar.add(Calendar.DAY_OF_MONTH,
				(myCalendar.getMaximum(Calendar.DAY_OF_MONTH) - myCalendar.get(Calendar.DAY_OF_MONTH)));
		Date monthEnd = new java.sql.Date(myCalendar.getTimeInMillis());
		
		Session sess = mainApp.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = sess.beginTransaction();

			// create criteria builder
			CriteriaBuilder builder = sess.getCriteriaBuilder();
			// create criteria
			CriteriaQuery<Water> query = builder.createQuery(Water.class);
			// specify criteria root
			Root<Water> root = query.from(Water.class);
			
			query.select(root)
					.where(builder.and(builder.equal(root.get("flat"), flat),
							builder.greaterThanOrEqualTo(root.get("date"), monthStart),
							builder.lessThanOrEqualTo(root.get("date"), monthEnd)));
			List<Water> waterList = sess.createQuery(query).getResultList();
			//System.out.println("Number of records of water for flat "+flat.getNumber()+" is "+waterList.size());
			if(waterList.size()>0){
				water = waterList.get(0);
			}

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			sess.close();
		}
		
		return water;
	}
	
	public static List<Water> getAll(Date date, House house){
		List<Water> waterList = null;
		Calendar myCalendar = Calendar.getInstance();
		myCalendar.setTime(date);
		myCalendar.set(Calendar.DAY_OF_MONTH, 1);
		Date monthStart = new java.sql.Date(myCalendar.getTimeInMillis());
		myCalendar.add(Calendar.DAY_OF_MONTH,
				(myCalendar.getMaximum(Calendar.DAY_OF_MONTH) - myCalendar.get(Calendar.DAY_OF_MONTH)));
		Date monthEnd = new java.sql.Date(myCalendar.getTimeInMillis());
		
		Session sess = mainApp.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = sess.beginTransaction();

			// create criteria builder
			CriteriaBuilder builder = sess.getCriteriaBuilder();
			// create criteria
			CriteriaQuery<Water> query = builder.createQuery(Water.class);
			// specify criteria root
			Root<Water> root = query.from(Water.class);
			
			query.select(root)
					.where(builder.and(builder.equal(root.get("flat").get("house"), house),
							builder.greaterThanOrEqualTo(root.get("date"), monthStart),
							builder.lessThanOrEqualTo(root.get("date"), monthEnd)));
			waterList = sess.createQuery(query).getResultList();

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			sess.close();
		}
		
		return waterList;
	}
	
	public static ArrayList<Water> getAll(){
		Session session = mainApp.getSessionFactory().openSession();
		ArrayList<Water> water = null;
		Transaction tx = null;
		 try {
		     tx = session.beginTransaction();
		     
		     TypedQuery<Water> query = session.createQuery("FROM Water", Water.class);
		     water = (ArrayList<Water>) query.getResultList();
		    
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
		return water;
	}
}
