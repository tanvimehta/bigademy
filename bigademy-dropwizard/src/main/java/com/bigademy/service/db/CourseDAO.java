package com.bigademy.service.db;

import com.bigademy.entities.Course;
import com.google.common.base.Optional;
import com.yammer.dropwizard.hibernate.AbstractDAO;

import org.hibernate.SessionFactory;

import java.util.List;

public class CourseDAO extends AbstractDAO<Course> {
    public CourseDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Course> findById(Long id) {
        return Optional.fromNullable(get(id));
    }

    public Course create(Course course) {
        return persist(course);
    }

    public List<Course> findAll() {
        return list(namedQuery("com.bigademy.service.core.Course.findAll"));
    }
}