package tosad.com.main;

import org.hibernate.Session;

import tosad.com.persistency.Targets;
import tosad.com.util.HibernateUtil;

public class Main {

	public static void main(String[] argv) {
		Session ses = HibernateUtil.getSession();
		Targets t = new Targets();
		ses.save(t);
		ses.close();
	}
}
