package com.spring.journalapp.service;

import com.spring.journalapp.api.response.WeatherResponse;
import com.spring.journalapp.cache.AppCache;
import com.spring.journalapp.constants.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    private RestTemplate restTemplate;

    private AppCache appCache;

    @Autowired
    public WeatherService(RestTemplate restTemplate, AppCache appCache) {
        this.restTemplate = restTemplate;
        this.appCache = appCache;
    }

    public WeatherResponse getWeather(String city) {
        String apiKey = appCache.getAppCacheMap().get(AppCache.keys.WEATHER_API_TOKEN.toString());
        String finalAPI = appCache.getAppCacheMap().get(AppCache.keys.WEATHER_API_URL.toString())
                .replace(AppConstants.API_KEY, apiKey)
                .replace(AppConstants.CITY, city);
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI,
                HttpMethod.GET, null, WeatherResponse.class);
        return response.getBody();
    }
}
