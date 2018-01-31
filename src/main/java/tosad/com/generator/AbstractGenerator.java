package tosad.com.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import tosad.com.generator.exception.GenerationException;
import tosad.com.generator.exception.SQLFormatException;
import tosad.com.generator.exception.TemplateNotFoundException;
import tosad.com.model.BusinessRule;
import tosad.com.model.CompareValue;
import tosad.com.model.TargetDatabaseType;

public abstract class AbstractGenerator  {
	
	private final Pattern KeyfinderPattern;
	
	protected BusinessRule businessRule;
	protected TemplateFinder templateFinder;
	protected SQLFormatter sqlFormatter;
	protected int valueCounter = 1;
	
	public abstract String generateCode() throws GenerationException, TemplateNotFoundException, SQLFormatException;
	public abstract String getContentForKeyword(String keyword) throws GenerationException, TemplateNotFoundException, SQLFormatException;
	
	public AbstractGenerator(BusinessRule businessRule) {
		KeyfinderPattern = Pattern.compile("\\{.*?\\}");
		this.businessRule = businessRule;
		TargetDatabaseType databaseType = this.businessRule.getTargetDatabase().getTargetDatabaseType();
		
		this.templateFinder = new TemplateFinder(databaseType);
		this.sqlFormatter = new SQLFormatter(this.templateFinder);
	}

	protected String generateRuleIdentifier() {
		String tableName = businessRule.getReferencedTable().toUpperCase();
		String ruleType = businessRule.getBusinessRuleType().getName().toUpperCase().replace(' ', '_');
		int base = 9;
		base += tableName.length();
		
		ruleType = ruleType.substring(0, 30 - base);		
		
		//TODO change the hardcoded rulenumber
		return String.format("BRG_%S_%S_%d", tableName, ruleType, 0);
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

	protected String getCompareValue() throws GenerationException, SQLFormatException{
		Set<CompareValue> values = businessRule.getCompareValues();
		for(CompareValue value : values){
			if(value.getOrder() == valueCounter){
				// retrieve string representation of the value
				String valueRepresentation = compileCompareValue(value);
				// if it hasn't throw any erros, increment the counter
				valueCounter++;
				return valueRepresentation;
			}
		}
		throw new GenerationException(String.format("No CompareValue was found for order '%d'. Please check the businessrule", valueCounter));
	}
	
	private String compileCompareValue(CompareValue compareValue) throws GenerationException, SQLFormatException {
		String empty	= new String();
		String literal	= compareValue.getValue() != null ? compareValue.getValue().trim() : new String();
		String table	= compareValue.getTable() != null ? compareValue.getTable().trim() : new String();
		String column	= compareValue.getColumn() != null ? compareValue.getColumn().trim() : new String();
				
		if( literal.equals(empty)){
			if( column.equals(empty)){
				throw new GenerationException(String.format("Error while evaluating CompareValue with id '%d': No value defined.", compareValue.getId()));
			} 
			// check whether reference is to external table, or own table
			
			if( table.equals(empty)){
				table = businessRule.getReferencedTable();
			}
			
			column	= sqlFormatter.format("column", column);
			table	= sqlFormatter.format("table", table);
			
			return String.format("%s.%s", column, table);
		} else {
			if( table.equals(empty) && column.equals(empty)){
				// only the literal value is set, so return it
				if( literal.matches("\\d+") ){
					return sqlFormatter.format("number", literal);
				} 
				return sqlFormatter.format("string", literal);
			}
			// both literal value and referenced table are set
			throw new GenerationException(String.format("Error while evaluating CompareValue with id '%d': Both a literal value and a reference are defined"));
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
		return String.join(",", values);
	}
}
