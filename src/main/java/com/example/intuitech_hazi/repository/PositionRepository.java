package com.example.intuitech_hazi.repository;

import com.example.intuitech_hazi.domain.Position;
import com.example.intuitech_hazi.dto.outgoing.PositionListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.lang.reflect.Array;
import java.util.List;

public interface PositionRepository extends JpaRepository<Position,Long> {

    @Query("SELECT p FROM Position p WHERE p.id = :id")
    Position findPositionById(@Param("id") Long id);

    //todo KÉRDÉS: érdmes modositani, mert most ha egy szóban van egy betű akkor azzal a betűvel az összes JobTitle-t megjeleniti

    @Query("SELECT p FROM Position p WHERE p.title LIKE %:jobTitle% OR p.location LIKE %:jobLocation%")
    List<Position> findPositionsBy(@Param("jobTitle") String jobTitle, @Param("jobLocation") String jobLocation);
}
