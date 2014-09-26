package com.bigademy.service.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.bigademy.pigademy.PigScriptExecutor;
import com.bigademy.utils.Response;

import com.bigademy.entities.Exercise;
import com.bigademy.service.db.ExerciseDAO;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import com.yammer.dropwizard.jersey.params.LongParam;
import com.yammer.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/pigrun")
@Produces(MediaType.APPLICATION_JSON)
public class PigRunResource {

    private static final Logger logger = LoggerFactory.getLogger(PigRunResource.class);

    private final ExerciseDAO exercisesDAO;

    public PigRunResource(ExerciseDAO exercisesDAO) {
        this.exercisesDAO = exercisesDAO;
    }

    @GET
    @Timed
    @UnitOfWork
    public Response runPig(@QueryParam("exerciseId") LongParam exerciseId, @QueryParam("pigScript") String pigScript) {
        PigScriptExecutor executor = new PigScriptExecutor();
        Exercise exercise = (exercisesDAO.findById(exerciseId.get())).get();
        Response response = new Response();

        if (pigScript.isEmpty()) {
            logger.debug("Pig script passed in is empty for exercise ID: " + exerciseId.toString());
            response.setSuccess(false);
            response.setErrorMessage("Please enter a pig script in the text editor.");
        } else {
            logger.debug("Pig script: " + pigScript + " passed in for exercise ID: " + exerciseId.toString());
            response = executor.executeAndAssertOutput(
                    exercise.getInputDatasets(),
                    exercise.getOutputDatasets(),
                    exercise.getLoadStatement()+
                    pigScript);
        }
        return response;
    }
}
