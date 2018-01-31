package tosad.com.main;

import org.hibernate.Session;

import tosad.com.generator.Generator;
import tosad.com.model.BusinessRule;
import tosad.com.model.util.HibernateUtil;

public class TestSiteTwo {

	public static void main(String[] args) {
		Session session = HibernateUtil.getSession();

		BusinessRule businessRule = session.get(BusinessRule.class, 6);
		
		Generator generator = new Generator();
		System.out.println(businessRule.toString());
		String code = "";
		try {
			code = generator.generateSQL(businessRule);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(code);
		
		session.close();
	}

}
