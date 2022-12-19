package org.nsu.fit.services.rest.data;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerPojo extends ContactPojo {

    @JsonProperty("id")
    public UUID id;

    public CustomerPojo(String firstName,
                        String lastName,
                        String login,
                        String pass,
                        int balance) {
        super(firstName, lastName, login, pass, balance);
    }

}
