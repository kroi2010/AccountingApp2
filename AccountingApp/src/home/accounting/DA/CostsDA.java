package home.accounting.DA;

import java.sql.Date;
import java.time.LocalDate;
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
import home.accounting.model.Costs;
import home.accounting.model.Costs;
import home.accounting.model.House;

public class CostsDA {

	private static MainApp mainApp;

	public static void setMainApp(MainApp app) {
		mainApp = app;
	}

	public static void update(Costs newCosts) {
		Costs costs = getCosts(newCosts.getDate(), newCosts.getHouse());

		Session sess = mainApp.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = sess.beginTransaction();
			
			if (costs == null) {
				sess.save(newCosts);
			} else {
				costs.setElectricity(newCosts.getElectricity());
				costs.setHeating(newCosts.getHeating());
				costs.setGarbage(newCosts.getGarbage());
				costs.setMaintenance(newCosts.getMaintenance());
				costs.setSubscFees(newCosts.getSubscFees());
				costs.setWaterSewer(newCosts.getWaterSewer());
				sess.update(costs);
			}
			
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			sess.close();
		}

	}

	public static Costs getCosts(Date lookUpDate, House house) {
		Costs costs = null;
		
		Calendar myCalendar = Calendar.getInstance();
		myCalendar.setTime(lookUpDate);
		myCalendar.set(Calendar.DAY_OF_MONTH, 1);
		Date monthStart = new java.sql.Date(myCalendar.getTimeInMillis());
		myCalendar.add(Calendar.DAY_OF_MONTH,
				(myCalendar.getMaximum(Calendar.DAY_OF_MONTH) - myCalendar.get(Calendar.DAY_OF_MONTH)));
		Date monthEnd = new java.sql.Date(myCalendar.getTimeInMillis());
		
		System.out.println("Start: " + monthStart);
		System.out.println("End: " + monthEnd);
		Session sess = mainApp.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = sess.beginTransaction();

			// create criteria builder
			CriteriaBuilder builder = sess.getCriteriaBuilder();
			// create criteria
			CriteriaQuery<Costs> query = builder.createQuery(Costs.class);
			// specify criteria root
			Root<Costs> root = query.from(Costs.class);
			query.select(root)
					.where(builder.and(builder.equal(root.get("house"), house),
							builder.greaterThanOrEqualTo(root.get("date"), monthStart),
							builder.lessThanOrEqualTo(root.get("date"), monthEnd)));
			List<Costs> costsList = sess.createQuery(query).getResultList();
			if(!costsList.isEmpty()){
				costs = costsList.get(0);
			}

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			System.out.println(e);
			throw e;
		} finally {
			sess.close();
		}
		return costs;
	}
	
	public static ArrayList<Costs> getAll(){
		Session session = mainApp.getSessionFactory().openSession();
		ArrayList<Costs> costs = null;
		Transaction tx = null;
		 try {
		     tx = session.beginTransaction();
		     
		     TypedQuery<Costs> query = session.createQuery("FROM Costs", Costs.class);
		     costs = (ArrayList<Costs>) query.getResultList();
		    
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
		return costs;
	}
}
