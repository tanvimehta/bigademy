package com.bigademy.service.db;

import com.bigademy.entities.Topic;
import com.google.common.base.Optional;
import com.yammer.dropwizard.hibernate.AbstractDAO;

import org.hibernate.SessionFactory;

import java.util.List;

public class TopicDAO extends AbstractDAO<Topic> {
    public TopicDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Topic> findById(Long id) {
        return Optional.fromNullable(get(id));
    }

    public Topic create(Topic topic) {
        return persist(topic);
    }

    public List<Topic> findAll() {
        return list(namedQuery("com.bigademy.service.core.Topic.findAll"));
    }
}