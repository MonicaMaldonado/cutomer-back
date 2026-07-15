package org.sotobotero.customer.controller;

import org.sotobotero.customer.entities.Customer;
import org.sotobotero.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/api/v1/customer")
@Tag(name = "customer API", description = "customer API")
@CrossOrigin(origins = "*")
public class CustomerController {
    
    public CustomerController() {
        super();
    }
    
    @Autowired
    private CustomerRepository customerRepository; // Cambiado de prsRepository a customerRepository

    @Value("${db.password}")
    private String password;

    @Operation(summary = "Test property")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the property"),
            @ApiResponse(responseCode = "404", description = "Property not found"),
    })
    @GetMapping("/testproperty")
    public ResponseEntity<String> getTestValue() {    
        return new ResponseEntity<>(password, HttpStatus.OK);
    }

    @Operation(summary = "Get all customers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the customers"),
            @ApiResponse(responseCode = "404", description = "Not found the customers"),
    })
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() { // Corregido camelCase
        List<Customer> customers = customerRepository.findAll();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @Operation(summary = "Get a customer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the customer"),
            @ApiResponse(responseCode = "404", description = "Not found the customer"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("id") Long id) {
    // Corregido: Convertimos el Long 'id' a String usando String.valueOf()
    Optional<Customer> customer = customerRepository.findById(String.valueOf(id)); 
    
    if (customer.isPresent()) {
        return new ResponseEntity<>(customer.get(), HttpStatus.OK);
    } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

    @Operation(summary = "Create a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the customer"), // El código HTTP correcto para creación es 201 (Created)
            @ApiResponse(responseCode = "400", description = "Invalid input"),
    })  
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) { // Corregido camelCase
        Customer newCustomer = customerRepository.save(customer);
        return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
    }
    

    @Operation(summary = "Update a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update the customer"),
            @ApiResponse(responseCode = "404", description = "Not found the customer to update"),
    })
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer) { // Corregido camelCase
        Optional<Customer> optionalCustomer = customerRepository.findById(String.valueOf(customer.getId()));// Eliminado .toString()
        if (optionalCustomer.isPresent()) {
            Customer existingCustomer = optionalCustomer.get();
            
            // Mapeando campos actualizados
            existingCustomer.setName(customer.getName());
            existingCustomer.setEmail(customer.getEmail());
            existingCustomer.setPhone(customer.getPhone());
            existingCustomer.setAddress(customer.getAddress());
            existingCustomer.setCity(customer.getCity());
            existingCustomer.setState(customer.getState());
            existingCustomer.setCountry(customer.getCountry());
            existingCustomer.setZip(customer.getZip());
            existingCustomer.setCompany(customer.getCompany());
            existingCustomer.setPosition(customer.getPosition());
            existingCustomer.setWebsite(customer.getWebsite());
            existingCustomer.setTwitter(customer.getTwitter());
            existingCustomer.setFacebook(customer.getFacebook());
            existingCustomer.setLinkedin(customer.getLinkedin());
            existingCustomer.setGithub(customer.getGithub());
            existingCustomer.setInstagram(customer.getInstagram());
            existingCustomer.setYoutube(customer.getYoutube());
            existingCustomer.setTiktok(customer.getTiktok());
            existingCustomer.setSnapchat(customer.getSnapchat());
            existingCustomer.setTwitch(customer.getTwitch());
            existingCustomer.setOther(customer.getOther());
            existingCustomer.setNotes(customer.getNotes());
            existingCustomer.setAge(customer.getAge());
            
            Customer updatedCustomer = customerRepository.save(existingCustomer);
            return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Update a customer name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update the customer name"),
            @ApiResponse(responseCode = "404", description = "Not update the customer name"),
    })
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCustomerName(
            @RequestParam("name") String name, // ¡CORREGIDO! Ahora Spring sabe que viene como Query Parameter
            @PathVariable("id") Long id) {
        
        Optional<Customer> optionalCustomer = customerRepository.findById(String.valueOf(id)); // Eliminado .toString()
        if (optionalCustomer.isPresent()) {
            Customer existingCustomer = optionalCustomer.get();
            existingCustomer.setName(name);
            Customer updatedCustomer = customerRepository.save(existingCustomer);
            return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted the customer"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable("id") Long id) { // Corregido camelCase
        Optional<Customer> optionalCustomer = customerRepository.findById(String.valueOf(id)); // Eliminado .toString()
        if (optionalCustomer.isPresent()) {
            customerRepository.delete(optionalCustomer.get());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }  
}

