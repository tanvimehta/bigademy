package com.bigademy.serializers;

import java.io.IOException;

import com.bigademy.entities.Subtopic;
import com.bigademy.entities.Topic;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class TopicSerializer extends JsonSerializer<Topic> {

    @Override
    public void serialize(Topic topic, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {

        jgen.writeStartObject();

        jgen.writeFieldName("topicId");
        jgen.writeNumber(topic.getTopicId());

        jgen.writeFieldName("courseId");
        jgen.writeNumber(topic.getCourseId());

        jgen.writeStringField("topicName", topic.getTopicName());

        jgen.writeStringField("explanation", topic.getExplanation());

        jgen.writeArrayFieldStart("subtopics");

        SubtopicSerializer subSerializer = new SubtopicSerializer();
        for(Subtopic subtopic: topic.getSubtopics()) {
            subSerializer.serialize(subtopic, jgen, provider);
        }

        jgen.writeEndArray();
        jgen.writeEndObject();
    }
}
