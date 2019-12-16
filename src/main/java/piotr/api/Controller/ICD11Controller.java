package piotr.api.Controller;

import org.apache.commons.io.IOUtils;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpProperties;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import piotr.DTOs.ICD11FullResponse;
import piotr.DTOs.ICD11TreeView;
import piotr.api.Service.ICD11Service;

import java.io.ByteArrayInputStream;
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
    public ResponseEntity<Resource> wholeDbOWL() throws Exception {
        byte[] owlFile = service.getAllOWL().toByteArray();
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(owlFile));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ICD-11-ontology.owl");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(owlFile.length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
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
