package com.example.intuitech_hazi.service;

import com.example.intuitech_hazi.domain.Client;
import com.example.intuitech_hazi.dto.ClientListItem;
import com.example.intuitech_hazi.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClientService {

    private ClientRepository clientRepository;



}
