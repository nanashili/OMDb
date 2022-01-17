package com.test.omdb.data.network.exceptions;

public class NotFoundException extends NonSuccessfulResponseCodeException {
    public NotFoundException(String s) {
        super(404, s);
    }
}
