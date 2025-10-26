package com.micros.msuser.controllers;

import com.micros.msuser.model.User;
import com.micros.msuser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST Controller that mimics GraphQL structure
 * Provides REST endpoints with GraphQL-like paths
 */
@RestController
@RequestMapping(path="/graphql")
public class GraphQLRestController {

    @Autowired
    private UserRepository userRepository;

    // GET /graphql/users - Get all users
    @GetMapping("/users")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        List<Map<String, Object>> users = StreamSupport.stream(userRepository.findAll().spliterator(), false)
            .map(this::userToMap)
            .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    // GET /graphql/users/{id} - Get user by ID
    @GetMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(userToMap(user.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /graphql/add - Create new user
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addUser(@RequestBody Map<String, String> userInput) {
        User user = new User();
        user.setNom(userInput.get("nom"));
        user.setPrenom(userInput.get("prenom"));
        user.setEmail(userInput.get("email"));
        user.setPassword(userInput.get("password"));
        
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(userToMap(savedUser));
    }

    // PUT /graphql/users/{id} - Update user
    @PutMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long id, @RequestBody Map<String, String> userInput) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (userInput.get("nom") != null) user.setNom(userInput.get("nom"));
            if (userInput.get("prenom") != null) user.setPrenom(userInput.get("prenom"));
            if (userInput.get("email") != null) user.setEmail(userInput.get("email"));
            if (userInput.get("password") != null) user.setPassword(userInput.get("password"));
            
            User savedUser = userRepository.save(user);
            return ResponseEntity.ok(userToMap(savedUser));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /graphql/users/{id} - Delete user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            Map<String, Boolean> response = new HashMap<>();
            response.put("success", true);
            return ResponseEntity.ok(response);
        } else {
            Map<String, Boolean> response = new HashMap<>();
            response.put("success", false);
            return ResponseEntity.ok(response);
        }
    }

    // Helper method to convert User entity to Map
    private Map<String, Object> userToMap(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getId().toString());
        map.put("nom", user.getNom());
        map.put("prenom", user.getPrenom());
        map.put("email", user.getEmail());
        // Ne pas retourner le password pour la sécurité
        return map;
    }
}