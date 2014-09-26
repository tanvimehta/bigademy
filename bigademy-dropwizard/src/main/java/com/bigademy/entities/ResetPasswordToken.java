package com.bigademy.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Id;

/**
 * Created by mshah on 6/12/14.
 */
@Entity
@Table(name = "reset_password_token")
@NamedQueries({
        @NamedQuery(
                name = "com.bigademy.entities.ResetPasswordToken.latestResetToken",
                query = "SELECT resetPasswordToken FROM ResetPasswordToken resetPasswordToken where email = :email ORDER BY reset_password_token_id DESC"
        ),
        @NamedQuery(
                name = "com.bigademy.entities.ResetPasswordToken.deleteResetToken",
                query = "DELETE FROM ResetPasswordToken where reset_password_token_id <= :reset_password_token_id"
        )
})
public class ResetPasswordToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reset_password_token_id", nullable = false)
    private long resetPasswordId;

    @Column(name = "email")
    private String email;

    @Column(name = "token")
    private String token;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getResetPasswordId() {
        return resetPasswordId;
    }

    public void setResetPasswordId(long resetPasswordId) {
        this.resetPasswordId = resetPasswordId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
