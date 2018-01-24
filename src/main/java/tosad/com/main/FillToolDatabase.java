package tosad.com.main;

import java.io.File;
import java.io.FileReader;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;

import tosad.com.generator.Generator;
import tosad.com.generator.GeneratorInterface;
import tosad.com.hibernate.HibernateUtil;
import tosad.com.hibernate.model.BusinessRule;
import tosad.com.hibernate.model.BusinessRuleType;
import tosad.com.hibernate.model.CompareValue;
import tosad.com.hibernate.model.Operator;
import tosad.com.hibernate.model.RuleTemplate;
import tosad.com.hibernate.model.TargetDatabase;
import tosad.com.hibernate.model.TargetDatabaseType;
import tosad.com.hibernate.model.Trigger;

public class FillToolDatabase {

	public static final boolean LOCAL_TESTING = true;

	private static Session entityManager;

	/**
	 * getExistingOrPersistNew
	 * 
	 * checks if the given databseObject exists in the database. If the given
	 * object exists, return the found object. If there are multiple instances
	 * found, return null, because no unique object was found for the given
	 * request If no objects are found. the given object will be persisted and
	 * returned
	 * 
	 * @param tObject
	 *            the database object to find
	 * @param entityManager
	 *            the entitymanager
	 * @return tObject if no replicas are found in the database, the repliace if
	 *         one object is found, null if multiple objects are gound
	 */
	public static <T> T getExistingOrPersistNew(T tObject) {
		if (LOCAL_TESTING)
			return tObject;

		Example example = Example.create(tObject);

		@SuppressWarnings("deprecation")
		Criteria criteria = entityManager.createCriteria(example.getClass()).add(example);

		@SuppressWarnings("unchecked")
		List<T> results = criteria.list();

		if (results.isEmpty()) {
			entityManager.persist(tObject);

			return tObject;
		}
		if (results.size() > 1) {
			System.err.println("[getExistingOrPersistNew] More than one object found! Returning null.");
			return null;
		}

		return results.get(0);
	}

	/**
	 * Retrieves the contents of a given file
	 * 
	 * @param templateFile
	 *            the file to scan
	 * @return file contents if the file was readable, else null
	 */
	public static String retrieveFileContent(File templateFile) {
		if (!(templateFile.exists() && templateFile.isFile()))
			return null;

		String result = "";
		try {
			FileReader fileReader = new FileReader(templateFile);

			int iChr;
			while ((iChr = fileReader.read()) != -1)
				result += (char) iChr;

			fileReader.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}

	public static void main(String[] args) {
		/*
		 * Name definitions
		 */
		final String ATTR_RANGE_RULE = "attribute_range_rule";

		// create session
		if (!LOCAL_TESTING)
			entityManager = HibernateUtil.getSession();

		/*
		 * TARGET DASTABASE TYPES
		 */
		TargetDatabaseType targetDatabaseType = new TargetDatabaseType();
		targetDatabaseType.setName("Oracle");

		getExistingOrPersistNew(targetDatabaseType);

		/*
		 * BusinessRuleType
		 */
		BusinessRuleType businessRuleType = new BusinessRuleType();
		businessRuleType.setName(ATTR_RANGE_RULE);

		businessRuleType = getExistingOrPersistNew(businessRuleType);

		/*
		 * TargetDatabase
		 */

		TargetDatabase targetDatabase = new TargetDatabase();
		targetDatabase.setConnection("");
		targetDatabase.setName("MyOracleDb");
		targetDatabase.setPassword("P4$$w0rd");
		targetDatabase.setUsername("Username");
		targetDatabase.setTargetDatabaseType(targetDatabaseType);

		targetDatabase = getExistingOrPersistNew(targetDatabase);

		/*
		 * Operator
		 */

		Operator operator = new Operator();
		operator.setName("operator_between");
		operator.setNumberOfValues(2);
		operator.setValue("operator_between");

		operator = getExistingOrPersistNew(operator);

		/*
		 * Trigger
		 */

		Trigger trigger = new Trigger();
		trigger.setExecutionLevel("BEFORE");
		trigger.setExecutionLevelTranslations("Before");

		trigger.setExecutionType("INSERT");
		trigger.setExecutionTypeTranslations("Insert");

		trigger.setType("Before insert");
		trigger.setTypeTranslation("Before Insert");

		trigger = getExistingOrPersistNew(trigger);

		/*
		 * Compare values
		 */

		CompareValue compareValue1 = new CompareValue();
		compareValue1.setValue("1");

		CompareValue compareValue2 = new CompareValue();
		compareValue2.setValue("10");

		/*
		 * BusinessRule
		 */

		BusinessRule businessRule = new BusinessRule();
		businessRule.setBusinessRuleType(businessRuleType);
		businessRule.setErrorMessage("Waarde moet tussen x en y liggen");
		businessRule.setExample("BusinessRuleVoorbeeld");
		businessRule.setExplanation("Hier nog meer uitleg over de regel");
		businessRule.setName("BRG_TABLEX_ARNG_01");
		businessRule.setOperator(operator);
		businessRule.setReferencedColumn("COLUMN-Y");
		businessRule.setReferencedTable("TABLE-X");
		businessRule.setTargetDatabase(targetDatabase);
		businessRule.setTrigger(trigger);
		businessRule.addCompareValue(compareValue1);
		businessRule.addCompareValue(compareValue2);

		businessRule = getExistingOrPersistNew(businessRule);

		/*
		 * ORACLE TEMPLATES
		 */
		// Generic Trigger Template
		String fileTriggerTemplate = retrieveFileContent(new File("templates/oracle/trigger.template"));

		RuleTemplate templateOracleTrigger = new RuleTemplate();
		templateOracleTrigger.setName("trigger");
		templateOracleTrigger.setTargetDatabaseType(targetDatabaseType);
		templateOracleTrigger.setTemplate(fileTriggerTemplate);

		templateOracleTrigger = getExistingOrPersistNew(templateOracleTrigger);

		// Attribute range rule
		String fileOracleARNGTemplate = retrieveFileContent(new File("templates/oracle/trigger.template"));

		RuleTemplate templateOracleARNG = new RuleTemplate();
		templateOracleARNG.setName(ATTR_RANGE_RULE);
		templateOracleARNG.setTargetDatabaseType(targetDatabaseType);
		templateOracleARNG.setTemplate(fileOracleARNGTemplate);

		templateOracleARNG = getExistingOrPersistNew(templateOracleARNG);

		
		
		/*
		 * *** *** *** TEST CODE BELOW *** *** ***
		 */
		
		targetDatabaseType.addTemplate(templateOracleARNG);
		targetDatabaseType.addTemplate(templateOracleTrigger);
		
		GeneratorInterface generator = new Generator();
		String output = generator.generateSQL(businessRule);

		System.out.println("GENERATED OUPUT: \n" + output);

		/*
		 * *** *** *** END OF TEST CODE *** *** ***
		 */

		if (!LOCAL_TESTING)
			entityManager.close();
	}
}
