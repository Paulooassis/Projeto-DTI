package com.dti.demo.repository;

import com.dti.demo.entities.DroneEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DroneRepository extends JpaRepository<DroneEntity, Integer> {
    List<DroneEntity> findByDisponivelTrue();
}
