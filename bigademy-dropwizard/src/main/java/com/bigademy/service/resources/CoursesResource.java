package com.bigademy.service.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import javax.ws.rs.core.MediaType;

import com.bigademy.utils.CourseListResponse;
import org.hibernate.mapping.Table;

import com.bigademy.entities.Course;
import com.bigademy.entities.Exercise;
import com.bigademy.entities.Subtopic;
import com.bigademy.entities.Topic;
import com.bigademy.service.db.CourseDAO;
import com.google.common.base.Optional;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import com.yammer.dropwizard.jersey.params.LongParam;
import com.yammer.metrics.annotation.Timed;

@Path("/courses")
@Produces(MediaType.APPLICATION_JSON)
public class CoursesResource {

    private final CourseDAO coursesDAO;

    public CoursesResource(CourseDAO coursesDAO) {
        this.coursesDAO = coursesDAO;
    }

    @POST
    @UnitOfWork
    public Course createCourse(Course course) {
        return coursesDAO.create(course);
    }

    @GET
    @Timed
    @UnitOfWork
    @Path("{courseId}")
    public Optional<Course> findTopic(@PathParam("courseId") LongParam courseId) {
        return coursesDAO.findById(courseId.get());
    }

    @GET
    @Timed
    @UnitOfWork
    @Path("all")
    public CourseListResponse getAllCourses() {
        return new CourseListResponse(coursesDAO.findAll());
    }
}
