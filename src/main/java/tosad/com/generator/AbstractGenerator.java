package tosad.com.generator;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tosad.com.generator.exception.TemplateNotFoundException;
import tosad.com.generator.exception.UnknownKeywordException;
import tosad.com.generator.exception.ValueNotFoundException;
import tosad.com.model.BusinessRule;
import tosad.com.model.CompareValue;

public abstract class AbstractGenerator  {
	
	private final Pattern KeyfinderPattern;
	
	protected BusinessRule businessRule;
	protected TemplateFinder templateFinder;
	protected int valueCounter;
	
	public abstract String generateCode() throws TemplateNotFoundException, UnknownKeywordException;
	public abstract String getContentForKeyword(String keyword) throws UnknownKeywordException, TemplateNotFoundException;
	
	public AbstractGenerator() {
		KeyfinderPattern = Pattern.compile("\\{.*?\\}");
		valueCounter = 0;
	}

	protected String generateRuleIdentifier() {
		String tableName = businessRule.getReferencedTable().toUpperCase();
		String ruleType = businessRule.getBusinessRuleType().getName().toUpperCase();
		
		//TODO change the hardcoded rulenumber
		return String.format("BRG_%S_%S_%d", tableName, ruleType, 0);
	}
	
	protected String getReferencedTableName() {
		return sqlReferenceFormat(businessRule.getReferencedTable().toLowerCase());
	}
	
	protected String getReferencedColumnName() {
		return sqlReferenceFormat(businessRule.getReferencedColumn());
	}
	
	protected String operatorValue() throws TemplateNotFoundException{
		String operatorTemplateName = businessRule.getOperator().getValue();
		String template =  templateFinder.findTemplate(operatorTemplateName);
		return template;
	}

	protected String getCompareValue() throws ValueNotFoundException, CompareValueError{
		Set<CompareValue> values = businessRule.getCompareValues();
		for(CompareValue value : values){
			if(value.getOrder() == valueCounter){
				// retrieve string representation of the value
				String valueRepresentation = compileCompareValue(value);
				// if it hasn't failed, increment the counter
				valueCounter++;
				return valueRepresentation;
			}
		}
		throw new ValueNotFoundException(Integer.toString(valueCounter));
	}
	
	private String compileCompareValue(CompareValue compareValue) throws CompareValueError {
		String empty	= new String();
		String literal	= compareValue.getValue().trim();
		String table	= compareValue.getTable().trim();
		String column	= compareValue.getColumn().trim();
				
		if( literal.equals(empty)){
			if( column.equals(empty)){
				throw new CompareValueError("No Literal or referenced value set.");
			} 
			// check whether reference is to external table, or own table
			if( table.equals(empty)){
				return String.format("%s.%s", column, businessRule.getReferencedTable());
			}
			return String.format("%s.%s", column, table);
		} else {
			if( table.equals(empty) && column.equals(empty)){
				// only the literal value is set, so return it
				return literal;
			}
			// both literal value and referenced table are set
			throw new CompareValueError("Litaral and non-litared values are defined");
		}
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
		Set<String> resultSet = new HashSet<String>();
		
		Matcher matcher = KeyfinderPattern.matcher(string);
		
		while(matcher.find()){
			String bracketedKeyword = matcher.group(0);
			String pureKeyword = bracketedKeyword.substring(1, bracketedKeyword.length() - 1);
			
			resultSet.add(pureKeyword);
		}
		
		return resultSet.toArray(new String[resultSet.size()]);
	}
	
	/**
	 * Transform a string to the correct string format for the database type
	 * @param string
	 * @return formatted string
	 */
	protected String sqlStringFormat(String string) {
		//TODO change to template!
		return String.format("\"%s\"", string);
	}
	
	/**
	 * Transform a string to the correct reference format for the database type
	 * @param string
	 * @return formatted string
	 */
	protected String sqlReferenceFormat(String string) {
		//TODO change to template!
		return string;
	}
}
