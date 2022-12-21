package com.groupe2.microservicedataobject.dataobject;

public class URLRequestErrorException extends RuntimeException {
    public URLRequestErrorException() {
        super("Could not access the URL");
    }
}
