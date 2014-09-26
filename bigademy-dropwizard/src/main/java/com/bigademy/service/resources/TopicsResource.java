package com.bigademy.service.resources;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.bigademy.entities.Topic;
import com.bigademy.service.db.TopicDAO;
import com.google.common.base.Optional;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import com.yammer.dropwizard.jersey.params.LongParam;
import com.yammer.metrics.annotation.Timed;

@Path("/topics/{topicId}")
@Produces(MediaType.APPLICATION_JSON)
public class TopicsResource {

    private final TopicDAO topicsDAO;

    public TopicsResource(TopicDAO topicsDAO) {
        this.topicsDAO = topicsDAO;
    }

    @POST
    @UnitOfWork
    public Topic createTopic(Topic topic) {
        return topicsDAO.create(topic);
    }

    @GET
    @Timed
    @UnitOfWork
    public Optional<Topic> findTopic(@PathParam("topicId") LongParam topicId) {
        return topicsDAO.findById(topicId.get());
    }
}
