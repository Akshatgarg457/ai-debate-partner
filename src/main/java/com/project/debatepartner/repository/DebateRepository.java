package com.project.debatepartner.repository;

import com.project.debatepartner.model.Debate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DebateRepository
        extends JpaRepository<Debate, Integer> {
}