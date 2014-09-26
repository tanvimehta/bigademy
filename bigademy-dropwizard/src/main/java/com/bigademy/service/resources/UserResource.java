package com.bigademy.service.resources;

import com.bigademy.entities.ResetPasswordToken;
import com.bigademy.entities.User;
import com.bigademy.service.db.ResetPasswordTokenDAO;
import com.bigademy.service.db.UserDAO;
import com.bigademy.utils.EmailSender;
import com.google.common.base.Optional;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import com.yammer.metrics.annotation.Timed;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Email;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.UUID;

/**
 * Created by mshah on 4/25/14.
 */
@Path("/user")
@Produces("application/json")
public class UserResource {
    private UserDAO userDAO;
    private ResetPasswordTokenDAO resetPasswordTokenDAO;

    public UserResource(UserDAO userDAO, ResetPasswordTokenDAO resetPasswordTokenDAO) {
        this.userDAO = userDAO;
        this.resetPasswordTokenDAO = resetPasswordTokenDAO;
    }

    @GET
    @Timed
    @UnitOfWork
    @Path("verify")
    public Optional<User> verifyUser(@QueryParam("password") String password,
                                     @QueryParam("email") String email) {
        return Optional.fromNullable(userDAO.getUserByEmail(email, DigestUtils.md5Hex(password).toString()));
    }

    @GET
    @Timed
    @UnitOfWork
    @Path("createUser")
    public Optional<User> createUser(@QueryParam("name") String name,
                                     @QueryParam("email") String email,
                                     @QueryParam("password") String password) {
        Optional<User> userOptional = Optional.absent();
        if (!userDAO.verifyUserExists(email)) {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(DigestUtils.md5Hex(password));
            user.setToken(UUID.randomUUID().toString());
            userOptional = Optional.fromNullable(userDAO.persistUser(user));
        }

        return userOptional;
    }

    @GET
    @Timed
    @UnitOfWork
    @Path("resetPassword")
    public boolean resetPassword(@QueryParam("email") String email) {
        User user = userDAO.getUserByEmail(email);
        if (user != null) {
            String token = UUID.randomUUID().toString();
            ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
            resetPasswordToken.setEmail(email);
            resetPasswordToken.setToken(token);
            resetPasswordTokenDAO.persistResetToken(resetPasswordToken);
            EmailSender.sendEmail(email, "Password change link", "wwww.bigademy.com/user/email=" + email + "&token=" + token);
            return true;
        }
        return false;
    }

    @GET
    @Timed
    @UnitOfWork
    @Path("changePassword")
    public boolean changePassword(@QueryParam("email") String email,
                                  @QueryParam("newPassword") String newPassword,
                                  @QueryParam("resetToken") String resetToken) {
        ResetPasswordToken resetPasswordToken = resetPasswordTokenDAO.getLatestResetPasswordToken(email);
        if (resetPasswordToken != null && StringUtils.equals(resetToken, resetPasswordToken.getToken())) {
            User user = userDAO.getUserByEmail(email);
            user.setPassword(DigestUtils.md5Hex(newPassword));
            user.setToken(UUID.randomUUID().toString());
            userDAO.persistUser(user);
            resetPasswordTokenDAO.deleteResetTokens(resetPasswordToken.getResetPasswordId());
            return true;
        }
        return false;
    }
}
