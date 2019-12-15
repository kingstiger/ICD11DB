package piotr.api.Controller;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import piotr.DTOs.ICD11FullResponse;
import piotr.DTOs.ICD11TreeView;
import piotr.api.Service.ICD11Service;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/ICD", produces = {"application/json"})
public class ICD11Controller {

    @Autowired
    private ICD11Service service;

    @GetMapping(value = "/test")
    public ResponseEntity<Boolean> checkIfWorks() {
        return new ResponseEntity<>(
                true,
                new HttpHeaders(),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/wholeDb")
    public ResponseEntity<List<ICD11FullResponse>> wholeDb() {
        return new ResponseEntity<>(
                service.getAll(),
                new HttpHeaders(),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/wholeDbOWL")
    public ResponseEntity<String> wholeDbOWL() throws Exception {
        return new ResponseEntity<>(
                service.getAllOWL(),
                new HttpHeaders(),
                HttpStatus.OK
        );
    }


    @GetMapping(value = "")
    public ResponseEntity<ICD11TreeView> getAllICDByNameOrId(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String ICD11
            ) throws Exception {

        return new ResponseEntity<>(
                service.getAllByNameOrICD11(name, ICD11),
                new HttpHeaders(),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/fuzzy")
    public ResponseEntity<ICD11TreeView> getAllICDByNameFuzzy(
            @RequestParam(required = false) String name
    ) {

        return new ResponseEntity<>(
                service.getAllByNameFuzzy(name, null),
                new HttpHeaders(),
                HttpStatus.OK
        );
    }
}
