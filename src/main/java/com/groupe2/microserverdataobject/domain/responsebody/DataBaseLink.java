package com.groupe2.microserverdataobject.domain.responsebody;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataBaseLink {
    private String url;
    public DataBaseLink() {
    }
    public DataBaseLink(String url) {
        this.url = url;
    }


    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return url;
    }


}
