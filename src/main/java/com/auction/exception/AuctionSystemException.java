package com.auction.exception;

import org.springframework.http.HttpStatus;


public class AuctionSystemException extends IllegalArgumentException {

	private static final long serialVersionUID = 1L;

	private HttpStatus status;
	private String message;

	public AuctionSystemException(HttpStatus status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public AuctionSystemException(HttpStatus status, String message, String message2) {
		super();
		this.status = status;
		this.message = message;
		this.message = message2;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

