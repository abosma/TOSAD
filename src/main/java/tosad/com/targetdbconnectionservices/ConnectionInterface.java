package tosad.com.targetdbconnectionservices;

import java.sql.SQLException;
import java.util.List;

import tosad.com.model.GeneratedCode;
import tosad.com.model.TargetDatabase;

public interface ConnectionInterface {
	public List<String> getTableNames(TargetDatabase targetDatabase) throws SQLException;
	public List<String> getColumnNames(TargetDatabase targetDatabase, String tableName) throws SQLException;
	public boolean insertCode(TargetDatabase targetDatabase, GeneratedCode generatedCode) throws SQLException;
}
