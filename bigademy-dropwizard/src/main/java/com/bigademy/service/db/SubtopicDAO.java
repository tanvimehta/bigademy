package com.bigademy.service.db;

import com.google.common.base.Optional;
import com.yammer.dropwizard.hibernate.AbstractDAO;
import com.bigademy.entities.Subtopic;

import org.hibernate.SessionFactory;

import java.util.List;

public class SubtopicDAO extends AbstractDAO<Subtopic> {
    public SubtopicDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Subtopic> findById(Long id) {
        return Optional.fromNullable(get(id));
    }

    public Subtopic create(Subtopic subtopic) {
        return persist(subtopic);
    }

    public List<Subtopic> findAll() {
        return list(namedQuery("com.bigademy.service.core.Subtopic.findAll"));
    }
}