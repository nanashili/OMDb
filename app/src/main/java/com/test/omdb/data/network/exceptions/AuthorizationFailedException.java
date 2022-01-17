package com.test.omdb.data.network.exceptions;

/**
 * We return an AuthorizationFailedException when the api key
 * has been either removed or is not correct
 */
public class AuthorizationFailedException extends NonSuccessfulResponseCodeException {
    public AuthorizationFailedException(int code, String s) {
        super(code, s);
    }
}
