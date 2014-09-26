package com.bigademy.service.db;

import com.bigademy.entities.Exercise;
import com.google.common.base.Optional;
import com.yammer.dropwizard.hibernate.AbstractDAO;

import org.hibernate.SessionFactory;

import java.util.List;

public class ExerciseDAO extends AbstractDAO<Exercise> {
    public ExerciseDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Exercise> findById(Long id) {
        return Optional.fromNullable(get(id));
    }

    public Exercise create(Exercise exercise) {
        return persist(exercise);
    }

    public List<Exercise> findAll() {
        return list(namedQuery("com.bigademy.service.core.Exercise.findAll"));
    }
}