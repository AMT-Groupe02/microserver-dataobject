package com.groupe2.microservicedataobject.dataobject;

public class DataObjectNotFoundException extends RuntimeException {
    public DataObjectNotFoundException() {
        super("File not found");
    }
}
