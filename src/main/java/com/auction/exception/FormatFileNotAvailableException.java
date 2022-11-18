package com.auction.exception;

public class FormatFileNotAvailableException extends IllegalArgumentException {

    public FormatFileNotAvailableException(String format) {
        super("Format File '"+ format +"' Is Not Available");
    }

}
