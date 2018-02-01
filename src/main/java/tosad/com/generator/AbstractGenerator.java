package tosad.com.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tosad.com.generator.exception.GenerationException;
import tosad.com.generator.exception.SQLFormatException;
import tosad.com.generator.exception.TemplateNotFoundException;
import tosad.com.model.BusinessRule;
import tosad.com.model.CompareValue;
import tosad.com.model.TargetDatabaseType;
import tosad.com.model.enums.ValueType;

public abstract class AbstractGenerator  {
	private static int nr = 0;
	private final Pattern KeyfinderPattern;
	private Map<String, CompareValue> compareValues;
	
	protected BusinessRule businessRule;
	protected TemplateFinder templateFinder;
	protected SQLFormatter sqlFormatter;
	
	public abstract String generateCode() throws GenerationException, TemplateNotFoundException, SQLFormatException;
	public abstract String getContentForKeyword(String keyword) throws GenerationException, TemplateNotFoundException, SQLFormatException;
	
	public AbstractGenerator(BusinessRule businessRule) {
		KeyfinderPattern = Pattern.compile("\\{.*?\\}");
		this.businessRule = businessRule;
		TargetDatabaseType databaseType = this.businessRule.getTargetDatabase().getTargetDatabaseType();
		
		this.templateFinder = new TemplateFinder(databaseType);
		this.sqlFormatter = new SQLFormatter(this.templateFinder);
		
		this.compareValues = new HashMap<String, CompareValue>();
		mapValues();
	}
	
	private void mapValues(){
		List<CompareValue> listCompareValues = new ArrayList<CompareValue>();
		for (CompareValue compareValue : businessRule.getCompareValues()) {
			if( compareValue.getAllowedValueType() == ValueType.CUSTOM_SQL ){
				this.compareValues.put("sql", compareValue);
			} else {
				listCompareValues.add(compareValue);
			}
		}
		Collections.sort(listCompareValues);
		int i = 0;
		for (CompareValue compareValue : listCompareValues) 
			this.compareValues.put(""+(++i), compareValue);
	}

	protected String generateRuleIdentifier() {
		String tableName = businessRule.getReferencedTable().toUpperCase();
		String ruleType = businessRule.getBusinessRuleType().getName().toUpperCase().replace(' ', '_');
		int base = 9;
		base += tableName.length();
		
		ruleType = ruleType.substring(0, 30 - base);		
		
		//TODO change the hardcoded rulenumber
		return String.format("BRG_%S_%S_%d", tableName, ruleType, nr++);
	}
	
	protected String getReferencedTableName() throws SQLFormatException {
		return sqlFormatter.format("table", businessRule.getReferencedTable().toLowerCase());
	}
	
	protected String getReferencedColumnName() throws SQLFormatException {
		return sqlFormatter.format("column", businessRule.getReferencedColumn().toLowerCase());
	}
	
	protected String operatorValue() throws TemplateNotFoundException{
		String operatorTemplateName = businessRule.getOperator().getCode();
		String template =  templateFinder.findTemplate(operatorTemplateName);
		return template;
	}
	
	protected String getCompareValue(String keyword) throws GenerationException, SQLFormatException {
		if(keyword.equals("values"))
			return getCompareValueList();
		
		String[] parts = keyword.split("_");
		String prefix = parts[0];
		
		if( ! prefix.equals("value"))
			throw new GenerationException(String.format("Invalid template keyword format; keyword should start with 'value_', found: '%s'.", prefix));
		
		if(parts.length < 2)
			throw new GenerationException(String.format("Cannot compile CompareValue with key '%s', missings identifier (int)", keyword));
		
		String subject = parts[1];
		
		CompareValue compareValue = getCompareValueObject(subject);
		
		if( ! ( parts.length > 2))
			return compileCompareValue(compareValue);
		
		String suffix = parts[2];
		if(suffix.equals("column")) {
			return sqlFormatter.format(suffix, compareValue.getColumn().toLowerCase());
		} else if(suffix.equals("table")) {
			return sqlFormatter.format(suffix, compareValue.getTable().toLowerCase());
		} else {
			throw new GenerationException(String.format("Cannot compile CompareValue with key '%s', Unknown Suffix '%s'", keyword, suffix));
		}
	}

	protected CompareValue getCompareValueObject(String subject) throws GenerationException, SQLFormatException{
		CompareValue compareValue = this.compareValues.get(subject);
		if( compareValue == null)
			throw new GenerationException(String.format("No CompareValue was found for subject '%s'. Please check the businessrule", subject));
		return compareValue;
	}
	
	private String compileCompareValue(CompareValue compareValue) throws GenerationException, SQLFormatException {		
		ValueType valueType = compareValue.getAllowedValueType();
		
		if( ValueType.STATIC_NUMBER == valueType )
			return sqlFormatter.format("number", compareValue.getValue());
		
		if( ValueType.STATIC_STRING == valueType )
			return sqlFormatter.format("string", compareValue.getValue());
		
		if( ValueType.CUSTOM_SQL == valueType )
			return compareValue.getValue(); // SQL into SQL requires no formatting
		
		if( ValueType.TUPLE == valueType )
			return String.format("%s.%s", 
					sqlFormatter.format("column", compareValue.getColumn()), 
					sqlFormatter.format("table", compareValue.getBusinessRule().getReferencedTable()));
		
		if( ValueType.ENTITY == valueType )
			return String.format("%s.%s", 
					sqlFormatter.format("column", compareValue.getColumn()), 
					sqlFormatter.format("table", compareValue.getTable()));
		
		throw new GenerationException("Unsupported CompareValue ValueType: " + valueType.toString());
	}
	
	/**
	 * Checks whether then given string contains keywords from the following pattern: '{keyword}'
	 * @param string	the string that might contain one or more keywords	
	 * @return			(boolean) True if the template contains one more more keywords
	 */
	protected boolean containsKeywords(String string){
		Matcher matcher = KeyfinderPattern.matcher(string);
		return matcher.find();
	}
	
	/**
	 * Returns all keywords from pattern '{keyword} ' within the given string
	 * @param string	the string to evaluate
	 * @return			String[] containing all found keywords, without brackets
	 */
	protected String[] retrieveTemplateKeywords(String string){
		List<String> resultSet = new ArrayList<String>();
		
		Matcher matcher = KeyfinderPattern.matcher(string);
		
		while(matcher.find()){
			String bracketedKeyword = matcher.group(0);
			String pureKeyword = bracketedKeyword.substring(1, bracketedKeyword.length() - 1);
			resultSet.add(pureKeyword);
		}
		
		return resultSet.toArray(new String[resultSet.size()]);
	}

	protected String getCompareValueList() throws GenerationException, SQLFormatException {
		Set<CompareValue> cValues = businessRule.getCompareValues();
		String[] values = new String[cValues.size()];

		int count = 0;
		for (CompareValue compareValue : cValues) {
			values[count++] = compileCompareValue(compareValue);
		}
		return sqlFormatter.format("list", String.join(",", values));
	}
}
