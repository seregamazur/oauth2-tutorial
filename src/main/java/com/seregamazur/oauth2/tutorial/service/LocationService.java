package com.seregamazur.oauth2.tutorial.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.seregamazur.oauth2.tutorial.client.location.LocationFeignClient;
import com.seregamazur.oauth2.tutorial.client.model.location.LocationDTO;
import com.seregamazur.oauth2.tutorial.client.model.location.LocationException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LocationService {
    @Autowired
    private LocationFeignClient locationFeignClient;

    @Value("${opencagedata.api-key}")
    private String apiKey;

    public LocationDTO.LocationComponents getCountry(String latitude, String longitude) {
        log.info("Get location using next params: {},{}", latitude, longitude);
        LocationDTO location = locationFeignClient.getLocationByCoordinates(latitude, longitude, apiKey);
        LocationDTO.LocationComponents components = location.getResults().stream().findFirst()
            .orElseThrow(
                () -> new LocationException("Location with lat:" + latitude + " and long:" + longitude + " cannot be identified!"))
            .getComponents();
        log.info("Country code: {}", components.getCountryCode());
        return components;
    }
}
