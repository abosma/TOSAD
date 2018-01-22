package tosad.com.webservices;

import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import tosad.com.targetdbconnectionservices.ConnectionFacade;


@Path("/get")
public class WebController {
	
	@GET
	@Path("/gettabellen/{dbid}")
	@Produces("application/json")
	public String GetTables(@PathParam("dbid") int targetDBID) {
		ConnectionFacade cf = new ConnectionFacade();
		List<String> tableNames = cf.GetTableNames(targetDBID);
		
		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		JsonObjectBuilder objectBuilder = Json.createObjectBuilder().add("tables", arrayBuilder);
		
		tableNames.forEach(a -> arrayBuilder.add(a));
		
		return objectBuilder.build().toString();
	}
	
	@GET
	@Path("/getkollomen/{dbid}/{tablename}")
	@Produces("application/json")
	public String GetColumns(@PathParam("dbid") int targetDBID,
							  @PathParam("tablename") String tableName) {
		ConnectionFacade cf = new ConnectionFacade();
		List<String> columnNames = cf.GetColumnNames(targetDBID, tableName);
		
		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		JsonObjectBuilder objectBuilder = Json.createObjectBuilder().add("columns", arrayBuilder);
		
		columnNames.forEach(a -> arrayBuilder.add(a));
		
		return objectBuilder.build().toString();
	}
}
