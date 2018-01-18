package tosad.com.webservices;

import java.sql.SQLException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;


@Path("/get")
public class GetController {
	
	@GET
	@Path("/gettabellen")
	@Produces("application/json")
	public String getAllTables() throws SQLException{
		return "";
	}
	
	@GET
	@Path("/getkollomen")
	@Produces("application/json")
	public String getKollomen() throws SQLException {
		return "";
	}
}
