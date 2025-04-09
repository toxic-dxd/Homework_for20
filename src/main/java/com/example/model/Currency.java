package com.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "currency")
public class Currency {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "base_currency", nullable = false, length = 3)
    private String baseCurrency;

    @Column(name = "price_change_range", length = 50)
    private String priceChangeRange;

    @Column(columnDefinition = "TEXT")
    private String description;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getPriceChangeRange() {
        return priceChangeRange;
    }

    public void setPriceChangeRange(String priceChangeRange) {
        this.priceChangeRange = priceChangeRange;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}