package com.bigademy.service;

import com.bigademy.entities.Course;
import com.bigademy.entities.Example;
import com.bigademy.entities.Exercise;
import com.bigademy.entities.Hint;
import com.bigademy.entities.InputDataset;
import com.bigademy.entities.OutputDataset;
import com.bigademy.entities.ResetPasswordToken;
import com.bigademy.entities.Subtopic;
import com.bigademy.entities.Topic;
import com.bigademy.entities.User;
import com.bigademy.entities.WaitList;
import com.bigademy.service.db.CourseDAO;
import com.bigademy.service.db.ExerciseDAO;
import com.bigademy.service.db.ResetPasswordTokenDAO;
import com.bigademy.service.db.SubtopicDAO;
import com.bigademy.service.db.TopicDAO;
import com.bigademy.service.db.UserDAO;
import com.bigademy.service.db.WaitListDAO;
import com.bigademy.service.resources.BigademyResource;
import com.bigademy.service.resources.CoursesResource;
import com.bigademy.service.resources.PigRunResource;
import com.bigademy.service.resources.SubtopicsResource;
import com.bigademy.service.resources.TopicsResource;
import com.bigademy.service.resources.UserResource;
import com.bigademy.service.resources.WaitListResource;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.db.DatabaseConfiguration;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import com.yammer.dropwizard.migrations.MigrationsBundle;
import com.bigademy.service.health.TemplateHealthCheck;

/**
 * To run the service, run mvn package followed by java -jar target/bigademy-dropwizard-1.0-SNAPSHOT.jar db migrate src/main/resources/bigademy-config.yaml
 *
 */
public class BigademyService extends Service<BigademyConfiguration> {

    public static void main(String[] args) throws Exception {
        new BigademyService().run(args);
    }

    private final HibernateBundle<BigademyConfiguration> hibernate = new HibernateBundle<BigademyConfiguration>(
            Subtopic.class,
            Exercise.class,
            Hint.class,
            InputDataset.class,
            OutputDataset.class,
            Topic.class,
            Course.class,
            User.class,
            WaitList.class,
            Example.class,
            ResetPasswordToken.class) {
        @Override
        public DatabaseConfiguration getDatabaseConfiguration(BigademyConfiguration configuration) {
            return configuration.getDatabaseConfiguration();
        }
    };

    private final MigrationsBundle<BigademyConfiguration> migration = new MigrationsBundle<BigademyConfiguration>() {
        @Override
        public DatabaseConfiguration getDatabaseConfiguration(BigademyConfiguration configuration) {
            return configuration.getDatabaseConfiguration();
        }
    };

    @Override
    public void initialize(Bootstrap<BigademyConfiguration> bootstrap) {
        bootstrap.setName("bigademy-service");
        bootstrap.addBundle(migration);
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(BigademyConfiguration configuration,
                    Environment environment) {
        final String template = configuration.getTemplate();
        final String defaultName = configuration.getDefaultName();
        final SubtopicDAO subtopicDao = new SubtopicDAO(hibernate.getSessionFactory());
        final TopicDAO topicDao = new TopicDAO(hibernate.getSessionFactory());
        final CourseDAO courseDao = new CourseDAO(hibernate.getSessionFactory());
        final ExerciseDAO exerciseDao = new ExerciseDAO(hibernate.getSessionFactory());
        final UserDAO userDAO = new UserDAO(hibernate.getSessionFactory());
        final WaitListDAO waitListDAO = new WaitListDAO(hibernate.getSessionFactory());
        final ResetPasswordTokenDAO resetPasswordTokenDAO = new ResetPasswordTokenDAO(hibernate.getSessionFactory());

        environment.addResource(new SubtopicsResource(subtopicDao));
        environment.addResource(new TopicsResource(topicDao));
        environment.addResource(new CoursesResource(courseDao));
        environment.addResource(new BigademyResource(template, defaultName));
        environment.addResource(new UserResource(userDAO, resetPasswordTokenDAO));
        environment.addResource(new WaitListResource(waitListDAO));
        environment.addResource(new PigRunResource(exerciseDao));
        environment.addHealthCheck(new TemplateHealthCheck(template));
    }
}