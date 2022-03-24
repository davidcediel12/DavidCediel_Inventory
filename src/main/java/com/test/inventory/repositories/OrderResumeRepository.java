package com.test.inventory.repositories;

import com.test.inventory.entities.OrderResume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderResumeRepository extends JpaRepository<OrderResume, Integer> {
}
