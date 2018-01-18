package tosad.com.main;

import org.hibernate.Session;

import tosad.com.persistency.Test;
import tosad.com.util.HibernateUtil;

public class Main {

	public static void main(String[] argv) {
		Session ses = HibernateUtil.getSession();
		Test t = new Test();
		t.setId((long) 100);
		ses.save(t);
		ses.close();
	}
}
