package com.renuox.mgmt.bills.controller;

import com.renuox.mgmt.bills.model.Person;
import com.renuox.mgmt.bills.service.impl.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "${cors.allowedOrigins}")
@RestController
@RequestMapping("/parsons")
public class PersonController {

    @Autowired
    PersonService personService;

    @GetMapping("/list")
    public List<Person> listPerson() {
        return personService.findAll();
    }

    @PostMapping
    public Person savePerson(@RequestBody Person person) {
        return personService.save(person);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> listPersonById(@PathVariable Long id) {
        return ResponseEntity.ok(personService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePersonById(@PathVariable Long id, @RequestBody Person personRequest) {
        return ResponseEntity.ok(personService.update(id, personRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deletePersonById(@PathVariable Long id) {
        personService.delete(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
