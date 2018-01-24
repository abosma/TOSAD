package tosad.com.generator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tosad.com.hibernate.model.BusinessRule;
import tosad.com.hibernate.model.RuleTemplate;
import tosad.com.hibernate.model.TargetDatabaseType;

public class Generator implements GeneratorInterface {

	private final String PATTERN_GENERIC = "\\{%s\\}";
	private final String PATTERN_BODY_GENERIC = ".*?";

	public Generator() {

	}

	@Override
	public String generateSQL(BusinessRule businessRule) {
		String result = "";

		if (businessRule.getTrigger() != null) {
			generateTriggerCode(businessRule);
		} else if(businessRule.getConstraint() != null ){
			result = "NOT IMPLEMENTED";
		} else {
			result = "NO TRIGGER NOR CONSTRAINT FOUND!";
		}

		return result;
	}
	
	private RuleTemplate findTemplateByName(TargetDatabaseType databaseType, String templateName){
		for (RuleTemplate ruleTemplate : databaseType.getTemplates()) {
			if(ruleTemplate.getName().equals(templateName)){
				return ruleTemplate;
			}
		}
		return null;
	}

	private void generateTriggerCode(BusinessRule businessRule) {
		TargetDatabaseType databaseType = businessRule.getTargetDatabase().getTargetDatabaseType();
		
		RuleTemplate ruleTemplate = findTemplateByName(databaseType, "trigger");
		String actualTemplate = ruleTemplate.getTemplate();
		Pattern regexp = Pattern.compile(String.format(PATTERN_GENERIC, PATTERN_BODY_GENERIC));
		
		Matcher matcher = regexp.matcher(actualTemplate);
		
		while(matcher.find()){
			actualTemplate = actualTemplate.replace(matcher.group(0), "---CHECK---");
		}
		
		System.out.println(actualTemplate);
	}

}
