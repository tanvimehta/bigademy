package com.bigademy.service.db;

import com.bigademy.entities.Course;
import com.bigademy.entities.WaitList;
import com.yammer.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

/**
 * Created by mshah on 4/25/14.
 */
public class WaitListDAO extends AbstractDAO<WaitList> {

    public WaitListDAO(SessionFactory factory) {
        super(factory);
    }

    public WaitList create(WaitList waitList) {
        return persist(waitList);
    }
}
