package com.bigademy.entities;

import javax.persistence.*;

@Entity
@Table(name = "input_datasets")
public class InputDataset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "input_dataset_id", nullable = false)
    private long inputDatasetId;

    @Column(name = "alias")
    private String alias;

    @Column(name = "dataset")
    private String dataset;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false )
    private Exercise exercise;

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public long getInputDatasetId() {
        return inputDatasetId;
    }

    public void setInputDatasetId(long inputDatasetId) {
        this.inputDatasetId = inputDatasetId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }
}
