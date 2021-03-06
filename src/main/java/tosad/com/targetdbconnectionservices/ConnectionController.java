package tosad.com.targetdbconnectionservices;

import tosad.com.model.GeneratedCode;
import tosad.com.model.TargetDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConnectionController implements ConnectionInterface{

	private ConnectionTemplate connectionTemplate;
	
	/**
	 * Returns a list of tablenames from the given Target Database object.
	 * 
	 * Calls getTargetConnection using info in the Target Database object to connect to the target database, 
	 * the type determines which JDBC driver it will use to connect to the given target database.
	 * 
	 * @param targetDatabase
	 * @return List<String>
	 * @throws SQLException
	 */
	@Override
	public List<String> getTableNames(TargetDatabase targetDatabase) throws SQLException{
		List<String> tableNames = new ArrayList<String>();
		
		Connection connection = getTargetConnection(targetDatabase.getTargetDatabaseType().getName(), targetDatabase.getConnection(), targetDatabase.getPassword(), targetDatabase.getUsername());

		Statement statement = connection.createStatement();
		String query = "select tablespace_name, table_name from user_tables";

		ResultSet resultSet = statement.executeQuery(query);

	    while (resultSet.next()) {
	      String tableName = resultSet.getString("TABLE_NAME");
	      tableNames.add(tableName);
	    }
	    
	    connection.close();
		
		return tableNames;
		
	}
	
	/**
	 * Returns a list of tablenames from the given Target Database object and given table name
	 * 
	 * Calls getTargetConnection using info in the Target Database object to connect to the target database, 
	 * the type determines which JDBC driver it will use to connect to the given target database.
	 * 
	 * @param targetDatabase
	 * @return List<String>
	 * @throws SQLException
	 */
	@Override
	public List<String> getColumnNames(TargetDatabase targetDatabase, String tableName) throws SQLException{
		List<String> columnNames = new ArrayList<>();
		Connection connection = getTargetConnection(targetDatabase.getTargetDatabaseType().getName(), targetDatabase.getConnection(), targetDatabase.getPassword(), targetDatabase.getUsername());
		
		DatabaseMetaData databaseMetaData = connection.getMetaData();
		ResultSet resultSet = databaseMetaData.getColumns(null, null, tableName, null);

	    while (resultSet.next()) {
	      String columnName = resultSet.getString("COLUMN_NAME");
	      columnNames.add(columnName);
	    }
	    
	    connection.close();
	    
		return columnNames;
	}
	
	/**
	 * Returns a boolean based on the success of inserting the given Generated Code object into the target database.
	 * 
	 * Calls getTargetConnection using info in the Target Database object to connect to the target database, 
	 * the type determines which JDBC driver it will use to connect to the given target database.
	 * 
	 * Uses the code given by the GeneratedCode object to create a query, then runs an executeUpdate query. 
	 * 
	 * Based on the amount of affected columns it will send back true or false.
	 * 
	 * @param targetDatabase
	 * @param generatedCode
	 * @return List<String>
	 * @throws SQLException
	 */
	@Override
	public boolean insertCode(TargetDatabase targetDatabase, GeneratedCode generatedCode) throws SQLException{
        Statement stmt = null;

		Connection connection = getTargetConnection(targetDatabase.getTargetDatabaseType().getName(), targetDatabase.getConnection(), targetDatabase.getPassword(), targetDatabase.getUsername());
		String query = generatedCode.getCode();
		try {
            stmt = connection.createStatement();
            int insertedCode = stmt.executeUpdate(query);;
			return true;
		} finally {
			if(stmt != null) {
                stmt.close();
			}
		}
	}
	
	/**
	 * Returns a JDBC Connection based on the given information.
	 * 
	 * Based on the type it will choose a different Connection class to return a connection.
	 * 
	 * Uses the Template design pattern to easily return the right connection.
	 * 
	 * @param type
	 * @param connectionString
	 * @param password
	 * @param username
	 * @return Connection
	 * @throws SQLException
	 */
	public Connection getTargetConnection(String type, String connectionString, String password, String username) throws SQLException {
		Connection connection = null;
		
		switch(type) {
			case "Oracle": 	 connectionTemplate = new OracleConnection();
						     connection = connectionTemplate.connect(connectionString, password, username);
		}
		
		return connection;
	}
}
