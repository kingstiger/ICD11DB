package piotr.api.Controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "", produces = {"application/json"})
public class EmptyUrlController {

    private final static String allEndpointsMsg = "Possible endpoints (with requests):\n" +
            "GET\n" +
            "[base address]/api/ICD?name={name}&ICD11={ICD11 code}\n" +
            "   returns JSON-formatted data about disease with given name and/or ICD11 code\n" +
            "[base address]/api/ICD/test\n" +
            "   returns true, if api is working/available\n" +
            "[base address]/api/ICD/wholeDb\n" +
            "   returns all data from database in JSON format\n" +
            "[base address]/api/ICD/fuzzy?name={name}\n" +
            "   returns JSON-formatted data about disease with given name after fuzzy search in DB";

    @GetMapping(value = "")
    public ResponseEntity<String> emptyURL() {
        return new ResponseEntity<>(
                allEndpointsMsg,
                new HttpHeaders(),
                HttpStatus.OK
        );
    }
}