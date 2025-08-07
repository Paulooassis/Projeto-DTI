package com.dti.demo.repository;

import com.dti.demo.entities.VooEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VooRepository extends JpaRepository<VooEntity, Integer> {}