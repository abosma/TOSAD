package tosad.com.generator;

import java.util.HashSet;
import java.util.Set;

import tosad.com.generator.exception.TemplateNotFoundException;
import tosad.com.model.RuleTemplate;
import tosad.com.model.TargetDatabaseType;

public class TemplateFinder {
	
	Set<RuleTemplate> templates;
	
	public TemplateFinder(TargetDatabaseType databaseType){
		this.templates = databaseType.getTemplates();
	}
	
	private RuleTemplate retrieveTemplate(String templateName){
		RuleTemplate ruleTemplate = null;
		for(RuleTemplate template : this.templates){			
			if(template.getName().equals(templateName)){
				ruleTemplate = template;
				break;
			}
		}
		return ruleTemplate;
	}
	
	public String findTemplate(String type) throws TemplateNotFoundException{
		RuleTemplate template = this.retrieveTemplate(type);
		if(template == null){
			throw new TemplateNotFoundException(type);
		}
		return template.getTemplate();
	}

	public Set<RuleTemplate> findTemplatesByPartial(String partial) {
		Set<RuleTemplate> ruleTemplates = new HashSet<RuleTemplate>();
		for(RuleTemplate ruleTemplate : this.templates){
			if(ruleTemplate.getName().contains(partial)){
				ruleTemplates.add(ruleTemplate);
			}
		}
		return ruleTemplates;		
	}
}
