package tosad.com.targetdbconnectionservices;

import java.sql.SQLException;
import java.util.List;

public interface ConnectionInterface {
	public List<String> GetTableNames(int targetDatabaseID) throws SQLException;
	public List<String> GetColumnNames(int targetDatabaseId)
}
