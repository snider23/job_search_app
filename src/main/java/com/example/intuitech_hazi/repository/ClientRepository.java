package com.example.intuitech_hazi.repository;

import com.example.intuitech_hazi.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {


    boolean existsByEmail(String email);
}
