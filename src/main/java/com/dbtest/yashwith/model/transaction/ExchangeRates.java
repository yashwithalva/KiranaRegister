package com.dbtest.yashwith.model.transaction;

import java.util.Map;
import lombok.Data;

@Data
public class ExchangeRates {
    private boolean success;
    private String terms;
    private String privacy;
    private long timestamp;
    private String date;
    private String base;
    private Map<String, Double> rates;

    public double getExchangeRates(String countryCode) {
        return rates.get(countryCode);
    }
}
