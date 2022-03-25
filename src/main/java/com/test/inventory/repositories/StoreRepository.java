package com.test.inventory.repositories;

import com.test.inventory.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, CriteriaBuilder.In> {
    Optional<Store> findByCode(String code);
}
