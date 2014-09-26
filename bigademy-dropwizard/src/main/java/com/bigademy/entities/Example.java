package com.bigademy.entities;

import javax.persistence.*;

@Entity
@Table(name = "examples")
public class Example {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "example_id", nullable = false)
    private long exampleId;

    @Column(name = "example")
    private String example;
    
    @Column(name = "example_name")
    private String exampleName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false )
    private Exercise exercise;

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public long getExampleId() {
        return exampleId;
    }

    public void setExampleId(long exampleId) {
        this.exampleId = exampleId;
    }
    
    public String getExampleName() {
        return exampleName;
    }

    public void setExampleId(String exampleName) {
        this.exampleName = exampleName;
    }
}
