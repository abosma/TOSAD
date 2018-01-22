package tosad.com.hibernate;

import org.hibernate.*;
import org.hibernate.cfg.*;

public class HibernateUtil {

private static final SessionFactory sessionFactory;
    static {
        try {
            sessionFactory = new Configuration()
            		.addAnnotatedClass(BusinessRule.class)
            		.addAnnotatedClass(BusinessRuleType.class)
            		.addAnnotatedClass(CompareValue.class)
            		.addAnnotatedClass(GeneratedCode.class)
            		.addAnnotatedClass(Operator.class)
            		.addAnnotatedClass(RuleTemplate.class)
            		.addAnnotatedClass(TargetDatabaseType.class)
            		.addAnnotatedClass(Trigger.class)
            		.addAnnotatedClass(ValidationType.class)
            		.addAnnotatedClass(TargetDatabase.class)
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
