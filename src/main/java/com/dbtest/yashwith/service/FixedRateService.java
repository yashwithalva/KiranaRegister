package com.dbtest.yashwith.service;

import com.dbtest.yashwith.model.transaction.ExchangeRates;
import org.hibernate.annotations.NaturalIdCache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class FixedRateService {
    static RestTemplate restTemplate = new RestTemplate();

    @Cacheable(value = "cache")
    public ExchangeRates getFixedRate(String url){
        return restTemplate.getForObject(url, ExchangeRates.class);
    }
}
