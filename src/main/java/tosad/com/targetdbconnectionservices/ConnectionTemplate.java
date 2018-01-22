package tosad.com.targetdbconnectionservices;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class ConnectionTemplate {

	abstract Connection createConnection(String connectionString, String password, String username) throws SQLException;
	
	public final Connection connect(String connectionString, String password, String username) throws SQLException {
		return createConnection(connectionString, password, username);
	}
	
}
