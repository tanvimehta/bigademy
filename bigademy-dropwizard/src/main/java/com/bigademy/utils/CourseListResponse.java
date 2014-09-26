package com.bigademy.utils;

import com.bigademy.entities.Course;

import java.util.List;

/**
 * Created by mshah on 3/18/14.
 */
public class CourseListResponse {

    private List<Course> courses;

    public CourseListResponse(List<Course> courses) {
        this.courses = courses;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}
