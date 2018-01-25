package tosad.com.targetdbconnectionservices;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tosad.com.model.GeneratedCode;
import tosad.com.model.TargetDatabase;

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
		
		ResultSet resultSet = null;
	    DatabaseMetaData databaseMetaData = connection.getMetaData();
	    resultSet = databaseMetaData.getTables(null, null, null, new String[]{"TABLE"});

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
		List<String> columnNames = new ArrayList<String>();
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
		Statement statement = null;
		
		Connection connection = getTargetConnection(targetDatabase.getTargetDatabaseType().getName(), targetDatabase.getConnection(), targetDatabase.getPassword(), targetDatabase.getUsername());
		String query = generatedCode.getCode();
		try {
			statement = connection.createStatement();
			int insertedCode = statement.executeUpdate(query);
			
			if(insertedCode > 0) {
				return true;
			}else {
				return false;
			}
			
		} finally {
			if(statement != null) {
				statement.close();
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
			case "Postgres": connectionTemplate = new PostgresConnection();
							 connection = connectionTemplate.connect(connectionString, password, username);
		}
		
		return connection;
	}
}
