package tosad.com.webservices;

import java.sql.SQLException;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import tosad.com.generator.Generator;
import tosad.com.generator.GeneratorInterface;
import tosad.com.hibernate.HibernateUtil;
import tosad.com.hibernate.model.BusinessRule;
import tosad.com.hibernate.model.GeneratedCode;
import tosad.com.hibernate.model.TargetDatabase;
import tosad.com.hibernate.model.TargetDatabaseType;
import tosad.com.targetdbconnectionservices.ConnectionController;
import tosad.com.targetdbconnectionservices.ConnectionInterface;

public class RequestHandler {
	
	@GET
	@Path("/gettables/{database_id}")
	@Produces("application/json")
	public String getTables(@PathParam("database_id") int targetDatabaseId) throws SQLException {
		
		Session session = HibernateUtil.getSession();
		Transaction transaction = null;
		TargetDatabase targetDatabase = null;
		
		try {
			transaction = session.beginTransaction();
			targetDatabase = (TargetDatabase)session.get(TargetDatabase.class, targetDatabaseId);
		}catch(HibernateException hibernateException) {
			if(transaction != null) {
				transaction.rollback();
			}
			hibernateException.printStackTrace();
		}finally {
			session.close();
		}
		
		ConnectionInterface connectionInterface = new ConnectionController();
		
		List<String> tableNames = connectionInterface.getTableNames(targetDatabase);
		
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
		
		try {
			transaction = session.beginTransaction();
			targetDatabase = (TargetDatabase)session.get(TargetDatabase.class, targetDatabaseId);
		}catch(HibernateException hibernateException) {
			if(transaction != null) {
				transaction.rollback();
			}
			hibernateException.printStackTrace();
		}finally {
			session.close();
		}
		
		ConnectionInterface connectionInterface = new ConnectionController();
		List<String> columnNames = connectionInterface.getColumnNames(targetDatabase, tableName);
		
		JsonArrayBuilder columnArrayBuilder = Json.createArrayBuilder();
		columnNames.forEach(a -> columnArrayBuilder.add(Json.createObjectBuilder().add("name", a)));
		
		JsonArray columnArray = columnArrayBuilder.build();
		JsonObjectBuilder objectBuilder = Json.createObjectBuilder().add("columns", columnArray);
		
		return objectBuilder.build().toString();
	}
	
	@POST
	@Path("/generatecode/{businessrule_id}")
	@Produces("application/json")
	public String generateCode(@PathParam("businessrule_id") int businessRuleId) throws SQLException {
		JsonObjectBuilder responseBuilder = Json.createObjectBuilder();
		GeneratorInterface generatorInterface = new Generator();
		Session session = HibernateUtil.getSession();
		Transaction transaction = null;
		BusinessRule businessRule = null;
		
		try {
			transaction = session.beginTransaction();
			businessRule = (BusinessRule)session.get(BusinessRule.class, businessRuleId);
		}catch(HibernateException hibernateException) {
			if(transaction != null) {
				transaction.rollback();
			}
			hibernateException.printStackTrace();
		}finally {
			session.close();
		}
		
		if(businessRule != null) {
			String code = generatorInterface.generateSQL(businessRule);
			
			GeneratedCode generatedCode = new GeneratedCode();
			generatedCode.setCode(code);
			generatedCode.setBusinessRule(businessRule);
			generatedCode.setId(businessRule.getId());
			generatedCode.setStatus(0);
			
			session.save(generatedCode);
			responseBuilder.add("status", "true");
		} else {
			responseBuilder.add("status", "false");
		}
		
		return responseBuilder.build().toString();
	}
	
	@POST
	@Path("/insertcode/{generatedcode_id}")
	@Produces("application/json")
	public String insertCode(@PathParam("generatedcode_id") int generatedCodeId) throws SQLException {
		ConnectionInterface connectionInterface = new ConnectionController();
		JsonObjectBuilder responseBuilder = Json.createObjectBuilder();
		Session session = HibernateUtil.getSession();
		Transaction transaction = null;
		GeneratedCode generatedCode = null;
		BusinessRule businessRule = null;
		TargetDatabase targetDatabase = null;
		
		try {
			transaction = session.beginTransaction();
			generatedCode = (GeneratedCode)session.get(GeneratedCode.class, generatedCodeId);
			businessRule = generatedCode.getBusinessRule();
			targetDatabase = businessRule.getTargetDatabase();
		}catch(HibernateException hibernateException) {
			if(transaction != null) {
				transaction.rollback();
			}
			hibernateException.printStackTrace();
		}catch(NullPointerException nullPointerException) {
			if(transaction != null) {
				transaction.rollback();
			}
			nullPointerException.printStackTrace();
		}finally {
			session.close();
		}
		
		boolean hasInserted = connectionInterface.insertCode(targetDatabase, generatedCode);
		
		if(hasInserted) {
			generatedCode.setStatus(1);
			session.save(generatedCode);
			responseBuilder.add("status", "true");
		}else {
			responseBuilder.add("status", "false");
		}
		
		return responseBuilder.build().toString();
	}
	
}
