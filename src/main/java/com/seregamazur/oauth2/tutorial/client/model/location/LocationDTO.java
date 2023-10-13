package com.seregamazur.oauth2.tutorial.client.model.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationDTO {
    private List<LocationResult> results;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LocationResult {
        private LocationComponents components;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LocationComponents {
        private String continent;
        private String country;
        @JsonProperty("country_code")
        private String countryCode;
        private String postcode;
    }
}
