package com.example.intuitech_hazi.repository;

import com.example.intuitech_hazi.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {


    boolean existsByEmail(String email);

   Optional<Client> findByApiKey(String id);
}
