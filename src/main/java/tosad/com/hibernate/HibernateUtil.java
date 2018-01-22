package tosad.com.hibernate;

import org.hibernate.*;
import org.hibernate.cfg.*;

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

public class HibernateUtil {

private static final SessionFactory sessionFactory;
    static {
        try {
            sessionFactory = new Configuration()
            		.addAnnotatedClass(BusinessRule.class)
            		.addAnnotatedClass(BusinessRuleType.class)
            		.addAnnotatedClass(CompareValue.class)
            		.addAnnotatedClass(Constraint.class)
            		.addAnnotatedClass(GeneratedCode.class)
            		.addAnnotatedClass(Operator.class)
            		.addAnnotatedClass(RuleTemplate.class)
            		.addAnnotatedClass(TargetDatabase.class)
            		.addAnnotatedClass(TargetDatabaseType.class)
            		.addAnnotatedClass(Trigger.class)
            		.addAnnotatedClass(ValidationType.class)
                    .configure().buildSessionFactory();
        } catch (Exception exception) {
            // Throw exception!
            throw new ExceptionInInitializerError(exception);
        }
    }

    public static Session getSession()
            throws HibernateException {
        return sessionFactory.openSession();
    }
}
