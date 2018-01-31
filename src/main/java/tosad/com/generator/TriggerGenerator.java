package tosad.com.generator;

import tosad.com.generator.exception.GenerationException;
import tosad.com.generator.exception.SQLFormatException;
import tosad.com.generator.exception.TemplateNotFoundException;
import tosad.com.model.BusinessRule;

public class TriggerGenerator extends AbstractGenerator{
	
	public TriggerGenerator(BusinessRule businessRule) {
		super(businessRule);
	}
	
	public String getContentForKeyword(String keyword) throws GenerationException, TemplateNotFoundException, SQLFormatException {
		switch (keyword) {
		case "trigger_identifier":
			return this.generateRuleIdentifier();
		case "trigger_execution":
			return generateTriggerExecution();
		case "trigger_types":
			return generateTriggerTypes();
		case "table_name":
			return getReferencedTableName();
		case "condition":
			return retrieveCondition();
		case "condition_sub_template":
			return retrieveTriggerConditionSubTemplate();
		case "column_name":
			return getReferencedColumnName();
		case "value":
			return getCompareValue();
		case "operator":
			return operatorValue();
		case "error_text":
			return retrieveErrorText();
		default:
			throw new GenerationException(String.format("Error while parsing a template: keyword '%s' is unknown.", keyword));
		}
	}

	public String generateCode() throws GenerationException, TemplateNotFoundException, SQLFormatException {
		String baseTemplate = templateFinder.findTemplate("trigger");
		String evaluatedTemplate = evaluateTemplate(baseTemplate);
		return evaluatedTemplate;
	}
	
	public String evaluateTemplate(String template) throws GenerationException, TemplateNotFoundException, SQLFormatException {
		
		// retrieve keywords from template
		String[] keywords = this.retrieveTemplateKeywords(template);
		for (String keyword : keywords) {
			// retrieve content for the given keyword
			String keywordContent = getContentForKeyword(keyword);
			
			// check whether the retrieved content contains keywords that need to be filled out
			if(containsKeywords(keywordContent)){
				// update the keyword content with the new content
				keywordContent = evaluateTemplate(keywordContent);
			}
			// replace the keyword with it's generated content
			template = template.replaceFirst(String.format("\\{%s\\}", keyword), keywordContent);
		}		
		
		return template;
	}
	
	private String generateTriggerTypes() {
		//TODO double check if this is correct... looks quite ugly
		return String.join(",", businessRule.getTrigger().getExecutionType().split(";")); 
	}

	private String generateTriggerExecution() {
		//TODO could it be this simple???
		return businessRule.getTrigger().getExecutionLevel();
	}

	private String retrieveCondition() throws TemplateNotFoundException {
		String rulename = businessRule.getBusinessRuleType().getName().toLowerCase().replaceAll(" ", "_");
		String templateName = "trigger_" + rulename;
		return templateFinder.findTemplate(templateName);
	}
	
	private String retrieveTriggerConditionSubTemplate() throws TemplateNotFoundException {
		String rulename = businessRule.getBusinessRuleType().getName().toLowerCase().replaceAll(" ", "_");
		return templateFinder.findTemplate(rulename);
	}
	
	private String retrieveErrorText() throws SQLFormatException{
		return sqlFormatter.format("string", businessRule.getErrorMessage());
	}
}
