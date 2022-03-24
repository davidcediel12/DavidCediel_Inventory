package com.test.inventory.repositories;

import com.test.inventory.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    Optional<Client> findByIdentification(String identification);
    boolean existsByIdentification(String identification);
    void deleteByIdentification(String identification);
}
