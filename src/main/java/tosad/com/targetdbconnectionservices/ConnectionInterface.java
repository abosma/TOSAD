package tosad.com.targetdbconnectionservices;

import java.sql.SQLException;
import java.util.List;

<<<<<<< HEAD
import tosad.com.model.GeneratedCode;
import tosad.com.model.TargetDatabase;
import tosad.com.model.TargetDatabaseType;
=======
import tosad.com.hibernate.model.GeneratedCode;
import tosad.com.hibernate.model.TargetDatabase;
>>>>>>> 75477289b104f92216984a1115444d19deee04ba

public interface ConnectionInterface {
	public List<String> getTableNames(TargetDatabase targetDatabase) throws SQLException;
	public List<String> getColumnNames(TargetDatabase targetDatabase, String tableName) throws SQLException;
	public boolean insertCode(TargetDatabase targetDatabase, GeneratedCode generatedCode) throws SQLException;
}
