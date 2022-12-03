package org.nsu.fit.tm_backend.shared;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;

public class JsonMapper {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final JsonFactory JSON_FACTORY = new JsonFactory();

    public static <T> T fromJson(String jsonAsString, Class<T> pojoClass) {
        try {
            return MAPPER.readValue(jsonAsString, pojoClass);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String toJson(Object pojo, boolean prettyPrint) {
        try {
            StringWriter sw = new StringWriter();
            JsonGenerator jg = JSON_FACTORY.createGenerator(sw);
            if (prettyPrint) {
                jg.useDefaultPrettyPrinter();
            }
            MAPPER.writeValue(jg, pojo);
            return sw.toString();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
