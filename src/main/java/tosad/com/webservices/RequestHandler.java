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

import tosad.com.generator.Generator;
import tosad.com.generator.GeneratorInterface;
import tosad.com.model.BusinessRule;
import tosad.com.model.GeneratedCode;
import tosad.com.model.TargetDatabase;
import tosad.com.model.util.HibernateUtil;
import tosad.com.targetdbconnectionservices.ConnectionController;
import tosad.com.targetdbconnectionservices.ConnectionInterface;

@Path("/get")
public class RequestHandler {
	
	/**
	 * Returns JSON with table names based on target database ID given with the GET request. 
	 * Database ID is based on the path parameter after /restservices/gettables/.
	 * Calls ConnectionController to get a list of table names, then adds these into a JSON array with a lambda loop.
	 * 
	 * JSON format is: {
	 *                  tables:[
	 *                          {name: "tablename"}
	 *                         ]
	 *                 }
	 *                 
	 * @param targetDatabaseId
	 * @return JSON
	 * @throws SQLException
	 */
	@GET
	@Path("/gettables/{database_id}")
	@Produces("application/json")
	public String getTables(@PathParam("database_id") int targetDatabaseId) throws SQLException {
		
		Session session = HibernateUtil.getSession();
		Transaction transaction = null;
		TargetDatabase targetDatabase = null;
		
		try {
			transaction = session.beginTransaction();
			targetDatabase = session.get(TargetDatabase.class, targetDatabaseId);
		}catch(HibernateException hibernateException) {
			if(transaction != null) {
				transaction.rollback();
			}
			hibernateException.printStackTrace();
		}finally {
			session.close();
		}
		
		if(targetDatabase != null) {
			ConnectionInterface connectionInterface = new ConnectionController();
			
			List<String> tableNames = connectionInterface.getTableNames(targetDatabase);
			
			JsonArrayBuilder tableArrayBuilder = Json.createArrayBuilder();
			tableNames.forEach(a -> tableArrayBuilder.add(Json.createObjectBuilder().add("name", a)));
			
			JsonArray tableArray = tableArrayBuilder.build();
			JsonObjectBuilder objectBuilder = Json.createObjectBuilder().add("tables", tableArray);
			
			return objectBuilder.build().toString();
		}else {
			JsonObjectBuilder responseBuilder = Json.createObjectBuilder();
			responseBuilder.add("status", "Geen data");
			return responseBuilder.build().toString();
		}
	}
	
	/**
	 * Returns JSON with column names based on target database ID and tablename given with the GET request. 
	 * 
	 * Calls ConnectionController to get a list of column names, then adds these into a JSON array with a lambda loop.
	 * JSON format is: {
	 *                  columns:[
	 *                           {name: "columnname"}
	 *                          ]
	 *                 }
	 * 
	 * Database ID is based on the path parameter after /restservices/gettables/.
	 * Tablename is based on the path parameter after /restservices/gettablecolumn/databaseid/
	 * 
	 * @param targetDatabaseId
	 * @param tableName
	 * @return JSON
	 * @throws SQLException
	 */
	@GET
	@Path("/gettablecolumns/{database_id}/{tablename}")
	@Produces("application/json")
	public String getColumns(@PathParam("database_id") int targetDatabaseId,
							  @PathParam("tablename") String tableName) throws SQLException {
		Session session = HibernateUtil.getSession();
		Transaction transaction = null;
		TargetDatabase targetDatabase = null;
		
		try {
			transaction = session.beginTransaction();
			targetDatabase = session.get(TargetDatabase.class, targetDatabaseId);
		}catch(HibernateException hibernateException) {
			if(transaction != null) {
				transaction.rollback();
			}
			hibernateException.printStackTrace();
		}finally {
			session.close();
		}
		
		if(targetDatabase != null) {
			ConnectionInterface connectionInterface = new ConnectionController();
			List<String> columnNames = connectionInterface.getColumnNames(targetDatabase, tableName.toUpperCase());
			
			JsonArrayBuilder columnArrayBuilder = Json.createArrayBuilder();
			columnNames.forEach(a -> columnArrayBuilder.add(Json.createObjectBuilder().add("name", a)));
			
			JsonArray columnArray = columnArrayBuilder.build();
			JsonObjectBuilder objectBuilder = Json.createObjectBuilder().add("columns", columnArray);
			
			return objectBuilder.build().toString();
		}else {
			JsonObjectBuilder responseBuilder = Json.createObjectBuilder();
			responseBuilder.add("status", "Geen data");
			return responseBuilder.build().toString();
		}
	}
	
	/**
	 * Returns JSON with true or false, this represents if the code has been generated based on the BusinessRule object.
	 * Creates a GeneratedCode object with a foreign key businessRuleId, with code generated at Generator.java class.
	 * It then gets status = 0 due to the generated code not yet being inserted into the target database.
	 * 
	 * JSON Format is: {status: "false"/"true"}
	 * 
	 * businessrule_id is based on the path parameter after /restservices/generatecode/
	 * 
	 * @param businessRuleId
	 * @return JSON
	 * @throws SQLException
	 */
	@GET
	@Path("/generatecode/{businessrule_id}")
	@Produces("application/json")
	public String generateCode(@PathParam("businessrule_id") int businessRuleId) {
		JsonObjectBuilder responseBuilder = Json.createObjectBuilder();
		GeneratorInterface generatorInterface = new Generator();
		Session session = HibernateUtil.getSession();
		Transaction transaction = null;
		BusinessRule businessRule;
		
		try {
			transaction = session.beginTransaction();

			businessRule = session.get(BusinessRule.class, businessRuleId);

			String code = generatorInterface.generateSQL(businessRule);

			GeneratedCode generatedCode = new GeneratedCode();
			generatedCode.setCode(code);
			generatedCode.setBusinessRule(businessRule);
			generatedCode.setStatus(0);

			session.persist(generatedCode);
			transaction.commit();

			if(session.get(GeneratedCode.class, generatedCode.getId()) != null) {
				responseBuilder.add("code", code);
				responseBuilder.add("id", generatedCode.getId());
				session.close();
			}else{
				responseBuilder.add("status", "Business Rule is null");
				session.close();
			}
		}catch( Exception multiException ) {
			if(transaction != null) {
				transaction.rollback();
			}
			multiException.printStackTrace();
			session.close();

            responseBuilder.add("status", multiException.getMessage());
		}

		return responseBuilder.build().toString();
	}
	
	
	/**
	 * Returns JSON with true or false, this represents if the code has been inserted based on the GeneratedCode object.
	 * Inserts the GeneratedCode object with the given ID into the target database using ConnectionController.insertCode.
	 * The GeneratedCode object then gets status = 1 due to the generated code being inserted into the target database.
	 * 
	 * JSON Format is: {status: "true"/"{errormessage}"}
	 * 
	 * generatedcode_id is based on the path parameter after /restservices/insertcode/
	 * 
	 * @param generatedCodeId
	 * @return JSON
	 * @throws SQLException
	 */
	@GET
	@Path("/insertcode/{generatedcode_id}")
	@Produces("application/json")
	public String insertCode(@PathParam("generatedcode_id") int generatedCodeId) throws SQLException {
		ConnectionInterface connectionInterface = new ConnectionController();
		JsonObjectBuilder responseBuilder = Json.createObjectBuilder();
		Session session = HibernateUtil.getSession();
		Transaction transaction = null;
		GeneratedCode generatedCode;
		BusinessRule businessRule;
		TargetDatabase targetDatabase;
		
		try {
			transaction = session.beginTransaction();

			generatedCode = session.get(GeneratedCode.class, generatedCodeId);
			businessRule = generatedCode.getBusinessRule();
			targetDatabase = businessRule.getTargetDatabase();

            System.out.println(generatedCode.toString());
            System.out.println(businessRule.toString());
            System.out.println(targetDatabase.toString());

			boolean hasInserted = connectionInterface.insertCode(targetDatabase, generatedCode);

			if(hasInserted) {
				generatedCode.setStatus(1);
				session.save(generatedCode);
				responseBuilder.add("status", "true");
				session.close();
			}else {
				responseBuilder.add("status", "Code was not inserted due to error.");
				session.close();
			}
		}catch(HibernateException|NullPointerException multiException) {
			if(transaction != null) {
				transaction.rollback();
			}
			multiException.printStackTrace();

			session.close();

            responseBuilder.add("status", multiException.getMessage());
		}
		
		return responseBuilder.build().toString();
	}
	
}
