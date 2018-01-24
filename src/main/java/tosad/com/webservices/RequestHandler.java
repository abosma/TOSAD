package tosad.com.webservices;

import java.sql.SQLException;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import tosad.com.hibernate.HibernateUtil;
import tosad.com.hibernate.model.TargetDatabase;
import tosad.com.hibernate.model.TargetDatabaseType;
import tosad.com.targetdbconnectionservices.ConnectionController;
import tosad.com.targetdbconnectionservices.ConnectionInterface;


@Path("/get")
public class RequestHandler {
	
	@GET
	@Path("/gettables/{database_id}")
	@Produces("application/json")
	public String getTables(@PathParam("database_id") int targetDatabaseId) throws SQLException {
		
		Session session = HibernateUtil.getSession();
		Transaction transaction = null;
		TargetDatabase targetDatabase = null;
		TargetDatabaseType targetDatabaseType = null;
		
		try {
			transaction = session.beginTransaction();
			targetDatabase = (TargetDatabase)session.get(TargetDatabase.class, targetDatabaseId);
			targetDatabaseType = targetDatabase.getTargetDatabaseType();
		}catch(HibernateException hibernateException) {
			if(transaction != null) {
				transaction.rollback();
			}
			hibernateException.printStackTrace();
		} finally {
			session.close();
		}
		
		ConnectionInterface connectionInterface = new ConnectionController();
		
		List<String> tableNames = connectionInterface.getTableNames(targetDatabase, targetDatabaseType);
		
		JsonArrayBuilder tableArrayBuilder = Json.createArrayBuilder();
		tableNames.forEach(a -> tableArrayBuilder.add(Json.createObjectBuilder().add("name", a)));
		
		JsonArray tableArray = tableArrayBuilder.build();
		JsonObjectBuilder objectBuilder = Json.createObjectBuilder().add("tables", tableArray);
		
		return objectBuilder.build().toString();
	}
	
	@GET
	@Path("/gettablecolumns/{database_id}/{tablename}")
	@Produces("application/json")
	public String getColumns(@PathParam("database_id") int targetDatabaseId,
							  @PathParam("tablename") String tableName) throws SQLException {
		Session session = HibernateUtil.getSession();
		Transaction transaction = null;
		TargetDatabase targetDatabase = null;
		TargetDatabaseType targetDatabaseType = null;
		
		try {
			transaction = session.beginTransaction();
			targetDatabase = (TargetDatabase)session.get(TargetDatabase.class, targetDatabaseId);
			targetDatabaseType = targetDatabase.getTargetDatabaseType();
		}catch(HibernateException hibernateException) {
			if(transaction != null) {
				transaction.rollback();
			}
			hibernateException.printStackTrace();
		} finally {
			session.close();
		}
		
		ConnectionInterface connectionInterface = new ConnectionController();
		List<String> columnNames = connectionInterface.getColumnNames(targetDatabase, targetDatabaseType, tableName);
		
		JsonArrayBuilder columnArrayBuilder = Json.createArrayBuilder();
		columnNames.forEach(a -> columnArrayBuilder.add(Json.createObjectBuilder().add("name", a)));
		
		JsonArray columnArray = columnArrayBuilder.build();
		JsonObjectBuilder objectBuilder = Json.createObjectBuilder().add("columns", columnArray);
		
		return objectBuilder.build().toString();
	}
}
