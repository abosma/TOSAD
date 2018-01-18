package tosad.com.connection;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionFacade {
	
	private ConnectionController cc;
	
	public Connection GetToolConnection() throws SQLException {
		return cc.GetToolConnection();
	}
}
