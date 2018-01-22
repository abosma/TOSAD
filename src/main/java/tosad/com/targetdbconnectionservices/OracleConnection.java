package tosad.com.targetdbconnectionservices;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleConnection extends ConnectionTemplate {

	Connection connection = null;

	public String GetConnectionString(String conString) {
		return "jdbc:oracle:thin:@//" + conString;
	}

	@Override
	Connection CreateConnection(String conString, String ww, String user) throws SQLException {
		try {
			connection = DriverManager.getConnection(this.GetConnectionString(conString), user, ww);
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
		}
		return connection;
	}

}
