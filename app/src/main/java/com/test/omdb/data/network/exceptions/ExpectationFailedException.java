package com.test.omdb.data.network.exceptions;

public class ExpectationFailedException extends NonSuccessfulResponseCodeException {
    public ExpectationFailedException() {
        super(417);
    }
}
