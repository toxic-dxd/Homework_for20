package com.example.controller;

import com.example.model.Currency;
import com.example.model.CurrencyRequest;
import com.example.repository.CurrencyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/currencies")
public class CurrencyController {

    private final CurrencyRepository currencyRepository;

    public CurrencyController(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @GetMapping
    public ResponseEntity<List<Currency>> getCurrencies() {
        return ResponseEntity.ok(currencyRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Currency> addCurrency(@RequestBody CurrencyRequest request) {
        Currency currency = new Currency();
        currency.setName(request.getName());
        currency.setBaseCurrency(request.getBaseCurrency());
        currency.setPriceChangeRange(request.getPriceChangeRange());
        currency.setDescription(request.getDescription());

        Currency savedCurrency = currencyRepository.save(currency);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCurrency);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Currency> getCurrency(@PathVariable UUID id) {
        return currencyRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Currency> updateCurrency(@PathVariable UUID id, @RequestBody CurrencyRequest request) {
        return currencyRepository.findById(id)
                .map(currency -> {
                    currency.setName(request.getName());
                    currency.setBaseCurrency(request.getBaseCurrency());
                    currency.setPriceChangeRange(request.getPriceChangeRange());
                    currency.setDescription(request.getDescription());
                    return ResponseEntity.ok(currencyRepository.save(currency));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable UUID id) {
        if (currencyRepository.existsById(id)) {
            currencyRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
