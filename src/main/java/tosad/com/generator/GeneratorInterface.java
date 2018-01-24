package tosad.com.generator;

import tosad.com.hibernate.model.BusinessRule;

public interface GeneratorInterface {
	public String generateSQL(BusinessRule businessRule);
}
