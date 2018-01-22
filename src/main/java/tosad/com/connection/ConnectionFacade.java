package tosad.com.connection;

import java.util.List;

public class ConnectionFacade {
	
	private ConnectionController cc;
	
	public List<String> GetTableNames(int targetDBID){
		return cc.GetTableNames(targetDBID);
	}
	
	public List<String> GetColumnNames(int targetDBID, String tableName){
		return cc.GetColumnNames(targetDBID, tableName);
	}
}
