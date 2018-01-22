package tosad.com.connection;

import java.util.List;

public class ConnectionFacade {
	
	private ConnectionController cc;
	
	public List<String> GetTableNames(int targetDBID){
		return cc.GetTableNames(targetDBID);
	}
}
