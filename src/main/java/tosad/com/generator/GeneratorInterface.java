package tosad.com.generator;

import tosad.com.model.BusinessRule;

public interface GeneratorInterface {
	public String generateSQL(BusinessRule businessRule);
}
