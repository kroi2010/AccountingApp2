package home.accounting.DA;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.Transaction;

import home.accounting.MainApp;
import home.accounting.model.Accountant;
import home.accounting.model.LogInfo;

public class LogInfoDA {

	private static MainApp mainApp;

	public static void setMainApp(MainApp app) {
		mainApp = app;
	}

	/**
	 * Saves info of user that was logged in last
	 * @param newUser
	 */
	public static void add(Accountant newUser) {
		LogInfo lastLogged = LogInfoDA.get();
		
		if(lastLogged==null){
			LogInfoDA.createLog(new LogInfo(newUser));
		}else{
			Accountant lastUser = lastLogged.getUser();
			if(newUser!=lastUser){
				//update
				Session sess = mainApp.getSessionFactory().openSession();
				Transaction tx = null;
				try {
					tx = sess.beginTransaction();
					lastLogged.setUser(newUser);
					sess.update(lastLogged);
					tx.commit();
				} catch (Exception e) {
					if (tx != null)
						tx.rollback();
					System.out.println(e);
					throw e;
				} finally {
					sess.close();
				}
			}	
		}
		
	}
	
	/**
	 * Creates first log note
	 * @param newUser
	 */
	private static void createLog(LogInfo newUser){
		Session sess = mainApp.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = sess.beginTransaction();
			sess.save(newUser);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			System.out.println(e);
			throw e;
		} finally {
			sess.close();
		}
	}

	/**
	 * Returns info on last logged user
	 * @return
	 */
	public static LogInfo get() {
		Session session = mainApp.getSessionFactory().openSession();
		List<LogInfo> fullInfo = new ArrayList<LogInfo>();
		LogInfo info = null;
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<LogInfo> query = builder.createQuery(LogInfo.class);
			Root<LogInfo> root = query.from(LogInfo.class);
			query.select(root);
			fullInfo = session.createQuery(query).list();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			System.out.println(e);
			throw e;
		} finally {
			session.close();
		}
		
		if (fullInfo.size() > 1) {
			clear();
			System.out.println("NB>>> MORE THAN ONE RECORDING WAS FOUND IN LOGS, CLEARING ALL DATA!");
		} else if (fullInfo.size() == 1) {
			info = fullInfo.get(0);
		}
		
		return info;
	}

	/**
	 * Used only to prevent possibility of having multiple lines in log info
	 */
	private static void clear() {
		Session sess = mainApp.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = sess.beginTransaction();
			String deleteAll = "DELETE * FROM log_info";
			sess.createNativeQuery(deleteAll).executeUpdate();
			tx.commit();
			System.out.println("Cleared log history.");
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			System.out.println(e);
			throw e;
		} finally {
			sess.close();
		}
	}
	
	/**
	 * Removes log of last user if user logs out manually  
	 * @param currentUser
	 */
	public static void remove(Accountant currentUser){
		Session sess = mainApp.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = sess.beginTransaction();
			
			CriteriaBuilder builder = sess.getCriteriaBuilder();
			CriteriaQuery<LogInfo> query = builder.createQuery(LogInfo.class);
			Root<LogInfo> root = query.from(LogInfo.class);
			query.select(root).where(builder.equal(root.get("user"), currentUser));
			LogInfo logInfo = sess.createQuery(query).getSingleResult();
			logInfo.setUser(null);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			System.out.println(e);
			throw e;
		} finally {
			sess.close();
		}
	}
}
