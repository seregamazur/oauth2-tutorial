package com.seregamazur.oauth2.tutorial.crud;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    // Add custom query methods if needed
}
