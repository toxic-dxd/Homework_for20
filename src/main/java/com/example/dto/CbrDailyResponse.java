package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class CbrDailyResponse {
    private Map<String, CurrencyRate> valute;

    @JsonProperty("Valute")
    public Map<String, CurrencyRate> getValute() {
        return valute;
    }

    @JsonProperty("Valute")
    public void setValute(Map<String, CurrencyRate> valute) {
        this.valute = valute;
    }

    public static class CurrencyRate {
        private String charCode;
        private double value;
        private double previous;

        @JsonProperty("CharCode")
        public String getCharCode() {
            return charCode;
        }

        @JsonProperty("CharCode")
        public void setCharCode(String charCode) {
            this.charCode = charCode;
        }

        @JsonProperty("Value")
        public double getValue() {
            return value;
        }

        @JsonProperty("Value")
        public void setValue(double value) {
            this.value = value;
        }

        @JsonProperty("Previous")
        public double getPrevious() {
            return previous;
        }

        @JsonProperty("Previous")
        public void setPrevious(double previous) {
            this.previous = previous;
        }
    }
}