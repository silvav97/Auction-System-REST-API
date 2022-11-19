package com.auction.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UpdateUserInformationDTO {

    @NotBlank(message = "Address may not be blank or null")
    @Size(min=3, message = "Address must have at least 3 characters")
    private String address;

    @NotBlank(message = "Cellphone may not be blank or null")
    @Size(min=6, message = "Cellphone must have at least 6 characters")
    private String cellPhone;

    @NotBlank(message = "City may not be blank or null")
    @Size(min=3, message = "City must have at least 3 characters")
    private String city;

    @NotBlank(message = "Document number may not be blank or null")
    @Size(min=7, message = "Document number must have at least 7 characters")
    private String documentNumber;

    @NotBlank(message = "Email may not be blank or null")
    @Email(message = "Email field must follow a mail pattern")
    private String email;

    public UpdateUserInformationDTO() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
