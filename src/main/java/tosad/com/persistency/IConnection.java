package tosad.com.persistency;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IConnection {
	String GetConnectionString();

	Connection CreateConnection() throws SQLException;
}
