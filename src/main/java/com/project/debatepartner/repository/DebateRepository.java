package com.project.debatepartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.project.debatepartner.model.Debate;
import java.util.List;

@Repository
public interface DebateRepository extends JpaRepository<Debate, Integer> {

    List<Debate> findByUsername(String username);

}