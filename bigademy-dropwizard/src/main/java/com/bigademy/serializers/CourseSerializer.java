package com.bigademy.serializers;

import java.io.IOException;

import com.bigademy.entities.Course;
import com.bigademy.entities.Exercise;
import com.bigademy.entities.Subtopic;
import com.bigademy.entities.Topic;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CourseSerializer extends JsonSerializer<Course> {

    @Override
    public void serialize(Course course, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {

        jgen.writeStartObject();

        jgen.writeFieldName("courseId");
        jgen.writeNumber(course.getCourseId());

        jgen.writeStringField("courseName", course.getCourseName());

        jgen.writeStringField("explanation", course.getExplanation());

        jgen.writeStringField("image", course.getImage());

        jgen.writeArrayFieldStart("topics");

        for (Topic topic : course.getTopics()) {
            jgen.writeStartObject();
            jgen.writeFieldName("topicId");
            jgen.writeNumber(topic.getTopicId());

            jgen.writeStringField("topicName", topic.getTopicName());

            jgen.writeArrayFieldStart("subtopics");
            for (Subtopic subtopic : topic.getSubtopics()) {
                jgen.writeStartObject();
                jgen.writeFieldName("subtopicId");
                jgen.writeNumber(subtopic.getSubtopicId());

                jgen.writeStringField("subtopicName", subtopic.getSubtopicName());

                jgen.writeArrayFieldStart("exercises");
                for (Exercise exercise : subtopic.getExercises()) {
                    jgen.writeStartObject();
                    jgen.writeFieldName("exerciseId");
                    jgen.writeNumber(exercise.getExerciseId());

                    jgen.writeStringField("exerciseName", exercise.getExerciseName());
                    jgen.writeEndObject();
                }
                jgen.writeEndArray();
                jgen.writeEndObject();
            }
            jgen.writeEndArray();
            jgen.writeEndObject();
        }
        jgen.writeEndArray();
        jgen.writeEndObject();
    }
}
