package com.bigademy.entities;

import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "exercises")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_id", nullable = false)
    private long exerciseId;

    @Column(name = "exercise_name", nullable = false)
    private String exerciseName;

    @Column(name = "explanation")
    private String explanation;

    @Column(name = "problem")
    private String problem;

    @Column(name = "solution")
    private String solution;

    @Column(name = "load_statement")
    private String loadStatement;

    @Column(name = "store_statement")
    private String storeStatement;

    @Column(name = "pass_to_backend")
    private boolean passToBackend;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subtopic_id", nullable = false )
    private Subtopic subtopic;
    
    @JoinColumn(name = "exercise_id", referencedColumnName = "exercise_id")
    @OneToMany(fetch = FetchType.EAGER)
    @OrderBy
    protected Set<Example> examples;

    @JoinColumn(name = "exercise_id", referencedColumnName = "exercise_id")
    @OneToMany(fetch = FetchType.EAGER)
    @OrderBy
    protected Set<Hint> hints;

    @JoinColumn(name = "exercise_id", referencedColumnName = "exercise_id")
    @OneToMany(fetch = FetchType.EAGER)
    @OrderBy
    protected Set<InputDataset> inputDatasets;

    @JoinColumn(name = "exercise_id", referencedColumnName = "exercise_id")
    @OneToMany(fetch = FetchType.EAGER)
    @OrderBy
    protected Set<OutputDataset> outputDatasets;

    public void setSubtopic(Subtopic subtopic){
        this.subtopic = subtopic;
     }

    public Subtopic getSubtopic(){
       return subtopic;
    }

    public long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }
    
    public Set<Example> getExamples() {
        return examples;
    }

    public void setExamples(Set<Example> examples) {
        this.examples = examples;
    }

    public Set<Hint> getHints() {
        return hints;
    }

    public void setHints(Set<Hint> hints) {
        this.hints = hints;
    }

    public Set<InputDataset> getInputDatasets() {
        return inputDatasets;
    }

    public void setInputDatasets(Set<InputDataset> inputDatasets) {
        this.inputDatasets = inputDatasets;
    }

    public void setOutputDatasets(Set<OutputDataset> outputDatasets) {
        this.outputDatasets = outputDatasets;
    }

    public Set<OutputDataset> getOutputDatasets() {
        return outputDatasets;
    }

    public String getLoadStatement() {
        return loadStatement;
    }

    public void setLoadStatement(String loadStatement) {
        this.loadStatement = loadStatement;
    }

    public String getStoreStatement() {
        return storeStatement;
    }

    public void setStoreStatement(String storeStatement) {
        this.storeStatement = storeStatement;
    }

    public boolean isPassToBackend() {
        return passToBackend;
    }

    public void setPassToBackend(boolean passToBackend) {
        this.passToBackend = passToBackend;
    }
}
