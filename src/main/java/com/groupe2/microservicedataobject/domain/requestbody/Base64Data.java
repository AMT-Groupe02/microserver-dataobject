package com.groupe2.microservicedataobject.domain.requestbody;


// (pour info) regardez lombok et spécifiquement @Data, @Getter et @Setter ça va vous économiser le clavier
public class Base64Data {
    private String data;
    private String path;

    public Base64Data() {
    }

    public String getPath() {
        return path;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return data;
    }
}
