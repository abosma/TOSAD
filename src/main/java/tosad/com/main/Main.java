package tosad.com.main;

import org.hibernate.Session;

import tosad.com.hibernate.*;
import tosad.com.hibernate.model.BusinessRule;
import tosad.com.hibernate.model.BusinessRuleType;
import tosad.com.hibernate.model.CompareValue;
import tosad.com.hibernate.model.Constraint;
import tosad.com.hibernate.model.GeneratedCode;
import tosad.com.hibernate.model.Operator;
import tosad.com.hibernate.model.RuleTemplate;
import tosad.com.hibernate.model.TargetDatabase;
import tosad.com.hibernate.model.TargetDatabaseType;
import tosad.com.hibernate.model.Trigger;
import tosad.com.hibernate.model.ValidationType;

public class Main {

	public static void main(String[] argv) {
		
		/* save database scheme */
		Session hibernateSession = HibernateUtil.getSession();
		
		hibernateSession.save(new BusinessRule());
		hibernateSession.save(new BusinessRuleType());
		hibernateSession.save(new CompareValue());
		hibernateSession.save(new Constraint());
		hibernateSession.save(new GeneratedCode());
		hibernateSession.save(new Operator());
		hibernateSession.save(new RuleTemplate());
		hibernateSession.save(new TargetDatabase());
		hibernateSession.save(new TargetDatabaseType());
		hibernateSession.save(new Trigger());
		hibernateSession.save(new ValidationType());
		
		hibernateSession.close();
	}
}
