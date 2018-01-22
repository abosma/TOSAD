package tosad.com.targetdbconnectionservices;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnection extends ConnectionTemplate {

	Connection connection = null;

	public String getConnectionString(String connectionString) {
		return "jdbc:postgresql://" + connectionString;
	}
	
	@Override
	Connection createConnection(String connectionString, String password, String username) throws SQLException {
		try {
			connection = DriverManager.getConnection(this.getConnectionString(connectionString), username, password);
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
		}
		return connection;
	}


}
