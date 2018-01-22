package tosad.com.connection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import tosad.com.persistency.*;
import tosad.com.util.HibernateUtil;

public class ConnectionController {

	private ConnectionTemplate ct;
	
	public List<String> GetTableNames(int targetDBID){
		Session ses = HibernateUtil.getSession();
		Transaction t = null;
		List<String> tableNames = new ArrayList<String>();
		
		try {
			t = ses.beginTransaction();
			TargetDatabase td = (TargetDatabase)ses.get(TargetDatabase.class, targetDBID);
			Connection c = GetTargetConnection(td.getDBType(), td.getConnection(), td.getPassword(), td.getUsername());
			
			ResultSet rs = null;
		    DatabaseMetaData meta = c.getMetaData();
		    rs = meta.getTables(null, null, null, new String[]{"TABLE"});

		    while (rs.next()) {
		      String tableName = rs.getString("TABLE_NAME");
		      tableNames.add(tableName);
		    }
		    
		    c.close();
		    
		    return tableNames;
			
		}catch(HibernateException he) {
			if(t != null) {
				t.rollback();
			}
			he.printStackTrace();
		} finally {
			ses.close();
		}
		
	}
	
	public Connection GetTargetConnection(String type, String conString, String ww, String user) throws SQLException {
		Connection c = null;
		
		switch(type) {
			case "Oracle": 	 ct = new OracleConnection();
						     c = ct.Connect(conString, ww, user);
			case "Postgres": ct = new PostgresConnection();
							 c = ct.Connect(conString, ww, user);
		}
		
		return c;
	}
}
