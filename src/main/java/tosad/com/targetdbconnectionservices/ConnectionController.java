package tosad.com.targetdbconnectionservices;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import tosad.com.hibernate.*;

public class ConnectionController {

	private ConnectionTemplate connectionTemplate;
	
	public List<String> getTableNames(int targetDBID) throws SQLException{
		Session ses = HibernateUtil.getSession();
		Transaction t = null;
		List<String> tableNames = new ArrayList<String>();
		
		try {
			t = ses.beginTransaction();
			TargetDatabase td = (TargetDatabase)ses.get(TargetDatabase.class, targetDBID);
			TargetDatabaseType tdt = td.getTargetDatabaseType();
			Connection c = getTargetConnection(tdt.getName(), td.getConnection(), td.getPassword(), td.getUsername());
			
			ResultSet rs = null;
		    DatabaseMetaData meta = c.getMetaData();
		    rs = meta.getTables(null, null, null, new String[]{"TABLE"});

		    while (rs.next()) {
		      String tableName = rs.getString("TABLE_NAME");
		      tableNames.add(tableName);
		    }
		    
		    c.close();
		}catch(HibernateException he) {
			if(t != null) {
				t.rollback();
			}
			he.printStackTrace();
		} finally {
			ses.close();
		}
		
		return tableNames;
		
	}
	
	public List<String> getColumnNames(int targetDBID, String tableName) throws SQLException{
		Session ses = HibernateUtil.getSession();
		Transaction t = null;
		List<String> columnNames = new ArrayList<String>();
		
		try {
			t = ses.beginTransaction();
			TargetDatabase td = (TargetDatabase)ses.get(TargetDatabase.class, targetDBID);
			TargetDatabaseType tdt = td.getTargetDatabaseType();
			Connection c = getTargetConnection(tdt.getName(), td.getConnection(), td.getPassword(), td.getUsername());
			Statement stmt = c.createStatement();
			
			DatabaseMetaData meta = c.getMetaData();
			ResultSet rs = meta.getColumns(null, null, tableName, null);

		    while (rs.next()) {
		      String columnName = rs.getString("COLUMN_NAME");
		      columnNames.add(columnName);
		    }
		    
		    c.close();
		}catch(HibernateException he) {
			if(t != null) {
				t.rollback();
			}
			he.printStackTrace();
		} finally {
			ses.close();
		}
		return columnNames;
	}
	
	public Connection getTargetConnection(String type, String conString, String ww, String user) throws SQLException {
		Connection connection = null;
		
		switch(type) {
			case "Oracle": 	 connectionTemplate = new OracleConnection();
						     connection = connectionTemplate.Connect(conString, ww, user);
			case "Postgres": connectionTemplate = new PostgresConnection();
							 connection = connectionTemplate.Connect(conString, ww, user);
		}
		
		return connection;
	}
}
