package com.bigademy.service.db;

import com.bigademy.entities.ResetPasswordToken;
import com.yammer.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by mshah on 6/12/14.
 */
public class ResetPasswordTokenDAO extends AbstractDAO<ResetPasswordToken> {
    public ResetPasswordTokenDAO(SessionFactory factory) {
        super(factory);
    }

    public ResetPasswordToken persistResetToken(ResetPasswordToken resetPasswordToken) {
        return this.persist(resetPasswordToken);
    }

    public ResetPasswordToken getLatestResetPasswordToken(String email) {
        List<ResetPasswordToken> resetPasswordTokenList = namedQuery("com.bigademy.entities.ResetPasswordToken.latestResetToken").
                        setString("email", email).list();
        if (resetPasswordTokenList != null && resetPasswordTokenList.size() > 0) {
            return resetPasswordTokenList.get(0);
        }
        return null;
    }

    public void deleteResetTokens(long id) {
        namedQuery("com.bigademy.entities.ResetPasswordToken.deleteResetToken").setBigInteger("reset_password_token_id", BigInteger.valueOf(id)).executeUpdate();
    }
}
