package tosad.com.generator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import tosad.com.generator.exception.SQLFormatException;
import tosad.com.model.RuleTemplate;

public class SQLFormatter {
	
	private final String FORMAT_SUFFIX = "_format";
	
	private Map<String, String> formats;
	
	public SQLFormatter(TemplateFinder finder) {
		Set<RuleTemplate> ruleTemplates = finder.findTemplatesByPartial(FORMAT_SUFFIX);
		
		this.formats = new HashMap<String, String>();
		
		int trimLength = FORMAT_SUFFIX.length();
		
		for (RuleTemplate ruleTemplate : ruleTemplates) {
			String templateName = ruleTemplate.getName().toLowerCase();
			String formatName = templateName.substring(0, templateName.length() - trimLength);
			String template = ruleTemplate.getTemplate().trim();
			String formatSpecifier = template.length() > 0 ? ""+template.charAt(0) : "";
			this.formats.put(formatName, formatSpecifier);
		}
	}
	
	public String format(String format, String subject) throws SQLFormatException {
		String key = format.trim().toLowerCase();
		if( !this.formats.containsKey(key) ){
			throw new SQLFormatException(String.format("Format '%s' was not found", key));
		}
		String theFormat = this.formats.get(key);
		return theFormat + subject + theFormat;
	}
}
