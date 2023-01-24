package com.groupe2.microservicedataobject.dataobject;

// le nom de la class devrait finir par Exception
// TODO La structure de votre microservice manque de cohérance, vous devriez avoir un endroit pour les exceptions séparément de vos classes de service
public class DataObjectAlreadyExists extends RuntimeException {
    public DataObjectAlreadyExists() {
        super("File already exists");
    }
}