package tosad.com.targetdbconnectionservices;

import java.sql.SQLException;
import java.util.List;

import tosad.com.hibernate.model.GeneratedCode;
import tosad.com.hibernate.model.TargetDatabase;
import tosad.com.hibernate.model.TargetDatabaseType;

public interface ConnectionInterface {
	public List<String> getTableNames(TargetDatabase targetDatabase) throws SQLException;
	public List<String> getColumnNames(TargetDatabase targetDatabase, String tableName) throws SQLException;
	public boolean insertCode(TargetDatabase targetDatabase, GeneratedCode generatedCode) throws SQLException;
}
