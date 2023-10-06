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

    //todo query

    @Query(value = "SELECT * FROM Positions p WHERE p.name LIKE %:title% OR p.location LIKE %:location%", nativeQuery = true)
    List<Position> findPositionsBy(@Param("title") String jobTitle, @Param("location") String jobLocation);
}
