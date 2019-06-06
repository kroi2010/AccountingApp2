package home.accounting.DA;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.Transaction;

import home.accounting.MainApp;
import home.accounting.controller.CurrentUser;
import home.accounting.model.Accountant;
import home.accounting.model.Bank;
import home.accounting.model.Flat;
import home.accounting.model.House;

public class HouseDA {

	private static MainApp mainApp;
	
	public static void setMainApp(MainApp app) {
		mainApp = app;
	}
	
	public static void save(House house) {
		Session sess = mainApp.getSessionFactory().openSession();
		Transaction tx = null;
		 try {
		     tx = sess.beginTransaction();
		     sess.save(house);
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
		 
		 //create flats for house
		 int numberOfFlats = house.getFlats();
		 for (int i = 0; i < numberOfFlats; i++) {
			FlatDA.save(new Flat(house, i+1));
		}
	}
	
	public static void register(House house, ArrayList<Bank> banks){
		HouseDA.save(house);
		banks.forEach(bank -> {
			BankDA.save(bank);
		});
		HouseDA.setDefault(house);
	}
	
	public static void setDefault(House house){
		Session sess = mainApp.getSessionFactory().openSession();
		Transaction tx = null;
		ArrayList<House> houses = new ArrayList<>();
		 try {
			 tx = sess.beginTransaction();
			 
		     CriteriaBuilder builder = sess.getCriteriaBuilder();
		     
		     CriteriaQuery<House> query = builder.createQuery(House.class);
		     
		     Root<House> root = query.from(House.class);
		     query.select(root).where(builder.equal(root.get("accountant"), house.getAccountant()));
		     houses = (ArrayList<House>) sess.createQuery(query).getResultList();
		     
		     for(House singleHouse : houses){
		    	 singleHouse.setDefaultHouse(false);
		    	 if(singleHouse.getId() == house.getId()){
		    		 singleHouse.setDefaultHouse(true);
		    	 }
		    	 sess.save(singleHouse);
		     }
		     
		     tx.commit();
		     
		     CurrentUser.setHouse(house);
		 }
		 catch (Exception e) {
		     if (tx!=null) tx.rollback();
		     System.out.println("Couldn't set default house");
		     e.printStackTrace();
		 }
		 finally {
		     sess.close();
		 }
	}
	
	public static House getDefault(Accountant user){
		Session sess = mainApp.getSessionFactory().openSession();
		Transaction tx = null;
		List<House> defaultHouses = new ArrayList<>();
		House house = null;
		 try {
		     tx = sess.beginTransaction();
		     // create criteria builder
		     CriteriaBuilder builder = sess.getCriteriaBuilder();
		     // create criteria
		     CriteriaQuery<House> query = builder.createQuery(House.class);
		     // specify criteria root
		     Root<House> root = query.from(House.class);
		     query.select(root).where(builder.and(builder.equal(root.get("defaultHouse"), true),builder.equal(root.get("accountant"), user)));
		     defaultHouses = sess.createQuery(query).getResultList();
		     tx.commit();
		     
		     if(defaultHouses.size()>1){
		    	 System.out.println("There are "+defaultHouses.size()+" default houses");
		    	 boolean setNew = false;
		    	 for(House h : defaultHouses) {
		    		 if(setNew){
		    			 h.setDefaultHouse(false);
		    		 }else{
		    			 h.setDefaultHouse(true);
		    			 house = h;
		    			 setNew = true;
		    		 }
		    		 HouseDA.update(h);
		    	 }
		     }else if(defaultHouses.size()==1){
		    	 house = defaultHouses.get(0);
		     }
		 }
		 catch (Exception e) {
		     if (tx!=null) tx.rollback();
		     System.out.println("Couldn't find default house for accountant "+user.getName());
		 }
		 finally {
		     sess.close();
		 }
		 return house;
	}
	
	public static void update(House house){
		Session sess = mainApp.getSessionFactory().openSession();
		Transaction tx = null;
		 try {
		     tx = sess.beginTransaction();
		     sess.update(house);
		     tx.commit();
		 }
		 catch (Exception e) {
		     if (tx!=null) tx.rollback();
		     System.out.println(e);
		 }
		 finally {
		     sess.close();
		 }
	}
	
	public static List<House> getAll(Accountant user){
		Session sess = mainApp.getSessionFactory().openSession();
		Transaction tx = null;
		List<House> houses = new ArrayList<>();
		 try {
		     tx = sess.beginTransaction();
		     // create criteria builder
		     CriteriaBuilder builder = sess.getCriteriaBuilder();
		     // create criteria
		     CriteriaQuery<House> query = builder.createQuery(House.class);
		     // specify criteria root
		     Root<House> root = query.from(House.class);
		     query.select(root).where(builder.equal(root.get("accountant"), user));
		     houses = sess.createQuery(query).getResultList();
		     
		     tx.commit();
		 }
		 catch (Exception e) {
		     if (tx!=null) tx.rollback();
		     System.out.println("Couldn't find default house for accountant "+user.getName());
		 }
		 finally {
		     sess.close();
		 }
		 return houses;
	}
	
	public static void delete(House house){
		Session sess = mainApp.getSessionFactory().openSession();
		Transaction tx = null;
		 try {
		     tx = sess.beginTransaction();
		     sess.delete(house);
		     tx.commit();
		 }
		 catch (Exception e) {
		     if (tx!=null) tx.rollback();
		     System.out.println("Couldn't delete house with id "+house.getId());
		 }
		 finally {
		     sess.close();
		 }
	}
	
	private static House getFirst(Accountant user){
		Session sess = mainApp.getSessionFactory().openSession();
		Transaction tx = null;
		List<House> houses = new ArrayList<>();
		House house = null;
		 try {
		     tx = sess.beginTransaction();
		     // create criteria builder
		     CriteriaBuilder builder = sess.getCriteriaBuilder();
		     // create criteria
		     CriteriaQuery<House> query = builder.createQuery(House.class);
		     // specify criteria root
		     Root<House> root = query.from(House.class);
		     query.select(root).where(builder.equal(root.get("accountant"), user));
		     houses = sess.createQuery(query).getResultList();
		     
		     if(houses.size()>0){
		    	 house = houses.get(0);
		     }
		     
		     tx.commit();
		 }
		 catch (Exception e) {
		     if (tx!=null) tx.rollback();
		     System.out.println("Couldn't find default house for accountant "+user.getName());
		 }
		 finally {
		     sess.close();
		 }
		 return house;
	}
}
