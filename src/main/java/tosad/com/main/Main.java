package tosad.com.main;

import org.hibernate.Session;

import tosad.com.hibernate.*;

public class Main {

	public static void main(String[] argv) {
		Session ses = HibernateUtil.getSession();
		BusinessRule br = new BusinessRule();
		BusinessRuleType brt = new BusinessRuleType();
		CompareValue cv = new CompareValue();
		GeneratedCode gc = new GeneratedCode();
		Operator op = new Operator();
		RuleTemplate rt = new RuleTemplate();
		TargetDatabase td = new TargetDatabase();
		Trigger t = new Trigger();
		ValidationType vt = new ValidationType();
		ses.save(br);
		ses.save(brt);
		ses.save(cv);
		ses.save(gc);
		ses.save(op);
		ses.save(rt);
		ses.save(td);
		ses.save(t);
		ses.save(vt);
		ses.close();
	}
}
