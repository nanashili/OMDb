package com.test.omdb.data.network.exceptions;

import java.io.IOException;

public class NetworkControllerException extends IOException {

    public NetworkControllerException(Exception exception) {
        super(exception);
    }

    public NetworkControllerException(String s) {
        super(s);
    }

}
