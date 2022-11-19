package com.auction.dto;

import javax.validation.constraints.*;

public class SignupDTO {

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

	@NotBlank(message = "Name may not be blank or null")
	@Size(min=3, message = "Name must have at least 3 characters")
	private String name;

	@NotBlank(message = "Password may not be blank or null")
	@Size(min=4, message = "Password must have at least 4 characters")
	private String password;

	@NotBlank(message = "Username may not be blank or null")
	@Size(min=3, message = "Username must have at least 3 characters")
	private String username;

	public SignupDTO() {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
