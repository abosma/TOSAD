package tosad.com.util;

import org.hibernate.*;
import org.hibernate.cfg.*;
import tosad.com.persistency.*;

public class HibernateUtil {

private static final SessionFactory sessionFactory;
    static {
        try {
            sessionFactory = new Configuration()
            		.addAnnotatedClass(Targets.class)
            		.addAnnotatedClass(ValidationType.class)
                    .configure().buildSessionFactory();
        } catch (Exception ex) {
            // Log exception!
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession()
            throws HibernateException {
        return sessionFactory.openSession();
    }
}
