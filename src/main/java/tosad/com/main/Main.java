package tosad.com.main;

import org.hibernate.Session;

import tosad.com.model.util.HibernateUtil;

public class Main {

	public static void main(String[] argv) {
		
//		/* save database scheme */
		Session hibernateSession = HibernateUtil.getSession();
		
		/*
		hibernateSession.save(new ValidationType());
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
		*/
		
		hibernateSession.close();
		
		/*RequestHandler rh = new RequestHandler();
		
		try {
			rh.getTables(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
