package org.nsu.fit.services.rest.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HealthCheckPojo {

    @JsonProperty("status")
    public String status;

    @JsonProperty("db_status")
    public String dbStatus;

}
