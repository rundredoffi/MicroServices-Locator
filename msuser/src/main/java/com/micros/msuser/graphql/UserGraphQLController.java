package com.micros.msuser.graphql;

import com.micros.msuser.model.User;
import com.micros.msuser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class UserGraphQLController {

    @Autowired
    private UserRepository userRepository;

    @QueryMapping
    public List<User> users() {
        return (List<User>) userRepository.findAll();
    }

    @QueryMapping
    public User user(@Argument Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    @MutationMapping
    public User createUser(@Argument CreateUserInput input) {
        User user = new User();
        user.setNom(input.getNom());
        user.setPrenom(input.getPrenom());
        user.setEmail(input.getEmail());
        user.setPassword(input.getPassword());
        return userRepository.save(user);
    }

    @MutationMapping
    public User updateUser(@Argument Long id, @Argument UpdateUserInput input) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (input.getNom() != null) user.setNom(input.getNom());
            if (input.getPrenom() != null) user.setPrenom(input.getPrenom());
            if (input.getEmail() != null) user.setEmail(input.getEmail());
            if (input.getPassword() != null) user.setPassword(input.getPassword());
            return userRepository.save(user);
        }
        return null;
    }

    @MutationMapping
    public Boolean deleteUser(@Argument Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Input classes for GraphQL
    public static class CreateUserInput {
        private String nom;
        private String prenom;
        private String email;
        private String password;

        public CreateUserInput() {}

        public CreateUserInput(String nom, String prenom, String email, String password) {
            this.nom = nom;
            this.prenom = prenom;
            this.email = email;
            this.password = password;
        }

        public String getNom() { return nom; }
        public void setNom(String nom) { this.nom = nom; }

        public String getPrenom() { return prenom; }
        public void setPrenom(String prenom) { this.prenom = prenom; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class UpdateUserInput {
        private String nom;
        private String prenom;
        private String email;
        private String password;

        public UpdateUserInput() {}

        public String getNom() { return nom; }
        public void setNom(String nom) { this.nom = nom; }

        public String getPrenom() { return prenom; }
        public void setPrenom(String prenom) { this.prenom = prenom; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}