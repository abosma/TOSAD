package tosad.com.targetdbconnectionservices;

import java.sql.SQLException;
import java.util.List;

public class ConnectionFacade {
	
	private ConnectionController cc;
	
	public List<String> GetTableNames(int targetDBID) throws SQLException{
		return cc.GetTableNames(targetDBID);
	}
	
	public List<String> GetColumnNames(int targetDBID, String tableName) throws SQLException{
		return cc.GetColumnNames(targetDBID, tableName);
	}
}
