package com.test.omdb.data.models;

import androidx.annotation.NonNull;

public class Ratings {

    private String source;
    private String value;

    public Ratings(){}

    public Ratings(@NonNull String source, @NonNull String value){
        this.source = source;
        this.value = value;
    }

    public String getSource() {
        return source;
    }

    public String getValue() {
        return value;
    }
}
