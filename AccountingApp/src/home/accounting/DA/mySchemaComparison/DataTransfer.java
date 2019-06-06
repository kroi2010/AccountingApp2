package home.accounting.DA.mySchemaComparison;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import home.accounting.MainApp;
import home.accounting.DA.mySchemaComparison.models.Column;
import home.accounting.DA.mySchemaComparison.models.Row;
import home.accounting.DA.mySchemaComparison.models.Schema;
import home.accounting.DA.mySchemaComparison.models.Table;
import home.accounting.DA.mySchemaComparison.models.Value;

public class DataTransfer {

	private static ArrayList<String> queries;

	private static MainApp mainApp;

	public static void setMainApp(MainApp app) {
		mainApp = app;
	}

	public DataTransfer(Schema oldDB, Schema newDB) {
		boolean operationSucsess = insertData(oldDB, newDB);
		
		if (operationSucsess) {
			mainApp.closeSessionFactory();
			operationSucsess = transferDB();
			mainApp.buildSessionFactory();
		}
		
		String msg = operationSucsess ? "Data transfer complete!" : "Data transfer failed!";
		if(!operationSucsess){
			mainApp.endSession();
		}
		System.out.println(msg);
	}

	private Boolean insertData(Schema oldDB, Schema newDB) {
		boolean done = true;
		
		oldDB = getData(oldDB);
		oldDB = updateMapping(oldDB, newDB);

		
		SessionFactory structureFactory = null; 
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("structure.cfg.xml").build();
		try {
			structureFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
			
			Session sess = structureFactory.openSession(); 
			Transaction tx = null;
			try { 
				tx = sess.beginTransaction(); 
				
				for(Table table : oldDB.getTables()){
					for(Row row : table.getRows()){
						String sqlStart = "INSERT INTO "+table.getName()+" (";
						String sqlMiddle =  ") VALUES (";
						String sqlEnd = ")";
						boolean firstValue = true;
						for(Value value : row.getValues()){
							if(!firstValue){
								sqlStart = sqlStart+", ";
								sqlMiddle = sqlMiddle+", ";
							}else{
								firstValue = false;
							}
							sqlStart = sqlStart+value.getColumn().getName();
							
							String par = value.getValue();
							if(value.getColumn().getType().contains("VARCHAR")){
								par = "'"+value.getValue()+"'";
							}else if(value.getColumn().getType().contains("BOOLEAN")){
								if(value.getValue().equals("false")){
									par = Integer.toString(0);
								}else{
									par = Integer.toString(1);
								}
							}
							sqlMiddle = sqlMiddle+par;
						}
						//System.out.println(sqlStart+sqlMiddle+sqlEnd);
						sess.createNativeQuery(sqlStart+sqlMiddle+sqlEnd).executeUpdate();
					}
				}
				
				tx.commit();
			} catch (Exception e) { 
				if(tx != null) tx.rollback(); 
				System.out.println(e); 
				e.printStackTrace();
				done = false;
				} finally { 
					sess.close(); 
				}
		}catch(Exception e){ 
		
			StandardServiceRegistryBuilder.destroy(registry); 
			e.printStackTrace(); 
			done = false;
		
		}finally{
		structureFactory.close();
	}

	return done;
	}
	
	private Boolean transferDB(){
		boolean success = true;
		if(DatabaseConstants.getBeforechangedbpath()!=null && !DatabaseConstants.getBeforechangedbpath().equals("")){
			Path pathFrom = Paths.get(DatabaseConstants.getBasepath(), DatabaseConstants.getDbname());
			Path PathTo = Paths.get(DatabaseConstants.getBasepath()+DatabaseConstants.getBeforechangedbpath(), DatabaseConstants.getDbname());
			try {
				Files.copy(pathFrom, PathTo, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Failed to save old database before replacing with new one.");
			}
		}
		
		Path sourceDB = Paths.get(DatabaseConstants.getBasepath()+DatabaseConstants.getStructurepath(), DatabaseConstants.getDbname());
		Path replacedDB = Paths.get(DatabaseConstants.getBasepath(), DatabaseConstants.getDbname());
		
		try {
			Files.copy(sourceDB, replacedDB, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			success = false;
			e.printStackTrace();
			System.out.println("Failed to transfer updated database.");
		}
		
		return success;
	}

	private static Schema getData(Schema oldDB) {
		Session session = mainApp.getSessionFactory().openSession();
		List<Object[]> fromDB;
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			for (int i = 0; i < oldDB.getTables().size(); i++) {
				fromDB = (ArrayList<Object[]>) session
						.createNativeQuery("SELECT * FROM " + oldDB.getTables().get(i).getName()).getResultList();
				for (Object[] element : fromDB) {
					Row dataRow = new Row();
					for (int j = 0; j < element.length; j++) {
						if (element[j] != null) {
							dataRow.addValue(new Value(element[j].toString(), oldDB.getTables().get(i).getColumns().get(j)));
						}
					}
					if (!dataRow.getValues().isEmpty() && !oldDB.getTables().get(i).getName().equals("sqlite_sequence")) {
						oldDB.getTables().get(i).addRows(dataRow);
					}
				}
			}
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			System.out.println(e);
			e.printStackTrace();
		} finally {
			session.close();
		}
		return oldDB;
	}

	public static Schema updateMapping(Schema currentDB, Schema structure) {
		// for each table in our database
		for (Table table : currentDB.getTables()) {
			boolean foundTable = false;
			int tableCount = 0;
			// look for it in structure table
			while (!foundTable && tableCount < structure.getTables().size()) {
				if (table.getName().equals(structure.getTables().get(tableCount).getName())) {
					// if this table exists
					foundTable = true;
					// for each column look for the match
					for (Column column : table.getColumns()) {
						boolean foundColumn = false;
						int columnCount = 0;
						while (!foundColumn
								&& columnCount < structure.getTables().get(tableCount).getColumns().size()) {
							if (column.getName().equals(
									structure.getTables().get(tableCount).getColumns().get(columnCount).getName())) {
								foundColumn = true;
							}
							columnCount++;
						}

						if (!foundColumn) {
							int lookThroughMap = 0;
							while (!foundColumn && lookThroughMap < DataMapping.getMapping().size()) {
								if (DataMapping.getMapping().get(lookThroughMap)[0].contains(".")) {
									String[] newMap = DataMapping.getMapping().get(lookThroughMap)[0].split("\\.");

									if (column.getName().equals(newMap[1]) && table.getName().equals(newMap[0])) {
										foundColumn = true;
										System.out.println("Column " + column.getName() + " was renamed into "
												+ DataMapping.getMapping().get(lookThroughMap)[1] + " in table "
												+ table.getName());
										column.setName(DataMapping.getMapping().get(lookThroughMap)[1]);
									}

								}
								lookThroughMap++;
							}
						}

						if (!foundColumn) {
							System.out.println("Column " + column.getName() + " was removed from table " + table.getName());
						}
					}
				}
				tableCount++;
			}

			if (!foundTable) {
				int lookThroughMap = 0;
				while (!foundTable && lookThroughMap < DataMapping.getMapping().size()) {
					if (!DataMapping.getMapping().get(lookThroughMap)[0].contains(".")) {
						if (table.getName().equals(DataMapping.getMapping().get(lookThroughMap)[0])) {
							foundTable = true;
							System.out.println("Table " + table.getName() + " was renamed into "
									+ DataMapping.getMapping().get(lookThroughMap)[1]);
							table.setName(DataMapping.getMapping().get(lookThroughMap)[1]);
						}
					}
					lookThroughMap++;
				}
			}

			if (!foundTable) {
				System.out.println("Table " + table.getName() + " was removed.");
			}
		}

		return currentDB;
	}

	public static void clear() {
		createClearSQL();

		SessionFactory structureFactory = null;
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("structure.cfg.xml") // configures
																														// settings
																														// from
																														// hibernate.cfg.xml
				.build();
		try {
			structureFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

			Session sess = structureFactory.openSession();
			Transaction tx = null;
			try {
				tx = sess.beginTransaction();
				queries.forEach(query -> {
					sess.createNativeQuery(query).executeUpdate();
				});
				tx.commit();
				System.out.println("Cleared structure.");
			} catch (Exception e) {
				if (tx != null)
					tx.rollback();
				System.out.println(e);
				e.printStackTrace();
			} finally {
				sess.close();
			}

		} catch (Exception e) {
			// The registry would be destroyed by the SessionFactory, but we had
			// trouble building the SessionFactory
			// so destroy it manually.
			System.out.println("Couldn't clear structure: " + e);
			StandardServiceRegistryBuilder.destroy(registry);
			throw e;
		} finally {
			structureFactory.close();
		}
	}

	private static void createClearSQL() {
		queries.add("DELETE * FROM log_info");
		queries.add("DELETE * FROM costs");
		queries.add("DELETE * FROM used_bank");
		queries.add("DELETE * FROM receipt_used_bank");
		queries.add("DELETE * FROM receipt");
		queries.add("DELETE * FROM flat_bank");
		queries.add("DELETE * FROM water");
		queries.add("DELETE * FROM sending_history");
		queries.add("DELETE * FROM mail");
		queries.add("DELETE * FROM flat");
		queries.add("DELETE * FROM house");
		queries.add("DELETE * FROM currency");
		queries.add("DELETE * FROM bank");
		queries.add("DELETE * FROM accountant");
	}
}
