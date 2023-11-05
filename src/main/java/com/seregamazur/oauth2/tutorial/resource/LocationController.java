package com.seregamazur.oauth2.tutorial.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.seregamazur.oauth2.tutorial.client.model.location.LocationDTO;
import com.seregamazur.oauth2.tutorial.client.model.location.LocationRequest;
import com.seregamazur.oauth2.tutorial.service.LocationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LocationController {

    private final LocationService service;

    @Transactional
    @PostMapping(value = "/api/v1/location")
    public ResponseEntity<LocationDTO.LocationComponents> getLocationByCoordinates(
        @RequestBody LocationRequest location) {
        LocationDTO.LocationComponents components = service.getCountry(location.getLatitude(), location.getLongitude());
        return ResponseEntity.ok().body(components);
    }
}
