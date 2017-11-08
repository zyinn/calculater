package com.sumscope.optimus.tfcalculator.planalysis.gateway.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * Created by Administrator on 2016/11/18.
 * 节假日model
 */
public class HolidayInfo {
    @JsonProperty("holiday_reason")
    private String holidayReason;
    @JsonProperty("holiday_date")
    private String holidayDate;
    @JsonProperty("market_type")
    private String marketType;
    @JsonProperty("country")
    private String country;

    public String getHolidayReason() {
        return holidayReason;
    }

    public void setHolidayReason(String holidayReason) {
        this.holidayReason = holidayReason;
    }

    public String getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(String holidayDate) {
        this.holidayDate = holidayDate;
    }

    public String getMarketType() {
        return marketType;
    }

    public void setMarketType(String marketType) {
        this.marketType = marketType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
