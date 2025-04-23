package com.example.service;

import com.example.dto.CbrDailyResponse;
import com.example.model.Currency;
import com.example.repository.CurrencyRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CurrencyRateCheckService {

    private static final String CBR_API_URL = "https://www.cbr-xml-daily.ru/daily_json.js";
    private final RestTemplate restTemplate;
    private final CurrencyRepository currencyRepository;

    private static final String RED = "\033[91m";
    private static final String YELLOW = "\033[93m";
    private static final String GREEN = "\033[92m";
    private static final String RESET = "\033[0m";

    public CurrencyRateCheckService(RestTemplate restTemplate,
                                    CurrencyRepository currencyRepository) {
        this.restTemplate = restTemplate;
        this.currencyRepository = currencyRepository;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void checkCurrencyRates() {
        printWithTimestamp(GREEN + "=== Начало проверки курсов валют ===" + RESET);

        try {
            CbrDailyResponse response = restTemplate.getForObject(CBR_API_URL, CbrDailyResponse.class);
            if (response == null || response.getValute() == null) {
                printError("Не удалось получить данные от API ЦБ");
                return;
            }

            List<Currency> trackedCurrencies = currencyRepository.findAll();
            if (trackedCurrencies.isEmpty()) {
                printWithTimestamp("В базе данных нет валют для отслеживания");
                return;
            }

            for (Currency currency : trackedCurrencies) {
                CbrDailyResponse.CurrencyRate rate = response.getValute().get(currency.getBaseCurrency());
                if (rate != null) {
                    checkCurrencyChange(currency, rate);
                } else {
                    printWarning("Валюта " + currency.getBaseCurrency() + " не найдена в ответе ЦБ");
                }
            }
        } catch (Exception e) {
            printError("Ошибка при проверке курсов валют: " + e.getMessage());
        }
    }

    private void checkCurrencyChange(Currency currency, CbrDailyResponse.CurrencyRate rate) {
        double changePercentage = calculatePercentageChange(rate.getPrevious(), rate.getValue());
        String range = currency.getPriceChangeRange();

        if (range != null && !range.isEmpty()) {
            try {
                double threshold = parsePercentage(range);
                if (Math.abs(changePercentage) >= threshold) {
                    String direction = changePercentage > 0 ? "вырос" : "упал";
                    String message = String.format("%s %s на %.2f%% (с %.4f до %.4f)",
                            currency.getName(),
                            direction,
                            Math.abs(changePercentage),
                            rate.getPrevious(),
                            rate.getValue());

                    if (currency.getDescription() != null && !currency.getDescription().isEmpty()) {
                        message = currency.getDescription();
                    }

                    if (changePercentage < 0) {
                        printAlert(message);
                    } else {
                        printWithTimestamp(message);
                    }
                }
            } catch (NumberFormatException e) {
                printWarning("Некорректный формат диапазона для " + currency.getBaseCurrency() + ": " + range);
            }
        }
    }
    
    private void printWithTimestamp(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("[" + timestamp + "] " + message);
    }

    private void printAlert(String message) {
        System.out.println(RED + "[!] " + message + RESET);
    }

    private void printWarning(String message) {
        System.out.println(YELLOW + "[ВНИМАНИЕ] " + message + RESET);
    }

    private void printError(String message) {
        System.err.println(RED + "[ОШИБКА] " + message + RESET);
    }

    private double calculatePercentageChange(double previous, double current) {
        return ((current - previous) / previous) * 100;
    }

    private double parsePercentage(String range) throws NumberFormatException {
        return Math.abs(Double.parseDouble(range.replace("%", "").trim()));
    }
}