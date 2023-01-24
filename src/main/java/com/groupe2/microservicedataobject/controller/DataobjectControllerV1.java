package com.groupe2.microservicedataobject.controller;

import com.groupe2.microservicedataobject.dataobject.DataObjectNotFoundException;
import com.groupe2.microservicedataobject.dataobject.aws.AwsDataObject;
import com.groupe2.microservicedataobject.domain.requestbody.Base64Data;
import com.groupe2.microservicedataobject.domain.responsebody.DataBaseLink;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
// (pas pénalisé) pourquoi il s'appelle V1 et le LabelDetector s'appelle pas V1 ?
public class DataobjectControllerV1 {

        // TODO collection of resource path should be plural
        @PostMapping("/object")
        public DataBaseLink postDataobject(@RequestBody Base64Data data) {
            AwsDataObject awsDataObject = new AwsDataObject(data.getPath());
            awsDataObject.upload(data.getData());
            // TODO ici on devrait retourner un 201 created !
            return new DataBaseLink(awsDataObject.getUrl());
        }

        // TODO collection of resource path should be plural
        @DeleteMapping("/object")
        public void deleteDataobject(@RequestParam String path) {
            AwsDataObject awsDataObject = new AwsDataObject(path);
            awsDataObject.delete();

            // honêtement ici je ne sais pas comment il va se comporter. 
            // en temps normal je retourne toujours un ResponseEntity 204 No Content mais je ne sais pas si c'est le cas ici, je pars du principe que il retourne un 200 (sans contenu mais bon...) et je vous pénalise pas mais ma solution est définitivement plus courante que la votre !
            // https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/DELETE
        }

        // TODO collection of resource path should be plural
        @GetMapping("/object")
        public DataBaseLink getDataobject(@RequestParam String path) {
            AwsDataObject awsDataObject = new AwsDataObject(path);

        // (pas pénalisé) mais vous devriez retourner des ResponseEntity et non des objets c'est plus flexible et plus standard
            return new DataBaseLink(awsDataObject.getUrl());
        }

        @ExceptionHandler()
        public ResponseEntity<String> handleDataObjectNotFoundException(
                DataObjectNotFoundException exception
        ) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(exception.getMessage());
        }
}