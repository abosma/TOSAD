package tosad.com.generator;

import tosad.com.hibernate.model.BusinessRule;

public class Generator implements GeneratorInterface{

	@Override
	public String generateSQL(BusinessRule businessRule) {
		String result = "";
		
		result = "ToDo: Template for: " + businessRule.getName();
		
		return result;
	}

}
