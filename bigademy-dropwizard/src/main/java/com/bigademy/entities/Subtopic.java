package com.bigademy.entities;

import java.util.Set;

import javax.persistence.*;

import com.bigademy.serializers.SubtopicSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "subtopics")
@NamedQueries({
    @NamedQuery(
        name = "com.bigademy.service.core.Subtopic.findAll",
        query = "SELECT subtopic FROM Subtopic subtopic"
    )
})
@JsonSerialize(using = SubtopicSerializer.class)
public class Subtopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subtopic_id", nullable = false)
    private long subtopicId;

    @Column(name = "topic_id", nullable = false)
    private long topicId;

    @Column(name = "subtopic_name", nullable = false)
    private String subtopicName;

    @Column(name = "explanation")
    private String explanation;

    @JoinColumn(name = "subtopic_id", referencedColumnName = "subtopic_id")
    @OneToMany(fetch = FetchType.EAGER)
    @OrderBy
    protected Set<Exercise> exercises;

    public long getSubtopicId() {
        return subtopicId;
    }

    public void setSubtopicId(long subtopicId) {
        this.subtopicId = subtopicId;
    }

    public long getTopicId() {
        return topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public String getSubtopicName() {
        return subtopicName;
    }

    public void setSubtopicName(String subtopicName) {
        this.subtopicName = subtopicName;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Set<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(Set<Exercise> exercises) {
        this.exercises = exercises;
    }
}



