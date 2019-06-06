package home.accounting.DA.mySchemaComparison;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import home.accounting.DA.SQLiteConnection;
import home.accounting.DA.mySchemaComparison.models.Column;
import home.accounting.DA.mySchemaComparison.models.Schema;
import home.accounting.DA.mySchemaComparison.models.Table;

public class SchemaCheck {
	
	private Boolean dataRetrieved;

	public SchemaCheck() {
		dataRetrieved = false;
	}
	
	public void run(Schema workingDB){
		
		Schema baseDB = new Schema(DatabaseConstants.getStructurepath());

		if(!dataRetrieved){
			System.out.println("Data was not previously retrieved.");
			System.out.println("Retrieving data...");
			retrieveData(workingDB);
		}
		retrieveData(baseDB);
		
		boolean upToDate = compareTables(workingDB, baseDB);
		if(upToDate){
			upToDate = compareColumns(workingDB, baseDB);
		}
		
		if (upToDate) {
			System.out.println("Database up to date");
		} else {
			System.out.println("Updaing databse...");
			new DataTransfer(workingDB, baseDB);
		}
	}
	
	public void fillSchema(Schema schema){
		retrieveData(schema);
		dataRetrieved = true;
	}

	/**
	 * Fills Schema object with tables and columns
	 * @param schema
	 */
	private void retrieveData(Schema schema) {
		Connection workingConnection = SQLiteConnection.getConnection(schema.getConnection());
		if (workingConnection != null) {
			try {
				DatabaseMetaData metaData = workingConnection.getMetaData();
				ResultSet tableResult = metaData.getTables(null, null, null, new String[] { "TABLE" });
				while (tableResult.next()) {
					schema.addTable(new Table(tableResult.getString("TABLE_NAME")));
				}
				tableResult.close();
				schema.getTables().forEach(table -> {
					try {
						ResultSet columnResult = metaData.getColumns(null, null, table.getName(), null);
						while (columnResult.next()) {
							table.addColumn(new Column(columnResult.getString("COLUMN_NAME"),columnResult.getString("TYPE_NAME"),columnResult.getString("IS_NULLABLE"),columnResult.getString("ORDINAL_POSITION")));
						}
						columnResult.close();
					} catch (SQLException e) {

					}
				});

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					workingConnection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	/**
	 * Checks whether table names are different from structure
	 * @param compared
	 * @param comparedTo
	 * @return
	 */
	private Boolean compareTables(Schema compared, Schema comparedTo){
		boolean tablesUpToDate = true;
		//check if table count didn't change
		if(compared.getTables().size()!=comparedTo.getTables().size()){
			tablesUpToDate = false;
		}
		if(tablesUpToDate){
			for(int i=0;i<compared.getTables().size();i++){
				boolean found = false;
				int k = 0;
				while(!found && k<compared.getTables().size()){
					String name = compared.getTables().get(i).getName();
					String compToName = comparedTo.getTables().get(k).getName();
					if(name.equals(compToName)){
						found = true;
					}
					k++;
				}
				if(!found){
					tablesUpToDate = false;
				}
			}
		}
		
		return tablesUpToDate;
	}
	
	/**
	 * Checks whether columns are the same
	 * @param compared
	 * @param comparedTo
	 * @return
	 */
	private Boolean compareColumns(Schema compared, Schema comparedTo){
		boolean columnsUpToDate = true;
		int i = 0;
		while(columnsUpToDate && i<compared.getTables().size()){	//loop through all tables
			boolean foundRightTable = false;
			int j = 0;
			while(!foundRightTable && j<comparedTo.getTables().size()){	//loop through all tables of structure database
				if(compared.getTables().get(i).getName().equals(comparedTo.getTables().get(j).getName())){	// if found matching table
					foundRightTable = true;
					columnsUpToDate = columnSearch(compared.getTables().get(i).getColumns(), comparedTo.getTables().get(j).getColumns());
				}
				j++;
			}
			i++;
		}
		return columnsUpToDate;
	}
	
	private Boolean columnSearch(ArrayList<Column> search, ArrayList<Column> searchIn){
		boolean fine = true;
		if(search.size()<searchIn.size()){
			ArrayList<Column> temp = search;
			search = searchIn;
			searchIn = temp;
			temp = null;
		}
		
		for(int i=0; i<search.size(); i++){
			boolean foundColumn = false;
			int searchInColumnCount = 0;
			while(!foundColumn && searchInColumnCount<searchIn.size()){
				if (search.get(i).getName().equals(searchIn.get(searchInColumnCount).getName())) {
					foundColumn = true;
					if(!search.get(i).getIsNullable().equals(searchIn.get(searchInColumnCount).getIsNullable()) || !search.get(i).getType().equals(searchIn.get(searchInColumnCount).getType())){
						System.out.println("Structure of column "+search.get(i).getName()+" was changed");
						fine = false;
					}
				}
				searchInColumnCount++;
			}
			if(!foundColumn){
				fine = false;
			}
		}
		return fine;
	}
}
