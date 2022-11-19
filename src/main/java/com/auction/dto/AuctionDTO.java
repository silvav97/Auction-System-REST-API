package com.auction.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class AuctionDTO {

    @NotBlank(message = "Product may not be blank or null")
    @Size(min=2, max = 20, message = "Product must have at least 2 characters and less than 20")
    private String product;

    @NotBlank(message = "Description may not be blank or null")
    @Size(min=3, max = 50, message = "Description must have at least 3 characters and less than 50")
    private String description;

    @Positive(message = "Initial value may be positive")
    private Float initialValue;

    public AuctionDTO() {
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(Float initialValue) {
        this.initialValue = initialValue;
    }
}
