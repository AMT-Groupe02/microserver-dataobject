package com.groupe2.microservicedataobject.dataobject;

public class DataObjectAlreadyExists extends RuntimeException {
    public DataObjectAlreadyExists() {
        super("File already exists");
    }
}