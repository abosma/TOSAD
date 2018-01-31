package tosad.com.main;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;

import tosad.com.generator.Generator;
import tosad.com.generator.GeneratorInterface;
import tosad.com.generator.exception.GenerationException;
import tosad.com.generator.exception.SQLFormatException;
import tosad.com.generator.exception.TemplateNotFoundException;
import tosad.com.model.BusinessRule;
import tosad.com.model.BusinessRuleType;
import tosad.com.model.CompareValue;
import tosad.com.model.Constraint;
import tosad.com.model.Operator;
import tosad.com.model.RuleTemplate;
import tosad.com.model.TargetDatabase;
import tosad.com.model.TargetDatabaseType;
import tosad.com.model.Trigger;
import tosad.com.model.enums.Amount;
import tosad.com.model.enums.ValueType;
import tosad.com.model.util.HibernateUtil;

public class FillToolDatabase {

	private final static boolean ONLINE = false;
	
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
		
		if( ! ONLINE ){
			//System.out.println(String.format("FAKED PERISTENCE OF %s", tObject.toString()));
			return tObject;
		}
			
		
		Example example = Example.create(tObject);

		@SuppressWarnings("deprecation")
		Criteria criteria = entityManager.createCriteria(example.getClass()).add(example);

		@SuppressWarnings("unchecked")
		List<T> results = criteria.list();

		if (results.size() < 1) {
			//System.out.println("Persisting object: " + tObject.toString());
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
		
		if( result.trim().equals(new String()))return null;
		
		return result;
	}

	private static RuleTemplate registerTemplate(File sourceFile, String name, TargetDatabaseType databaseType){
		String templateBody = retrieveFileContent(sourceFile);

		/*if( templateBody == null)
			return null;
		*/
		RuleTemplate ruleTemplate = new RuleTemplate();
		ruleTemplate.setName(name);
		ruleTemplate.setTargetDatabaseType(databaseType);
		ruleTemplate.setTemplate(templateBody);

		return ruleTemplate;

	}
	
	private static BusinessRuleType newBusinessRuleType(String name, String code, Operator[] operators, Set<ValueType> hashSet){
		BusinessRuleType businessRuleType = new BusinessRuleType();
		businessRuleType.setName(name);
		businessRuleType.setCode(code);
		businessRuleType.setAllowedValueTypes(hashSet);
		
		for( Operator operator : operators ){
			businessRuleType.addOperator(operator);
		}
		
		return businessRuleType;
	}
	
	private static BusinessRule newBusinessRule(
			String name, 
			BusinessRuleType businessRuleType, 
			Operator operator, 
			String referencedColumn, 
			String referencedTable, 
			Trigger trigger, 
			TargetDatabase targetDatabase)
	{
		BusinessRule businessRule = new BusinessRule();
		businessRule.setName(name);
		businessRule.setBusinessRuleType(businessRuleType);
		businessRule.setOperator(operator);
		businessRule.setReferencedColumn(referencedColumn);
		businessRule.setReferencedTable(referencedTable);
		businessRule.setTrigger(trigger);
		businessRule.setTargetDatabase(targetDatabase);
		businessRule.setExample(String.format("example for %s", name));
		businessRule.setExplanation(String.format("explanation for %s", name));
		
		return businessRule;
	}
	
	public static Operator newOperator(String name, String code, Amount amount){
		Operator operator = new Operator();
		operator.setName(name);
		operator.setCode(code);
		operator.setAmountOfValues(amount);
		
		return operator;
	}
	
	private static Set<CompareValue> getCompareValuesFor(BusinessRule businessRule, ValueType valueType, Amount amount) {
		Set<CompareValue> compareValues = new HashSet<CompareValue>();
		
		int iterations = amount == Amount.SINGLE  ? 1 : amount == Amount.DOUBLE ? 2 : 3;
		for(int i = 0; i < iterations; i++){
			CompareValue compareValue = new CompareValue();
			compareValue.setBusinessRule(businessRule);
			compareValue.setOrder(i+1);
			if( ValueType.STATIC_STRING == valueType ){
				compareValue.setValue(String.format("literal_%d", i));
			} else if(ValueType.STATIC_NUMBER == valueType ){
				compareValue.setValue(""+i*3);
			} else {
				compareValue.setTable(ValueType.TUPLE == valueType ? businessRule.getReferencedTable() : "OTHER_TABLE");
				compareValue.setColumn(ValueType.ENTITY == valueType ? "EXTERNAL_COLUMN" : "OTHER_COLUMN");
			}
			compareValues.add(compareValue);
			//System.out.println(compareValue);
		}
		return compareValues;
	}

	public static void main(String[] args) {
				
		// create session
		if(ONLINE)entityManager = HibernateUtil.getSession();
	
		//TARGET DASTABASE TYPES
		 
		TargetDatabaseType targetDatabaseTypeOracle = new TargetDatabaseType();
		targetDatabaseTypeOracle.setName("Oracle");

		targetDatabaseTypeOracle = getExistingOrPersistNew(targetDatabaseTypeOracle);

		//targetDatabase = getExistingOrPersistNew(targetDatabase);
		//* Operator
		Operator operatorBT		 = newOperator("Between",				"operator_between",					Amount.DOUBLE);
		Operator operatorNBT	 = newOperator("Not Between",			"operator_not_between",				Amount.DOUBLE);
		Operator operatorIN		 = newOperator("In",					"operator_in",						Amount.MULTIPLE);
		Operator operatorNIN	 = newOperator("Not In",				"operator_not_in",					Amount.MULTIPLE);
		Operator operatorEQ		 = newOperator("Equals",				"operator_equals",					Amount.SINGLE);
		Operator operatorNEQ	 = newOperator("Not Equals",			"operator_not_equals",				Amount.SINGLE);
		Operator operatorLT		 = newOperator("Less Than",				"operator_less_than",				Amount.SINGLE);
		Operator operatorGT		 = newOperator("Greater Than",			"operator_greater_than",			Amount.SINGLE);
		Operator operatorLTE	 = newOperator("Less Than Or Equal",	"operator_less_than_or_equal",		Amount.SINGLE);
		Operator operatorGTE	 = newOperator("Greater Than Or Equal",	"operator_greater_than_or_equal",	Amount.SINGLE);
		
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
		
		Operator[] operALL	= new Operator[] {operatorBT, operatorNBT, operatorIN, operatorNIN, operatorEQ, operatorNEQ, operatorLT, operatorGT, operatorLTE, operatorGTE};

		Operator[] opersRNG = new Operator[] {operatorBT, operatorNBT};
		Operator[] opersCMP = new Operator[] {operatorEQ, operatorNEQ, operatorGT, operatorGTE, operatorLT, operatorLTE};
		Operator[] opersLST = new Operator[] {operatorIN, operatorNIN};
		Operator[] opersOTH = new Operator[] {};
		
		//BusinessRuleType 
		BusinessRuleType attr_rng_rule		= newBusinessRuleType("Attribute Range Rule",		"attribute_range_rule",			opersRNG,			new HashSet<ValueType>(Arrays.asList(ValueType.STATIC_NUMBER)));
		BusinessRuleType attr_cmp_rule		= newBusinessRuleType("Attribute Compare Rule",		"attribute_compare_rule",		opersCMP,			new HashSet<ValueType>(Arrays.asList(ValueType.STATIC_NUMBER)));
		BusinessRuleType attr_lst_rul		= newBusinessRuleType("Attribute List Rule",		"attribute_list_rule",			opersLST,			new HashSet<ValueType>(Arrays.asList(ValueType.STATIC_NUMBER, ValueType.STATIC_STRING)));
		BusinessRuleType attr_oth_rule		= newBusinessRuleType("Attribute Other Rule",		"attribute_other_rule",			opersOTH,			new HashSet<ValueType>(Arrays.asList(ValueType.STATIC_NUMBER, ValueType.STATIC_STRING)));
		BusinessRuleType tuple_cmp_rule 	= newBusinessRuleType("Tuple Compare Rule",			"tuple_compare_rule",			opersCMP,			new HashSet<ValueType>(Arrays.asList(ValueType.TUPLE)));
		BusinessRuleType tuple_oth_rule 	= newBusinessRuleType("Tuple Compare Rule",			"tuple_other_rule",				opersOTH,			new HashSet<ValueType>(Arrays.asList(ValueType.TUPLE)));
		BusinessRuleType int_ent_cmp_rule	= newBusinessRuleType("Inter Entity Compare Rule",	"inter_entity_compare_rule"	,	opersCMP,			new HashSet<ValueType>(Arrays.asList(ValueType.ENTITY)));
		BusinessRuleType ent_oth_rule 		= newBusinessRuleType("Entity Other Rule",			"entity_other_rule", 			opersOTH,			new HashSet<ValueType>(Arrays.asList(ValueType.ENTITY)));
		BusinessRuleType modif_rule 		= newBusinessRuleType("Modify Rule", 				"modify_rule", 					new Operator[]{},	new HashSet<ValueType>(Arrays.asList(ValueType.ENTITY)));
		
		attr_rng_rule		= getExistingOrPersistNew(attr_rng_rule);
		attr_cmp_rule		= getExistingOrPersistNew(attr_cmp_rule);
		attr_lst_rul		= getExistingOrPersistNew(attr_lst_rul);
		attr_oth_rule		= getExistingOrPersistNew(attr_oth_rule);
		tuple_cmp_rule		= getExistingOrPersistNew(tuple_cmp_rule);
		tuple_oth_rule		= getExistingOrPersistNew(tuple_oth_rule);
		int_ent_cmp_rule	= getExistingOrPersistNew(int_ent_cmp_rule);
		ent_oth_rule		= getExistingOrPersistNew(ent_oth_rule);
		modif_rule			= getExistingOrPersistNew(modif_rule);

		BusinessRuleType[] allBusinessRuleTypes = new BusinessRuleType[]{attr_rng_rule,attr_cmp_rule,attr_lst_rul,attr_oth_rule,tuple_cmp_rule,tuple_oth_rule,int_ent_cmp_rule,ent_oth_rule,modif_rule};

		//* TargetDatabase
		TargetDatabase targetDatabase = new TargetDatabase();
		targetDatabase.setConnection("ondora02.hu.nl:8521/cursus02.hu.nl");
		targetDatabase.setName("HU_Target_DB");
		targetDatabase.setPassword("tosad_2017_2a_team1_target");
		targetDatabase.setUsername("tosad_2017_2a_team1_target");
		targetDatabase.setTargetDatabaseType(targetDatabaseTypeOracle);

		targetDatabase = getExistingOrPersistNew(targetDatabase);

		//* Trigger
		String[] trigger_levels = {"Before", "After"};
		String[] e = {"Insert", "Update", "Delete"};
		
		List<Trigger> allTriggers = new ArrayList<Trigger>();
		for(int i = 0; i < trigger_levels.length; i++){
			String level = trigger_levels[i];
			for(int j = 0; j < e.length; j++){
				String eType = e[j];
				
				Trigger _trigger = new Trigger();
				_trigger.setExecutionLevel(level.toUpperCase());
				_trigger.setExecutionLevelTranslations(level);
				_trigger.setExecutionType(eType.toUpperCase());
				_trigger.setExecutionTypeTranslations(eType);
				
				allTriggers.add(_trigger);
				
				String thisAndNextType = String.format("%s:%s", e[j], e[j%e.length]);
				
				Trigger _triggerAB = new Trigger();
				_triggerAB.setExecutionLevel(level.toUpperCase());
				_triggerAB.setExecutionLevelTranslations(level);
				_triggerAB.setExecutionType(thisAndNextType.toUpperCase());
				_triggerAB.setExecutionTypeTranslations(thisAndNextType);
				
				allTriggers.add(_triggerAB);
			}
			
			Trigger _trigger = new Trigger();
			_trigger.setExecutionLevel(level.toUpperCase());
			_trigger.setExecutionLevelTranslations(level);
			_trigger.setExecutionType(String.format("%s:%s:%s", e[0], e[1], e[2]).toUpperCase());
			_trigger.setExecutionTypeTranslations(String.format("%s, %s, %s", e[0], e[1], e[2]));
			
			allTriggers.add(_trigger);
		}

		for (Trigger t : allTriggers) {
			t = getExistingOrPersistNew(t);
		}
		
		ArrayList<RuleTemplate> ruleTemplates = new ArrayList<RuleTemplate>();
		
		RuleTemplate ruleTemplateOracleTrigger = registerTemplate( new File("templates/oracle/trigger.template"), "trigger", targetDatabaseTypeOracle);
		ruleTemplateOracleTrigger = getExistingOrPersistNew(ruleTemplateOracleTrigger);
		ruleTemplates.add(ruleTemplateOracleTrigger);
		
		// ALL BUSINESS RULE TYPES
		for (BusinessRuleType o : allBusinessRuleTypes) {
			
			String v = o.getCode();
			
			String[] prefixes = new String[]{"", "trigger_"};
			
			for( String prefix : prefixes){
				String name = prefix + v;
				String fileLocation = String.format( "templates/oracle/%s.template", name );
				File theFile = new File(fileLocation);
				
				RuleTemplate ruleTemplate = registerTemplate(theFile, name, targetDatabaseTypeOracle);
				if(ruleTemplate == null)
					continue;
				
				ruleTemplate = getExistingOrPersistNew(ruleTemplate);
				ruleTemplates.add(ruleTemplate);
			}
		}

		// ALL OPERATORS
		for( Operator o : operALL){
			String v = o.getCode();
			RuleTemplate ruleTemplate = registerTemplate(new File(String.format("templates/oracle/%s.template", v)), v, targetDatabaseTypeOracle);
			if(ruleTemplate == null)
				continue;
			
			ruleTemplate = getExistingOrPersistNew(ruleTemplate);
			ruleTemplates.add(ruleTemplate);
		}

		/* register formats */
		String[] formats = new String[]{"string","number","table","column"};
		for(String s : formats){
			RuleTemplate ruleTemplateOracleString = registerTemplate( new File(String.format("templates/oracle/%s.template", s)), String.format("%s_format", s), targetDatabaseTypeOracle);
			
			if(ruleTemplateOracleString == null)
				continue;
			
			ruleTemplateOracleString = getExistingOrPersistNew(ruleTemplateOracleString);
			ruleTemplates.add(ruleTemplateOracleString);
		}
	
		for (RuleTemplate ruleTemplate : ruleTemplates) {
			targetDatabaseTypeOracle.addTemplate(ruleTemplate);
		}
		
		if(ONLINE){
			entityManager.beginTransaction();
			entityManager.persist(targetDatabaseTypeOracle);
			entityManager.getTransaction().commit();
		}
		
		Constraint nullConstraint = new Constraint();
		nullConstraint.setId(0);
		nullConstraint = getExistingOrPersistNew(nullConstraint);
		
		Trigger trigger = new Trigger();
		trigger.setExecutionLevel("BEFORE");
		trigger.setExecutionType("INSERT;UPDATE;DELETE");
		
		trigger = getExistingOrPersistNew(trigger);
		
		List<BusinessRule> businessRules = new ArrayList<BusinessRule>();
		
		int businessRuleCounter = 0;
		for(BusinessRuleType businessRuleType : allBusinessRuleTypes){
			
			Set<ValueType> allowedTypes = businessRuleType.getAllowedValueTypes();
			
			for(ValueType valueType : allowedTypes){
			
				Set<Operator> operators = businessRuleType.getOperators();
				for (Operator operator : operators) {
					
					BusinessRule businessRule = newBusinessRule(
							String.format("BRT_%d", businessRuleCounter++), 
							businessRuleType, 
							operator, 
							"BR_COLUMN",
							"BR_TABLE",  
							trigger, 
							targetDatabase
					);
					businessRule = getExistingOrPersistNew(businessRule);

					Set<CompareValue> compareValues = getCompareValuesFor(businessRule, valueType, operator.getAmountOfValues());
					for (CompareValue compareValue : compareValues) {
						compareValue = getExistingOrPersistNew(compareValue);
						businessRule.addCompareValue(compareValue);
					}
					businessRules.add(businessRule);
				}
			}
		}
				
		GeneratorInterface generator = new Generator();
		for (BusinessRule businessRule : businessRules) {
			System.out.println(String.format("\n\n----- GENERATING FOR %s ------", businessRule.getName()));
			System.out.println(String.format("%s | %s", businessRule.getBusinessRuleType().getName(), businessRule.getOperator().getName()));
			for (CompareValue compareValue : businessRule.getCompareValues()) 
				System.out.println(compareValue.toString());
			System.out.println();
			
			try {
				String OSql = generator.generateSQL(businessRule);
				System.out.println(OSql);
			} catch (GenerationException e1) {
				e1.printStackTrace();
			} catch (TemplateNotFoundException e1) {
				e1.printStackTrace();
			} catch (SQLFormatException e1) {
				e1.printStackTrace();
			}
		}
		
		if(ONLINE)
			entityManager.close();
	}
}
