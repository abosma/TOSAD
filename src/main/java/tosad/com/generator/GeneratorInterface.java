package tosad.com.generator;

import tosad.com.generator.exception.GenerationException;
import tosad.com.generator.exception.SQLFormatException;
import tosad.com.generator.exception.TemplateNotFoundException;
import tosad.com.model.BusinessRule;

public interface GeneratorInterface {
	public String generateSQL(BusinessRule businessRule) throws GenerationException, TemplateNotFoundException, SQLFormatException;
}
