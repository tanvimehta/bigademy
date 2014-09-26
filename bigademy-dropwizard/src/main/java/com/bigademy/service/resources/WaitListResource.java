package com.bigademy.service.resources;

import com.bigademy.entities.WaitList;
import com.bigademy.service.db.WaitListDAO;
import com.bigademy.utils.EmailSender;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import com.yammer.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.Properties;

/**
 * Created by mshah on 4/25/14.
 */
@Path("/waitList")
public class WaitListResource {

    private static final Logger logger = LoggerFactory.getLogger(WaitListResource.class);
    private WaitListDAO waitListDAO;

    public WaitListResource(WaitListDAO waitListDAO) {
        this.waitListDAO = waitListDAO;
    }

    @PUT
    @Path("/add")
    @Timed
    @UnitOfWork
    public void addToWaitList(@QueryParam("email") String email) {
        logger.info("Adding email: " + email + " to waitlist.");
        WaitList waitList = new WaitList();
        waitList.setEmail(email);
        waitListDAO.create(waitList);
        EmailSender.sendEmail(email, "Welcome to Bigademy!", "Thanks for signing up to our waitlist!" +
                " We are excited to have you join us and start learning.<br/><br/><b>" +
                "A little about us:</b><br/>Bigademy is an interactive learning web application " +
                "to help you learn <b>Big Data Technologies</b>. " +
                "We have designed courses for technologies such as Apache Pig to " +
                "help you learn these new concepts not only theoretically but also practically! " +
                "Bigademy allows you to understand the fundamentals of these technologies through " +
                "the <b>course material</b> and additionally lets you practise and perfect " +
                "the skills you acquire by using our <b>interactive console</b>.<br/></br/>We are " +
                "currently working towards perfecting our website to give you " +
                "the best experience. We will contact you as soon as you are at " +
                "the top of our waitlist. Please stay tuned for more news and " +
                "updates!<br/><br/>If you have any questions, concerns or requests," +
                " please contact us at <a href=\\\"mailto:team@bigademy.com\\\">" +
                "team@bigademy.com</a>.<br/><br/>Cheers,<br/>The Bigademy Team");
    }
}
