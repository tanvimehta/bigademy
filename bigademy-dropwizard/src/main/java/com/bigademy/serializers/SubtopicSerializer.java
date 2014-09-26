package com.bigademy.serializers;

import java.io.IOException;

import com.bigademy.entities.Example;
import com.bigademy.entities.Exercise;
import com.bigademy.entities.Hint;
import com.bigademy.entities.InputDataset;
import com.bigademy.entities.OutputDataset;
import com.bigademy.entities.Subtopic;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class SubtopicSerializer extends JsonSerializer<Subtopic> {

    @Override
    public void serialize(Subtopic subtopic, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {

        jgen.writeStartObject();

        jgen.writeFieldName("subtopicId");
        jgen.writeNumber(subtopic.getSubtopicId());

        jgen.writeFieldName("topicId");
        jgen.writeNumber(subtopic.getTopicId());

        jgen.writeStringField("subtopicName", subtopic.getSubtopicName());

        jgen.writeStringField("explanation", subtopic.getExplanation());

        jgen.writeArrayFieldStart("exercises");
        for(Exercise exercise: subtopic.getExercises()) {

            jgen.writeStartObject();

            jgen.writeFieldName("exerciseId");
            jgen.writeNumber(exercise.getExerciseId());

            jgen.writeStringField("exerciseName", exercise.getExerciseName());
            jgen.writeStringField("explanation", exercise.getExplanation());
            jgen.writeStringField("problem", exercise.getProblem());
            jgen.writeStringField("solution", exercise.getSolution());
            jgen.writeStringField("loadStatement", exercise.getLoadStatement());
            jgen.writeStringField("storeStatement", exercise.getStoreStatement());
            jgen.writeBooleanField("passToBackend", exercise.isPassToBackend());

            jgen.writeArrayFieldStart("hints");
            for(Hint hint: exercise.getHints()) {
                jgen.writeStartObject();
                jgen.writeFieldName("hintId");
                jgen.writeNumber(hint.getHintId());
                jgen.writeStringField("hint", hint.getHint());
                jgen.writeEndObject();
            }
            jgen.writeEndArray();
            
            jgen.writeArrayFieldStart("examples");
            for(Example example: exercise.getExamples()) {
                jgen.writeStartObject();
                jgen.writeFieldName("exampleId");
                jgen.writeNumber(example.getExampleId());
                jgen.writeStringField("exampleName", example.getExampleName());
                jgen.writeStringField("example", example.getExample());
                jgen.writeEndObject();
            }
            jgen.writeEndArray();

            jgen.writeArrayFieldStart("inputDatasets");
            for(InputDataset inputDataset: exercise.getInputDatasets()) {
                jgen.writeStartObject();
                jgen.writeFieldName("inputDataset");
                jgen.writeNumber(inputDataset.getInputDatasetId());
                jgen.writeStringField("alias", inputDataset.getAlias());
                jgen.writeStringField("dataset", inputDataset.getDataset());
                jgen.writeEndObject();
            }
            jgen.writeEndArray();

            jgen.writeArrayFieldStart("outputDatasets");
            for(OutputDataset outputDataset: exercise.getOutputDatasets()) {
                jgen.writeStartObject();
                jgen.writeFieldName("ouptutDataset");
                jgen.writeNumber(outputDataset.getOutputDatasetId());
                jgen.writeStringField("alias", outputDataset.getAlias());
                jgen.writeStringField("dataset", outputDataset.getDataset());
                jgen.writeEndObject();
            }
            jgen.writeEndArray();

            jgen.writeEndObject();
        }
        jgen.writeEndArray();

        jgen.writeStringField("field", "value");
        jgen.writeEndObject();
    }
}
