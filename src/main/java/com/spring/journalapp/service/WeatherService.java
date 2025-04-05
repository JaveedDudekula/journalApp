package com.spring.journalapp.service;

import com.spring.journalapp.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {

    private static final String API_KEY = "0ba81c2f2ad33dc2bab0d3d40c6c9b48";

    private static final String API = "https://api.weatherstack.com/current?access_key=API_KEY&query=CITY";

    private RestTemplate restTemplate;

    @Autowired
    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WeatherResponse getWeather(String city) {
        String finalAPI = API.replace("API_KEY", API_KEY).replace("CITY", city);
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI,
                HttpMethod.GET, null, WeatherResponse.class);
        return response.getBody();
    }
}
