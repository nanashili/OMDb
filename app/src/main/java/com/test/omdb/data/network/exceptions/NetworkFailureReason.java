package com.test.omdb.data.network.exceptions;

import androidx.annotation.NonNull;

import java.io.IOException;

public enum NetworkFailureReason {
    AUTH,
    NETWORK,
    OTHER;

    public static @NonNull
    NetworkFailureReason fromException(@NonNull Throwable e) {
        if (e instanceof AuthorizationFailedException) return NetworkFailureReason.AUTH;
        if (e instanceof IOException) return NetworkFailureReason.NETWORK;
        return NetworkFailureReason.OTHER;
    }
}
