package tosad.com.main;

import java.io.File;
import java.io.FileReader;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;

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

	public static <T> T getExistingOrPersistNew(T tObject, Session entityManager) {
		Example example = Example.create(tObject);
		
		@SuppressWarnings("deprecation")
		Criteria criteria = entityManager.createCriteria(example.getClass()).add(example);
		
		@SuppressWarnings("unchecked")
		List<T> results = criteria.list();

		if (results.isEmpty()) {
			entityManager.persist(tObject);

			return tObject;
		}
		if (results.size() > 1){
			System.err.println("[getExistingOrPersistNew] More than one object found! Returning null.");
			return null;
		}

		return results.get(0);
	}

	public static String retrieveTemplate(File template) {
		if (!(template.exists() && template.isFile()))
			return null;

		String result = "";
		try {
			FileReader fileReader = new FileReader(template);

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
		Session entityManager = HibernateUtil.getSession();

		/*
		 * TARGET DASTABASE TYPES
		 */
		TargetDatabaseType targetDatabaseType = new TargetDatabaseType();
		targetDatabaseType.setName("Oracle");
		
		getExistingOrPersistNew(targetDatabaseType, entityManager);

		/*
		 * BusinessRuleType
		 */
		BusinessRuleType businessRuleType = new BusinessRuleType();
		businessRuleType.setName(ATTR_RANGE_RULE);
		
		businessRuleType = getExistingOrPersistNew(businessRuleType, entityManager);
		

		/*
		 * TargetDatabase		
		 */
		
		TargetDatabase targetDatabase = new TargetDatabase();
		targetDatabase.setConnection("");
		targetDatabase.setName("MyOracleDb");
		targetDatabase.setPassword("P4$$w0rd");
		targetDatabase.setUsername("Username");
		targetDatabase.setTargetDatabaseType(targetDatabaseType);
		
		targetDatabase = getExistingOrPersistNew(targetDatabase, entityManager);
		
		/*
		 * Operator 
		 */
		
		Operator operator = new Operator();
		operator.setName("operator_between");
		operator.setNumberOfValues(2);
		operator.setValue("operator_between");
		
		operator = getExistingOrPersistNew(operator, entityManager);
		
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
		
		trigger = getExistingOrPersistNew(trigger, entityManager);
		
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
		
		businessRule = getExistingOrPersistNew(businessRule, entityManager);
		
		System.out.println(businessRule);
		
		/*
		 * ORACLE TEMPLATES
		 */
		// Generic Trigger Template
		String fileTriggerTemplate = retrieveTemplate(new File("templates/oracle/trigger.template"));

		RuleTemplate templateOracleTrigger = new RuleTemplate();
		templateOracleTrigger.setName("trigger");
		templateOracleTrigger.setTargetDatabaseType(targetDatabaseType);
		templateOracleTrigger.setTemplate(fileTriggerTemplate);

		templateOracleTrigger = getExistingOrPersistNew(templateOracleTrigger, entityManager);
		
		// Attribute range rule
		String fileOracleARNGTemplate = retrieveTemplate(new File("templates/oracle/trigger.template"));
		
		RuleTemplate templateOracleARNG = new RuleTemplate();
		templateOracleARNG.setName(ATTR_RANGE_RULE);
		templateOracleARNG.setTargetDatabaseType(targetDatabaseType);
		templateOracleARNG.setTemplate(fileOracleARNGTemplate);

		templateOracleARNG = getExistingOrPersistNew(templateOracleARNG, entityManager);
		
		entityManager.close();

	}

}
