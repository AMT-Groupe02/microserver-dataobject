package com.groupe2.microservicedataobject.dataobject;


public class PathContainsOtherObjectsException extends RuntimeException {
    public PathContainsOtherObjectsException() {
        super("This path contains other objects, do a recursive delete to fix it.");
    }
}