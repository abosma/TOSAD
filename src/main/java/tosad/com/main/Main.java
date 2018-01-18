package tosad.com.main;

import org.hibernate.Session;

import tosad.com.persistency.TargetDatabase;
import tosad.com.util.HibernateUtil;

public class Main {

	public static void main(String[] argv) {
		Session ses = HibernateUtil.getSession();
		TargetDatabase t = new TargetDatabase();
		ses.save(t);
		ses.close();
	}
}
