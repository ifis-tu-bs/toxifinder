/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifis.toxifinder.sandbox;

import ifis.toxifinder.datamodel.MToxicDump;
import java.util.List;
import junit.framework.TestCase;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Christoph
 */
public class HibernateTest {

    private SessionFactory sessionFactory;

    @Before
    public void setUp() throws Exception {
        // A SessionFactory is set up once for an application
        sessionFactory = new Configuration()
                .configure() // configures settings from hibernate.cfg.xml
                .buildSessionFactory();
    }

    @After
    public void tearDown() throws Exception {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    public void testBasicUsage() {
        // create a couple of events...
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        MToxicDump dump1= new MToxicDump("Area 51", "Tote Aliens überall...");
        session.save(dump1);

        session.getTransaction().commit();
        session.close();

        // now lets pull events from the database and list them
        session = sessionFactory.openSession();
        session.beginTransaction();
        List<MToxicDump> result = session.createQuery("from MToxicDump").list();
        for (MToxicDump toxicDump :  result) {
            System.out.println(toxicDump.getName()+ " : " + toxicDump.getDescription());
        }
        session.getTransaction().commit();
        session.close();
    }

}
