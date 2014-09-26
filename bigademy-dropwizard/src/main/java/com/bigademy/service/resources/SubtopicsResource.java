package com.bigademy.service.resources;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.bigademy.entities.Subtopic;
import com.bigademy.service.db.SubtopicDAO;
import com.google.common.base.Optional;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import com.yammer.dropwizard.jersey.params.LongParam;
import com.yammer.metrics.annotation.Timed;

@Path("/subtopics/{subtopicId}")
@Produces(MediaType.APPLICATION_JSON)
public class SubtopicsResource {

    private final SubtopicDAO subtopicsDAO;

    public SubtopicsResource(SubtopicDAO subtopicsDAO) {
        this.subtopicsDAO = subtopicsDAO;
    }

    @POST
    @UnitOfWork
    public Subtopic createSubtopic(Subtopic subtopic) {
        return subtopicsDAO.create(subtopic);
    }

    @GET
    @Timed
    @UnitOfWork
    public Optional<Subtopic> findSubtopic(@PathParam("subtopicId") LongParam subtopicId) {
        return subtopicsDAO.findById(subtopicId.get());
    }
}
