package tosad.com.persistency;

import java.sql.*;

public class SqLiteConnection implements IConnection {

	Connection connection = null;

	@Override
	public String GetConnectionString() {
		return "jdbc:oracle:thin:@//ondora02.hu.nl:8521/cursus02.hu.nl";
	}

	@Override
	public Connection CreateConnection() throws SQLException {
		try {

			connection = DriverManager.getConnection(

					this.GetConnectionString(), "tosad_2017_2a_team1", "tosad_2017_2a_team1");

		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();

		}
		return connection;
	}

}
