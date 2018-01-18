package tosad.com.connection;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionController {

	private ConnectionTemplate ct;
	
	public Connection GetToolConnection() throws SQLException {
		ct = new OracleConnection();
		return ct.Connect("ondora02.hu.nl:8521/cursus02.hu.nl", "tosad_2017_2a_team1", "tosad_2017_2a_team1");
	}
}
