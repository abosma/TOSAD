package tosad.com.generator;

import tosad.com.generator.exception.GenerationException;
import tosad.com.model.BusinessRule;

public class Generator implements GeneratorInterface {
	
	public Generator() {
		
	}

	@Override
	public String generateSQL(BusinessRule businessRule) throws Exception {
		String result = "";

		if (businessRule.getTrigger() != null) {
			TriggerGenerator triggerGenerator = new TriggerGenerator(businessRule);
			result = triggerGenerator.generateCode();
		}
		else if (businessRule.getConstraint() != null) {
			result = "NOT IMPLEMENTED";
		}
		else {
			throw new GenerationException("No Thingy Defined");
		}

		return result;
	}
}
