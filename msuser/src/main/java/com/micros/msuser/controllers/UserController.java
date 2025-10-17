package com.micros.msuser.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.micros.msuser.model.User;
import com.micros.msuser.dto.AddUserRequest;
import com.micros.msuser.repository.UserRepository;

import java.util.Optional;

@Controller
@RequestMapping(path="/users")
public class UserController {
  @Autowired
  private UserRepository userRepository;

  @PostMapping(path="/add")
  public @ResponseBody String addNewUser (@RequestBody AddUserRequest request) {

    User n = new User();
    n.setNom(request.getNom());
    n.setPrenom(request.getPrenom());
    n.setEmail(request.getEmail());
    n.setPassword(request.getPassword());
    userRepository.save(n);
    return "Saved";
  }

  @GetMapping(path="/{id}")
  public ResponseEntity<User> getUserById(@PathVariable Long id) {
    Optional<User> user = userRepository.findById(id);
    if (user.isPresent()) {
      return ResponseEntity.ok(user.get());
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping(path="/all")
  public @ResponseBody Iterable<User> getAllUsers() {
    return userRepository.findAll();
  }
}
