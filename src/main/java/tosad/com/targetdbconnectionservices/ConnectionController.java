package tosad.com.targetdbconnectionservices;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import tosad.com.hibernate.HibernateUtil;
import tosad.com.hibernate.model.TargetDatabase;
import tosad.com.hibernate.model.TargetDatabaseType;

public class ConnectionController implements ConnectionInterface{

	private ConnectionTemplate connectionTemplate;
	
	@Override
	public List<String> getTableNames(TargetDatabase targetDatabase, TargetDatabaseType targetDatabaseType) throws SQLException{
		List<String> tableNames = new ArrayList<String>();
		Connection connection = getTargetConnection(targetDatabaseType.getName(), targetDatabase.getConnection(), targetDatabase.getPassword(), targetDatabase.getUsername());
		
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
	
	@Override
	public List<String> getColumnNames(TargetDatabase targetDatabase, TargetDatabaseType targetDatabaseType, String tableName) throws SQLException{
		List<String> columnNames = new ArrayList<String>();
		Connection connection = getTargetConnection(targetDatabaseType.getName(), targetDatabase.getConnection(), targetDatabase.getPassword(), targetDatabase.getUsername());
		
		DatabaseMetaData databaseMetaData = connection.getMetaData();
		ResultSet resultSet = databaseMetaData.getColumns(null, null, tableName, null);

	    while (resultSet.next()) {
	      String columnName = resultSet.getString("COLUMN_NAME");
	      columnNames.add(columnName);
	    }
	    
	    connection.close();
	    
		return columnNames;
	}
	
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
