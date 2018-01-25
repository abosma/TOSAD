package tosad.com.generator;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tosad.com.model.BusinessRule;
import tosad.com.model.CompareValue;
import tosad.com.model.RuleTemplate;
import tosad.com.model.TargetDatabaseType;

public class Generator implements GeneratorInterface {

	private final String PATTERN_GENERIC = "\\{%s\\}";
	private final String PATTERN_BODY_GENERIC = ".*?";
	private String triggerOrConstraint = "";
	public Generator() {

	}

	@Override
	public String generateSQL(BusinessRule businessRule) {
		String result = "";

		if (businessRule.getTrigger() != null) {
			triggerOrConstraint = "trigger";
			result = generateTriggerCode(businessRule);
		} else if (businessRule.getConstraint() != null) {
			result = "NOT IMPLEMENTED";
		} else {
			result = "NO TRIGGER NOR CONSTRAINT FOUND!";
		}

		return result;
	}

	private RuleTemplate findTemplateByName(TargetDatabaseType databaseType, String templateName) {
		for (RuleTemplate ruleTemplate : databaseType.getTemplates()) {
			if (ruleTemplate.getName().equals(templateName)) {
				return ruleTemplate;
			}
		}
		return null;
	}

	private String genericParameterDecoder(String parameter, BusinessRule businessRule) {
		switch (parameter) {
		case "trigger_identifier":
			return generateRuleIdentifier(businessRule);
		case "trigger_execution":
			return generateTriggerExecution(businessRule);
		case "trigger_types":
			return generateTriggerTypes(businessRule);
		case "table_name":
			return sqlTabellic(businessRule.getReferencedTable());
		case "condition":
			return retrieveTriggerConditionTemplate(businessRule);
		case "error_text":
			return sqlString(businessRule.getErrorMessage());
		case "condition_sub_template":
			return retrieveTriggerConditionSubTemplate(businessRule);
		case "column_name":
			return sqlTabellic(businessRule.getReferencedColumn());
		case "value_0":
			return retrieveCompareValue(businessRule, 0);
		case "value_1":
			return retrieveCompareValue(businessRule, 1);
		case "operator":
			return findTemplateByName(businessRule.getTargetDatabase().getTargetDatabaseType(), businessRule.getOperator().getValue()).getTemplate();
		default:
			return parameter;
		}
	}

	private String retrieveCompareValue(BusinessRule businessRule, int i) {
		int counter = 0;
		for(CompareValue cv : ((HashSet<CompareValue>)businessRule.getCompareValues())){
			if( counter++ == i)
				return cv.getValue();
		}
		return "unkownValue";
	}

	private String generateTriggerTypes(BusinessRule businessRule) {
		return businessRule.getTrigger().getExecutionType();
	}

	private String generateTriggerExecution(BusinessRule businessRule) {
		return businessRule.getTrigger().getExecutionLevel();
	}

	private String generateRuleIdentifier(BusinessRule businessRule) {
		return String.format("BRG_%S_%S_%d", businessRule.getReferencedTable().toUpperCase(), businessRule.getBusinessRuleType().getName().toUpperCase(), 1);
	}

	private String generateTriggerCode(BusinessRule businessRule) {
		TargetDatabaseType databaseType = businessRule.getTargetDatabase().getTargetDatabaseType();

		RuleTemplate ruleTemplate = findTemplateByName(databaseType, "trigger");
		
		String sTemplate = ruleTemplate.getTemplate();

		return interpretTemplate(sTemplate, businessRule);
	}

	private String interpretTemplate(String template, BusinessRule businessRule){
		Pattern regexp = Pattern.compile(String.format(PATTERN_GENERIC, PATTERN_BODY_GENERIC));

		Matcher matcher = regexp.matcher(template);
		
		if(matcher.find()){
			String replaceArg = matcher.group(0);
			String actualArg = replaceArg.substring(1, replaceArg.length() - 1);
			String replacement = genericParameterDecoder(actualArg, businessRule);
			template = template.replace(replaceArg, replacement);
			template = interpretTemplate(template, businessRule);
		}
		
		return template;
	}
	
	private String sqlString(String string) {
		return '"' + string + '"';
	}

	private String sqlTabellic(String string) {
		return '`' + string + '`';
	}
	
	private String retrieveTriggerConditionTemplate(BusinessRule businessRule) {
		String templateName = triggerOrConstraint + '_' + businessRule.getBusinessRuleType().getName();
		
		RuleTemplate ruleTemplate = findTemplateByName(businessRule.getTargetDatabase().getTargetDatabaseType(),
				templateName);

		if(ruleTemplate == null){
			System.err.println("Template not found:" + templateName);
			return templateName;
		}
		
		return ruleTemplate.getTemplate();
	}
	
	private String retrieveTriggerConditionSubTemplate(BusinessRule businessRule) {
		String templateName = businessRule.getBusinessRuleType().getName();

		RuleTemplate ruleTemplate = findTemplateByName(businessRule.getTargetDatabase().getTargetDatabaseType(),
				templateName);

		return ruleTemplate.getTemplate();
	}

}
