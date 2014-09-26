package com.bigademy.entities;

import javax.persistence.*;

@Entity
@Table(name = "output_datasets")
public class OutputDataset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "output_dataset_id", nullable = false)
    private long outputDatasetId;

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

    public long getOutputDatasetId() {
        return outputDatasetId;
    }

    public void setOutputDatasetId(long outputDatasetId) {
        this.outputDatasetId = outputDatasetId;
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
