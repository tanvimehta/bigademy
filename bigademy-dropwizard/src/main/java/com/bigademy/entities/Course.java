package com.bigademy.entities;

import java.util.Set;

import javax.persistence.*;

import com.bigademy.serializers.CourseSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "courses")
@NamedQueries({
    @NamedQuery(
        name = "com.bigademy.service.core.Course.findAll",
        query = "SELECT course FROM Course course"
    )
})
@JsonSerialize(using = CourseSerializer.class)
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id", nullable = false)
    private long courseId;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "explanation")
    private String explanation;

    @Column(name = "image")
    private String image;

    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    @OneToMany(fetch = FetchType.EAGER)
    @OrderBy
    protected Set<Topic> topics;

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Set<Topic> getTopics() {
        return topics;
    }

    public void setTopics(Set<Topic> topics) {
        this.topics = topics;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}



