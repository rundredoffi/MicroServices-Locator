package com.micros.msuser.graphql;

import com.micros.msuser.model.User;
import com.micros.msuser.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;

@GraphQlTest(UserGraphQLController.class)
public class UserGraphQLControllerTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldGetEmptyUsersList() {
        // Test query for all users
        this.graphQlTester
            .document("{ users { id nom prenom email } }")
            .execute()
            .path("users")
            .entityList(User.class)
            .hasSize(0);
    }

    @Test
    void shouldCreateUser() {
        // Test mutation to create a user
        this.graphQlTester
            .document("""
                mutation createUser($input: CreateUserInput!) {
                    createUser(input: $input) {
                        id
                        nom
                        prenom
                        email
                    }
                }
                """)
            .variable("input", new UserGraphQLController.CreateUserInput("Dupont", "Jean", "jean@example.com", "password"))
            .execute()
            .path("createUser.nom")
            .entity(String.class)
            .isEqualTo("Dupont");
    }
}