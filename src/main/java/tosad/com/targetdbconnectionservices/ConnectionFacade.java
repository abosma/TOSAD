package tosad.com.targetdbconnectionservices;

import java.sql.SQLException;
import java.util.List;

public class ConnectionFacade {
	
	private ConnectionController cc;
	
	public List<String> getTableNames(int targetDBID) throws SQLException{
		return cc.getTableNames(targetDBID);
	}
	
	public List<String> getColumnNames(int targetDBID, String tableName) throws SQLException{
		return cc.getColumnNames(targetDBID, tableName);
	}
}
