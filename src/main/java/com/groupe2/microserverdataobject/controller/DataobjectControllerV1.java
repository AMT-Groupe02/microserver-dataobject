package com.groupe2.microserverdataobject.controller;

import com.groupe2.microserverdataobject.dataobject.DataObjectNotFoundException;
import com.groupe2.microserverdataobject.dataobject.aws.AwsDataObject;
import com.groupe2.microserverdataobject.domain.requestbody.Base64Data;
import com.groupe2.microserverdataobject.domain.responsebody.DataBaseLink;
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

        @DeleteMapping("/object/{path}")
        public void deleteDataobject(@PathVariable(value="path") String path) {
            path = path.replace("@", "/");
            AwsDataObject awsDataObject = new AwsDataObject(path);
            awsDataObject.delete();
        }

        @GetMapping("/object/{path}")
        public DataBaseLink getDataobject(@PathVariable(value="path") String path) {
            path = path.replace("@", "/");
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