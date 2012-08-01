/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.src;

import com.bean.RatedCdr;
import java.util.Date;
import java.util.ArrayList;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

/**
 *
 * @author Nimil
 */
public class RatedCdrHelper {

    private Session session = null;

    public RatedCdrHelper() {
        this.session = HibernateUtil.getSessionFactory().getCurrentSession();
    }

    public ArrayList<RatedCdr> getEvents(String msn, Date eventMonth) {
        DateTime dt = new DateTime(eventMonth);
        org.hibernate.Transaction tx = session.beginTransaction();
        Criteria crit = session.createCriteria(RatedCdr.class);
        crit.add(Restrictions.eq("msn", msn));
        crit.add(Restrictions.between("startTimestamp", getMinimum(dt), getMaximum(dt)));
        return (ArrayList<RatedCdr>) crit.list();
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
        mdt.setDayOfMonth(dateTime.dayOfMonth().getMaximumValue());
        mdt.setMillisOfDay(dateTime.millisOfDay().getMaximumValue());
        return (Date) mdt.toDate();
    }
}