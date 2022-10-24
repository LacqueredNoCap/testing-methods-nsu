package org.nsu.fit.tm_backend.repository.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Getter @Setter
public class SubscriptionPojo {
    @JsonProperty("id")
    public UUID id;

    @JsonProperty("customer_id")
    public UUID customerId;

    @JsonProperty("plan_id")
    public UUID planId;

    @JsonProperty("plan_name")
    public String planName;

    @JsonProperty("plan_details")
    public String planDetails;

    @JsonProperty("plan_fee")
    public Integer planFee;
}
