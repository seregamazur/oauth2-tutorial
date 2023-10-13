package com.seregamazur.oauth2.tutorial.client.location;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.seregamazur.oauth2.tutorial.client.model.location.LocationDTO;

@FeignClient(name = "locationClient", url = "https://api.opencagedata.com")
public interface LocationFeignClient {

    @GetMapping(value = "/geocode/v1/json?q={lat}%2C{lng}&key={apiKey}",
        headers = { "Content-Type=application/json", "Accept=application/json" })
    LocationDTO getLocationByCoordinates(@PathVariable("lat") String latitude,
        @PathVariable("lng") String longitude,
        @PathVariable("apiKey") String apiKey);
}
