package com.src;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.SessionFactory;

/**
 * Hibernate Utility class with a convenient method to get Session Factory object.
 *
 * @author Nimil
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;
    private static final SessionFactory sessionFactoryInvoice;
    
    static {
        try {
            // Create the SessionFactory from standard (hibernate.cfg.xml) 
            // config file.
            sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
            sessionFactoryInvoice = new AnnotationConfiguration().configure("hibernate-Invoice.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            // Log the exception. 
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static SessionFactory getSessionFactory(Boolean isInvoiceDatabase) {
        if(isInvoiceDatabase){
            return sessionFactoryInvoice;
        }else{
            return sessionFactory;
        }
    }
}
