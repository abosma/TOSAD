package tosad.com.targetdbconnectionservices;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class ConnectionTemplate {

	abstract Connection CreateConnection(String conString, String ww, String user) throws SQLException;
	
	public final Connection Connect(String conString, String ww, String user) throws SQLException {
		return CreateConnection(conString, ww, user);
	}
	
}
