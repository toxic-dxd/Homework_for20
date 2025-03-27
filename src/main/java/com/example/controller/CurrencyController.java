package com.example.controller;

import com.example.model.Currency;
import com.example.model.CurrencyRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/currencies")
public class CurrencyController {

    private final List<Currency> currencies = new ArrayList<>();

    @GetMapping
    public ResponseEntity<List<Currency>> getCurrencies() {
        return ResponseEntity.ok(currencies);
    }

    @PostMapping
    public ResponseEntity<Void> addCurrency(@RequestBody CurrencyRequest request) {
        Currency currency = new Currency();
        currency.setId(UUID.randomUUID().toString());
        currency.setName(request.getName());
        currency.setBaseCurrency(request.getBaseCurrency());
        currency.setPriceChangeRange(request.getPriceChangeRange());
        currency.setDescription(request.getDescription());
        currencies.add(currency);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Currency> getCurrency(@PathVariable String id) {
        return currencies.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCurrency(@PathVariable String id, @RequestBody CurrencyRequest request) {
        for (Currency currency : currencies) {
            if (currency.getId().equals(id)) {
                currency.setName(request.getName());
                currency.setBaseCurrency(request.getBaseCurrency());
                currency.setPriceChangeRange(request.getPriceChangeRange());
                currency.setDescription(request.getDescription());
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable String id) {
        if (currencies.removeIf(c -> c.getId().equals(id))) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

