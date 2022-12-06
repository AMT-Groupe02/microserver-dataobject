package com.groupe2.microserverdataobject.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataBaseLink {
    private String url;
    public DataBaseLink() {
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return url;
    }


}
