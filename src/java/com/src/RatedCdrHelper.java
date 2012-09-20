/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.src;

import com.bean.RatedCdr;
import com.bean.InvoiceNumber;
import java.sql.SQLException;
import java.util.Date;
import java.util.ArrayList;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

/**
 *
 * @author Nimil
 */
public class RatedCdrHelper {

    private Session session = null;
    private Session sessionInvoice = null;

    public RatedCdrHelper() {
        this.session = HibernateUtil.getSessionFactory(false).getCurrentSession();
        this.sessionInvoice = HibernateUtil.getSessionFactory(true).getCurrentSession();
    }
    
    public ArrayList<InvoiceNumber> getInvoiceNumbers(Set<String> accNumbers){
        //DateTime dt = new DateTime(eventMonth);
        org.hibernate.Transaction tx = sessionInvoice.beginTransaction();
        Criteria crit = sessionInvoice.createCriteria(InvoiceNumber.class);
        crit.add(Restrictions.in("Tier1", accNumbers));
        //crit.add(Restrictions.between("startTimestamp", getMinimum(dt), getMaximum(dt)));
        return (ArrayList<InvoiceNumber>) crit.list();
    }
    
    public ArrayList<RatedCdr> getEvents(String msn, Date eventMonth) {
        DateTime dt = new DateTime(eventMonth);
        org.hibernate.Transaction tx = session.beginTransaction();
        Criteria crit = session.createCriteria(RatedCdr.class);
        crit.add(Restrictions.eq("msn", msn));
        crit.add(Restrictions.between("startTimestamp", getMinimum(dt), getMaximum(dt)));
        return (ArrayList<RatedCdr>) crit.list();
    }
    
    public ArrayList<Object> getCallReport(String msn, Date eventMonth){
        DateTime dt = new DateTime(eventMonth);
        org.hibernate.Transaction tx = session.beginTransaction();
        Criteria crit = session.createCriteria(RatedCdr.class);
        crit.add(Restrictions.eq("msn", msn));
        crit.add(Restrictions.between("startTimestamp", getMinimum(dt), getMaximum(dt)));
        crit.setProjection(Projections.projectionList().add(Projections.groupProperty("zoneDestination")).add(Projections.sum("retailPrice")).add(Projections.count("id")));
        return (ArrayList<Object>) crit.list();
    }

    private Date getMinimum(DateTime dateTime) {
        MutableDateTime mdt = new MutableDateTime(dateTime);
        mdt.addMonths(-1);
        mdt.setDayOfMonth(1);
        mdt.setMillisOfDay(0);
        return (Date) mdt.toDate();
    }

    private Date getMaximum(DateTime dateTime) {
        MutableDateTime mdt = new MutableDateTime(dateTime);
        mdt.addMonths(-1);
        mdt.setDayOfMonth(mdt.dayOfMonth().getMaximumValue());
        mdt.setMillisOfDay(mdt.millisOfDay().getMaximumValue());
        return (Date) mdt.toDate();
    }
    
    public void closeSession() throws SQLException{
        this.session.clear();
        this.session.close();
        this.sessionInvoice.clear();
        this.sessionInvoice.close();
    }
}