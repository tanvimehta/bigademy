package com.bigademy.entities;

import java.util.Set;

import javax.persistence.*;

import com.bigademy.serializers.TopicSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "topics")
@NamedQueries({
    @NamedQuery(
        name = "com.bigademy.service.core.Topic.findAll",
        query = "SELECT topic FROM Topic topic"
    )
})
@JsonSerialize(using = TopicSerializer.class)
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id", nullable = false)
    private long topicId;

    @Column(name = "course_id", nullable = false)
    private long courseId;

    @Column(name = "topic_name", nullable = false)
    private String topicName;

    @Column(name = "explanation")
    private String explanation;

    @JoinColumn(name = "topic_id", referencedColumnName = "topic_id")
    @OneToMany(fetch = FetchType.EAGER)
    @OrderBy
    protected Set<Subtopic> subtopics;

    public long getTopicId() {
        return topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Set<Subtopic> getSubtopics() {
        return subtopics;
    }

    public void setSubtopics(Set<Subtopic> subtopics) {
        this.subtopics = subtopics;
    }
}



