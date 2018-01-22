package tosad.com.targetdbconnectionservices;

import java.sql.SQLException;
import java.util.List;

public interface ConnectionInterface {
	public List<String> getTableNames(int targetDatabaseId) throws SQLException;
	public List<String> getColumnNames(int targetDatabaseId, String tableName) throws SQLException;
}
