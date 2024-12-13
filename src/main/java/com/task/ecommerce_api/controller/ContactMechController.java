package com.task.ecommerce_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.task.ecommerce_api.entity.ContactMech;
import com.task.ecommerce_api.entity.Customer;
import com.task.ecommerce_api.repository.ContactMechRepository;
import com.task.ecommerce_api.repository.CustomerRepository;

import java.util.List;

@RestController
@RequestMapping("/contact-mechs")
public class ContactMechController {

    @Autowired
    private ContactMechRepository contactMechRepository;

    @Autowired
    private CustomerRepository customerRepository;

    // Create Contact Mechanism
    @PostMapping("/{customerId}")
    public ResponseEntity<ContactMech> createContactMech(@PathVariable int customerId, @RequestBody ContactMech contactMech) {
        try {
            // Find customer by ID to associate with the contact mechanism
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found with id " + customerId));

            // Set the customer for the contact mechanism
            contactMech.setCustomer(customer);
            ContactMech savedContactMech = contactMechRepository.save(contactMech);
            return new ResponseEntity<>(savedContactMech, HttpStatus.CREATED);
        } catch (Exception e) {
            // Handle any exceptions or database errors
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get All Contact Mechanisms
    @GetMapping
    public ResponseEntity<List<ContactMech>> getAllContactMechs() {
        try {
            List<ContactMech> contactMechs = contactMechRepository.findAll();
            if (contactMechs.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // No data found
            }
            return new ResponseEntity<>(contactMechs, HttpStatus.OK);
        } catch (Exception e) {
            // Handle any exceptions or database errors
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get Contact Mechanism by ID
    @GetMapping("/{contactMechId}")
    public ResponseEntity<ContactMech> getContactMech(@PathVariable int contactMechId) {
        try {
            return contactMechRepository.findById(contactMechId)
                    .map(contactMech -> new ResponseEntity<>(contactMech, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            // Handle any exceptions or database errors
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update Contact Mechanism
    @PutMapping("/{contactMechId}")
    public ResponseEntity<ContactMech> updateContactMech(@PathVariable int contactMechId, @RequestBody ContactMech contactMechDetails) {
        try {
            return contactMechRepository.findById(contactMechId).map(contactMech -> {
                // Update the fields of the existing contact mechanism
                contactMech.setStreetAddress(contactMechDetails.getStreetAddress());
                contactMech.setCity(contactMechDetails.getCity());
                contactMech.setState(contactMechDetails.getState());
                contactMech.setPostalCode(contactMechDetails.getPostalCode());
                contactMech.setPhoneNumber(contactMechDetails.getPhoneNumber());
                contactMech.setEmail(contactMechDetails.getEmail());
                return new ResponseEntity<>(contactMechRepository.save(contactMech), HttpStatus.OK);
            }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            // Handle any exceptions or database errors
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete Contact Mechanism
    @DeleteMapping("/{contactMechId}")
    public ResponseEntity<Void> deleteContactMech(@PathVariable int contactMechId) {
        try {
            return contactMechRepository.findById(contactMechId).map(contactMech -> {
                contactMechRepository.delete(contactMech);
                return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
            }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            // Handle any exceptions or database errors
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
