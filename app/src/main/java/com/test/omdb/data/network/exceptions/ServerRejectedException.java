package com.test.omdb.data.network.exceptions;

/**
 * Indicates the server has rejected the request and we should stop retrying.
 */
public class ServerRejectedException extends NonSuccessfulResponseCodeException {
    public ServerRejectedException() {
        super(508);
    }
}
