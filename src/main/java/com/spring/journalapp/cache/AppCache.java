package com.spring.journalapp.cache;

import com.spring.journalapp.entity.ConfigJournalAppEntity;
import com.spring.journalapp.repository.ConfigJournalAppRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

    public enum keys {
        WEATHER_API_URL,
        WEATHER_API_TOKEN
    }

    private final ConfigJournalAppRepository configJournalAppRepository;

    private Map<String, String> appCacheMap;

    @Autowired
    public AppCache(ConfigJournalAppRepository configJournalAppRepository) {
        this.configJournalAppRepository = configJournalAppRepository;
    }

    @PostConstruct
    public void init() {
        appCacheMap = new HashMap<>();
        List<ConfigJournalAppEntity> configList = configJournalAppRepository.findAll();
        for (ConfigJournalAppEntity config : configList) {
            appCacheMap.put(config.getKey(), config.getValue());
        }
    }

    public Map<String, String> getAppCacheMap() {
        return appCacheMap;
    }
}
