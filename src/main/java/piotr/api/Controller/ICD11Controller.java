package piotr.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import piotr.DTOs.ICD11FullResponse;
import piotr.api.Service.ICD11Service;

import java.util.List;

@RestController
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

    @GetMapping(value = "")
    public ResponseEntity<List<ICD11FullResponse>> getAllICDByNameOrId(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String ICD,
            @PageableDefault(page = 0, size = 100) Pageable pageable
            ) {

        return new ResponseEntity<>(
                service.getAllByNameOrICD11(name, ICD, pageable),
                new HttpHeaders(),
                HttpStatus.OK
        );
    }
}
