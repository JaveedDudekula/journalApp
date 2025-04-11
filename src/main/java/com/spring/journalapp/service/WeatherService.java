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

    private RedisService redisService;

    @Autowired
    public WeatherService(RestTemplate restTemplate, AppCache appCache, RedisService redisService) {
        this.restTemplate = restTemplate;
        this.appCache = appCache;
        this.redisService = redisService;
    }

    public WeatherResponse getWeather(String city) {
        WeatherResponse weatherResponse = redisService
                .get("weather_of_" + city.toLowerCase().replace(" ", "_"), WeatherResponse.class);
        if (weatherResponse != null) {
            return weatherResponse;
        }
        String apiKey = appCache.getAppCacheMap().get(AppCache.keys.WEATHER_API_TOKEN.toString());
        String finalAPI = appCache.getAppCacheMap().get(AppCache.keys.WEATHER_API_URL.toString())
                .replace(AppConstants.API_KEY, apiKey)
                .replace(AppConstants.CITY, city);
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI,
                HttpMethod.GET, null, WeatherResponse.class);
        WeatherResponse body = response.getBody();
        if (body != null && body.getCurrent() != null) {
            redisService.set("weather_of_" + city.toLowerCase().replace(" ", "_"), body, 3600l);
        }
        return body;
    }
}
