package tosad.com.webservices;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/test")
public class GetController {
	
	@GET
	@Produces("application/json")
	public String getAllCountries(){
		JsonArrayBuilder uriArray = Json.createArrayBuilder();
		
		JsonObjectBuilder jo = Json.createObjectBuilder().add("test", "test");
		uriArray.add(jo);
				
		return uriArray.build().toString();
	}
}
