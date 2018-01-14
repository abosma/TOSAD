package tosad.com.webservices;

import java.sql.SQLException;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import tosad.com.dao.TableDao;
import tosad.com.domain.Table;
import tosad.com.domain.TableColumn;

@Path("/test")
public class GetController {
	TableDao dao = new TableDao();
	

	
	@GET
	@Produces("application/json")
	public String getAllTables() throws SQLException{
		
		JsonArrayBuilder arrayTables = Json.createArrayBuilder();
		
		for (Table t : dao.getAllTables()) {
			System.out.println(t.getName());
			JsonObjectBuilder ta = Json.createObjectBuilder().add("tablename", t.getName());
			JsonArrayBuilder arrayColumns = Json.createArrayBuilder();

			for (TableColumn column : t.getAllcolumns()) {

				JsonObjectBuilder co = Json.createObjectBuilder().add("columnname", column.getName());
				co.add("type", column.getType());
				arrayColumns.add(co);
			}
			ta.add("Columns", arrayColumns);
			arrayTables.add(ta);

		}
				
		return arrayTables.build().toString();
	}
}
