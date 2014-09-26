package com.bigademy.entities;

import javax.persistence.*;

@Entity
@Table(name = "hints")
public class Hint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hint_id", nullable = false)
    private long hintId;

    @Column(name = "hint")
    private String hint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false )
    private Exercise exercise;

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public long getHintId() {
        return hintId;
    }

    public void setHintId(long hintId) {
        this.hintId = hintId;
    }
}
