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

public class GeneratorTestSite {

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
	
	private static RuleTemplate registerTemplate(File sourceFile, String name, TargetDatabaseType databaseType){
		String templateBody = retrieveFileContent(sourceFile);

		RuleTemplate ruleTemplate = new RuleTemplate();
		ruleTemplate.setName(name);
		ruleTemplate.setTargetDatabaseType(databaseType);
		ruleTemplate.setTemplate(templateBody);

		return getExistingOrPersistNew(ruleTemplate);

	}

	public static void main(String[] args) {
		/*
		 * Name definitions
		 */
		final String ATTR_RANGE_RULE = "attribute_range_rule";
		final String ATTR_COMPARE_RULE = "attribute_compare_rule";

		// create session
		if (!LOCAL_TESTING)
			entityManager = HibernateUtil.getSession();

		/*
		 * TARGET DASTABASE TYPES
		 */
		TargetDatabaseType targetDatabaseTypeOracle = new TargetDatabaseType();
		targetDatabaseTypeOracle.setName("Oracle");

		getExistingOrPersistNew(targetDatabaseTypeOracle);

		/*
		 * BusinessRuleType
		 */
		BusinessRuleType businessRuleTypeARNG = new BusinessRuleType();
		businessRuleTypeARNG.setName(ATTR_COMPARE_RULE);

		businessRuleTypeARNG = getExistingOrPersistNew(businessRuleTypeARNG);

		/*
		 * BusinessRuleType
		 */
		BusinessRuleType businessRuleTypeCMP = new BusinessRuleType();
		businessRuleTypeCMP.setName(ATTR_COMPARE_RULE);

		businessRuleTypeCMP = getExistingOrPersistNew(businessRuleTypeCMP);

		/*
		 * TargetDatabase
		 */

		TargetDatabase targetDatabase = new TargetDatabase();
		targetDatabase.setConnection("ondora02.hu.nl:8521/cursus02.hu.nl");
		targetDatabase.setName("HU_Target_DB");
		targetDatabase.setPassword("tosad_2017_2a_team1_target");
		targetDatabase.setUsername("tosad_2017_2a_team1_target");
		targetDatabase.setTargetDatabaseType(targetDatabaseTypeOracle);

		targetDatabase = getExistingOrPersistNew(targetDatabase);

		/*
		 * Operator
		 */

		Operator oracleOperatorBetween = new Operator();
		oracleOperatorBetween.setName("operator_between");
		oracleOperatorBetween.setNumberOfValues(2);
		oracleOperatorBetween.setValue("operator_between");

		oracleOperatorBetween = getExistingOrPersistNew(oracleOperatorBetween);

		/*
		 * Operator
		 */

		Operator oracleOperatorLte = new Operator();
		oracleOperatorLte.setName("operator_lte");
		oracleOperatorLte.setNumberOfValues(2);
		oracleOperatorLte.setValue("operator_lte");

		oracleOperatorLte = getExistingOrPersistNew(oracleOperatorLte);

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

		BusinessRule businessRuleARNG = new BusinessRule();
		businessRuleARNG.setBusinessRuleType(businessRuleTypeCMP);
		businessRuleARNG.setErrorMessage(String.format("Waarde moet tussen %d en %d liggen", compareValue1.getValue(), compareValue2.getValue()));
		businessRuleARNG.setExample("BusinessRuleVoorbeeld");
		businessRuleARNG.setExplanation("Hier nog meer uitleg over de regel");
		businessRuleARNG.setName("BRG_TABLEX_ARNG_01");
		businessRuleARNG.setOperator(oracleOperatorBetween);
		businessRuleARNG.setReferencedColumn("score");
		businessRuleARNG.setReferencedTable("waarderingen");
		businessRuleARNG.setTargetDatabase(targetDatabase);
		businessRuleARNG.setTrigger(trigger);
		businessRuleARNG.addCompareValue(compareValue1);
		businessRuleARNG.addCompareValue(compareValue2);

		businessRuleARNG = getExistingOrPersistNew(businessRuleARNG);

		/*
		 * BusinessRule
		 */

		BusinessRule businessRuleCMP = new BusinessRule();
		businessRuleCMP.setBusinessRuleType(businessRuleTypeCMP);
		businessRuleCMP.setErrorMessage(String.format("Waarde moet kleiner dan %s zijn", compareValue2.getValue()));
		businessRuleCMP.setExample("BusinessRule Compare voorbeeld");
		businessRuleCMP.setExplanation("Hier nog meer uitleg over de regel");
		businessRuleCMP.setName("BRG_TABLEX_CMP_01");
		businessRuleCMP.setOperator(oracleOperatorLte);
		businessRuleCMP.setReferencedColumn("aantal");
		businessRuleCMP.setReferencedTable("producten");
		businessRuleCMP.setTargetDatabase(targetDatabase);
		businessRuleCMP.setTrigger(trigger);
		businessRuleCMP.addCompareValue(compareValue2);

		businessRuleCMP = getExistingOrPersistNew(businessRuleCMP);

		/*
		 * ORACLE TEMPLATES
		 */
		
		RuleTemplate ruleTemplateOracleTrigger = 
				registerTemplate(new File("templates/oracle/trigger.template"), 							"trigger", 						targetDatabaseTypeOracle);
		
		RuleTemplate ruleTemplateOracleAttributeRangeRule = 
				registerTemplate(new File("templates/oracle/" + ATTR_RANGE_RULE + ".template"), 			ATTR_RANGE_RULE, 				targetDatabaseTypeOracle);
		
		RuleTemplate ruleTemplateOracleAttributeCompareRule = 
				registerTemplate(new File("templates/oracle/" + ATTR_COMPARE_RULE + ".template"), 			ATTR_COMPARE_RULE, 				targetDatabaseTypeOracle);
		
		RuleTemplate ruleTemplateOracleAttributeRangeRuleTriggerBody = 
				registerTemplate(new File("templates/oracle/trigger_" + ATTR_RANGE_RULE + ".template"), 	"trigger_"+ATTR_RANGE_RULE, 	targetDatabaseTypeOracle);
		
		RuleTemplate ruleTemplateOracleAttributeCompareRuleTriggerBody = 
				registerTemplate(new File("templates/oracle/trigger_" + ATTR_COMPARE_RULE + ".template"), 	"trigger_"+ATTR_COMPARE_RULE, 	targetDatabaseTypeOracle);
		
		RuleTemplate ruleTemplateOracleOperatorBetween = 
				registerTemplate(new File("templates/oracle/operator_between.template"), 					"operator_between", 			targetDatabaseTypeOracle);
		
		RuleTemplate ruleTemplateOracleOperatorLTE = 
				registerTemplate(new File("templates/oracle/operator_lte.template"), 						"operator_lte", 				targetDatabaseTypeOracle);

		/*
		 * *** *** *** TEST CODE BELOW *** *** ***
		 */

		targetDatabaseTypeOracle.addTemplate(ruleTemplateOracleAttributeRangeRule);
		targetDatabaseTypeOracle.addTemplate(ruleTemplateOracleAttributeCompareRule);
		targetDatabaseTypeOracle.addTemplate(ruleTemplateOracleTrigger);
		targetDatabaseTypeOracle.addTemplate(ruleTemplateOracleAttributeRangeRuleTriggerBody);
		targetDatabaseTypeOracle.addTemplate(ruleTemplateOracleAttributeCompareRuleTriggerBody);
		targetDatabaseTypeOracle.addTemplate(ruleTemplateOracleOperatorBetween);
		targetDatabaseTypeOracle.addTemplate(ruleTemplateOracleOperatorLTE);

		GeneratorInterface generator = new Generator();
		String output = generator.generateSQL(businessRuleARNG);

		System.out.println("Generated output for attribute range rule: \n" + output);

		String outputCMP = generator.generateSQL(businessRuleCMP);
		System.out.println("\nGenerated output for attribute compare rule\n\n" + outputCMP);

		/*
		 * *** *** *** END OF TEST CODE *** *** ***
		 */

		if (!LOCAL_TESTING)
			entityManager.close();
	}
}
