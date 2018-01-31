package tosad.com.generator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import tosad.com.generator.exception.SQLFormatException;
import tosad.com.model.RuleTemplate;

public class SQLFormatter {
	
	private final String FORMAT_SUFFIX = "_format";
	
	/**
	 * 
	 */
	private Map<String, char[]> formats;
	
	public SQLFormatter(TemplateFinder finder) {
		Set<RuleTemplate> ruleTemplates = finder.findTemplatesByPartial(FORMAT_SUFFIX);
		
		this.formats = new HashMap<String, char[]>();
		
		int trimLength = FORMAT_SUFFIX.length();
		
		for (RuleTemplate ruleTemplate : ruleTemplates) {
			String templateName = ruleTemplate.getName().toLowerCase();
			String formatName = templateName.substring(0, templateName.length() - trimLength);
			String template = ruleTemplate.getTemplate() != null ? ruleTemplate.getTemplate().trim() : "";
			
			char[] chars = new char[2];
			
			chars[0] = template.length() > 0 ? template.charAt(0) : ' ';
			chars[1] = template.length() > 1 ? template.charAt(1) : chars[0];
			this.formats.put(formatName, chars);
		}
	}
	
	public String format(String format, String subject) throws SQLFormatException {
		String key = format.trim().toLowerCase();
		if( !this.formats.containsKey(key) ){
			throw new SQLFormatException(String.format("Format '%s' was not found", key));
		}
		char[] theFormat = this.formats.get(key);
		return (theFormat[0] + subject + theFormat[1]).trim();
	}
}
