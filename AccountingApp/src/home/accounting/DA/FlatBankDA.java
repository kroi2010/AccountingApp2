package home.accounting.DA;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.Transaction;

import home.accounting.MainApp;
import home.accounting.model.Bank;
import home.accounting.model.Flat;
import home.accounting.model.FlatBank;

public class FlatBankDA {

	private static MainApp mainApp;

	public static void setMainApp(MainApp app) {
		mainApp = app;
	}

	public static ArrayList<FlatBank> getAll() {
		Session session = mainApp.getSessionFactory().openSession();
		ArrayList<FlatBank> flatBanks = null;
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			TypedQuery<FlatBank> query = session.createQuery("FROM FlatBank", FlatBank.class);
			flatBanks = (ArrayList<FlatBank>) query.getResultList();

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			System.out.println(e);
			throw e;
		} finally {
			session.close();
		}
		return flatBanks;
	}

	public static void save(Flat flat, Bank bank) {
		Session sess = mainApp.getSessionFactory().openSession();
		Transaction tx = null;
		FlatBank flatBank = new FlatBank(flat, bank);
		try {
			tx = sess.beginTransaction();
			sess.save(flatBank);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			sess.close();
		}
	}

	public static void delete(Flat flat, Bank bank) {
		Session sess = mainApp.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = sess.beginTransaction();

			CriteriaBuilder builder = sess.getCriteriaBuilder();

			CriteriaDelete<FlatBank> delete = builder.createCriteriaDelete(FlatBank.class);
			Root<FlatBank> root = delete.from(FlatBank.class);
			delete.where(builder.and(builder.equal(root.get("bank"), bank), builder.equal(root.get("flat"), flat)));
			sess.createQuery(delete).executeUpdate();

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			System.out.println("Couldn't delete bank from flat " + flat.getNumber());
		} finally {
			sess.close();
		}
	}

	public static Boolean check(Flat flat, Bank bank) {
		boolean exists = false;

		Session sess = mainApp.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = sess.beginTransaction();

			CriteriaBuilder builder = sess.getCriteriaBuilder();
			CriteriaQuery<FlatBank> query = builder.createQuery(FlatBank.class);
			Root<FlatBank> rootQuery = query.from(FlatBank.class);
			query.select(rootQuery).where(builder.and(builder.equal(rootQuery.get("flat"), flat),
					builder.equal(rootQuery.get("bank"), bank)));
			List<FlatBank> connection = sess.createQuery(query).getResultList();
			if (connection.size() > 0) {
				exists = true;
			}

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			sess.close();
		}

		return exists;
	}

	public static List<FlatBank> getAll(Flat flat) {
		List<FlatBank> flatBanks = null;

		Session session = mainApp.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			// create criteria builder
			CriteriaBuilder builder = session.getCriteriaBuilder();
			// create criteria
			CriteriaQuery<FlatBank> query = builder.createQuery(FlatBank.class);
			// specify criteria root
			Root<FlatBank> root = query.from(FlatBank.class);
			query.select(root).where(builder.equal(root.get("flat"), flat));
			flatBanks = session.createQuery(query).getResultList();

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			System.out.println(e);
			throw e;
		} finally {
			session.close();
		}

		return flatBanks;
	}
}
