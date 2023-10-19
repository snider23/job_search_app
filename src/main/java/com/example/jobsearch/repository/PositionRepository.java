package com.example.jobsearch.repository;

import com.example.jobsearch.domain.Position;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PositionRepository extends JpaRepository<Position,Long> {

    @Query("SELECT p FROM Position p WHERE p.id = :id")
    Position findPositionById(@Param("id") Long id);


    @Query("SELECT p FROM Position p WHERE CONCAT(' ', p.title, ' ') LIKE %:jobTitle% AND CONCAT(' ', p.location, ' ') LIKE %:jobLocation%")
    List<Position> findPositionsBy(@Param("jobTitle") String jobTitle, @Param("jobLocation") String jobLocation);
}
