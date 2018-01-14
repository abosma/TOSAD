package tosad.com.dao;

import java.sql.*;

import java.util.ArrayList;

import tosad.com.domain.Table;
import tosad.com.domain.TableColumn;
import tosad.com.persistency.SqLiteConnection;

public class TableDao {
	Connection dbconnection;
	

	Statement stmt = null;

	String queryGetTables = "select table_name from all_tables  where TABLE_NAME like 'TOSAD_%'";

	public TableDao()  {
	}

	public ArrayList<Table> getAllTables() throws SQLException {
		dbconnection = new SqLiteConnection().CreateConnection();

		ArrayList<Table> alltables = new ArrayList<Table>();

		try {
			stmt = dbconnection.createStatement();
			ResultSet rs = stmt.executeQuery(queryGetTables);
			while (rs.next()) {
				System.out.println("hier");
				String tableName = rs.getString("TABLE_NAME");
				Table table = new Table(tableName);

				DatabaseMetaData meta = dbconnection.getMetaData();
				ResultSet columns = meta.getColumns(null, null, tableName, null);
				int i = 1;
				while (columns.next()) {
					TableColumn tc = new TableColumn(columns.getString("TYPE_NAME"), columns.getString("COLUMN_NAME"));
					table.addColumns(tc);
				
				}
				
				alltables.add(table);

			}
			dbconnection.close();
		} catch (SQLException e) {
		}
		return alltables;

	}
}