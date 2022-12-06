package com.groupe2.microserverdataobject.domain;

public class DataBaseLink {
    private String url;
    public DataBaseLink(String url){
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
