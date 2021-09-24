package com.TestProject.JuiceStock.repository;

import com.TestProject.JuiceStock.entity.Juice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JuiceRepository extends JpaRepository<Juice, Long> {

    Optional<Juice> findByName(String name);
}
