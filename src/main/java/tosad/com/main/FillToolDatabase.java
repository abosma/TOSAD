package tosad.com.main;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;

import tosad.com.model.BusinessRuleType;
import tosad.com.model.Operator;
import tosad.com.model.RuleTemplate;
import tosad.com.model.TargetDatabase;
import tosad.com.model.TargetDatabaseType;
import tosad.com.model.Trigger;
import tosad.com.model.util.HibernateUtil;

public class FillToolDatabase {

	private static Session entityManager;

	/**
	 *getExistingOrPersistNew
	 *
	 * checks if the given databseObject exists in the database. If the given
	 * object exists, return the found object. If there are multiple instances
	 * found, return null, because no unique object was found for the given
	 * request If no objects are found. the given object will be persisted and
	 * returned
	 *
	 * @param tObject the database object to find
	 * @param entityManager the entity manager
	 * @return tObject if no replicas are found in the database, the replicate if
	 *        one object is found, null if multiple objects were found
	 */
	public static <T> T getExistingOrPersistNew(T tObject) {

		Example example = Example.create(tObject);

		@SuppressWarnings("deprecation")
		Criteria criteria = entityManager.createCriteria(example.getClass()).add(example);

		@SuppressWarnings("unchecked")
		List<T> results = criteria.list();

		if (results.size() < 1) {
			System.out.println("Persisting object: " + tObject.toString());
			entityManager.beginTransaction();
			entityManager.persist(tObject);
			entityManager.getTransaction().commit();
			return tObject;
		} else if (results.size() > 1) {
			System.err.println("[getExistingOrPersistNew] More than one object found! Returning null.");
			return null;
		}

		return results.get(0);
	}

	/**
	 *Retrieves the contents of a given file
	 *
	 *@param templateFile the file to scan
	 *@return file contents if the file was readable, else null
	 */
	public static String retrieveFileContent(File templateFile) {
		if (!(templateFile.exists() && templateFile.isFile())){
			try {
				templateFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

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
		
		if( result.trim().equals(new String()))
			return null;
		
		return result;
	}

	private static RuleTemplate registerTemplate(File sourceFile, String name, TargetDatabaseType databaseType){
		String templateBody = retrieveFileContent(sourceFile);

		if( templateBody == null)
			return null;
		
		RuleTemplate ruleTemplate = new RuleTemplate();
		ruleTemplate.setName(name);
		ruleTemplate.setTargetDatabaseType(databaseType);
		ruleTemplate.setTemplate(templateBody);

		return ruleTemplate;

	}
	
	public static BusinessRuleType newBusinessRuleType(String name, String code, Operator[] operators){
		BusinessRuleType businessRuleType = new BusinessRuleType();
		businessRuleType.setName(name);
		businessRuleType.setCode(code);
		
		for( Operator operator : operators ){
			businessRuleType.addOperator(operator);
		}
		
		return businessRuleType;
	}
	
	public static Operator newOperator(String name, String code, String amountOfValues){
		Operator operator = new Operator();
		operator.setName(name);
		operator.setCode(code);
		operator.setAmountOfValues(amountOfValues);
		
		return operator;
	}
	
	public static void main(String[] args) {
				
		// create session
		entityManager = HibernateUtil.getSession();
	
		//TARGET DASTABASE TYPES
		 
		TargetDatabaseType targetDatabaseType = new TargetDatabaseType();
		targetDatabaseType.setName("Oracle");

		targetDatabaseType = getExistingOrPersistNew(targetDatabaseType);

		//targetDatabase = getExistingOrPersistNew(targetDatabase);
		//* Operator
		Operator operatorBT		 = newOperator("Between",				"operator_between",					"double");
		Operator operatorNBT	 = newOperator("Not Between",			"operator_not_between",				"double");
		Operator operatorIN		 = newOperator("In",					"operator_in",						"multiple");
		Operator operatorNIN	 = newOperator("Not In",				"operator_not_in",					"multiple");
		Operator operatorEQ		 = newOperator("Equals",				"operator_equals",					"single");
		Operator operatorNEQ	 = newOperator("Not Equals",			"operator_not_equals",				"single");
		Operator operatorLT		 = newOperator("Less Than",				"operator_less_than",				"single");
		Operator operatorGT		 = newOperator("Greater Than",			"operator_greater_than",			"single");
		Operator operatorLTE	 = newOperator("Less Than Or Equal",	"operator_less_than_or_equal",		"single");
		Operator operatorGTE	 = newOperator("Greater Than Or Equal",	"operator_greater_than_or_equal",	"single");
		
		operatorBT	= getExistingOrPersistNew(operatorBT);
		operatorNBT	= getExistingOrPersistNew(operatorNBT);
		operatorIN	= getExistingOrPersistNew(operatorIN);
		operatorNIN	= getExistingOrPersistNew(operatorNIN);
		operatorEQ	= getExistingOrPersistNew(operatorEQ);
		operatorNEQ	= getExistingOrPersistNew(operatorNEQ);
		operatorLT	= getExistingOrPersistNew(operatorLT);
		operatorGT	= getExistingOrPersistNew(operatorGT);
		operatorLTE	= getExistingOrPersistNew(operatorLTE);
		operatorGTE	= getExistingOrPersistNew(operatorGTE);
		
		Operator[] operALL	= new Operator[] {operatorBT,operatorNBT,operatorIN,operatorNIN,operatorEQ,operatorNEQ,operatorLT,operatorGT,operatorLTE,operatorGTE};

		Operator[] opersRNG = new Operator[] {operatorBT, operatorNBT};
		Operator[] opersCMP = new Operator[] {operatorEQ, operatorNEQ, operatorGT, operatorGTE, operatorLT, operatorLTE};
		Operator[] opersLST = new Operator[] {operatorIN, operatorNIN};
		Operator[] opersOTH = new Operator[] {};
		
		//BusinessRuleType 
		BusinessRuleType attr_rng_rule		= newBusinessRuleType("Attribute Range Rule",		"attribute_range_rule",			opersRNG);
		BusinessRuleType attr_cmp_rule		= newBusinessRuleType("Attribute Compare Rule",		"attribute_compare_rule",		opersCMP);
		BusinessRuleType attr_lst_rul		= newBusinessRuleType("Attribute List Rule",		"attribute_list_rule",			opersLST);
		BusinessRuleType attr_oth_rule		= newBusinessRuleType("Attribute Other Rule",		"attribute_other_rule",			opersOTH);
		BusinessRuleType tuple_cmp_rule 	= newBusinessRuleType("Tuple Compare Rule",			"tuple_compare_rule",			opersCMP);
		BusinessRuleType tuple_oth_rule 	= newBusinessRuleType("Tuple Compare Rule",			"tuple_other_rule",				opersOTH);
		BusinessRuleType int_ent_cmp_rule	= newBusinessRuleType("Inter Entity Compare Rule",	"inter_entity_compare_rule"	,	opersCMP);
		BusinessRuleType ent_oth_rule 		= newBusinessRuleType("Entity Other Rule",			"entity_other_rule", 			opersOTH);
		BusinessRuleType modif_rule 		= newBusinessRuleType("Modify Rule", 				"modify_rule", 					new Operator[]{});
		
		attr_rng_rule		= getExistingOrPersistNew(attr_rng_rule);
		attr_cmp_rule		= getExistingOrPersistNew(attr_cmp_rule);
		attr_lst_rul		= getExistingOrPersistNew(attr_lst_rul);
		attr_oth_rule		= getExistingOrPersistNew(attr_oth_rule);
		tuple_cmp_rule		= getExistingOrPersistNew(tuple_cmp_rule);
		tuple_oth_rule		= getExistingOrPersistNew(tuple_oth_rule);
		int_ent_cmp_rule	= getExistingOrPersistNew(int_ent_cmp_rule);
		ent_oth_rule		= getExistingOrPersistNew(ent_oth_rule);
		modif_rule			= getExistingOrPersistNew(modif_rule);

		BusinessRuleType[] brtALL = new BusinessRuleType[]{attr_rng_rule,attr_cmp_rule,attr_lst_rul,attr_oth_rule,tuple_cmp_rule,tuple_oth_rule,int_ent_cmp_rule,ent_oth_rule,modif_rule};

		//* TargetDatabase
		TargetDatabase targetDatabase = new TargetDatabase();
		targetDatabase.setConnection("ondora02.hu.nl:8521/cursus02.hu.nl");
		targetDatabase.setName("HU_Target_DB");
		targetDatabase.setPassword("tosad_2017_2a_team1_target");
		targetDatabase.setUsername("tosad_2017_2a_team1_target");
		targetDatabase.setTargetDatabaseType(targetDatabaseType);

		targetDatabase = getExistingOrPersistNew(targetDatabase);

		//* Trigger
		String[] trigger_levels = {"Before", "After"};
		String[] e = {"Insert", "Update", "Delete"};
		
		List<Trigger> triggers = new ArrayList<Trigger>();
		for(int i = 0; i < trigger_levels.length; i++){
			String level = trigger_levels[i];
			for(int j = 0; j < e.length; j++){
				String eType = e[j];
				
				Trigger _trigger = new Trigger();
				_trigger.setExecutionLevel(level.toUpperCase());
				_trigger.setExecutionLevelTranslations(level);
				_trigger.setExecutionType(eType.toUpperCase());
				_trigger.setExecutionTypeTranslations(eType);
				
				triggers.add(_trigger);
				
				String thisAndNextType = String.format("%s:%s", e[j], e[j%e.length]);
				
				Trigger _triggerAB = new Trigger();
				_triggerAB.setExecutionLevel(level.toUpperCase());
				_triggerAB.setExecutionLevelTranslations(level);
				_triggerAB.setExecutionType(thisAndNextType.toUpperCase());
				_triggerAB.setExecutionTypeTranslations(thisAndNextType);
				
				triggers.add(_triggerAB);
			}
			
			Trigger _trigger = new Trigger();
			_trigger.setExecutionLevel(level.toUpperCase());
			_trigger.setExecutionLevelTranslations(level);
			_trigger.setExecutionType(String.format("%s:%s:%s", e[0], e[1], e[2]).toUpperCase());
			_trigger.setExecutionTypeTranslations(String.format("%s, %s, %s", e[0], e[1], e[2]));
			
			triggers.add(_trigger);
		}

		for (Trigger t : triggers) {
			t = getExistingOrPersistNew(t);
		}
		
		ArrayList<RuleTemplate> ruleTemplates = new ArrayList<RuleTemplate>();
		
		RuleTemplate ruleTemplateOracleTrigger = registerTemplate( new File("templates/oracle/trigger.template"), "trigger", targetDatabaseType);
		ruleTemplateOracleTrigger = getExistingOrPersistNew(ruleTemplateOracleTrigger);
		ruleTemplates.add(ruleTemplateOracleTrigger);
		
		// ALL BUSINESS RULE TYPES
		for (BusinessRuleType o : brtALL) {
			
			String v = o.getCode();
		
			for( String prefix : new String[]{"", "trigger_"}){
				RuleTemplate ruleTemplate = registerTemplate(new File(String.format("templates/oracle/%s.template", prefix + v)), v, targetDatabaseType);
				if(ruleTemplate == null)
					continue;
				
				ruleTemplate = getExistingOrPersistNew(ruleTemplate);
				ruleTemplates.add(ruleTemplate);
			}
		}

		// ALL OPERATORS
		for( Operator o : operALL){
			String v = o.getCode();
			RuleTemplate ruleTemplate = registerTemplate(new File(String.format("templates/oracle/%s.template", v)), v, targetDatabaseType);
			if(ruleTemplate == null)
				continue;
			
			ruleTemplate = getExistingOrPersistNew(ruleTemplate);
			ruleTemplates.add(ruleTemplate);
		}

		for(String s : new String[]{"string","number","table","column"}){
			RuleTemplate ruleTemplateOracleString = registerTemplate( new File(String.format("templates/oracle/%s.template", s)), s, targetDatabaseType);
			
			if(ruleTemplateOracleString == null)
				continue;
			
			ruleTemplateOracleString = getExistingOrPersistNew(ruleTemplateOracleString);
			ruleTemplates.add(ruleTemplateOracleString);
		}
		
		
	/*	//Compare values
		CompareValue compareValue1 = new CompareValue();
		compareValue1.setValue("1");

		CompareValue compareValue2 = new CompareValue();
		compareValue2.setValue("10");

		
		//BusinessRule
		 

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

		
		//ORACLE TEMPLATES
		 
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
		*/
		entityManager.close();
	}
}
