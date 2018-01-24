package tosad.com.webservices;

import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import tosad.com.targetdbconnectionservices.ConnectionController;
import tosad.com.targetdbconnectionservices.ConnectionInterface;


@Path("/get")
public class RequestHandler {
	
	@GET
	@Path("/gettables/{database_id}")
	@Produces("application/json")
	public String getTables(@PathParam("database_id") int targetDatabaseId) throws SQLException {
		ConnectionInterface connectionInterface = new ConnectionController();
		
		//List<String> tableNames = connectionInterface.getTableNames(targetDatabaseId);
		
		List<String> tableNames = new ArrayList<String>();
		
		tableNames.add("Test1");
		tableNames.add("Test2");
		tableNames.add("Test3");
		
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
		
		ConnectionInterface connectionInterface = new ConnectionController();
		//List<String> columnNames = connectionInterface.getColumnNames(targetDatabaseId, tableName);
		
		List<String> columnNames = new ArrayList<String>();
		
		columnNames.add("Column1");
		columnNames.add("Column2");
		columnNames.add("Column3");
		
		JsonArrayBuilder columnArrayBuilder = Json.createArrayBuilder();
		columnNames.forEach(a -> columnArrayBuilder.add(Json.createObjectBuilder().add("name", a)));
		
		JsonArray columnArray = columnArrayBuilder.build();
		JsonObjectBuilder objectBuilder = Json.createObjectBuilder().add("columns", columnArray);
		
		return objectBuilder.build().toString();
	}
}
