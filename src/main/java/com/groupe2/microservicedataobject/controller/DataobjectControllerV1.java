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
public class DataobjectControllerV1 {

        @PostMapping("/object")
        public DataBaseLink postDataobject(@RequestBody Base64Data data) {
            AwsDataObject awsDataObject = new AwsDataObject(data.getPath());
            awsDataObject.upload(data.getData());
            return new DataBaseLink(awsDataObject.getUrl());
        }

        @DeleteMapping("/object")
        public void deleteDataobject(@RequestParam String path) {
            AwsDataObject awsDataObject = new AwsDataObject(path);
            awsDataObject.delete();
        }

        @GetMapping("/object")
        public DataBaseLink getDataobject(@RequestParam String path) {
            AwsDataObject awsDataObject = new AwsDataObject(path);
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