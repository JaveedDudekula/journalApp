package com.spring.journalapp.service;

import com.spring.journalapp.api.response.WeatherResponse;
import com.spring.journalapp.cache.AppCache;
import com.spring.journalapp.constants.AppConstants;
import com.spring.journalapp.exceptions.WeatherServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class WeatherService {

    private final RestClient restClient;

    private final AppCache appCache;

    private final RedisService redisService;

    @Autowired
    public WeatherService(RestClient restClient, AppCache appCache, RedisService redisService) {
        this.restClient = restClient;
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

        weatherResponse = restClient.get()
                .uri(finalAPI)
                .retrieve()
                .onStatus(HttpStatusCode::isError, ((req, res) -> {
                    throw new WeatherServiceException("Issue observed at Weather API service");
                }))
                .body(WeatherResponse.class);

        if (weatherResponse != null && weatherResponse.getCurrent() != null) {
            redisService.set("weather_of_" + city.toLowerCase().replace(" ", "_"),
                    weatherResponse, 3600L);
        }
        return weatherResponse;
    }
}
