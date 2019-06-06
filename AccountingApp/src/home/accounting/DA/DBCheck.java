package home.accounting.DA;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.management.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import home.accounting.MainApp;
import home.accounting.DA.mySchemaComparison.DataMapping;
import home.accounting.DA.mySchemaComparison.DataTransfer;
import home.accounting.DA.mySchemaComparison.DatabaseConstants;
import home.accounting.DA.mySchemaComparison.SchemaCheck;
import home.accounting.DA.mySchemaComparison.models.Schema;
import home.accounting.model.Currency;
import home.accounting.model.Languages;

public class DBCheck {

	private static List<String> sqlQueries = new ArrayList<>();
	private static MainApp mainApp;

	private static String[] messages = new String[] { "Opened database successfully", "Database does not exist!",
			"Created database successfully", "Database was corrupted!" };

	public static void start() {

		DBCheck.createQueries();
		Schema workingDB = new Schema(DatabaseConstants.getMainpath());
		SchemaCheck check = new SchemaCheck();
		check.fillSchema(workingDB);
		
		if(workingDB.getTables().size()>0){	// if DB exists
			
			boolean structureUpdated = createStructure();
			
			if(structureUpdated){
				//Examples of mapping:
				//"table.column", new_table_name.new_column_name"
				//DataMapping.setMapping("accountant", "user");
				//DataMapping.setMapping("flat.empty", "completely_empty");
				check.run(workingDB);
			}
		}else{	// if not, check for backup
			if(dbExists(DatabaseConstants.getBasepath()+DatabaseConstants.getBackuppath()+DatabaseConstants.getDbname())){	// if backup exists
				
			}else{	// if not, create DB
				System.out.println("First time db setup");
				DBCheck.createTables(mainApp.getSessionFactory());
				DBCheck.fill();
			}
		}
		
	}
	
	private static Boolean dbExists(String path){
		File dbFile = new File(path);
		if (dbFile.exists()) {
			return true;
		} else {
			return false;
		}
	}

	private static void createQueries() {
		String languages = "CREATE TABLE IF NOT EXISTS \"languages\"("
				+ "language_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + "language_name VARCHAR(50) NOT NULL,"
				+ "language_code VARCHAR(3) UNIQUE NOT NULL);";

		String accountant = "CREATE TABLE IF NOT EXISTS \"accountant\"("
				+ "accountant_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + "name VARCHAR(50) NOT NULL,"
				+ "surname VARCHAR(50) NOT NULL," + "mail VARCHAR(100) NOT NULL," + "avatar VARCHAR(150),"
				+ "phone VARCHAR(16)," + "interface_lang_id INTEGER NOT NULL," + "password VARCHAR(15),"
				+ "FOREIGN KEY(interface_lang_id) REFERENCES languages(language_id) ON DELETE RESTRICT);";

		String currency = "CREATE TABLE IF NOT EXISTS \"currency\"("
				+ "currency_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + "name VARCHAR(25) UNIQUE NOT NULL,"
				+ "symbol VARCHAR(5) NOT NULL," + "prior BOOLEAN NOT NULL);";

		String house = "CREATE TABLE IF NOT EXISTS \"house\"(" + "house_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
				+ "accountant_id INTEGER NOT NULL," + "country VARCHAR(50) NOT NULL," + "city VARCHAR(50) NOT NULL,"
				+ "street VARCHAR(50) NOT NULL," + "post_code VARCHAR(10) NOT NULL,"
				+ "house_number VARCHAR(10) NOT NULL," + "flats SMALLINT NOT NULL," + "currency_id INTEGER NOT NULL,"
				+ "surname_first BOOLEAN NOT NULL," + "cut_name BOOLEAN NOT NULL,"
				+ "documentation_lang_id INTEGER NOT NULL," + "picture VARCHAR(150),"
				+ "default_house BOOLEAN NOT NULL,"
				+ "FOREIGN KEY(accountant_id) REFERENCES accountant(accountant_id) ON DELETE RESTRICT,"
				+ "FOREIGN KEY(currency_id) REFERENCES currency(currency_id) ON DELETE RESTRICT,"
				+ "FOREIGN KEY(documentation_lang_id) REFERENCES languages(language_id) ON DELETE RESTRICT);";

		String bank = "CREATE TABLE IF NOT EXISTS \"bank\"(" + "bank_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
				+ "house_id INTEGER NOT NULL," + "bank_name VARCHAR(150) NOT NULL," + "account VARCHAR(50) NOT NULL,"
				+ "FOREIGN KEY(house_id) REFERENCES house(house_id) ON DELETE CASCADE);";

		String flat = "CREATE TABLE IF NOT EXISTS \"flat\"(" + "flat_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
				+ "house_id INTEGER NOT NULL," + "number INTEGER NOT NULL," + "owner_name VARCHAR(50),"
				+ "owner_surname VARCHAR(50)," + "area_size INTEGER," + "people_living TINYINT,"
				+ "need_mail BOOLEAN NOT NULL,"
				+ "FOREIGN KEY(house_id) REFERENCES house(house_id) ON DELETE CASCADE);";

		String flatBank = "CREATE TABLE IF NOT EXISTS \"flat_bank\"(" + "flat_id INTEGER NOT NULL,"
				+ "bank_id INTEGER NOT NULL," + "PRIMARY KEY(flat_id, bank_id),"
				+ "FOREIGN KEY(flat_id) REFERENCES flat(flat_id) ON DELETE CASCADE,"
				+ "FOREIGN KEY(bank_id) REFERENCES bank(bank_id) ON DELETE CASCADE);";

		String water = "CREATE TABLE IF NOT EXISTS \"water\"(" + "water_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
				+ "flat_id INTEGER NOT NULL," + "date DATE NOT NULL," + "amount INTEGER,"
				+ "FOREIGN KEY(flat_id) REFERENCES flat(flat_id) ON DELETE CASCADE);";

		String mail = "CREATE TABLE IF NOT EXISTS \"mail\"(" + "mail_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
				+ "flat_id INTEGER NOT NULL," + "mail VARCHAR(100) NOT NULL," + "active BOOLEAN NOT NULL,"
				+ "default_message BOOLEAN," + "message VARCHAR(10000)," + "language_id INTEGER,"
				+ "FOREIGN KEY(language_id) REFERENCES languages(language_id) ON DELETE RESTRICT,"
				+ "FOREIGN KEY(flat_id) REFERENCES flat(flat_id) ON DELETE CASCADE);";

		String sendingHistory = "CREATE TABLE IF NOT EXISTS \"sending_history\"("
				+ "sent_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + "flat_id INTEGER NOT NULL,"
				+ "mail VARCHAR(100) NOT NULL," + "message VARCHAR(1000)," + "receipt_id INTEGER NOT NULL,"
				+ "FOREIGN KEY(flat_id) REFERENCES flat(flat_id) ON DELETE CASCADE,"
				+ "FOREIGN KEY(receipt_id) REFERENCES receipt(receipt_id) ON DELETE CASCADE);";

		String costs = "CREATE TABLE IF NOT EXISTS \"costs\"(" + "cost_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
				+ "house_id INTEGER NOT NULL," + "date DATE NOT NULL," + "maintenance INTEGER," + "heating INTEGER,"
				+ "garbage_collection INTEGER," + "water_sewer INTEGER," + "subscription_fees_water INTEGER,"
				+ "electricity INTEGER," + "FOREIGN KEY(house_id) REFERENCES house(house_id) ON DELETE CASCADE);";

		String receipt = "CREATE TABLE IF NOT EXISTS \"receipt\"("
				+ "receipt_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + "flat_id INTEGER NOT NULL,"
				+ "accountant VARCHAR(101) NOT NULL," + "owner_name VARCHAR(101) NOT NULL,"
				+ "people_living TINYINT NOT NULL," + "area_size INTEGER NOT NULL," + "water INTEGER NOT NULL,"
				+ "date DATE NOT NULL," + "deadline DATE NOT NULL," 
				+ "debt INTEGER NOT NULL," + "empty BOOLEAN NOT NULL," + "sent DATE,"
				+ "FOREIGN KEY(flat_id) REFERENCES flat(flat_id) ON DELETE CASCADE);";

		String usedBank = "CREATE TABLE IF NOT EXISTS \"used_bank\"("
				+ "used_bank_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + "bank_name VARCHAR(150) NOT NULL,"
				+ "bank_account VARCHAR(50) NOT NULL);";

		String receiptUsedBank = "CREATE TABLE IF NOT EXISTS \"receipt_used_bank\"(" + "receipt_id INTEGER NOT NULL,"
				+ "used_bank_id INTEGER NOT NULL," + "PRIMARY KEY(receipt_id, used_bank_id),"
				+ "FOREIGN KEY(receipt_id) REFERENCES receipt(receipt_id) ON DELETE CASCADE,"
				+ "FOREIGN KEY(used_bank_id) REFERENCES used_bank(used_bank_id) ON DELETE RESTRICT);";

		String appInfo = "CREATE TABLE IF NOT EXISTS \"log_info\"(" + "log_id INTEGER PRIMARY KEY NOT NULL,"
				+ "user_id INTEGER," + "FOREIGN KEY(user_id) REFERENCES accountant(accountant_id) ON DELETE CASCADE);";
		Collections.addAll(sqlQueries, languages, accountant, currency, house, bank, flat, flatBank, water, mail,
				sendingHistory, costs, receipt, usedBank, receiptUsedBank, appInfo);
	}

	//private static Boolean checkForBackup() {}

	private static void createTables(SessionFactory sf) {
		Session session = sf.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			sqlQueries.forEach(query -> {
				session.createNativeQuery(query).executeUpdate();
			});
			
			tx.commit();
		} catch (Exception e) {
			if (tx != null){
					tx.rollback();
				}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	private static Boolean createStructure() {		
		File dbFile = new File(DatabaseConstants.getBasepath()+DatabaseConstants.getStructurepath()+DatabaseConstants.getDbname());
		boolean success = true;
		if (dbFile.exists()) {
			success = dbFile.delete();
			if(!success){
				System.out.println("Couldn't delete structure");
			}else{
				SessionFactory structureFactory = null;
				final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("structure.cfg.xml").build();
				try {
					structureFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
					DBCheck.createTables(structureFactory);
				} catch (Exception e) {
					System.out.println("Couldn't build sessionfactory for structure: " + e);
					StandardServiceRegistryBuilder.destroy(registry);
					e.printStackTrace();
				} finally {
					structureFactory.close();
				}
			}
		}
		return success;
	}

	private static void fill() {
		CurrencyDA.save(new Currency("euro", "€", false));
		CurrencyDA.save(new Currency("pound", "£", true));
		
		LanguagesDA.save(new Languages("english", "en"));
		LanguagesDA.save(new Languages("estonian", "et"));
		LanguagesDA.save(new Languages("russian", "ru"));
	}

	private static void copyBackup() {
	}

	public static void setMainApp(MainApp app) {
		mainApp = app;
	}
}
