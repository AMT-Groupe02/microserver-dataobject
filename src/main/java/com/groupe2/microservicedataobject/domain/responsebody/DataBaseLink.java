package com.groupe2.microservicedataobject.domain.responsebody;

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
