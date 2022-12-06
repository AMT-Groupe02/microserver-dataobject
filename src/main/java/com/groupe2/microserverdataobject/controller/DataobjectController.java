package com.groupe2.microserverdataobject.controller;

import com.groupe2.microserverdataobject.domain.DataBaseLink;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class DataobjectController {

        @PostMapping("/helloworld")
        public DataBaseLink getDataobject(@RequestBody DataBaseLink url) {
            return url;
        }
}